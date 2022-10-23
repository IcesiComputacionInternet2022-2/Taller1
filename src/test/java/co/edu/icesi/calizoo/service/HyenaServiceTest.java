package co.edu.icesi.calizoo.service;

import co.edu.icesi.calizoo.error.exception.HyenaException;
import co.edu.icesi.calizoo.model.Hyena;
import co.edu.icesi.calizoo.repository.HyenaRepository;
import co.edu.icesi.calizoo.service.impl.HyenaServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class HyenaServiceTest {

    private HyenaService hyenaService;
    private HyenaRepository hyenaRepository;
    private ObjectMapper objectMapper;


    @BeforeEach
    public void init () {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        hyenaRepository = mock(HyenaRepository.class);
        hyenaService = new HyenaServiceImpl(hyenaRepository);
    }



    @SneakyThrows
    private Hyena getHyenaExample() {
        return objectMapper.readValue(new File("src/test/resources/createHyena.json"), Hyena.class);
    }

    @Test
    public void testGetHyena() {
        Hyena hyena = getHyenaExample();
        hyena.setId(UUID.randomUUID());

        when(hyenaRepository.findById(any())).thenReturn(Optional.of(hyena));

        assertEquals(hyena, hyenaService.getHyena(hyena.getId()));

        verify(hyenaRepository, times(1)).findById(any());
    }

    @Test
    public void createHyena() {
        Hyena hyena = getHyenaExample();
        hyena.setId(UUID.randomUUID());

        when(hyenaRepository.save(any())).thenReturn(hyena);

        assertEquals(hyena, hyenaService.createHyena(hyena));

        verify(hyenaRepository, times(1)).save(any());
    }

    @Test
    public void getHyenaList () {
        List<Hyena> hyenas = new ArrayList<>();

        when(hyenaRepository.findAll()).thenReturn(hyenas);

        assertEquals(hyenas, hyenaService.getHyenas());

    }

    @Test
    public void createHyenaWithNameRepeated () {
        Hyena hyena = getHyenaExample();
        hyena.setId(UUID.randomUUID());

        Hyena hyena2 = getHyenaExample();
        hyena2.setId(UUID.randomUUID());

        List<Hyena> hyenas = new ArrayList<>();
        hyenas.add(hyena2);

        when(hyenaRepository.findAll()).thenReturn(hyenas);

        try {
            hyenaService.createHyena(hyena);
            fail();
        } catch (HyenaException h) {
            assertEquals("Name is already taken", h.getError().getMessage());
            verify(hyenaRepository, times(0)).save(any());
            verify(hyenaRepository, times(1)).findAll();
        }

    }
}


