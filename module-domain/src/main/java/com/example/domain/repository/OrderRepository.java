package com.example.domain.repository;

import com.example.domain.entity.Order;
import com.example.domain.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("""
		    select count(o) from Order o
		    where o.createdAt >= :from and o.createdAt < :to
		""")
	long countAllBetween(@Param("from") LocalDateTime from,
		@Param("to") LocalDateTime to);

	@Query("""
		    select count(o) from Order o
		    where o.updatedAt >= :from and o.updatedAt < :to
		      and o.status = :status
		""")
	long countByStatusBetween(@Param("from") LocalDateTime from,
		@Param("to") LocalDateTime to,
		@Param("status") OrderStatus status);

	@Query("""
		    select coalesce(sum(o.amount), 0)
		    from Order o
		    where o.updatedAt >= :from and o.updatedAt < :to
		""")
	BigDecimal sumAmountBetween(@Param("from") LocalDateTime from,
		@Param("to") LocalDateTime to);
}