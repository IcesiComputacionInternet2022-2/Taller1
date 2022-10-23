package com.co.edu.icesi.zooWeb.controller;

import com.co.edu.icesi.zooWeb.dto.BlackSwanDTO;
import com.co.edu.icesi.zooWeb.mapper.BlackSwanMapper;
import com.co.edu.icesi.zooWeb.service.BlackSwanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class BlackSwanControllerTest {
    private BlackSwanController blackSwanController;
    private BlackSwanService blackSwanService;
    private BlackSwanMapper blackSwanMapper;
    private BlackSwanDTO blackSwanDTO;
    private BlackSwanDTO newBlackSwanDTO;
    private String blackSwanName;

    @BeforeEach
    private void init(){
    blackSwanService= mock(BlackSwanService.class);
    blackSwanMapper=mock(BlackSwanMapper.class);
    blackSwanController= new BlackSwanController(blackSwanService,blackSwanMapper);
    }

    private void scene1(){
        blackSwanName="Paco";
        char sex='M';
        double weight=4;
        int age=13;
        double height=120;
        LocalDateTime arrivalDate= LocalDateTime.of(2020,9,22,11,11,0);
        UUID fatherID=UUID.nameUUIDFromBytes("creatingANewMaleId".getBytes());
        UUID motherID=UUID.nameUUIDFromBytes("creatingANewFemaleId".getBytes());
        blackSwanDTO = new BlackSwanDTO(UUID.randomUUID(),blackSwanName,sex,weight,age,height,arrivalDate,fatherID,motherID);
    }

    private void scene2(){
        String name = "PedroPicaPapas";
        char sex='M';
        double weight=4;
        int age=14;
        double height=120;
        LocalDateTime arrivalDate=  LocalDateTime.of(2020,9,22,11,11,0);;
        UUID fatherID=UUID.nameUUIDFromBytes("creatingANewMaleId".getBytes());
        UUID motherID=UUID.nameUUIDFromBytes("creatingANewFemaleId".getBytes());
        newBlackSwanDTO=new BlackSwanDTO(UUID.randomUUID(),name,sex,weight,age,height,arrivalDate,fatherID,motherID);
    }

    @Test
    public void testCreateSwan(){
        scene1();
        assertFalse(createGeneratesException());
    }

    @Test
    public void testUpdateAnimal(){
        scene1();
        scene2();
        assertFalse(updateGeneratesException());
    }

    @Test
    public void testGetAnimals(){
        blackSwanController.getSwans();
        verify(blackSwanService, times(1)).getSwans();
    }

    @Test
    public void testGetUser(){
        scene1();
        blackSwanController.getSwan(blackSwanName);
        verify(blackSwanService,times(1)).getSwan(blackSwanName);
    }

    @Test
    public void testValidateNameNoNumbers(){
        scene1();
        blackSwanDTO.setName("222232323323233232321313131231231231231231231241241241241241244444444444444");
        assertTrue(createGeneratesException() && updateGeneratesException());
    }

    @Test
    public void testValidateNameStringSize(){
        scene1();
        String name = StringUtils.repeat("h", 121);
        blackSwanDTO.setName(name);
        assertTrue(createGeneratesException() && updateGeneratesException());
    }

    @Test
    public void testValidateDate(){
        scene1();
        blackSwanDTO.setArrivedZooDate(LocalDateTime.MAX);
        assertTrue(createGeneratesException() && updateGeneratesException());
    }

    @Test
    public void testValidateAnimalHeight(){
        scene1();
        blackSwanDTO.setHeight(141);
        assertFalse(createGeneratesException() && updateGeneratesException());
    }

    @Test
    public void testValidateAnimalWeight(){
        scene1();
        blackSwanDTO.setWeight(10);
        assertFalse(createGeneratesException() && updateGeneratesException());
    }

    @Test
    public void testValidateAnimalAge(){
        scene1();
        blackSwanDTO.setAge(24);
        assertFalse(createGeneratesException() && updateGeneratesException());
    }


    private boolean createGeneratesException(){
        when(blackSwanMapper.fromBlackSwan(any())).thenReturn(blackSwanDTO);
        try {
            blackSwanController.createSwan(blackSwanDTO);
        }
        catch (Exception e){
            return true;
        }
        return false;
    }

    private boolean updateGeneratesException(){
        when(blackSwanMapper.fromBlackSwan(any())).thenReturn(blackSwanDTO);
        try {
            blackSwanController.updateSwan(blackSwanName,blackSwanDTO);
        }
        catch (Exception e){
            return true;
        }
        return false;
    }
}
