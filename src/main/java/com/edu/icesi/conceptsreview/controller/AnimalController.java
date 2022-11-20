package com.edu.icesi.conceptsreview.controller;

import com.edu.icesi.conceptsreview.api.AnimalsAPI;
import com.edu.icesi.conceptsreview.dto.*;
import com.edu.icesi.conceptsreview.error.exception.AnimalError;
import com.edu.icesi.conceptsreview.error.exception.AnimalException;
import com.edu.icesi.conceptsreview.mapper.AnimalMapper;
import com.edu.icesi.conceptsreview.model.Animal;
import com.edu.icesi.conceptsreview.service.AnimalService;
import com.edu.icesi.conceptsreview.service.impl.AnimalServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.edu.icesi.conceptsreview.constant.AnimalsErrorCodes.*;

@Controller
@AllArgsConstructor
public class AnimalController implements AnimalsAPI {

    private AnimalServiceImpl animalServiceImpl;
    private AnimalMapper animalMapper;
    private final String regexForValidateNames = "^[a-zA-Z\\s]*$";

    @Override
    public String getIndex(Model model) {
        return "index";
    }

    @Override
    public String createAnimal(AnimalParentsDTO animalParentsDTO, Model model) {
        String message = "";
        UUID animalid = null;
        boolean createdSuccessfully;
        try {
            verifyNameLength(animalParentsDTO.getName());
            verifyNameContent(animalParentsDTO.getName());
            validateDateOfEntry(animalParentsDTO.getArriveDate());
            AnimalDTO animal = animalMapper.fromAnimalToAnimalDTO(
                    animalServiceImpl.createAnimal(animalMapper.fromAnimalParentsDTOtoAnimal(animalParentsDTO)));
            message = "Animal created Successfully";
            animalid = animal.getId();
            createdSuccessfully = true;
        } catch (AnimalException e) {
            createdSuccessfully = false;
            AnimalError error = e.getError();
            message = error.getMessage();
        } catch (NullPointerException np) {
            createdSuccessfully = false;
            message = "Fill all the fields with valid information";
        }
        model.addAttribute("animalCreationResponse", true);
        model.addAttribute("createdSuccessfully", createdSuccessfully);
        model.addAttribute("message", message);
        model.addAttribute("animalCreatedId", animalid);
        return "createAnimals";
    }
    @Override
    public String getCreateAnimalPage(Model model) {
        model.addAttribute("animalParentsDTO", new AnimalParentsDTO());
        model.addAttribute("createdSuccessfully", false);
        model.addAttribute("animalCreationResponse", false);
        model.addAttribute("message", "");
        model.addAttribute("animalCreatedId", "");
        return "createAnimals";
    }

    @Override
    public String getAnimals(Model model) {
        List<AnimalDTO> animals = animalServiceImpl.getAnimals().stream().map(animalMapper::fromAnimalToAnimalDTO).collect(Collectors.toList());
        model.addAttribute("animals", animals);
        model.addAttribute("title", "List of animals");
        return "animals";
    }

    @Override
    public String getAnimal(UUID animalId, Model model) {
        AnimalParentsObjectDTO animalFinded = null;
        boolean animalFounded = false;
        String message = "Animal founded";
        try {
            animalFinded = animalServiceImpl.getAnimalWithParents(animalId);
            animalFounded = true;
            if (animalFinded.getFather() != null) {
                model.addAttribute("fatherPresent", true);
                model.addAttribute("father", animalFinded.getFather());
            }
            if(animalFinded.getMother() != null) {
                model.addAttribute("motherPresent", true);
                model.addAttribute("mother", animalFinded.getMother());
            }
        } catch (AnimalException e) {
            message = "It doesn't exist an animal with that ID";
        }
        model.addAttribute("animalPresent", animalFounded);
        model.addAttribute("animal", animalFinded);
        model.addAttribute("message", message);
        return "searchAnimal";
    }

    @Override
    public String getSearchAnimalPage(Model model) {
        model.addAttribute("animalPresent", false);
        model.addAttribute("message","Write a valid UUID to search it");
        return "searchAnimal";
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
    private void validateDateOfEntry(Date dateArrive) {
        if(dateArrive.before(new Date())) {
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(CODE_06, CODE_06.getMessage()));
        }
    }

}
