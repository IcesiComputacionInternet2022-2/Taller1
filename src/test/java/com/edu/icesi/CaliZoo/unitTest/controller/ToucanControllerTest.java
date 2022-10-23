package com.edu.icesi.CaliZoo.unitTest.controller;

import com.edu.icesi.CaliZoo.controller.ToucanController;
import com.edu.icesi.CaliZoo.dto.ToucanDTO;
import com.edu.icesi.CaliZoo.error.exception.ToucanException;
import com.edu.icesi.CaliZoo.mapper.ToucanMapper;
import com.edu.icesi.CaliZoo.model.Toucan;
import com.edu.icesi.CaliZoo.service.ToucanService;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;


import static org.mockito.Mockito.mock;

public class ToucanControllerTest {

    private ToucanController toucanController;

    @BeforeEach
    private void init(){
        ToucanMapper toucanMapper = mock(ToucanMapper.class);
        ToucanService toucanService = mock(ToucanService.class);
        toucanController = new ToucanController(toucanService, toucanMapper);
    }//End init

    @Test
    public void testVerifyNameConstraint(){
        UUID toucanId = UUID.fromString("8c657e5d-79cc-457f-b1e8-e897a3556857");
        ToucanDTO toucan = new ToucanDTO(toucanId, null, null, "Tou%&can", "M", 140, 5, 20,
                LocalDate.of(2022, 10, 2));
        assertThrows(ToucanException.class, ()-> toucanController.createToucan(toucan));
    }//End testVerifyNameConstraint

    @Test
    public void testVerifyArrivalDate(){
        UUID toucanId = UUID.fromString("8c657e5d-79cc-457f-b1e8-e897a3556857");
        ToucanDTO toucan = new ToucanDTO(toucanId, null, null, "Toucan", "M",
                140, 5, 20, getNextDate());
        assertThrows(ToucanException.class, ()-> toucanController.createToucan(toucan));
    }//End testVerifyArrivalDate

    @Test
    public void testValidateSexInBounds(){
        UUID toucanId = UUID.fromString("8c657e5d-79cc-457f-b1e8-e897a3556857");
        ToucanDTO toucan = new ToucanDTO(toucanId, null, null, "Toucan", "S",
                140, 5, 20, LocalDate.of(2022, 10, 2));
        assertThrows(ToucanException.class, ()-> toucanController.createToucan(toucan));
    }//End testValidateSexInBounds

    private LocalDate getNextDate(){
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        if((month + 1) > 12 )
            year += 1;
        else
            month += 1;
        return LocalDate.of(year, month, 1);
    }//End getNextDay
}//End ToucanControllerTest
