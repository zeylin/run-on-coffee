package com.zeylin.runoncoffee.dto.dailyrecord;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DailyRecordDisplayDto {

    private LocalDate day;
    private Integer grains;
    private Integer veggie;
    private Integer dairy;
    private Integer protein;

}
