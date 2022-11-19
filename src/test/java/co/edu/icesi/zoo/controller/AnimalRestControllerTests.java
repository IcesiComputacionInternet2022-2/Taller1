package co.edu.icesi.zoo.controller;

import co.edu.icesi.zoo.constant.AnimalErrorCode;
import co.edu.icesi.zoo.constant.AnimalTestConstants;
import co.edu.icesi.zoo.constant.BurmesePython;
import co.edu.icesi.zoo.controller.rest.AnimalRestController;
import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.error.exception.AnimalException;
import co.edu.icesi.zoo.mapper.AnimalMapper;
import co.edu.icesi.zoo.mapper.AnimalMapperImpl;
import co.edu.icesi.zoo.service.AnimalService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnimalRestControllerTests {

    private AnimalRestController animalRestController;
    private AnimalMapper animalMapper;
    private AnimalService animalService;

    private AnimalDTO dummyAnimal;

    public void setupScenery1() {
        //A random python with valid average characteristics and no parents
        dummyAnimal = new AnimalDTO(AnimalTestConstants.ANIMAL_TEST_UUID, AnimalTestConstants.ANIMAL_TEST_NAME, BurmesePython.MALE, BurmesePython.MAX_WEIGHT / 2, BurmesePython.MAX_AGE / 2, BurmesePython.MAX_HEIGHT / 2, AnimalTestConstants.ANIMAL_TEST_ARRIVAL_DATE, null, null);
    }

    @BeforeEach
    public void init() {

        animalMapper = new AnimalMapperImpl();
        animalService = mock(AnimalService.class);
        animalRestController = new AnimalRestController(animalService, animalMapper);

    }




    @Test
    public void createAnimalTest(){

        setupScenery1();

        try{

            animalRestController.createAnimal(dummyAnimal);
            verify(animalService, times(1)).createAnimal(any());

        }catch (AnimalException exception){
            fail();
        }


    }

    @Test
    public void getAnimalsTest(){

        animalRestController.getAnimals();
        verify(animalService, times(1)).getAnimals();

    }

    @Test
    public void getAnimalWithParentsTest(){

        animalRestController.getAnimalWithParents(any());
        verify(animalService, times(1)).getAnimalWithParents(any());

    }

    @Test
    public void wrongNameLengthsTest() {

        int[] nameLengths = {0, 121};

        for (int nameLength : nameLengths) {

            setupScenery1();

            dummyAnimal.setName(generateRandomOnlyLettersString(nameLength));
            verifyAnimalException( AnimalErrorCode.CODE_01);

        }

    }
    // Check https://www.baeldung.com/parameterized-tests-junit-5 (JUnit 5 Parameterized Tests) to no
    @Test
    public void invalidNameCharactersTest() {

        //Testing invalid characters
        String[] invalidNames = {"-Burmese", "_Burmese", ".Burmese"};

        for (String invalidName : invalidNames) {

            setupScenery1();

            dummyAnimal.setName(generateRandomOnlyLettersString(20) + invalidName);
            verifyAnimalException( AnimalErrorCode.CODE_01);

        }

    }

    @Test
    public void futureArrivalDateTest() {

        //No animals can be registered after the current date
        //------------- Second, minute, hour, day, month and year in ms to get the future time
        long[] times = {1000L, 60000L, 3600000L, 86400000L, 2628000000L, 31540000000L};
        long currentTime = System.currentTimeMillis();
        ZoneId zoneId = ZoneId.systemDefault();

        for (long time : times) {

            setupScenery1();
            long futureArrivalTime = currentTime + time;
            LocalDateTime arrivalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(futureArrivalTime), zoneId);

            dummyAnimal.setArrivalDate(arrivalDate.toString());
            verifyAnimalException( AnimalErrorCode.CODE_02_01);

        }
    }

    @Test
    public void validatePythonWrongHeightTest(){

        double[] invalidHeights = {-1, -0.1, 0, 8.01, 9};
        validatePythonCharacteristic(invalidHeights, BurmesePython.HEIGHT);

    }

    @Test
    public void validatePythonWrongWeightTest(){

        double[] invalidWeights = {-1, -0.1, 0, 180.01, 182};
        validatePythonCharacteristic(invalidWeights, BurmesePython.WEIGHT);

    }

    @Test
    public void validatePythonWrongAgeTest(){

        double[] invalidAges = {-1, -0.1, 0, 30.01, 31};
        validatePythonCharacteristic(invalidAges, BurmesePython.AGE);

    }

    @Test
    public void validateNotParseableUUIDTest(){

        String[] invalidsUUIDs = {"","1143876087", "a00347293", "5b666754-e217-4775-9c95-352ebb0673cx"};
        for(String invalidUUID : invalidsUUIDs){

            // We only need to check the parents UUIDs as these are the only ones manually set by the user
            setupScenery1();
            dummyAnimal.setMotherId(invalidUUID);
            verifyAnimalException( AnimalErrorCode.CODE_06);
            setupScenery1();
            dummyAnimal.setFatherId(invalidUUID);
            verifyAnimalException( AnimalErrorCode.CODE_06);
            

        }
    }

    @Test
    public void validateWrongDateFormat(){
        setupScenery1();
        dummyAnimal.setArrivalDate("inavlid_date");
        verifyAnimalException( AnimalErrorCode.CODE_02_02);
    }

    /*
     * UTILS
     */
    public void validatePythonCharacteristic(double[] invalidCharacteristics, char characteristic) {

        for (double invalidCharacteristic : invalidCharacteristics) {

            setupScenery1();

            if (characteristic == BurmesePython.AGE) dummyAnimal.setAge(invalidCharacteristic);
            if (characteristic == BurmesePython.WEIGHT) dummyAnimal.setWeight(invalidCharacteristic);
            if (characteristic == BurmesePython.HEIGHT) dummyAnimal.setHeight(invalidCharacteristic);


            verifyAnimalException( AnimalErrorCode.CODE_03);


        }

    }

    public void verifyAnimalException( AnimalErrorCode correctCode) {

        // Check if the corresponding exception is thrown when we are trying to create an animal
        // containing an invalid attribute
        try {
            animalRestController.createAnimal(dummyAnimal);
            fail();
        } catch (AnimalException exception) {

            assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
            assertNotNull(exception.getError());
            assertEquals(correctCode.getMessage(), exception.getError().getMessage());
            assertEquals(correctCode, exception.getError().getCode());

        }

    }

    public String generateRandomOnlyLettersString(int length) {

        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);

    }


}
