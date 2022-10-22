package com.edu.icesi.conceptsreview.controller;

import com.edu.icesi.conceptsreview.api.AnimalsAPI;
import com.edu.icesi.conceptsreview.dto.*;
import com.edu.icesi.conceptsreview.error.exception.AnimalError;
import com.edu.icesi.conceptsreview.error.exception.AnimalException;
import com.edu.icesi.conceptsreview.mapper.AnimalMapper;
import com.edu.icesi.conceptsreview.service.AnimalService;
import com.edu.icesi.conceptsreview.service.impl.AnimalServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.edu.icesi.conceptsreview.constant.AnimalsErrorCodes.*;

@RestController
@AllArgsConstructor
public class AnimalController implements AnimalsAPI {

    private AnimalServiceImpl animalServiceImpl;
    private AnimalMapper animalMapper;
    private final String regexForValidateNames = "^[a-zA-Z\\s]*$";

    @Override
    public AnimalDTO createAnimal(AnimalParentsDTO animalParentsDTO) {
        verifyNameLength(animalParentsDTO.getName());
        verifyNameContent(animalParentsDTO.getName());
        validateDateOfEntry(animalParentsDTO.getArriveDate());
        return animalMapper.fromAnimalToAnimalDTO(
                animalServiceImpl.createAnimal(animalMapper.fromAnimalParentsDTOtoAnimal(animalParentsDTO)));
    }

    @Override
    public List<AnimalDTO> getAnimals() {
        return animalServiceImpl.getAnimals().stream().map(animalMapper::fromAnimalToAnimalDTO).collect(Collectors.toList());
    }

    @Override
    public AnimalParentsObjectDTO getAnimal(UUID animalId) {
        return animalServiceImpl.getAnimalWithParents(animalId);
    }
    private void verifyNameLength(String name) {
        if(name.length() > 120) {
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_01, CODE_01.getMessage()));
        }
    }
    private void verifyNameContent(String name) {
        if(!name.matches(regexForValidateNames)) {
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(CODE_05, CODE_05.getMessage()));
        }
    }
    private void validateDateOfEntry(LocalDateTime dateArrive) {
        if(dateArrive.isAfter(LocalDateTime.now())) {
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(CODE_06, CODE_06.getMessage()));
        }
    }

}
