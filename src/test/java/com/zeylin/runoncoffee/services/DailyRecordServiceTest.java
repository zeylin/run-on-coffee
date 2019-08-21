package com.zeylin.runoncoffee.services;

import com.zeylin.runoncoffee.dto.DailyRecordAveragesDto;
import com.zeylin.runoncoffee.dto.DailyRecordDisplayDto;
import com.zeylin.runoncoffee.dto.DailyRecordSaveDto;
import com.zeylin.runoncoffee.dto.DailyRecordStatsDto;
import com.zeylin.runoncoffee.exceptions.NotFoundException;
import com.zeylin.runoncoffee.models.DailyRecord;
import com.zeylin.runoncoffee.repositories.DailyRecordRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DailyRecordServiceTest {

    private static final long GUIDE_ONE = 1L;
    private static final long GUIDE_FIVE = 5L;
    private static final long GUIDE_THREE = 3L;

    @Autowired
    DailyRecordService dailyRecordService;

    @Autowired
    DailyRecordRepository dailyRecordRepository;

    private UUID id;

    @Before
    public void setUp() {
        // Clear the database
        List<DailyRecord> records = dailyRecordRepository.findAll();
        records.forEach(record -> dailyRecordService.delete(record.getId()));
    }

    @Test
    public void getDailyRecordTest() {
        //given
        DailyRecordSaveDto dto = new DailyRecordSaveDto();
        dto.setGrains(5);
        dto.setVeggie(4);
        dto.setDairy(1);
        dto.setProtein(2);
        DailyRecord savedDailyRecord = dailyRecordService.save(dto);
        id = savedDailyRecord.getId();

        // when
        DailyRecordDisplayDto dailyRecord = dailyRecordService.getRecord(id);

        // then
        assertEquals(5, dailyRecord.getGrains().intValue());
        assertEquals(4, dailyRecord.getVeggie().intValue());
        assertEquals(1, dailyRecord.getDairy().intValue());
        assertEquals(2, dailyRecord.getProtein().intValue());
    }

    @Test(expected = NotFoundException.class)
    public void itemNotFoundTest() {
        // given
        UUID randomId = UUID.randomUUID();
        // when
        dailyRecordService.getRecord(randomId);
    }

    @Test
    public void getLastWeekAveragesWhenNoRecordsPresentTest() {
        // when
        DailyRecordAveragesDto response = dailyRecordService.getLastWeekAverage();

        // then
        assertEquals(0, response.getGrainsAverage(), 0.0001);
        assertEquals(0, response.getVeggieAverage(), 0.0001);
        assertEquals(0, response.getDairyAverage(), 0.0001);
        assertEquals(0, response.getProteinAverage(), 0.0001);
    }

    @Test
    public void getLastMonthAveragesWhenNoRecordsPresentTest() {
        // when
        DailyRecordAveragesDto response = dailyRecordService.getLastMonthAverage();

        // then
        assertEquals(0, response.getGrainsAverage(), 0.0001);
        assertEquals(0, response.getVeggieAverage(), 0.0001);
        assertEquals(0, response.getDairyAverage(), 0.0001);
        assertEquals(0, response.getProteinAverage(), 0.0001);
    }

    @Test
    public void checkLastWeekAveragesTest() {
        // given
        addTodaysRecord(6,5,4,3);
        addRecord(5,4,3,2,1);
        addRecord(4,3,2,1,2);

        // when
        DailyRecordAveragesDto response = dailyRecordService.getLastWeekAverage();

        // then
        double expectedGrains = (double) (6 + 5 + 4) / 7;
        double expectedVeggie = (double) (5 + 4 + 3) / 7;
        double expectedDairy = (double) (4 + 3 + 2) / 7;
        double expectedProtein = (double) (3 + 2 + 1) / 7;

        assertEquals(expectedGrains, response.getGrainsAverage(), 0.005); // since rounding to two decimals
        assertEquals(expectedVeggie, response.getVeggieAverage(), 0.005);
        assertEquals(expectedDairy, response.getDairyAverage(), 0.005);
        assertEquals(expectedProtein, response.getProteinAverage(), 0.005);
    }

    @Test
    public void checkLastMonthAveragesTest() {
        // given
        addTodaysRecord(7,6,5,4);
        addRecord(5,4,3,2,2);
        addRecord(5,4,3,2,5);
        addRecord(4,3,2,1,10);
        addRecord(5,3,2,3,12);
        addRecord(1,2,3,4,15);
        addRecord(6,4,0,2,17);
        addRecord(2,3,4,5,20);
        addRecord(5,4,0,2,22);
        addRecord(5,5,1,3,25);

        // when
        DailyRecordAveragesDto response = dailyRecordService.getLastMonthAverage();

        // then
        double expectedGrains = (double) (7+5+4+1+2+5+5+5+6+5) / 30;
        double expectedVeggie = (double) (6+4+3+2+3+5+4+3+4+4) / 30;
        double expectedDairy = (double) (5+3+2+3+4+1+3+2) / 30;
        double expectedProtein = (double) (4+2+1+4+5+3+2+3+2+2) / 30;

        assertEquals(expectedGrains, response.getGrainsAverage(), 0.005); // since rounding to two decimals
        assertEquals(expectedVeggie, response.getVeggieAverage(), 0.005);
        assertEquals(expectedDairy, response.getDairyAverage(), 0.005);
        assertEquals(expectedProtein, response.getProteinAverage(), 0.005);
    }

    @Test
    public void checkDailyStatsTest() {
        // given
        addTodaysRecord(6,5,2,3);
        LocalDate today = LocalDate.now();

        // when
        DailyRecordStatsDto response = dailyRecordService.getDailyStats(today, GUIDE_ONE);

        // then
        assertEquals(86, response.getGrainsRec());
        assertEquals(83, response.getVeggieRec());
        assertEquals(100, response.getDairyRec());
        assertEquals(150, response.getProteinRec());
    }

    @Test
    public void checkWeeklyStatsTest() {
        // given
        addTodaysRecord(6,7,4,3);
        addRecord(5,4,3,2,2);
        addRecord(4,6,2,2,4);

        // when
        DailyRecordStatsDto response = dailyRecordService.getWeeklyStats(GUIDE_FIVE);

        // then
        assertEquals(27, response.getGrainsRec());
        assertEquals(35, response.getVeggieRec());
        assertEquals(65, response.getDairyRec());
        assertEquals(50, response.getProteinRec());
    }

    @Test
    public void checkMonthlyStatsTest() {
        // given
        addTodaysRecord(7,6,5,4);
        addRecord(5,4,3,2,2);
        addRecord(5,4,3,2,5);
        addRecord(4,3,2,1,10);
        addRecord(5,3,2,3,12);
        addRecord(1,2,3,4,15);
        addRecord(6,4,0,2,17);
        addRecord(2,3,4,5,20);
        addRecord(5,4,0,2,22);
        addRecord(5,5,1,3,25);

        // when
        DailyRecordStatsDto response = dailyRecordService.getMonthlyStats(GUIDE_THREE);

        // then
        assertEquals(80, response.getGrainsRec());
        assertEquals(68, response.getVeggieRec());
        assertEquals(165, response.getDairyRec());
        assertEquals(133, response.getProteinRec());
    }

    private void addRecord(int grains, int veggie, int dairy, int protein, long daysOffset) {
        DailyRecord record = new DailyRecord();
        record.setGrains(grains);
        record.setVeggie(veggie);
        record.setDairy(dairy);
        record.setProtein(protein);
        LocalDate day = LocalDate.now().minusDays(daysOffset);
        record.setDay(day);
        dailyRecordRepository.save(record);
    }

    private void addTodaysRecord(int grains, int veggie, int dairy, int protein) {
        DailyRecord record = new DailyRecord();
        record.setGrains(grains);
        record.setVeggie(veggie);
        record.setDairy(dairy);
        record.setProtein(protein);
        LocalDate day = LocalDate.now();
        record.setDay(day);
        dailyRecordRepository.save(record);
    }

}
