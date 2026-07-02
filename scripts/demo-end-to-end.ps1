$ErrorActionPreference = "Stop"
$BASE_URL = "http://localhost:8080"

function Invoke-HACRequest {
    param(
        [string]$Method = "GET",
        [string]$Path,
        [object]$Body = $null,
        [string]$ContentType = "application/json"
    )

    $headers = @{}
    if ($script:TOKEN) {
        $headers["Authorization"] = "Bearer $script:TOKEN"
    }

    $params = @{
        Method      = $Method
        Uri         = "$BASE_URL$Path"
        Headers     = $headers
        ContentType = $ContentType
    }

    if ($Body) {
        $params["Body"] = ($Body | ConvertTo-Json -Depth 10 -Compress)
    }

    try {
        $response = Invoke-RestMethod @params
        return $response
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        $reader.Close()
        Write-Host "ERROR $Method $Path ($statusCode): $responseBody" -ForegroundColor Red
        throw
    }
}

function Invoke-HACLogin {
    param(
        [string]$Username = "medecin1",
        [string]$Password = "password"
    )

    Write-Host "`n=== Step 1: Login ===" -ForegroundColor Cyan

    $loginBody = @{
        grant_type = "password"
        client_id  = "hacnation-backend"
        username   = $Username
        password   = $Password
    }

    $tokenResponse = Invoke-RestMethod -Method POST -Uri "http://localhost:8080/realms/hacnation/protocol/openid-connect/token" `
        -ContentType "application/x-www-form-urlencoded" -Body $loginBody

    $script:TOKEN = $tokenResponse.access_token
    Write-Host "Logged in as $Username" -ForegroundColor Green
    return $script:TOKEN
}

function Invoke-HACCreatePatient {
    Write-Host "`n=== Step 2: Create Patient ===" -ForegroundColor Cyan

    $patientBody = @{
        nom             = "DIALLO"
        prenom          = "Fatoumata"
        dateNaissance   = "1995-04-15"
        sexe            = "FEMININ"
        adresse         = "Commune IV, Bamako"
        telephone       = "+22371234568"
        email           = "fatoumata.diallo@example.ml"
        profession      = "Enseignante"
        groupeSanguin   = "O+"
        statutMatrimonial = "MARIEE"
        personneAContacter = "Moussa DIALLO"
        telephoneContact = "+22398765432"
        antecedentMedical = "Diabete de type 2"
    }

    $patient = Invoke-HACRequest -Method POST -Path "/api/patients" -Body $patientBody
    $script:PATIENT_ID = $patient.id
    $script:PATIENT_MATRICULE = $patient.matricule
    Write-Host "Patient created: $($patient.nom) $($patient.prenom) (ID: $($patient.id), Matricule: $($patient.matricule))" -ForegroundColor Green
    return $patient
}

function Invoke-HACCreateRDV {
    Write-Host "`n=== Step 3: Create RDV with QR Code ===" -ForegroundColor Cyan

    $rdvBody = @{
        patientId    = $script:PATIENT_ID
        dateHeureRdv = (Get-Date).AddDays(1).ToString("yyyy-MM-ddTHH:mm:ss")
        motif        = "Consultation de suivi diabete"
        typeRdv      = "CONSULTATION"
        notes        = "Apporter le carnet de sante"
    }

    $rdv = Invoke-HACRequest -Method POST -Path "/api/rdvs" -Body $rdvBody
    $script:RDV_ID = $rdv.id
    $script:QR_CODE = $rdv.qrCode
    Write-Host "RDV created (ID: $($rdv.id))" -ForegroundColor Green
    Write-Host "QR Code: $($rdv.qrCode)" -ForegroundColor Green
    return $rdv
}

function Invoke-HACCheckInQR {
    Write-Host "`n=== Step 4: Check-in via QR ===" -ForegroundColor Cyan

    $checkinBody = @{
        qrCode = $script:QR_CODE
    }

    $checkin = Invoke-HACRequest -Method POST -Path "/api/fileattente/checkin-qr" -Body $checkinBody
    $script:TICKET_ID = $checkin.ticketId
    $script:TICKET_NUM = $checkin.numeroTicket
    Write-Host "Checked in (Ticket: $($checkin.numeroTicket))" -ForegroundColor Green
    return $checkin
}

