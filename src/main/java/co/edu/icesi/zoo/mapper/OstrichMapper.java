package co.edu.icesi.zoo.mapper;

import org.mapstruct.Mapper;

import co.edu.icesi.zoo.dto.OstrichDTO;
import co.edu.icesi.zoo.model.Ostrich;

@Mapper(componentModel = "spring") 
public interface OstrichMapper {
	
    Ostrich fromDTO(OstrichDTO ostrichDTO);

    OstrichDTO fromOstrich(Ostrich ostrich);
    
}
