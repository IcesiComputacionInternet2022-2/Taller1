package com.co.edu.icesi.zooWeb.mapper;

import com.co.edu.icesi.zooWeb.dto.BlackSwanDTO;
import com.co.edu.icesi.zooWeb.model.BlackSwan;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-23T11:03:37-0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
@Component
public class BlackSwanMapperImpl implements BlackSwanMapper {

    @Override
    public BlackSwan fromBlackSwanDTO(BlackSwanDTO blackSwanDTO) {
        if ( blackSwanDTO == null ) {
            return null;
        }

        BlackSwan.BlackSwanBuilder blackSwan = BlackSwan.builder();

        blackSwan.id( blackSwanDTO.getId() );
        blackSwan.name( blackSwanDTO.getName() );
        blackSwan.sex( blackSwanDTO.getSex() );
        blackSwan.weight( blackSwanDTO.getWeight() );
        blackSwan.age( blackSwanDTO.getAge() );
        blackSwan.height( blackSwanDTO.getHeight() );
        blackSwan.arrivedZooDate( blackSwanDTO.getArrivedZooDate() );
        blackSwan.fatherId( blackSwanDTO.getFatherId() );
        blackSwan.motherId( blackSwanDTO.getMotherId() );

        return blackSwan.build();
    }

    @Override
    public BlackSwanDTO fromBlackSwan(BlackSwan blackSwan) {
        if ( blackSwan == null ) {
            return null;
        }

        BlackSwanDTO blackSwanDTO = new BlackSwanDTO();

        blackSwanDTO.setId( blackSwan.getId() );
        blackSwanDTO.setName( blackSwan.getName() );
        blackSwanDTO.setSex( blackSwan.getSex() );
        blackSwanDTO.setWeight( blackSwan.getWeight() );
        blackSwanDTO.setAge( blackSwan.getAge() );
        blackSwanDTO.setHeight( blackSwan.getHeight() );
        blackSwanDTO.setArrivedZooDate( blackSwan.getArrivedZooDate() );
        blackSwanDTO.setFatherId( blackSwan.getFatherId() );
        blackSwanDTO.setMotherId( blackSwan.getMotherId() );

        return blackSwanDTO;
    }

    @Override
    public BlackSwan fromDTO(String swanName, BlackSwanDTO blackSwanDTO) {
        if ( swanName == null && blackSwanDTO == null ) {
            return null;
        }

        BlackSwan.BlackSwanBuilder blackSwan = BlackSwan.builder();

        if ( blackSwanDTO != null ) {
            blackSwan.id( blackSwanDTO.getId() );
            blackSwan.name( blackSwanDTO.getName() );
            blackSwan.sex( blackSwanDTO.getSex() );
            blackSwan.weight( blackSwanDTO.getWeight() );
            blackSwan.age( blackSwanDTO.getAge() );
            blackSwan.height( blackSwanDTO.getHeight() );
            blackSwan.arrivedZooDate( blackSwanDTO.getArrivedZooDate() );
            blackSwan.fatherId( blackSwanDTO.getFatherId() );
            blackSwan.motherId( blackSwanDTO.getMotherId() );
        }

        return blackSwan.build();
    }
}
