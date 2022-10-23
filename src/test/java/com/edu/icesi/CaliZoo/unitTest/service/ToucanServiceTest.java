package com.edu.icesi.CaliZoo.unitTest.service;

import com.edu.icesi.CaliZoo.error.exception.ToucanException;
import com.edu.icesi.CaliZoo.model.Toucan;
import com.edu.icesi.CaliZoo.repository.ToucanRepository;
import com.edu.icesi.CaliZoo.service.ToucanService;
import com.edu.icesi.CaliZoo.service.impl.ToucanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


public class ToucanServiceTest {

    private ToucanService toucanService;
    private ToucanRepository toucanRepository;

    @BeforeEach
    private void init(){
        toucanRepository = mock(ToucanRepository.class);
        toucanService = new ToucanServiceImpl(toucanRepository);
    }//End init

    private Toucan setUpToucan(){
        Toucan toucan = new Toucan(UUID.fromString("fdec867f-26a7-4691-8875-4d99b20fa861"), null, null,
                "Daniel", "M", 140, 5, 20, LocalDate.of(2022, 10, 2));
        return toucan;
    }

    private List<Toucan> setUpToucanParents(){
        List<Toucan> family = new ArrayList<>();
        UUID fatherId = UUID.fromString("ddec867f-26a7-4691-8875-4d99b20fa861");
        UUID motherId = UUID.fromString("fdec867f-26a7-4691-8875-4d99b20fa861");
        family.add(new Toucan(fatherId, null, null, "Padre", "M", 140, 5, 20,
                LocalDate.of(2022, 10, 2)));
        family.add(new Toucan(motherId, null, null, "Madre", "F", 140, 5, 20,
                LocalDate.of(2022, 10, 2)));
        return family;
    }//End setUpToucanFamily

    @Test
    public void testAddCorrectToucan(){
        Toucan toucan = new Toucan(UUID.fromString("fdec867f-26a7-4691-8875-4d99b20fa861"), null, null,
                "Brian", "M", 140, 5, 20, LocalDate.of(2022, 10, 2));
        when(toucanRepository.save(any())).thenReturn(toucan);
        assertEquals(toucan, toucanService.createToucan(toucan));
    }//End testAddCorrectToucan

   /* @Test
    public void testNotAddAnimalWithExistingName(){
        Toucan toucan = setUpToucan();
        toucanService.createToucan(toucan);
        UUID id = UUID.fromString("fdec867f-26a7-3691-8875-5d99b20fa861");
        Toucan toucanSameName = new Toucan(id, null, null,
                toucan.getName(), "M", 145, 7, 25, LocalDate.of(2022, 10, 3));
        assertThrows(ToucanException.class,() -> toucanService.createToucan(toucanSameName));
        verify(toucanRepository, times(0)).save(any());
    }//End testNotAddAnimalWithExistingName*/

    @Test
    public void testCorrectFatherSex(){
        List<Toucan> parents = setUpToucanParents();
        Toucan son = setUpToucan();
        son.setFatherId(parents.get(1).getId());
        assertThrows(ToucanException.class,()-> toucanService.createToucan(son));
        verify(toucanRepository, times(0)).save(any());
    }//End testNotAddAnimalWithExistingName

    @Test
    public void testCorrectMotherSex(){
        List<Toucan> parents = setUpToucanParents();
        Toucan son = setUpToucan();
        son.setMotherId(parents.get(0).getId());
        assertThrows(ToucanException.class,()-> toucanService.createToucan(son));
        verify(toucanRepository, times(0)).save(any());
    }//End testNotAddAnimalWithExistingName

    @Test
    public void testVerifyThereIsParentWithId(){
        Toucan toucan = setUpToucan();
        UUID notExistingId = UUID.fromString("fdec867f-26a7-4691-a875-4d99b208b861");
        toucan.setFatherId(notExistingId);
        assertThrows(ToucanException.class,()-> toucanService.createToucan(toucan));
        verify(toucanRepository, times(0)).save(any());
    }//End testVerifyThereIsParentWithId

    @Test
    public void testAddFatherToToucan(){
        UUID fatherId = UUID.fromString("e035341b-461a-45f1-b5d2-01a97f55298d");
        UUID sonId = UUID.fromString("b41ef241-caec-404b-84d3-44816c4f06f7");
        Toucan father = new Toucan(fatherId, null, null, "S", "M", 600, 5,
                65, LocalDate.of(2022, 10, 2));
        Toucan f = toucanService.createToucan(father);
        Toucan son = new Toucan(sonId, fatherId, null, "FU", "M", 600, 5, 65,
                LocalDate.of(2022, 10, 2));
        assertThrows(ToucanException.class,()-> toucanService.createToucan(son));
    }//End testAddFatherToToucan
}//End ToucanServiceTest

