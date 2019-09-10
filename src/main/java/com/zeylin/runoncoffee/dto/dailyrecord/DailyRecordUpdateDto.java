package com.zeylin.runoncoffee.dto.dailyrecord;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DailyRecordUpdateDto {

    private Integer grainsVal;
    private Integer veggieVal;
    private Integer dairyVal;
    private Integer proteinVal;

}
