package com.zeylin.runoncoffee.controllers.dictionary;

import com.zeylin.runoncoffee.dto.dictionary.BaseFoodItemDto;
import com.zeylin.runoncoffee.dto.dictionary.BaseFoodItemWithServingDto;
import com.zeylin.runoncoffee.models.dictionary.base.BaseFoodItem;
import com.zeylin.runoncoffee.services.dictionary.DairyService;
import com.zeylin.runoncoffee.services.dictionary.GrainsService;
import com.zeylin.runoncoffee.services.dictionary.PlantService;
import com.zeylin.runoncoffee.services.dictionary.ProteinService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Food items dictionaries.
 */
@RestController
@RequestMapping("/dictionary/item")
public class FoodItemController {

    private GrainsService grainsService;
    private DairyService dairyService;
    private ProteinService proteinService;
    private PlantService plantService;
    private ModelMapper modelMapper;

    public FoodItemController(GrainsService grainsService,
                              DairyService dairyService,
                              ProteinService proteinService,
                              PlantService plantService,
                              ModelMapper modelMapper
    ) {
        this.grainsService = grainsService;
        this.dairyService = dairyService;
        this.proteinService = proteinService;
        this.plantService = plantService;
        this.modelMapper = modelMapper;
    }

    /**
     * Get grains dictionary.
     */
    @GetMapping(value = "/grains", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BaseFoodItemWithServingDto>> getGrainItems() {
        return ResponseEntity.ok(grainsService.getAll()
                .stream()
                .map(item -> convertToBaseFoodItemWithServingDto(item))
                .collect(Collectors.toList()));
    }

    /**
     * Get plants dictionary.
     */
    @GetMapping(value = "/plants", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BaseFoodItemDto>> getPlantItems() {
        return ResponseEntity.ok(plantService.getAll()
                .stream()
                .map(item -> convertToBaseFoodItemDto(item))
                .collect(Collectors.toList()));
    }

    /**
     * Get dairy dictionary.
     */
    @GetMapping(value = "/dairy", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BaseFoodItemWithServingDto>> getDairyItems() {
        return ResponseEntity.ok(dairyService.getAll()
                .stream()
                .map(item -> convertToBaseFoodItemWithServingDto(item))
                .collect(Collectors.toList()));
    }

    /**
     * Get protein dictionary.
     */
    @GetMapping(value = "/protein", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BaseFoodItemWithServingDto>> getProteinItems() {
        return ResponseEntity.ok(proteinService.getAll()
                .stream()
                .map(item -> convertToBaseFoodItemWithServingDto(item))
                .collect(Collectors.toList()));
    }

    private BaseFoodItemDto convertToBaseFoodItemDto(BaseFoodItem item) {
        return modelMapper.map(item, BaseFoodItemDto.class);
    }

    private BaseFoodItemWithServingDto convertToBaseFoodItemWithServingDto(BaseFoodItem item) {
        return modelMapper.map(item, BaseFoodItemWithServingDto.class);
    }

}
