package com.co.edu.icesi.zooWeb.service.impl;

import com.co.edu.icesi.zooWeb.model.BlackSwan;
import com.co.edu.icesi.zooWeb.repository.BlackSwanRepository;
import com.co.edu.icesi.zooWeb.service.BlackSwanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BlackSwanServiceTest {

    private BlackSwanRepository blackSwanRepository;
    private BlackSwanService blackSwanService;
    private String blackSwanName;
    private BlackSwan blackSwan;
    private BlackSwan newBlackSwan;
    private BlackSwan mother;
    private BlackSwan father;

    @BeforeEach
    private void init() {
        blackSwanRepository= mock(BlackSwanRepository.class);
        blackSwanService = new BlackSwanServiceImpl(blackSwanRepository);
    }

    private void scene1(){
        blackSwanName="Paco";
        char sex='M';
        double weight=4;
        int age=13;
        double height=110;
        LocalDateTime arrivalDate= LocalDateTime.of(2020,9,22,11,11,0);
        UUID fatherID=UUID.nameUUIDFromBytes("creatingANewMaleId".getBytes());
        UUID motherID=UUID.nameUUIDFromBytes("creatingANewFemaleId".getBytes());
        blackSwan = new BlackSwan(UUID.randomUUID(),blackSwanName,sex,weight,age,height,arrivalDate,fatherID,motherID);
    }

    private void scene2(){
        String name = "Pedropicapapas";
        char sex='M';
        double weight=4;
        int age=13;
        double height=110;
        LocalDateTime arrivalDate= LocalDateTime.of(2020,9,22,11,11,0);
        UUID fatherID=UUID.nameUUIDFromBytes("creatingANewMaleId".getBytes());
        UUID motherID=UUID.nameUUIDFromBytes("creatingANewFemaleId".getBytes());
        newBlackSwan=new BlackSwan(UUID.randomUUID(),name,sex,weight,age,height,arrivalDate,fatherID,motherID);
    }

    private void scene3(){
        String name = "Eugenia";
        char sex='F';
        double weight=4;
        int age=13;
        double height=110;
        LocalDateTime arrivalDate= LocalDateTime.of(2020,9,22,11,11,0);;
        UUID fatherID=UUID.nameUUIDFromBytes("creatingANewMaleId".getBytes());
        UUID motherID=UUID.nameUUIDFromBytes("creatingANewFemaleId".getBytes());
        mother=new BlackSwan(UUID.randomUUID(),name,sex,weight,age,height,arrivalDate,fatherID,motherID);
    }

    private void scene4(){
        String name = "Yanfri";
        char sex='M';
        double weight=4;
        int age=13;
        double height=110;
        LocalDateTime arrivalDate= LocalDateTime.of(2020,9,22,11,11,0);;
        UUID fatherID=UUID.nameUUIDFromBytes("creatingANewMaleId".getBytes());
        UUID motherID=UUID.nameUUIDFromBytes("creatingANewFemaleId".getBytes());
        father=new BlackSwan(UUID.randomUUID(),name,sex,weight,age,height,arrivalDate,fatherID,motherID);
    }

    private List<BlackSwan> fakeSwans(){
        List<BlackSwan> fakeAnimals = new ArrayList<>();
        fakeAnimals.add(blackSwan);
        fakeAnimals.add(father);
        fakeAnimals.add(mother);
        return fakeAnimals;
    }


    private boolean createGeneratesException(){
        try {
            blackSwanService.createSwan(blackSwan);
        }
        catch (Exception e){
            return true;
        }
        return false;
    }

    private boolean updateGeneratesException(){
        when(blackSwanRepository.findAll()).thenReturn(fakeSwans());
        try {
            blackSwanService.updateSwan(blackSwanName,newBlackSwan);
        }
        catch (Exception e){
            return true;
        }
        return false;
    }

    @Test
    public void testCreateSwan(){
        scene1();
        assertFalse(createGeneratesException());
        verify(blackSwanRepository,times(1)).save(any());
    }

    @Test
    public void testUpdateSwan(){
        scene1();
        scene2();
        assertFalse(updateGeneratesException());
        verify(blackSwanRepository,times(1)).save(any());
    }

    @Test
    public void testGetSwan() {
        scene1();
        when(blackSwanRepository.findAll()).thenReturn(fakeSwans());
        List<BlackSwan> obtainedAnimal = blackSwanService.getSwan(blackSwanName);
        verify(blackSwanRepository, times(1)).findAll();
        verify(blackSwanRepository, times(2)).findById(any());
    }

    @Test
    public void testGetSwans() {
        blackSwanService.getSwans();
        verify(blackSwanRepository, times(1)).findAll();
    }



    @Test
    public void testCreateMother(){
        scene1();
        scene4();
        blackSwan.setMotherId(father.getId());
        when(blackSwanRepository.findById(blackSwan.getMotherId())).thenReturn(Optional.ofNullable(father));
        assertFalse(createGeneratesException());
    }

    @Test
    public void testUpdateMother(){
        scene1();
        scene2();
        scene4();
        newBlackSwan.setMotherId(father.getId());
        when(blackSwanRepository.findById(newBlackSwan.getMotherId())).thenReturn(Optional.ofNullable(father));
        assertFalse(updateGeneratesException());
    }

    @Test
    public void testCreateFather(){
        scene1();
        scene3();
        blackSwan.setFatherId(mother.getId());
        when(blackSwanRepository.findById(blackSwan.getFatherId())).thenReturn(Optional.ofNullable(mother));
        assertFalse(createGeneratesException());
    }

    @Test
    public void testUpdateFather(){
        scene1();
        scene2();
        scene3();
        newBlackSwan.setFatherId(mother.getId());
        when(blackSwanRepository.findById(newBlackSwan.getFatherId())).thenReturn(Optional.ofNullable(mother));
        assertFalse(updateGeneratesException());
    }

    @Test
    public void sexChange(){
        scene1();
        scene2();
        newBlackSwan.setSex('F');
        assertTrue(updateGeneratesException());
    }
}
