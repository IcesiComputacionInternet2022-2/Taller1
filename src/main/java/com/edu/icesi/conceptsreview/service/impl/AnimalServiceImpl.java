package com.edu.icesi.conceptsreview.service.impl;

import com.edu.icesi.conceptsreview.dto.AnimalParentsDTO;
import com.edu.icesi.conceptsreview.dto.AnimalParentsObjectDTO;
import com.edu.icesi.conceptsreview.error.exception.AnimalError;
import com.edu.icesi.conceptsreview.error.exception.AnimalException;
import com.edu.icesi.conceptsreview.mapper.AnimalMapper;
import com.edu.icesi.conceptsreview.model.Animal;
import com.edu.icesi.conceptsreview.repository.AnimalRepository;
import com.edu.icesi.conceptsreview.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.edu.icesi.conceptsreview.constant.AnimalsErrorCodes.*;
import static com.edu.icesi.conceptsreview.constant.GeneralConstantsForVerifications.*;

@Service
@AllArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    @Override
    public List<Animal> getAnimals() {
        return StreamSupport.stream(animalRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public Animal createAnimal(Animal animalDTO) {

        verifyAnimalNameAlreadyExists(animalDTO);

        verifyAnimalAlreadyExistsByID(animalDTO.getFatherId(), " father id");
        verifyAnimalGenre(animalDTO.getFatherId(), "M", ": father");

        verifyAnimalAlreadyExistsByID(animalDTO.getMotherId(), " mother id");
        verifyAnimalGenre(animalDTO.getMotherId(), "W", ": mother");

        verifyWeight(animalDTO.getWeight());
        verifyLength(animalDTO.getHeight());

        return animalRepository.save(animalDTO);}

    @Override
    public Animal getAnimal(UUID animalId) {
        return animalRepository.findById(animalId).orElseThrow(
                () -> new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_02, CODE_02.getMessage())));
    }

    @Override
    public AnimalParentsObjectDTO getAnimalWithParents(UUID animalId) {
        Animal animalForFill = getAnimal(animalId);
        AnimalParentsObjectDTO animalForReturn = animalMapper.fromAnimalToAnimalParentsObjectDTO(animalForFill);
        animalForReturn.setFather(animalMapper.fromAnimalToAnimalDTO(Optional.ofNullable(animalForFill.getFatherId()).map(this::getAnimal).orElse(null)));
        animalForReturn.setMother(animalMapper.fromAnimalToAnimalDTO(Optional.ofNullable(animalForFill.getMotherId()).map(this::getAnimal).orElse(null)));
        return animalForReturn;
    }

    private void verifyAnimalNameAlreadyExists(Animal animalDTO) {
        List<Animal> animalsCreated = getAnimals();
        for (Animal animal : animalsCreated) {
            if(animal.getName().equalsIgnoreCase(animalDTO.getName())) {
                throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_03, CODE_03.getMessage()));}}}

    private int verifyAnimalAlreadyExistsByID(UUID animalId, String optionalMsg) {
        if(animalId == null || animalId.equals("")) {
            return 0;
        }
        getAnimal(animalId);
        return 0;}

    private int verifyAnimalGenre(UUID animalId, String genre, String optionalMessage) {
        if(animalId == null || animalId.equals("")) {
            return 0;
        }
        Animal animal = getAnimal(animalId);
        if(!animal.getGender().equalsIgnoreCase(genre)) {
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(CODE_04, CODE_04.getMessage() + optionalMessage));}
        return 0;}

    private void verifyWeight(float weight) {
        if(weight > MAX_WEIGHT_KG || weight < MIN_WEIGHT_KG) {
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(CODE_07, CODE_07.getMessage()));}}
    private void verifyLength(float length) {
        if(length > MAX_LENGTH_CM || length < MIN_LENGTH_CM) {
            throw new AnimalException(HttpStatus.BAD_REQUEST,
                    new AnimalError(CODE_08, CODE_08.getMessage()));
        }}}
