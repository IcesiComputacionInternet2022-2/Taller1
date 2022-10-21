package co.edu.icesi.zoologico.service;

import co.edu.icesi.zoologico.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoologico.error.exception.AnimalDemoException;
import co.edu.icesi.zoologico.mapper.AnimalMapper;
import co.edu.icesi.zoologico.mapper.AnimalMapperImpl;
import co.edu.icesi.zoologico.model.Animal;
import co.edu.icesi.zoologico.repository.ZooRepository;
import co.edu.icesi.zoologico.service.impl.ZooServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZooServiceTest {

    private Animal testNewAnimalToAdd;
    private Animal animalFather;
    private Animal animalMother;
    private List<Animal> animalListTest;

    private ZooRepository zooRepository;

    private ZooService zooService;

    private AnimalMapper animalMapper;


    @BeforeEach
    public void init(){
        zooRepository = mock(ZooRepository.class);
        animalMapper = new AnimalMapperImpl();
        zooService = new ZooServiceImpl(zooRepository,animalMapper);

    }

    private void createAnimalsParentsAndSon(){
        animalListTest = new ArrayList<>();
        animalFather= new Animal(UUID.randomUUID(),"Topa Father","Male",20,500,20,null,null, LocalDateTime.now());
        animalMother= new Animal(UUID.randomUUID(),"Topa Mother","Female",20,500,20,null,null,LocalDateTime.now());
        testNewAnimalToAdd = new Animal(UUID.randomUUID(),"Topo Son","Male",20,500,20,animalMother.getId(),animalFather.getId(), LocalDateTime.now());

        animalListTest.add(animalFather);
        animalListTest.add(animalMother);
        animalListTest.add(testNewAnimalToAdd);

        when(zooRepository.findById(animalFather.getId())).thenReturn(Optional.ofNullable(animalFather));
        when(zooRepository.findById(animalMother.getId())).thenReturn(Optional.ofNullable(animalMother));
        when(zooRepository.findAll()).thenReturn(animalListTest);

    }

    private void createAnimalMotherMale(){
        animalListTest = new ArrayList<>();
        animalFather= new Animal(UUID.randomUUID(),"Topa Father","Male",20,500,20,null,null, LocalDateTime.now());
        animalMother= new Animal(UUID.randomUUID(),"Topa Mother","Male",20,500,20,null,null,LocalDateTime.now());

        animalListTest.add(animalFather);
        animalListTest.add(animalMother);

        when(zooRepository.findById(animalFather.getId())).thenReturn(Optional.ofNullable(animalFather));
        when(zooRepository.findById(animalMother.getId())).thenReturn(Optional.ofNullable(animalMother));
        when(zooRepository.findAll()).thenReturn(animalListTest);

    }

    @Test
    public void testCreateAnimal(){
        Animal animal = new Animal(UUID.randomUUID(),"Topo","Male",20,500,20,null,null, LocalDateTime.now());

        when(zooRepository.save(ArgumentMatchers.any())).thenReturn(animal);

        Animal user = zooService.createAnimal(animal);
        assertEquals(animal,user);
    }

    @Test
    public void testGetAnimals(){
        when(zooRepository.findAll()).thenReturn(new ArrayList<Animal>());
        List<Animal> users = zooService.getAnimals();
        assertTrue(users.isEmpty());
    }

    @Test
    public void testGetAnimalByNameWithParents(){
        createAnimalsParentsAndSon();

        AnimalWithParentsDTO animalResult= zooService.getAnimalByName("Topo Son");
        assertEquals(animalResult.getName(),testNewAnimalToAdd.getName());
    }


    @Test
    public void testVerifyNameRepeated(){
        createAnimalsParentsAndSon();

        Animal animalToAdd = new Animal(UUID.randomUUID(),"Topo Son","Male",20,500,20,animalMother.getId(),animalFather.getId(), LocalDateTime.now());

        when(zooRepository.save(ArgumentMatchers.any())).thenReturn(animalToAdd);

        try{
            zooService.createAnimal(animalToAdd);
            fail();
        }catch (AnimalDemoException animalDemoException){
            assertEquals(animalDemoException.getError().getMessage(),"Throw AnimalDemoException");
        }
    }

    @Test
    public void testVerifyFatherIsMale(){
        Animal animalFather= new Animal(UUID.randomUUID(),"Topa Father","Female",20,500,20,null,null, LocalDateTime.now());
        when(zooRepository.findById(animalFather.getId())).thenReturn(Optional.ofNullable(animalFather));

        Animal animalToAdd = new Animal(UUID.randomUUID(),"Topito","Male",20,500,20,null,animalFather.getId(), LocalDateTime.now());
        when(zooRepository.save(ArgumentMatchers.any())).thenReturn(animalToAdd);

        try{
            zooService.createAnimal(animalToAdd);
            fail();
        }catch (AnimalDemoException animalDemoException){
            assertEquals(animalDemoException.getError().getMessage(),"Throw AnimalDemoException");
        }
    }

    @Test
    public void testVerifyFatherExist(){

        Animal animalToAdd = new Animal(UUID.randomUUID(),"Topito","Male",20,500,20,null,UUID.randomUUID(), LocalDateTime.now());
        when(zooRepository.save(ArgumentMatchers.any())).thenReturn(animalToAdd);

        try{
            zooService.createAnimal(animalToAdd);
            fail();
        }catch (AnimalDemoException animalDemoException){
            assertEquals(animalDemoException.getError().getMessage(),"Throw AnimalDemoException");
        }
    }
    @Test
    public void testVerifyFatherCorrect(){
        Animal animalFather= new Animal(UUID.randomUUID(),"Topa Father","Male",20,500,20,null,null, LocalDateTime.now());
        when(zooRepository.findById(animalFather.getId())).thenReturn(Optional.ofNullable(animalFather));

        Animal animalToAdd = new Animal(UUID.randomUUID(),"Topito","Male",20,500,20,null,animalFather.getId(), LocalDateTime.now());
        when(zooRepository.save(ArgumentMatchers.any())).thenReturn(animalToAdd);
    }

    @Test
    public void testVerifyMotherIsFemale(){
        Animal animalMother= new Animal(UUID.randomUUID(),"Topa Mother","Male",20,500,20,null,null, LocalDateTime.now());
        when(zooRepository.findById(animalMother.getId())).thenReturn(Optional.ofNullable(animalMother));

        Animal animalToAdd = new Animal(UUID.randomUUID(),"Topito","Male",20,500,20, animalMother.getId(),null, LocalDateTime.now());
        when(zooRepository.save(ArgumentMatchers.any())).thenReturn(animalToAdd);

        try{
            zooService.createAnimal(animalToAdd);
            fail();
        }catch (AnimalDemoException animalDemoException){
            assertEquals(animalDemoException.getError().getMessage(),"Throw AnimalDemoException");
        }
    }

    @Test
    public void testVerifyMotherExist(){

        Animal animalToAdd = new Animal(UUID.randomUUID(),"Topito","Male",20,500,20, UUID.randomUUID(),null, LocalDateTime.now());
        when(zooRepository.save(ArgumentMatchers.any())).thenReturn(animalToAdd);

        try{
            zooService.createAnimal(animalToAdd);
            fail();
        }catch (AnimalDemoException animalDemoException){
            assertEquals(animalDemoException.getError().getMessage(),"Throw AnimalDemoException");
        }
    }

    @Test
    public void testVerifyMotherCorrect(){
        Animal animalMother= new Animal(UUID.randomUUID(),"Topa Mother","Male",20,500,20,null,null, LocalDateTime.now());
        when(zooRepository.findById(animalMother.getId())).thenReturn(Optional.ofNullable(animalMother));

        Animal animalToAdd = new Animal(UUID.randomUUID(),"Topito","Male",20,500,20,animalMother.getId(),null, LocalDateTime.now());
        when(zooRepository.save(ArgumentMatchers.any())).thenReturn(animalToAdd);
    }


}
