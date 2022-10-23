package co.edu.icesi.ecozoo.controller;

import co.edu.icesi.ecozoo.api.CapybaraAPI;
import co.edu.icesi.ecozoo.dto.AnimalDTO;
import co.edu.icesi.ecozoo.dto.AnimalResponseDTO;
import co.edu.icesi.ecozoo.dto.CapybaraDTO;
import co.edu.icesi.ecozoo.mapper.AnimalMapper;
import co.edu.icesi.ecozoo.model.Animal;
import co.edu.icesi.ecozoo.service.AnimalService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
public class AnimalController implements CapybaraAPI {

    private AnimalMapper animalMapper;
    private AnimalService animalService;

    @Override
    public AnimalResponseDTO getAnimal(UUID capybaraId) {

        Animal child = animalService.getAnimal(capybaraId);

        return getAnimalResponse(child);
    }

    @Override
    public AnimalResponseDTO getAnimalByName(String capybaraName) {

        Animal child = animalService.getAnimalByName(capybaraName);

        return getAnimalResponse(child);
    }

    @Override
    public CapybaraDTO createAnimal(CapybaraDTO capybaraDTO) {
        return animalMapper.animalToCapybara(animalService.createAnimal(animalMapper.capybaraToAnimal(capybaraDTO)));
    }

    @Override
    public List<CapybaraDTO> getAnimals() {
        return animalService.getAnimals().stream().map(animalMapper::animalToCapybara).collect(Collectors.toList());
    }

    private AnimalResponseDTO getAnimalResponse(Animal child) {
        AtomicReference<Animal> mother = new AtomicReference<>();
        AtomicReference<Animal> father = new AtomicReference<>();

        Optional.ofNullable(child).map(Animal::getMotherID).ifPresent(motherId -> {
            mother.set(animalService.getAnimal(motherId));
        });
        Optional.ofNullable(child).map(Animal::getFatherID).ifPresent(fatherId -> {
            father.set(animalService.getAnimal(fatherId));
        });

        return animalMapper.toAnimalResponseDTO(child, father.get(), mother.get());
    }
}
