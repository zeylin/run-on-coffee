package com.zeylin.runoncoffee.models;

import com.zeylin.runoncoffee.models.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "DAILY_RECORDS")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DailyRecord extends BaseEntity {

    @Column(name = "day", columnDefinition = "DATE")
    private LocalDate day;

    // Quantities
    private Integer grains;
    private Integer veggie;
    private Integer dairy;
    private Integer protein;

}
