package com.hacnation.fileattente.unit;

import com.hacnation.common.dto.FileAttenteDto;
import com.hacnation.common.enums.StatutFileAttente;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.fileattente.application.service.CallNextUseCase;
import com.hacnation.fileattente.domain.model.FileAttente;
import com.hacnation.fileattente.domain.port.FileAttenteRepositoryPort;
import com.hacnation.fileattente.domain.port.QueueEventPublisherPort;
import com.hacnation.fileattente.domain.port.RedisQueuePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallNextUseCaseTest {

    @Mock
    private FileAttenteRepositoryPort repository;

    @Mock
    private RedisQueuePort redisQueue;

    @Mock
    private QueueEventPublisherPort eventPublisher;

    @InjectMocks
    private CallNextUseCase callNextUseCase;

    @Test
    @DisplayName("Should call next patient, set APPELE status, and publish event")
    void execute_shouldCallNextAndPublishEvent() {
        FileAttente waitingEntry = new FileAttente();
        waitingEntry.setId("entry-1");
        waitingEntry.setPatientId("patient-1");
        waitingEntry.setRdvId("rdv-1");
        waitingEntry.setService("CONSULTATION");
        waitingEntry.setPatientNom("Doe");
        waitingEntry.setPatientPrenom("John");
        waitingEntry.setPosition(1);
        waitingEntry.setStatut(StatutFileAttente.EN_ATTENTE);
        waitingEntry.setTempsEstime(15);

        when(repository.findByServiceAndStatutOrderByPositionAsc(
                eq("CONSULTATION"), eq(StatutFileAttente.EN_ATTENTE)))
                .thenReturn(List.of(waitingEntry));

        FileAttente updated = new FileAttente();
        updated.setId("entry-1");
        updated.setPatientId("patient-1");
        updated.setService("CONSULTATION");
        updated.setStatut(StatutFileAttente.APPELE);
        updated.setPosition(1);

        when(repository.save(any(FileAttente.class))).thenReturn(updated);

        FileAttenteDto result = callNextUseCase.execute("CONSULTATION");

        assertNotNull(result);
        assertEquals("patient-1", result.getPatientId());
        assertEquals(StatutFileAttente.APPELE, result.getStatut());
        verify(redisQueue).removePosition(eq("CONSULTATION"), eq("patient-1"));
        verify(eventPublisher).publishCallNext(eq("patient-1"), eq("CONSULTATION"));
    }

    @Test
    @DisplayName("Should throw BusinessException when queue is empty")
    void execute_shouldThrowException_whenQueueIsEmpty() {
        when(repository.findByServiceAndStatutOrderByPositionAsc(
                eq("CONSULTATION"), eq(StatutFileAttente.EN_ATTENTE)))
                .thenReturn(Collections.emptyList());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                callNextUseCase.execute("CONSULTATION"));

        assertEquals(404, exception.getStatus());
        verify(repository, never()).save(any(FileAttente.class));
        verify(eventPublisher, never()).publishCallNext(anyString(), anyString());
    }

    @Test
    @DisplayName("Should reposition remaining patients when calling next")
    void execute_shouldRepositionRemainingPatients() {
        FileAttente first = new FileAttente();
        first.setId("entry-1");
        first.setPatientId("patient-1");
        first.setService("CONSULTATION");
        first.setPosition(1);
        first.setStatut(StatutFileAttente.EN_ATTENTE);
        first.setTempsEstime(15);

        FileAttente second = new FileAttente();
        second.setId("entry-2");
        second.setPatientId("patient-2");
        second.setService("CONSULTATION");
        second.setPosition(2);
        second.setStatut(StatutFileAttente.EN_ATTENTE);
        second.setTempsEstime(30);

        FileAttente third = new FileAttente();
        third.setId("entry-3");
        third.setPatientId("patient-3");
        third.setService("CONSULTATION");
        third.setPosition(3);
        third.setStatut(StatutFileAttente.EN_ATTENTE);
        third.setTempsEstime(45);

        when(repository.findByServiceAndStatutOrderByPositionAsc(
                eq("CONSULTATION"), eq(StatutFileAttente.EN_ATTENTE)))
                .thenReturn(List.of(first, second, third));

        FileAttente updatedFirst = new FileAttente();
        updatedFirst.setId("entry-1");
        updatedFirst.setPatientId("patient-1");
        updatedFirst.setStatut(StatutFileAttente.APPELE);
        updatedFirst.setPosition(1);

        when(repository.save(first)).thenReturn(updatedFirst);
        when(repository.save(second)).thenReturn(second);
        when(repository.save(third)).thenReturn(third);

        FileAttenteDto result = callNextUseCase.execute("CONSULTATION");

        assertNotNull(result);
        assertEquals(StatutFileAttente.APPELE, result.getStatut());

        assertEquals(1, second.getPosition());
        assertEquals(15, second.getTempsEstime());
        assertEquals(2, third.getPosition());
        assertEquals(30, third.getTempsEstime());

        verify(redisQueue).removePosition(eq("CONSULTATION"), eq("patient-1"));
        verify(redisQueue).setPosition(eq("CONSULTATION"), eq("patient-2"), eq(1));
        verify(redisQueue).setPosition(eq("CONSULTATION"), eq("patient-3"), eq(2));
        verify(eventPublisher).publishCallNext(eq("patient-1"), eq("CONSULTATION"));
    }
}
