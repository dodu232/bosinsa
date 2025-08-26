(function () {
  const CLIENT_KEY = "test_ck_P24xLea5zVA09kKb2Gl3QAMYNwW6";
  const tossPayments = TossPayments(CLIENT_KEY);

  const customerKey = "guest-" + Math.random().toString(36).slice(2);
  const payment = tossPayments.payment({customerKey});

  const $btn = document.getElementById("payment-request-button");
  const $orderId = document.getElementById("orderId");

  $btn.addEventListener("click", onClick);

  async function onClick() {
    // 1) 입력값 확인
    const idVal = $orderId?.value?.trim();
    if (!idVal) {
      alert("orderId를 입력하세요.");
      return;
    }

    // 버튼 잠깐 비활성화(중복 클릭 방지)
    $btn.disabled = true;

    try {
      // 2) 주문 조회
      const url = `/api/v1/orders/${encodeURIComponent(idVal)}`;
      const res = await fetch(url, {
        method: "GET",
        headers: {"Accept": "application/json"},
        credentials: "same-origin"
      });

      if (!res.ok) {
        const text = await res.text().catch(() => "");
        throw new Error(`주문 조회 실패 (${res.status}) ${text}`);
      }

      const json = await res.json();
      const data = json?.data ?? json; // {data:{...}} 또는 {...} 모두 대응

      // 서버가 주는 값 우선, 없으면 입력값 사용
      const orderIdForPay = "orderId-" + String(data.orderId ?? idVal);
      const orderName = data.orderName ?? "주문";
      const amount = Number(data.amount ?? 0);
      const email = data.buyerEmail ?? data.email ?? "";

      if (!amount || !Number.isFinite(amount)) {
        throw new Error("결제 금액(amount)이 올바르지 않습니다.");
      }

      // 3) 결제 요청
      await payment.requestPayment({
        method: "CARD",
        amount: {currency: "KRW", value: amount},
        orderId: orderIdForPay,
        orderName: orderName,
        successUrl: window.location.origin + "/api/v1/web/orders/success",
        failUrl: window.location.origin + "/api/v1/web/orders/fail",
        card: {flowMode: "DEFAULT"},
        customerEmail: email,
        customerName: "김토스"
      });

    } catch (e) {
      console.error(e);
      alert(e?.message || "결제 요청 중 오류가 발생했습니다.");
    } finally {
      $btn.disabled = false;
    }
  }
})();