function Invoke-HACGetQueuePosition {
    Write-Host "`n=== Step 5: Get Queue Position ===" -ForegroundColor Cyan

    $queue = Invoke-HACRequest -Method GET -Path "/api/fileattente/queue?serviceId=CONSULTATION"
    Write-Host "Queue position for ticket $script:TICKET_NUM retrieved" -ForegroundColor Green
    return $queue
}

function Invoke-HACCallNextPatient {
    Write-Host "`n=== Step 6: Call Next Patient ===" -ForegroundColor Cyan

    $callBody = @{
        serviceId = "CONSULTATION"
        guichetId = "G001"
    }

    $called = Invoke-HACRequest -Method POST -Path "/api/fileattente/call-next" -Body $callBody
    $script:CALLED_TICKET = $called.ticketId
    Write-Host "Called patient (Ticket: $($called.numeroTicket))" -ForegroundColor Green
    return $called
}

function Invoke-HACCreateConsultation {
    Write-Host "`n=== Step 7: Create Consultation ===" -ForegroundColor Cyan

    $consultBody = @{
        patientId = $script:PATIENT_ID
        ticketId  = $script:CALLED_TICKET
        poids     = 72.5
        taille    = 168
        pressionArterielle = "135/85"
        temperature = 37.2
        frequenceCardiaque = 78
        saturationOxygene = 98
        glycemie  = 1.45
        motif      = "Suivi diabete de type 2"
        diagnosticPrincipal = "Diabete de type 2 desequilibre"
        diagnosticSecondaire = "HTA grade 1"
        notes      = "Glycemie elevee, adaptation therapeutique necessaire"
    }

    $consult = Invoke-HACRequest -Method POST -Path "/api/consultations" -Body $consultBody
    $script:CONSULT_ID = $consult.id
    Write-Host "Consultation created (ID: $($consult.id))" -ForegroundColor Green
    return $consult
}

function Invoke-HACPrescribeExam {
    Write-Host "`n=== Step 8: Prescribe Exam (Lab) ===" -ForegroundColor Cyan

    $examBody = @{
        consultationId = $script:CONSULT_ID
        patientId      = $script:PATIENT_ID
        typeAnalyse    = "BIOCHIMIE"
        examens = @(
            @{ code = "GLYC"; libelle = "Glycemie a jeun"; urgence = $false },
            @{ code = "HBA1C"; libelle = "Hemoglobine glyquee"; urgence = $false },
            @{ code = "CREAT"; libelle = "Creatininemie"; urgence = $false }
        )
        notes = "Controle trimestriel diabete"
    }

    $exam = Invoke-HACRequest -Method POST -Path "/api/prescriptions/examens" -Body $examBody
    $script:EXAM_ID = $exam.id
    Write-Host "Exam prescribed (ID: $($exam.id))" -ForegroundColor Green
    return $exam
}

function Invoke-HACPrescribeMedication {
    Write-Host "`n=== Step 9: Prescribe Medication ===" -ForegroundColor Cyan

    $medBody = @{
        consultationId = $script:CONSULT_ID
        patientId      = $script:PATIENT_ID
        medicaments    = @(
            @{
                code = "MET850"
                libelle = "Metformine 850mg"
                posologie = "1 cp matin et soir"
                dureeJours = 30
                quantite = 60
            },
            @{
                code = "GLIC60"
                libelle = "Gliclazide 60mg"
                posologie = "1 cp le matin"
                dureeJours = 30
                quantite = 30
            }
        )
        notes = "Renouvellement traitement antidiabetique"
    }

    $med = Invoke-HACRequest -Method POST -Path "/api/prescriptions/medicaments" -Body $medBody
    $script:MED_ID = $med.id
    Write-Host "Medication prescribed (ID: $($med.id))" -ForegroundColor Green
    return $med
}

function Invoke-HACTerminateConsultation {
    Write-Host "`n=== Step 10: Terminate Consultation ===" -ForegroundColor Cyan

    $terminateBody = @{
        statut = "TERMINEE"
        conclusion = "Patient stabilise, poursuite traitement, controle dans 3 mois"
    }

    $terminated = Invoke-HACRequest -Method PUT -Path "/api/consultations/$script:CONSULT_ID/terminate" -Body $terminateBody
    Write-Host "Consultation terminated" -ForegroundColor Green
    return $terminated
}

