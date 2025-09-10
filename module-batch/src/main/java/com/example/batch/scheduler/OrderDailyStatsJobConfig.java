package com.example.batch.scheduler;

import com.example.domain.enums.OrderStatus;
import com.example.domain.repository.OrderDailyStatsRepository;
import com.example.domain.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
	private final OrderRepository orderRepository;
	private final OrderDailyStatsRepository statsRepository;
	private final TransactionTemplate txTemplate;

	// JobParameters: targetDate=YYYY-MM-DD (없으면 어제)
	@Bean
	public Job orderDailyStatsJob() {
		return new JobBuilder("orderDailyStatsJob", jobRepository)
			.incrementer(new RunIdIncrementer())
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
				.getJobParameters()
				.getOrDefault("targetDate", "");

			LocalDate targetDate = (dateStr == null || dateStr.isBlank())
				? LocalDate.now(ZoneOffset.UTC).minusDays(1)
				: LocalDate.parse(dateStr);

			LocalDateTime from = targetDate.atStartOfDay();
			LocalDateTime to = targetDate.plusDays(1).atStartOfDay();

			// 집계 (트랜잭션 경계 내에서 수행)
			txTemplate.executeWithoutResult(tx -> {
				long totalOrders = orderRepository.countAllBetween(from, to);
				long paidOrders = orderRepository.countByStatusBetween(from, to, OrderStatus.PAID);
				long canceled = orderRepository.countByStatusBetween(from, to,
					OrderStatus.CANCELED);
				BigDecimal totalAmount = orderRepository.sumAmountBetween(from, to);
				BigDecimal aov = (totalOrders == 0)
					? BigDecimal.ZERO
					: totalAmount.divide(BigDecimal.valueOf(totalOrders), 2,
						java.math.RoundingMode.HALF_UP);

				OrderDailyStats stats = OrderDailyStats.builder()
					.statDate(targetDate)
					.totalOrders(totalOrders)
					.paidOrders(paidOrders)
					.canceledOrders(canceled)
					.totalAmount(totalAmount)
					.aov(aov)
					.updatedAt(LocalDateTime.now(ZoneOffset.UTC))
					.build();

				statsRepository.save(stats); // PK(stat_date)라서 upsert 처럼 동작(JPA는 merge semantics)
			});

			return RepeatStatus.FINISHED;
		};
	}
}
