package com.example.domain.repository;

import com.example.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
		    UPDATE Product p
		    SET p.stock = p.stock - :quantity
		    WHERE p.id = :productId
		      AND p.stock >= :quantity
		""")
	int decreaseStockIfEnough(@Param("productId") Long productId,
		@Param("quantity") int quantity);
}
