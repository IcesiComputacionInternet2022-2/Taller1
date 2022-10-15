package co.edu.icesi.zoo.service;

import co.edu.icesi.zoo.constant.AnimalGender;
import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoo.mapper.AnimalMapper;
import co.edu.icesi.zoo.mapper.impl.AnimalMapperImpl;
import co.edu.icesi.zoo.model.Animal;
import co.edu.icesi.zoo.repository.AnimalRepository;
import co.edu.icesi.zoo.service.impl.AnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class AnimalServiceTest {

    private final static UUID ANIMAL_UUID = UUID.fromString("c1f820e0-4100-4d36-a7af-0a7fade4201b");
    private static final String ANIMAL_NAME = "Luna";
    private static final AnimalGender ANIMAL_SEX = AnimalGender.F;
    private static final int ANIMAL_AGE = 10;
    private static final double ANIMAL_HEIGHT = 2;
    private static final double ANIMAL_WEIGHT = 150;
    private static final LocalDateTime ANIMAL_ARRIVAL_DATE = LocalDateTime.of(2002, 11, 13, 0, 0, 0);

    private AnimalRepository animalRepository;
    private AnimalMapper animalMapper;
    private AnimalService animalService;

    @BeforeEach
    public void init() {
        animalRepository = mock(AnimalRepository.class);
        animalMapper = new AnimalMapperImpl();
        animalService = new AnimalServiceImpl(animalRepository, animalMapper);
    }

    @Test
    public void testGetNameFromUUID() {
        Animal animal = dummyAnimal();
        when(animalRepository.findById(animal.getId())).thenReturn(Optional.of(animal));
        assertEquals(animal.getName(), animalService.getNameFromUUID(animal.getId()));
    }

    @Test
    public void testGetUUIDFromName() {
        Animal animal = dummyAnimal();
        when(animalRepository.findByName(animal.getName())).thenReturn(Optional.of(animal));
        assertEquals(animal.getId(), animalService.getUUIDFromName(animal.getName()));
    }

    @Test
    public void testServiceCreateAnimal() {
        Animal animal = dummyAnimal();
        when(animalRepository.save(any())).thenReturn(animal);
        assertEquals(animal, animalService.createAnimal(animal));
        verify(animalRepository, times(1)).save(any());
    }

    @Test
    public void testGetDTOFromAnimal() {
        Animal animal = dummyAnimal();
        animal.setId(null);
        AnimalDTO animalDTO = animalMapper.fromAnimalToDTO(animal, null, null);
        assertEquals(animalDTO, animalService.getDTOFromAnimal(animal));
    }

    @Test
    public void testGetAnimalFromDTO() {
        Animal animal = dummyAnimal();
        animal.setId(null);
        AnimalDTO animalDTO = animalMapper.fromAnimalToDTO(animal, null, null);
        assertEquals(animal, animalService.getAnimalFromDTO(animalDTO));
    }

    @Test
    public void testGetNullAnimalWithParentsFromAnimal() {
        assertNull(animalService.getAnimalWithParentsFromAnimal(null));
    }

    @Test
    public void testGetAnimalWithParentsFromAnimal() {
        AnimalWithParentsDTO animalWithParentsDTO = AnimalWithParentsDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
        Animal animal = dummyAnimal();
        assertEquals(animalWithParentsDTO, animalService.getAnimalWithParentsFromAnimal(animal));
    }

    @Test
    public void testGetEmptyAnimals() {
        assertEquals(0, animalService.getAnimals().size());
    }

    private Animal dummyAnimal() {
        return Animal.builder().id(ANIMAL_UUID).name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
    }
}
