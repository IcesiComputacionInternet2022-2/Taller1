package co.edu.icesi.calizoowebapp.service;

import co.edu.icesi.calizoowebapp.constants.AfricanLionErrorCode;
import co.edu.icesi.calizoowebapp.constants.AnimalSex;
import co.edu.icesi.calizoowebapp.error.exception.AfricanLionException;
import co.edu.icesi.calizoowebapp.model.AfricanLion;
import co.edu.icesi.calizoowebapp.model.AfricanLionQueryResponse;
import co.edu.icesi.calizoowebapp.repository.AfricanLionRespository;
import co.edu.icesi.calizoowebapp.service.impl.AfricanLionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class AfricanLionServiceTest {

    private AfricanLionRespository africanLionRespository;
    private AfricanLionService africanLionService;


    @BeforeEach
    public void init(){
        africanLionRespository = mock(AfricanLionRespository.class);
        africanLionService = new AfricanLionServiceImpl(africanLionRespository);
    }

    private ArrayList<AfricanLion> setUpScenarioNewLionsList(){

        ArrayList<AfricanLion> currentAfricanLionList;
        String lionId = "69a91b05-4621-4a79-9c94-372fa108d132";
        String lionName = "lionFather";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 240;
        int lionAge = 5;
        double lionHeight = 110;
        String lionArrivedZooDate = "2020-09-10T08:10:59";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLion fatherAfricanLion =  new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        lionId = "72786b9d-5485-4512-995c-b658e098f46a";
        lionName = "lionMother";
        lionSex = AnimalSex.FEMALE;
        lionWeight = 170;
        lionAge = 2;
        lionHeight = 100;
        lionArrivedZooDate = "2021-10-30T10:45:50";
        AfricanLion motherAfricanLion =  new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        lionId = "82787b9d-5485-3102-005c-a658e018f76a";
        lionName = "lionOther";
        lionSex = AnimalSex.MALE;
        lionWeight = 230;
        lionAge = 7;
        lionHeight = 110;
        lionArrivedZooDate = "2020-03-20T12:35:22";
        lionFatherId = UUID.fromString("69a91b05-4621-4a79-9c94-372fa108d132");
        lionMotherId = UUID.fromString("72786b9d-5485-4512-995c-b658e098f46a");
        AfricanLion otherAfricanLion =  new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);


        currentAfricanLionList = new ArrayList<>();
        currentAfricanLionList.add(fatherAfricanLion);
        currentAfricanLionList.add(motherAfricanLion);
        currentAfricanLionList.add(otherAfricanLion);

        return currentAfricanLionList;
    }


    @Test
    public void testCreateLionWithoutParents(){

        String lionId = "0a26da1a-6673-4b7e-9b12-84fa6fe0f25a";
        String lionName = "lionfather";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 240;
        int lionAge = 5;
        double lionHeight = 110;
        String lionArrivedZooDate = "2020-09-20T10:10:10";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLion newAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        when(africanLionRespository.save(newAfricanLion)).thenReturn(newAfricanLion);
        when(africanLionRespository.findAll()).thenReturn(new ArrayList<AfricanLion>());
        africanLionService.createLion(newAfricanLion);
        verify(africanLionRespository, times(1)).save(newAfricanLion);
    }

    @Test
    public void testCreateLionWithParents(){
        ArrayList<AfricanLion> currentAfricanLionList = setUpScenarioNewLionsList();
        String lionId = "aaa6ad08-1dc0-4015-9e21-614afd01b2c9";
        String lionName = "lionchild";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 200;
        int lionAge = 0;
        double lionHeight = 105;
        String lionArrivedZooDate = "2021-12-10T08:10:59";
        UUID lionFatherId = UUID.fromString("69a91b05-4621-4a79-9c94-372fa108d132");
        UUID lionMotherId = UUID.fromString("cbe5ea52-0edb-4d2e-a883-1488f1520b20");
        AfricanLion newAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        when(africanLionRespository.save(newAfricanLion)).thenReturn(newAfricanLion);
        when(africanLionRespository.findAll()).thenReturn(currentAfricanLionList);
        when(africanLionRespository.findById(lionFatherId)).thenReturn(Optional.ofNullable(currentAfricanLionList.get(0)));
        when(africanLionRespository.findById(lionMotherId)).thenReturn(Optional.ofNullable(currentAfricanLionList.get(1)));
        africanLionService.createLion(newAfricanLion);
        verify(africanLionRespository, times(1)).save(newAfricanLion);
    }

    @Test
    public void testCreateLionWithParentsSameSex(){
        String lionId = "0a26da1a-6673-4b7e-9b12-84fa6fe0f25a";
        String lionName = "invalidLion";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 200;
        int lionAge = 5;
        double lionHeight = 105;
        String lionArrivedZooDate = "2020-09-20T10:10:10";
        UUID lionFatherId = UUID.fromString("69a91b05-4621-4a79-9c94-372fa108d132");
        UUID lionMotherId = UUID.fromString("82787b9d-5485-3102-005c-a658e018f76a");
        AfricanLion invalidAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        ArrayList<AfricanLion> africanLionList = setUpScenarioNewLionsList();

        when(africanLionRespository.findAll()).thenReturn(africanLionList);
        when(africanLionRespository.findById(lionFatherId)).thenReturn(Optional.ofNullable(africanLionList.get(0)));
        when(africanLionRespository.findById(lionMotherId)).thenReturn(Optional.ofNullable(africanLionList.get(2)));

        try {
            africanLionService.createLion(invalidAfricanLion);
            fail();
        } catch (AfricanLionException africanLionException){
            verify(africanLionRespository, times(0)).save(invalidAfricanLion);
            assertEquals(africanLionException.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(africanLionException.getError().getCode(), AfricanLionErrorCode.CODE_03);
            assertEquals(africanLionException.getError().getMessage(), AfricanLionErrorCode.CODE_03.getMessage());
        }
    }

    @Test
    public void testCreateLionWithParentsNotSaved(){
        String lionId = "0a26da1a-6673-4b7e-9b12-84fa6fe0f25a";
        String lionName = "invalidLion";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 200;
        int lionAge = 5;
        double lionHeight = 105;
        String lionArrivedZooDate = "2020-09-20T10:10:10";
        UUID lionFatherId = UUID.fromString("10a91b05-4621-4a79-9c94-372fa108d110");
        UUID lionMotherId = UUID.fromString("80787b9d-5485-3102-005c-a658e018f71c");
        AfricanLion invalidAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        ArrayList<AfricanLion> africanLionList = setUpScenarioNewLionsList();

        when(africanLionRespository.findAll()).thenReturn(africanLionList);
        when(africanLionRespository.findById(lionFatherId)).thenReturn(Optional.empty());
        when(africanLionRespository.findById(lionMotherId)).thenReturn(Optional.empty());

        try {
            africanLionService.createLion(invalidAfricanLion);
            fail();
        } catch (AfricanLionException africanLionException){
            verify(africanLionRespository, times(0)).save(invalidAfricanLion);
            assertEquals(africanLionException.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(africanLionException.getError().getCode(), AfricanLionErrorCode.CODE_10);
            assertEquals(africanLionException.getError().getMessage(), AfricanLionErrorCode.CODE_10.getMessage());
        }
    }

    @Test
    public void testCreateLionRepeatedName(){
        String lionId = "0a26da1a-6673-4b7e-9b12-84fa6fe0f25a";
        String lionName = "lionfather";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 240;
        int lionAge = 5;
        double lionHeight = 110;
        String lionArrivedZooDate = "2020-09-20T10:10:10";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLion repeatedAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        ArrayList<AfricanLion> africanLionList = setUpScenarioNewLionsList();

        when(africanLionRespository.findAll()).thenReturn(africanLionList);
        try {
            africanLionService.createLion(repeatedAfricanLion);
            fail();
        } catch (AfricanLionException africanLionException){
            verify(africanLionRespository, times(0)).save(repeatedAfricanLion);
            assertEquals(africanLionException.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(africanLionException.getError().getCode(), AfricanLionErrorCode.CODE_02);
            assertEquals(africanLionException.getError().getMessage(), AfricanLionErrorCode.CODE_02.getMessage());
        }
    }

    @Test
    public void testCreateLionOutOfStandardMaleWeight(){
        String lionId = "0a26da1a-6673-4b7e-9b12-84fa6fe0f25a";
        String lionName = "lionOutStandard";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 260;
        int lionAge = 5;
        double lionHeight = 110;
        String lionArrivedZooDate = "2020-09-20T10:10:10";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLion outStandardsAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        ArrayList<AfricanLion> africanLionList = setUpScenarioNewLionsList();
        when(africanLionRespository.findAll()).thenReturn(africanLionList);

        try {
            africanLionService.createLion(outStandardsAfricanLion);
            fail();
        } catch (AfricanLionException africanLionException){
            verify(africanLionRespository, times(0)).save(outStandardsAfricanLion);
            assertEquals(africanLionException.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(africanLionException.getError().getCode(), AfricanLionErrorCode.CODE_06);
            assertEquals(africanLionException.getError().getMessage(), AfricanLionErrorCode.CODE_06.getMessage());
        }
    }

    @Test
    public void testCreateLionOutOfStandardFemaleWeight(){
        String lionId = "0a26da1a-6673-4b7e-9b12-84fa6fe0f25a";
        String lionName = "lionOutStandard";
        AnimalSex lionSex = AnimalSex.FEMALE;
        double lionWeight = 200;
        int lionAge = 5;
        double lionHeight = 110;
        String lionArrivedZooDate = "2020-09-20T10:10:10";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLion outStandardsAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        ArrayList<AfricanLion> africanLionList = setUpScenarioNewLionsList();
        when(africanLionRespository.findAll()).thenReturn(africanLionList);

        try {
            africanLionService.createLion(outStandardsAfricanLion);
            fail();
        } catch (AfricanLionException africanLionException){
            verify(africanLionRespository, times(0)).save(outStandardsAfricanLion);
            assertEquals(africanLionException.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(africanLionException.getError().getCode(), AfricanLionErrorCode.CODE_06);
            assertEquals(africanLionException.getError().getMessage(), AfricanLionErrorCode.CODE_06.getMessage());
        }
    }

    @Test
    public void testCreateLionOutOfStandardMaleHeight(){
        String lionId = "0a26da1a-6673-4b7e-9b12-84fa6fe0f25a";
        String lionName = "lionOutStandard";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 240;
        int lionAge = 5;
        double lionHeight = 160;
        String lionArrivedZooDate = "2020-09-20T10:10:10";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLion outStandardsAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        ArrayList<AfricanLion> africanLionList = setUpScenarioNewLionsList();
        when(africanLionRespository.findAll()).thenReturn(africanLionList);

        try {
            africanLionService.createLion(outStandardsAfricanLion);
            fail();
        } catch (AfricanLionException africanLionException){
            verify(africanLionRespository, times(0)).save(outStandardsAfricanLion);
            assertEquals(africanLionException.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(africanLionException.getError().getCode(), AfricanLionErrorCode.CODE_04);
            assertEquals(africanLionException.getError().getMessage(), AfricanLionErrorCode.CODE_04.getMessage());
        }
    }

    @Test
    public void testCreateLionOutOfStandardFemaleHeight(){
        String lionId = "0a26da1a-6673-4b7e-9b12-84fa6fe0f25a";
        String lionName = "lionOutStandard";
        AnimalSex lionSex = AnimalSex.FEMALE;
        double lionWeight = 160;
        int lionAge = 5;
        double lionHeight = 120;
        String lionArrivedZooDate = "2020-09-20T10:10:10";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLion outStandardsAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        ArrayList<AfricanLion> africanLionList = setUpScenarioNewLionsList();
        when(africanLionRespository.findAll()).thenReturn(africanLionList);

        try {
            africanLionService.createLion(outStandardsAfricanLion);
            fail();
        } catch (AfricanLionException africanLionException){
            verify(africanLionRespository, times(0)).save(outStandardsAfricanLion);
            assertEquals(africanLionException.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(africanLionException.getError().getCode(), AfricanLionErrorCode.CODE_04);
            assertEquals(africanLionException.getError().getMessage(), AfricanLionErrorCode.CODE_04.getMessage());
        }
    }

    @Test
    public void testCreateLionOutOfStandardMaleAge(){
        String lionId = "0a26da1a-6673-4b7e-9b12-84fa6fe0f25a";
        String lionName = "lionOutStandard";
        AnimalSex lionSex = AnimalSex.MALE;
        double lionWeight = 240;
        int lionAge = 20;
        double lionHeight = 110;
        String lionArrivedZooDate = "2020-09-20T10:10:10";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLion outStandardsAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        ArrayList<AfricanLion> africanLionList = setUpScenarioNewLionsList();
        when(africanLionRespository.findAll()).thenReturn(africanLionList);

        try {
            africanLionService.createLion(outStandardsAfricanLion);
            fail();
        } catch (AfricanLionException africanLionException){
            verify(africanLionRespository, times(0)).save(outStandardsAfricanLion);
            assertEquals(africanLionException.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(africanLionException.getError().getCode(), AfricanLionErrorCode.CODE_05);
            assertEquals(africanLionException.getError().getMessage(), AfricanLionErrorCode.CODE_05.getMessage());
        }
    }

    @Test
    public void testCreateLionOutOfStandardFemaleAge(){
        String lionId = "0a26da1a-6673-4b7e-9b12-84fa6fe0f25a";
        String lionName = "lionOutStandard";
        AnimalSex lionSex = AnimalSex.FEMALE;
        double lionWeight = 160;
        int lionAge = 16;
        double lionHeight = 100;
        String lionArrivedZooDate = "2020-09-20T10:10:10";
        UUID lionFatherId = null;
        UUID lionMotherId = null;
        AfricanLion outStandardsAfricanLion = new AfricanLion(UUID.fromString(lionId), lionName, lionSex, lionWeight, lionAge, lionHeight, LocalDateTime.parse(lionArrivedZooDate), lionFatherId, lionMotherId);

        ArrayList<AfricanLion> africanLionList = setUpScenarioNewLionsList();
        when(africanLionRespository.findAll()).thenReturn(africanLionList);

        try {
            africanLionService.createLion(outStandardsAfricanLion);
            fail();
        } catch (AfricanLionException africanLionException){
            verify(africanLionRespository, times(0)).save(outStandardsAfricanLion);
            assertEquals(africanLionException.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(africanLionException.getError().getCode(), AfricanLionErrorCode.CODE_05);
            assertEquals(africanLionException.getError().getMessage(), AfricanLionErrorCode.CODE_05.getMessage());
        }
    }


    @Test
    public void testGetLion(){
        ArrayList<AfricanLion> currentAfricanLionList = setUpScenarioNewLionsList();
        AfricanLion requestedLion = currentAfricanLionList.get(0);
        String lionName = requestedLion.getName();

        when(africanLionRespository.findAll()).thenReturn(currentAfricanLionList);
        AfricanLionQueryResponse africanLion = africanLionService.getLion(lionName);
        assertEquals(africanLion.getRequestedLion(), requestedLion);
        verify(africanLionRespository, times(1)).findAll();
    }

    @Test
    public void testGetLionNonSaved(){
        ArrayList<AfricanLion> currentAfricanLionList = setUpScenarioNewLionsList();
        String lionName = "nonSavedLion";

        when(africanLionRespository.findAll()).thenReturn(currentAfricanLionList);

        try {
            africanLionService.getLion(lionName);
        }catch (AfricanLionException africanLionException){
            verify(africanLionRespository, times(0)).findById(any());
            assertEquals(africanLionException.getHttpStatus(), HttpStatus.NOT_FOUND);
            assertEquals(africanLionException.getError().getCode(), AfricanLionErrorCode.CODE_01);
            assertEquals(africanLionException.getError().getMessage(), AfricanLionErrorCode.CODE_01.getMessage());
        }
    }

    @Test
    public void testGetLions(){
        ArrayList<AfricanLion> currentAfricanLionList = setUpScenarioNewLionsList();
        when(africanLionRespository.findAll()).thenReturn(currentAfricanLionList);
        africanLionService.getLions();
        verify(africanLionRespository,times(1)).findAll();
    }

}
