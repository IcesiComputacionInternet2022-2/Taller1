package icesi.edu.co.zoodemo.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import icesi.edu.co.zoodemo.dto.SuricatoDTO;
import icesi.edu.co.zoodemo.dto.SuricatoParentsIdDTO;
import icesi.edu.co.zoodemo.error.exception.SuricatoError;
import icesi.edu.co.zoodemo.error.exception.SuricatoException;
import icesi.edu.co.zoodemo.mapper.SuricatoMapper;
import icesi.edu.co.zoodemo.repository.SuricatoRepository;
import icesi.edu.co.zoodemo.service.SuricatoService;
import icesi.edu.co.zoodemo.service.impl.SuricatoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SuricatoControllerTest {
    private SuricatoController suricatoController;
    private SuricatoMapper suricatoMapper;
    private SuricatoServiceImpl suricatoServiceImpl;
    private SuricatoRepository suricatoRepository;


    private SuricatoParentsIdDTO suricatoSebastian;
    private SuricatoParentsIdDTO suricatoClaudia;
    private SuricatoParentsIdDTO suricatoHijo;
    private SuricatoParentsIdDTO suricatoParentsIdDTO;

    @BeforeEach
    private void init() {
        suricatoServiceImpl = mock(SuricatoServiceImpl.class);
        suricatoMapper = mock(SuricatoMapper.class);
        suricatoController = new SuricatoController(suricatoServiceImpl, suricatoMapper);
    }

    public void setupScenery1() {
        String name = "Sebastian";
        String gender = "M";
        float weight = 510;
        int age = 8;
        float height = 30;
        LocalDateTime date = LocalDateTime.now();
        suricatoSebastian = new SuricatoParentsIdDTO(UUID.fromString("14d96594-9250-4a59-8423-2d2f461d673d"), name, gender, weight, age, height, date, null, null);


    }

    public void setupScenery2() {

        String name = "Claudia";
        String gender = "F";
        float weight = 450;
        int age = 10;
        float height = 34;
        LocalDateTime date = LocalDateTime.of(2020, 5, 9, 8, 6, 56);
        suricatoClaudia = new SuricatoParentsIdDTO(UUID.randomUUID(), name, gender, weight, age, height, date, null, null);
    }

    public void setupScenery3() {
        String name = "Sebastian";
        String gender = "M";
        float weight = 510;
        int age = 8;
        float height = 30;
        LocalDateTime date = LocalDateTime.now();
        suricatoSebastian = new SuricatoParentsIdDTO(UUID.fromString("14d96594-9250-4a59-8423-2d2f461d673d"), name, gender, weight, age, height, date, null, null);


    }

    @Test
    public void createSuricato() {
        setupScenery1();
        try {

            suricatoController.createSuricato(suricatoSebastian);
            verify(suricatoServiceImpl, times(1)).createSuricato(any());

        } catch (Exception exception) {
            fail();
        }
    }

    @Test
    public void createSuricatoCharactersVerify() {
        setupScenery1();
        suricatoSebastian.setName("sebastian1");
        try {
            suricatoController.createSuricato(suricatoSebastian);
            fail();
        } catch (SuricatoException e) {
            SuricatoError errorGenerate = e.getError();
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals("Invalid characters", errorGenerate.getMessage());

        }

    }

    @Test
    public void dateValidateTest() {
        setupScenery1();
        suricatoSebastian.setArriveDate(LocalDateTime.of(2025
                , 5, 9, 8, 6, 56));
        try {
            suricatoController.createSuricato(suricatoSebastian);
            fail();
        } catch (SuricatoException e) {
            SuricatoError errorGenerate = e.getError();
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals("Invalid date", errorGenerate.getMessage());


        }
    }
    @Test
    public void nameLengthTest(){
        setupScenery1();
        suricatoSebastian.setName("Sebastiannnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
        try {
            suricatoController.createSuricato(suricatoSebastian);
            fail();
        } catch (SuricatoException e) {
            SuricatoError errorGenerate = e.getError();
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals("The length of the name exceeds the established characters", errorGenerate.getMessage());


        }
    }
    @Test
    public void getSuricatoTest(){
        suricatoController.getSuricato(any());
        verify(suricatoServiceImpl,times(0)).getSuricato(any());
       }
    @Test
    public void getSuricatosTest(){

        suricatoController.getSuricatos();
        verify(suricatoServiceImpl, times(1)).getSuricatos();

    }
}