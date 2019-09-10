package com.zeylin.runoncoffee.models;

import com.zeylin.runoncoffee.models.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DailyRecordFractioned extends BaseEntity {

    @Column(name = "day", columnDefinition = "DATE")
    private LocalDate day;

    // Quantities
    private Double grains;
    private Double veggie;
    private Double dairy;
    private Double protein;

}
