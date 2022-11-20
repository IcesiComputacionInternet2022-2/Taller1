package co.edu.icesi.zoologico.controller;

import co.edu.icesi.zoologico.api.ZooAPI;
import co.edu.icesi.zoologico.constant.AnimalErrorCode;
import co.edu.icesi.zoologico.dto.AnimalDTO;
import co.edu.icesi.zoologico.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoologico.error.exception.AnimalDemoError;
import co.edu.icesi.zoologico.error.exception.AnimalDemoException;
import co.edu.icesi.zoologico.mapper.AnimalMapper;
import co.edu.icesi.zoologico.service.ZooService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ZooController implements ZooAPI {

    private final AnimalMapper animalMapper;

    private final ZooService zooService;


    @Override
    public AnimalWithParentsDTO getAnimalByName(String animalName) {
        return zooService.getAnimalByName(animalName);
    }


    @Override
    public AnimalDTO createAnimal(AnimalDTO animalDTO) {

        verifyAnimalNameEmpty(animalDTO.getName());
        verifyAnimalNameLength(animalDTO.getName());
        verifyAnimalArrivalDate(animalDTO.getArrivalDate());
        verifyAnimalAge(animalDTO.getAge());
        verifyAnimalHeight(animalDTO.getHeight());
        verifyAnimalWeight(animalDTO.getWeight());
        return animalMapper.fromAnimal(zooService.createAnimal(animalMapper.fromDTO(animalDTO)));

    }


    @Override
    public List<AnimalDTO> getAnimals() {
        return zooService.getAnimals().stream().map(animalMapper::fromAnimal).collect(Collectors.toList());
    }


    private boolean verifyAnimalNameEmpty(String animalName){
            if(animalName==null||animalName.isEmpty()) {
                throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_01, "Throw AnimalDemoException - Animal name empty"));
            }else{
                return true;
            }
    }

    private boolean verifyAnimalNameLength(String animalName){

        if(animalName.length() <= 120  && animalName.matches("^[a-z A-Z]*$")){
            return true;
        }else{
            throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_01,"Throw AnimalDemoException - Animal name have an incorrect format"));
        }
    }

    private boolean verifyAnimalArrivalDate(LocalDateTime arrivalDate){
        LocalDateTime now=LocalDateTime.now();

        if (!now.isAfter(arrivalDate)){
            throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_02,"Throw AnimalDemoException - Wrong animal arrival date"));
        }
            return true;
    }

    private boolean verifyAnimalHeight(Integer animalHeight){
        int minHeightZorroCañeroInCm=10;
        int maxHeightZorroCañeroInCm=90;

        if (animalHeight<maxHeightZorroCañeroInCm&&animalHeight>minHeightZorroCañeroInCm){
            return true;
        }else{
            throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_03,"Throw AnimalDemoException - The height of the animal must be between 10 and 90"));
        }
    }

    private boolean verifyAnimalWeight(Integer animalWeight){
        int minHeightZorroCañeroInKg=10;
        int maxHeightZorroCañeroInKg=50;

        if (animalWeight<maxHeightZorroCañeroInKg&&animalWeight>minHeightZorroCañeroInKg){
            return true;
        }else{
            throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_04,"Throw AnimalDemoException - The weight of the animal must be between 10 and 50"));
        }
    }

    private boolean verifyAnimalAge(Integer animalAge){
        int maxAgeZorroCañeroInYears=90;

        if (animalAge<maxAgeZorroCañeroInYears){
            return true;
        }else{
            throw new AnimalDemoException(HttpStatus.BAD_REQUEST, new AnimalDemoError(AnimalErrorCode.CODE_05,"Throw AnimalDemoException - The age of the animal must be between 0 and 90"));
        }
    }

}


