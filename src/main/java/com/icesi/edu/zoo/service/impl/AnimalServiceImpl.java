package com.icesi.edu.zoo.service.impl;

import com.icesi.edu.zoo.constant.CondorCharacteristics;
import com.icesi.edu.zoo.error.exception.AnimalError;
import com.icesi.edu.zoo.error.exception.AnimalException;
import com.icesi.edu.zoo.model.Animal;
import com.icesi.edu.zoo.repository.AnimalRepository;
import com.icesi.edu.zoo.service.AnimalService;
import static com.icesi.edu.zoo.constant.AnimalErrorCode.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
@Primary
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;

    @Override
    public List<Animal> getAnimal(String animalName) {
        return createFamilyList(animalName);
    }

    private List<Animal> createFamilyList(String animalName) {
        List<Animal> family = new ArrayList<>();
        Animal child = getAnimalByName(animalName);
        if(child != null) {
            family.add(child);
            if(child.getMaleParentName() != null)
                family.add(animalRepository.findById(child.getMaleParentName()).orElse(null));
            if(child.getMaleParentName() != null)
                family.add(animalRepository.findById(child.getFemaleParentName()).orElse(null));
            family.removeAll(Collections.singleton(null));
        }
        return family;
    }

    private Animal getAnimalByName(String animalName) {
        return getAnimals().stream().peek(System.out::println).filter(a -> a.getName().equalsIgnoreCase(animalName)).findFirst().orElse(null);
    }

    @Override
    public Animal createAnimal(Animal animalDTO) {
        if(animalExists(animalDTO.getName()))
            throw new AnimalException(HttpStatus.I_AM_A_TEAPOT, new AnimalError(CODE_O5, CODE_O5.getMessage()));
        if(!animalExists(animalDTO.getMaleParentName()))
            throw new AnimalException(HttpStatus.NOT_FOUND, new AnimalError(CODE_06, CODE_06.getMessage()));
        if(!animalExists(animalDTO.getFemaleParentName()))
            throw new AnimalException(HttpStatus.NOT_FOUND, new AnimalError(CODE_06, CODE_06.getMessage()));
        parentsAreDifferent(animalDTO.getMaleParentName(), animalDTO.getFemaleParentName());
        checkParentSex(animalDTO.getMaleParentName(), "m");
        checkParentSex(animalDTO.getFemaleParentName(), "h");
        nameIsAvailable(animalDTO.getName());
        checkCharacteristics(animalDTO);
        return animalRepository.save(animalDTO);
    }

    private boolean animalExists(String animalName) {
        return animalName == null || animalRepository.findById(animalName).isPresent();
    }

    private boolean checkParentSex(String animalName, String sex) {
        if(animalName == null)
            return true;
        Animal parent = animalRepository.findById(animalName).orElse(null);
        if(Character.toString(parent.getSex()).equalsIgnoreCase(sex))
            return true;
        throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_08, CODE_08.getMessage()));
    }

    private boolean parentsAreDifferent(String maleName, String femaleName) {
        if(maleName == null || femaleName == null || maleName.compareTo(femaleName) != 0)
            return true;
        throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_07, CODE_07.getMessage()));
    }

    private boolean nameIsAvailable(String animalName) {
        if(getAnimals().stream().noneMatch(a -> a.getName().equalsIgnoreCase(animalName)))
            return true;
        throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_09, CODE_09.getMessage()));
    }

    private boolean checkCharacteristics(final Animal animalDTO) {
        double height = animalDTO.getHeight();
        double weight = animalDTO.getWeight();
        if(hasValidCharacteristic(height, CondorCharacteristics.HEIGHT) && hasValidCharacteristic(weight, CondorCharacteristics.WEIGHT))
            return true;
        throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_10, CODE_10.getMessage()));
    }

    private <T extends CondorCharacteristics> boolean hasValidCharacteristic(double attr, T validRange) {
        return inClosedRange(attr, validRange.getMin(), validRange.getMax());
    }

    private boolean inClosedRange(double num, double min, double max) {
        return (num >= min) && (num <= max);
    }

    @Override
    public List<Animal> getAnimals() {
        return StreamSupport.stream(animalRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

}
