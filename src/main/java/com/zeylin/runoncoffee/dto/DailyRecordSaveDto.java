package com.zeylin.runoncoffee.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DailyRecordSaveDto {

    private Integer grains;
    private Integer veggie;
    private Integer dairy;
    private Integer protein;

}
