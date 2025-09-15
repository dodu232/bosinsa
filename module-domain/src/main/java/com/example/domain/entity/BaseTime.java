package com.example.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {

	@CreatedDate
	@Column(updatable = false, columnDefinition = "timestamp(6)")
	private Instant createdAt;

	@LastModifiedDate
	@Column(nullable = false, columnDefinition = "timestamp(6)")
	private Instant updatedAt;
}
