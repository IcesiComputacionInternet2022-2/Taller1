package co.edu.icesi.apizoo.controller;

import co.edu.icesi.apizoo.api.AnimalAPI;
import co.edu.icesi.apizoo.dto.AnimalDTO;
import co.edu.icesi.apizoo.mapper.AnimalMapper;
import co.edu.icesi.apizoo.model.Animal;
import co.edu.icesi.apizoo.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import co.edu.icesi.apizoo.root.exception.AnimalError;
import co.edu.icesi.apizoo.root.exception.AnimalException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class AnimalController implements AnimalAPI {

    public final AnimalService animalService;
    public final AnimalMapper animalMapper;

    @Override
    public AnimalDTO getAnimalById(UUID animalID) {
        return animalMapper.toAnimalDTO(animalService.getAnimalById(animalID));
    }

    @Override
    public List<AnimalDTO> getAnimals() {
        return animalService.getAnimals().stream().map(animalMapper::toAnimalDTO).collect(Collectors.toList());
    }

    @Override
    public AnimalDTO createAnimal(AnimalDTO animalDTO) {
        if(validateLengthName(animalDTO.getName()) && validateCharactersName(animalDTO.getName()) && validUniqueName(animalDTO.getName()) && validateAge(animalDTO.getAge()) && validateWeight(animalDTO.getWeight()) && validateHeight(animalDTO.getHeight()) && validateArrivalDate(animalDTO.getArrivalDate())){
            return animalMapper.toAnimalDTO(animalService.createAnimal(animalMapper.toAnimal(animalDTO)));
        }else{
            return null;
        }
    }

    private boolean validateLengthName(String name) {
        if (name.length() <= 120){
            return true;
        }else {
            throw new AnimalException(HttpStatus.NOT_ACCEPTABLE, new AnimalError("406","The name can only be up to 120 characters including spaces."));
        }
    }

    private boolean validateArrivalDate(LocalDateTime arrivalDate) {
        if(arrivalDate.isBefore(LocalDateTime.now())){
            return true;
        }else{
            throw new AnimalException(HttpStatus.NOT_ACCEPTABLE, new AnimalError("406", "the date of arrival should be earlier than the current one"));
        }
    }

    private boolean validateWeight(Integer weight) {
        if(weight >= 50 && weight <= 140){
            return true;
        }else{
            throw new AnimalException(HttpStatus.NOT_ACCEPTABLE, new AnimalError("406", "Weight must be between 50 and 140 kg"));
        }
    }

    private boolean validateHeight(Integer height) {
        if(height >= 53 && height <= 120){
            return true;
        }else{
            throw new AnimalException(HttpStatus.NOT_ACCEPTABLE, new AnimalError("406", "Height must be between 53 and 120 cm"));
        }
    }

    private boolean validateAge(Integer age) {
        if (age <= 20){
            return true;
        }else{
            throw new AnimalException(HttpStatus.NOT_ACCEPTABLE, new AnimalError("406","Age should be less than or equal to 20 years old"));
        }

    }

    private boolean validateCharactersName(String name) {
        Pattern range = Pattern.compile("[^A-Za-z ]");
        Matcher validName = range.matcher(name);
        if(!validName.find()){
            return true;
        }else {
            throw new AnimalException(HttpStatus.NOT_ACCEPTABLE, new AnimalError("406","The name can only have letters and spaces"));
        }
    }

    private boolean validUniqueName(String name) {
        boolean result = true;
        List<AnimalDTO> animals = getAnimals();
        for (AnimalDTO animalDTO : animals){
            if(animalDTO.getName().equalsIgnoreCase(name)){
                throw new AnimalException(HttpStatus.NOT_ACCEPTABLE, new AnimalError("406","The name must be unique"));
            }
        }
        return result;
    }

    @Override
    public AnimalDTO updateAnimal(AnimalDTO animalDTO, String animalID) {
        Animal animal = animalService.getAnimalById(UUID.fromString(animalID));

        if(validateSexParents(animal)){
            animal.setFather(animalDTO.getFatherID());
            animal.setMother(animalDTO.getMotherID());
        }

        return animalMapper.toAnimalDTO(animalService.updateAnimal(animal));
    }

    private boolean validateSexParents(Animal animal) {
        if(animalService.getAnimalById(animal.getFather()).getSex().equalsIgnoreCase("male")){
            if (animalService.getAnimalById(animal.getMother()).getSex().equalsIgnoreCase("female")){
                return true;
            }else{
                throw new AnimalException(HttpStatus.NOT_ACCEPTABLE, new AnimalError("406", "The mother should be female"));
            }
        }else{
            throw new AnimalException(HttpStatus.NOT_ACCEPTABLE, new AnimalError("406", "The father should be male"));
        }
    }
}
