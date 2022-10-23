package co.edu.icesi.apizoo.mapper;

import co.edu.icesi.apizoo.dto.AnimalDTO;
import co.edu.icesi.apizoo.model.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimalMapper {


    @Mapping(target = "mother", source = "motherID")
    @Mapping(target = "father", source = "fatherID")
    Animal toAnimal(AnimalDTO animalDTO);


    @Mapping(target = "motherID", source = "mother")
    @Mapping(target = "fatherID", source = "father")
    AnimalDTO toAnimalDTO(Animal animal);
}