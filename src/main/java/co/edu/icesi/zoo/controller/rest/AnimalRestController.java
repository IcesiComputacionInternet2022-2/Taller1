package co.edu.icesi.zoo.controller.rest;

import co.edu.icesi.zoo.api.AnimalZooAPI;
import co.edu.icesi.zoo.constant.AnimalErrorCode;
import co.edu.icesi.zoo.constant.BurmesePython;
import co.edu.icesi.zoo.constant.UtilConstants;
import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalNoParentsDTO;
import co.edu.icesi.zoo.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoo.mapper.AnimalMapper;
import co.edu.icesi.zoo.service.AnimalService;
import co.edu.icesi.zoo.utils.AnimalExceptionUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class AnimalRestController implements AnimalZooAPI {

    public final AnimalService animalService;
    public final AnimalMapper animalMapper;

    @Override
    public AnimalDTO createAnimal(AnimalDTO animalDTO) {

        validateAnimalName(animalDTO.getName(), 1, BurmesePython.MAX_LENGTH_NAME, BurmesePython.REGEX_FOR_NAME);
        validateDate(animalDTO.getArrivalDate());
        validatePythonCharacteristic(animalDTO.getAge(), 0, BurmesePython.MAX_AGE);
        validatePythonCharacteristic(animalDTO.getHeight(), 0, BurmesePython.MAX_HEIGHT);
        validatePythonCharacteristic(animalDTO.getWeight(), 0, BurmesePython.MAX_WEIGHT);
        validateParseableID(animalDTO.getMotherId());
        validateParseableID(animalDTO.getFatherId());

        return animalMapper.fromAnimal(animalService.createAnimal(animalMapper.fromDTO(animalDTO)));

    }

    @Override
    public List<AnimalNoParentsDTO> getAnimals(){
        return animalService.getAnimals().stream().map(animalMapper::fromAnimalToNoParents).collect(Collectors.toList());
    }

    @Override
    public AnimalWithParentsDTO getAnimalWithParents(String animalName) {
        return animalService.getAnimalWithParents(animalName);
    }

    /*
     * CONTROLLER VALIDATIONS
     */
    private void validateAnimalName(String name,int minLength,int maxLength, String regex){

        if(name.length()<minLength || name.length() > maxLength || !name.matches(regex))
            AnimalExceptionUtils.throwAnimalException(HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_01);

    }

    private void validateDate(String date){

        try{
            LocalDateTime arrivalDate = LocalDateTime.parse(date);
            LocalDateTime now = LocalDateTime.now();
            if(arrivalDate.isAfter(now))
                AnimalExceptionUtils.throwAnimalException(HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_02_01);
        }catch ( DateTimeParseException e){
            AnimalExceptionUtils.throwAnimalException(HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_02_02);
        }

    }

    private void validatePythonCharacteristic(double characteristic, double min ,double max){


        if(characteristic<=min||characteristic>max)
            AnimalExceptionUtils.throwAnimalException(HttpStatus.BAD_REQUEST,AnimalErrorCode.CODE_03);

    }

    private void validateParseableID(String parentId){

        if(parentId!=null&&!parentId.matches(UtilConstants.UUID_REGEX))
            AnimalExceptionUtils.throwAnimalException(HttpStatus.BAD_REQUEST, AnimalErrorCode.CODE_06);


    }

}
