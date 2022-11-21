package co.edu.icesi.ZooAnimalRegistry.service.impl;

import co.edu.icesi.ZooAnimalRegistry.repository.AnimalRepository;
import co.edu.icesi.ZooAnimalRegistry.repository.model.Animal;
import co.edu.icesi.ZooAnimalRegistry.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class AnimalServiceImpl implements AnimalService {

    public final AnimalRepository animalRepository;

    @Override
    @Transactional(readOnly = true)
    public Animal getAnimal(UUID animalId) {
        return animalRepository.findById(animalId).orElse(null);
    }

    @Override
    @Transactional
    public Animal createAnimal(Animal animalDTO) {
        return animalRepository.save(animalDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Animal> getAnimals() {
        return StreamSupport.stream(animalRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteAnimal(UUID animalId) {
        animalRepository.deleteById(animalId);
    }
}
