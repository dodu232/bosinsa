(function () {
  // 결제창/브랜드페이용 클라이언트 키 사용 (test_ck_*)
  const CLIENT_KEY = "test_ck_P24xLea5zVA09kKb2Gl3QAMYNwW6";

  // 전역 SDK 초기화
  const tossPayments = TossPayments(CLIENT_KEY);

  // 구매자 식별용 키 (테스트에선 임시 생성)
  const customerKey = "guest-" + Math.random().toString(36).slice(2);

  // 결제창 객체 생성
  const payment = tossPayments.payment({customerKey});

  document.getElementById("payment-request-button").addEventListener("click",
      async () => {
        try {
          await payment.requestPayment({
            method: "CARD",
            amount: {currency: "KRW", value: 50000},
            orderId: genId(),
            orderName: "토스 티셔츠 외 2건",
            successUrl: window.location.origin + "/sandbox/success",
            failUrl: window.location.origin + "/sandbox/fail",
            // (옵션) 결제창 타입 지정: DEFAULT=호스티드 통합창, DIRECT=개별 앱/지갑창
            card: {
              flowMode: "DEFAULT",
              // flowMode가 DIRECT면 아래처럼 지정 가능
              // easyPay: "TOSSPAY", // 혹은 cardCompany: "HYUNDAI" 등
            },
            customerEmail: "customer123@gmail.com",
            customerName: "김토스",
          });
        } catch (e) {
          console.error(e);
          alert("결제 요청 중 오류가 발생했습니다.");
        }
      });

  function genId() {
    return "order-" + Math.random().toString(36).slice(2, 12);
  }
})();