function Invoke-HACProcessLabResults {
    Write-Host "`n=== Step 11: Process Lab Demands and Results ===" -ForegroundColor Cyan

    $demands = Invoke-HACRequest -Method GET -Path "/api/laboratoire/demandes?statut=EN_ATTENTE"
    Write-Host "Lab demands retrieved ($($demands.Count) pending)" -ForegroundColor Green

    if ($demands -and $demands.Count -gt 0) {
        $demandeId = $demands[0].id

        $preleveBody = @{ statut = "PREL_EFFECTUE" }
        Invoke-HACRequest -Method PUT -Path "/api/laboratoire/demandes/$demandeId/statut" -Body $preleveBody

        $resultsBody = @{
            resultats = @(
                @{ codeExamen = "GLYC"; valeur = "1.52"; unite = "g/L"; valeurReference = "0.70-1.10"; statut = "ANORMAL" },
                @{ codeExamen = "HBA1C"; valeur = "8.2"; unite = "%"; valeurReference = "<6.0"; statut = "ANORMAL" },
                @{ codeExamen = "CREAT"; valeur = "9.5"; unite = "mg/L"; valeurReference = "6.0-12.0"; statut = "NORMAL" }
            )
        }
        Invoke-HACRequest -Method POST -Path "/api/laboratoire/demandes/$demandeId/resultats" -Body $resultsBody

        $validateBody = @{ statut = "VALIDEE" }
        Invoke-HACRequest -Method PUT -Path "/api/laboratoire/demandes/$demandeId/statut" -Body $validateBody

        Write-Host "Lab results entered and validated for demande $demandeId" -ForegroundColor Green
    }
}

function Invoke-HACDeliverMedication {
    Write-Host "`n=== Step 12: Get Pharmacy Orders and Deliver ===" -ForegroundColor Cyan

    $orders = Invoke-HACRequest -Method GET -Path "/api/pharmacie/commandes?statut=EN_ATTENTE"
    Write-Host "Pharmacy orders retrieved ($($orders.Count) pending)" -ForegroundColor Green

    if ($orders -and $orders.Count -gt 0) {
        $orderId = $orders[0].id

        $deliverBody = @{ statut = "LIVREE" }
        Invoke-HACRequest -Method PUT -Path "/api/pharmacie/commandes/$orderId/livrer" -Body $deliverBody
        Write-Host "Medication delivered for order $orderId" -ForegroundColor Green
    }
}

function Invoke-HACGenerateInvoice {
    Write-Host "`n=== Step 13: Generate Invoice ===" -ForegroundColor Cyan

    $invoiceBody = @{
        patientId = $script:PATIENT_ID
        consultationId = $script:CONSULT_ID
        lignesFacture = @(
            @{ codeActe = "CS-GEN"; libelle = "Consultation generale"; quantite = 1; prixUnitaire = 5000 },
            @{ codeActe = "BIOCHIM"; libelle = "Bilan biochimique"; quantite = 1; prixUnitaire = 15000 },
            @{ codeActe = "PHARMA"; libelle = "Medicaments"; quantite = 1; prixUnitaire = 8500 }
        )
        notes = "Facture consultation + examens"
    }

    $invoice = Invoke-HACRequest -Method POST -Path "/api/facturation/factures" -Body $invoiceBody
    $script:INVOICE_ID = $invoice.id
    Write-Host "Invoice generated (ID: $($invoice.id), Total: $($invoice.montantTotal))" -ForegroundColor Green
    return $invoice
}

function Invoke-HACProcessPayment {
    Write-Host "`n=== Step 14: Process Payment (Mobile Money) ===" -ForegroundColor Cyan

    $paymentBody = @{
        factureId   = $script:INVOICE_ID
        modePaiement = "MOBILE_MONEY"
        referencePaiement = "MM-$(Get-Date -Format 'yyyyMMddHHmmss')"
        montant     = 28500
    }

    $payment = Invoke-HACRequest -Method POST -Path "/api/caisse/paiements" -Body $paymentBody
    $script:PAYMENT_ID = $payment.id
    Write-Host "Payment processed (ID: $($payment.id), Mode: Mobile Money)" -ForegroundColor Green
    return $payment
}

