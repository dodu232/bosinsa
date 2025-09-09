package com.example.external.mail;

import com.example.contracts.payload.OrderCreatedEventPayload;
import com.example.domain.entity.User;
import com.example.domain.repository.ProcessedEventRepository;
import com.example.domain.repository.UserRepository;
import com.example.external.mail.dto.MailSendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailSendService {

	private final MailService mailService;
	private final UserRepository userRepository;
	private final ProcessedEventRepository processedEventRepository;

	@Transactional
	public void sendOrderPaidEmail(long eventId, long userId, OrderCreatedEventPayload payload) {
		User user = userRepository.findById(userId).orElse(null);

		if (user == null) {
			log.warn("Skip mail: user not found. userId={}", userId);
			return;
		}

		String to = user.getEmail();
		String from = "bosinsa";
		String subject = "bosinsa 결제 완료 알림";
		String content =
			user.getNickname() + "님 안녕하세요.\n주문해 주셔서 감사합니다.\n" + "주문번호: " + payload.getOrderId()
				+ "\n총 금액: " + payload.getAmount() + "\n결제 완료 시간: " + payload.getUpdatedAt();

		MailSendDto dto = MailSendDto.of(from, to, subject, content);

		try {
			processedEventRepository.tryInsert("mail-" + eventId);
		} catch (DataIntegrityViolationException e) {
			log.info("Skip duplicate by DB UNIQUE. eventId={}", eventId);
			return;
		}

		mailService.sendEmail(dto);
	}
}
