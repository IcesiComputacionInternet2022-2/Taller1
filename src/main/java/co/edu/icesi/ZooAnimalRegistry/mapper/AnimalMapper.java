package co.edu.icesi.ZooAnimalRegistry.mapper;

import co.edu.icesi.ZooAnimalRegistry.dto.AnimalDTO;
import co.edu.icesi.ZooAnimalRegistry.repository.model.Animal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

     Animal fromDTO(AnimalDTO animalDTO);
     AnimalDTO fromAnimal(Animal animal);
}
