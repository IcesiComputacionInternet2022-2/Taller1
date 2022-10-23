package co.edu.icesi.calizoowebapp.controller;

import co.edu.icesi.calizoowebapp.constants.AfricanLionErrorCode;
import co.edu.icesi.calizoowebapp.constants.AnimalSex;
import co.edu.icesi.calizoowebapp.dto.AfricanLionDTO;
import co.edu.icesi.calizoowebapp.dto.AfricanLionQueryResponseDTO;
import co.edu.icesi.calizoowebapp.error.exception.AfricanLionException;
import co.edu.icesi.calizoowebapp.mapper.AfricanLionMapper;
import co.edu.icesi.calizoowebapp.model.AfricanLion;
import co.edu.icesi.calizoowebapp.model.AfricanLionQueryResponse;
import co.edu.icesi.calizoowebapp.service.AfricanLionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class AfricanLionControllerTest {

    private AfricanLionService africanLionService;
    private AfricanLionMapper africanLionMapper;
    private AfricanLionController africanLionController;

    @BeforeEach
    public void init(){
        africanLionService = mock(AfricanLionService.class);
        africanLionMapper = mock(AfricanLionMapper.class);
        africanLionController = new AfricanLionController(africanLionService, africanLionMapper);
    }

    private List<AfricanLion> setUpScenarioNewLionsList(){

        ArrayList<AfricanLion> currentAfricanLionList;
        String lionId = "69a91b05-4621-4a79-9c94-372fa108d132";
        String lionName = "lionFather";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 240;
        int lionAge = 5;
        double lionHeight = 110;
        String lionArrivedZooDate = "2020-09-10T08:10:59";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLion fatherAfricanLion =  new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        lionId = "72786b9d-5485-4512-995c-b658e098f46a";
        lionName = "lionMother";
        lionSex = AnimalSex.FEMALE;
        lionWeight = 170;
        lionAge = 2;
        lionHeight = 100;
        lionArrivedZooDate = "2021-10-30T10:45:50";
        AfricanLion motherAfricanLion =  new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        lionId = "82787b9d-5485-3102-005c-a658e018f76a";
        lionName = "lionOther";
        lionSex = AnimalSex.MALE;
        lionWeight = 230;
        lionAge = 7;
        lionHeight = 110;
        lionArrivedZooDate = "2020-03-20T12:35:22";
        lionFatherId = UUID.fromString("69a91b05-4621-4a79-9c94-372fa108d132");
        lionMotherId = UUID.fromString("72786b9d-5485-4512-995c-b658e098f46a");
        AfricanLion otherAfricanLion =  new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);


        currentAfricanLionList = new ArrayList<>();
        currentAfricanLionList.add(fatherAfricanLion);
        currentAfricanLionList.add(motherAfricanLion);
        currentAfricanLionList.add(otherAfricanLion);

        return currentAfricanLionList;
    }

    private List<AfricanLionDTO> setUpScenarioNewLionListDTO(){
        ArrayList<AfricanLionDTO> currentAfricanLionListDTO;
        String lionId = "69a91b05-4621-4a79-9c94-372fa108d132";
        String lionName = "lionFather";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 240;
        int lionAge = 5;
        double lionHeight = 110;
        String lionArrivedZooDate = "2020-09-10T08:10:59";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLionDTO fatherAfricanLion =  new AfricanLionDTO(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        lionId = "72786b9d-5485-4512-995c-b658e098f46a";
        lionName = "lionMother";
        lionSex = AnimalSex.FEMALE;
        lionWeight = 170;
        lionAge = 2;
        lionHeight = 100;
        lionArrivedZooDate = "2021-10-30T10:45:50";
        AfricanLionDTO motherAfricanLion =  new AfricanLionDTO(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        lionId = "82787b9d-5485-3102-005c-a658e018f76a";
        lionName = "lionOther";
        lionSex = AnimalSex.MALE;
        lionWeight = 230;
        lionAge = 7;
        lionHeight = 110;
        lionArrivedZooDate = "2020-03-20T12:35:22";
        lionFatherId = UUID.fromString("69a91b05-4621-4a79-9c94-372fa108d132");
        lionMotherId = UUID.fromString("72786b9d-5485-4512-995c-b658e098f46a");
        AfricanLionDTO otherAfricanLion =  new AfricanLionDTO(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);


        currentAfricanLionListDTO = new ArrayList<>();
        currentAfricanLionListDTO.add(fatherAfricanLion);
        currentAfricanLionListDTO.add(motherAfricanLion);
        currentAfricanLionListDTO.add(otherAfricanLion);

        return currentAfricanLionListDTO;
    }

    @Test
    public void testGetLionWithParents(){
        String lionName = "lionOther";
        List<AfricanLionDTO> africanLionDTOList = setUpScenarioNewLionListDTO();
        List<AfricanLion> africanLionList = setUpScenarioNewLionsList();

        AfricanLionDTO africanLionDTO =  africanLionDTOList.get(2);
        AfricanLion africanLion =  africanLionList.get(2);

        AfricanLionQueryResponseDTO africanLionQueryResponseDTO = new AfricanLionQueryResponseDTO(africanLionDTO, africanLionDTOList.get(0), africanLionDTOList.get(1));
        AfricanLionQueryResponse africanLionQueryResponse = new AfricanLionQueryResponse(africanLion, africanLionList.get(0), africanLionList.get(1));

        when(africanLionService.getLion(lionName)).thenReturn(africanLionQueryResponse);
        when(africanLionMapper.fromAfricanLionQueryResponse(africanLionQueryResponse)).thenReturn(africanLionQueryResponseDTO);
        africanLionController.getLion(lionName);
        verify(africanLionService, times(1)).getLion(lionName);
        verify(africanLionMapper, times(1)).fromAfricanLionQueryResponse(africanLionQueryResponse);
    }

    @Test
    public void testGetLionWithOutParents(){
        String lionName = "lionFather";
        List<AfricanLionDTO> africanLionDTOList = setUpScenarioNewLionListDTO();
        List<AfricanLion> africanLionList = setUpScenarioNewLionsList();

        AfricanLionDTO africanLionDTO =  africanLionDTOList.get(0);
        AfricanLion africanLion =  africanLionList.get(0);

        AfricanLionQueryResponseDTO africanLionQueryResponseDTO = new AfricanLionQueryResponseDTO(africanLionDTO, null, null);
        AfricanLionQueryResponse africanLionQueryResponse = new AfricanLionQueryResponse(africanLion, null, null);

        when(africanLionService.getLion(lionName)).thenReturn(africanLionQueryResponse);
        when(africanLionMapper.fromAfricanLionQueryResponse(africanLionQueryResponse)).thenReturn(africanLionQueryResponseDTO);
        africanLionController.getLion(lionName);
        verify(africanLionService, times(1)).getLion(lionName);
        verify(africanLionMapper, times(1)).fromAfricanLionQueryResponse(africanLionQueryResponse);
    }

    @Test
    public void testCreateLion(){
        String lionId = "aaa6ad08-1dc0-4015-9e21-614afd01b2c9";
        String lionName = "lionChild";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 200;
        int lionAge = 0;
        double lionHeight = 105;
        String lionArrivedZooDate = "2021-12-10T08:10:59";
        UUID lionFatherId = UUID.fromString("69a91b05-4621-4a79-9c94-372fa108d132");
        UUID lionMotherId = UUID.fromString("cbe5ea52-0edb-4d2e-a883-1488f1520b20");

        AfricanLion newAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);
        AfricanLionDTO newAfricanLionDTO = new AfricanLionDTO(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        when(africanLionMapper.fromAfricanLionDTO(newAfricanLionDTO)).thenReturn(newAfricanLion);
        when(africanLionMapper.fromAfricanLion(newAfricanLion)).thenReturn(newAfricanLionDTO);
        when(africanLionService.createLion(newAfricanLion)).thenReturn(newAfricanLion);

        try{
            africanLionController.createLion(newAfricanLionDTO);
            verify(africanLionMapper, times(1)).fromAfricanLion(newAfricanLion);
            verify(africanLionMapper, times(1)).fromAfricanLionDTO(newAfricanLionDTO);
            verify(africanLionService, times(1)).createLion(newAfricanLion);

        }catch(AfricanLionException africanLionException){
            fail();
        }

    }

    @Test
    public void testCreateLionInvalidArrivedDate(){
        String lionId = "aaa6ad08-1dc0-4015-9e21-614afd01b2c9";
        String lionName = "lionChild";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 200;
        int lionAge = 0;
        double lionHeight = 105;
        String lionArrivedZooDate = "2023-12-10T08:10:59";
        UUID lionFatherId = UUID.fromString("69a91b05-4621-4a79-9c94-372fa108d132");
        UUID lionMotherId = UUID.fromString("cbe5ea52-0edb-4d2e-a883-1488f1520b20");

        AfricanLion newAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);
        AfricanLionDTO newAfricanLionDTO = new AfricanLionDTO(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        try{
            africanLionController.createLion(newAfricanLionDTO);
            fail();

        }catch(AfricanLionException africanLionException){

            assertEquals(HttpStatus.BAD_REQUEST, africanLionException.getHttpStatus());
            assertEquals(AfricanLionErrorCode.CODE_09, africanLionException.getError().getCode());
            assertEquals(AfricanLionErrorCode.CODE_09.getMessage(), africanLionException.getError().getCode().getMessage());

            verify(africanLionMapper, times(0)).fromAfricanLion(newAfricanLion);
            verify(africanLionMapper, times(0)).fromAfricanLionDTO(newAfricanLionDTO);
            verify(africanLionService, times(0)).createLion(newAfricanLion);
        }

    }

    @Test
    public void testCreateLionNameSpecialChars(){
        String lionId = "aaa6ad08-1dc0-4015-9e21-614afd01b2c9";
        String lionName = "lionChild1@";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 200;
        int lionAge = 0;
        double lionHeight = 105;
        String lionArrivedZooDate = "2021-12-10T08:10:59";
        UUID lionFatherId = UUID.fromString("69a91b05-4621-4a79-9c94-372fa108d132");
        UUID lionMotherId = UUID.fromString("cbe5ea52-0edb-4d2e-a883-1488f1520b20");

        AfricanLion newAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);
        AfricanLionDTO newAfricanLionDTO = new AfricanLionDTO(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        try{
            africanLionController.createLion(newAfricanLionDTO);
            fail();

        }catch(AfricanLionException africanLionException){

            assertEquals(HttpStatus.BAD_REQUEST, africanLionException.getHttpStatus());
            assertEquals(AfricanLionErrorCode.CODE_08, africanLionException.getError().getCode());
            assertEquals(AfricanLionErrorCode.CODE_08.getMessage(), africanLionException.getError().getCode().getMessage());

            verify(africanLionMapper, times(0)).fromAfricanLion(newAfricanLion);
            verify(africanLionMapper, times(0)).fromAfricanLionDTO(newAfricanLionDTO);
            verify(africanLionService, times(0)).createLion(newAfricanLion);
        }

    }

    @Test
    public void testCreateLionNameLongerThan120Chars(){
        String lionId = "aaa6ad08-1dc0-4015-9e21-614afd01b2c9";
        String lionName = "lionChilddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 200;
        int lionAge = 0;
        double lionHeight = 105;
        String lionArrivedZooDate = "2021-12-10T08:10:59";
        UUID lionFatherId = UUID.fromString("69a91b05-4621-4a79-9c94-372fa108d132");
        UUID lionMotherId = UUID.fromString("cbe5ea52-0edb-4d2e-a883-1488f1520b20");

        AfricanLion newAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);
        AfricanLionDTO newAfricanLionDTO = new AfricanLionDTO(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        try{
            africanLionController.createLion(newAfricanLionDTO);
            fail();

        }catch(AfricanLionException africanLionException){

            assertEquals(HttpStatus.BAD_REQUEST, africanLionException.getHttpStatus());
            assertEquals(AfricanLionErrorCode.CODE_07, africanLionException.getError().getCode());
            assertEquals(AfricanLionErrorCode.CODE_07.getMessage(), africanLionException.getError().getCode().getMessage());

            verify(africanLionMapper, times(0)).fromAfricanLion(newAfricanLion);
            verify(africanLionMapper, times(0)).fromAfricanLionDTO(newAfricanLionDTO);
            verify(africanLionService, times(0)).createLion(newAfricanLion);
        }

    }


    @Test
    public void testGetLions(){
        List<AfricanLionDTO> africanLionDTOList = setUpScenarioNewLionListDTO();
        List<AfricanLion> africanLionList = setUpScenarioNewLionsList();

        when(africanLionService.getLions()).thenReturn(africanLionList);

        IntStream.range(0, africanLionList.size()).forEach(index ->
                        when(africanLionMapper.fromAfricanLion(africanLionList.get(index)))
                        .thenReturn(africanLionDTOList.get(index))
        );

        africanLionController.getLions();
        verify(africanLionService, times(1)).getLions();
        africanLionList.forEach(africanLion ->
                verify(africanLionMapper, times(1)).fromAfricanLion(africanLion)
        );
    }

}
