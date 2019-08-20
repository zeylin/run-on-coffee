package com.zeylin.runoncoffee.services;

import com.zeylin.runoncoffee.dto.DailyRecordAveragesDto;
import com.zeylin.runoncoffee.dto.DailyRecordDisplayDto;
import com.zeylin.runoncoffee.dto.DailyRecordSaveDto;
import com.zeylin.runoncoffee.dto.DailyRecordUpdateDto;
import com.zeylin.runoncoffee.exceptions.NotFoundException;
import com.zeylin.runoncoffee.models.DailyRecord;
import com.zeylin.runoncoffee.repositories.DailyRecordRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
public class DailyRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyRecordService.class);

    private static final int SEVEN_DAYS = 7;
    private static final int THIRTY_DAYS = 30;
    private static DecimalFormat twoDecimalPointsFormat = new DecimalFormat("#.##");

    private DailyRecordRepository dailyRecordRepository;
    private ModelMapper modelMapper;

    public enum FoodGroup {
        GRAINS,
        VEGGIE,
        DAIRY,
        PROTEIN
    }

    @Autowired
    public DailyRecordService(DailyRecordRepository dailyRecordRepository,
                              ModelMapper modelMapper) {
        this.dailyRecordRepository = dailyRecordRepository;
        this.modelMapper = modelMapper;
    }

    public List<DailyRecordDisplayDto> getAllRecords() {
        List<DailyRecord> records = dailyRecordRepository.findAll();
        return records.stream()
                .map(record -> convertToDisplayDto(record))
                .collect(Collectors.toList());
    }

    public DailyRecordDisplayDto getRecord(UUID id) {
        Optional<DailyRecord> record = dailyRecordRepository.findById(id);
        return record
                .map(t -> convertToDisplayDto(record.get()))
                .orElseThrow(NotFoundException::new);
    }

    public List<DailyRecordDisplayDto> getRecordByDate(LocalDate date) {
        List<DailyRecord> records = dailyRecordRepository.findByDay(date);
        return records.stream()
                .map(record -> convertToDisplayDto(record))
                .collect(Collectors.toList());
    }

    private Optional<DailyRecord> getById(UUID id) {
        return dailyRecordRepository.findById(id);
    }

    @Transactional
    public DailyRecord save(DailyRecordSaveDto recordDto) {
        DailyRecord record = convertToDailyRecord(recordDto);
        LocalDate today = LocalDate.now();
        record.setDay(today);
        return dailyRecordRepository.save(record);
    }

    @Transactional
    public DailyRecordDisplayDto update(UUID id, DailyRecordSaveDto recordDto) {
        Optional<DailyRecord> dbRecord = getById(id);
        if(dbRecord.isPresent()) {
            DailyRecord record = convertToDailyRecord(recordDto);
            record.setId(id);
            record.setDay(dbRecord.get().getDay());
            record.setCreatedDate(dbRecord.get().getCreatedDate());
            return convertToDisplayDto(dailyRecordRepository.save(record));
        } else {
            throw new NotFoundException();
        }
    }

    @Transactional
    public void delete(UUID id) {
        Optional<DailyRecord> dbRecord = getById(id);
        dbRecord.ifPresent(record -> dailyRecordRepository.delete(record));
    }

    @Transactional
    public DailyRecordDisplayDto incrementRecord(UUID id, DailyRecordUpdateDto updateDto) {
        Optional<DailyRecord> dbRecord = getById(id);
        if(dbRecord.isPresent()) {
            DailyRecord existingRecord = dbRecord.get();

            boolean isUpdated = false;
            if(updateDto.getGrainsVal() != null) {
                int newGrains = existingRecord.getGrains() + updateDto.getGrainsVal();
                if(newGrains < 0 ) newGrains = 0;
                existingRecord.setGrains(newGrains);
                isUpdated = true;
            }
            if(updateDto.getDairyVal() != null) {
                int newDairy = existingRecord.getDairy() + updateDto.getDairyVal();
                if(newDairy < 0 ) newDairy = 0;
                existingRecord.setDairy(newDairy);
                isUpdated = true;
            }
            if(updateDto.getVeggieVal() != null) {
                int newVeggie = existingRecord.getVeggie() + updateDto.getVeggieVal();
                if(newVeggie < 0 ) newVeggie = 0;
                existingRecord.setVeggie(newVeggie);
                isUpdated = true;
            }
            if(updateDto.getProteinVal() != null) {
                int newProtein = existingRecord.getProtein() + updateDto.getProteinVal();
                if(newProtein < 0 ) newProtein = 0;
                existingRecord.setProtein(newProtein);
                isUpdated = true;
            }

            if(isUpdated) {
                return convertToDisplayDto(dailyRecordRepository.save(existingRecord));
            } else {
                // return existing record if nothing changed
                return convertToDisplayDto(existingRecord);
            }
        } else {
            throw new NotFoundException();
        }
    }

    public List<DailyRecordDisplayDto> getLastWeek() {
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        return getRecordsAfterDate(weekAgo);
    }

    public List<DailyRecordDisplayDto> getLastMonth() {
        LocalDate monthAgo = LocalDate.now().minusMonths(1);
        return getRecordsAfterDate(monthAgo);
    }

    private List<DailyRecordDisplayDto> getRecordsAfterDate(LocalDate date) {
        LOGGER.info("get records after {} ", date);
        List<DailyRecord> records = dailyRecordRepository.findByDayAfterOrderByDayAsc(date);
        return records.stream()
                .map(record -> convertToDisplayDto(record))
                .collect(Collectors.toList());
    }

    /**
     * Get last week's averages (past 7 days).
     */
    public DailyRecordAveragesDto getLastWeekAverage() {
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        List<DailyRecord> records = dailyRecordRepository.findByDayAfterOrderByDayAsc(weekAgo);

        if(records.isEmpty()) {
            return DailyRecordAveragesDto.builder()
                    .grainsAverage(0)
                    .veggieAverage(0)
                    .dairyAverage(0)
                    .proteinAverage(0)
                    .build();
        }

        double grainsAverage = getWeeklyAverage(records, FoodGroup.GRAINS);
        double veggieAverage = getWeeklyAverage(records, FoodGroup.VEGGIE);
        double dairyAverage = getWeeklyAverage(records, FoodGroup.DAIRY);
        double proteinAverage = getWeeklyAverage(records, FoodGroup.PROTEIN);

        return DailyRecordAveragesDto.builder()
                .grainsAverage(grainsAverage)
                .veggieAverage(veggieAverage)
                .dairyAverage(dairyAverage)
                .proteinAverage(proteinAverage)
                .build();

    }

    private double getWeeklyAverage(List<DailyRecord> records, FoodGroup foodGroup) {
        double sum = 0;
        for(DailyRecord record : records) {
            switch(foodGroup) {
                case GRAINS:
                    sum = sum + record.getGrains();
                    break;
                case VEGGIE:
                    sum = sum + record.getVeggie();
                    break;
                case DAIRY:
                    sum = sum + record.getDairy();
                    break;
                case PROTEIN:
                    sum = sum + record.getProtein();
                    break;
            }
        }
        double avg = sum / SEVEN_DAYS;
        twoDecimalPointsFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        return Double.valueOf(twoDecimalPointsFormat.format(avg));
    }

    /**
     * Get last month's averages (past 30 days).
     */
    public DailyRecordAveragesDto getLastMonthAverage() {
        LocalDate monthAgo = LocalDate.now().minusMonths(1);
        List<DailyRecord> records = dailyRecordRepository.findByDayAfterOrderByDayAsc(monthAgo);

        if(records.isEmpty()) {
            return DailyRecordAveragesDto.builder()
                    .grainsAverage(0)
                    .veggieAverage(0)
                    .dairyAverage(0)
                    .proteinAverage(0)
                    .build();
        }

        double grainsAverage = getMonthlyAverage(records, FoodGroup.GRAINS);
        double veggieAverage = getMonthlyAverage(records, FoodGroup.VEGGIE);
        double dairyAverage = getMonthlyAverage(records, FoodGroup.DAIRY);
        double proteinAverage = getMonthlyAverage(records, FoodGroup.PROTEIN);

        return DailyRecordAveragesDto.builder()
                .grainsAverage(grainsAverage)
                .veggieAverage(veggieAverage)
                .dairyAverage(dairyAverage)
                .proteinAverage(proteinAverage)
                .build();
    }

    private double getMonthlyAverage(List<DailyRecord> records, FoodGroup foodGroup) {
        double sum = 0;
        for(DailyRecord record : records) {
            switch(foodGroup) {
                case GRAINS:
                    sum = sum + record.getGrains();
                    break;
                case VEGGIE:
                    sum = sum + record.getVeggie();
                    break;
                case DAIRY:
                    sum = sum + record.getDairy();
                    break;
                case PROTEIN:
                    sum = sum + record.getProtein();
                    break;
            }
        }
        double avg = sum / THIRTY_DAYS;
        twoDecimalPointsFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        return Double.valueOf(twoDecimalPointsFormat.format(avg));
    }

    private DailyRecord convertToDailyRecord(DailyRecordSaveDto recordDto) {
        return modelMapper.map(recordDto, DailyRecord.class);
    }

    private DailyRecordDisplayDto convertToDisplayDto(DailyRecord record) {
        return modelMapper.map(record, DailyRecordDisplayDto.class);
    }

}
