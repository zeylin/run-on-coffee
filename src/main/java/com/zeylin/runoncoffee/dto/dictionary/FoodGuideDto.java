package com.zeylin.runoncoffee.dto.dictionary;

import com.zeylin.runoncoffee.models.dictionary.FoodGuide.AgeGroup;
import com.zeylin.runoncoffee.models.dictionary.FoodGuide.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodGuideDto {

    private Long id;
    private Gender gender;
    private AgeGroup ageGroup;

    // Recommended number of servings
    private int grains;
    private int veggie;
    private int dairy;
    private int protein;

}
