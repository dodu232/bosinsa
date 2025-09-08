package com.example.messaging.consumer;

import com.example.common.exception.ApiException;
import com.example.common.exception.ErrorType;
import com.example.contracts.Event;
import com.example.contracts.EventPayload;
import com.example.contracts.EventType.Topic;
import com.example.contracts.payload.OrderCreatedEventPayload;
import com.example.domain.entity.User;
import com.example.domain.repository.UserRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PointGrantConsumer {

	private final UserRepository userRepository;

	@Transactional
	@KafkaListener(topics = Topic.ORDER, groupId = "point-consumer")
	public void onMessage(ConsumerRecord<String, String> rec) throws Exception {
		Event<EventPayload> payload = Event.fromJson(rec.value());

		OrderCreatedEventPayload eventPayload = (OrderCreatedEventPayload) payload.getPayload();

		BigDecimal point = eventPayload.getAmount().multiply(BigDecimal.valueOf(0.1));

		long userId = eventPayload.getUserId();

		User user = userRepository.findById(userId).orElseThrow(
			() -> new ApiException("존재하지 않는 유저입니다.", ErrorType.INVALID_PARAMETER));

		user.updatePoint(point);
	}
}
