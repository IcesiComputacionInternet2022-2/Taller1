package co.edu.icesi.zoo.service;

import co.edu.icesi.zoo.model.Animal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface AnimalService {

    public Animal getAnimal(@PathVariable UUID animalId);

    public Animal getAnimal(@PathVariable String animalName);

    public Animal createAnimal(@RequestBody Animal animal);

    public List<Animal> getAnimals();

}
