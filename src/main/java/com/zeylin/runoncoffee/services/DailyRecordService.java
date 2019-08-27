package com.zeylin.runoncoffee.services;

import com.zeylin.runoncoffee.dto.DailyRecordAveragesDto;
import com.zeylin.runoncoffee.dto.DailyRecordDisplayDto;
import com.zeylin.runoncoffee.dto.DailyRecordSaveDto;
import com.zeylin.runoncoffee.dto.DailyRecordStatsDto;
import com.zeylin.runoncoffee.dto.DailyRecordUpdateDto;
import com.zeylin.runoncoffee.exceptions.NotFoundException;
import com.zeylin.runoncoffee.models.DailyRecord;
import com.zeylin.runoncoffee.models.dictionary.FoodGuide;
import com.zeylin.runoncoffee.repositories.DailyRecordRepository;
import com.zeylin.runoncoffee.services.dictionary.FoodGuideService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
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
    private static final int SEVEN_DAYS_AGO = 7;
    private static final int MONTH_AGO = 1;
    private static final int MONDAY_INDEX = 1;
    private static final int FIRST_OF_THE_MONTH = 1;
    private static DecimalFormat twoDecimalPointsFormat = new DecimalFormat("#.##");
    private static DecimalFormat noDecimalPointFormat = new DecimalFormat("#");

    private DailyRecordRepository dailyRecordRepository;
    private FoodGuideService foodGuideService;
    private ModelMapper modelMapper;

    public enum FoodGroup {
        GRAINS,
        VEGGIE,
        DAIRY,
        PROTEIN
    }

    /* Type of stats analysis */
    public enum StatsType {
        day,
        week,
        month
    }

    @Autowired
    public DailyRecordService(DailyRecordRepository dailyRecordRepository,
                              FoodGuideService foodGuideService,
                              ModelMapper modelMapper) {
        this.dailyRecordRepository = dailyRecordRepository;
        this.foodGuideService = foodGuideService;
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

    public DailyRecordDisplayDto getRecordByDate(LocalDate date) {
        Optional<DailyRecord> record = dailyRecordRepository.findByDay(date);
        return record
                .map(t -> convertToDisplayDto(record.get()))
                .orElseGet(DailyRecordDisplayDto::new);
    }

    private DailyRecordAveragesDto getRecordDataByDate(LocalDate date) {
        Optional<DailyRecord> record = dailyRecordRepository.findByDay(date);
        return record
                .map(t -> convertToAveragesDto(record.get()))
                .orElseGet(DailyRecordAveragesDto::new);
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

    /**
     * Get stats for day, week, or month.
     */
    public DailyRecordAveragesDto getStatsByDateAndTime(LocalDate date, StatsType type) {
        switch(type) {
            case day:
                return getRecordDataByDate(date);
            case week:
                return getAveragesForWeekOf(date);
            case month:
                return getAveragesForMonthOf(date);
            default:
                return new DailyRecordAveragesDto();
        }
    }

    /**
     * Get average for the week of the given date.
     * @param date date within the week to get the stats for
     * @return averages over week
     */
    private DailyRecordAveragesDto getAveragesForWeekOf(LocalDate date) {
        if (date.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            return getAveragesStartingFromDateOverTime(date, SEVEN_DAYS);
        }

        // Get beginning of the week (Monday)
        int mondayOffset = date.getDayOfWeek().getValue() - MONDAY_INDEX;
        LocalDate dateOfMonday = date.minusDays(mondayOffset);
        return getAveragesStartingFromDateOverTime(dateOfMonday, SEVEN_DAYS);
    }

    /**
     * Get average for the month of the given date.
     * @param date date within the month to get the stats for
     * @return averages over month
     */
    private DailyRecordAveragesDto getAveragesForMonthOf(LocalDate date) {
        if (date.getDayOfMonth() == 1) { // 1st of the month
            return getAveragesStartingFromDateOverTime(date, THIRTY_DAYS);
        }

        // Get 1st of the month
        LocalDate monthStart = LocalDate.of(date.getYear(), date.getMonthValue(), FIRST_OF_THE_MONTH);
        return getAveragesStartingFromDateOverTime(monthStart, THIRTY_DAYS);
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
        LocalDate weekAgo = LocalDate.now().minusDays(SEVEN_DAYS_AGO);
        return getRecordsAfterDate(weekAgo);
    }

    public List<DailyRecordDisplayDto> getLastMonth() {
        LocalDate monthAgo = LocalDate.now().minusMonths(MONTH_AGO);
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
        return getAveragesStartingFromDateOverTime(LocalDate.now().minusDays(SEVEN_DAYS_AGO), SEVEN_DAYS);
    }

    /**
     * Get last month's averages (past 30 days).
     */
    public DailyRecordAveragesDto getLastMonthAverage() {
        return getAveragesStartingFromDateOverTime(LocalDate.now().minusMonths(MONTH_AGO), THIRTY_DAYS);
    }

    /**
     * Compute averages starting from the given date, over the given number of days.
     * @param date date to start computing the averages from
     * @param numberOfDays number of days to compute the averages for
     * @return averages for each food group
     */
    private DailyRecordAveragesDto getAveragesStartingFromDateOverTime(LocalDate date, int numberOfDays) {

        LOGGER.info("get averages after {} ", date);

        List<DailyRecord> records = dailyRecordRepository.findByDayAfterOrderByDayAsc(date);

        if (records.isEmpty()) {
            return DailyRecordAveragesDto.builder()
                    .grains(0)
                    .veggie(0)
                    .dairy(0)
                    .protein(0)
                    .build();
        }

        return computeAveragesOverTime(records, numberOfDays);
    }

    /**
     * Compute averages for each food group over the given time.
     * @param records list of daily records to analyze
     * @param numberOfDays number of days to compute averages for
     * @return averages for each of the food groups
     */
    private DailyRecordAveragesDto computeAveragesOverTime(List<DailyRecord> records, int numberOfDays) {
        double grainsAverage = getAverageForFoodGroupOverTime(records, FoodGroup.GRAINS, numberOfDays);
        double veggieAverage = getAverageForFoodGroupOverTime(records, FoodGroup.VEGGIE, numberOfDays);
        double dairyAverage = getAverageForFoodGroupOverTime(records, FoodGroup.DAIRY, numberOfDays);
        double proteinAverage = getAverageForFoodGroupOverTime(records, FoodGroup.PROTEIN, numberOfDays);

        return DailyRecordAveragesDto.builder()
                .grains(grainsAverage)
                .veggie(veggieAverage)
                .dairy(dairyAverage)
                .protein(proteinAverage)
                .build();
    }

    /**
     * Compute average for a given food group over a given time.
     * @param records list of daily records
     * @param foodGroup name of the food group
     * @param numberOfDays number of days to calculate the average over
     * @return average value for this food group over the given time
     */
    private double getAverageForFoodGroupOverTime(List<DailyRecord> records, FoodGroup foodGroup, int numberOfDays) {
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
        double avg = sum / numberOfDays;
        twoDecimalPointsFormat.setRoundingMode(RoundingMode.HALF_UP);
        return Double.valueOf(twoDecimalPointsFormat.format(avg));
    }

    /**
     * Get statistics on percentages of recommended daily guides for a given day.
     * @param date date to analyze
     * @param guideId food guide id
     * @return stats on that day, if present
     */
    public DailyRecordStatsDto getDailyStatsByDate(LocalDate date, Long guideId) {
        Optional<DailyRecord> dbRecord = dailyRecordRepository.findByDay(date);
        if(dbRecord.isPresent()) {
            DailyRecord record = dbRecord.get();

            Optional<FoodGuide> dbFoodGuide = foodGuideService.getById(guideId);
            FoodGuide foodGuide =  dbFoodGuide
                    .map(t -> dbFoodGuide.get())
                    .orElseGet(FoodGuide::getDefault);

            double grainsPercent = (double) record.getGrains() / foodGuide.getGrains() * 100;
            double veggiePercent = (double) record.getVeggie() / foodGuide.getVeggie() * 100;
            double dairyPercent = (double) record.getDairy() / foodGuide.getDairy() * 100;
            double proteinPercent = (double) record.getProtein() / foodGuide.getProtein() * 100;

            noDecimalPointFormat.setRoundingMode(RoundingMode.HALF_UP);
            return DailyRecordStatsDto.builder()
                    .grainsRec(Integer.valueOf(noDecimalPointFormat.format(grainsPercent)))
                    .veggieRec(Integer.valueOf(noDecimalPointFormat.format(veggiePercent)))
                    .dairyRec(Integer.valueOf(noDecimalPointFormat.format(dairyPercent)))
                    .proteinRec(Integer.valueOf(noDecimalPointFormat.format(proteinPercent)))
                    .build();
        }
        return new DailyRecordStatsDto();
    }

    /**
     * Get stats on recommended daily guides percentages for the last week.
     */
    public DailyRecordStatsDto getWeeklyStats(Long guideId) {
        LocalDate weekAgo = LocalDate.now().minusDays(SEVEN_DAYS_AGO);
        return getStatsStartingFromForGuide(weekAgo, guideId);
    }

    /**
     * Get stats on recommended daily guides percentages for the last month.
     */
    public DailyRecordStatsDto getMonthlyStats(Long guideId) {
        LocalDate monthAgo = LocalDate.now().minusMonths(MONTH_AGO);
        return getStatsStartingFromForGuide(monthAgo, guideId);
    }

    /**
     * Get stats (recommended daily guides percentages) starting from a given date, for a given food guide.
     * @param date date to start analysing data from
     * @param guideId id of the food guide to follow
     * @return percentages of recommended daily nutrition guides
     */
    private DailyRecordStatsDto getStatsStartingFromForGuide(LocalDate date, Long guideId) {
        List<DailyRecord> records = dailyRecordRepository.findByDayAfterOrderByDayAsc(date);
        if (records.isEmpty()) {
            return DailyRecordStatsDto.builder()
                    .grainsRec(0)
                    .veggieRec(0)
                    .dairyRec(0)
                    .proteinRec(0)
                    .build();
        }

        double grainsAverage = getAverageForFoodGroupOverTime(records, FoodGroup.GRAINS, SEVEN_DAYS);
        double veggieAverage = getAverageForFoodGroupOverTime(records, FoodGroup.VEGGIE, SEVEN_DAYS);
        double dairyAverage = getAverageForFoodGroupOverTime(records, FoodGroup.DAIRY, SEVEN_DAYS);
        double proteinAverage = getAverageForFoodGroupOverTime(records, FoodGroup.PROTEIN, SEVEN_DAYS);

        Optional<FoodGuide> dbFoodGuide = foodGuideService.getById(guideId);
        FoodGuide foodGuide =  dbFoodGuide
                .map(t -> dbFoodGuide.get())
                .orElseGet(FoodGuide::getDefault);

        double grainsPercent = grainsAverage / foodGuide.getGrains() * 100;
        double veggiePercent = veggieAverage / foodGuide.getVeggie() * 100;
        double dairyPercent = dairyAverage / foodGuide.getDairy() * 100;
        double proteinPercent = proteinAverage / foodGuide.getProtein() * 100;

        noDecimalPointFormat.setRoundingMode(RoundingMode.HALF_UP);
        return DailyRecordStatsDto.builder()
                .grainsRec(Integer.valueOf(noDecimalPointFormat.format(grainsPercent)))
                .veggieRec(Integer.valueOf(noDecimalPointFormat.format(veggiePercent)))
                .dairyRec(Integer.valueOf(noDecimalPointFormat.format(dairyPercent)))
                .proteinRec(Integer.valueOf(noDecimalPointFormat.format(proteinPercent)))
                .build();
    }

    private DailyRecord convertToDailyRecord(DailyRecordSaveDto recordDto) {
        return modelMapper.map(recordDto, DailyRecord.class);
    }

    private DailyRecordDisplayDto convertToDisplayDto(DailyRecord record) {
        return modelMapper.map(record, DailyRecordDisplayDto.class);
    }

    private DailyRecordAveragesDto convertToAveragesDto(DailyRecord record) {
        return modelMapper.map(record, DailyRecordAveragesDto.class);
    }

}
