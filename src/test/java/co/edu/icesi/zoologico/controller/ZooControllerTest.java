package co.edu.icesi.zoologico.controller;


import co.edu.icesi.zoologico.dto.AnimalDTO;
import co.edu.icesi.zoologico.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoologico.error.exception.AnimalDemoError;
import co.edu.icesi.zoologico.error.exception.AnimalDemoException;
import co.edu.icesi.zoologico.mapper.AnimalMapper;
import co.edu.icesi.zoologico.mapper.AnimalMapperImpl;
import co.edu.icesi.zoologico.model.Animal;
import co.edu.icesi.zoologico.service.ZooService;
import co.edu.icesi.zoologico.service.impl.ZooServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZooControllerTest {



    private ZooService zooService;

    private ZooController zooController;

    private AnimalMapper animalMapper;


    @BeforeEach
    public void init(){
        zooService = mock(ZooServiceImpl.class);
        animalMapper = new AnimalMapperImpl();
        zooController = new ZooController( animalMapper,zooService);
    }


    /**
     * - This scenary register the father and mother in the system.
     */
    public void scenary3(){
        UUID animalFatherId=UUID.randomUUID();
        UUID animalMotherId=UUID.randomUUID();
        Animal animalTestFather = new Animal(animalFatherId,"Fox Father","Male",20,20,5,null,null, LocalDateTime.now());
        Animal animalTestMother = new Animal(animalMotherId,"Fox Mother","Female",20,20,5,null,null, LocalDateTime.now());
        zooService.createAnimal(animalTestFather);
        zooService.createAnimal(animalTestMother);
    }

    @Test
    public void testCreateAnimal(){
        UUID animalId=UUID.randomUUID();
        when(zooService.createAnimal(ArgumentMatchers.any())).thenReturn(new Animal(animalId,"Paco","Male",20,20,5,null,null, LocalDateTime.now()));
        AnimalDTO testAnimal = new AnimalDTO(animalId,"Paco","Male",20,5,20,null,null,LocalDateTime.now());
        assertEquals(testAnimal.getName(),zooController.createAnimal(testAnimal).getName());
    }


    @Test
    public void testGetAnimals(){
        ArrayList<Animal> list= new ArrayList<Animal>();
        list.add(new Animal(UUID.randomUUID(),"Paco","Male",20,20,5,null,null, LocalDateTime.now()));
        when(zooService.getAnimals()).thenReturn(list);
        assertTrue(zooController.getAnimals().get(0).getName().equals("Paco"));
    }


    @Test
    public void testVerifyIfNameLengthIsMoreThan120(){
        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(),"Paco","Male",20,5,20,null,null,LocalDateTime.now());
        String animalName = "";

        for (int i=0;i<125;i++){
            animalName+="X";
        }

        animalDTO.setName(animalName);
        AnimalDemoException exception =assertThrows(AnimalDemoException.class, () -> {zooController.createAnimal(animalDTO);} );
        assertEquals("Throw AnimalDemoException",exception.getError().getMessage());
    }

    @Test
    public void testVerifyNameHaveSpecialCharacters(){
        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(),"Paco.","Male",20,5,20,null,null,LocalDateTime.now());
        try{
            zooController.createAnimal(animalDTO);
            fail();
        }catch (AnimalDemoException exception){
            assertEquals("Throw AnimalDemoException",exception.getError().getMessage());
        }
    }

    @Test
    public void testVerifyNameEmpty(){
        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(),null,"Male",20,5,20,null,null,LocalDateTime.now());
        try{
            zooController.createAnimal(animalDTO);
            fail();
        }catch (AnimalDemoException exception){
            assertEquals("Throw AnimalDemoException",exception.getError().getMessage());
        }
    }

    @Test
    public void testVerifyArrivalDateAfterNow() {

        LocalDateTime dateBefore = LocalDateTime.now();
        LocalDateTime dateAfter = dateBefore.plusDays(2);

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(),null,"Male",20,5,20,null,null,dateAfter);
        try{
            zooController.createAnimal(animalDTO);
            fail();
        }catch (AnimalDemoException exception){
            assertEquals("Throw AnimalDemoException",exception.getError().getMessage());
        }

    }

    @Test
    public void testVerifyArrivalDateBeforeNow(){
        LocalDateTime dateBefore = LocalDateTime.now();

        UUID randomUUID=UUID.randomUUID();
        when(zooService.createAnimal(ArgumentMatchers.any())).thenReturn(new Animal(randomUUID,"Topo","Male",20,10,20,null,null,dateBefore));
        AnimalDTO animalDTO = new AnimalDTO(randomUUID,"Topo","Male",20,10,20,null,null,dateBefore);;
        assertEquals(animalDTO.getAge(),zooController.createAnimal(animalDTO).getAge());
    }

    @Test
    public void testVerifyAnimalHeightIncorrect() {

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(),"Topo","Male",20,5,200,null,null,LocalDateTime.now());
        try{
            zooController.createAnimal(animalDTO);
            fail();
        }catch (AnimalDemoException exception){
            assertEquals("Throw AnimalDemoException",exception.getError().getMessage());
        }
    }

    @Test
    public void testVerifyAnimalHeightCorrect(){

        UUID randomUUID=UUID.randomUUID();
        when(zooService.createAnimal(ArgumentMatchers.any())).thenReturn(new Animal(randomUUID,"Topo","Male",20,10,20,null,null,LocalDateTime.now()));
        AnimalDTO animalDTO = new AnimalDTO(randomUUID,"Topo","Male",20,10,20,null,null,LocalDateTime.now());;
        assertEquals(animalDTO.getAge(),zooController.createAnimal(animalDTO).getAge());
    }

    @Test
    public void testVerifyAnimalWeightIncorrect() {

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(),"Topo","Male",100,5,20,null,null,LocalDateTime.now());
        try{
            zooController.createAnimal(animalDTO);
            fail();
        }catch (AnimalDemoException exception){
            assertEquals("Throw AnimalDemoException",exception.getError().getMessage());
        }
    }

    @Test
    public void testVerifyAnimalWeightCorrect(){

        UUID randomUUID=UUID.randomUUID();
        when(zooService.createAnimal(ArgumentMatchers.any())).thenReturn(new Animal(randomUUID,"Topo","Male",20,10,20,null,null,LocalDateTime.now()));
        AnimalDTO animalDTO = new AnimalDTO(randomUUID,"Topo","Male",20,10,20,null,null,LocalDateTime.now());;
        assertEquals(animalDTO.getAge(),zooController.createAnimal(animalDTO).getAge());
    }

    @Test
    public void testVerifyAnimalAgeIncorrect() {

        AnimalDTO animalDTO = new AnimalDTO(UUID.randomUUID(),"Topo","Male",20,500,20,null,null,LocalDateTime.now());
        try{
            zooController.createAnimal(animalDTO);
            fail();
        }catch (AnimalDemoException exception){
            assertEquals("Throw AnimalDemoException",exception.getError().getMessage());
        }
    }

    @Test
    public void testVerifyAnimalAgeCorrect() {
        UUID randomUUID=UUID.randomUUID();
        when(zooService.createAnimal(ArgumentMatchers.any())).thenReturn(new Animal(randomUUID,"Topo","Male",20,10,20,null,null,LocalDateTime.now()));
        AnimalDTO animalDTO = new AnimalDTO(randomUUID,"Topo","Male",20,10,20,null,null,LocalDateTime.now());;
        assertEquals(animalDTO.getAge(),zooController.createAnimal(animalDTO).getAge());
    }

    @Test
    public void testFindAnimalByName(){
        UUID animalId = UUID.randomUUID();
        AnimalWithParentsDTO animalWithParentsDTO = new AnimalWithParentsDTO(animalId,"Topo","Male",20,10,20,null,null,LocalDateTime.now());

        when(zooService.createAnimal(ArgumentMatchers.any())).thenReturn(new Animal(animalId,"Topo","Male",20,10,20,null,null,LocalDateTime.now()));
        when(zooService.getAnimalByName("Topo")).thenReturn(animalWithParentsDTO);

        AnimalWithParentsDTO animalResult= zooController.getAnimalByName("Topo");
        assertEquals(animalResult.getName(),animalWithParentsDTO.getName());
    }
}

