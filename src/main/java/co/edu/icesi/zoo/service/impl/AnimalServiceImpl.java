
package co.edu.icesi.zoo.service.impl;

import co.edu.icesi.zoo.constant.AnimalErrorCode;
import co.edu.icesi.zoo.constant.BurmesePython;
import co.edu.icesi.zoo.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoo.mapper.AnimalMapper;
import co.edu.icesi.zoo.model.Animal;
import co.edu.icesi.zoo.repository.AnimalRepository;
import co.edu.icesi.zoo.service.AnimalService;
import co.edu.icesi.zoo.utils.AnimalExceptionUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class AnimalServiceImpl implements AnimalService {

    public final AnimalRepository animalRepository;

    public final AnimalMapper animalMapper;

    @Override
    public Animal createAnimal(Animal animal) {

        validateUniqueName(animal.getName());
        validateParentSex(animal.getMotherId(), BurmesePython.FEMALE);
        validateParentSex(animal.getFatherId(), BurmesePython.MALE);
        return animalRepository.save(animal);

    }

    @Override
    public List<Animal> getAnimals(){
        return StreamSupport.stream(animalRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public AnimalWithParentsDTO getAnimalWithParents(String animalName) {

        Optional<Animal> optionalAnimal = searchByName(animalName);

        if(optionalAnimal.isEmpty())
            return null;

        Animal mother = Optional.ofNullable(optionalAnimal.get().getMotherId()).map(this::getAnimal).orElse(null);
        Animal father = Optional.ofNullable(optionalAnimal.get().getFatherId()).map(this::getAnimal).orElse(null);

        return animalMapper.fromAnimalToWithParents(optionalAnimal.get(), mother, father);

    }

    private Animal getAnimal(UUID animalId){
        return animalRepository.findById(animalId).orElse(null);
    }

    private Optional<Animal> searchByName(String name){

        for(Animal currentAnimal:getAnimals()){
            if(currentAnimal.getName().equals(name))
               return Optional.of(currentAnimal);
        }

        return Optional.empty();
    }

    /*
     * SERVICE VALIDATIONS
     */
    private void validateUniqueName(String name){

        for(Animal currentAnimal:getAnimals()){
            if(currentAnimal.getName().equals(name))
                AnimalExceptionUtils.throwAnimalException(HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_04);
        }

    }

    private void validateParentSex(UUID parentId, char sex){

        if( parentId!=null){
            Optional<Animal> parent = Optional.ofNullable(getAnimal(parentId));
            if(parent.isPresent() && parent.get().getSex() != sex){
                AnimalErrorCode code = (sex==BurmesePython.FEMALE)?AnimalErrorCode.CODE_05_01:AnimalErrorCode.CODE_05_02;
                AnimalExceptionUtils.throwAnimalException(HttpStatus.BAD_REQUEST, code);
            }
        }

    }

}