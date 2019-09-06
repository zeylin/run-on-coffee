package com.zeylin.runoncoffee.repositories.dictionary;

import com.zeylin.runoncoffee.models.dictionary.Protein;
import com.zeylin.runoncoffee.repositories.dictionary.base.BaseFoodItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProteinRepository extends BaseFoodItemRepository<Protein> {
}
