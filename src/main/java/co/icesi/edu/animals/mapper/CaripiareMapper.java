package co.icesi.edu.animals.mapper;

import co.icesi.edu.animals.dto.CaripiareAndParentsDTO;
import co.icesi.edu.animals.dto.CaripiareDTO;
import co.icesi.edu.animals.model.Caripiare;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CaripiareMapper {

    Caripiare fromCaripiareDTO(CaripiareDTO caripiareDTO);

    Caripiare fromCaripiareDTO(UUID caripiareId, CaripiareDTO caripiareDTO);

    CaripiareDTO fromCaripiare(Caripiare caripiare);

    @Mapping(source = "caripiare.id", target = "id")
    @Mapping(source = "caripiare.name", target = "name")
    @Mapping(source = "caripiare.gender", target = "gender")
    @Mapping(source = "caripiare.weight", target = "weight")
    @Mapping(source = "caripiare.age", target = "age")
    @Mapping(source = "caripiare.height", target = "height")
    @Mapping(source = "caripiare.arrivalDate", target = "arrivalDate")
    @Mapping(source = "father", target = "mother")
    @Mapping(source = "mother", target = "father")
    CaripiareAndParentsDTO fromCaripiareDTOtoCaripiareAndParentsDTO(Caripiare caripiare, Caripiare father, Caripiare mother);
}
