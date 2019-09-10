package com.zeylin.runoncoffee.dto;

import com.zeylin.runoncoffee.services.DailyRecordService;
import com.zeylin.runoncoffee.services.DailyRecordService.FoodGroup;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFoodItemDto {

    private Long id;
    private FoodGroup foodGroup;
    private double quantity;

}
