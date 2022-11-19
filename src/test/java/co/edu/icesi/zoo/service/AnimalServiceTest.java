package co.edu.icesi.zoo.service;

import co.edu.icesi.zoo.constant.AnimalErrorCode;
import co.edu.icesi.zoo.constant.AnimalTestConstants;
import co.edu.icesi.zoo.constant.BurmesePython;
import co.edu.icesi.zoo.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoo.error.exception.AnimalException;
import co.edu.icesi.zoo.mapper.AnimalMapper;
import co.edu.icesi.zoo.mapper.AnimalMapperImpl;
import co.edu.icesi.zoo.model.Animal;
import co.edu.icesi.zoo.repository.AnimalRepository;
import co.edu.icesi.zoo.service.impl.AnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AnimalServiceTest {

    private AnimalRepository animalRepository;
    private AnimalService animalService;
    private AnimalMapper animalMapper;
    private Animal dummyAnimal;

    private Animal fatherAnimal;
    private Animal motherAnimal;


    public void setupScenery1() {
        //A random python with valid average characteristics and no parents
        dummyAnimal = new Animal(UUID.fromString(AnimalTestConstants.ANIMAL_TEST_UUID), AnimalTestConstants.ANIMAL_TEST_NAME, BurmesePython.MALE, BurmesePython.MAX_WEIGHT / 2, BurmesePython.MAX_AGE / 2, BurmesePython.MAX_HEIGHT / 2, LocalDateTime.parse(AnimalTestConstants.ANIMAL_TEST_ARRIVAL_DATE), null, null);
        fatherAnimal = new Animal(UUID.randomUUID(), "Linux",  BurmesePython.MALE, BurmesePython.MAX_WEIGHT / 3, BurmesePython.MAX_AGE / 3, BurmesePython.MAX_HEIGHT / 3, LocalDateTime.parse(AnimalTestConstants.ANIMAL_TEST_ARRIVAL_DATE), null, null );
        motherAnimal =  new Animal(UUID.randomUUID(), "Godzilla",  BurmesePython.FEMALE, BurmesePython.MAX_WEIGHT / 3, BurmesePython.MAX_AGE / 3, BurmesePython.MAX_HEIGHT / 3, LocalDateTime.parse(AnimalTestConstants.ANIMAL_TEST_ARRIVAL_DATE), null, null );
    }

    @BeforeEach
    private void init(){
        animalMapper = new AnimalMapperImpl();
        animalRepository = mock(AnimalRepository.class);
        animalService = new AnimalServiceImpl(animalRepository, animalMapper);
    }

    @Test
    public void createValidAnimalWithParentsTest(){

        setupScenery1();
        configParents();

        animalService.createAnimal(dummyAnimal);
        verify(animalRepository, times(1)).findAll();
        verify(animalRepository, times(1)).findById(motherAnimal.getAnimalId());
        verify(animalRepository, times(1)).findById(fatherAnimal.getAnimalId());
        verify(animalRepository, times(1)).save(dummyAnimal);

    }

    @Test
    public void createAnimalRepeatedNameTest(){

        setupScenery1();
        fatherAnimal.setName(dummyAnimal.getName());

        when(animalRepository.findAll()).thenReturn(List.of(fatherAnimal));
        verifyAnimalException( AnimalErrorCode.CODE_04);

    }

    @Test
    public void getNotExistingAnimal(){

        setupScenery1();
        when(animalRepository.findById(dummyAnimal.getAnimalId())).thenReturn(Optional.empty());
        AnimalWithParentsDTO nullAnimal = animalService.getAnimalWithParents(dummyAnimal.getName());
        assertNull(nullAnimal);

    }

    @Test
    public void getAnimalWithParentsTest(){

        setupScenery1();
        configParents();

        AnimalWithParentsDTO animalWithParentsDTO = animalMapper.fromAnimalToWithParents(dummyAnimal, motherAnimal, fatherAnimal);
        when(animalRepository.findAll()).thenReturn(List.of(fatherAnimal, dummyAnimal, motherAnimal));
        AnimalWithParentsDTO response = animalService.getAnimalWithParents(dummyAnimal.getName());

        assertEquals(animalWithParentsDTO, response);
        verify(animalRepository, times(1)).findById(motherAnimal.getAnimalId());
        verify(animalRepository, times(1)).findById(fatherAnimal.getAnimalId());
        verify(animalRepository, times(1)).findAll();


    }

    @Test
    public void wrongFatherSexTest(){

        setupScenery1();
        fatherAnimal.setSex(BurmesePython.FEMALE);
        dummyAnimal.setFatherId(fatherAnimal.getAnimalId());

        when(animalRepository.findById(fatherAnimal.getAnimalId())).thenReturn(Optional.of(fatherAnimal));
        verifyAnimalException( AnimalErrorCode.CODE_05_02);

    }

    @Test
    public void wrongMotherSexTest(){

        setupScenery1();
        motherAnimal.setSex(BurmesePython.MALE);
        dummyAnimal.setMotherId(motherAnimal.getAnimalId());

        when(animalRepository.findById(motherAnimal.getAnimalId())).thenReturn(Optional.of(motherAnimal));
        verifyAnimalException( AnimalErrorCode.CODE_05_01);

    }


    /*
     * UTILS
     */
    public void verifyAnimalException(AnimalErrorCode correctCode) {

        // Check if the corresponding exception is thrown when we are trying to create an animal
        // containing an invalid attribute
        try {
            animalService.createAnimal(dummyAnimal);
            fail();
        } catch (AnimalException exception) {

            assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
            assertNotNull(exception.getError());
            assertEquals(correctCode.getMessage(), exception.getError().getMessage());
            assertEquals(correctCode, exception.getError().getCode());

        }

    }

    public void configParents(){

        dummyAnimal.setFatherId(fatherAnimal.getAnimalId());
        dummyAnimal.setMotherId(motherAnimal.getAnimalId());
        when(animalRepository.findById(fatherAnimal.getAnimalId())).thenReturn(Optional.of(fatherAnimal));
        when(animalRepository.findById(motherAnimal.getAnimalId())).thenReturn(Optional.of(motherAnimal));

    }




}
