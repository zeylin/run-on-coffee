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

    private double grains;
    private double veggie;
    private double dairy;
    private double protein;

}
