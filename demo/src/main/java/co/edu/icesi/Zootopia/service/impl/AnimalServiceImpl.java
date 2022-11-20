package co.edu.icesi.Zootopia.service.impl;

import co.edu.icesi.Zootopia.model.Animal;
import co.edu.icesi.Zootopia.repository.AnimalRepository;
import co.edu.icesi.Zootopia.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class AnimalServiceImpl implements AnimalService {

    public final AnimalRepository animalRepository;
    @Override
    public Animal getAnimalUsingName(String name) {
        return animalRepository.findByName(name).orElse(null);
    }

    @Override
    public Animal getAnimalUsingId(UUID id) {
        return animalRepository.findById(id).orElse(null);
    }

    @Override
    public Animal createAnimal(Animal animalDTO) {
        return animalRepository.save(animalDTO);
    }

    @Override
    public List<Animal> getAnimals() {
        return StreamSupport.stream(animalRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }
}
