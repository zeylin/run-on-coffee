package com.zeylin.runoncoffee.dto.dictionary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseFoodItemWithServingDto extends BaseFoodItemDto {
    private String servingName;
}
