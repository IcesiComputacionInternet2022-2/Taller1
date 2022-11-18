package com.icesi.edu.zoo.service.impl;

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
            if(child.getFemaleParentName() != null)
                family.add(animalRepository.findById(child.getFemaleParentName()).orElse(null));
            family.removeAll(Collections.singleton(null));
            return family;
        }
        throw new AnimalException(HttpStatus.NOT_FOUND, new AnimalError(CODE_11, CODE_11.getMessage()));
    }

    private Animal getAnimalByName(String animalName) {
        return getAnimals().stream().filter(a -> a.getName().equalsIgnoreCase(animalName)).findFirst().orElse(null);
    }

    @Override
    public Animal createAnimal(Animal animalDTO) {
        if(animalExists(animalDTO.getName()))
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_O5, CODE_O5.getMessage()));
        if(!parentExists(animalDTO.getMaleParentName()))
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_06, CODE_06.getMessage()));
        if(!parentExists(animalDTO.getFemaleParentName()))
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_06, CODE_06.getMessage()));
        checkParentSex(animalDTO.getMaleParentName(), "m");
        checkParentSex(animalDTO.getFemaleParentName(), "h");
        nameIsAvailable(animalDTO.getName());
        return animalRepository.save(animalDTO);
    }

    private boolean animalExists(String animalName) {
        return animalRepository.findById(animalName).isPresent();
    }

    private boolean parentExists(String animalName) {
        return animalName.isBlank() || animalExists(animalName);
    }

    private void checkParentSex(String animalName, String sex) {
        if(animalName == null)
            return;
        if(animalName.isBlank())
            return;
        Animal parent = animalRepository.findById(animalName).orElse(null);
        if(Character.toString(parent.getSex()).equalsIgnoreCase(sex))
            return;
        throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_08, CODE_08.getMessage()));
    }

    private void nameIsAvailable(String animalName) {
        if(getAnimals().stream().noneMatch(a -> a.getName().equalsIgnoreCase(animalName)))
            return;
        throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_09, CODE_09.getMessage()));
    }

    @Override
    public List<Animal> getAnimals() {
        return StreamSupport.stream(animalRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

}
