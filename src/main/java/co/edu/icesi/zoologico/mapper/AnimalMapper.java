package co.edu.icesi.zoologico.mapper;

import co.edu.icesi.zoologico.dto.AnimalDTO;
import co.edu.icesi.zoologico.dto.AnimalParentsDTO;
import co.edu.icesi.zoologico.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoologico.model.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

     Animal fromDTO(AnimalDTO animalDTO);

     AnimalDTO fromAnimal(Animal animal);




     AnimalParentsDTO fromAnimalParent(Animal animalParent);

     @Mapping(source = "animal.id", target = "id")
     @Mapping(source = "animal.name", target = "name")
     @Mapping(source = "animal.gender", target = "gender")
     @Mapping(source = "animal.weight", target = "weight")
     @Mapping(source = "animal.age", target = "age")
     @Mapping(source = "animal.height", target = "height")
     @Mapping(source = "animal.arrivalDate", target = "arrivalDate")
     @Mapping(source = "mother", target = "mother")
     @Mapping(source = "father", target = "father")
     AnimalWithParentsDTO fromAnimalAndParentsDTO(Animal animal, AnimalParentsDTO mother, AnimalParentsDTO father);


}
