package co.edu.icesi.zoo.mapper;

import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.dto.AnimalNoParentsDTO;
import co.edu.icesi.zoo.dto.AnimalWithParentsDTO;
import co.edu.icesi.zoo.model.Animal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-19T15:07:28-0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.16 (Eclipse Adoptium)"
)
@Component
public class AnimalMapperImpl implements AnimalMapper {

    @Override
    public Animal fromDTO(AnimalDTO animalDTO) {
        if ( animalDTO == null ) {
            return null;
        }

        Animal.AnimalBuilder animal = Animal.builder();

        if ( animalDTO.getAnimalId() != null ) {
            animal.animalId( UUID.fromString( animalDTO.getAnimalId() ) );
        }
        animal.name( animalDTO.getName() );
        animal.sex( animalDTO.getSex() );
        animal.weight( animalDTO.getWeight() );
        animal.age( animalDTO.getAge() );
        animal.height( animalDTO.getHeight() );
        if ( animalDTO.getArrivalDate() != null ) {
            animal.arrivalDate( LocalDateTime.parse( animalDTO.getArrivalDate() ) );
        }
        if ( animalDTO.getMotherId() != null ) {
            animal.motherId( UUID.fromString( animalDTO.getMotherId() ) );
        }
        if ( animalDTO.getFatherId() != null ) {
            animal.fatherId( UUID.fromString( animalDTO.getFatherId() ) );
        }

        return animal.build();
    }

    @Override
    public AnimalDTO fromAnimal(Animal animal) {
        if ( animal == null ) {
            return null;
        }

        AnimalDTO.AnimalDTOBuilder animalDTO = AnimalDTO.builder();

        if ( animal.getAnimalId() != null ) {
            animalDTO.animalId( animal.getAnimalId().toString() );
        }
        animalDTO.name( animal.getName() );
        animalDTO.sex( animal.getSex() );
        animalDTO.weight( animal.getWeight() );
        animalDTO.age( animal.getAge() );
        animalDTO.height( animal.getHeight() );
        if ( animal.getArrivalDate() != null ) {
            animalDTO.arrivalDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( animal.getArrivalDate() ) );
        }
        if ( animal.getMotherId() != null ) {
            animalDTO.motherId( animal.getMotherId().toString() );
        }
        if ( animal.getFatherId() != null ) {
            animalDTO.fatherId( animal.getFatherId().toString() );
        }

        return animalDTO.build();
    }

    @Override
    public AnimalNoParentsDTO fromAnimalToNoParents(Animal animal) {
        if ( animal == null ) {
            return null;
        }

        AnimalNoParentsDTO animalNoParentsDTO = new AnimalNoParentsDTO();

        if ( animal.getAnimalId() != null ) {
            animalNoParentsDTO.setAnimalId( animal.getAnimalId().toString() );
        }
        animalNoParentsDTO.setName( animal.getName() );
        animalNoParentsDTO.setSex( animal.getSex() );
        animalNoParentsDTO.setWeight( animal.getWeight() );
        animalNoParentsDTO.setAge( animal.getAge() );
        animalNoParentsDTO.setHeight( animal.getHeight() );
        if ( animal.getArrivalDate() != null ) {
            animalNoParentsDTO.setArrivalDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( animal.getArrivalDate() ) );
        }

        return animalNoParentsDTO;
    }

    @Override
    public AnimalWithParentsDTO fromAnimalToWithParents(Animal animal, Animal mother, Animal father) {
        if ( animal == null && mother == null && father == null ) {
            return null;
        }

        AnimalWithParentsDTO animalWithParentsDTO = new AnimalWithParentsDTO();

        if ( animal != null ) {
            if ( animal.getAnimalId() != null ) {
                animalWithParentsDTO.setAnimalId( animal.getAnimalId().toString() );
            }
            animalWithParentsDTO.setName( animal.getName() );
            animalWithParentsDTO.setSex( animal.getSex() );
            animalWithParentsDTO.setWeight( animal.getWeight() );
            animalWithParentsDTO.setAge( animal.getAge() );
            animalWithParentsDTO.setHeight( animal.getHeight() );
            if ( animal.getArrivalDate() != null ) {
                animalWithParentsDTO.setArrivalDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( animal.getArrivalDate() ) );
            }
        }
        animalWithParentsDTO.setMotherInfo( mother );
        animalWithParentsDTO.setFatherInfo( father );

        return animalWithParentsDTO;
    }
}
