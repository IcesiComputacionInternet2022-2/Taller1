package co.edu.icesi.zoo.mapper;

import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalNoParentsDTO;
import co.edu.icesi.zoo.dto.AnimalSearchDTO;
import co.edu.icesi.zoo.model.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    Animal fromDTO(AnimalDTO animalDTO);
    AnimalDTO fromAnimalToDTO(Animal animal);

    @Mapping(source = "animal.id", target = "id")
    @Mapping(source = "animal.name", target = "name")
    @Mapping(source = "animal.gender", target = "gender")
    @Mapping(source = "animal.age", target = "age")
    @Mapping(source = "animal.weight", target = "weight")
    @Mapping(source = "animal.height", target = "height")
    @Mapping(source = "animal.arrivalDate", target = "arrivalDate")
    @Mapping(source = "father", target = "father")
    @Mapping(source = "mother", target = "mother")
    AnimalSearchDTO fromAnimalToSearchDTO(Animal animal, AnimalNoParentsDTO father, AnimalNoParentsDTO mother);

    AnimalNoParentsDTO fromAnimalToNoParentsDTO(Animal animal);

}
