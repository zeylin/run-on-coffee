package com.zeylin.runoncoffee.services;

import com.zeylin.runoncoffee.dto.DailyRecordDisplayDto;
import com.zeylin.runoncoffee.dto.DailyRecordSaveDto;
import com.zeylin.runoncoffee.exceptions.NotFoundException;
import com.zeylin.runoncoffee.models.DailyRecord;
import com.zeylin.runoncoffee.repositories.DailyRecordRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
public class DailyRecordService {

    private DailyRecordRepository dailyRecordRepository;
    private ModelMapper modelMapper;

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

    private DailyRecord convertToDailyRecord(DailyRecordSaveDto recordDto) {
        return modelMapper.map(recordDto, DailyRecord.class);
    }

    private DailyRecordDisplayDto convertToDisplayDto(DailyRecord record) {
        return modelMapper.map(record, DailyRecordDisplayDto.class);
    }

}
