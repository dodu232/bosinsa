package com.example.domain.repository;

import com.example.domain.entity.OrderDailyStat;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDailyStatsRepository extends JpaRepository<OrderDailyStat, LocalDate> {

}
