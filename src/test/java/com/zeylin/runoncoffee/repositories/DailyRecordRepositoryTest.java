package com.zeylin.runoncoffee.repositories;

import com.zeylin.runoncoffee.models.DailyRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DailyRecordRepositoryTest {

    @Autowired
    DailyRecordRepository dailyRecordRepository;

    private UUID id;

    @Test
    public void retrieveItemTest() {
        // given
        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setGrains(5);
        dailyRecord.setVeggie(4);
        dailyRecord.setDairy(1);
        dailyRecord.setProtein(2);
        LocalDate today = LocalDate.now();
        dailyRecord.setDay(today);

        DailyRecord savedRecord = dailyRecordRepository.save(dailyRecord);
        id = savedRecord.getId();

        // when
        Optional<DailyRecord> dbRecord = dailyRecordRepository.findById(id);

        // then
        assertTrue(dbRecord.isPresent());

        DailyRecord retrievedRecord = dbRecord.get();
        assertEquals(5, retrievedRecord.getGrains().intValue());
        assertEquals(4, retrievedRecord.getVeggie().intValue());
        assertEquals(1, retrievedRecord.getDairy().intValue());
        assertEquals(2, retrievedRecord.getProtein().intValue());
    }

    @Test
    public void itemNotFoundTest() {
        // given
        UUID randomId = UUID.randomUUID();
        // when
        Optional<DailyRecord> dbRecord = dailyRecordRepository.findById(randomId);
        assertFalse(dbRecord.isPresent());
    }

}
