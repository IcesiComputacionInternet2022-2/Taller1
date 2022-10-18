package com.icesi.edu.zoo.service;

import com.icesi.edu.zoo.model.Animal;
import com.icesi.edu.zoo.repository.AnimalRepository;
import com.icesi.edu.zoo.service.impl.AnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        UUID id = UUID.fromString("");
        return new Animal(id, "4466308c-eac5-4975-9b9d-dd1780764b0f", null, null, 'F', 100, 5, 100, new Date());
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

    }

    @Test
    public void testAddAnimalWithInvalidHeight() {

    }

    @Test
    public void testAddAnimalWithInvalidWeight() {

    }

    @Test
    public void testAddAnimalWithExistentId() {

    }

    @Test
    public void testAddAnimalWithNonExistentFather() {

    }

    @Test
    public void testAddAnimalWithNonExistentMother() {

    }

    @Test
    public void testAddAnimalWithNonExistentParents() {

    }

    @Test
    public void testGetNonexistentAnimal() {

    }

    @Test
    public void testGetAnimalWithNoFather() {

    }

    @Test
    public void testGetAnimalWithNoMother() {

    }

    @Test
    public void testGetAnimalWithNoParents() {

    }


}
