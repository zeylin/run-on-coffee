package com.zeylin.runoncoffee.repositories.dictionary;

import com.zeylin.runoncoffee.models.dictionary.Dairy;
import com.zeylin.runoncoffee.repositories.dictionary.base.BaseFoodItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DairyRepository extends BaseFoodItemRepository<Dairy> {
}
