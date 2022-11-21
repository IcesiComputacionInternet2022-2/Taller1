package co.edu.icesi.zoo.api;

import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalWithParentsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/animals")
public interface AnimalAPI {

    @PostMapping
    AnimalDTO createAnimal(@RequestBody AnimalDTO animalDTO);

    @GetMapping("/{animalName}")
    AnimalWithParentsDTO getAnimal(@PathVariable String animalName);

    @GetMapping
    List<AnimalDTO> getAnimals();
}
