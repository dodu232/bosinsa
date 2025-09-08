package com.example.domain.entity;

import com.example.contracts.EventType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "outbox")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox {

	@Id
	private Long id;
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	private String payload;
	private Long shardKey;
	private LocalDateTime createdAt;

	public static Outbox create(Long outboxId, EventType eventType, String payload, Long shardKey) {
		Outbox outbox = new Outbox();
		outbox.id = outboxId;
		outbox.eventType = eventType;
		outbox.payload = payload;
		outbox.shardKey = shardKey;
		outbox.createdAt = LocalDateTime.now();
		return outbox;
	}
}
