package com.icesi.edu.zoo.api;

import com.icesi.edu.zoo.dto.AnimalDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/animal")
@CrossOrigin(origins = "http://localhost:3000/")
public interface AnimalAPI {

    @GetMapping("/{animalName}")
    List<AnimalDTO> getAnimal(@PathVariable String animalName);

    @PostMapping()
    AnimalDTO createAnimal(@RequestBody AnimalDTO animalDTO);

    @GetMapping()
    List<AnimalDTO> getAnimals();

}
