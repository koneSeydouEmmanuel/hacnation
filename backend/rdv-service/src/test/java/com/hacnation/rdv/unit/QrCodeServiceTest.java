package com.hacnation.rdv.unit;

import com.hacnation.rdv.application.service.QrCodeService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QrCodeServiceTest {

    private final QrCodeService qrCodeService = new QrCodeService();

    @Test
    void generateQrCode_shouldReturnBase64String() {
        String qrCode = qrCodeService.generateQrCode("rdv-1", "patient-1");

        assertNotNull(qrCode);
        assertFalse(qrCode.isEmpty());
        assertTrue(qrCode.length() > 100);
    }

    @Test
    void generateQrCode_shouldReturnDifferentCodesForDifferentInputs() {
        String qrCode1 = qrCodeService.generateQrCode("rdv-1", "patient-1");
        String qrCode2 = qrCodeService.generateQrCode("rdv-2", "patient-2");

        assertNotEquals(qrCode1, qrCode2);
    }
}
