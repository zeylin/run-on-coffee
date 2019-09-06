package com.zeylin.runoncoffee.repositories.dictionary.base;

import com.zeylin.runoncoffee.models.dictionary.base.BaseFoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseFoodItemRepository<E extends BaseFoodItem> extends JpaRepository<E, Long> {
}
