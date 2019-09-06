package com.zeylin.runoncoffee.models.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "dictionary")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FoodGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 2)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 9)
    private AgeGroup ageGroup;

    // Recommended number of servings
    private int grains;
    private int veggie;
    private int dairy;
    private int protein;

    public enum Gender {
        F,
        M,
        NB,
        O
    }

    public enum AgeGroup {
        ADULT,
        FIFTYPLUS
    }

    public static FoodGuide getDefault() {
        return FoodGuide.builder()
                .gender(Gender.O)
                .ageGroup(AgeGroup.ADULT)
                .grains(8)
                .veggie(7)
                .dairy(2)
                .protein(2)
                .build();
    }

}
