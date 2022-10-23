package icesi.edu.co.zoodemo.service;

import icesi.edu.co.zoodemo.dto.SuricatoDTO;
import icesi.edu.co.zoodemo.dto.SuricatoParentsDTO;
import icesi.edu.co.zoodemo.dto.SuricatoParentsIdDTO;
import icesi.edu.co.zoodemo.error.exception.SuricatoError;
import icesi.edu.co.zoodemo.error.exception.SuricatoException;
import icesi.edu.co.zoodemo.mapper.SuricatoMapper;
import icesi.edu.co.zoodemo.mapper.SuricatoMapperImpl;
import icesi.edu.co.zoodemo.model.Suricato;
import icesi.edu.co.zoodemo.repository.SuricatoRepository;
import icesi.edu.co.zoodemo.service.impl.SuricatoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;

import static icesi.edu.co.zoodemo.constant.SuricatoConstraints.MIN_HEIGHT;
import static icesi.edu.co.zoodemo.constant.SuricatoConstraints.MIN_WEIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SuricatoServiceTest {

    private SuricatoRepository suricatoRepository;
    private SuricatoMapper suricatoMapper;
    private SuricatoService suricatoService;
    private Suricato suricatoSebastian;
    private Suricato suricatoClaudia;
    private Suricato suricataHija;

    @BeforeEach
    public void init() {
        suricatoMapper = new SuricatoMapperImpl();
        suricatoRepository = mock(SuricatoRepository.class);
        suricatoService = new SuricatoServiceImpl(suricatoRepository, suricatoMapper);

    }

    public void setupScenery1() {
        String name = "Sebastian";
        String gender = "M";
        float weight = 510;
        int age = 8;
        float height = 30;
        Date date = new Date(116, Calendar.JUNE, 3, 10, 5, 6);
        suricatoSebastian = new Suricato(UUID.fromString("14d96594-9250-4a59-8423-2d2f461d673d"), name, gender, weight, age, height, date, null, null);
    }

    public void setupScenery2() {
        String name = "Claudia";
        String gender = "F";
        float weight = 450;
        int age = 10;
        float height = 34;
        Date date = new Date(119, Calendar.AUGUST, 25, 8, 56, 45);
        suricatoClaudia = new Suricato(UUID.fromString("6e13e2e8-1e1e-4e07-a700-76b3e3c4c95f"), name, gender, weight, age, height, date, null, null);
    }

    public void setupScenery3() {
        String name = "SuricataHija";
        String gender = "F";
        float weight = 425;
        int age = 4;
        float height = 30;
        Date date = Calendar.getInstance().getTime();
        suricataHija = new Suricato(UUID.randomUUID(), name, gender, weight, age, height, date, null, null);
    }



    @Test
    public void getSuricatosTest() {
        setupScenery1();
        setupScenery2();
        setupScenery3();
        List<Suricato> listOfSuricatos = new ArrayList<>();
        listOfSuricatos.add(suricatoClaudia);
        listOfSuricatos.add(suricatoSebastian);
        listOfSuricatos.add(suricataHija);
        when(suricatoRepository.findAll()).thenReturn(listOfSuricatos);
        List<Suricato> listCompare = suricatoService.getSuricatos();
        assertEquals(listCompare.size(), listOfSuricatos.size());
        assertEquals(listCompare.get(1).getName(), listOfSuricatos.get(1).getName());


    }

    @Test
    public void SuricatoAlreadyEnteredTest() {
        setupScenery1();
        List<Suricato> suricatoList = new ArrayList<>();
        suricatoList.add(suricatoSebastian);
        when(suricatoRepository.findAll()).thenReturn(suricatoList);
        try {
            suricatoService.createSuricato(suricatoSebastian);
            fail();
        } catch (SuricatoException e) {
            SuricatoError errorLauncher = e.getError();
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals("The name of this meerkat is already entered", errorLauncher.getMessage());
        }
    }
    @Test
    public void verifyAnimalAlreadyExistsById() {
        setupScenery1();
        setupScenery2();
        List<Suricato> suricatoList = new ArrayList<>();
        suricatoSebastian.setMotherId(suricatoClaudia.getId());
        when(suricatoRepository.findById(suricatoSebastian.getId())).thenReturn(null);
        when(suricatoRepository.findAll()).thenReturn(suricatoList);
        try {
            suricatoService.createSuricato(suricatoSebastian);
            fail();
        } catch (SuricatoException e) {
            SuricatoError error = e.getError();
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals("Meerkat id not found", error.getMessage());
        }
    }

    @Test
    public void outOfWeightRangeTest() {
        setupScenery1();
        List<Suricato> suricatoList = new ArrayList<>();

        when(suricatoRepository.findAll()).thenReturn(suricatoList);
        suricatoSebastian.setWeight(MIN_WEIGHT - 8);
        try {
            suricatoService.createSuricato(suricatoSebastian);
            fail();
        } catch (SuricatoException e) {
            SuricatoError error = e.getError();
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals("The weight must be between 400 - 850 (g) ", error.getMessage());
        }
    }
    @Test
    public void outOfHeightRange(){
        setupScenery1();
        List<Suricato> suricatoList = new ArrayList<>();

        when(suricatoRepository.findAll()).thenReturn(suricatoList);
        suricatoSebastian.setHeight(MIN_HEIGHT - 3);
        try {
            suricatoService.createSuricato(suricatoSebastian);
            fail();
        } catch (SuricatoException e) {
            SuricatoError error = e.getError();
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals("The height must be between 25 - 35 (Cm)", error.getMessage());
        }
    }
    @Test
    public void wrongGenreTest(){
        setupScenery1();
        setupScenery2();
        List<Suricato> suricatoList = new ArrayList<>();
        suricatoSebastian.setFatherId(UUID.fromString("6e13e2e8-1e1e-4e07-a700-76b3e3c4c95f"));
        suricatoService.createSuricato(suricatoClaudia);
        when(suricatoRepository.findById(suricatoClaudia.getId())).thenReturn(Optional.ofNullable(suricatoClaudia));

        try {
            suricatoService.createSuricato(suricatoSebastian);
            fail();
        } catch (SuricatoException e) {
            SuricatoError error = e.getError();
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertEquals("Wrong genre Its father id", error.getMessage());
        }


    }
    @Test
    public void getSuricatosParentsTest(){
        setupScenery1();
        when(suricatoRepository.findById(suricatoSebastian.getId())).thenReturn(Optional.ofNullable(suricatoSebastian));
        SuricatoParentsDTO suricaParents = suricatoService.getSuricatosParents(suricatoSebastian.getId());
        assertEquals(suricaParents.getId(), suricatoSebastian.getId());
    }
//    setupScenery1();
//    setupScenery2();
//    setupScenery3();
//    configParents();
//        suricatoService.createSuricato(suricatoSebastian);
//        suricatoService.createSuricato(suricatoClaudia);
//        suricatoService.createSuricato(suricataHija);
}

