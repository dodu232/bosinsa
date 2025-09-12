package com.example.batch.runner;

import java.time.LocalDate;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchRunner implements CommandLineRunner {

	private final JobLauncher jobLauncher;
	private final Job orderDailyStatsJob;

	// 환경변수/커맨드라인로 주입: --targetDate=YYYY-MM-DD (없으면 어제)
	@Value("${targetDate:}")
	private String targetDateArg;

	@Override
	public void run(String... args) throws Exception {
		String targetDate = (targetDateArg == null || targetDateArg.isBlank())
			? LocalDate.now(ZoneOffset.UTC).minusDays(1).toString()
			: targetDateArg;

		JobParameters params = new JobParametersBuilder()
			.addString("targetDate", targetDate)
			.addLong("ts", System.currentTimeMillis())
			.toJobParameters();

		log.info("Starting job=orderDailyStatsJob targetDate={}", targetDate);

		JobExecution execution = jobLauncher.run(orderDailyStatsJob, params);

		BatchStatus status = execution.getStatus();
		log.info("Job finished with status={}", status);

		// 성공 0, 그 외 1
		System.exit(status == BatchStatus.COMPLETED ? 0 : 1);
	}
}