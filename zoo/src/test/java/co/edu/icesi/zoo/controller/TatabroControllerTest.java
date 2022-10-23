package co.edu.icesi.zoo.controller;

import co.edu.icesi.zoo.service.TatabroService;
import co.edu.icesi.zoo.dto.TatabroDTO;
import co.edu.icesi.zoo.mapper.TatabroMapper;
import co.edu.icesi.zoo.model.Tatabro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TatabroControllerTest {

    private TatabroController tatabroController;
    private TatabroService tatabroService;
    private TatabroMapper tatabroMapper;

    @BeforeEach
    public void init() {
        tatabroService = mock(TatabroService.class);
        tatabroMapper = mock(TatabroMapper.class);
        tatabroController = new TatabroController(tatabroService, tatabroMapper);
    }

    @Test
    public void testControllerGetTatabroByID() {
        UUID id = UUID.randomUUID();
        when(tatabroMapper.fromTatabro(any())).thenReturn(new TatabroDTO(id, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        TatabroDTO tatabroDTOCreate = tatabroController.createTatabro(new TatabroDTO(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        TatabroDTO tatabroDTOGet = tatabroController.getTatabroByID(tatabroDTOCreate.getId());
        assertNotNull(tatabroDTOGet);
        assertEquals(tatabroDTOCreate, tatabroDTOGet);
        verify(tatabroService, times(1)).getTatabroByID(id);
    }

    @Test
    public void testControllerGetTatabroByIDNonExistent() {
        UUID id = UUID.randomUUID();
        assertNull(tatabroController.getTatabroByID(id));
        verify(tatabroService, times(1)).getTatabroByID(id);
    }

    @Test
    public void testControllerGetTatabroByName() {
        UUID id = UUID.randomUUID();
        when(tatabroMapper.fromTatabro(any())).thenReturn(new TatabroDTO(id, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        TatabroDTO tatabroDTOCreate = tatabroController.createTatabro(new TatabroDTO(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        TatabroDTO tatabroDTOGet = tatabroController.getTatabroByID(tatabroDTOCreate.getId());
        assertNotNull(tatabroDTOGet);
        assertEquals(tatabroDTOCreate, tatabroDTOGet);
        verify(tatabroService, times(1)).getTatabroByID(id);
    }

    @Test
    public void testControllerGetTatabroByNameNonExistent() {
        when(tatabroMapper.fromTatabroDTO(any(), any(), any())).thenReturn(null);
        when(tatabroMapper.fromTatabro(any())).thenReturn(null);
        assertNull(tatabroController.getTatabroByName("Tommy"));
    }

    @Test
    public void testControllerGetTatabrosEmpty() {
        assertEquals(0, tatabroController.getTatabros().size());
        verify(tatabroService, times(1)).getTatabros();
    }

    @Test
    public void testControllerGetTatabrosNonEmpty() {
        assertEquals(0, tatabroController.getTatabros().size());
        when(tatabroMapper.fromTatabro(any())).thenReturn(new TatabroDTO(UUID.randomUUID(), "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        TatabroDTO userDTOCreate = tatabroController.createTatabro(new TatabroDTO(null,  "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        List<Tatabro> tatabros = tatabroService.getTatabros();
        tatabros.add(tatabroMapper.fromDTO(userDTOCreate));
        when(tatabroService.getTatabros()).thenReturn(tatabros);
        assertEquals(1, tatabroController.getTatabros().size());
    }

    @Test
    public void testControllerCreateTatabro() {
        when(tatabroMapper.fromTatabro(any())).thenReturn(new TatabroDTO(UUID.randomUUID(), "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        assertNotNull(tatabroController.createTatabro(new TatabroDTO(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
    }
}