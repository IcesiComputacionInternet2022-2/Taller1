package co.edu.icesi.restzoo.service;

import co.edu.icesi.restzoo.constant.Constants;
import co.edu.icesi.restzoo.model.Animal;
import co.edu.icesi.restzoo.repository.ZooRepository;
import co.edu.icesi.restzoo.service.impl.AnimalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnimalServiceTest {

    private ZooRepository zooRepository;
    private AnimalService animalService;
    private Animal daddyBear;
    private Animal mommyBear;
    private Animal littleBear;
    private final String BEAR_DAD = "Papa Oso";
    private final UUID DAD_ID = UUID.randomUUID();
    private final String BEAR_MOM = "Mama Oso";
    private final UUID MOM_ID = UUID.randomUUID();
    private final String BEAR_SON = "Osito";
    private final UUID SON_ID = UUID.randomUUID();

    @BeforeEach
    public void init() {
        zooRepository = mock(ZooRepository.class);
        animalService = new AnimalServiceImpl(zooRepository);
    }

    private List<Animal> setAnimalList() {
        List<Animal> animals = new ArrayList<>();
        animals.add(daddyBear);
        animals.add(mommyBear);
        animals.add(littleBear);
        return animals;
    }

    private void setAll() {
        setDadBear();
        setMomBear();
        setLittleBear();
    }

    private void setDadBear() {
        char sex = 'M';
        double weight = 28000;
        double age = 18;
        double length = 150;
        LocalDateTime date = LocalDateTime.of(2003, 4, 10, 9,36,45);
        daddyBear = new Animal(DAD_ID, BEAR_DAD, sex, weight, age, length, date, null, null);
    }

    private void setMomBear() {
        char sex = 'F';
        double weight = 26000;
        double age = 19;
        double length = 130;
        LocalDateTime date = LocalDateTime.of(2002, 8, 22, 16,20,28);
        mommyBear = new Animal(MOM_ID, BEAR_MOM, sex, weight, age, length, date, null, null);
    }

    private void setLittleBear() {
        char sex = 'M';
        double weight = 12000;
        double age = 7;
        double length = 80;
        LocalDateTime date = LocalDateTime.of(2015, 7, 29, 7, 30, 51);
        littleBear = new Animal(SON_ID, BEAR_SON, sex, weight, age, length, date, BEAR_DAD, BEAR_MOM);
    }

    private boolean successfulCreation(Animal animal) {
        try {
            animalService.createAnimal(animal);
            return true;
        }
        catch (Exception ignored){ return false; }
    }

    @Test
    public void testGetAnimalById() {
        setLittleBear();
        when(zooRepository.findAll()).thenReturn(setAnimalList());
        Animal result = animalService.getAnimal(SON_ID);
        verify(zooRepository, times(1)).findById(any());
    }

    @Test
    public void testGetAnimalByName() {
        setLittleBear();
        when(zooRepository.findAll()).thenReturn(setAnimalList());
        Animal result = animalService.getAnimal(BEAR_SON);
        verify(zooRepository, times(1)).findByName(any());
    }

    @Test
    public void testCreateAnimal() {
        setDadBear();
        assertTrue(successfulCreation(daddyBear));
        verify(zooRepository,times(1)).save(any());
    }

    @Test
    public void testGetAnimals() {
        animalService.getAnimals();
        verify(zooRepository, times(1)).findAll();
    }

    @Test
    public void testDateIsOnThePast() {
        setLittleBear();
        littleBear.setArrivalDate(LocalDateTime.now());
        assertFalse(successfulCreation(littleBear));
    }

    @Test
    public void testExactlyTwoParentsNoMom() {
        setLittleBear();
        setDadBear();
        littleBear.setMother(null);
        assertFalse(successfulCreation(littleBear));
    }

    @Test
    public void testExactlyTwoParentsNoDad() {
        setLittleBear();
        setMomBear();
        littleBear.setFather(null);
        assertFalse(successfulCreation(littleBear));
    }

    @Test
    public void testMotherExists() {
        setAll();
        littleBear.setMother(Constants.NO_MOTHER.getValue());
        assertFalse(successfulCreation(littleBear));
    }

    @Test
    public void testFatherExists() {
        setAll();
        littleBear.setFather(Constants.NO_MOTHER.getValue());
        assertFalse(successfulCreation(littleBear));
    }

    @Test
    public void testMotherSexMatches() {
        setAll();
        mommyBear.setSex('M');
        animalService.createAnimal(mommyBear);
        assertFalse(successfulCreation(littleBear));
    }

    @Test
    public void testFatherSexMatches() {
        setAll();
        daddyBear.setSex('F');
        animalService.createAnimal(daddyBear);
        assertFalse(successfulCreation(littleBear));
    }
}
