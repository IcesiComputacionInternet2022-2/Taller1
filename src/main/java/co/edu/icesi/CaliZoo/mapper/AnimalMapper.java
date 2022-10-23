package co.edu.icesi.CaliZoo.mapper;

import co.edu.icesi.CaliZoo.dto.AnimalDTO;
import co.edu.icesi.CaliZoo.model.Animal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    Animal fromDTO(AnimalDTO userDTO);
    AnimalDTO fromAnimal(Animal user);
}
