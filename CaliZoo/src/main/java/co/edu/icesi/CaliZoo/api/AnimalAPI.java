package co.edu.icesi.CaliZoo.api;

import co.edu.icesi.CaliZoo.dto.AnimalDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/animals")
public interface AnimalAPI {

    @GetMapping("/{animalId}")
    public AnimalDTO getAnimal(@PathVariable UUID animalId);

    @PostMapping()
    public AnimalDTO createAnimal(@RequestBody AnimalDTO userDTO);

    @GetMapping
    public List<AnimalDTO> getAnimals();
}
