package com.zeylin.runoncoffee.controllers.dictionary;

import com.zeylin.runoncoffee.models.dictionary.Dairy;
import com.zeylin.runoncoffee.models.dictionary.Grains;
import com.zeylin.runoncoffee.models.dictionary.Plant;
import com.zeylin.runoncoffee.models.dictionary.Protein;
import com.zeylin.runoncoffee.services.dictionary.DairyService;
import com.zeylin.runoncoffee.services.dictionary.GrainsService;
import com.zeylin.runoncoffee.services.dictionary.PlantService;
import com.zeylin.runoncoffee.services.dictionary.ProteinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dictionary/item")
public class FoodItemController {

    private GrainsService grainsService;
    private DairyService dairyService;
    private ProteinService proteinService;
    private PlantService plantService;

    public FoodItemController(GrainsService grainsService,
                              DairyService dairyService,
                              ProteinService proteinService,
                              PlantService plantService
    ) {
        this.grainsService = grainsService;
        this.dairyService = dairyService;
        this.proteinService = proteinService;
        this.plantService = plantService;
    }

    /**
     * Get grains dictionary.
     */
    @GetMapping(value = "/grains", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Grains>> getGrainItems() {
        return ResponseEntity.ok(grainsService.getAll());
    }

    /**
     * Get plants dictionary.
     */
    @GetMapping(value = "/plants", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Plant>> getPlantItems() {
        return ResponseEntity.ok(plantService.getAll());
    }

    /**
     * Get dairy dictionary.
     */
    @GetMapping(value = "/dairy", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Dairy>> getDairyItems() {
        return ResponseEntity.ok(dairyService.getAll());
    }

    /**
     * Get protein dictionary.
     */
    @GetMapping(value = "/protein", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Protein>> getProteinItems() {
        return ResponseEntity.ok(proteinService.getAll());
    }

}
