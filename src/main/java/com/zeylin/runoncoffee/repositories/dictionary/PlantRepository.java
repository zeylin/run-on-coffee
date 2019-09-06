package com.zeylin.runoncoffee.repositories.dictionary;

import com.zeylin.runoncoffee.models.dictionary.Plant;
import com.zeylin.runoncoffee.repositories.dictionary.base.BaseFoodItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends BaseFoodItemRepository<Plant> {
}
