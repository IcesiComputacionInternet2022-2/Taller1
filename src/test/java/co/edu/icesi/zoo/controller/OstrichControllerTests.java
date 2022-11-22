package co.edu.icesi.zoo.controller;

import co.edu.icesi.zoo.constant.OstrichErrorCode;
import co.edu.icesi.zoo.dto.OstrichDTO;
import co.edu.icesi.zoo.error.exception.OstrichException;
import co.edu.icesi.zoo.mapper.OstrichMapper;
import co.edu.icesi.zoo.mapper.OstrichMapperImpl;
import co.edu.icesi.zoo.service.OstrichService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class OstrichControllerTests {

    private final static UUID ID = UUID.fromString("94cd0d26-79b6-44ef-acc7-58792bd9b3a6");
    private final static String NAME = "Medium Ostrich";
    private final static int GENDER = 1;
    private final static int AGE = 35;
    private final static float WEIGHT = 65.5f;
    private final static float HEIGHT = 1.75f;
    private final static LocalDateTime ARRIVALDATE = LocalDateTime.parse("2022-10-15T12:00:00");

    private OstrichController ostrichController;
    private OstrichService ostrichService;
    private OstrichMapper ostrichMapper;

    @BeforeEach
    public void init(){
        ostrichMapper = new OstrichMapperImpl();
        ostrichService = mock(OstrichService.class);
        ostrichController = new OstrichController(ostrichMapper, ostrichService);
    }

    @Test
    public void testGetOstrichById() {
        OstrichDTO baseOstrich = baseOstrich();
        when(ostrichService.getOstrichById(baseOstrich.getId())).thenReturn(ostrichMapper.fromDTO(baseOstrich));
        OstrichDTO ostrichResult = ostrichController.getOstrichById(baseOstrich.getId());
        verify(ostrichService, times(1)).getOstrichById(baseOstrich.getId());
        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.getId(), ID);
        assertEquals(ostrichResult.getName(), NAME);
        assertEquals(ostrichResult.getGender(), GENDER);
        assertEquals(ostrichResult.getAge(), AGE);
        assertEquals(ostrichResult.getWeight(), WEIGHT);
        assertEquals(ostrichResult.getHeight(), HEIGHT);
        assertEquals(ostrichResult.getArrivalDate(), ARRIVALDATE);
    }

    @Test
    public void testGetOstrichByName() {
        OstrichDTO baseOstrich = baseOstrich();
        when(ostrichService.getOstrichByName(baseOstrich.getName())).thenReturn(List.of(ostrichMapper.fromDTO(baseOstrich)));
        List<OstrichDTO> ostrichResult = ostrichController.getOstrichByName(baseOstrich.getName());
        verify(ostrichService, times(1)).getOstrichByName(baseOstrich.getName());
        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.get(0).getId(), ID);
        assertEquals(ostrichResult.get(0).getName(), NAME);
        assertEquals(ostrichResult.get(0).getGender(), GENDER);
        assertEquals(ostrichResult.get(0).getAge(), AGE);
        assertEquals(ostrichResult.get(0).getWeight(), WEIGHT);
        assertEquals(ostrichResult.get(0).getHeight(), HEIGHT);
        assertEquals(ostrichResult.get(0).getArrivalDate(), ARRIVALDATE);
    }

    @Test
    public void testGetOstriches() {
        OstrichDTO baseOstrich = baseOstrich();
        when(ostrichService.getOstriches()).thenReturn(List.of(ostrichMapper.fromDTO(baseOstrich)));
        List<OstrichDTO> ostrichResult = ostrichController.getOstriches();
        verify(ostrichService, times(1)).getOstriches();
        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.get(0).getId(), ID);
        assertEquals(ostrichResult.get(0).getName(), NAME);
        assertEquals(ostrichResult.get(0).getGender(), GENDER);
        assertEquals(ostrichResult.get(0).getAge(), AGE);
        assertEquals(ostrichResult.get(0).getWeight(), WEIGHT);
        assertEquals(ostrichResult.get(0).getHeight(), HEIGHT);
        assertEquals(ostrichResult.get(0).getArrivalDate(), ARRIVALDATE);
    }

    @Test
    public void testCreateOstrich() {
        OstrichDTO baseOstrich = baseOstrich();
        when(ostrichService.createOstrich(any())).thenReturn(ostrichMapper.fromDTO(baseOstrich));
        OstrichDTO ostrichResult = ostrichController.createOstrich(baseOstrich);
        verify(ostrichService, times(1)).createOstrich(any());
        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.getId(), ID);
        assertEquals(ostrichResult.getName(), NAME);
        assertEquals(ostrichResult.getGender(), GENDER);
        assertEquals(ostrichResult.getAge(), AGE);
        assertEquals(ostrichResult.getWeight(), WEIGHT);
        assertEquals(ostrichResult.getHeight(), HEIGHT);
        assertEquals(ostrichResult.getArrivalDate(), ARRIVALDATE);
    }

    @Test
    public void testCreateOstrichWithInvalidNameLength() {
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Smaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaal Ostrich");
        try {
            ostrichController.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichService, times(0)).createOstrich(any());
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_02.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_02.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidName() {
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setName("Ostrich 1");
        try {
            ostrichController.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichService, times(0)).createOstrich(any());
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_03.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_03.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidArrivalDate() {
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setArrivalDate(LocalDateTime.now().plusDays(1));
        try {
            ostrichController.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichService, times(0)).createOstrich(any());
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_04.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_04.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidMinWeight() {
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setWeight(50.5f);
        try {
            ostrichController.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichService, times(0)).createOstrich(any());
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_05.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_05.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidMaxWeight() {
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setWeight(150.5f);
        try {
            ostrichController.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichService, times(0)).createOstrich(any());
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_05.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_05.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidMinAge() {
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setAge(-1);
        try {
            ostrichController.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichService, times(0)).createOstrich(any());
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_06.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_06.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidMaxAge() {
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setAge(85);
        try {
            ostrichController.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichService, times(0)).createOstrich(any());
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_06.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_06.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidMinHeight() {
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setHeight(1.5f);
        try {
            ostrichController.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichService, times(0)).createOstrich(any());
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_07.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_07.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidMaxHeight() {
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setHeight(3.5f);
        try {
            ostrichController.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichService, times(0)).createOstrich(any());
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_07.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_07.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidGender() {
        OstrichDTO baseOstrich = baseOstrich();
        baseOstrich.setGender(2);
        try {
            ostrichController.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichService, times(0)).createOstrich(any());
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_08.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_08.getMessage());
        }
    }

    private OstrichDTO baseOstrich() {
        return OstrichDTO.builder()
                .id(ID)
                .name(NAME)
                .gender(GENDER)
                .age(AGE)
                .weight(WEIGHT)
                .height(HEIGHT)
                .arrivalDate(ARRIVALDATE)
                .build();
    }

}
