package co.edu.icesi.ecozoo.service;

import co.edu.icesi.ecozoo.constant.AnimalErrorCode;
import co.edu.icesi.ecozoo.constant.AnimalSex;
import co.edu.icesi.ecozoo.error.exception.AnimalException;
import co.edu.icesi.ecozoo.model.Animal;
import co.edu.icesi.ecozoo.repository.AnimalRepository;
import co.edu.icesi.ecozoo.service.impl.AnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AnimalServiceImplTest {

    private static AnimalService animalService;
    private static AnimalRepository animalRepository;

    private static final String NAME_ANIMAL = "Portgas D. Ace";
    private static final UUID ID_ANIMAL = UUID.fromString("c0a80101-0000-0000-0000-000000000000");
    private static final UUID ID_ANIMAL_F = UUID.fromString("c0a80101-0000-0000-0000-000000000001");
    private static final UUID ID_ANIMAL_M = UUID.fromString("c0a80101-0000-0000-0000-000000000002");


    @BeforeEach
    void init() {
        animalRepository = mock(AnimalRepository.class);
        animalService = new AnimalServiceImpl(animalRepository);
    }

    @Test
    void getAnimal() {
        when(animalRepository.findById(ID_ANIMAL)).thenReturn(Optional.of(new Animal()));
        animalService.getAnimal(ID_ANIMAL);
        verify(animalRepository, times(1)).findById(ID_ANIMAL);
    }

    @Test
    void getAnimalByName() {
        when(animalRepository.findByName(NAME_ANIMAL)).thenReturn(Optional.of(new Animal()));
        animalService.getAnimalByName(NAME_ANIMAL);
        verify(animalRepository, times(1)).findByName(NAME_ANIMAL);
    }

    @Test
    void getAnimalNotFound() {
        when(animalRepository.findById(ID_ANIMAL)).thenReturn(Optional.empty());
        AnimalException exception = assertThrows(AnimalException.class, () -> animalService.getAnimal(ID_ANIMAL));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(AnimalErrorCode.CODE_01.getMessage(), exception.getError().getMessage());
        assertEquals(AnimalErrorCode.CODE_01, exception.getError().getCode());
        verify(animalRepository, times(1)).findById(ID_ANIMAL);
    }

    @Test
    void createAnimal() {
        Animal animal = Animal.builder().id(ID_ANIMAL).name(NAME_ANIMAL).build();
        when(animalRepository.save(animal)).thenReturn(animal);
        animalService.createAnimal(animal);
        verify(animalRepository, times(1)).save(animal);
    }

    @Test
    void createAnimalWithParents() {
        Animal father = Animal.builder().id(ID_ANIMAL_F).name("Portgas D. Rouge").sex(AnimalSex.MALE.isValue()).build();
        Animal mother = Animal.builder().id(ID_ANIMAL_M).name("Gol D. Roger").sex(AnimalSex.FEMALE.isValue()).build();
        Animal animal = Animal.builder().id(ID_ANIMAL).name(NAME_ANIMAL).fatherID(ID_ANIMAL_F).motherID(ID_ANIMAL_M).build();
        when(animalRepository.save(animal)).thenReturn(animal);
        when(animalRepository.findById(father.getId())).thenReturn(Optional.of(father));
        when(animalRepository.findById(mother.getId())).thenReturn(Optional.of(mother));
        animalService.createAnimal(animal);
        verify(animalRepository, times(1)).save(animal);
        verify(animalRepository, times(1)).findById(father.getId());
        verify(animalRepository, times(1)).findById(mother.getId());
    }

    @Test
    void createAnimalRepeated() {
        Animal animal = Animal.builder().id(ID_ANIMAL).name(NAME_ANIMAL).build();
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));
        AnimalException exception = assertThrows(AnimalException.class, () -> animalService.createAnimal(animal));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(AnimalErrorCode.CODE_05.getMessage(), exception.getError().getMessage());
        assertEquals(AnimalErrorCode.CODE_05, exception.getError().getCode());
        verify(animalRepository, times(1)).findByName(animal.getName());
        verify(animalRepository, times(0)).save(animal);
    }

    @Test
    void createAnimalWithFatherNotFound(){
        Animal animal = Animal.builder().id(ID_ANIMAL).name(NAME_ANIMAL).fatherID(ID_ANIMAL_F).build();
        when(animalRepository.findById(ID_ANIMAL_F)).thenReturn(Optional.empty());
        AnimalException exception = assertThrows(AnimalException.class, () -> animalService.createAnimal(animal));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(AnimalErrorCode.CODE_02.getMessage(), exception.getError().getMessage());
        assertEquals(AnimalErrorCode.CODE_02, exception.getError().getCode());
        verify(animalRepository, times(1)).findById(ID_ANIMAL_F);
        verify(animalRepository, times(0)).save(animal);
    }

    @Test
    void createAnimalWithMotherNotFound(){
        Animal animal = Animal.builder().id(ID_ANIMAL).name(NAME_ANIMAL).motherID(ID_ANIMAL_M).build();
        when(animalRepository.findById(ID_ANIMAL_M)).thenReturn(Optional.empty());
        AnimalException exception = assertThrows(AnimalException.class, () -> animalService.createAnimal(animal));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(AnimalErrorCode.CODE_02.getMessage(), exception.getError().getMessage());
        assertEquals(AnimalErrorCode.CODE_02, exception.getError().getCode());
        verify(animalRepository, times(1)).findById(ID_ANIMAL_M);
        verify(animalRepository, times(0)).save(animal);
    }

    @Test
    void createAnimalWithNotFemaleMother(){
        Animal animal = Animal.builder().id(ID_ANIMAL).name(NAME_ANIMAL).motherID(ID_ANIMAL_F).build();
        Animal mother = Animal.builder().id(ID_ANIMAL_F).name("Gol D. Roger").sex(AnimalSex.MALE.isValue()).build();
        when(animalRepository.findById(ID_ANIMAL_F)).thenReturn(Optional.of(mother));
        AnimalException exception = assertThrows(AnimalException.class, () -> animalService.createAnimal(animal));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(AnimalErrorCode.CODE_03.getMessage(), exception.getError().getMessage());
        assertEquals(AnimalErrorCode.CODE_03, exception.getError().getCode());
        verify(animalRepository, times(1)).findById(ID_ANIMAL_F);
        verify(animalRepository, times(0)).save(animal);
    }

    @Test
    void createAnimalWithNotMaleFather(){
        Animal animal = Animal.builder().id(ID_ANIMAL).name(NAME_ANIMAL).fatherID(ID_ANIMAL_M).build();
        Animal father = Animal.builder().id(ID_ANIMAL_M).name("Portgas D. Rouge").sex(AnimalSex.FEMALE.isValue()).build();
        when(animalRepository.findById(ID_ANIMAL_M)).thenReturn(Optional.of(father));
        AnimalException exception = assertThrows(AnimalException.class, () -> animalService.createAnimal(animal));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(AnimalErrorCode.CODE_04.getMessage(), exception.getError().getMessage());
        assertEquals(AnimalErrorCode.CODE_04, exception.getError().getCode());
        verify(animalRepository, times(1)).findById(ID_ANIMAL_M);
        verify(animalRepository, times(0)).save(animal);
    }

    @Test
    void getAnimals(){
        animalService.getAnimals();
        verify(animalRepository, times(1)).findAll();
    }

}
