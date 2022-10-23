package co.edu.icesi.zoo.service;

import co.edu.icesi.zoo.dto.TatabroDTO;
import co.edu.icesi.zoo.dto.TatabroParentsDTO;
import co.edu.icesi.zoo.error.exception.TatabroException;
import co.edu.icesi.zoo.mapper.TatabroMapper;
import co.edu.icesi.zoo.model.Tatabro;
import co.edu.icesi.zoo.repository.TatabroRepository;
import co.edu.icesi.zoo.service.impl.TatabroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TatabroServiceTest {

    private TatabroRepository tatabroRepository;
    private TatabroService tatabroService;
    private TatabroMapper tatabroMapper;
    private List<Tatabro> tatabros;

    @BeforeEach
    public void init() {
        tatabroRepository = mock(TatabroRepository.class);
        tatabroMapper = mock(TatabroMapper.class);
        tatabroService = new TatabroServiceImpl(tatabroRepository, tatabroMapper);
        tatabros = new ArrayList<>();
    }

    public void stage1() {
        Tatabro tatabro = new Tatabro(UUID.randomUUID(), "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null);
        assertDoesNotThrow(() -> tatabroService.createTatabro(new Tatabro(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
        tatabros.add(tatabro);
    }

    public void stage2() {
        assertDoesNotThrow(() -> tatabroService.createTatabro(new Tatabro(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
        assertDoesNotThrow(() -> tatabroService.createTatabro(new Tatabro(null, "Maggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null)));
        tatabros.add(new Tatabro(UUID.randomUUID(), "Piggy", "M", 40, 20, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        tatabros.add(new Tatabro(UUID.randomUUID(), "Maggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null));
    }

    public void stage3() {
        assertDoesNotThrow(() -> tatabroService.createTatabro(new Tatabro(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
        assertDoesNotThrow(() -> tatabroService.createTatabro(new Tatabro(null, "Maggy", "M", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null)));
        tatabros.add(new Tatabro(UUID.randomUUID(), "Piggy", "M", 40, 20, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        tatabros.add(new Tatabro(UUID.randomUUID(), "Maggy", "M", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null));
    }

    public void stage4() {
        assertDoesNotThrow(() -> tatabroService.createTatabro(new Tatabro(null, "Piggy", "F", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
        assertDoesNotThrow(() -> tatabroService.createTatabro(new Tatabro(null, "Maggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null)));
        tatabros.add(new Tatabro(UUID.randomUUID(), "Piggy", "F", 40, 20, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        tatabros.add(new Tatabro(UUID.randomUUID(), "Maggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null));
    }

    @Test
    public void testGetTatabroByID() {
        UUID id = UUID.randomUUID();
        Tatabro tatabro = new Tatabro(id, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null);
        when(tatabroRepository.save(any())).thenReturn(tatabro);
        when(tatabroRepository.findById(any())).thenReturn(Optional.of(tatabro));
        Tatabro tatabroCreate = tatabroService.createTatabro(tatabro);
        Tatabro tatabroGet = tatabroService.getTatabroByID(id);
        assertNotNull(tatabroGet);
        assertEquals(tatabroCreate, tatabroGet);
    }

    @Test
    public void testGetTatabroByIDNonExistent() {
        UUID id = UUID.randomUUID();
        assertThrows(TatabroException.class, () -> tatabroService.getTatabroByID(id), "Tatabro ID doesn't exist.");
        verify(tatabroRepository, times(1)).findById(id);
    }

    @Test
    public void testGetTatabroByName() {
        UUID fatherID = UUID.randomUUID();
        when(tatabroRepository.save(any())).thenReturn(new Tatabro(fatherID, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        Tatabro father = tatabroService.createTatabro(new Tatabro(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        UUID motherID = UUID.randomUUID();
        when(tatabroRepository.save(any())).thenReturn(new Tatabro(motherID, "Maggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null));
        Tatabro mother = tatabroService.createTatabro(new Tatabro(null, "Maggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null));
        UUID childID = UUID.randomUUID();
        when(tatabroRepository.save(any())).thenReturn(new Tatabro(childID, "Tommy", "M", 50, 15, 90, LocalDateTime.now(), father.getId(), mother.getId()));
        when(tatabroRepository.findById(father.getId())).thenReturn(Optional.of(new Tatabro(fatherID, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
        when(tatabroRepository.findById(mother.getId())).thenReturn(Optional.of(new Tatabro(motherID, "Maggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null)));
        Tatabro child = tatabroService.createTatabro(new Tatabro(null, "Tommy", "M", 50, 15, 90, LocalDateTime.now(), father.getId(), mother.getId()));
        TatabroDTO fatherDTO = tatabroMapper.fromTatabro(father);
        TatabroDTO motherDTO = tatabroMapper.fromTatabro(mother);
        when(tatabroRepository.findByName(any())).thenReturn(Optional.of(child));
        when(tatabroMapper.fromTatabro(any())).thenReturn(new TatabroDTO(childID, "Tommy", "M", 50, 15, 90, LocalDateTime.now(), father.getId(), mother.getId()));
        when(tatabroMapper.fromTatabroDTO(any(), any(), any())).thenReturn(new TatabroParentsDTO(childID, "Tommy", "M", 50, 15, 90, LocalDateTime.now(), fatherDTO, motherDTO));
        assertDoesNotThrow(() -> tatabroService.getTatabroByName(child.getName()));
        verify(tatabroRepository, times(1)).findByName(child.getName());
        verify(tatabroMapper, times(1)).fromTatabroDTO(tatabroMapper.fromTatabro(child), tatabroMapper.fromTatabro(father), tatabroMapper.fromTatabro(mother));
    }

    @Test
    public void testGetTatabroByNameNonExistent() {
        assertThrows(TatabroException.class, () -> tatabroService.getTatabroByName("Piggy"), "Tatabro's name doesn't exist.");
        verify(tatabroRepository, times(1)).findByName("Piggy");
    }

    @Test
    public void testGetTatabrosEmpty() {
        assertEquals(0, tatabroService.getTatabros().size());
        verify(tatabroRepository, times(1)).findAll();
    }

    @Test
    public void testGetTatabrosNonEmpty() {
        assertEquals(0, tatabroService.getTatabros().size());
        when(tatabroRepository.save(any())).thenReturn(new Tatabro(UUID.randomUUID(), "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        Tatabro tatabroCreate = tatabroService.createTatabro(new Tatabro(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null));
        List<Tatabro> tatabros = new ArrayList<>();
        tatabros.add(tatabroCreate);
        when(tatabroService.getTatabros()).thenReturn(tatabros);
        assertEquals(1, tatabroService.getTatabros().size());
    }

    @Test
    public void testCreateTatabro() {
        assertDoesNotThrow(() -> tatabroService.createTatabro(new Tatabro(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
        verify(tatabroRepository, times(1)).save(any());
    }

    @Test
    public void testCreateTatabroWithRepeatedName() {
        stage1();
        when(tatabroService.getTatabros()).thenReturn(tatabros);
        assertThrows(TatabroException.class, () -> tatabroService.createTatabro(new Tatabro(null, "Piggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null)), "Tatabro's name already exists.");
        verify(tatabroRepository, times(1)).save(any());
        assertEquals(1, tatabroService.getTatabros().size());
    }

    @Test
    public void testCreateTatabroWithParents() {
        stage2();
        when(tatabroService.getTatabros()).thenReturn(tatabros);
        Tatabro tatabroChild = new Tatabro(null, "Tommy", "M", 50, 15, 90, LocalDateTime.now(), tatabros.get(0).getId(), tatabros.get(1).getId());
        when(tatabroRepository.findById(tatabros.get(0).getId())).thenReturn(Optional.of(new Tatabro(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
        when(tatabroRepository.findById(tatabros.get(1).getId())).thenReturn(Optional.of(new Tatabro(UUID.randomUUID(), "Maggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null)));
        assertDoesNotThrow(() -> tatabroService.createTatabro(tatabroChild));
        verify(tatabroRepository, times(3)).save(any());
        tatabros.add(tatabroChild);
        assertEquals(3, tatabroService.getTatabros().size());
    }

    @Test
    public void testCreateTatabroWithParentsNonExistent() {
        stage2();
        when(tatabroService.getTatabros()).thenReturn(tatabros);
        when(tatabroRepository.findById(tatabros.get(0).getId())).thenReturn(Optional.of(new Tatabro(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
        when(tatabroRepository.findById(tatabros.get(1).getId())).thenReturn(Optional.of(new Tatabro(UUID.randomUUID(), "Maggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null)));
        assertThrows(TatabroException.class, () -> tatabroService.createTatabro(new Tatabro(null, "Tommy", "M", 50, 15, 90, LocalDateTime.now(), UUID.fromString("e964cdd5-051f-4f9f-b6c8-d4a227d97719"), tatabros.get(1).getId())), "Tatabro ID doesn't exist.");
        assertThrows(TatabroException.class, () -> tatabroService.createTatabro(new Tatabro(null, "Tommy", "M", 50, 15, 90, LocalDateTime.now(), tatabros.get(0).getId(), UUID.fromString("e964cdd5-051f-4f9f-b6c8-d4a227d97719"))), "Tatabro ID doesn't exist.");
        verify(tatabroRepository, times(2)).save(any());
        assertEquals(2, tatabroService.getTatabros().size());
    }

    @Test
    public void testCreateTatabroWithParentsSameSexMale() {
        stage3();
        when(tatabroService.getTatabros()).thenReturn(tatabros);
        when(tatabroRepository.findById(tatabros.get(0).getId())).thenReturn(Optional.of(new Tatabro(null, "Piggy", "M", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
        when(tatabroRepository.findById(tatabros.get(1).getId())).thenReturn(Optional.of(new Tatabro(UUID.randomUUID(), "Maggy", "M", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null)));
        assertThrows(TatabroException.class, () -> tatabroService.createTatabro(new Tatabro(null, "Tommy", "M", 50, 15, 90, LocalDateTime.now(), tatabros.get(0).getId(), tatabros.get(1).getId())), "Tatabro's father must be a male and tatabro's mother must be a female.");
        verify(tatabroRepository, times(2)).save(any());
        assertEquals(2, tatabroService.getTatabros().size());
    }

    @Test
    public void testCreateTatabroWithParentsSameSexFemale() {
        stage4();
        when(tatabroService.getTatabros()).thenReturn(tatabros);
        when(tatabroRepository.findById(tatabros.get(0).getId())).thenReturn(Optional.of(new Tatabro(null, "Piggy", "F", 40, 15, 90, LocalDateTime.of(2022, 9, 11, 12, 1, 22), null, null)));
        when(tatabroRepository.findById(tatabros.get(1).getId())).thenReturn(Optional.of(new Tatabro(UUID.randomUUID(), "Maggy", "F", 42, 17, 92, LocalDateTime.of(2021, 8, 14, 11, 4, 25), null, null)));
        assertThrows(TatabroException.class, () -> tatabroService.createTatabro(new Tatabro(null, "Tommy", "M", 50, 15, 90, LocalDateTime.now(), tatabros.get(0).getId(), tatabros.get(1).getId())), "Tatabro's father must be a male and tatabro's mother must be a female.");
        verify(tatabroRepository, times(2)).save(any());
        assertEquals(2, tatabroService.getTatabros().size());
    }
}