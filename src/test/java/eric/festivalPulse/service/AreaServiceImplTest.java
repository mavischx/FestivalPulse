package eric.festivalPulse.service;

import eric.festivalPulse.model.FestivalArea;
import eric.festivalPulse.repository.AreaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AreaServiceImplTest {

    @Mock
    private AreaRepository areaRepository;

    @InjectMocks
    private AreaServiceImpl areaService;

    private FestivalArea testArea;

    @BeforeEach
    void setUp() {
        testArea = new FestivalArea(null, "Main Stage", "The main stage", "STAGE");
    }

    @Test
    void createArea_savesAndReturnsArea() {
        when(areaRepository.save(any(FestivalArea.class))).thenAnswer(invocation -> {
            FestivalArea a = invocation.getArgument(0);
            a.setId(1L);
            return a;
        });

        FestivalArea result = areaService.createArea(testArea);

        assertNotNull(result.getId());
        assertEquals("Main Stage", result.getName());
        verify(areaRepository).save(testArea);
    }

    @Test
    void getAreaById_existing_returnsArea() {
        testArea.setId(1L);
        when(areaRepository.findById(1L)).thenReturn(Optional.of(testArea));

        FestivalArea result = areaService.getAreaById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Main Stage", result.getName());
        verify(areaRepository).findById(1L);
    }

    @Test
    void getAreaById_notFound_throwsRuntimeException() {
        when(areaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> areaService.getAreaById(99L));

        assertTrue(ex.getMessage().contains("Area not found"));
        verify(areaRepository).findById(99L);
    }

    @Test
    void getAllAreas_delegatesToRepository() {
        FestivalArea area1 = new FestivalArea(1L, "Main Stage", "The main stage", "STAGE");
        FestivalArea area2 = new FestivalArea(2L, "Food Court", "Food area", "FOOD");
        when(areaRepository.findAll()).thenReturn(List.of(area1, area2));

        List<FestivalArea> result = areaService.getAllAreas();

        assertEquals(2, result.size());
        verify(areaRepository).findAll();
    }
}
