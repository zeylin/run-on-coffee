package com.zeylin.runoncoffee.repositories.dictionary;

import com.zeylin.runoncoffee.models.dictionary.FoodGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodGuideRepository extends JpaRepository<FoodGuide, Long> {
}
