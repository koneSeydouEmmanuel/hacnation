package com.hacnation.fileattente.unit;

import com.hacnation.common.dto.FileAttenteDto;
import com.hacnation.common.enums.StatutFileAttente;
import com.hacnation.fileattente.application.service.CheckInUseCase;
import com.hacnation.fileattente.domain.model.FileAttente;
import com.hacnation.fileattente.domain.port.FileAttenteRepositoryPort;
import com.hacnation.fileattente.domain.port.QueueEventPublisherPort;
import com.hacnation.fileattente.domain.port.RedisQueuePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInUseCaseTest {

    @Mock
    private FileAttenteRepositoryPort repository;

    @Mock
    private RedisQueuePort redisQueue;

    @Mock
    private QueueEventPublisherPort eventPublisher;

    @InjectMocks
    private CheckInUseCase checkInUseCase;

    private FileAttente savedEntry;

    @BeforeEach
    void setUp() {
        savedEntry = new FileAttente();
        savedEntry.setId("entry-1");
        savedEntry.setPatientId("patient-1");
        savedEntry.setRdvId("rdv-1");
        savedEntry.setService("CONSULTATION");
        savedEntry.setPatientNom("Doe");
        savedEntry.setPatientPrenom("John");
        savedEntry.setPosition(1);
        savedEntry.setStatut(StatutFileAttente.EN_ATTENTE);
        savedEntry.setTempsEstime(30);
    }

    @Test
    void execute_shouldCheckInPatient() {
        when(repository.countByServiceAndStatut(eq("CONSULTATION"), eq(StatutFileAttente.EN_ATTENTE)))
                .thenReturn(0);
        when(repository.save(any(FileAttente.class))).thenReturn(savedEntry);

        FileAttenteDto result = checkInUseCase.execute("patient-1", "rdv-1", "CONSULTATION", "Doe", "John");

        assertNotNull(result);
        assertEquals("patient-1", result.getPatientId());
        assertEquals(1, result.getPosition());
        verify(eventPublisher).publishCheckIn(eq("patient-1"), eq("rdv-1"), eq("CONSULTATION"), eq(1));
        verify(redisQueue).setPosition(eq("CONSULTATION"), eq("patient-1"), eq(1));
    }

    @Test
    void getFileAttente_shouldReturnList() {
        when(repository.findByServiceAndStatutOrderByPositionAsc(eq("CONSULTATION"), eq(StatutFileAttente.EN_ATTENTE)))
                .thenReturn(List.of(savedEntry));

        List<FileAttenteDto> results = checkInUseCase.getFileAttente("CONSULTATION");

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void getPositionInfo_shouldReturnPositionData() {
        savedEntry.setPosition(3);
        when(repository.findByPatientIdAndStatut(eq("patient-1"), eq(StatutFileAttente.EN_ATTENTE)))
                .thenReturn(Optional.of(savedEntry));

        Map<String, Object> info = checkInUseCase.getPositionInfo("patient-1");

        assertNotNull(info);
        assertEquals("patient-1", info.get("patientId"));
        assertEquals(3, info.get("position"));
        assertEquals(2, info.get("personnesDevant"));
    }
}
