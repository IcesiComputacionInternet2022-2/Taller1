package co.edu.icesi.calizoo.controller;

import co.edu.icesi.calizoo.dto.HyenaDTO;
import co.edu.icesi.calizoo.error.exception.HyenaException;
import co.edu.icesi.calizoo.mapper.HyenaMapper;
import co.edu.icesi.calizoo.mapper.HyenaMapperImpl;
import co.edu.icesi.calizoo.model.Hyena;
import co.edu.icesi.calizoo.service.HyenaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HyenaControllerTest {

    private HyenaController hyenaController;
    private HyenaMapper hyenaMapper;
    private HyenaService hyenaService;

    @BeforeEach
    public void init () {
        hyenaService = mock(HyenaService.class);
        hyenaMapper = new HyenaMapperImpl();
        hyenaController = new HyenaController(hyenaService, hyenaMapper);
    }

    private Hyena exampleHyena () {
        UUID id = UUID.randomUUID();
        String name = "Abby";
        String sex = "Female";
        double weight = 40;
        int age = 2;
        int height = 50;
        LocalDateTime localDateTime = LocalDateTime.of(2021,10,7,13, 12);
        return new Hyena(id,name, sex, weight, age, height, localDateTime, null, null);
    }

    @Test
    public void testGetHyena() {
        Hyena hyena = exampleHyena();
        when(hyenaService.getHyena(any())).thenReturn(hyena);
        HyenaDTO returnedHyena = hyenaController.getHyena(hyena.getId());
        verify(hyenaService,times(1)).getHyena(any());
    }

    @Test
    public void testCreateHyena() {
        HyenaDTO hyenaDTO = hyenaMapper.fromHyena(exampleHyena());
        Hyena hyena = exampleHyena();
        when(hyenaService.createHyena(any())).thenReturn(hyena);
        hyenaController.createHyena(hyenaDTO);
        verify(hyenaService, times(1)).createHyena(any());
    }

    @Test
    public void testGetUsers() {
        List<Hyena> hyenaList = new ArrayList<>();
        List<HyenaDTO> hyenaDTOList = new ArrayList<>();

        when(hyenaService.getHyenas()).thenReturn(hyenaList);

        assertEquals(hyenaController.getHyenas(),hyenaDTOList);
        verify(hyenaService, times(1)).getHyenas();
    }

    @Test
    public void testWrongName() {
        HyenaDTO hyenaDTO = hyenaMapper.fromHyena(exampleHyena());
        hyenaDTO.setName("Alex2002");

        tryCreation(hyenaDTO);
    }

    @Test
    public void testWrongArrivalDate() {
        HyenaDTO hyenaDTO = hyenaMapper.fromHyena(exampleHyena());
        hyenaDTO.setArrivalDate(LocalDateTime.of(2023,10,7,13, 12));

        tryCreation(hyenaDTO);
    }

    @Test
    public void testWrongFather () {
        HyenaDTO hyenaDTO = hyenaMapper.fromHyena(exampleHyena());
        hyenaDTO.setId(UUID.randomUUID());
        hyenaDTO.setName("Nico");

        when(hyenaService.getHyena(any())).thenReturn(hyenaMapper.fromDTO(hyenaDTO));

        HyenaDTO childHyenaDTO = hyenaMapper.fromHyena(exampleHyena());
        hyenaDTO.setFatherId(hyenaDTO.getId());

        tryCreation(childHyenaDTO);
    }

    @Test
    public void testWrongMother () {
        HyenaDTO hyenaDTO = hyenaMapper.fromHyena(exampleHyena());
        hyenaDTO.setId(UUID.randomUUID());
        hyenaDTO.setName("Venus");
        hyenaDTO.setSex("Male");

        when(hyenaService.getHyena(any())).thenReturn(hyenaMapper.fromDTO(hyenaDTO));

        HyenaDTO childHyenaDTO = hyenaMapper.fromHyena(exampleHyena());
        hyenaDTO.setMotherId(hyenaDTO.getId());

        tryCreation(childHyenaDTO);
    }

    @Test
    public void testWrongAge () {
        HyenaDTO hyenaDTO = hyenaMapper.fromHyena(exampleHyena());
        hyenaDTO.setAge(38);

        tryCreation(hyenaDTO);
    }

    @Test
    public void testWrongWeight () {
        HyenaDTO hyenaDTO = hyenaMapper.fromHyena(exampleHyena());
        hyenaDTO.setWeight(70);

        tryCreation(hyenaDTO);
    }

    @Test
    public void testWrongHeight () {
        HyenaDTO hyenaDTO = hyenaMapper.fromHyena(exampleHyena());
        hyenaDTO.setHeight(100);

        tryCreation(hyenaDTO);
    }

    private void tryCreation (HyenaDTO hyenaDTO) {
        try {
            hyenaController.createHyena(hyenaDTO);
            fail();
        } catch (HyenaException he) {
            verify(hyenaService, times(0)).createHyena(any());
        }
    }

}
