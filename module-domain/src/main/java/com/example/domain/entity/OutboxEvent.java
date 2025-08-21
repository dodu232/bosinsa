package com.example.domain.entity;

import com.example.domain.enums.OutBoxStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent extends BaseTime {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String eventType;

	@Lob
	@Column(nullable = false)
	private String payload;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private OutBoxStatus status = OutBoxStatus.PENDING;

	public static OutboxEvent of(String eventType, String payloadJson) {
		OutboxEvent e = new OutboxEvent();
		e.eventType = eventType;
		e.payload = payloadJson;
		e.status = OutBoxStatus.PENDING;
		return e;
	}
}
