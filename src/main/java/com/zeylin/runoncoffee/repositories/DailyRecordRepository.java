package com.zeylin.runoncoffee.repositories;

import com.zeylin.runoncoffee.models.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DailyRecordRepository extends JpaRepository<DailyRecord, UUID> {

    List<DailyRecord> findByDay(LocalDate date);

    List<DailyRecord> findByDayAfterOrderByDayAsc(LocalDate date);

}
