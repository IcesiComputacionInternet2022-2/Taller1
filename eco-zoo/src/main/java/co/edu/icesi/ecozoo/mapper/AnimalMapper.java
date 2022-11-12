package co.edu.icesi.ecozoo.mapper;

import co.edu.icesi.ecozoo.constant.AnimalSex;
import co.edu.icesi.ecozoo.dto.AnimalDTO;
import co.edu.icesi.ecozoo.dto.AnimalResponseDTO;
import co.edu.icesi.ecozoo.dto.CapybaraDTO;
import co.edu.icesi.ecozoo.model.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    Animal fromDTO(AnimalDTO animalDTO);

    CapybaraDTO animalToCapybara(Animal animal);

    Animal capybaraToAnimal(CapybaraDTO capybaraDTO);

    @Mapping(source = "child.id", target = "id")
    @Mapping(source = "child.name", target = "name")
    @Mapping(source = "child.sex", target = "sex")
    @Mapping(source = "child.age", target = "age")
    @Mapping(source = "child.weight", target = "weight")
    @Mapping(source = "child.height", target = "height")
    @Mapping(source = "child.arrivalDate", target = "arrivalDate")
    @Mapping(source = "father", target = "father")
    @Mapping(source = "mother", target = "mother")
    AnimalResponseDTO toAnimalResponseDTO(Animal child, Animal father, Animal mother);

    default boolean sexAnimalToBoolean(AnimalSex sex) {
        return sex.isValue();
    }

    default AnimalSex booleanToSexAnimalT(boolean sex) {
        return sex ? AnimalSex.FEMALE:AnimalSex.MALE;
    }
}