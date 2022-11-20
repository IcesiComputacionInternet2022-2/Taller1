package co.edu.icesi.zoologico.service.impl;

import co.edu.icesi.zoologico.constant.AnimalErrorCode;
import co.edu.icesi.zoologico.dto.AnimalParentsDTO;
import co.edu.icesi.zoologico.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoologico.error.exception.AnimalDemoError;
import co.edu.icesi.zoologico.error.exception.AnimalDemoException;
import co.edu.icesi.zoologico.mapper.AnimalMapper;
import co.edu.icesi.zoologico.model.Animal;
import co.edu.icesi.zoologico.constant.Gender;
import co.edu.icesi.zoologico.repository.ZooRepository;
import co.edu.icesi.zoologico.service.ZooService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ZooServiceImpl implements ZooService {


    public final ZooRepository zooRepository;
    private final AnimalMapper animalMapper;

    @Override
    public AnimalWithParentsDTO getAnimalByName(String animalName) {
        List<Animal> animalListDB= StreamSupport.stream(zooRepository.findAll().spliterator(),false).collect(Collectors.toList());
        Animal animal= new Animal();
        Animal animalMother= null;
        Animal animalFather= null;
        System.out.println(animalListDB.size()+" size");
        for (Animal animalDB: animalListDB ) {
            if (animalDB.getName().equals(animalName)){
                animal=animalDB;
                if(animalDB.getMother()!=null){
                    animalMother= getAnimalParent(animalDB.getMother());
                }
                if(animalDB.getFather()!=null){
                    animalFather= getAnimalParent(animalDB.getFather());
                }
                break;}}
        AnimalParentsDTO animalMotherDTO = animalMapper.fromAnimalParent(animalMother);
        AnimalParentsDTO animalFatherDTO = animalMapper.fromAnimalParent(animalFather);

        return animalMapper.fromAnimalAndParentsDTO(animal,animalMotherDTO,animalFatherDTO);
    }

    private Animal getAnimalParent(UUID fatherId){

        return zooRepository.findById(fatherId).orElse(null);}


    @Override
    public Animal createAnimal(Animal animal) {
        verifyNameRepeated(animal.getName());
        verifyFatherIdIsCorrectOrIsFemale(animal.getFather());
        verifyMotherIdIsCorrectOrIsMale(animal.getMother());

        return zooRepository.save(animal);
    }


    @Override
    public List<Animal> getAnimals() {
        return StreamSupport.stream(zooRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    public void verifyNameRepeated(String animalName){
        for (Animal i:getAnimals()) {
            if (i.getName().equals(animalName)){
                throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_06,"Throw AnimalDemoException - Animal name exist in the system"));
            }
        }
    }

    public void verifyMotherIdIsCorrectOrIsMale(UUID animalMotherId){

        if( (animalMotherId!=null) && (!animalMotherId.toString().equals("")) ){

            Animal animalMother =zooRepository.findById(animalMotherId).orElse(null);
            if (animalMother==null){
                throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_07,"Throw AnimalDemoException - Animal's mother does not exist in the system"));
            }
            if (!animalMother.getGender().equals(Gender.GENDER_FEMALE)){
                throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_08,"Throw AnimalDemoException - Animal's mother is male"));
            }
        }
    }


    public void verifyFatherIdIsCorrectOrIsFemale(UUID animalFatherId){

        if ( (animalFatherId!=null) && (!animalFatherId.toString().equals("")) ){
            Animal animalFather = zooRepository.findById(animalFatherId).orElse(null);
            if (animalFather == null) {
                throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_09, "Throw AnimalDemoException - Animal's father does not exist in the system"));
            }
            if (!animalFather.getGender().equals(Gender.GENDER_MALE)) {
                throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_10, "Throw AnimalDemoException - Animal's father is female"));
            }
        }
    }


}
