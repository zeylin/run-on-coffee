package com.zeylin.runoncoffee.services.dictionary;

import com.zeylin.runoncoffee.models.dictionary.Protein;
import com.zeylin.runoncoffee.repositories.dictionary.ProteinRepository;
import com.zeylin.runoncoffee.services.dictionary.base.BaseFoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProteinService extends BaseFoodItemService<Protein> {

    @Autowired
    public ProteinService(ProteinRepository proteinRepository) {
        super(proteinRepository);
    }

}
