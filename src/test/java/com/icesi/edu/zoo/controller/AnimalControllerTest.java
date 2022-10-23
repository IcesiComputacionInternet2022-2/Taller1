package com.icesi.edu.zoo.controller;

import com.icesi.edu.zoo.constant.AnimalErrorCode;
import com.icesi.edu.zoo.dto.AnimalDTO;
import com.icesi.edu.zoo.error.exception.AnimalError;
import com.icesi.edu.zoo.error.exception.AnimalException;
import com.icesi.edu.zoo.mapper.AnimalMapper;
import com.icesi.edu.zoo.service.AnimalService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnimalControllerTest {

    private AnimalController animalController;
    private AnimalService animalService;
    private AnimalMapper animalMapper;

    private AnimalDTO toAdd;

    @BeforeEach
    private void init() {
        animalService = mock(AnimalService.class);
        animalMapper = mock(AnimalMapper.class);
        animalController = new AnimalController(animalService, animalMapper);
    }

    private void createCorrectAnimal1() {
        toAdd = new AnimalDTO(null, "stitch", null, null, 'M', 13, 7, 110, new Date());
    }

    private void createCorrectAnimal2() {
        toAdd = new AnimalDTO(null, "condor herido", null, null, 'H', 12, 8, 110, new Date());
    }

    private void createAnimalWithInvalidName1() {
        toAdd = new AnimalDTO(null, "", null, null, 'H', 12, 3, 110, new Date());
    }

    private void createAnimalWithInvalidName2() {
        toAdd = new AnimalDTO(null, "abc123", null, null, 'H', 14, 5, 111, new Date());
    }

    private void createAnimalWithInvalidName3() {
        String name = StringUtils.repeat('a', 121);
        toAdd = new AnimalDTO(null, name, null, null, 'H', 13, 3, 111, new Date());
    }

    private void createAnimalWithInvalidHeight() {
        toAdd = new AnimalDTO(null, "otro condor", null, null, 'M', 12, 6, 200, new Date());
    }

    private void createAnimalWithInvalidWeight() {
        toAdd = new AnimalDTO(null, "cesar", null, null, 'M', 120, 6, 111, new Date());
    }

    private void createAnimalWithInvalidSex() {
        toAdd = new AnimalDTO(null, "condorito", null, null, 'j', 13, 8, 111, new Date());
    }

    private void createAnimalWithDateGreaterThanCurrent() {
        createCorrectAnimal1();
        Calendar currentDatePlusFiveYears = Calendar.getInstance();
        currentDatePlusFiveYears.add(Calendar.YEAR, 5);
        Date futureDate = currentDatePlusFiveYears.getTime();
        toAdd.setArrivalDate(futureDate);
    }

    private void verifyExceptionAttributes(AnimalException e, HttpStatus httpStatus, AnimalErrorCode animalErrorCode) {
        assertEquals(httpStatus, e.getHttpStatus());
        assertNotNull(e.getAnimalError());
        AnimalError animalError = e.getAnimalError();
        assertEquals(animalErrorCode, animalError.getCode());
        assertEquals(animalErrorCode.getMessage(), animalError.getMessage());
    }

    @Test
    public void testAddCorrectAnimal1() {
        createCorrectAnimal2();
        when(animalMapper.fromAnimal(any())).thenReturn(toAdd);
        AnimalDTO res = animalController.createAnimal(toAdd);
        verify(animalService, times(1)).createAnimal(any());
        assertNotNull(res);
        assertEquals(res, toAdd);
    }

    @Test
    public void testAddCorrectAnimal2() {
        createCorrectAnimal2();
        when(animalMapper.fromAnimal(any())).thenReturn(toAdd);
        AnimalDTO res = animalController.createAnimal(toAdd);
        verify(animalService, times(1)).createAnimal(any());
        assertNotNull(res);
        assertEquals(res, toAdd);
    }

    @Test
    public void testAddEmptyAnimal() {
        try {
            animalController.createAnimal(null);
            fail();
        } catch(AnimalException e) {
            verify(animalService, times(0)).createAnimal(any());
            verifyExceptionAttributes(e, HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_01);
        }
    }

    @Test
    public void testAddAnimalWithInvalidHeight() {
        createAnimalWithInvalidHeight();
        try {
            animalController.createAnimal(toAdd);
            fail();
        } catch(AnimalException e) {
            verifyExceptionAttributes(e, HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_10);
        }
    }

    @Test
    public void testAddAnimalWithInvalidWeight() {
        createAnimalWithInvalidWeight();
        try {
            animalController.createAnimal(toAdd);
            fail();
        } catch(AnimalException e) {
            verifyExceptionAttributes(e, HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_10);
        }
    }

    @Test
    public void testAddAnimalWithInvalidName1() {
        createAnimalWithInvalidName1();
        try{
            animalController.createAnimal(toAdd);
            fail();
        } catch (AnimalException e) {
            verify(animalService, times(0)).createAnimal(any());
            verifyExceptionAttributes(e, HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_02);
        }
    }

    @Test
    public void testAddAnimalWithInvalidName2() {
        createAnimalWithInvalidName2();
        try{
            animalController.createAnimal(toAdd);
            fail();
        } catch (AnimalException e) {
            verify(animalService, times(0)).createAnimal(any());
            verifyExceptionAttributes(e, HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_02);
        }
    }

    @Test
    public void testAddAnimalWithInvalidName3() {
        createAnimalWithInvalidName3();
        try {
            animalController.createAnimal(toAdd);
            fail();
        } catch(AnimalException e) {
            verify(animalService, times(0)).createAnimal(any());
            verifyExceptionAttributes(e, HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_02);
        }
    }

    @Test
    public void testAddAnimalWithInvalidSex() {
        createAnimalWithInvalidSex();
        try{
            animalController.createAnimal(toAdd);
            fail();
        } catch (AnimalException e) {
            verify(animalService, times(0)).createAnimal(any());
            verifyExceptionAttributes(e, HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_03);
        }
    }

    @Test
    public void testAddAnimalWithDateGreaterThanCurrent() {
        createAnimalWithDateGreaterThanCurrent();
        try {
            animalController.createAnimal(toAdd);
            fail();
        } catch(AnimalException e) {
            verify(animalService, times(0)).createAnimal(any());
            verifyExceptionAttributes(e, HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_04);
        }
    }

    @Test
    public void testGetAnimalWithNoParents() {
        animalController.getAnimal(any());
        verify(animalService, times(1)).getAnimal(any());
    }

    @Test
    public void testGetAnimals() {
        animalController.getAnimals();
        verify(animalService, times(1)).getAnimals();
    }

}
