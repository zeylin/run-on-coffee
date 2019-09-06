package com.zeylin.runoncoffee.services.dictionary;

import com.zeylin.runoncoffee.models.dictionary.Dairy;
import com.zeylin.runoncoffee.repositories.dictionary.DairyRepository;
import com.zeylin.runoncoffee.services.dictionary.base.BaseFoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DairyService extends BaseFoodItemService<Dairy> {

    @Autowired
    public DairyService(DairyRepository dairyRepository) {
        super(dairyRepository);
    }

}
