package com.example.domain.service;

import com.example.domain.entity.User;
import com.example.domain.repository.ProcessedEventRepository;
import com.example.domain.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPointService {

	private final UserRepository userRepository;
	private final ProcessedEventRepository processedEventRepository;
	private static BigDecimal RATE = BigDecimal.valueOf(0.05);

	@Transactional
	public void grantPoints(long eventId, long userId, BigDecimal amount) {

		User user = userRepository.findById(userId).orElse(null);

		if (user == null) {
			log.warn("Skip point: user not found. userId={}", userId);
			return;
		}

		try {
			processedEventRepository.tryInsert("point-" + eventId);

		} catch (DataIntegrityViolationException e) {
			log.info("Skip duplicate by DB UNIQUE. eventId={}", eventId);
		}

		BigDecimal point = amount
			.multiply(RATE)
			.setScale(0, RoundingMode.DOWN);

		user.updatePoint(point);
	}

}
