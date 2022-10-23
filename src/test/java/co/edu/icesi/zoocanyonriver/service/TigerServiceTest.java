package co.edu.icesi.zoocanyonriver.service;

import co.edu.icesi.zoocanyonriver.constants.CodesError;
import co.edu.icesi.zoocanyonriver.dto.TigerDTO;
import co.edu.icesi.zoocanyonriver.dto.TigerResponseDTO;
import co.edu.icesi.zoocanyonriver.error.exception.TigerDemoException;
import co.edu.icesi.zoocanyonriver.mapper.TigerMapper;
import co.edu.icesi.zoocanyonriver.mapper.TigerMapperImpl;
import co.edu.icesi.zoocanyonriver.model.Tiger;
import co.edu.icesi.zoocanyonriver.repository.TigerRepository;
import co.edu.icesi.zoocanyonriver.service.impl.TigerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class TigerServiceTest {

    private TigerRepository tigerRepository;
    private TigerService tigerService;
    private TigerMapper tigerMapper;

    @BeforeEach
    public void init() {
        tigerRepository = mock(TigerRepository.class);
        tigerMapper = new TigerMapperImpl();
        tigerService = new TigerServiceImpl(tigerRepository, tigerMapper);
    }

    public List<Tiger> scenary1() {
        UUID uuid1 = UUID.fromString("c30f50a0-b01b-4770-b228-12eb0603506e");
        UUID uuid2 = UUID.fromString("98d0c8af-3e20-4be9-8158-fc74c7b4d32d");
        UUID uuid3 = UUID.fromString("999da34f-9e21-4392-b224-6185ee90ae40");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Tiger tiger1 = new Tiger(uuid1, null, null, "Sanson", "Male", 200, "5", "130", LocalDateTime.parse("2021-12-13 15:15:00", formatter));
        Tiger tiger2 = new Tiger(uuid2, null, null, "Nala", "Female", 180, "4", "120", LocalDateTime.parse("2020-01-15 02:50:30", formatter));
        Tiger tiger3 = new Tiger(uuid3, "98d0c8af-3e20-4be9-8158-fc74c7b4d32d", "c30f50a0-b01b-4770-b228-12eb0603506e", "Alex", "Male", 180, "2", "110", LocalDateTime.now());
        List<Tiger> tigers = new ArrayList<Tiger>();
        tigers.add(tiger1);
        tigers.add(tiger2);
        tigers.add(tiger3);
        return tigers;
    }

    @Test
    public void getTigerWhitNameTest() {
        List<Tiger> tigers = scenary1();
        TigerResponseDTO expectedTiger = tigerMapper.fromTigerToTigerResponseDTO(tigers.get(1),null,null);

        when(tigerRepository.findAll()).thenReturn(tigers);

        TigerResponseDTO searchTiger = tigerService.getTiger("Nala");
        assertEquals(expectedTiger, searchTiger);
    }

    @Test
    public void getTigerWhitNameTestWrong() {
        List<Tiger> tigers = scenary1();

        when(tigerRepository.findAll()).thenReturn(tigers);

        try{
            tigerService.getTiger("javier");
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_03.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void getTigerWhitIDTest() {
        List<Tiger> tigers = scenary1();
        Tiger expectedTiger = tigers.get(1);

        when(tigerRepository.findById(UUID.fromString("c30f50a0-b01b-4770-b228-12eb0603506e"))).thenReturn(Optional.ofNullable(tigers.get(1)));
        Tiger searchTiger = tigerService.getTiger(UUID.fromString("c30f50a0-b01b-4770-b228-12eb0603506e"));
        assertEquals(expectedTiger, searchTiger);
    }

    @Test
    public void getTigerWhitIDTestWrong() {
        List<Tiger> tigers = scenary1();

        when(tigerRepository.findAll()).thenReturn(tigers);

        try{
            tigerService.getTiger(UUID.fromString("d2de769a-336a-42fa-b3ba-3f34222b667c"));
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_04.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void getTigerDTOParentsTest(){
        UUID uuid0 = UUID.fromString("c30f50a0-b01b-4770-b228-12eb0603506e");
        UUID uuid1 = UUID.fromString("98d0c8af-3e20-4be9-8158-fc74c7b4d32d");
        List<Tiger> tigers = scenary1();
        TigerResponseDTO expectedTiger = tigerMapper.fromTigerToTigerResponseDTO(tigers.get(2),tigers.get(1),tigers.get(0));
        TigerDTO expectedFather = expectedTiger.getFather();
        TigerDTO expectedMother = expectedTiger.getMother();

        when(tigerRepository.findAll()).thenReturn(tigers);
        when(tigerRepository.findById(uuid0)).thenReturn(Optional.ofNullable(tigers.get(0)));
        when(tigerRepository.findById(uuid1)).thenReturn(Optional.ofNullable(tigers.get(1)));

        TigerResponseDTO searchTiger = tigerService.getTiger("Alex");
        assertEquals(expectedFather, searchTiger.getFather());
        assertEquals(expectedMother, searchTiger.getMother());
    }

    @Test
    public void createTigerTest(){
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        Tiger tiger = new Tiger(uuid, null, null, "Xena", "Male", 300, "10", "120", LocalDateTime.now());

        tigerService.createTiger(tiger);
        verify(tigerRepository, times(1)).save(tiger);
    }

    @Test
    public void verifyNameRepeatTest(){
        List<Tiger> tigers = scenary1();
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        Tiger tiger = new Tiger(uuid, null, null, "Alex", "Male", 300, "10", "120", LocalDateTime.now());

        when(tigerRepository.findAll()).thenReturn(tigers);

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_12.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyParentMotherExistenceTest(){
        List<Tiger> tigers = scenary1();
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        String motherFalse = "34379fd5-ce8b-44d7-982f-d4e3b3a5693a";
        Tiger tiger = new Tiger(uuid, motherFalse, null, "Xena", "Male", 300, "10", "120", LocalDateTime.now());

        when(tigerRepository.findAll()).thenReturn(tigers);
        when(tigerRepository.findById(UUID.fromString(motherFalse))).thenReturn(Optional.empty());

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_13.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyParentMotherGenderTest(){
        List<Tiger> tigers = scenary1();
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        String mother = "c30f50a0-b01b-4770-b228-12eb0603506e";
        Tiger tiger = new Tiger(uuid, mother, null, "Xena", "Male", 300, "10", "120", LocalDateTime.now());

        when(tigerRepository.findAll()).thenReturn(tigers);
        when(tigerRepository.findById(UUID.fromString(mother))).thenReturn(Optional.ofNullable(tigers.get(0)));

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_15.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyParentFatherExistenceTest(){
        List<Tiger> tigers = scenary1();
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        String fatherFalse = "1664715c-313b-4817-aa83-45472dab4ac4";
        Tiger tiger = new Tiger(uuid, null, fatherFalse , "Xena", "Male", 300, "10", "120", LocalDateTime.now());

        when(tigerRepository.findAll()).thenReturn(tigers);
        when(tigerRepository.findById(UUID.fromString(fatherFalse))).thenReturn(Optional.empty());

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_14.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyParentFatherGenderTest(){
        List<Tiger> tigers = scenary1();
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        String father = "98d0c8af-3e20-4be9-8158-fc74c7b4d32d";
        Tiger tiger = new Tiger(uuid, null, father, "Xena", "Male", 300, "10", "120", LocalDateTime.now());

        when(tigerRepository.findAll()).thenReturn(tigers);
        when(tigerRepository.findById(UUID.fromString(father))).thenReturn(Optional.ofNullable(tigers.get(1)));

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_16.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyHeightTest(){
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        Tiger tiger = new Tiger(uuid, null, null, "Xena", "Male", 300, "10", "150", LocalDateTime.now());

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_09.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyAgeTest(){
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        Tiger tiger = new Tiger(uuid, null, null, "Xena", "Male", 300, "27", "120", LocalDateTime.now());

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_08.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyGender(){
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        Tiger tiger = new Tiger(uuid, null, null, "Xena", "Person", 300, "10", "100", LocalDateTime.now());

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_07.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyWeightMaleTest(){
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        Tiger tiger = new Tiger(uuid, null, null, "Xena", "Male", 311, "10", "120", LocalDateTime.now());

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_05.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyWeightFemaleTest(){
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        Tiger tiger = new Tiger(uuid, null, null, "Xena", "Female", 190, "10", "120", LocalDateTime.now());

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_06.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void verifyDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        UUID uuid = UUID.fromString("a65e4f66-55ee-45d4-9a07-8de382cbfd33");
        Tiger tiger = new Tiger(uuid, null, null, "Xena", "Male", 300, "10", "120", LocalDateTime.parse("2022-12-13 15:15:00", formatter));

        try{
            tigerService.createTiger(tiger);
            fail();
        }catch (TigerDemoException error){
            assertEquals(CodesError.CODE_11.getCode(), error.getError().getCode());
        }
    }

    @Test
    public void getTigersTest(){
        tigerService.getTigers();
        verify(tigerRepository, times(1)).findAll();
    }

}
