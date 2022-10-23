package co.edu.icesi.CaliZoo.Controller;

import co.edu.icesi.CaliZoo.controller.AnimalController;
import co.edu.icesi.CaliZoo.dto.AnimalDTO;
import co.edu.icesi.CaliZoo.mapper.AnimalMapper;
import co.edu.icesi.CaliZoo.mapper.AnimalMapperImpl;
import co.edu.icesi.CaliZoo.model.Animal;
import co.edu.icesi.CaliZoo.model.Sex;
import co.edu.icesi.CaliZoo.service.AnimalService;
import co.edu.icesi.CaliZoo.service.impl.AnimalServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnimalControllerTest {

    private AnimalService animalService;
    private AnimalController animalController;
    private AnimalMapper animalMapper;

    @BeforeEach
    void setUp() {

        animalService = Mockito.mock(AnimalServiceImpl.class);
        animalMapper = new AnimalMapperImpl();
        animalController = new AnimalController(animalService, animalMapper);

        UUID u1=UUID.randomUUID();
        UUID u2=UUID.randomUUID();

        Animal a1 = new Animal(u1, "Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);
        Animal a2 = new Animal(u2, "Alberta", Sex.FEMALE, 90, 12, 1.1, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);
        animalService.createAnimal(a1);
        animalService.createAnimal(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAnimal() {

        ArrayList<Animal> animalList= new ArrayList<Animal>();
        animalList.add(new Animal(UUID.randomUUID(),"Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null));
        animalList.add(new Animal(UUID.randomUUID(), "Alberta", Sex.FEMALE, 90, 12, 1.1, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null));
        when(animalService.getAnimals()).thenReturn(animalList);
        assertTrue(animalController.getAnimals().get(0).getName().equals("Albert") && animalController.getAnimals().get(1).getName().equals("Alberta"));
    }

    @Test
    void createAnimal() {

        ArrayList<Animal> animalList= new ArrayList<Animal>();
        animalList.add(new Animal(UUID.randomUUID(),"Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null));
        animalList.add(new Animal(UUID.randomUUID(), "Alberta", Sex.FEMALE, 90, 12, 1.1, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null));
        when(animalService.getAnimals()).thenReturn(animalList);
        Animal[] parents = new Animal[2];
        parents[0] = animalService.getAnimals().get(0);
        parents[1] = animalService.getAnimals().get(1);

        UUID u1=UUID.randomUUID();
        when(animalService.createAnimal(ArgumentMatchers.any())).thenReturn(new Animal(u1, "Albertito", Sex.MALE, 30, 1, 1, LocalDateTime.of(2022, 10, 14, 0, 0, 0), parents));
        System.out.println(parents[1]);
        AnimalDTO a1 = new AnimalDTO(u1, "Albertitux", Sex.MALE, 30, 1, 1.2, LocalDateTime.of(2022, 10, 14, 0, 0, 0), null);
        animalController.createAnimal(a1);

    }

    @Test
    void getAnimals() {
    }

    @Test
    void checkName() {

        UUID u1=UUID.randomUUID();
        UUID u2=UUID.randomUUID();
        when(animalService.createAnimal(ArgumentMatchers.any())).thenReturn(new Animal(u1, "Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null));
        AnimalDTO a1 = new AnimalDTO(u1, "Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);
        AnimalDTO a2 = new AnimalDTO(u2, "Alberta", Sex.FEMALE, 90, 12, 1.1, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);
        assertTrue(animalController.checkName(a1.getName()));
        assertTrue(animalController.checkName(a2.getName()));

    }

    @Test
    void isLlama() {

        UUID u1=UUID.randomUUID();
        UUID u2=UUID.randomUUID();
        when(animalService.createAnimal(ArgumentMatchers.any())).thenReturn(new Animal(u1, "Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null));
        AnimalDTO a1 = new AnimalDTO(u1, "Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);
        AnimalDTO a2 = new AnimalDTO(u2, "Alberta", Sex.FEMALE, 90, 12, 1.1, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);
        assertTrue(animalController.isLlama(a1));
        assertTrue(animalController.isLlama(a2));
    }

    @Test
    void validateRepeatName() {

        UUID u1=UUID.randomUUID();
        UUID u2=UUID.randomUUID();
        when(animalService.createAnimal(ArgumentMatchers.any())).thenReturn(new Animal(u1, "Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null));
        AnimalDTO a1 = new AnimalDTO(u1, "Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);
        AnimalDTO a2 = new AnimalDTO(u2, "Alberta", Sex.FEMALE, 90, 12, 1.1, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);
        AnimalDTO a3 = new AnimalDTO(u1, "Albert", Sex.MALE, 100, 10, 1.2, LocalDateTime.of(2020, 12, 14, 0, 0, 0), null);

        assertTrue(animalController.validateRepeatName(a2.getName()));

    }

    @Test
    void validateDate() {



    }

    @Test
    void validateParents() {
    }
}