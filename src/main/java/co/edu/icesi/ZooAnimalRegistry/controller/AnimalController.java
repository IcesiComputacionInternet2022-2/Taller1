package co.edu.icesi.ZooAnimalRegistry.controller;


import co.edu.icesi.ZooAnimalRegistry.api.AnimalAPI;
import co.edu.icesi.ZooAnimalRegistry.dto.AnimalDTO;
import co.edu.icesi.ZooAnimalRegistry.mapper.AnimalMapper;
import co.edu.icesi.ZooAnimalRegistry.repository.model.Animal;
import co.edu.icesi.ZooAnimalRegistry.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@AllArgsConstructor
public class AnimalController implements AnimalAPI {

    public final AnimalService animalService;
    public final AnimalMapper animalMapper;

    @Override
    public List<AnimalDTO> getAnimals() {
        return animalService.getAnimals().stream().map(animalMapper::fromAnimal).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> getAnimal(UUID animalId) {

        Animal animal;
        Map<String, Object> response = new HashMap<>();

        try {
            animal = animalService.getAnimal(animalId);
        } catch(DataAccessException e) {
            response.put("message", "Error when querying the database");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(animal == null) {
            response.put("message", "The animal with the ID: ".concat(animalId.toString().concat(" does not exist in the database!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Animal>(animal, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createAnimal(@Valid @RequestBody AnimalDTO animal, BindingResult result) {

        AnimalDTO animalNew = null;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {

            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "Field '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            animalNew = animalMapper.fromAnimal(animalService.createAnimal(animalMapper.fromDTO(animal)));
        } catch(DataAccessException e) {
            response.put("message", "Error when inserting into the database");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "The animal has been successfully created!");
        response.put("animal", animalNew);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> update(@Valid @RequestBody AnimalDTO animal, BindingResult result, @PathVariable UUID animalId) {

        Animal animalActual = animalService.getAnimal(animalId);

        Animal animalUpdated = null;

        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {

            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "Field '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (animalActual == null) {
            response.put("message", "Error: could not edit the animal with the ID:"
                    .concat(animalId.toString().concat(" does not exist in the database!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            animalActual.setName(animal.getName());
            animalActual.setSex(animal.getSex());
            animalActual.setWeight(animal.getWeight());
            animalActual.setAge(animal.getAge());
            animalActual.setHeight(animal.getHeight());
            animalActual.setDate(animal.getDate());
            animalActual.setMotherId(animal.getMotherId());
            animalActual.setFatherId(animal.getFatherId());

            animalUpdated = animalService.createAnimal(animalActual);

        } catch (DataAccessException e) {
            response.put("message", "Error updating the animal in the database");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "The animal has been successfully updated!");
        response.put("animal", animalUpdated);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> delete(@PathVariable UUID animalId) {

        Map<String, Object> response = new HashMap<>();

        try {
            animalService.deleteAnimal(animalId);
        } catch (DataAccessException e) {
            response.put("message", "Error removing the animal from the database");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "Animal removed successfully!");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }


}
