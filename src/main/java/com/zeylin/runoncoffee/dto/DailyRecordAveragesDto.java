package com.zeylin.runoncoffee.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
public class DailyRecordAveragesDto {

    private double grainsAverage;
    private double veggieAverage;
    private double dairyAverage;
    private double proteinAverage;

}
