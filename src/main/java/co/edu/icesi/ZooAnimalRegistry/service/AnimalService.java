package co.edu.icesi.ZooAnimalRegistry.service;

import co.edu.icesi.ZooAnimalRegistry.repository.model.Animal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface
AnimalService {
    public Animal getAnimal(@PathVariable UUID animalId);
    public Animal createAnimal(@RequestBody Animal animalDTO);
    public List<Animal> getAnimals();
    public void deleteAnimal(@PathVariable UUID animalId);

}
