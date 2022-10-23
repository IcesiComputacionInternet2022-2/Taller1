package co.edu.icesi.apizoo.controller;

import co.edu.icesi.apizoo.dto.AnimalDTO;
import co.edu.icesi.apizoo.mapper.AnimalMapper;
import co.edu.icesi.apizoo.root.exception.AnimalException;
import co.edu.icesi.apizoo.service.AnimalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnimalControllerTest {

    private AnimalService animalService;

    private AnimalMapper animalMapper;

    private AnimalController animalController;

    @BeforeEach
    void init(){
        animalService = mock(AnimalService.class);
        animalMapper = mock(AnimalMapper.class);
        animalController = new AnimalController(animalService,animalMapper);
    }

    @Test
    void TestController(){
        String name = "Venao Cola Blanca";
        String sex = "Male";
        Integer weight = 60;
        Integer age = 10;
        Integer height = 60;

        String str = "2020-04-08 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime arrivalDate = LocalDateTime.parse(str, formatter);

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(), name, sex, weight, age, height, arrivalDate, null, null);

        when(animalMapper.toAnimalDTO(animalService.createAnimal(animalMapper.toAnimal(animalDTO)))).thenReturn(animalDTO);
        assertNotNull(animalController.createAnimal(animalDTO));
    }

    @Test
    void TestValidateNameUnique(){
        /*String name = "Venao Cola Blanca";
        String sex = "Male";
        Integer weight = 60;
        Integer age = 10;
        Integer height = 60;

        String str = "2020-04-08 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime arrivalDate = LocalDateTime.parse(str, formatter);

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(), name, sex, weight, age, height, arrivalDate, null, null);

        when(animalMapper.toAnimalDTO(animalService.createAnimal(animalMapper.toAnimal(animalDTO)))).thenReturn(animalDTO);

        Animal firstAnimal = animalService.createAnimal(animalMapper.toAnimal(animalDTO));

        when(animalService.getAnimalById(firstAnimal.getId())).thenReturn(animalMapper.toAnimal(animalDTO));

        AnimalDTO animal = animalMapper.toAnimalDTO(animalService.getAnimalById(firstAnimal.getId()));

        when(animalMapper.toAnimalDTO(animalService.createAnimal(animalMapper.toAnimal(animalDTO)))).thenReturn(animal);
        assertThrows(AnimalException.class, () -> animalController.createAnimal(animalDTO));*/
    }

    @Test
    void TestValidateNameCharacters(){
        String name = "Venao 1234";
        String sex = "Male";
        Integer weight = 60;
        Integer age = 10;
        Integer height = 60;

        String str = "2020-04-08 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime arrivalDate = LocalDateTime.parse(str, formatter);

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(), name, sex, weight, age, height, arrivalDate, null, null);

        when(animalMapper.toAnimalDTO(animalService.createAnimal(animalMapper.toAnimal(animalDTO)))).thenReturn(animalDTO);
        assertThrows(AnimalException.class, () -> animalController.createAnimal(animalDTO));

    }

    @Test
    void TestLenghtName(){
        String name = "Venaoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo";
        String sex = "Male";
        Integer weight = 60;
        Integer age = 10;
        Integer height = 60;

        String str = "2020-04-08 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime arrivalDate = LocalDateTime.parse(str, formatter);

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(), name, sex, weight, age, height, arrivalDate, null, null);

        when(animalMapper.toAnimalDTO(animalService.createAnimal(animalMapper.toAnimal(animalDTO)))).thenReturn(animalDTO);
        assertThrows(AnimalException.class, () -> animalController.createAnimal(animalDTO));
    }

    @Test
    void TestValidateArrivalDate(){
        String name = "Venaoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo";
        String sex = "Male";
        Integer weight = 60;
        Integer age = 10;
        Integer height = 60;

        String str = "2023-04-08 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime arrivalDate = LocalDateTime.parse(str, formatter);

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(), name, sex, weight, age, height, arrivalDate, null, null);

        when(animalMapper.toAnimalDTO(animalService.createAnimal(animalMapper.toAnimal(animalDTO)))).thenReturn(animalDTO);
        assertThrows(AnimalException.class, () -> animalController.createAnimal(animalDTO));
    }

    @Test
    void TestValidateAge(){
        String name = "Venao Cola Blanca";
        String sex = "Male";
        Integer weight = 60;
        Integer age = 25;
        Integer height = 60;

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(), name, sex, weight, age, height, null, null, null);

        when(animalMapper.toAnimalDTO(animalService.createAnimal(animalMapper.toAnimal(animalDTO)))).thenReturn(animalDTO);
        assertThrows(AnimalException.class, () -> animalController.createAnimal(animalDTO));
    }

    @Test
    void TestValidateHeight(){
        String name = "Venao Cola Blanca";
        String sex = "Male";
        Integer weight = 40;
        Integer age = 18;
        Integer height = 60;

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(), name, sex, weight, age, height, null, null, null);

        when(animalMapper.toAnimalDTO(animalService.createAnimal(animalMapper.toAnimal(animalDTO)))).thenReturn(animalDTO);
        assertThrows(AnimalException.class, () -> animalController.createAnimal(animalDTO));

    }

}
