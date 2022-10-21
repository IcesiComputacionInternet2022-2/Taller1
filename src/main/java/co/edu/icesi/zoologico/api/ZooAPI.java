package co.edu.icesi.zoologico.api;

import co.edu.icesi.zoologico.dto.AnimalDTO;
import co.edu.icesi.zoologico.dto.AnimalWithParentsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/animals")
public interface ZooAPI {


    @GetMapping("/{animalName}")
    public AnimalWithParentsDTO getAnimalByName(@PathVariable String animalName);

    @PostMapping()
    public AnimalDTO createAnimal(@RequestBody AnimalDTO userDTO);



    @GetMapping
    public List<AnimalDTO> getAnimals();

}