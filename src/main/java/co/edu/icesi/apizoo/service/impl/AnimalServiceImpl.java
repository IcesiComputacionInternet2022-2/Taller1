package co.edu.icesi.apizoo.service.impl;

import co.edu.icesi.apizoo.dto.AnimalDTO;
import co.edu.icesi.apizoo.model.Animal;
import co.edu.icesi.apizoo.repository.AnimalRepository;
import co.edu.icesi.apizoo.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    public final AnimalRepository animalRepository;

    @Override
    public Animal getAnimalById(UUID animalID) {
        return animalRepository.findById(animalID).orElse(null);
    }

    @Override
    public List<Animal> getAnimals() {
        return StreamSupport.stream(animalRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Animal createAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    @Override
    public Animal updateAnimal(Animal animalDTO) {
        return animalRepository.save(animalDTO);
    }
}
