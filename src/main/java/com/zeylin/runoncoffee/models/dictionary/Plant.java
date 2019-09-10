package com.zeylin.runoncoffee.models.dictionary;

import com.zeylin.runoncoffee.models.dictionary.base.BaseFoodItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Plant food items dictionary.
 */
@Entity
@Table(schema = "dictionary")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Plant extends BaseFoodItem {
}
