#!/bin/bash
# ===========================================================
# HACNATION Hackathon Quick Start
# Usage: ./start-hackathon.sh [full]
#   Sans argument : coeur (12 services, ~15 conteneurs)
#   Avec "full"  : complet (17 services, ~21 conteneurs)
# ===========================================================
set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

log() { echo -e "${BLUE}[HACNATION]${NC} $1"; }
ok()  { echo -e "${GREEN}[OK]${NC} $1"; }
warn(){ echo -e "${YELLOW}[WARN]${NC} $1"; }
err() { echo -e "${RED}[ERR]${NC} $1"; }

MODE="${1:-core}"
PROFILES=""

if [ "$MODE" = "full" ]; then
    PROFILES="--profile full"
    log "Mode COMPLET (17 services) selectionne"
else
    log "Mode COEUR (12 services) selectionne"
    log "  Inclus : gateway, patient, dme, rdv, fileattente, consultation, prescription, labo, pharmacie, facturation, caisse, accueil, notification, reporting-admin"
    log "  Exclus : hospitalisation, urgences, soins, bloc (utiliser './start-hackathon.sh full')"
fi

log "Arret des conteneurs existants..."
docker compose down --remove-orphans 2>/dev/null || true

log "Build des images (premiere fois uniquement)..."
docker compose build $PROFILES --quiet 2>/dev/null || docker compose build $PROFILES

log "Demarrage des conteneurs..."
docker compose up -d $PROFILES

log ""
log "Attente du demarrage complet..."
log ""

SERVICES=(
    "postgres:PostgreSQL"
    "redis:Redis"
    "keycloak:Keycloak"
    "kafka:Kafka"
    "discovery-server:Eureka"
    "gateway:Gateway"
    "patient-identity:Patient"
    "dme:DME"
    "rdv:RDV"
    "fileattente:File Attente"
    "consultation:Consultation"
    "prescription:Prescription"
    "laboratoire:Laboratoire"
    "pharmacie:Pharmacie"
    "facturation:Facturation"
    "caisse:Caisse"
    "accueil:Accueil"
    "notification:Notification"
    "reporting-admin:Admin"
)

START=$(date +%s)
MAX_WAIT=300

for entry in "${SERVICES[@]}"; do
    svc="${entry%%:*}"
    label="${entry##*:}"

    if [ "$svc" = "postgres" ] || [ "$svc" = "redis" ] || [ "$svc" = "keycloak" ] || [ "$svc" = "kafka" ] || [ "$svc" = "discovery-server" ] || [ "$svc" = "gateway" ]; then
        : # toujours present
    else
        if [ "$MODE" != "full" ] && { [ "$svc" = "hospitalisation" ] || [ "$svc" = "urgences" ] || [ "$svc" = "soins" ] || [ "$svc" = "bloc" ]; }; then
            continue
        fi
    fi

    ELAPSED=$(($(date +%s) - START))
    if [ $ELAPSED -gt $MAX_WAIT ]; then
        err "Timeout apres ${MAX_WAIT}s. Certains services peuvent ne pas etre prets."
        break
    fi

    printf "  Attente %-25s ... " "$label"
    until docker compose ps --format json "$svc" 2>/dev/null | grep -q '"Health":"healthy"'; do
        sleep 2
        ELAPSED=$(($(date +%s) - START))
        if [ $ELAPSED -gt $MAX_WAIT ]; then break; fi
    done

    if docker compose ps --format json "$svc" 2>/dev/null | grep -q '"Health":"healthy"'; then
        ok "$label pret"
    else
        err "$label non pret"
    fi
done

ELAPSED=$(($(date +%s) - START))
log ""
log "============================================"
ok  "HACNATION est pret en ${ELAPSED}s !"
log "============================================"
log ""
log "Endpoints:"
log "  Gateway (API)     : http://localhost:8080"
log "  Keycloak (Admin)  : http://localhost:8180 (admin/admin)"
log "  Swagger UI        : http://localhost:8080/swagger-ui.html"
log "  Eureka Dashboard  : http://localhost:8761"
log "  Kafka (Broker)    : localhost:9092"
log ""
log "Utilisateurs pre-configures:"
log "  admin     / admin123     (Administrateur)"
log "  medecin   / medecin123   (Medecin)"
log "  accueil   / accueil123   (Agent d'accueil)"
log "  laborantin/ labo123      (Laborantin)"
log "  pharmacien/ pharma123    (Pharmacien)"
log "  caissier  / caisse123    (Caissier)"
log "  patient1  / patient123   (Patient)"
log ""
log "Demo rapide : ./scripts/demo-end-to-end.sh"
log "Demo PS     : powershell -File scripts/demo-end-to-end.ps1"
log "Postman     : Importer postman/HACNATION.postman_collection.json"
log ""
