package com.example.messaging.outbox;

import com.example.contracts.Event;
import com.example.contracts.EventPayload;
import com.example.contracts.EventType;
import com.example.domain.entity.Outbox;
import com.example.messaging.MessageRelayConstants;
import com.example.messaging.OutboxEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public void publish(EventType type, EventPayload payload, Long shardKey) {
		UUID uuid = UUID.randomUUID();

		Outbox outbox = Outbox.create(
			uuid.getMostSignificantBits(),
			type,
			Event.of(
				uuid.getLeastSignificantBits(), type, payload
			).toJson(),
			shardKey % MessageRelayConstants.SHARD_COUNT
		);
		applicationEventPublisher.publishEvent(OutboxEvent.of(outbox));
	}
}
