package co.edu.icesi.ZooAnimalRegistry.api;

import co.edu.icesi.ZooAnimalRegistry.dto.AnimalDTO;
import co.edu.icesi.ZooAnimalRegistry.repository.model.Animal;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/animals")
public interface AnimalAPI {

    @GetMapping
    public List<AnimalDTO> getAnimals();
    @GetMapping("/{animalId}")
    public ResponseEntity<?> getAnimal(@PathVariable UUID animalId);

    @PostMapping()
    ResponseEntity<?> createAnimal(@Valid @RequestBody AnimalDTO animal, BindingResult result);

    @PutMapping("/{animalId}")
    ResponseEntity<?> update(@Valid @RequestBody AnimalDTO animal, BindingResult result, @PathVariable UUID animalId);

    @DeleteMapping("/{animalId}")
    ResponseEntity<?> delete(@PathVariable UUID animalId);
}
