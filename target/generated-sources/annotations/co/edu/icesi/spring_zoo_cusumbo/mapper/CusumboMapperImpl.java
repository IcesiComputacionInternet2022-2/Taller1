package co.edu.icesi.spring_zoo_cusumbo.mapper;

import co.edu.icesi.spring_zoo_cusumbo.dto.CusumboDTO;
import co.edu.icesi.spring_zoo_cusumbo.model.Cusumbo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-19T21:46:50-0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class CusumboMapperImpl implements CusumboMapper {

    @Override
    public Cusumbo fromDTO(CusumboDTO cusumboDTO) {
        if ( cusumboDTO == null ) {
            return null;
        }

        Cusumbo.CusumboBuilder cusumbo = Cusumbo.builder();

        cusumbo.id( cusumboDTO.getId() );
        cusumbo.name( cusumboDTO.getName() );
        cusumbo.sex( cusumboDTO.getSex() );
        cusumbo.weight( cusumboDTO.getWeight() );
        cusumbo.age( cusumboDTO.getAge() );
        cusumbo.height( cusumboDTO.getHeight() );
        cusumbo.arrivalDate( cusumboDTO.getArrivalDate() );
        cusumbo.fatherId( cusumboDTO.getFatherId() );
        cusumbo.motherId( cusumboDTO.getMotherId() );

        return cusumbo.build();
    }

    @Override
    public CusumboDTO fromCusumbo(Cusumbo cusumbo) {
        if ( cusumbo == null ) {
            return null;
        }

        CusumboDTO cusumboDTO = new CusumboDTO();

        cusumboDTO.setId( cusumbo.getId() );
        cusumboDTO.setName( cusumbo.getName() );
        cusumboDTO.setSex( cusumbo.getSex() );
        cusumboDTO.setWeight( cusumbo.getWeight() );
        cusumboDTO.setAge( cusumbo.getAge() );
        cusumboDTO.setHeight( cusumbo.getHeight() );
        cusumboDTO.setArrivalDate( cusumbo.getArrivalDate() );
        cusumboDTO.setFatherId( cusumbo.getFatherId() );
        cusumboDTO.setMotherId( cusumbo.getMotherId() );

        return cusumboDTO;
    }
}
