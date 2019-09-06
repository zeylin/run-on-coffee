package com.zeylin.runoncoffee.services.dictionary;

import com.zeylin.runoncoffee.models.dictionary.Grains;
import com.zeylin.runoncoffee.repositories.dictionary.GrainsRepository;
import com.zeylin.runoncoffee.services.dictionary.base.BaseFoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrainsService extends BaseFoodItemService<Grains> {

    @Autowired
    public GrainsService(GrainsRepository grainsRepository) {
        super(grainsRepository);
    }

}
