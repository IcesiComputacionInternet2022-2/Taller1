package com.edu.icesi.conceptsreview;

import com.edu.icesi.conceptsreview.controller.AnimalController;
import com.edu.icesi.conceptsreview.dto.AnimalDTO;
import com.edu.icesi.conceptsreview.dto.AnimalParentsDTO;
import com.edu.icesi.conceptsreview.dto.AnimalParentsObjectDTO;
import com.edu.icesi.conceptsreview.error.exception.AnimalError;
import com.edu.icesi.conceptsreview.error.exception.AnimalException;
import com.edu.icesi.conceptsreview.mapper.AnimalMapperImpl;
import com.edu.icesi.conceptsreview.model.Animal;
import com.edu.icesi.conceptsreview.service.impl.AnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnimalTestsController {

    private AnimalController animalController;
    private AnimalServiceImpl animalService;
    private AnimalMapperImpl animalMapper;
    private AnimalParentsDTO animalParentsDTOForCreation = new AnimalParentsDTO(
        UUID.fromString("7007674c-c765-49b5-8d6a-d22cd5376fb5"),
            "NutriaPadre", "M", Float.parseFloat("8.4"),
            Integer.parseInt("8"),
            Float.parseFloat("67.5"),
            new Date(),
            null, null
    );

    @BeforeEach
    public void init()
    {
        animalMapper = new AnimalMapperImpl();
        animalService = mock(AnimalServiceImpl.class);
        animalController = new AnimalController(animalService, animalMapper);
    }

    @Test
    public void createAnimalNameBadLength()
    {
        animalParentsDTOForCreation.setName("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG" +
                "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
        try {
            animalController.createAnimal(animalParentsDTOForCreation);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("The name length exceeds the maximum allowed", error.getMessage());
            assertEquals("CODE_01", error.getCode().name());
        }
    }

    @Test
    public void createAnimalBadContent()
    {
        animalParentsDTOForCreation.setName("@$%Animal");
        try {
            animalController.createAnimal(animalParentsDTOForCreation);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("The name must contain only letters and spaces", error.getMessage());
            assertEquals("CODE_05", error.getCode().name());
        }
    }

    @Test
    public void createAnimalBadDateOfEntry()
    {
        animalParentsDTOForCreation.setArriveDate(LocalDateTime.now().plusDays(10));
        try {
            animalController.createAnimal(animalParentsDTOForCreation);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("The arrive date cannot be before the actual date", error.getMessage());
            assertEquals("CODE_06", error.getCode().name());
        }
    }

    @Test
    public void getAnimalsTest()
    {
        List<Animal> animalsForReturn = new ArrayList<Animal>();
        animalController.createAnimal(animalParentsDTOForCreation);
        animalsForReturn.add(animalMapper.fromAnimalParentsDTOtoAnimal(animalParentsDTOForCreation));
        animalParentsDTOForCreation.setName("SecondAnimal");
        animalController.createAnimal(animalParentsDTOForCreation);
        animalsForReturn.add(animalMapper.fromAnimalParentsDTOtoAnimal(animalParentsDTOForCreation));
        when(animalService.getAnimals()).thenReturn(animalsForReturn);
        List<AnimalDTO> animals = animalController.getAnimals();
        assertEquals(animals.size(), 2);
    }

    @Test
    public void getAnimalTest()
    {
        animalController.createAnimal(animalParentsDTOForCreation);
        when(animalService.getAnimalWithParents(animalParentsDTOForCreation.getId()))
                .thenReturn(animalMapper.fromAnimalParentsDTOtoAnimalParentsObjectDTO(animalParentsDTOForCreation));
        AnimalParentsObjectDTO animal = animalController.getAnimal(animalParentsDTOForCreation.getId());
        assertEquals(animal.getId(), animalParentsDTOForCreation.getId());
        assertEquals(animal.getName(), animalParentsDTOForCreation.getName());
    }

}
