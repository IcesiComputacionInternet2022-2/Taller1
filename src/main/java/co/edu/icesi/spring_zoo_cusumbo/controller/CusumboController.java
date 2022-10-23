package co.edu.icesi.spring_zoo_cusumbo.controller;

import co.edu.icesi.spring_zoo_cusumbo.api.CusumboApi;
import co.edu.icesi.spring_zoo_cusumbo.dto.CusumboDTO;
import co.edu.icesi.spring_zoo_cusumbo.error.exception.CusumboError;
import co.edu.icesi.spring_zoo_cusumbo.error.exception.CusumboException;
import co.edu.icesi.spring_zoo_cusumbo.mapper.CusumboMapper;
import co.edu.icesi.spring_zoo_cusumbo.service.CusumboService;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static co.edu.icesi.spring_zoo_cusumbo.error.ErrorCode.*;

@RestController
@AllArgsConstructor
public class CusumboController implements CusumboApi {

    //Height measured in centimeters, for cusumbo it represents the body length
    public static final float MAX_HEIGHT = 42.0f;

    //Weight measured in kilograms
    public static final float MAX_WEIGHT = 10.0f;

    //Age measured in years
    public static final int MAX_AGE = 15;

    public final CusumboService cusumboService;

    public final CusumboMapper cusumboMapper;

    @Override
    public List<CusumboDTO> getCusumboWithParents(String cusumboName) {
        return cusumboService.getCusumboWithParents(cusumboName).stream().map(cusumboMapper::fromCusumbo).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public CusumboDTO createCusumbo(CusumboDTO cusumboDTO) {
        validateAttributes(cusumboDTO);
        return cusumboMapper.fromCusumbo(cusumboService.createCusumbo(cusumboMapper.fromDTO(cusumboDTO)));
    }


    private void validateAttributes(CusumboDTO cusumboDTO){
       validateNameLengthAndCharacters(cusumboDTO.getName());
       validateArrivalDate(cusumboDTO.getArrivalDate());
       validateAge(cusumboDTO.getAge());
       validateHeight(cusumboDTO.getHeight());
       validateWeight(cusumboDTO.getWeight());
       validateSex(cusumboDTO.getSex());
    }

    private void validateSex(char sex){
        if(!(sex == 'F' || sex == 'M'))
            throw new CusumboException(HttpStatus.BAD_REQUEST, new CusumboError(CODE_ATR_06.getMessage(),CODE_ATR_06));
    }

    private void validateNameLengthAndCharacters(String name){
        if (name.length() > 120 || name.length() == 0 || !name.replaceAll(" ","").matches("^[a-zA-Z]*$")){
            throw new CusumboException(HttpStatus.BAD_REQUEST, new CusumboError(CODE_ATR_01A.getMessage(),CODE_ATR_01A));
        }
    }

    private void validateArrivalDate(LocalDateTime arrivalDate){
        if (arrivalDate.isAfter(LocalDateTime.now())){
            throw new CusumboException(HttpStatus.BAD_REQUEST, new CusumboError(CODE_ATR_02.getMessage(),CODE_ATR_02));
        }
    }

    private void validateAge(int age){
        if(age < 0 || age > MAX_AGE){
            throw new CusumboException(HttpStatus.BAD_REQUEST, new CusumboError(CODE_ATR_03.getMessage(),CODE_ATR_03));
        }
    }

    private void validateHeight(float height){
        if(height <= 0 || height > MAX_HEIGHT){
            throw new CusumboException(HttpStatus.BAD_REQUEST, new CusumboError(CODE_ATR_04.getMessage(),CODE_ATR_04));
        }
    }

    private void validateWeight(float weight){
        if(weight <= 0 || weight > MAX_WEIGHT){
            throw new CusumboException(HttpStatus.BAD_REQUEST, new CusumboError(CODE_ATR_05.getMessage(),CODE_ATR_05));
        }
    }

    @Override
    public List<CusumboDTO> getCusumbos() {
        return cusumboService.getCusumbos().stream().map(cusumboMapper::fromCusumbo).collect(Collectors.toList());
    }
}
