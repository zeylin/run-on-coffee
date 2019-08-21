package com.zeylin.runoncoffee.controllers.dictionary;

import com.zeylin.runoncoffee.dto.dictionary.FoodGuideDto;
import com.zeylin.runoncoffee.services.dictionary.FoodGuideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dictionary/guide")
public class FoodGuideController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FoodGuideController.class);

    private FoodGuideService foodGuideService;

    @Autowired
    public FoodGuideController(FoodGuideService foodGuideService) {
        this.foodGuideService = foodGuideService;
    }

    /**
     * GET ALL
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FoodGuideDto>> getFoodGuides() {
        return ResponseEntity.ok(foodGuideService.getAll());
    }

}
