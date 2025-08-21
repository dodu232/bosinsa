package com.example.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "order_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;

	@JoinColumn(name = "product_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private BigDecimal price;

	public static OrderItem of(Order order, Product product, int quantity, BigDecimal price) {
		OrderItem orderItem = new OrderItem();
		orderItem.order = order;
		orderItem.product = product;
		orderItem.quantity = quantity;
		orderItem.price = price;
		return orderItem;
	}

	public void setOrder(Order order) {
		if (this.order == order) {
			return;
		}
		if (this.order != null) {
			this.order.getItems().remove(this);
		}
		this.order = order;
		if (order != null && !order.getItems().contains(this)) {
			order.getItems().add(this);
		}
	}
}
