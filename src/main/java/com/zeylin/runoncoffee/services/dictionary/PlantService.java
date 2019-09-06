package com.zeylin.runoncoffee.services.dictionary;

import com.zeylin.runoncoffee.models.dictionary.Plant;
import com.zeylin.runoncoffee.repositories.dictionary.PlantRepository;
import com.zeylin.runoncoffee.services.dictionary.base.BaseFoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantService extends BaseFoodItemService<Plant> {

    @Autowired
    public PlantService(PlantRepository plantRepository) {
        super(plantRepository);
    }
}
