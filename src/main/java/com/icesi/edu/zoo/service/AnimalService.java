package com.icesi.edu.zoo.service;

import com.icesi.edu.zoo.model.Animal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AnimalService {

    List<Animal> getAnimal(@PathVariable String animalName);

    Animal createAnimal(@RequestBody Animal animalDTO);

    List<Animal> getAnimals();

}
