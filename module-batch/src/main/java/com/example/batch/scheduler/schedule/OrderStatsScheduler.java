package com.example.batch.scheduler.schedule;


import java.time.LocalDate;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class OrderStatsScheduler {

	private final JobLauncher jobLauncher;
	private final Job orderDailyStatsJob;

	// 매일 01:10 UTC 실행
	@Scheduled(cron = "0 10 1 * * *", zone = "UTC")
	public void runDaily() throws Exception {
		String targetDate = LocalDate.now(ZoneOffset.UTC).minusDays(1).toString();

		jobLauncher.run(orderDailyStatsJob,
			new JobParametersBuilder()
				.addString("targetDate", targetDate)
				.addLong("ts", System.currentTimeMillis()) // RunIdIncrementer와 충돌 피하기
				.toJobParameters());
	}
}