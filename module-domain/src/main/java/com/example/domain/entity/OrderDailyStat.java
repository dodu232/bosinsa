package com.example.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_daily_stats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDailyStat {

	@Id
	@Column(name = "stat_date")
	private LocalDate statDate;

	@Column(nullable = false)
	private Long totalOrders;

	@Column(nullable = false)
	private Long paidOrders;

	@Column(nullable = false)
	private Long canceledOrders;

	@Column(nullable = false)
	private BigDecimal totalAmount;

	@Column(nullable = false)
	private BigDecimal aov;

	@Column(nullable = false)
	private LocalDateTime updatedAt;
}
