package co.edu.icesi.zoo.service.impl;

import co.edu.icesi.zoo.error.exception.AnimalError;
import co.edu.icesi.zoo.error.exception.AnimalException;
import co.edu.icesi.zoo.model.Animal;
import co.edu.icesi.zoo.repository.AnimalRepository;
import co.edu.icesi.zoo.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static co.edu.icesi.zoo.constant.AnimalErrorCode.*;
import static co.edu.icesi.zoo.constant.Jabiru.*;

@AllArgsConstructor
@Service
public class AnimalServiceImpl implements AnimalService {

    public final AnimalRepository animalRepository;

    @Override
    public Animal getAnimal(UUID animalId) {
        return animalRepository.findById(animalId).orElse(null);
    }

    @Override
    public Animal getAnimal(String animalName) {
        return animalRepository.findByName(animalName).orElse(null);
    }

    @Override
    public Animal createAnimal(Animal animal) {
        validateWeight(animal.getWeight());
        validateAge(animal.getAge());
        validateHeight(animal.getHeight());
        validateParentsGender(animal.getFatherId(), animal.getMotherId());
        validateNameUniqueness(animal.getName());
        return animalRepository.save(animal);
    }

    @Override
    public List<Animal> getAnimals() {
        return StreamSupport.stream(animalRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }
    private void validateAge(int age){
        if( age < AGE.getMin() || age > AGE.getMax() ){
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_S2, CODE_S2.getErrorMessage()) );
        }
    }

    private void validateWeight(double weight){
        if( weight < WEIGHT.getMin() || weight > WEIGHT.getMax() ){
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_S1, CODE_S1.getErrorMessage()) );
        }
    }


    private void validateHeight(double height){
        if( height < HEIGHT.getMin() || height > HEIGHT.getMax() ){
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_S3, CODE_S3.getErrorMessage()) );
        }
    }

    private void validateParentsGender(UUID fatherId, UUID motherId){

        if(fatherId != null && motherId != null){
            Animal father = animalRepository.findById(fatherId).orElse(null);
            Animal mother = animalRepository.findById(motherId).orElse(null);

            if(father.getGender().equals(mother.getGender())){
                throw new AnimalException( HttpStatus.BAD_REQUEST, new AnimalError( CODE_S4, CODE_S4.getErrorMessage() ) );
            }
        }
    }

    private void validateNameUniqueness(String name){
        Optional<Animal> animalSearched = animalRepository.findByName(name);
        if(animalSearched.isPresent()){
            throw new AnimalException( HttpStatus.BAD_REQUEST, new AnimalError( CODE_S5, CODE_S5.getErrorMessage()) );
        }
    }

}
