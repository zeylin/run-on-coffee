package com.zeylin.runoncoffee.models.dictionary;

import com.zeylin.runoncoffee.models.dictionary.base.BaseFoodItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Protein food items dictionary.
 */
@Entity
@Table(schema = "dictionary")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Protein extends BaseFoodItem {

    private String servingName;

}
