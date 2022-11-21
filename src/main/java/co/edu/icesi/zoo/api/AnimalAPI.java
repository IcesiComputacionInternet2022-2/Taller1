package co.edu.icesi.zoo.api;

import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalSearchDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/animals")
public interface AnimalAPI {

    @GetMapping("/id/{animalId}")
    public AnimalSearchDTO getAnimal(@PathVariable UUID animalId);

    @GetMapping("/name/{animalName}")
    public AnimalSearchDTO getAnimal(@PathVariable String animalName);

    @PostMapping()
    public AnimalDTO createAnimal(@RequestBody AnimalDTO animalDTO);

    @GetMapping
    public List<AnimalDTO> getAnimals();

}
