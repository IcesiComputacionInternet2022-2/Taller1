package com.icesi.edu.zoo.service;

import com.icesi.edu.zoo.constant.AnimalErrorCode;
import com.icesi.edu.zoo.error.exception.AnimalError;
import com.icesi.edu.zoo.error.exception.AnimalException;
import com.icesi.edu.zoo.model.Animal;
import com.icesi.edu.zoo.repository.AnimalRepository;
import com.icesi.edu.zoo.service.impl.AnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnimalServiceTest {

    private AnimalService animalService;
    private AnimalRepository animalRepository;

    @BeforeEach
    private void init() {
        animalRepository = mock(AnimalRepository.class);
        animalService = new AnimalServiceImpl(animalRepository);
    }

    private Animal setUpCorrectAnimal() {
        UUID id = UUID.fromString("ba9dd985-ca3a-4d24-84d1-2405daea6a08");
        return new Animal(id, "condor herido", null, null, 'F', 13, 5, 100, new Date());
    }

    private void addAnimalToRepository(Animal toAdd) {
        when(animalRepository.save(toAdd)).thenReturn(toAdd);
    }

    @Test
    public void testAddCorrectAnimal() {
        Animal toAdd = setUpCorrectAnimal();
        when(animalRepository.save(any())).thenReturn(toAdd);
        assertEquals(toAdd, animalService.createAnimal(toAdd));
        verify(animalRepository, times(1)).save(any());
    }

    @Test
    public void testAddAnimalWithExistentName() {
        Animal existentName = setUpCorrectAnimal();
        when(animalRepository.findById(any())).thenReturn(Optional.of(existentName));
        UUID id = UUID.fromString("9b3266e1-176a-4b14-b0c8-f5dc96f52d8f");
        Animal toAdd = new Animal(id, existentName.getName(), null, null, 'M', 12, 6, 112, new Date());
        assertThrows(AnimalException.class, () -> animalService.createAnimal(toAdd));
        verify(animalRepository, times(0)).save(any());
    }

    @Test
    public void testAddAnimalWithInvalidHeight() {
        UUID id = UUID.fromString("9b3266e1-176a-4b14-b0c8-f5dc96f52d8f");
        Animal toAdd = new Animal(id, "otro condor", null, null, 'M', 12, 6, 200, new Date());
        try {
            animalService.createAnimal(toAdd);
            fail();
        } catch(AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getAnimalError());
            AnimalError animalError = e.getAnimalError();
            assertEquals(AnimalErrorCode.CODE_10.getMessage(), animalError.getMessage());
            assertEquals(AnimalErrorCode.CODE_10, animalError.getCode());
        }
    }

    @Test
    public void testAddAnimalWithInvalidWeight() {
        UUID id = UUID.fromString("d4b9e964-1427-4bf5-bb99-c1767f879157");
        Animal toAdd = new Animal(id, "ave ave", null, null, 'M', 100, 6, 113, new Date());
        try {
            animalService.createAnimal(toAdd);
            fail();
        } catch(AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getAnimalError());
            AnimalError animalError = e.getAnimalError();
            assertEquals(AnimalErrorCode.CODE_10.getMessage(), animalError.getMessage());
            assertEquals(AnimalErrorCode.CODE_10, animalError.getCode());
        }
    }

    private Animal createDummyAnimal1() {
        UUID id = UUID.fromString("ad6b7362-383d-46f6-ab13-cca70497121e");
        return new Animal(id, "marichui", null, null, 'H', 100, 8, 111, new Date());
    }

    private Animal createDummyAnimal2() {
        UUID id = UUID.fromString("ad6b7362-383d-46f6-ab13-cca70497121e");
        return new Animal(id, "aeiou", null, null, 'M', 100, 8, 113, new Date());
    }

    @Test
    public void testAddAnimalWithNonExistentFather() {
        Animal toAdd = createDummyAnimal1();
        toAdd.setMaleParentName("papagayoo");
        try {
            animalService.createAnimal(toAdd);
            fail();
        } catch(AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getAnimalError());
            AnimalError animalError = e.getAnimalError();
            assertEquals(AnimalErrorCode.CODE_06.getMessage(), animalError.getMessage());
            assertEquals(AnimalErrorCode.CODE_06, animalError.getCode());
        }
    }

    @Test
    public void testAddAnimalWithNonExistentMother() {
        Animal toAdd = createDummyAnimal1();
        toAdd.setFemaleParentName("mamagaayoo");
        try {
            animalService.createAnimal(toAdd);
            fail();
        } catch(AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getAnimalError());
            AnimalError animalError = e.getAnimalError();
            assertEquals(AnimalErrorCode.CODE_06.getMessage(), animalError.getMessage());
            assertEquals(AnimalErrorCode.CODE_06, animalError.getCode());
        }
    }

    @Test
    public void testAddAnimalWithNotMatchingGenderFather() {
        Animal parent = createDummyAnimal1();
        //Sex is H and we are going to assign it as a father, so it must throw an exception when added
        Animal toAdd = setUpCorrectAnimal();
        when(animalRepository.findById(parent.getName())).thenReturn(Optional.of(parent));
        toAdd.setMaleParentName(parent.getName());
        try {
            animalService.createAnimal(toAdd);
            fail();
        } catch(AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getAnimalError());
            AnimalError animalError = e.getAnimalError();
            assertEquals(AnimalErrorCode.CODE_08.getMessage(), animalError.getMessage());
            assertEquals(AnimalErrorCode.CODE_08, animalError.getCode());
        }
    }

    @Test
    public void testAddAnimalWithNotMatchingGenderMother() {
        Animal parent = createDummyAnimal1();
        //Sex is M and we are going to assign it as a mother, so it should throw an exception when added
        Animal toAdd = setUpCorrectAnimal();
        toAdd.setFemaleParentName(parent.getFemaleParentName());
        when(animalRepository.findById(parent.getName())).thenReturn(Optional.of(parent));
        toAdd.setMaleParentName(parent.getName());
        try {
            animalService.createAnimal(toAdd);
            fail();
        } catch(AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getAnimalError());
            AnimalError animalError = e.getAnimalError();
            assertEquals(AnimalErrorCode.CODE_08.getMessage(), animalError.getMessage());
            assertEquals(AnimalErrorCode.CODE_08, animalError.getCode());
        }
    }

    @Test
    public void testGetNonexistentAnimal() {
        try {
            animalService.getAnimal("no existo");
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
            assertNotNull(e.getAnimalError());
            AnimalError animalError = e.getAnimalError();
            assertEquals(AnimalErrorCode.CODE_11.getMessage(), animalError.getMessage());
            assertEquals(AnimalErrorCode.CODE_11, animalError.getCode());
        }
    }

    @Test
    public void testGetAnimalWithNoMother() {
        Animal father = createDummyAnimal2();
        Animal child = setUpCorrectAnimal();
        child.setMaleParentName(father.getName());
        ArrayList<Animal> allAnimals = new ArrayList<>(List.of(new Animal[]{father, child}));
        when(animalRepository.findById(father.getName())).thenReturn(Optional.of(father));
        when(animalService.getAnimals()).thenReturn(allAnimals);
        List<Animal> found = animalService.getAnimal(child.getName());
        assertNotNull(found);
        assertEquals(found.size(), 2);
        assertEquals(found.get(0).getName(), child.getName());
        assertEquals(found.get(0).getMaleParentName(), father.getName());
        assertEquals(found.get(1).getName(), father.getName());
    }

    @Test
    public void testGetAnimalWithNoFather() {
        Animal mother = createDummyAnimal1();
        Animal child = setUpCorrectAnimal();
        child.setFemaleParentName(mother.getName());
        ArrayList<Animal> allAnimals = new ArrayList<>(List.of(new Animal[]{mother, child}));
        when(animalRepository.findById(mother.getName())).thenReturn(Optional.of(mother));
        when(animalService.getAnimals()).thenReturn(allAnimals);
        List<Animal> found = animalService.getAnimal(child.getName());
        assertNotNull(found);
        assertEquals(found.size(), 2);
        assertEquals(found.get(0).getName(), child.getName());
        assertEquals(found.get(0).getFemaleParentName(), mother.getName());
        assertEquals(found.get(1).getName(), mother.getName());
    }

    @Test
    public void testGetAnimalWithNoParents() {
        Animal animal = setUpCorrectAnimal();
        ArrayList<Animal> allAnimals = new ArrayList<>(List.of(new Animal[]{animal}));
        when(animalService.getAnimals()).thenReturn(allAnimals);
        List<Animal> found = animalService.getAnimal(animal.getName());
        assertNotNull(found);
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getName(), animal.getName());
    }


}
