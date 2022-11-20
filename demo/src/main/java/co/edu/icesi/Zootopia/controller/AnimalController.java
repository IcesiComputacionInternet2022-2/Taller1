package co.edu.icesi.Zootopia.controller;

import co.edu.icesi.Zootopia.DTO.AnimalDTO;
import co.edu.icesi.Zootopia.api.AnimalAPI;
import co.edu.icesi.Zootopia.mapper.AnimalMapper;
import co.edu.icesi.Zootopia.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
public class AnimalController implements AnimalAPI {

    public final AnimalMapper animalMapper;
    public final AnimalService animalService;

    @CrossOrigin(origins = "*")
    @Override
    public AnimalDTO getAnimalUsingName(String name) {
        return animalMapper.fromAnimal(animalService.getAnimalUsingName(name));
    }

    @CrossOrigin(origins = "*")
    @Override
    public AnimalDTO getAnimalUsingId(UUID id) {
        return animalMapper.fromAnimal(animalService.getAnimalUsingId(id));
    }


    @CrossOrigin(origins = "*")
    @Override
    public ResponseEntity<AnimalDTO> createAnimal(AnimalDTO animalDTO) {
        return new ResponseEntity<AnimalDTO>(animalMapper.fromAnimal(animalService.createAnimal(animalMapper.fromDTO(animalDTO))), HttpStatus.CREATED);
    }


    @CrossOrigin(origins = "*")
    @Override
    public List<AnimalDTO> getAnimals() {
        return animalService.getAnimals().stream().map(animalMapper::fromAnimal).collect(Collectors.toList());
    }
}
