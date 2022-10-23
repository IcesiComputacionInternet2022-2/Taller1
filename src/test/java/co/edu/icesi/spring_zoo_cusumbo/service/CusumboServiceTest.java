package co.edu.icesi.spring_zoo_cusumbo.service;

import co.edu.icesi.spring_zoo_cusumbo.controller.CusumboController;
import co.edu.icesi.spring_zoo_cusumbo.dto.CusumboDTO;
import co.edu.icesi.spring_zoo_cusumbo.error.ErrorCode;
import co.edu.icesi.spring_zoo_cusumbo.error.exception.CusumboError;
import co.edu.icesi.spring_zoo_cusumbo.error.exception.CusumboException;
import co.edu.icesi.spring_zoo_cusumbo.mapper.CusumboMapper;
import co.edu.icesi.spring_zoo_cusumbo.mapper.CusumboMapperImpl;
import co.edu.icesi.spring_zoo_cusumbo.model.Cusumbo;
import co.edu.icesi.spring_zoo_cusumbo.repository.CusumboRepository;
import co.edu.icesi.spring_zoo_cusumbo.service.impl.CusumboServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CusumboServiceTest {
    private CusumboRepository cusumboRepository;

    private CusumboService cusumboService;

    private Cusumbo testCusumbo;
    private Cusumbo testCusumboMother;
    private Cusumbo testCusumboFather;

    @BeforeEach
    public void init(){
        cusumboRepository = mock(CusumboRepository.class);
        cusumboService = new CusumboServiceImpl(cusumboRepository);
    }

    private void createTestCusumbo(){
        testCusumbo = new Cusumbo(UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d74")
                ,"ImSpeed"
                ,'M'
                ,7
                ,1
                ,32
                ,LocalDateTime.now().minusDays(1)
                ,null
                ,null);
    }

    private void createMother(){
        testCusumboMother = new Cusumbo(UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d75")
                ,"ImSpeedMother"
                ,'F'
                ,6
                ,5
                ,30
                ,LocalDateTime.now().minusDays(1)
                ,null
                ,null);

        when(cusumboRepository.findById(testCusumboMother.getId())).thenReturn(Optional.of(testCusumboMother));
    }

    private void createFather(){
        testCusumboFather = new Cusumbo(UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d76")
                ,"ImSpeedFather"
                ,'M'
                ,7
                ,5
                ,32
                ,LocalDateTime.now().minusDays(1)
                ,null
                ,null);

        when(cusumboRepository.findById(testCusumboFather.getId())).thenReturn(Optional.of(testCusumboFather));
    }

    private void createTestCusumboFamily(){
        createTestCusumbo();
        createMother();
        createFather();

        testCusumbo.setFatherId(testCusumboFather.getId());
        testCusumbo.setMotherId(testCusumboMother.getId());

        when(cusumboRepository.findByName(testCusumbo.getName())).thenReturn(Optional.of(testCusumbo));
    }

    @Test
    public void testCreateCusumbo(){
        createTestCusumbo();

        when(cusumboRepository.save(ArgumentMatchers.any())).thenReturn(testCusumbo);
        when(cusumboRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());

        Cusumbo cusumbo = cusumboService.createCusumbo(testCusumbo);
        assertEquals(testCusumbo.getId(),cusumbo.getId());
    }

    @Test
    public void testGetCusumboWithParents(){
        createTestCusumboFamily();

        List<Cusumbo> cusumboFamily = cusumboService.getCusumboWithParents(testCusumbo.getName());

        assertEquals(3, cusumboFamily.size());
        assertEquals(cusumboFamily.get(0).getId(), testCusumbo.getId());
        assertEquals(cusumboFamily.get(1).getId(), testCusumboFather.getId());
        assertEquals(cusumboFamily.get(2).getId(), testCusumboMother.getId());
    }

    @Test
    public void testgetCusumbos(){
        createTestCusumbo();
        when(cusumboRepository.findAll()).thenReturn(Collections.singletonList(testCusumbo));
        List<Cusumbo> cusumbos = cusumboService.getCusumbos();

        verify(cusumboRepository,times(1)).findAll();
        assertEquals(1,cusumbos.size());
        assertEquals(testCusumbo.getId(),cusumbos.get(0).getId());
    }

    @Test
    public void testValidateParentsBothParentsPresent(){
        createFather();
        createMother();
        createTestCusumbo();

        when(cusumboRepository.save(ArgumentMatchers.any())).thenReturn(testCusumbo);
        when(cusumboRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());

        Cusumbo cusumbo = cusumboService.createCusumbo(testCusumbo);

        //Pass if no exception is thrown
    }

    @Test
    public void testValidateParentsBothParentsPresentSameSex(){
        createTestCusumboFamily();

        testCusumboFather.setSex('F');

        when(cusumboRepository.save(ArgumentMatchers.any())).thenReturn(testCusumbo);
        when(cusumboRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboService.createCusumbo(testCusumbo),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_07B.getMessage(),thrown.getError().getMessage());
    }

    @Test
    public void testValidateParentsBothParentsPresentTranslocatedSex(){
        createTestCusumboFamily();

        testCusumboFather.setSex('F');
        testCusumboMother.setSex('M');

        when(cusumboRepository.save(ArgumentMatchers.any())).thenReturn(testCusumbo);
        when(cusumboRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboService.createCusumbo(testCusumbo),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_07C.getMessage(),thrown.getError().getMessage());
    }

    @Test
    public void testValidateParentsParentsNotPresent(){
        createTestCusumbo();

        testCusumbo.setFatherId(UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d76"));
        testCusumbo.setMotherId(UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d75"));

        when(cusumboRepository.save(ArgumentMatchers.any())).thenReturn(testCusumbo);
        when(cusumboRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboService.createCusumbo(testCusumbo),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_07A.getMessage(),thrown.getError().getMessage());
    }
    @Test
    public void testValidateParentsFatherPresent(){
        createTestCusumbo();
        createFather();

        testCusumbo.setFatherId(testCusumboFather.getId());

        when(cusumboRepository.save(ArgumentMatchers.any())).thenReturn(testCusumbo);
        when(cusumboRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());

        cusumboService.createCusumbo(testCusumbo);

    }

    @Test
    public void testValidateParentsFatherPresentWrongSex(){
        createTestCusumbo();
        createFather();
        testCusumboFather.setSex('F');

        testCusumbo.setFatherId(testCusumboFather.getId());

        when(cusumboRepository.save(ArgumentMatchers.any())).thenReturn(testCusumbo);
        when(cusumboRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboService.createCusumbo(testCusumbo),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_07C.getMessage(),thrown.getError().getMessage());

    }

    @Test
    public void testValidateParentsMotherPresent(){
        createTestCusumbo();
        createMother();

        testCusumbo.setMotherId(testCusumboMother.getId());

        when(cusumboRepository.save(ArgumentMatchers.any())).thenReturn(testCusumbo);
        when(cusumboRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());

        cusumboService.createCusumbo(testCusumbo);

    }

    @Test
    public void testValidateParentsMotherPresentWrongSex(){
        createTestCusumbo();
        createMother();
        testCusumboMother.setSex('M');

        testCusumbo.setMotherId(testCusumboMother.getId());

        when(cusumboRepository.save(ArgumentMatchers.any())).thenReturn(testCusumbo);
        when(cusumboRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboService.createCusumbo(testCusumbo),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_07C.getMessage(),thrown.getError().getMessage());

    }


    @Test
    public void testValidateUniqueName(){

        createTestCusumboFamily();

        CusumboException thrown =
                assertThrows(CusumboException.class,
                        () -> cusumboService.createCusumbo(testCusumbo),
                        "CusumboException expected");

        assertEquals(ErrorCode.CODE_ATR_01B.getMessage(),thrown.getError().getMessage());

    }




}
