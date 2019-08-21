package com.zeylin.runoncoffee.services.dictionary;

import com.zeylin.runoncoffee.dto.dictionary.FoodGuideDto;
import com.zeylin.runoncoffee.models.dictionary.FoodGuide;
import com.zeylin.runoncoffee.repositories.dictionary.FoodGuideRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodGuideService {

    private FoodGuideRepository foodGuideRepository;
    private ModelMapper modelMapper;

    @Autowired
    public FoodGuideService(FoodGuideRepository foodGuideRepository,
                            ModelMapper modelMapper) {
        this.foodGuideRepository = foodGuideRepository;
        this.modelMapper = modelMapper;
    }

    public Optional<FoodGuide> getById(Long id) {
        return foodGuideRepository.findById(id);
    }

    public List<FoodGuideDto> getAll() {
        List<FoodGuide> guides = foodGuideRepository.findAll();
        return guides.stream()
                .map(guide -> convertToDto(guide)).collect(Collectors.toList());
    }

    private FoodGuideDto convertToDto(FoodGuide record) {
        return modelMapper.map(record, FoodGuideDto.class);
    }

}
