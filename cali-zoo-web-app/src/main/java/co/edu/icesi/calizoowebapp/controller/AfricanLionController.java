package co.edu.icesi.calizoowebapp.controller;

import co.edu.icesi.calizoowebapp.api.AfricanLionAPI;
import co.edu.icesi.calizoowebapp.constants.AfricanLionErrorCode;
import co.edu.icesi.calizoowebapp.dto.AfricanLionDTO;
import co.edu.icesi.calizoowebapp.dto.AfricanLionQueryResponseDTO;
import co.edu.icesi.calizoowebapp.error.exception.AfricanLionError;
import co.edu.icesi.calizoowebapp.error.exception.AfricanLionException;
import co.edu.icesi.calizoowebapp.mapper.AfricanLionMapper;
import co.edu.icesi.calizoowebapp.service.AfricanLionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class AfricanLionController implements AfricanLionAPI {

    private AfricanLionService africanLionService;
    private AfricanLionMapper africanLionMapper;

    @Override
    public AfricanLionQueryResponseDTO getLion(String lionName) {
        validateAfricanLionName(lionName);
        return africanLionMapper.fromAfricanLionQueryResponse(africanLionService.getLion(lionName));
    }

    @Override
    public AfricanLionDTO createLion(@Valid AfricanLionDTO africanLionDTO) {
        validateAfricanLionFields(africanLionDTO);
        return africanLionMapper.fromAfricanLion(africanLionService.createLion(africanLionMapper.fromAfricanLionDTO(africanLionDTO)));
    }

    @Override
    public List<AfricanLionDTO> getLions() {
        return africanLionService.getLions().stream().map(africanLionMapper::fromAfricanLion).collect(Collectors.toList());
    }

    private void validateAfricanLionFields(AfricanLionDTO africanLionDTO) {
        validateAfricanLionName(africanLionDTO.getName());
        validateAfricanLionArrivedZooDate(africanLionDTO.getArrivedZooDate());
    }

    private void validateAfricanLionArrivedZooDate(LocalDateTime arrivedZooDate) {
        if(arrivedZooDate.isAfter(LocalDateTime.now())){
            throw new AfricanLionException(HttpStatus.BAD_REQUEST, new AfricanLionError(AfricanLionErrorCode.CODE_09, AfricanLionErrorCode.CODE_09.getMessage()));
        }
    }

    private void validateAfricanLionName(String lionName) {
        validateAfricanLionNameSize(lionName);
        validateAfricanLionNameSpecialChars(lionName);
    }

    private void validateAfricanLionNameSpecialChars(String lionName) {
        if(!lionName.matches("[\\sa-zA-Z]+")){
            throw new AfricanLionException(HttpStatus.BAD_REQUEST, new AfricanLionError(AfricanLionErrorCode.CODE_08, AfricanLionErrorCode.CODE_08.getMessage()));
        }
    }

    private void validateAfricanLionNameSize(String lionName) {
        if(lionName.length() > 120){
            throw new AfricanLionException(HttpStatus.BAD_REQUEST, new AfricanLionError(AfricanLionErrorCode.CODE_07, AfricanLionErrorCode.CODE_07.getMessage()));

        }
    }
}
