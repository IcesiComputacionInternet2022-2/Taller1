package co.edu.icesi.zoocanyonriver.mapper;

import co.edu.icesi.zoocanyonriver.dto.TigerDTO;
import co.edu.icesi.zoocanyonriver.dto.TigerResponseDTO;
import co.edu.icesi.zoocanyonriver.model.Tiger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TigerMapper {
    Tiger fromDTO(TigerDTO tigerDTO);
    TigerDTO fromTiger(Tiger tiger);

    @Mapping(source = "tiger.id", target = "id")
    @Mapping(source = "tiger.name", target = "name")
    @Mapping(source = "tiger.gender", target = "gender")
    @Mapping(source = "tiger.weight", target = "weight")
    @Mapping(source = "tiger.age", target = "age")
    @Mapping(source = "tiger.height", target = "height")
    @Mapping(source = "tiger.arriveDate", target = "arriveDate")
    @Mapping(source = "mother", target = "mother")
    @Mapping(source = "father", target = "father")
    TigerResponseDTO fromTigerToTigerResponseDTO(Tiger tiger, Tiger mother, Tiger father);
}
