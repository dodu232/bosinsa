package com.example.domain.repository;

import com.example.domain.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Long> {

	@Modifying
	@Query(value = "INSERT INTO processed_events(id) VALUES (:eventId)", nativeQuery = true)
	int tryInsert(@Param("eventId") String eventId);
}
