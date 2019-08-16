package com.zeylin.runoncoffee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DailyRecordAveragesDto {

    private double grainsAverage;
    private double veggieAverage;
    private double dairyAverage;
    private double proteinAverage;

}
