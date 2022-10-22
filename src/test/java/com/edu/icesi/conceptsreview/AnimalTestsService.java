package com.edu.icesi.conceptsreview;

import com.edu.icesi.conceptsreview.dto.AnimalParentsObjectDTO;
import com.edu.icesi.conceptsreview.error.exception.AnimalError;
import com.edu.icesi.conceptsreview.error.exception.AnimalException;
import com.edu.icesi.conceptsreview.mapper.AnimalMapper;
import com.edu.icesi.conceptsreview.mapper.AnimalMapperImpl;
import com.edu.icesi.conceptsreview.model.Animal;
import com.edu.icesi.conceptsreview.repository.AnimalRepository;
import com.edu.icesi.conceptsreview.service.AnimalService;
import com.edu.icesi.conceptsreview.service.impl.AnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;

import static com.edu.icesi.conceptsreview.constant.GeneralConstantsForVerifications.MAX_LENGTH_CM;
import static com.edu.icesi.conceptsreview.constant.GeneralConstantsForVerifications.MAX_WEIGHT_KG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnimalTestsService {

    private AnimalRepository animalRepository;
    private AnimalMapper animalMapper;
    private AnimalService animalService;
    private Animal firstAnimalForTests;
    private Animal secondAnimalForTests;

    @BeforeEach
    public void init()
    {
        animalMapper = new AnimalMapperImpl();
        animalRepository = mock(AnimalRepository.class);
        animalService = new AnimalServiceImpl(animalRepository, animalMapper);
        firstAnimalForTests = new Animal( UUID.fromString("7007674c-c765-49b5-8d6a-d22cd5376fb5"),
                "NutriaPadre", "M", Float.parseFloat("8.4"),
                Integer.parseInt("8"),
                Float.parseFloat("67.5"),
                Calendar.getInstance().getTime(),
                null, null);
        secondAnimalForTests = new Animal( UUID.fromString("7007674c-c765-49b5-8d6a-d22cd5376fb5"),
                "NutriaMadre", "W", Float.parseFloat("8.4"),
                Integer.parseInt("8"),
                Float.parseFloat("67.5"),
                Calendar.getInstance().getTime(),
                null, null);
    }

    @Test
    public void getAnimalsTest()
    {
        List<Animal> animalsForMock = new ArrayList<>();
        animalsForMock.add(firstAnimalForTests);
        animalsForMock.add(secondAnimalForTests);
        when(animalRepository.findAll()).thenReturn(animalsForMock);
        List<Animal> animals = animalService.getAnimals();
        assertEquals(animals.size(), animalsForMock.size());
        assertEquals(animals.get(1).getName(), animalsForMock.get(1).getName());
    }

    @Test
    public void createAnimalExistingName()
    {
        List<Animal> animalsForMock = new ArrayList<>();
        animalsForMock.add(firstAnimalForTests);
        when(animalRepository.findAll()).thenReturn(animalsForMock);
        try {
            animalService.createAnimal(firstAnimalForTests);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("This animal name already exist", error.getMessage());
            assertEquals("CODE_03", error.getCode().name());
        }
    }

    @Test
    public void createAnimalNotExistingFatherId()
    {
        //when(animalRepository.findById(any())).thenReturn(null);
        try {
            firstAnimalForTests.setMotherId(UUID.fromString("7007674c-c765-49b5-8d6a-d22cd5376fb5"));
            animalService.createAnimal(firstAnimalForTests);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("This animal id doesn't exist", error.getMessage());
            assertEquals("CODE_02", error.getCode().name());
        }
    }

    @Test
    public void createAnimalBadParentGenre()
    {
        firstAnimalForTests.setFatherId(secondAnimalForTests.getId());
        when(animalRepository.findById(secondAnimalForTests.getId())).thenReturn(Optional.ofNullable(secondAnimalForTests));
        try {
            animalService.createAnimal(firstAnimalForTests);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("Animal wrong genre: father", error.getMessage());
            assertEquals("CODE_04", error.getCode().name());
        }
    }

    @Test
    public void createAnimalBadWeightRange()
    {
        List<Animal> animalsForMock = new ArrayList<>();
        when(animalRepository.findAll()).thenReturn(animalsForMock);
        firstAnimalForTests.setWeight(MAX_WEIGHT_KG + 15);
        try {
            animalService.createAnimal(firstAnimalForTests);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("The weight must be between the standards (Kg) 7 - 12", error.getMessage());
            assertEquals("CODE_07", error.getCode().name());
        }
    }

    @Test
    public void createAnimalBadHLengthRange()
    {
        List<Animal> animalsForMock = new ArrayList<>();
        when(animalRepository.findAll()).thenReturn(animalsForMock);
        firstAnimalForTests.setHeight(MAX_LENGTH_CM + 15);
        try {
            animalService.createAnimal(firstAnimalForTests);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("The length/height must be between the standards (Cm) 57 - 95", error.getMessage());
            assertEquals("CODE_08", error.getCode().name());
        }
    }

    @Test
    public void getAnimalWithParentsTest()
    {
        when(animalRepository.findById(firstAnimalForTests.getId())).thenReturn(Optional.ofNullable(firstAnimalForTests));
        AnimalParentsObjectDTO animalParentsObjectDTO = animalService.getAnimalWithParents(firstAnimalForTests.getId());
        assertEquals(animalParentsObjectDTO.getName(), firstAnimalForTests.getName());
    }


    public void verifyAnimalAlreadyExistsById()
    {
        List<Animal> animalsForMock = new ArrayList<>();
        firstAnimalForTests.setFatherId(secondAnimalForTests.getId());
        when(animalRepository.findById(firstAnimalForTests.getId())).thenReturn(null);
        when(animalRepository.findAll()).thenReturn(animalsForMock);
        try {
            animalService.createAnimal(firstAnimalForTests);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("This animal id doesn't exist", error.getMessage());
            assertEquals("CODE_02", error.getCode().name());
        }
    }

}
