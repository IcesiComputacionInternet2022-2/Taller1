package co.edu.icesi.zoo.service;

import co.edu.icesi.zoo.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoo.model.Animal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface AnimalService {

    public Animal createAnimal(@RequestBody Animal animalDTO);
    public List<Animal> getAnimals();
    public AnimalWithParentsDTO getAnimalWithParents(@PathVariable String animalName);






}
