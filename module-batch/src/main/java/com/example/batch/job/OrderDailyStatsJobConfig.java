package com.example.batch.job;

import com.example.domain.entity.OrderDailyStat;
import com.example.domain.enums.OrderStatus;
import com.example.domain.repository.OrderDailyStatsRepository;
import com.example.domain.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class OrderDailyStatsJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final TransactionTemplate txTemplate;

	private final OrderRepository orderRepository;
	private final OrderDailyStatsRepository orderDailyStatsRepository;

	@Bean
	public Job orderDailyStatsJob() {
		return new JobBuilder("orderDailyStatsJob", jobRepository)
			.start(orderDailyStatsStep())
			.build();
	}

	@Bean
	public Step orderDailyStatsStep() {
		return new StepBuilder("orderDailyStatsStep", jobRepository)
			.tasklet(orderDailyStatsTasklet(), transactionManager)
			.build();
	}

	@Bean
	public Tasklet orderDailyStatsTasklet() {
		return (contribution, chunkContext) -> {
			String dateStr = (String) chunkContext.getStepContext()
				.getJobParameters().get("targetDate");
			LocalDate targetDate = LocalDate.parse(dateStr);

			LocalDateTime fromLdt = targetDate.atStartOfDay();
			LocalDateTime toLdt = targetDate.plusDays(1).atStartOfDay();

			ZoneId zone = ZoneId.of("Asia/Seoul");

			Instant from = fromLdt.atZone(zone).toInstant();
			Instant to = toLdt.atZone(zone).toInstant();

			txTemplate.executeWithoutResult(tx -> {
				long totalOrders = orderRepository.countAllBetween(from, to);
				long paidOrders = orderRepository.countByStatusBetween(from, to, OrderStatus.PAID);
				long canceled = orderRepository.countByStatusBetween(from, to,
					OrderStatus.CANCELED);
				BigDecimal totalAmount = orderRepository.sumAmountBetween(from, to);

				BigDecimal aov = (totalOrders == 0) ? BigDecimal.ZERO
					: totalAmount.divide(BigDecimal.valueOf(totalOrders), 2,
						java.math.RoundingMode.HALF_UP);

				OrderDailyStat stats = OrderDailyStat.of(
					targetDate,
					totalOrders,
					paidOrders,
					canceled,
					totalAmount,
					aov
				);

				orderDailyStatsRepository.save(stats);
			});

			return RepeatStatus.FINISHED;
		};
	}
}