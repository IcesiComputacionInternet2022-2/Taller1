package co.edu.icesi.spring_zoo_cusumbo.controller;

import co.edu.icesi.spring_zoo_cusumbo.dto.CusumboDTO;
import co.edu.icesi.spring_zoo_cusumbo.error.ErrorCode;
import co.edu.icesi.spring_zoo_cusumbo.error.exception.CusumboException;
import co.edu.icesi.spring_zoo_cusumbo.mapper.CusumboMapper;
import co.edu.icesi.spring_zoo_cusumbo.mapper.CusumboMapperImpl;
import co.edu.icesi.spring_zoo_cusumbo.repository.CusumboRepository;
import co.edu.icesi.spring_zoo_cusumbo.service.CusumboService;
import co.edu.icesi.spring_zoo_cusumbo.service.impl.CusumboServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CusumboControllerTest {

    private CusumboService cusumboService;

    private CusumboController cusumboController;

    private CusumboMapper cusumboMapper;



    @BeforeEach
    public void init(){
        cusumboService = mock(CusumboServiceImpl.class);
        cusumboMapper = new CusumboMapperImpl();
        cusumboController = new CusumboController(cusumboService,cusumboMapper);

    }

    private CusumboDTO createTestCusumbo(){
        return new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Pearl",'F',7.0f,5,35.0f, LocalDateTime.now().minusDays(1L),null,null);
    }


    //Tests for veryfying proper call of corresponding CusumboService methods

    @Test
    public void testCusumboFamily(){
        cusumboController.getCusumboWithParents("CusumboName");
        verify(cusumboService,times(1)).getCusumboWithParents(ArgumentMatchers.anyString());
    }

    @Test
    public void testGetCusumbo(){
        cusumboController.getCusumbos();
        verify(cusumboService,times(1)).getCusumbos();
    }

    @Test
    public void testCreateCusumboCallsService(){
        cusumboController.createCusumbo(createTestCusumbo());
        verify(cusumboService,times(1)).createCusumbo(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCusumboVerifyNameLength(){
        final CusumboDTO cusumboDTO1 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"",'F',7.0f,5,35.0f, LocalDateTime.now().minusDays(1L),null,null);

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO1),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_01A.getMessage(),thrown.getError().getMessage());

        final CusumboDTO cusumboDTO2 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",'F',7.0f,5,35.0f, LocalDateTime.now().minusDays(1L),null,null);

        thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO2),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_01A.getMessage(),thrown.getError().getMessage());
    }

    @Test
    public void testCreateCusumboVerifyNameCharacters(){

        final CusumboDTO cusumboDTO1 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Hello????@$$#$@/*-",'F',7.0f,5,35.0f, LocalDateTime.now().minusDays(1L),null,null);

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO1),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_01A.getMessage(),thrown.getError().getMessage());

    }

    @Test
    public void testCreateCusumboVerifyArrivalDate(){
        final CusumboDTO cusumboDTO1 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Pearl",'F',7.0f,5,35.0f, LocalDateTime.now().plusDays(1L),null,null);

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO1),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_02.getMessage(),thrown.getError().getMessage());


    }

    @Test
    public void testCreateCusumboVerifyAge(){
        final CusumboDTO cusumboDTO1 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Pearl",'F',7.0f,50,35.0f, LocalDateTime.now().minusDays(1L),null,null);

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO1),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_03.getMessage(),thrown.getError().getMessage());

        final CusumboDTO cusumboDTO2 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Pearl",'F',7.0f,-10,35.0f, LocalDateTime.now().minusDays(1L),null,null);

        thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO2),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_03.getMessage(),thrown.getError().getMessage());

    }

    @Test
    public void testCreateCusumboVerifyHeight(){
        final CusumboDTO cusumboDTO1 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Pearl",'F',7.0f,5,-6.0f, LocalDateTime.now().minusDays(1L),null,null);

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO1),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_04.getMessage(),thrown.getError().getMessage());

        final CusumboDTO cusumboDTO2 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Pearl",'F',7.0f,5,-10.0f, LocalDateTime.now().minusDays(1L),null,null);

        thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO2),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_04.getMessage(),thrown.getError().getMessage());


    }

    @Test
    public void testCreateCusumboVerifyWeight(){
        final CusumboDTO cusumboDTO1 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Pearl",'F',50f,5,35.0f, LocalDateTime.now().minusDays(1L),null,null);

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO1),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_05.getMessage(),thrown.getError().getMessage());

        final CusumboDTO cusumboDTO2 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Pearl",'F',-3.5f,5,35.0f, LocalDateTime.now().minusDays(1L),null,null);

        thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO2),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_05.getMessage(),thrown.getError().getMessage());

        final CusumboDTO cusumboDTO3 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Pearl",'F',0f,5,35.0f, LocalDateTime.now().minusDays(1L),null,null);

        thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO3),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_05.getMessage(),thrown.getError().getMessage());


    }
    @Test
    public void testCreateCusumboVerifySex(){

        final CusumboDTO cusumboDTO1 = new CusumboDTO(UUID.fromString("0b8a1ce3-b1ac-40ff-9920-bd4e7e37edf6"),"Pearl",'G',7.0f,5,35.0f, LocalDateTime.now().minusDays(1L),null,null);

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboController.createCusumbo(cusumboDTO1),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_06.getMessage(),thrown.getError().getMessage());

    }



}
