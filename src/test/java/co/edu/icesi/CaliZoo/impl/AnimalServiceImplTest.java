package co.edu.icesi.CaliZoo.service.impl;

import co.edu.icesi.CaliZoo.dto.AnimalDTO;
import co.edu.icesi.CaliZoo.model.Animal;
import co.edu.icesi.CaliZoo.model.Sex;
import co.edu.icesi.CaliZoo.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnimalServiceImplTest {

    private AnimalServiceImpl animalService;

    private AnimalRepository animalRepository;

    @BeforeEach
    public void init(){

        animalRepository = mock(AnimalRepository.class);
        animalService = new AnimalServiceImpl(animalRepository);
    }


    @Test
    void getAnimal() {

        UUID u1 = UUID.randomUUID();
        Animal a1 = new Animal(u1, "Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);
        when(animalRepository.findById(a1.getId())).thenReturn(Optional.ofNullable(a1));
        animalService.createAnimal(a1);
        assertTrue(a1.getId().equals(animalService.getAnimal(u1).getId()));
    }

    @Test
    void createAnimal() {

        UUID u1 = UUID.randomUUID();
        UUID  u2 = UUID.randomUUID();
        Animal a1 = new Animal(u1, "Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);
        when(animalRepository.save(ArgumentMatchers.any())).thenReturn(a1);
        assertTrue(animalService.createAnimal(a1).equals(a1));
    }

    @Test
    void getAnimals() {

        assertTrue(true);

    }
}