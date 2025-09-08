package com.example.messaging.consumer;

import com.example.contracts.Event;
import com.example.contracts.EventPayload;
import com.example.contracts.EventType.Topic;
import com.example.contracts.payload.OrderCreatedEventPayload;
import com.example.domain.entity.User;
import com.example.domain.repository.UserRepository;
import com.example.external.mail.EmailService;
import com.example.external.mail.MailSendDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationConsumer {

	private final EmailService emailService;
	private final UserRepository userRepository;

	@Transactional
	@KafkaListener(topics = Topic.ORDER, groupId = "email-consumer")
	public void onMessage(ConsumerRecord<String, String> rec) throws Exception {
		Event<EventPayload> payload = Event.fromJson(rec.value());

		OrderCreatedEventPayload eventPayload = (OrderCreatedEventPayload) payload.getPayload();

		long userId = eventPayload.getUserId();
		User user = userRepository.findById(userId).orElse(null);
		if (user == null) {
			log.warn("Skip email: user not found. eventId={}, userId={}, orderId={}",
				payload.getEventId(), userId, eventPayload.getOrderId());
			return;
		}

		String to = user.getEmail();
		String from = "bosinsa";
		String target = user.getNickname();
		String subject = "bosinsa 결제 완료 알림";
		String content = target + "님 안녕하세요. \n주문해 주셔서 감사합니다. \n"
			+ "주문번호: " + eventPayload.getOrderId()
			+ "\n총 금액: " + eventPayload.getAmount()
			+ "\n결제 완료 시간: " + eventPayload.getUpdatedAt();

		MailSendDto dto = MailSendDto.of(
			from,
			to,
			subject,
			content
		);

		emailService.sendEmail(dto);

	}
}
