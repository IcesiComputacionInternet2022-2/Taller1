package co.edu.icesi.Zootopia.api;

import co.edu.icesi.Zootopia.DTO.AnimalDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RequestMapping("/animals")
public interface AnimalAPI {
    @GetMapping("/{name}")
    public AnimalDTO getAnimalUsingName(@PathVariable String name);

    @GetMapping("/{id}")
    public AnimalDTO getAnimalUsingId(@PathVariable UUID id);
    @PostMapping()
    public ResponseEntity<AnimalDTO> createAnimal(@RequestBody  @Valid AnimalDTO animalDTO);

    @GetMapping
    public List<AnimalDTO> getAnimals();
}
