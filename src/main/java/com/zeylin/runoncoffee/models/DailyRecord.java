package com.zeylin.runoncoffee.models;

import com.zeylin.runoncoffee.utils.LocalDateAttributeConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "DAILY_RECORDS")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate day; // record day

    // Quantities
    private Integer grains;
    private Integer veggie;
    private Integer dairy;
    private Integer protein;

}
