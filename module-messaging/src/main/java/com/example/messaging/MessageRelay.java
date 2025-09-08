package com.example.messaging;

import com.example.domain.entity.Outbox;
import com.example.domain.repository.OutBoxRepository;
import com.example.messaging.outbox.MessageRelayCoordinator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageRelay {

	private final OutBoxRepository outBoxRepository;
	private final MessageRelayCoordinator messageRelayCoordinator;
	private final KafkaTemplate<String, String> kafkaTemplate;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void createOutbox(OutboxEvent outboxEvent) {
		log.info("MessageRelay.createOutbox({})", outboxEvent);
		outBoxRepository.save(outboxEvent.getOutbox());
	}

	@Async("messageRelayPublishEventExecutor")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void publishEvent(OutboxEvent outboxEvent) {
		publishEvent(outboxEvent.getOutbox());
	}

	private void publishEvent(Outbox outbox) {
		try {
			RecordMetadata metadata = kafkaTemplate.send(outbox.getEventType().getTopic(),
					String.valueOf(outbox.getShardKey()), outbox.getPayload()).get(1, TimeUnit.SECONDS)
				.getRecordMetadata();

			log.info("Kafka 전송 성공: topic={}, partition={}, offset={}", metadata.topic(),
				metadata.partition(), metadata.offset());

			outBoxRepository.delete(outbox);
		} catch (Exception e) {
			log.error("publishEvent outbox = {}", outbox, e);
		}
	}

	@Scheduled(fixedDelay = 10, initialDelay = 5, timeUnit = TimeUnit.SECONDS, scheduler = "messageRelayPublishPendingEventExecutor")
	public void publishPendingEvent() {
		AssignedShard assignedShard = messageRelayCoordinator.assignedShard();
		log.info("MessageRelay.publishPendingEvent assignedShard size = {}",
			assignedShard.getShards().size());
		for (Long shard : assignedShard.getShards()) {
			List<Outbox> outboxes = outBoxRepository.findAllByShardKeyAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
				shard, LocalDateTime.now().minusSeconds(10), Pageable.ofSize(100));
			for (Outbox outbox : outboxes) {
				publishEvent(outbox);
			}
		}
	}

}
