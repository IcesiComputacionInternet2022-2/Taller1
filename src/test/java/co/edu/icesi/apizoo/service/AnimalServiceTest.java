package co.edu.icesi.apizoo.service;

import co.edu.icesi.apizoo.dto.AnimalDTO;
import co.edu.icesi.apizoo.model.Animal;
import co.edu.icesi.apizoo.repository.AnimalRepository;
import co.edu.icesi.apizoo.root.exception.AnimalException;
import co.edu.icesi.apizoo.service.impl.AnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnimalServiceTest {

    private AnimalRepository animalRepository;

    private AnimalService animalService;


    @BeforeEach
    void init(){
        animalRepository = mock(AnimalRepository.class);
        animalService = new AnimalServiceImpl(animalRepository);
    }

    @Test
    void TestGetAnimal(){
        Animal animal = new Animal();
        when(animalRepository.save(any())).thenReturn(animal);
        Animal animalCreated = animalRepository.save(animal);

        assertNotNull(animalRepository.findById(animalCreated.getId()));
    }

    @Test
    void TestGetAnimalNull(){
        Animal animal = new Animal();

        Animal animalCreated = animalRepository.save(animal);
        assertThrows(NullPointerException.class, () -> animalRepository.findById(animalCreated.getId()));
    }

    @Test
    void TestGetAnimals(){
        Animal animal1 = new Animal();
        Animal animal2 = new Animal();

        when(animalRepository.save(any())).thenReturn(animal1);
        Animal animalCreated1 = animalRepository.save(animal1);

        when(animalRepository.save(any())).thenReturn(animal2);
        Animal animalCreated2 = animalRepository.save(animal2);

        assertNotNull(animalRepository.findAll());
    }

    @Test
    void TestCreateAnimal(){
        Animal animal = new Animal();

        when(animalService.createAnimal(any())).thenReturn(animal);

        assertNotNull(animalService.createAnimal(animal));
    }

    @Test
    void TestUpdateAnimal(){
        Animal animal = new Animal();

        when(animalService.createAnimal(any())).thenReturn(animal);

        Animal animalCreated = animalService.createAnimal(animal);

        animalCreated.setSex("female");

        when(animalService.createAnimal(animalCreated)).thenReturn(animalCreated);

        assertNotNull(animalService.updateAnimal(animal));
    }
}
