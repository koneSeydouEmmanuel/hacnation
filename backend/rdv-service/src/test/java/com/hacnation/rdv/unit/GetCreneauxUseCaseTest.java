package com.hacnation.rdv.unit;

import com.hacnation.common.enums.StatutRdv;
import com.hacnation.rdv.application.service.GetCreneauxUseCase;
import com.hacnation.rdv.domain.model.RendezVous;
import com.hacnation.rdv.domain.port.RdvRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCreneauxUseCaseTest {

    @Mock
    private RdvRepositoryPort rdvRepository;

    @InjectMocks
    private GetCreneauxUseCase getCreneauxUseCase;

    @Test
    @DisplayName("Should return available slots excluding booked hours")
    void execute_shouldReturnAvailableSlots_whenSomeBooked() {
        RendezVous bookedRdv = new RendezVous();
        bookedRdv.setId("rdv-1");
        bookedRdv.setService("CONSULTATION");
        bookedRdv.setDateHeure(LocalDateTime.of(2026, 7, 3, 10, 0));
        bookedRdv.setStatut(StatutRdv.EN_ATTENTE);

        when(rdvRepository.findByServiceAndDateHeureBetween(eq("CONSULTATION"), any(), any()))
                .thenReturn(List.of(bookedRdv));

        List<LocalTime> result = getCreneauxUseCase.execute("CONSULTATION", LocalDate.of(2026, 7, 3));

        assertNotNull(result);
        assertEquals(9, result.size());
        assertFalse(result.contains(LocalTime.of(10, 0)));
        assertTrue(result.contains(LocalTime.of(8, 0)));
        assertTrue(result.contains(LocalTime.of(9, 0)));
        assertTrue(result.contains(LocalTime.of(11, 0)));
    }

    @Test
    @DisplayName("Should return empty list when all slots are booked")
    void execute_shouldReturnEmptyList_whenAllSlotsBooked() {
        List<RendezVous> allBooked = new java.util.ArrayList<>();
        for (int hour = 8; hour < 18; hour++) {
            RendezVous rdv = new RendezVous();
            rdv.setId("rdv-" + hour);
            rdv.setService("CONSULTATION");
            rdv.setDateHeure(LocalDateTime.of(2026, 7, 3, hour, 0));
            rdv.setStatut(StatutRdv.EN_ATTENTE);
            allBooked.add(rdv);
        }

        when(rdvRepository.findByServiceAndDateHeureBetween(eq("CONSULTATION"), any(), any()))
                .thenReturn(allBooked);

        List<LocalTime> result = getCreneauxUseCase.execute("CONSULTATION", LocalDate.of(2026, 7, 3));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should not block slots for cancelled appointments")
    void execute_shouldIgnoreCancelledAppointments() {
        RendezVous cancelledRdv = new RendezVous();
        cancelledRdv.setId("rdv-1");
        cancelledRdv.setService("CONSULTATION");
        cancelledRdv.setDateHeure(LocalDateTime.of(2026, 7, 3, 10, 0));
        cancelledRdv.setStatut(StatutRdv.ANNULE);

        when(rdvRepository.findByServiceAndDateHeureBetween(eq("CONSULTATION"), any(), any()))
                .thenReturn(List.of(cancelledRdv));

        List<LocalTime> result = getCreneauxUseCase.execute("CONSULTATION", LocalDate.of(2026, 7, 3));

        assertNotNull(result);
        assertEquals(10, result.size());
        assertTrue(result.contains(LocalTime.of(10, 0)));
    }

    @Test
    @DisplayName("Should return all business hour slots when no bookings")
    void execute_shouldReturnAllSlots_whenNoBookings() {
        when(rdvRepository.findByServiceAndDateHeureBetween(eq("CONSULTATION"), any(), any()))
                .thenReturn(Collections.emptyList());

        List<LocalTime> result = getCreneauxUseCase.execute("CONSULTATION", LocalDate.of(2026, 7, 3));

        assertNotNull(result);
        assertEquals(10, result.size());
        assertEquals(LocalTime.of(8, 0), result.get(0));
        assertEquals(LocalTime.of(17, 0), result.get(9));
    }
}
