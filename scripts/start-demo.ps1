# ===========================================================
# HACNATION Hackathon Quick Start (PowerShell)
# Usage: .\scripts\start-demo.ps1 [-Full]
#   Sans -Full : coeur (12 services)
#   Avec -Full : complet (17 services)
# ===========================================================
param([switch]$Full)

$ErrorActionPreference = "Stop"

function Write-Step { Write-Host "[HACNATION] $args" -ForegroundColor Blue }
function Write-OK    { Write-Host "[OK] $args" -ForegroundColor Green }
function Write-Warn  { Write-Host "[WARN] $args" -ForegroundColor Yellow }
function Write-Err   { Write-Host "[ERR] $args" -ForegroundColor Red }

if ($Full) {
    $Profiles = "--profile full"
    Write-Step "Mode COMPLET (17 services)"
} else {
    $Profiles = ""
    Write-Step "Mode COEUR (12 services) - parcours patient essentiel"
    Write-Step "  Exclus : hospitalisation, urgences, soins, bloc (utiliser -Full)"
}

Write-Step "Arret des conteneurs existants..."
docker compose down --remove-orphans 2>$null

Write-Step "Build des images..."
$buildCmd = "docker compose build $Profiles 2>&1"
Invoke-Expression $buildCmd

Write-Step "Demarrage des conteneurs..."
$upCmd = "docker compose up -d $Profiles"
Invoke-Expression $upCmd

Write-Step "Attente du demarrage complet..."

$services = @(
    @{Name="postgres"; Label="PostgreSQL"},
    @{Name="redis"; Label="Redis"},
    @{Name="keycloak"; Label="Keycloak (30-60s)"},
    @{Name="kafka"; Label="Kafka"},
    @{Name="discovery-server"; Label="Eureka"},
    @{Name="gateway"; Label="Gateway (8080)"},
    @{Name="patient-identity"; Label="Patient-Identity"},
    @{Name="dme"; Label="DME"},
    @{Name="rdv"; Label="RDV"},
    @{Name="fileattente"; Label="File Attente"},
    @{Name="consultation"; Label="Consultation"},
    @{Name="prescription"; Label="Prescription"},
    @{Name="laboratoire"; Label="Laboratoire"},
    @{Name="pharmacie"; Label="Pharmacie"},
    @{Name="facturation"; Label="Facturation"},
    @{Name="caisse"; Label="Caisse"},
    @{Name="accueil"; Label="Accueil"},
    @{Name="notification"; Label="Notification"},
    @{Name="reporting-admin"; Label="Admin & Reporting"}
)

$fullOnlyServices = @("hospitalisation", "urgences", "soins", "bloc")
$startTime = Get-Date
$maxWait = 300

foreach ($svc in $services) {
    if (-not $Full -and ($fullOnlyServices -contains $svc.Name)) { continue }

    $elapsed = ((Get-Date) - $startTime).TotalSeconds
    if ($elapsed -gt $maxWait) {
        Write-Err "Timeout apres ${maxWait}s"
        break
    }

    Write-Host -NoNewline "  Attente $($svc.Label.PadRight(25)) ... "
    
    $ready = $false
    while (-not $ready -and ((Get-Date) - $startTime).TotalSeconds -lt $maxWait) {
        $status = docker compose ps --format json $svc.Name 2>$null | ConvertFrom-Json
        if ($status.Health -eq "healthy") {
            $ready = $true
        } else {
            Start-Sleep -Seconds 2
        }
    }

    if ($ready) {
        Write-OK "$($svc.Label) pret"
    } else {
        Write-Err "$($svc.Label) non pret"
    }
}

$elapsed = [math]::Round(((Get-Date) - $startTime).TotalSeconds)
Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-OK "HACNATION est pret en ${elapsed}s !"
Write-Host "============================================" -ForegroundColor Green
Write-Host ""
Write-Host "Endpoints:" -ForegroundColor Cyan
Write-Host "  Gateway (API)     : http://localhost:8080"
Write-Host "  Keycloak (Admin)  : http://localhost:8180 (admin/admin)"
Write-Host "  Swagger UI        : http://localhost:8080/swagger-ui.html"
Write-Host "  Eureka Dashboard  : http://localhost:8761"
Write-Host "  Kafka (Broker)    : localhost:9092"
Write-Host ""
Write-Host "Utilisateurs pre-configures:" -ForegroundColor Cyan
Write-Host "  admin      / admin123    (Administrateur)"
Write-Host "  medecin    / medecin123  (Medecin)"
Write-Host "  accueil    / accueil123  (Agent d'accueil)"
Write-Host "  laborantin / labo123     (Laborantin)"
Write-Host "  pharmacien / pharma123   (Pharmacien)"
Write-Host "  caissier   / caisse123   (Caissier)"
Write-Host "  patient1   / patient123  (Patient)"
Write-Host ""
Write-Host "Demo rapide :" -ForegroundColor Cyan
Write-Host "  bash scripts/demo-end-to-end.sh"
Write-Host "  powershell -File scripts/demo-end-to-end.ps1"
Write-Host ""
Write-Host "Postman : Importer postman/HACNATION.postman_collection.json" -ForegroundColor Cyan
Write-Host ""
