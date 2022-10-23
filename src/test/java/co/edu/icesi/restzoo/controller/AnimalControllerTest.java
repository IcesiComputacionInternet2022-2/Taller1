package co.edu.icesi.restzoo.controller;

import co.edu.icesi.restzoo.constant.Constants;
import co.edu.icesi.restzoo.dto.AnimalDTO;
import co.edu.icesi.restzoo.mapper.AnimalMapper;
import co.edu.icesi.restzoo.repository.ZooRepository;
import co.edu.icesi.restzoo.service.AnimalService;
import org.apache.maven.shared.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnimalControllerTest {

    private AnimalController animalController;
    private AnimalService animalService;
    private AnimalDTO animalDTO;
    private ZooRepository zooRepository;
    private AnimalMapper animalMapper;

    private final String BEAR_DAD = "Papa Oso";
    private final UUID DAD_ID = UUID.randomUUID();
    private final String BEAR_MOM = "Mama Oso";
    private final UUID MOM_ID = UUID.randomUUID();

    @BeforeEach
    public void init() {
        animalService = mock(AnimalService.class);
        animalMapper = mock(AnimalMapper.class);
        animalController = new AnimalController(animalMapper, animalService);
    }

    private void setupSceneDad() {
        char sex = 'M';
        double weight = 28000;
        double age = 18;
        double length = 150;
        LocalDateTime date = LocalDateTime.of(2003, 4, 10, 9,36,45);
        animalDTO = new AnimalDTO(DAD_ID, BEAR_DAD, sex, weight, age, length, date, null, null);
    }

    private void setupSceneMom() {
        char sex = 'F';
        double weight = 26000;
        double age = 19;
        double length = 130;
        LocalDateTime date = LocalDateTime.of(2002, 8, 22, 16,20,28);
        animalDTO = new AnimalDTO(MOM_ID, BEAR_MOM, sex, weight, age, length, date, null, null);
    }

    private boolean successfulCreation() {
        when(animalMapper.fromAnimal(any())).thenReturn(animalDTO);
        try {
            animalController.createAnimal(animalDTO);
            return true;
        }
        catch (Exception ignored){ return false; }
    }

    @Test
    public void testCreateAnimal() {
        setupSceneDad();
        assertTrue(successfulCreation());
    }

    @Test
    public void testGetAnimals(){
        animalController.getAnimals();
        verify(animalService, times(1)).getAnimals();
    }

    @Test
    public void testGetAnimalByName(){
        setupSceneDad();
        animalController.getAnimalByName(BEAR_DAD);
        verify(animalService,times(1)).getAnimal(BEAR_DAD);
    }

    @Test
    public void testGetAnimalById() {
        setupSceneMom();
        animalController.getAnimalById(MOM_ID);
        verify(animalService,times(1)).getAnimal(MOM_ID);
    }

    @Test
    public void testValidNameFormat() {
        setupSceneDad();
        animalDTO.setName("0987654321{+}´-goofy ahh string");
        assertFalse(successfulCreation());
    }

    @Test
    public void testValidNameLength() {
        setupSceneMom();
        animalDTO.setName(StringUtils.repeat("E", animalController.NAME_LENGTH_CAP + 1));
        assertFalse(successfulCreation());
    }

    @Test
    public void testValidSex() {
        setupSceneDad();
        animalDTO.setSex('I');
        assertFalse(successfulCreation());
    }

    @Test
    public void testValidAgeCap() {
        setupSceneMom();
        animalDTO.setAge(Double.parseDouble(Constants.MAX_LONGEVITY.getValue()) + 1);
        assertFalse(successfulCreation());
    }

    @Test
    public void testValidAgePositiveNumber() {
        setupSceneMom();
        animalDTO.setAge(-1);
        assertFalse(successfulCreation());
    }

    @Test
    public void testHealthyWeightFloor() {
        setupSceneDad();
        animalDTO.setWeight(Double.parseDouble(Constants.MIN_HEALTHY_WEIGHT.getValue()) - 1);
        assertFalse(successfulCreation());
    }

    @Test
    public void testHealthyWeightCeil() {
        setupSceneMom();
        animalDTO.setWeight(Double.parseDouble(Constants.MAX_HEALTHY_WEIGHT.getValue()) + 1);
        assertFalse(successfulCreation());
    }

    @Test
    public void testBabyLengthFloor() {
        setupSceneDad();
        animalDTO.setWeight(Double.parseDouble(Constants.MIN_BABY_LENGTH.getValue()) - 1);
        assertFalse(successfulCreation());
    }

    @Test
    public void testElderLengthCeil() {
        setupSceneMom();
        animalDTO.setWeight(Double.parseDouble(Constants.MAX_ELDER_LENGTH.getValue()) + 1);
        assertFalse(successfulCreation());
    }
}
