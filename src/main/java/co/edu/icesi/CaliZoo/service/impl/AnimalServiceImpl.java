package co.edu.icesi.CaliZoo.service.impl;

import co.edu.icesi.CaliZoo.model.Animal;
import co.edu.icesi.CaliZoo.repository.AnimalRepository;
import co.edu.icesi.CaliZoo.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class AnimalServiceImpl implements AnimalService {

    public final AnimalRepository animalRepository;

    @Override
    public Animal getAnimal(UUID animalId) {
        return animalRepository.findById(animalId).orElse(null);
    }

    @Override
    public Animal createAnimal(Animal animalDTO) {
        return animalRepository.save(animalDTO);
    }

    @Override
    public List<Animal> getAnimals() {
        return StreamSupport.stream(animalRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }
}
