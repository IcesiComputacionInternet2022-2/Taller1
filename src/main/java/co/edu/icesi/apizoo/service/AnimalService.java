package co.edu.icesi.apizoo.service;

import co.edu.icesi.apizoo.dto.AnimalDTO;
import co.edu.icesi.apizoo.model.Animal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;


public interface AnimalService {

    Animal getAnimalById(@PathVariable UUID animalID);

    List<Animal> getAnimals();

    Animal createAnimal(@RequestBody Animal animalDTO);

    Animal updateAnimal(@RequestBody Animal animalDTO);
}
