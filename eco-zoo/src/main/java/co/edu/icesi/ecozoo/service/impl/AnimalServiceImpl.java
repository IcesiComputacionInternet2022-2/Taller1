package co.edu.icesi.ecozoo.service.impl;

import co.edu.icesi.ecozoo.constant.AnimalErrorCode;
import co.edu.icesi.ecozoo.constant.AnimalSex;
import co.edu.icesi.ecozoo.error.exception.AnimalError;
import co.edu.icesi.ecozoo.error.exception.AnimalException;
import co.edu.icesi.ecozoo.model.Animal;
import co.edu.icesi.ecozoo.repository.AnimalRepository;
import co.edu.icesi.ecozoo.service.AnimalService;
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

    private final AnimalRepository animalRepository;

    @Override
    public Animal getAnimal(UUID animalId) {
        Optional<Animal> animal = animalRepository.findById(animalId);
        animal.orElseThrow(() -> new AnimalException(HttpStatus.NOT_FOUND, new AnimalError(AnimalErrorCode.CODE_01, AnimalErrorCode.CODE_01.getMessage())));
        return animal.get();
    }

    @Override
    public Animal getAnimalByName(String name) {
        Optional<Animal> animal = animalRepository.findByName(name);
        animal.orElseThrow(() -> new AnimalException(HttpStatus.NOT_FOUND, new AnimalError(AnimalErrorCode.CODE_01, AnimalErrorCode.CODE_01.getMessage())));
        return animal.get();
    }

    @Override
    public Animal createAnimal(Animal animal) {
        //Unique name
        validateUniqueName(animal.getName());

        Optional<UUID> fatherID = Optional.ofNullable(animal.getFatherID());
        Optional<UUID> motherID = Optional.ofNullable(animal.getMotherID());

        //Mother validations
        validateMother(motherID);

        //Father validations
        validateFather(fatherID);

        return animalRepository.save(animal);
    }

    @Override
    public List<Animal> getAnimals() {
        return StreamSupport.stream(animalRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    private void validateUniqueName(String name) {
        animalRepository.findByName(name).ifPresent(animal -> {
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(AnimalErrorCode.CODE_05, AnimalErrorCode.CODE_05.getMessage()));
        });
    }

    private void validateMother(Optional<UUID> motherID) {
        motherID.ifPresent(motherId -> {
            //Mother exists
            Animal mother = animalRepository.findById(motherId).orElseThrow(() -> new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(AnimalErrorCode.CODE_02, AnimalErrorCode.CODE_02.getMessage())));
            //Mother is a female
            if (mother.isSex() != AnimalSex.FEMALE.isValue())
                throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(AnimalErrorCode.CODE_03, AnimalErrorCode.CODE_03.getMessage()));
        });
    }

    private void validateFather(Optional<UUID> fatherID) {
        fatherID.ifPresent(fatherId -> {
            //Father exists
            Animal father = animalRepository.findById(fatherId).orElseThrow(() -> new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(AnimalErrorCode.CODE_02, AnimalErrorCode.CODE_02.getMessage())));
            //Father is a male
            if (father.isSex() != AnimalSex.MALE.isValue())
                throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(AnimalErrorCode.CODE_04, AnimalErrorCode.CODE_04.getMessage()));
        });
    }
}

