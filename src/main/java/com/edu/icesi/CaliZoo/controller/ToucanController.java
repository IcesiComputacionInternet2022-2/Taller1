package com.edu.icesi.CaliZoo.controller;

import com.edu.icesi.CaliZoo.api.ToucanAPI;
import com.edu.icesi.CaliZoo.constants.ErrorCodes;
import com.edu.icesi.CaliZoo.dto.ToucanDTO;
import com.edu.icesi.CaliZoo.error.exception.ToucanError;
import com.edu.icesi.CaliZoo.error.exception.ToucanException;
import com.edu.icesi.CaliZoo.mapper.ToucanMapper;
import com.edu.icesi.CaliZoo.service.ToucanService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class ToucanController implements ToucanAPI {

    private final ToucanService toucanService;
    private final ToucanMapper toucanMapper;

    @Override
    public List<ToucanDTO> getToucan(String toucanName) {
        return toucanMapper.fromToucan(toucanService.getToucan(toucanName));
    }

    @Override
    public ToucanDTO createToucan(@Valid ToucanDTO toucanDTO) {
        if(toucanDTO == null)
            throw new RuntimeException();
        verifyNameConstrains(toucanDTO.getName());
        verifyArrivalDate(toucanDTO.getDateOfArrival());
        validateSexInBounds(toucanDTO.getSex());
        return toucanMapper.fromToucan(toucanService.createToucan(toucanMapper.fromDTO(toucanDTO)));
    }

    @Override
    public List<ToucanDTO> getToucans() {
        return toucanService.getToucans().stream().map(toucanMapper::fromToucan).collect(Collectors.toList());
    }

    private void verifyNameConstrains(final String toucanName){
        if(toucanName.isBlank() || !toucanName.matches("\\A[a-zA-Z\\s]+\\z"))
            throw new ToucanException(HttpStatus.BAD_REQUEST,new ToucanError(ErrorCodes.INVALID_FORMAT.getCode(),"Name contains invalid characters"));
    }//End verifyNameConstrains

    private void verifyArrivalDate(LocalDate arrivalDate){
       if(arrivalDate == null || arrivalDate.compareTo(LocalDate.now()) > 0)
           throw new ToucanException(HttpStatus.BAD_REQUEST,new ToucanError(ErrorCodes.INVALID_FORMAT.getCode(), "Date couldn't be greater than the current date"));
    }//End verifyArrivalDate

    private void validateSexInBounds(final String toucanSex){
        if(!toucanSex.toUpperCase().matches("(F|M)"))
            throw new ToucanException(HttpStatus.BAD_REQUEST,new ToucanError(ErrorCodes.INVALID_FORMAT.getCode(), "Sex has to be 'F' to female or 'M' to male"));
    }//End checkSex
}//End ToucanController
