package com.example.messaging.consumer;

import com.example.contracts.common.Event;
import com.example.contracts.common.EventPayload;
import com.example.contracts.common.EventType.Topic;
import com.example.contracts.payload.OrderCreatedEventPayload;
import com.example.external.mail.MailSendService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSendConsumer {

	private final MailSendService mailSendService;
	private final StringRedisTemplate redisTemplate;

	@KafkaListener(topics = Topic.ORDER, groupId = "order-email-group")
	@RetryableTopic(attempts = "5", backoff = @Backoff(delay = 1000, multiplier = 2), dltTopicSuffix = ".dlt")
	public void onMessage(ConsumerRecord<String, String> rec) {
		Event<EventPayload> payload = Event.fromJson(rec.value());

		if (payload == null) {
			log.warn("Skip deserialize: {}", rec.value());
			return;
		}

		Long eventId = payload.getEventId();
		String redisKey = "idemp:mail:" + eventId;

		Boolean firstSeen = redisTemplate
			.opsForValue()
			.setIfAbsent(redisKey, "1", Duration.ofHours(24));
		if (Boolean.FALSE.equals(firstSeen)) {
			log.info("Skip duplicate by Redis. eventId={}", eventId);
			return;
		}

		OrderCreatedEventPayload eventPayload = (OrderCreatedEventPayload) payload.getPayload();

		mailSendService.sendOrderPaidEmail(eventId, eventPayload.getUserId(), eventPayload);

	}
}
