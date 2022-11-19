package co.edu.icesi.zoocanyonriver.controller;

import co.edu.icesi.zoocanyonriver.api.TigerApi;
import co.edu.icesi.zoocanyonriver.constants.CodeSpecifications;
import co.edu.icesi.zoocanyonriver.constants.CodesError;
import co.edu.icesi.zoocanyonriver.dto.TigerDTO;
import co.edu.icesi.zoocanyonriver.dto.TigerResponseDTO;
import co.edu.icesi.zoocanyonriver.error.exception.TigerDemoError;
import co.edu.icesi.zoocanyonriver.error.exception.TigerDemoException;
import co.edu.icesi.zoocanyonriver.mapper.TigerMapper;
import co.edu.icesi.zoocanyonriver.service.TigerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor

public class TigerController implements TigerApi {

    TigerService tigerService;

    TigerMapper tigerMapper;

    @CrossOrigin(origins = "*")
    @Override
    public TigerResponseDTO getTiger(String tigerName) {
        return tigerService.getTiger(tigerName);
    }

    @CrossOrigin(origins = "*")
    @Override
    public TigerDTO createTiger(TigerDTO tigerDTO) {
        verifyNameLenght(tigerDTO.getName());
        verifyNameContent(tigerDTO.getName());

        return tigerMapper.fromTiger(tigerService.createTiger(tigerMapper.fromDTO(tigerDTO)));
    }

    private void verifyNameContent(String name) {
        if (!name.matches("[a-zA-Z\\s]+")) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST,
                        new TigerDemoError(CodesError.CODE_O1.getCode(), CodesError.CODE_O1.getMessage()));
        }
    }

    private void verifyNameLenght(String name) {
        if (name.length() > CodeSpecifications.MAX_LEGTH_NAME.getValue()) {
            throw new TigerDemoException(HttpStatus.BAD_REQUEST,
                    new TigerDemoError(CodesError.CODE_02.getCode(), CodesError.CODE_02.getMessage()));
        }
    }

    @CrossOrigin(origins = "*")
    @Override
    public List<TigerDTO> getTigers() {
        return tigerService.getTigers().stream().map(tigerMapper::fromTiger).collect(Collectors.toList());
    }
}
