package com.zeylin.runoncoffee.controllers;

import com.zeylin.runoncoffee.dto.DailyRecordDisplayDto;
import com.zeylin.runoncoffee.dto.DailyRecordSaveDto;
import com.zeylin.runoncoffee.dto.DailyRecordUpdateDto;
import com.zeylin.runoncoffee.models.DailyRecord;
import com.zeylin.runoncoffee.services.DailyRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/record")
public class DailyRecordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyRecordController.class);

    private DailyRecordService recordService;

    @Autowired
    public DailyRecordController(DailyRecordService recordService) {
        this.recordService = recordService;
    }

    /**
     * GET BY ID
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DailyRecordDisplayDto> getById(@PathVariable UUID id) {
        LOGGER.debug("get daily record with id {} ", id);
        return ResponseEntity.ok(recordService.getRecord(id));
    }

    /**
     * GET BY DATE
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DailyRecordDisplayDto>> getByDate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("date") LocalDate date) {
        LOGGER.debug("get daily record with date {} ", date);
        return ResponseEntity.ok(recordService.getRecordByDate(date));
    }

    /**
     * GET ALL
     */
    @GetMapping(value = "/list", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DailyRecordDisplayDto>> getAllRecords() {
        LOGGER.debug("get daily records");
        List<DailyRecordDisplayDto> records = recordService.getAllRecords();
        return ResponseEntity.ok(records);
    }

    /**
     * SAVE
     */
    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DailyRecord> save(@RequestBody DailyRecordSaveDto saveDto) {
        LOGGER.debug("create daily record {} ", saveDto);
        return ResponseEntity.ok(recordService.save(saveDto));
    }

    /**
     * UPDATE
     */
    @PutMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DailyRecordDisplayDto> update(@PathVariable UUID id, @RequestBody DailyRecordSaveDto record) {
        LOGGER.debug("update record with id {} ", id);
        return ResponseEntity.ok(recordService.update(id, record));
    }

    /**
     * DELETE
     */
    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        LOGGER.debug("delete record with id {} ", id);
        recordService.delete(id);
    }

    /**
     * Increment record's data.
     */
    @PutMapping(value = "/add/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DailyRecordDisplayDto> increment(@PathVariable UUID id,
                                                           @RequestBody DailyRecordUpdateDto data) {
        LOGGER.info("increment record with id {},{} ", id, data);
        return ResponseEntity.ok(recordService.incrementRecord(id, data));
    }

    /**
     * Get last week's records.
     */
    @GetMapping(value = "/list/week", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DailyRecordDisplayDto>> getLastWeek() {
        LOGGER.debug("get last week records");
        return ResponseEntity.ok(recordService.getLastWeek());
    }

    /**
     * Get last month's records.
     */
    @GetMapping(value = "/list/month", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DailyRecordDisplayDto>> getLastMonth() {
        LOGGER.debug("get last month records");
        return ResponseEntity.ok(recordService.getLastMonth());
    }

}