function Invoke-HACCashRegisterSummary {
    Write-Host "`n=== Step 15: Get Cash Register Summary ===" -ForegroundColor Cyan

    $summary = Invoke-HACRequest -Method GET -Path "/api/caisse/caisse/resume"
    Write-Host "Cash register summary retrieved" -ForegroundColor Green
    return $summary
}

function Invoke-HACNotificationHistory {
    Write-Host "`n=== Step 16: Check Notification History ===" -ForegroundColor Cyan

    $notifications = Invoke-HACRequest -Method GET -Path "/api/notifications/history"
    Write-Host "Notification history retrieved ($($notifications.Count) notifications)" -ForegroundColor Green
    return $notifications
}

function Invoke-HACDashboards {
    Write-Host "`n=== Step 17: Get Dashboards ===" -ForegroundColor Cyan

    $waitingRoom = Invoke-HACRequest -Method GET -Path "/api/reporting/dashboard/salle-attente"
    Write-Host "Waiting room dashboard retrieved" -ForegroundColor Green

    $consultations = Invoke-HACRequest -Method GET -Path "/api/reporting/dashboard/consultations"
    Write-Host "Consultations dashboard retrieved" -ForegroundColor Green

    $revenue = Invoke-HACRequest -Method GET -Path "/api/reporting/dashboard/revenus"
    Write-Host "Revenue dashboard retrieved" -ForegroundColor Green

    $stocks = Invoke-HACRequest -Method GET -Path "/api/reporting/dashboard/stocks"
    Write-Host "Stocks dashboard retrieved" -ForegroundColor Green
}

function Invoke-HACParametres {
    Write-Host "`n=== Step 18: Get Parametres ===" -ForegroundColor Cyan

    $parametres = Invoke-HACRequest -Method GET -Path "/api/parametres"
    Write-Host "Parametres retrieved ($($parametres.Count) entries)" -ForegroundColor Green
    return $parametres
}

function Invoke-HACAuditTrail {
    Write-Host "`n=== Step 19: Get Audit Trail ===" -ForegroundColor Cyan

    $audit = Invoke-HACRequest -Method GET -Path "/api/audit/trail"
    Write-Host "Audit trail retrieved ($($audit.Count) entries)" -ForegroundColor Green
    return $audit
}

function Invoke-HACHealthCheck {
    Write-Host "`n=== Step 20: Health Check ===" -ForegroundColor Cyan

    $health = Invoke-RestMethod -Method GET -Uri "$BASE_URL/actuator/health"
    Write-Host "System status: $($health.status)" -ForegroundColor Green
    return $health
}

Write-Host @"
`n
==============================================
  HACNATION - Demo End-to-End (PowerShell)
==============================================
"@ -ForegroundColor Magenta

try {
    Invoke-HACLogin
    Invoke-HACCreatePatient
    Invoke-HACCreateRDV
    Invoke-HACCheckInQR
    Invoke-HACGetQueuePosition
    Invoke-HACCallNextPatient
    Invoke-HACCreateConsultation
    Invoke-HACPrescribeExam
    Invoke-HACPrescribeMedication
    Invoke-HACTerminateConsultation
    Invoke-HACProcessLabResults
    Invoke-HACDeliverMedication
    Invoke-HACGenerateInvoice
    Invoke-HACProcessPayment
    Invoke-HACCashRegisterSummary
    Invoke-HACNotificationHistory
    Invoke-HACDashboards
    Invoke-HACParametres
    Invoke-HACAuditTrail
    Invoke-HACHealthCheck

    Write-Host "`n==============================================" -ForegroundColor Magenta
    Write-Host "  Demo completed successfully!" -ForegroundColor Green
    Write-Host "==============================================" -ForegroundColor Magenta
} catch {
    Write-Host "`n==============================================" -ForegroundColor Magenta
    Write-Host "  Demo failed!" -ForegroundColor Red
    Write-Host "  Error: $_" -ForegroundColor Red
    Write-Host "==============================================" -ForegroundColor Magenta
    exit 1
}
