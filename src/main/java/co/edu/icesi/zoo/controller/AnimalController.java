package co.edu.icesi.zoo.controller;

import co.edu.icesi.zoo.api.AnimalAPI;
import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalNoParentsDTO;
import co.edu.icesi.zoo.dto.AnimalSearchDTO;
import co.edu.icesi.zoo.error.exception.AnimalError;
import co.edu.icesi.zoo.error.exception.AnimalException;
import co.edu.icesi.zoo.mapper.AnimalMapper;
import co.edu.icesi.zoo.model.Animal;
import co.edu.icesi.zoo.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.edu.icesi.zoo.constant.AnimalErrorCode.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AnimalController implements AnimalAPI{

    private final int NAME_MAX_LENGTH = 120;
    private final String NAME_RESTRICTION = "^[a-zA-Z\\s]+$";
    private final String GENDER_OPTIONS = "^[M|F]$";

    public final AnimalService animalService;
    public final AnimalMapper animalMapper;

    @Override
    public AnimalSearchDTO getAnimal(UUID animalId) {
        Animal animal = animalService.getAnimal(animalId);
        AnimalNoParentsDTO[] parents = validateParents(animal);

        return animalMapper.fromAnimalToSearchDTO(animal, parents[0], parents[1]);
    }

    @Override
    public AnimalSearchDTO getAnimal(String animalName) {
        Animal animal = animalService.getAnimal(animalName);
        AnimalNoParentsDTO[] parents = validateParents(animal);

        return animalMapper.fromAnimalToSearchDTO(animal, parents[0], parents[1]);
    }

    @Override
    public AnimalDTO createAnimal(AnimalDTO animalDTO) {
        validateNameSize(animalDTO.getName());
        validateNameCharacters(animalDTO.getName());
        validateGender(animalDTO.getGender());
        validateArrivalDate(animalDTO.getArrivalDate());
        return animalMapper.fromAnimalToDTO(animalService.createAnimal(animalMapper.fromDTO(animalDTO)));
    }

    @Override
    public List<AnimalDTO> getAnimals() {
        return animalService.getAnimals().stream().map(animalMapper::fromAnimalToDTO).collect(Collectors.toList());
    }

    private void validateNameSize(String name){
        if (name.length() > NAME_MAX_LENGTH){
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_C11, CODE_C11.getErrorMessage()));
        }
    }

    private void validateNameCharacters(String name){
        if (name.matches(NAME_RESTRICTION) == false) {
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_C12, CODE_C12.getErrorMessage()));
        }
    }

    private void validateGender(String gender){
        if (gender.matches(GENDER_OPTIONS) == false) {
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_C2, CODE_C2.getErrorMessage()));
        }
    }

    private void validateArrivalDate(LocalDateTime arrivalDate){
        if (arrivalDate.compareTo(LocalDateTime.now()) > 0) {
            //For some reason 'compareTo()' is not affected by hour, minutes or second difference
            System.out.println(LocalDateTime.now());
            throw new AnimalException(HttpStatus.BAD_REQUEST, new AnimalError(CODE_C3, CODE_C3.getErrorMessage()));
        }
    }

    private AnimalNoParentsDTO[] validateParents(Animal animal) {
        AnimalNoParentsDTO[] parents = {null, null};

        if (animal.getFatherId() != null) {
            parents[0] = animalMapper.fromAnimalToNoParentsDTO(animalService.getAnimal(animal.getFatherId()));
        }

        if (animal.getMotherId() != null) {
            parents[1] = animalMapper.fromAnimalToNoParentsDTO(animalService.getAnimal(animal.getMotherId()));
        }

        return parents;
    }

}
