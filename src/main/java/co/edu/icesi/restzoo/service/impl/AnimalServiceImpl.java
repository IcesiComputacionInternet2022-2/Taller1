package co.edu.icesi.restzoo.service.impl;

import co.edu.icesi.restzoo.constant.AnimalErrorCode;
import co.edu.icesi.restzoo.constant.Constants;
import co.edu.icesi.restzoo.error.exception.AnimalError;
import co.edu.icesi.restzoo.error.exception.AnimalException;
import co.edu.icesi.restzoo.model.Animal;
import co.edu.icesi.restzoo.repository.ZooRepository;
import co.edu.icesi.restzoo.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    public final ZooRepository zooRepository;

    @Override
    public Animal getAnimal(UUID animalId) { return zooRepository.findById(animalId).orElse(null); }

    @Override
    public Animal getAnimal(String animalName) { return zooRepository.findByName(animalName).orElse(null); }

    @Override
    public Animal createAnimal(Animal animal) {
        uniqueAnimalName(animal.getName());
        dateIsInThePast(animal.getArrivalDate());

        if (animal.getFather() != null ||
                animal.getMother() != null)
            parentsValidations(animal);
        else {
            animal.setFather(Constants.NO_FATHER.getValue());
            animal.setMother(Constants.NO_MOTHER.getValue());
        }

        return zooRepository.save(animal);
    }

    @Override
    public List<Animal> getAnimals() {
        return StreamSupport.stream(zooRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    private void uniqueAnimalName(String name) {
        if (zooRepository.findByName(name).isPresent())
            throw new AnimalException(HttpStatus.CONFLICT,
                    new AnimalError(AnimalErrorCode.SER_E0x01, AnimalErrorCode.SER_E0x01.getMessage()));
    }

    private void dateIsInThePast(LocalDateTime arrivalDate) {
        if (arrivalDate.isAfter(LocalDateTime.now()))
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(AnimalErrorCode.SER_E0x04, AnimalErrorCode.SER_E0x04.getMessage()));
    }

    private void parentsValidations(Animal animal) {
        exactlyTwoParents(animal);
        parentsExist(animal.getFather(), animal.getMother());
        parentsSexMatch(animal.getFather(), animal.getMother());
    }

    private void exactlyTwoParents(Animal animal) {
        if (animal.getFather() == null || animal.getMother() == null)
            throw new AnimalException(HttpStatus.NOT_ACCEPTABLE,
                    new AnimalError(AnimalErrorCode.SER_E0x02_3, AnimalErrorCode.SER_E0x02_3.getMessage()));
    }

    private void parentsExist(String father, String mother) {
        if (getAnimal(getAnimal(father).getId()) == null)
            throw new AnimalException(HttpStatus.NOT_FOUND,
                    new AnimalError(AnimalErrorCode.SER_E0x02_1, AnimalErrorCode.SER_E0x02_1.getMessage()));
        if (getAnimal(getAnimal(mother).getId()) == null)
            throw new AnimalException(HttpStatus.NOT_FOUND,
                    new AnimalError(AnimalErrorCode.SER_E0x02_2, AnimalErrorCode.SER_E0x02_2.getMessage()));
    }

    private void parentsSexMatch(String father, String mother) {
        if (getAnimal(father).getSex() != 'M' || getAnimal(mother).getSex() != 'F')
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(AnimalErrorCode.SER_E0x03, AnimalErrorCode.SER_E0x03.getMessage()));
    }
}
