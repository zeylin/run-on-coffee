package com.zeylin.runoncoffee.services.dictionary.base;

import com.zeylin.runoncoffee.models.dictionary.base.BaseFoodItem;
import com.zeylin.runoncoffee.repositories.dictionary.base.BaseFoodItemRepository;

import java.util.List;
import java.util.Optional;

/**
 * Base service for food item dictionaries.
 */
public class BaseFoodItemService<E extends BaseFoodItem> {

    private BaseFoodItemRepository<E> baseRepository;

    public BaseFoodItemService(BaseFoodItemRepository<E> baseRepository) {
        this.baseRepository = baseRepository;
    }

    public List<E> getAll() {
        return baseRepository.findAll();
    }

    public Optional<E> getById(Long id) {
        return baseRepository.findById(id);
    }

}
