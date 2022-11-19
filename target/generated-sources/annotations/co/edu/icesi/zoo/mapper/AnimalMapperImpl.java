package co.edu.icesi.zoo.mapper;

import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalNoParentsDTO;
import co.edu.icesi.zoo.dto.AnimalSearchDTO;
import co.edu.icesi.zoo.model.Animal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-19T03:45:05-0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.1 (Oracle Corporation)"
)
@Component
public class AnimalMapperImpl implements AnimalMapper {

    @Override
    public Animal fromDTO(AnimalDTO animalDTO) {
        if ( animalDTO == null ) {
            return null;
        }

        Animal.AnimalBuilder animal = Animal.builder();

        animal.id( animalDTO.getId() );
        animal.name( animalDTO.getName() );
        animal.gender( animalDTO.getGender() );
        animal.age( animalDTO.getAge() );
        animal.weight( animalDTO.getWeight() );
        animal.height( animalDTO.getHeight() );
        animal.arrivalDate( animalDTO.getArrivalDate() );
        animal.fatherId( animalDTO.getFatherId() );
        animal.motherId( animalDTO.getMotherId() );

        return animal.build();
    }

    @Override
    public AnimalDTO fromAnimalToDTO(Animal animal) {
        if ( animal == null ) {
            return null;
        }

        AnimalDTO animalDTO = new AnimalDTO();

        animalDTO.setId( animal.getId() );
        animalDTO.setName( animal.getName() );
        animalDTO.setGender( animal.getGender() );
        animalDTO.setAge( animal.getAge() );
        animalDTO.setWeight( animal.getWeight() );
        animalDTO.setHeight( animal.getHeight() );
        animalDTO.setArrivalDate( animal.getArrivalDate() );
        animalDTO.setFatherId( animal.getFatherId() );
        animalDTO.setMotherId( animal.getMotherId() );

        return animalDTO;
    }

    @Override
    public AnimalSearchDTO fromAnimalToSearchDTO(Animal animal, AnimalNoParentsDTO father, AnimalNoParentsDTO mother) {
        if ( animal == null && father == null && mother == null ) {
            return null;
        }

        AnimalSearchDTO animalSearchDTO = new AnimalSearchDTO();

        if ( animal != null ) {
            animalSearchDTO.setId( animal.getId() );
            animalSearchDTO.setName( animal.getName() );
            animalSearchDTO.setGender( animal.getGender() );
            animalSearchDTO.setAge( animal.getAge() );
            animalSearchDTO.setWeight( animal.getWeight() );
            animalSearchDTO.setHeight( animal.getHeight() );
            animalSearchDTO.setArrivalDate( animal.getArrivalDate() );
        }
        animalSearchDTO.setFather( father );
        animalSearchDTO.setMother( mother );

        return animalSearchDTO;
    }

    @Override
    public AnimalNoParentsDTO fromAnimalToNoParentsDTO(Animal animal) {
        if ( animal == null ) {
            return null;
        }

        AnimalNoParentsDTO animalNoParentsDTO = new AnimalNoParentsDTO();

        animalNoParentsDTO.setId( animal.getId() );
        animalNoParentsDTO.setName( animal.getName() );
        animalNoParentsDTO.setGender( animal.getGender() );
        animalNoParentsDTO.setAge( animal.getAge() );
        animalNoParentsDTO.setWeight( animal.getWeight() );
        animalNoParentsDTO.setHeight( animal.getHeight() );
        animalNoParentsDTO.setArrivalDate( animal.getArrivalDate() );

        return animalNoParentsDTO;
    }
}
