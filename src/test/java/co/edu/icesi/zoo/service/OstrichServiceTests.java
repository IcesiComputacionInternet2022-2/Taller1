package co.edu.icesi.zoo.service;

import co.edu.icesi.zoo.constant.OstrichErrorCode;
import co.edu.icesi.zoo.error.exception.OstrichException;
import co.edu.icesi.zoo.model.Ostrich;
import co.edu.icesi.zoo.repository.OstrichRepository;
import co.edu.icesi.zoo.service.impl.OstrichServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class OstrichServiceTests {

    private final static UUID ID = UUID.fromString("94cd0d26-79b6-44ef-acc7-58792bd9b3a6");
    private final static String NAME = "Medium Ostrich";
    private final static int GENDER = 1;
    private final static int AGE = 35;
    private final static float WEIGHT = 65.5f;
    private final static float HEIGHT = 1.75f;
    private final static LocalDateTime ARRIVALDATE = LocalDateTime.parse("2022-10-15T12:00:00");

    private OstrichService ostrichService;
    private OstrichRepository ostrichRepository;

    @BeforeEach
    public void init(){
        ostrichRepository = mock(OstrichRepository.class);
        ostrichService = new OstrichServiceImpl(ostrichRepository);
    }

    @Test
    public void testGetOstrichById() {
        Ostrich baseOstrich = baseOstrich();

        when(ostrichRepository.findById(baseOstrich.getId())).thenReturn(Optional.of(baseOstrich));

        Ostrich ostrichResult = ostrichService.getOstrichById(baseOstrich.getId());

        verify(ostrichRepository, times(1)).findById(baseOstrich.getId());

        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.getId(), ID);
        assertEquals(ostrichResult.getName(), NAME);
        assertEquals(ostrichResult.getGender(), GENDER);
        assertEquals(ostrichResult.getAge(), AGE);
        assertEquals(ostrichResult.getWeight(), WEIGHT);
        assertEquals(ostrichResult.getHeight(), HEIGHT);
        assertEquals(ostrichResult.getArrivalDate(), ARRIVALDATE);
        assertNull(ostrichResult.getFatherId());
        assertNull(ostrichResult.getMotherId());
    }

    @Test
    public void testGetOstrichByIdNotFound() {
        Ostrich baseOstrich = baseOstrich();
        try {
            when(ostrichRepository.findById(baseOstrich.getId())).thenReturn(Optional.empty());
            ostrichService.getOstrichById(baseOstrich.getId());
            fail();
        } catch (OstrichException e) {
            verify(ostrichRepository, times(1)).findById(baseOstrich.getId());
            assertEquals(e.getHttpStatus(), HttpStatus.NOT_FOUND);
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_13.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_13.getMessage());
        }
    }

    @Test
    public void testGetOstrichByName() {
        Ostrich baseOstrich = baseOstrich();

        when(ostrichRepository.findByName(baseOstrich.getName())).thenReturn(Optional.of(baseOstrich));

        List<Ostrich> ostrichResult = ostrichService.getOstrichByName(baseOstrich.getName());

        verify(ostrichRepository, times(1)).findByName(baseOstrich.getName());

        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.size(), 1);

        assertNotNull(ostrichResult.get(0));
        assertEquals(ostrichResult.get(0).getId(), ID);
        assertEquals(ostrichResult.get(0).getName(), NAME);
        assertEquals(ostrichResult.get(0).getGender(), GENDER);
        assertEquals(ostrichResult.get(0).getAge(), AGE);
        assertEquals(ostrichResult.get(0).getWeight(), WEIGHT);
        assertEquals(ostrichResult.get(0).getHeight(), HEIGHT);
        assertEquals(ostrichResult.get(0).getArrivalDate(), ARRIVALDATE);
        assertNull(ostrichResult.get(0).getFatherId());
        assertNull(ostrichResult.get(0).getMotherId());
    }

    @Test
    public void testGetOstrichByNameNotFound() {
        Ostrich baseOstrich = baseOstrich();
        try {
            when(ostrichRepository.findByName(baseOstrich.getName())).thenReturn(Optional.empty());
            ostrichService.getOstrichByName(baseOstrich.getName());
            fail();
        } catch (OstrichException e) {
            verify(ostrichRepository, times(1)).findByName(baseOstrich.getName());
            assertEquals(e.getHttpStatus(), HttpStatus.NOT_FOUND);
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_13.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_13.getMessage());
        }
    }

    @Test
    public void testGetOstrichByNameWithFather() {
        Ostrich fatherOstrich = baseOstrich();
        fatherOstrich.setId(UUID.randomUUID());
        fatherOstrich.setName("Father Ostrich");

        Ostrich baseOstrich = baseOstrich();
        baseOstrich.setFatherId(fatherOstrich.getId());

        when(ostrichRepository.findByName(baseOstrich.getName())).thenReturn(Optional.of(baseOstrich));
        when(ostrichRepository.findById(fatherOstrich.getId())).thenReturn(Optional.of(fatherOstrich));

        List<Ostrich> ostrichResult = ostrichService.getOstrichByName(baseOstrich.getName());

        verify(ostrichRepository, times(1)).findByName(baseOstrich.getName());
        verify(ostrichRepository, times(1)).findById(fatherOstrich.getId());

        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.size(), 2);

        assertNotNull(ostrichResult.get(0));
        assertEquals(ostrichResult.get(0).getId(), ID);
        assertEquals(ostrichResult.get(0).getName(), NAME);
        assertEquals(ostrichResult.get(0).getGender(), GENDER);
        assertEquals(ostrichResult.get(0).getAge(), AGE);
        assertEquals(ostrichResult.get(0).getWeight(), WEIGHT);
        assertEquals(ostrichResult.get(0).getHeight(), HEIGHT);
        assertEquals(ostrichResult.get(0).getArrivalDate(), ARRIVALDATE);
        assertEquals(ostrichResult.get(0).getFatherId(), fatherOstrich.getId());
        assertNull(ostrichResult.get(0).getMotherId());

        assertNotNull(ostrichResult.get(1));
        assertEquals(ostrichResult.get(1).getId(), fatherOstrich.getId());
        assertEquals(ostrichResult.get(1).getName(), fatherOstrich.getName());
        assertEquals(ostrichResult.get(1).getGender(), fatherOstrich.getGender());
        assertEquals(ostrichResult.get(1).getAge(), fatherOstrich.getAge());
        assertEquals(ostrichResult.get(1).getWeight(), fatherOstrich.getWeight());
        assertEquals(ostrichResult.get(1).getHeight(), fatherOstrich.getHeight());
        assertEquals(ostrichResult.get(1).getArrivalDate(), fatherOstrich.getArrivalDate());
        assertNull(ostrichResult.get(1).getFatherId());
        assertNull(ostrichResult.get(1).getMotherId());
    }

    @Test
    public void testGetOstrichByNameWithFatherNotFound() {
        Ostrich baseOstrich = baseOstrich();
        baseOstrich.setFatherId(UUID.randomUUID());
        try {
            when(ostrichRepository.findByName(baseOstrich.getName())).thenReturn(Optional.of(baseOstrich));
            when(ostrichRepository.findById(baseOstrich.getFatherId())).thenReturn(Optional.empty());
            ostrichService.getOstrichByName(baseOstrich.getName());
            fail();
        } catch (OstrichException e) {
            verify(ostrichRepository, times(1)).findByName(baseOstrich.getName());
            verify(ostrichRepository, times(1)).findById(baseOstrich.getFatherId());
            assertEquals(e.getHttpStatus(), HttpStatus.NOT_FOUND);
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_09.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_09.getMessage());
        }
    }

    @Test
    public void testGetOstrichByNameWithMother() {
        Ostrich motherOstrich = baseOstrich();
        motherOstrich.setId(UUID.randomUUID());
        motherOstrich.setName("Mother Ostrich");
        motherOstrich.setGender(0);

        Ostrich baseOstrich = baseOstrich();
        baseOstrich.setMotherId(motherOstrich.getId());

        when(ostrichRepository.findByName(baseOstrich.getName())).thenReturn(Optional.of(baseOstrich));
        when(ostrichRepository.findById(motherOstrich.getId())).thenReturn(Optional.of(motherOstrich));

        List<Ostrich> ostrichResult = ostrichService.getOstrichByName(baseOstrich.getName());

        verify(ostrichRepository, times(1)).findByName(baseOstrich.getName());
        verify(ostrichRepository, times(1)).findById(motherOstrich.getId());

        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.size(), 2);

        assertNotNull(ostrichResult.get(0));
        assertEquals(ostrichResult.get(0).getId(), ID);
        assertEquals(ostrichResult.get(0).getName(), NAME);
        assertEquals(ostrichResult.get(0).getGender(), GENDER);
        assertEquals(ostrichResult.get(0).getAge(), AGE);
        assertEquals(ostrichResult.get(0).getWeight(), WEIGHT);
        assertEquals(ostrichResult.get(0).getHeight(), HEIGHT);
        assertEquals(ostrichResult.get(0).getArrivalDate(), ARRIVALDATE);
        assertNull(ostrichResult.get(0).getFatherId());
        assertEquals(ostrichResult.get(0).getMotherId(), motherOstrich.getId());

        assertNotNull(ostrichResult.get(1));
        assertEquals(ostrichResult.get(1).getId(), motherOstrich.getId());
        assertEquals(ostrichResult.get(1).getName(), motherOstrich.getName());
        assertEquals(ostrichResult.get(1).getGender(), motherOstrich.getGender());
        assertEquals(ostrichResult.get(1).getAge(), motherOstrich.getAge());
        assertEquals(ostrichResult.get(1).getWeight(), motherOstrich.getWeight());
        assertEquals(ostrichResult.get(1).getHeight(), motherOstrich.getHeight());
        assertEquals(ostrichResult.get(1).getArrivalDate(), motherOstrich.getArrivalDate());
        assertNull(ostrichResult.get(1).getFatherId());
        assertNull(ostrichResult.get(1).getMotherId());
    }

    @Test
    public void testGetOstrichByNameWithMotherNotFound() {
        Ostrich baseOstrich = baseOstrich();
        baseOstrich.setMotherId(UUID.randomUUID());
        try {
            when(ostrichRepository.findByName(baseOstrich.getName())).thenReturn(Optional.of(baseOstrich));
            when(ostrichRepository.findById(baseOstrich.getMotherId())).thenReturn(Optional.empty());
            ostrichService.getOstrichByName(baseOstrich.getName());
            fail();
        } catch (OstrichException e) {
            verify(ostrichRepository, times(1)).findByName(baseOstrich.getName());
            verify(ostrichRepository, times(1)).findById(baseOstrich.getMotherId());
            assertEquals(e.getHttpStatus(), HttpStatus.NOT_FOUND);
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_10.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_10.getMessage());
        }
    }

    @Test
    public void testGetOstrichByNameWithParents() {
        Ostrich fatherOstrich = baseOstrich();
        fatherOstrich.setId(UUID.randomUUID());
        fatherOstrich.setName("Father Ostrich");

        Ostrich motherOstrich = baseOstrich();
        motherOstrich.setId(UUID.randomUUID());
        motherOstrich.setName("Mother Ostrich");
        motherOstrich.setGender(0);

        Ostrich baseOstrich = baseOstrich();
        baseOstrich.setFatherId(fatherOstrich.getId());
        baseOstrich.setMotherId(motherOstrich.getId());

        when(ostrichRepository.findByName(baseOstrich.getName())).thenReturn(Optional.of(baseOstrich));
        when(ostrichRepository.findById(fatherOstrich.getId())).thenReturn(Optional.of(fatherOstrich));
        when(ostrichRepository.findById(motherOstrich.getId())).thenReturn(Optional.of(motherOstrich));

        List<Ostrich> ostrichResult = ostrichService.getOstrichByName(baseOstrich.getName());

        verify(ostrichRepository, times(1)).findByName(baseOstrich.getName());
        verify(ostrichRepository, times(1)).findById(fatherOstrich.getId());
        verify(ostrichRepository, times(1)).findById(motherOstrich.getId());

        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.size(), 3);

        assertNotNull(ostrichResult.get(0));
        assertEquals(ostrichResult.get(0).getId(), ID);
        assertEquals(ostrichResult.get(0).getName(), NAME);
        assertEquals(ostrichResult.get(0).getGender(), GENDER);
        assertEquals(ostrichResult.get(0).getAge(), AGE);
        assertEquals(ostrichResult.get(0).getWeight(), WEIGHT);
        assertEquals(ostrichResult.get(0).getHeight(), HEIGHT);
        assertEquals(ostrichResult.get(0).getArrivalDate(), ARRIVALDATE);
        assertEquals(ostrichResult.get(0).getFatherId(), fatherOstrich.getId());
        assertEquals(ostrichResult.get(0).getMotherId(), motherOstrich.getId());

        assertNotNull(ostrichResult.get(1));
        assertEquals(ostrichResult.get(1).getId(), fatherOstrich.getId());
        assertEquals(ostrichResult.get(1).getName(), fatherOstrich.getName());
        assertEquals(ostrichResult.get(1).getGender(), fatherOstrich.getGender());
        assertEquals(ostrichResult.get(1).getAge(), fatherOstrich.getAge());
        assertEquals(ostrichResult.get(1).getWeight(), fatherOstrich.getWeight());
        assertEquals(ostrichResult.get(1).getHeight(), fatherOstrich.getHeight());
        assertEquals(ostrichResult.get(1).getArrivalDate(), fatherOstrich.getArrivalDate());
        assertNull(ostrichResult.get(1).getFatherId());
        assertNull(ostrichResult.get(1).getMotherId());

        assertNotNull(ostrichResult.get(2));
        assertEquals(ostrichResult.get(2).getId(), motherOstrich.getId());
        assertEquals(ostrichResult.get(2).getName(), motherOstrich.getName());
        assertEquals(ostrichResult.get(2).getGender(), motherOstrich.getGender());
        assertEquals(ostrichResult.get(2).getAge(), motherOstrich.getAge());
        assertEquals(ostrichResult.get(2).getWeight(), motherOstrich.getWeight());
        assertEquals(ostrichResult.get(2).getHeight(), motherOstrich.getHeight());
        assertEquals(ostrichResult.get(2).getArrivalDate(), motherOstrich.getArrivalDate());
        assertNull(ostrichResult.get(2).getFatherId());
        assertNull(ostrichResult.get(2).getMotherId());
    }

    @Test
    public void testGetOstriches() {
        Ostrich baseOstrich = baseOstrich();

        when(ostrichRepository.findAll()).thenReturn(List.of(baseOstrich));

        List<Ostrich> ostrichResult = ostrichService.getOstriches();

        verify(ostrichRepository, times(1)).findAll();

        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.size(), 1);

        assertNotNull(ostrichResult.get(0));
        assertEquals(ostrichResult.get(0).getId(), ID);
        assertEquals(ostrichResult.get(0).getName(), NAME);
        assertEquals(ostrichResult.get(0).getGender(), GENDER);
        assertEquals(ostrichResult.get(0).getAge(), AGE);
        assertEquals(ostrichResult.get(0).getWeight(), WEIGHT);
        assertEquals(ostrichResult.get(0).getHeight(), HEIGHT);
        assertEquals(ostrichResult.get(0).getArrivalDate(), ARRIVALDATE);
        assertNull(ostrichResult.get(0).getFatherId());
        assertNull(ostrichResult.get(0).getMotherId());
    }

    @Test
    public void testCreateOstrich(){
        Ostrich baseOstrich = baseOstrich();

        when(ostrichRepository.save(baseOstrich)).thenReturn(baseOstrich);

        Ostrich ostrichResult = ostrichService.createOstrich(baseOstrich);

        verify(ostrichRepository, times(1)).save(baseOstrich);

        assertNotNull(ostrichResult);
        assertEquals(ostrichResult.getId(), ID);
        assertEquals(ostrichResult.getName(), NAME);
        assertEquals(ostrichResult.getGender(), GENDER);
        assertEquals(ostrichResult.getAge(), AGE);
        assertEquals(ostrichResult.getWeight(), WEIGHT);
        assertEquals(ostrichResult.getHeight(), HEIGHT);
        assertEquals(ostrichResult.getArrivalDate(), ARRIVALDATE);
        assertNull(ostrichResult.getFatherId());
        assertNull(ostrichResult.getMotherId());
    }

    @Test
    public void testCreateOstrichWithRepeatName() {
        Ostrich baseOstrich = baseOstrich();
        try {
            when(ostrichRepository.findByName(baseOstrich.getName())).thenReturn(Optional.of(baseOstrich));
            ostrichService.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichRepository, times(1)).findByName(baseOstrich.getName());
            verify(ostrichRepository, times(0)).save(baseOstrich);
            assertEquals(e.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_01.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_01.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithFatherNotFound() {
        Ostrich baseOstrich = baseOstrich();
        baseOstrich.setFatherId(UUID.randomUUID());
        try {
            when(ostrichRepository.findById(baseOstrich.getFatherId())).thenReturn(Optional.empty());
            ostrichService.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichRepository, times(1)).findById(baseOstrich.getFatherId());
            verify(ostrichRepository, times(0)).save(baseOstrich);
            assertEquals(e.getHttpStatus(), HttpStatus.NOT_FOUND);
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_09.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_09.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithMotherNotFound() {
        Ostrich baseOstrich = baseOstrich();
        baseOstrich.setMotherId(UUID.randomUUID());
        try {
            when(ostrichRepository.findById(baseOstrich.getMotherId())).thenReturn(Optional.empty());
            ostrichService.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichRepository, times(1)).findById(baseOstrich.getMotherId());
            verify(ostrichRepository, times(0)).save(baseOstrich);
            assertEquals(e.getHttpStatus(), HttpStatus.NOT_FOUND);
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_10.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_10.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidFatherGender() {
        Ostrich fatherOstrich = baseOstrich();
        fatherOstrich.setId(UUID.randomUUID());
        fatherOstrich.setName("Father Ostrich");
        fatherOstrich.setGender(0);

        Ostrich baseOstrich = baseOstrich();
        baseOstrich.setFatherId(fatherOstrich.getId());
        try {
            when(ostrichRepository.findById(baseOstrich.getFatherId())).thenReturn(Optional.of(fatherOstrich));
            ostrichService.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichRepository, times(3)).findById(baseOstrich.getFatherId());
            verify(ostrichRepository, times(0)).save(baseOstrich);
            assertEquals(e.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_11.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_11.getMessage());
        }
    }

    @Test
    public void testCreateOstrichWithInvalidMotherGender() {
        Ostrich motherOstrich = baseOstrich();
        motherOstrich.setId(UUID.randomUUID());
        motherOstrich.setName("Mother Ostrich");
        motherOstrich.setGender(1);

        Ostrich baseOstrich = baseOstrich();
        baseOstrich.setMotherId(motherOstrich.getId());
        try {
            when(ostrichRepository.findById(baseOstrich.getMotherId())).thenReturn(Optional.of(motherOstrich));
            ostrichService.createOstrich(baseOstrich);
            fail();
        } catch (OstrichException e) {
            verify(ostrichRepository, times(3)).findById(baseOstrich.getMotherId());
            verify(ostrichRepository, times(0)).save(baseOstrich);
            assertEquals(e.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertEquals(e.getError().getCode(), OstrichErrorCode.CODE_12.name());
            assertEquals(e.getError().getMessage(), OstrichErrorCode.CODE_12.getMessage());
        }
    }

    private Ostrich baseOstrich() {
        return Ostrich.builder()
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
