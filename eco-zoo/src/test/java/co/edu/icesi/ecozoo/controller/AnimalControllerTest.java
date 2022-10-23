package co.edu.icesi.ecozoo.controller;

import co.edu.icesi.ecozoo.constant.AnimalSex;
import co.edu.icesi.ecozoo.dto.AnimalDTO;
import co.edu.icesi.ecozoo.dto.AnimalResponseDTO;
import co.edu.icesi.ecozoo.dto.CapybaraDTO;
import co.edu.icesi.ecozoo.mapper.AnimalMapper;
import co.edu.icesi.ecozoo.mapper.AnimalMapperImpl;
import co.edu.icesi.ecozoo.model.Animal;
import co.edu.icesi.ecozoo.service.AnimalService;
import co.edu.icesi.ecozoo.service.impl.AnimalServiceImpl;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@AllArgsConstructor
public class AnimalControllerTest {
    private static AnimalController animalController;
    private static AnimalService animalService;
    private static AnimalMapper animalMapper;

    private static ArrayList<Animal> animals;
    private static ArrayList<Object> animalsDTO;

    private static AnimalDTO capybaraDTO;

    @BeforeAll
    static void init() {
        animalService = mock(AnimalServiceImpl.class);
        animalMapper = new AnimalMapperImpl();
        animalController = new AnimalController(animalMapper, animalService);
        animals = new ArrayList<>();
        animalsDTO = new ArrayList<>();
    }

    @BeforeEach
    void setup1() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Animal animal1 = Animal.builder()
                .id(UUID.fromString("ae44cbe6-af04-49e2-9254-eccd2f5721af"))
                .name("Capybary F")
                .sex(AnimalSex.FEMALE.isValue())
                .height(1.5)
                .age(2)
                .weight(2.5)
                .arrivalDate(LocalDateTime.parse("2020-11-09 10:30", formatter))
                .build();
        Animal animal2 = Animal.builder()
                .id(UUID.fromString("ae44cbe6-af04-49e2-9254-eccd2f5721ad"))
                .name("Capybary M")
                .sex(AnimalSex.MALE.isValue())
                .height(1.5)
                .age(2)
                .weight(2.5)
                .arrivalDate(LocalDateTime.parse("2020-11-09 10:30", formatter))
                .motherID(UUID.fromString("ae44cbe6-af04-49e2-9254-eccd2f5721af"))
                .build();
        Animal animal3 = Animal.builder()
                .id(UUID.fromString("ae44cbe6-af04-49e2-9254-eccd2f5721ae"))
                .name("Capybary")
                .sex(AnimalSex.FEMALE.isValue())
                .height(1.5)
                .age(2)
                .weight(2.5)
                .arrivalDate(LocalDateTime.parse("2020-11-09 10:30", formatter))
                .fatherID(UUID.fromString("ae44cbe6-af04-49e2-9254-eccd2f5721ad"))
                .motherID(UUID.fromString("ae44cbe6-af04-49e2-9254-eccd2f5721af"))
                .build();

        animals.add(animal1);
        animals.add(animal2);
        animals.add(animal3);

        AnimalResponseDTO animalDTO1 = AnimalResponseDTO.builder()
                .id(animal1.getId())
                .name(animal1.getName())
                .sex(AnimalSex.FEMALE)
                .height(animal1.getHeight())
                .age(animal1.getAge())
                .weight(animal1.getWeight())
                .arrivalDate(animal1.getArrivalDate())
                .build();
        AnimalResponseDTO animalDTO2 = AnimalResponseDTO.builder()
                .id(animal2.getId())
                .name(animal2.getName())
                .sex(AnimalSex.MALE)
                .height(animal2.getHeight())
                .age(animal2.getAge())
                .weight(animal2.getWeight())
                .arrivalDate(animal2.getArrivalDate())
                .mother(animal1)
                .build();
        AnimalResponseDTO animalDTO3 = AnimalResponseDTO.builder()
                .id(animal3.getId())
                .name(animal3.getName())
                .sex(AnimalSex.FEMALE)
                .height(animal3.getHeight())
                .age(animal3.getAge())
                .weight(animal3.getWeight())
                .arrivalDate(animal3.getArrivalDate())
                .mother(animal1)
                .father(animal2)
                .build();
        animalsDTO.add(animalDTO1);
        animalsDTO.add(animalDTO2);
        animalsDTO.add(animalDTO3);

        capybaraDTO = new CapybaraDTO();
        capybaraDTO.setHeight(1.5);
        capybaraDTO.setAge(2);
        capybaraDTO.setWeight(2.5);
        capybaraDTO.setName("Capybary");
        capybaraDTO.setSex(AnimalSex.FEMALE);
        capybaraDTO.setArrivalDate(LocalDateTime.parse("2020-11-09 10:30", formatter));
    }

    @Test
    void getAnimalResponseByID() {
        when(animalService.getAnimal(animals.get(0).getId())).thenReturn(animals.get(0));
        when(animalService.getAnimal(animals.get(1).getId())).thenReturn(animals.get(1));
        when(animalService.getAnimal(animals.get(2).getId())).thenReturn(animals.get(2));
        AnimalResponseDTO response = animalController.getAnimal(animals.get(2).getId());
        assertEquals(animalsDTO.get(2), response);
    }

    @Test
    void getAnimalResponseByName() {
        when(animalService.getAnimalByName(animals.get(0).getName())).thenReturn(animals.get(0));
        when(animalService.getAnimalByName(animals.get(1).getName())).thenReturn(animals.get(1));
        when(animalService.getAnimalByName(animals.get(2).getName())).thenReturn(animals.get(2));
        AnimalResponseDTO response = animalController.getAnimalByName(animals.get(1).getName());
        assertEquals(animalsDTO.get(1), response);
    }

    @Test
    void getAnimals() {
        animalController.getAnimals();
        verify(animalService, times(1)).getAnimals();
    }

    @Test
    void createAnimal() {
        animalController.createAnimal((CapybaraDTO) capybaraDTO);
        verify(animalService, times(1)).createAnimal(animalMapper.fromDTO(capybaraDTO));
    }
}
