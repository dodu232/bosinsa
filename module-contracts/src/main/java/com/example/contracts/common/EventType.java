package com.example.contracts.common;

import com.example.contracts.payload.OrderCancelledEventPayload;
import com.example.contracts.payload.OrderCreatedEventPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
	ORDER_CREATED(OrderCreatedEventPayload.class, Topic.ORDER),
	ORDER_CANCELED(OrderCancelledEventPayload.class, Topic.ORDER);

	private final Class<? extends EventPayload> payloadClass;
	private final String topic;

	public static EventType from(String type) {
		try {
			return valueOf(type);
		} catch (Exception e) {
			return null;
		}
	}

	public static class Topic {

		public static final String ORDER = "order";
	}
}
