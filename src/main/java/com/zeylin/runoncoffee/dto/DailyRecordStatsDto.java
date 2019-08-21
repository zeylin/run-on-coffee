package com.zeylin.runoncoffee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Percentages of recommended daily amounts per food group.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordStatsDto {

    private int grainsRec;
    private int veggieRec;
    private int dairyRec;
    private int proteinRec;

}
