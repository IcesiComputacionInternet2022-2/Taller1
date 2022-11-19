package co.i.e.a.service;

import co.icesi.edu.animals.constant.CaripiareErrorCode;
import co.icesi.edu.animals.dto.CaripiareAndParentsDTO;
import co.icesi.edu.animals.error.exception.CaripiareException;
import co.icesi.edu.animals.mapper.CaripiareMapper;
import co.icesi.edu.animals.model.Caripiare;
import co.icesi.edu.animals.repository.CaripiareRepository;
import co.icesi.edu.animals.service.CaripiareService;
import co.icesi.edu.animals.service.impl.CaripiareServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CaripiareServiceTest {

    private CaripiareService caripiareService;
    private CaripiareRepository caripiareRepository;
    private CaripiareMapper caripiareMapper;
    private Caripiare caripiareFather;
    private Caripiare caripiareMother;
    private Caripiare caripiareChildren;
    private List<Caripiare> caripiareList;
    private CaripiareAndParentsDTO caripiareAndParentsDTO;

    @BeforeEach
    public void init(){
        caripiareRepository = mock(CaripiareRepository.class);
        caripiareMapper = mock(CaripiareMapper.class);
        caripiareService = new CaripiareServiceImpl(caripiareRepository, caripiareMapper);
    }

    private void setUpStage() {
        caripiareFather = new Caripiare(UUID.randomUUID(), "father", "M", 3.5, 9,
                0.15, LocalDate.now(), null, null);
        caripiareMother = new Caripiare(UUID.randomUUID(), "mother", "F", 3.5, 9,
                0.15, LocalDate.now(), null, null);
        caripiareChildren = new Caripiare(UUID.randomUUID(), "children", "M", 3.5, 9,
                0.15, LocalDate.now(), null, null);
        caripiareList = new ArrayList<>();
        caripiareList.add(caripiareFather);
        caripiareList.add(caripiareMother);
        caripiareList.add(caripiareChildren);
        caripiareAndParentsDTO = caripiareMapper.fromCaripiareDTOtoCaripiareAndParentsDTO(caripiareChildren, caripiareFather, caripiareMother);
    }

    @Test
    public void testCreateCaripiare() {
        setUpStage();
        when(caripiareRepository.save(any())).thenReturn(caripiareFather);
        Caripiare expectedCaripiareFather = caripiareService.createCaripiare(caripiareFather);
        verify(caripiareRepository, times(1)).save(any());
        assertEquals(caripiareFather, expectedCaripiareFather);
    }

    @Test
    public void testCreateCaripiareAndParents() {
        setUpStage();
        when(caripiareRepository.save(any())).thenReturn(caripiareChildren);
        when(caripiareRepository.findById(      caripiareFather.getId())).thenReturn(Optional.of(caripiareFather));
        when(caripiareRepository.findById(caripiareMother.getId())).thenReturn(Optional.of(caripiareMother));

        caripiareChildren.setFatherId(caripiareFather.getId());
        caripiareChildren.setMotherId(caripiareMother.getId());
        Caripiare actualCaripiareChildren = caripiareService.createCaripiare(caripiareChildren);

        verify(caripiareRepository, times(1)).save(caripiareChildren);
        verify(caripiareRepository, times(1)).findById(caripiareFather.getId());
        verify(caripiareRepository, times(1)).findById(caripiareMother.getId());
        assertEquals(caripiareChildren, actualCaripiareChildren);
        assertEquals(caripiareFather.getId(), actualCaripiareChildren.getFatherId());
        assertEquals(caripiareMother.getId(), actualCaripiareChildren.getMotherId());
    }

    @Test
    public void testCreateCaripiareAndOneNullParent() {
        setUpStage();
        when(caripiareRepository.save(any())).thenReturn(caripiareChildren);
        when(caripiareRepository.findById(caripiareFather.getId())).thenReturn(Optional.of(caripiareFather));

        caripiareChildren.setFatherId(caripiareFather.getId());
        Caripiare actualCaripiareChildren = caripiareService.createCaripiare(caripiareChildren);

        verify(caripiareRepository, times(1)).save(caripiareChildren);
        verify(caripiareRepository, times(1)).findById(caripiareFather.getId());
        assertEquals(caripiareChildren, actualCaripiareChildren);
        assertEquals(caripiareFather.getId(), actualCaripiareChildren.getFatherId());
        assertNull(actualCaripiareChildren.getMotherId());
    }

    @Test
    public void testCreateCaripiareDuplicatedName(){
        setUpStage();
        try {
            when(caripiareRepository.findAll()).thenReturn(caripiareList);
            Caripiare duplicatedNameCaripiare = new Caripiare(caripiareFather.getId(), "father", "M",
                    3.5, 9, 0.15, LocalDate.now(), null, null);
            caripiareService.createCaripiare(duplicatedNameCaripiare);
            fail("Expected caripiareException");
        } catch(CaripiareException caripiareException) {
            assertEquals(CaripiareErrorCode.CODE_01.getMessage(), caripiareException.getCaripiareError().getMessage());
        }
    }

    @Test
    public void testCreateCaripiareAndParentsMaleGenderValidation() {
        setUpStage();
        when(caripiareRepository.save(any())).thenReturn(caripiareChildren);
        when(caripiareRepository.findById(caripiareFather.getId())).thenReturn(Optional.of(caripiareFather));
        when(caripiareRepository.findById(caripiareMother.getId())).thenReturn(Optional.of(caripiareMother));

        caripiareChildren.setFatherId(caripiareFather.getId());
        caripiareChildren.setMotherId(caripiareMother.getId());
        caripiareFather.setGender("F");

        try {
            caripiareService.createCaripiare(caripiareChildren);
            fail("Expected caripiareException");
        } catch(CaripiareException caripiareException) {
            assertEquals(CaripiareErrorCode.CODE_03.getMessage(), caripiareException.getCaripiareError().getMessage());
        }
    }

    @Test
    public void testCreateCaripiareAndParentsFemaleGenderValidation() {
        setUpStage();
        when(caripiareRepository.save(any())).thenReturn(caripiareChildren);
        when(caripiareRepository.findById(caripiareFather.getId())).thenReturn(Optional.of(caripiareFather));
        when(caripiareRepository.findById(caripiareMother.getId())).thenReturn(Optional.of(caripiareMother));

        caripiareChildren.setFatherId(caripiareFather.getId());
        caripiareChildren.setMotherId(caripiareMother.getId());
        caripiareMother.setGender("M");

        try {
            caripiareService.createCaripiare(caripiareChildren);
            fail("Expected caripiareException");
        } catch(CaripiareException caripiareException) {
            assertEquals(CaripiareErrorCode.CODE_03.getMessage(), caripiareException.getCaripiareError().getMessage());
        }
    }

    @Test
    public void testGetCaripiare() {
        setUpStage();
        when(caripiareRepository.findById(caripiareFather.getId())).thenReturn(Optional.of(caripiareFather));
        Caripiare actualCaripiare = caripiareService.getCaripiare(caripiareFather.getId());
        verify(caripiareRepository, times(1)).findById(any());
        assertEquals(caripiareFather, actualCaripiare);
    }

    @Test
    public void testGetCaripiareNotFound() {
        setUpStage();
        when(caripiareRepository.findById(caripiareFather.getId())).thenReturn(Optional.of(caripiareFather));
        try {
            caripiareService.getCaripiare(caripiareMother.getId());
        }catch (CaripiareException caripiareException) {
            verify(caripiareRepository, times(1)).findById(any());
            assertEquals(CaripiareErrorCode.CODE_02.getMessage(), caripiareException.getCaripiareError().getMessage());
        }
    }

    @Test
    public void testGetCaripiareAndParents() {
        setUpStage();
        when(caripiareMapper.fromCaripiareDTOtoCaripiareAndParentsDTO(
                caripiareChildren, caripiareFather, caripiareMother)).thenReturn(caripiareAndParentsDTO);
        when(caripiareRepository.findAll()).thenReturn(caripiareList);
        CaripiareAndParentsDTO actualCaripiareAndParentsDTO = caripiareService.getCaripiareAndParents(caripiareChildren.getName());
        verify(caripiareMapper, times(2)).fromCaripiareDTOtoCaripiareAndParentsDTO(any(), any(), any());
        assertEquals(caripiareAndParentsDTO, actualCaripiareAndParentsDTO);
    }

    @Test
    public void testGetCaripiareAndParentsNotFound() {
        setUpStage();
        when(caripiareMapper.fromCaripiareDTOtoCaripiareAndParentsDTO(
                caripiareChildren, caripiareFather, caripiareMother)).thenReturn(caripiareAndParentsDTO);
        try {
            caripiareService.getCaripiareAndParents(caripiareFather.getName());
        }catch (CaripiareException caripiareException) {
            verify(caripiareMapper, times(1)).fromCaripiareDTOtoCaripiareAndParentsDTO(any(), any(), any());
            assertEquals(CaripiareErrorCode.CODE_02.getMessage(), caripiareException.getCaripiareError().getMessage());
        }
    }

    @Test
    public void testGetCaripiares() {
        setUpStage();
        when(caripiareRepository.findAll()).thenReturn(caripiareList);
        assertEquals(3, caripiareService.getCaripiares().size());
        verify(caripiareRepository, times(1)).findAll();
    }

    @Test
    public void testGetCaripiaresEmpty() {
        setUpStage();
        when(caripiareRepository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, caripiareService.getCaripiares().size());
        verify(caripiareRepository, times(1)).findAll();
    }
}
