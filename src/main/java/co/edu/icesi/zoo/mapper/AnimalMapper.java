package co.edu.icesi.zoo.mapper;

import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalNoParentsDTO;
import co.edu.icesi.zoo.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoo.model.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    Animal fromDTO(AnimalDTO animalDTO);
    AnimalDTO fromAnimal(Animal animal);
    AnimalNoParentsDTO fromAnimalToNoParents(Animal animal);

    @Mapping(source = "animal.animalId", target = "animalId")
    @Mapping(source = "animal.name", target = "name")
    @Mapping(source = "animal.sex", target = "sex")
    @Mapping(source = "animal.weight", target = "weight")
    @Mapping(source = "animal.age", target = "age")
    @Mapping(source = "animal.height", target = "height")
    @Mapping(source = "animal.arrivalDate", target = "arrivalDate")
    @Mapping(source = "mother", target = "motherInfo")
    @Mapping(source = "father", target = "fatherInfo")
    AnimalWithParentsDTO fromAnimalToWithParents(Animal animal, Animal mother, Animal father);




}


