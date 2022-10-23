package co.edu.icesi.zoocanyonriver.controller;

import co.edu.icesi.zoocanyonriver.constants.CodesError;
import co.edu.icesi.zoocanyonriver.dto.TigerDTO;
import co.edu.icesi.zoocanyonriver.error.exception.TigerDemoException;
import co.edu.icesi.zoocanyonriver.mapper.TigerMapper;
import co.edu.icesi.zoocanyonriver.mapper.TigerMapperImpl;
import co.edu.icesi.zoocanyonriver.service.TigerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class TigerControllerTest {

    private TigerService tigerService;
    private TigerMapper tigerMapper;
    private TigerController tigerController;

    @BeforeEach
    public void init() {
        tigerService = mock(TigerService.class);
        tigerMapper = new TigerMapperImpl();
        tigerController = new TigerController(tigerService, tigerMapper);
    }

    @Test
    public void testGetTiger(){
        String name = "Tiger";

        tigerController.getTiger(name);
        verify(tigerService, times(1)).getTiger(name);
    }

    @Test
    public void verifyNameContentTest() {
        UUID uuid = UUID.fromString("c30f50a0-b01b-4770-b228-12eb0603506e");
        String name = "alberto2+";
        TigerDTO tigerDTO = new TigerDTO(uuid,null,null, name,"Male",200,"3","110", LocalDateTime.now());


        try{
            tigerController.createTiger(tigerDTO);
        }catch(TigerDemoException error){
            assertEquals(CodesError.CODE_O1.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyNameLenghtTest(){
        UUID uuid = UUID.fromString("c30f50a0-b01b-4770-b228-12eb0603506e");
        String name = "albertiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii";
        TigerDTO tigerDTO = new TigerDTO(uuid,null,null, name,"Male",200,"3","110", LocalDateTime.now());

        try{
            tigerController.createTiger(tigerDTO);
            fail();
        }catch(TigerDemoException error){
            assertEquals(CodesError.CODE_02.getCode(), error.getError().getCode());
        }
    }


    @Test
    public void createTigerTest(){
        UUID uuid = UUID.randomUUID();
        TigerDTO tigerDTO = new TigerDTO(uuid,null,null,"Tiger","Male",200,"3","110", LocalDateTime.now());

        tigerController.createTiger(tigerDTO);
        verify(tigerService,times(1)).createTiger(any());
    }

    @Test
    public void getTigersTest(){
        tigerController.getTigers();
        verify(tigerService,times(1)).getTigers();
    }
}
