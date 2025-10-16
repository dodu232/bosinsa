package com.example.messaging.consumer;

import com.example.contracts.common.Event;
import com.example.contracts.common.EventPayload;
import com.example.contracts.common.EventType.Topic;
import com.example.contracts.payload.OrderCreatedEventPayload;
import com.example.domain.service.UserPointService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PointGrantConsumer {

	private final UserPointService pointService;
	private final StringRedisTemplate redisTemplate;

	@Transactional
	@KafkaListener(topics = Topic.ORDER, groupId = "order-point-group")
	@RetryableTopic(attempts = "5", backoff = @Backoff(delay = 1000, multiplier = 2), dltTopicSuffix = ".dlt")
	public void onMessage(ConsumerRecord<String, String> rec) {
		Event<EventPayload> payload = Event.fromJson(rec.value());

		if (payload == null) {
			log.warn("Skip deserialize: {}", rec.value());
			return;
		}

		Long eventId = payload.getEventId();
		String redisKey = "idemp:point:" + eventId;

		Boolean firstSeen = redisTemplate
			.opsForValue()
			.setIfAbsent(redisKey, "1", Duration.ofHours(24));
		if (Boolean.FALSE.equals(firstSeen)) {
			log.info("Skip duplicate by Redis. eventId={}", eventId);
			return;
		}

		OrderCreatedEventPayload eventPayload = (OrderCreatedEventPayload) payload.getPayload();
		long userId = eventPayload.getUserId();

		pointService.grantPoints(eventId, userId, eventPayload.getAmount());

	}
}
