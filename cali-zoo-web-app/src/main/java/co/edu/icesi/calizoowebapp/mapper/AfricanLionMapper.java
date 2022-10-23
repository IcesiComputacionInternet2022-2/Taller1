package co.edu.icesi.calizoowebapp.mapper;

import co.edu.icesi.calizoowebapp.dto.AfricanLionDTO;
import co.edu.icesi.calizoowebapp.dto.AfricanLionQueryResponseDTO;
import co.edu.icesi.calizoowebapp.model.AfricanLion;
import co.edu.icesi.calizoowebapp.model.AfricanLionQueryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AfricanLionMapper {

    AfricanLion fromAfricanLionDTO(AfricanLionDTO africanLionDTO);
    AfricanLionDTO fromAfricanLion(AfricanLion africanLion);
    AfricanLionQueryResponse fromAfricanLionQueryResponseDTO(AfricanLionQueryResponseDTO africanLionQueryResponseDTO);
    AfricanLionQueryResponseDTO fromAfricanLionQueryResponse(AfricanLionQueryResponse africanLionQueryResponse);
}
