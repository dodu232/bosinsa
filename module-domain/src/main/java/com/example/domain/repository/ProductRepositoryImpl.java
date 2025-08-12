package com.example.domain.repository;

import com.example.domain.entity.Product;
import com.example.domain.entity.QCategory;
import com.example.domain.entity.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Product> findByCategory(Pageable pageable, String category) {
		QProduct qProduct = new QProduct("product");
		QCategory qCategory = new QCategory("category");

		BooleanBuilder where = new BooleanBuilder();
		if (category != null) {
			where.and(qProduct.category.name.eq(category));
		}

		OrderSpecifier<?> orderSpec = resolveSort(pageable, qProduct);

		List<Product> content = queryFactory
			.selectFrom(qProduct)
			.leftJoin(qProduct.category, qCategory).fetchJoin()
			.where(where)
			.orderBy(orderSpec)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(qProduct.count())
			.from(qProduct)
			.leftJoin(qProduct.category, qCategory)
			.where(where)
			.fetchOne();

		return new PageImpl<>(content, pageable, total == null ? 0 : total);
	}

	private OrderSpecifier<?> resolveSort(Pageable pageable, QProduct qProduct) {
		if (pageable.getSort().isUnsorted()) {
			return new OrderSpecifier<>(Order.DESC, qProduct.createdAt);
		}

		Sort.Order sortOrder = pageable.getSort().iterator().next();
		Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;

		return switch (sortOrder.getProperty()) {
			case "name" -> new OrderSpecifier<>(direction, qProduct.name);
			case "price" -> new OrderSpecifier<>(direction, qProduct.price);
			case "createdAt" -> new OrderSpecifier<>(direction, qProduct.createdAt);
			default -> new OrderSpecifier<>(Order.DESC, qProduct.createdAt);
		};
	}

}
