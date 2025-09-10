package com.example.domain.repository;

import com.example.domain.entity.Order;
import com.example.domain.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("""
		  select count(o) from Order o
		  where o.createdAt >= :from and o.createdAt < :to
		""")
	long countAllBetween(LocalDateTime from, LocalDateTime to);

	@Query("""
		  select count(o) from Order o
		  where o.createdAt >= :from and o.createdAt < :to
		    and o.status = :status
		""")
	long countByStatusBetween(LocalDateTime from, LocalDateTime to, OrderStatus status);

	@Query("""
		  select coalesce(sum(o.amount), 0)
		  from Order o
		  where o.createdAt >= :from and o.createdAt < :to
		""")
	BigDecimal sumAmountBetween(LocalDateTime from, LocalDateTime to);

}
