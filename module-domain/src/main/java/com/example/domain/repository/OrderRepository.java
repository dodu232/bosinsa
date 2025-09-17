package com.example.domain.repository;

import com.example.domain.entity.Order;
import com.example.domain.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("""
		    select count(*) from Order o
		    where o.createdAt >= :from and o.createdAt < :to
		""")
	long countAllBetween(@Param("from") Instant from,
		@Param("to") Instant to);

	@Query("""
		    select count(*) from Order o
		    where o.updatedAt >= :from and o.updatedAt < :to
		      and o.status = :status
		""")
	long countByStatusBetween(@Param("from") Instant from,
		@Param("to") Instant to,
		@Param("status") OrderStatus status);

	@Query("""
		    select coalesce(sum(o.amount), 0)
		    from Order o
		    where o.updatedAt >= :from and o.updatedAt < :to
			  and o.status = :status
		""")
	BigDecimal sumAmountBetween(@Param("from") Instant from,
		@Param("to") Instant to,
		@Param("status") OrderStatus status);
}