#!/bin/bash
# ======================================================
# HACNATION - Script de démonstration end-to-end
# Parcours patient complet
# ======================================================
# Prérequis: docker compose up -d
# Attendre que tous les services soient UP
# ======================================================

BASE_URL="http://localhost:8080"
TOKEN=""

echo "============================================="
echo " HACNATION - DEMO PARCOURS PATIENT"
echo "============================================="
echo ""

# ---------------------------------------------
# Étape 1 : Authentification
# ---------------------------------------------
echo ">>> Étape 1 : Authentification (via Keycloak)"
TOKEN=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | grep -o '"access_token":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
  echo "WARN: Keycloak non disponible, fallback mode..."
  TOKEN="dev-token-placeholder"
fi
echo "Token obtenu: ${TOKEN:0:20}..."
echo ""

AUTH="Authorization: Bearer $TOKEN"

# ---------------------------------------------
# Étape 2 : Création / Recherche patient
# ---------------------------------------------
echo ">>> Étape 2 : Recherche du patient"

PATIENT=$(curl -s -X GET "$BASE_URL/api/patients?q=Kouadio" \
  -H "$AUTH" -H "Content-Type: application/json")

PATIENT_ID=$(echo "$PATIENT" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)

if [ -z "$PATIENT_ID" ]; then
  echo "  Création d'un nouveau patient..."
  PATIENT=$(curl -s -X POST "$BASE_URL/api/patients" \
    -H "$AUTH" -H "Content-Type: application/json" \
    -d '{"nom":"Kouadio","prenom":"Yao","dateNaissance":"1985-06-15","sexe":"M","telephone":"0701020304","email":"yao.kouadio@email.ci","adresse":"Abidjan, Cocody","groupeSanguin":"O+"}')
  PATIENT_ID=$(echo "$PATIENT" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
fi
echo "Patient ID: $PATIENT_ID"
echo ""

# ---------------------------------------------
# Étape 3 : Prise de rendez-vous
# ---------------------------------------------
echo ">>> Étape 3 : Prise de rendez-vous"

RDV=$(curl -s -X POST "$BASE_URL/api/rdv" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"patientId\":\"$PATIENT_ID\",\"praticienId\":\"U002\",\"service\":\"CONSULTATION\",\"dateHeure\":\"2026-07-03T09:00:00\",\"motif\":\"Consultation de controle\"}")

RDV_ID=$(echo "$RDV" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
echo "Rendez-vous ID: $RDV_ID"
echo ""

# ---------------------------------------------
# Étape 4 : QR Code
# ---------------------------------------------
echo ">>> Étape 4 : Génération QR Code"

QR=$(curl -s -X GET "$BASE_URL/api/rdv/$RDV_ID/qr" \
  -H "$AUTH")
echo "QR Code généré (base64): ${QR:0:50}..."
echo ""

# ---------------------------------------------
# Étape 5 : Check-in dans la file d'attente
# ---------------------------------------------
echo ">>> Étape 5 : Check-in patient"

CHECKIN=$(curl -s -X POST "$BASE_URL/api/queue/checkin" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"patientId\":\"$PATIENT_ID\",\"rdvId\":\"$RDV_ID\",\"service\":\"CONSULTATION\",\"patientNom\":\"Kouadio\",\"patientPrenom\":\"Yao\"}")

echo "Check-in: $CHECKIN"
echo ""

# ---------------------------------------------
# Étape 6 : File d'attente — position
# ---------------------------------------------
echo ">>> Étape 6 : Position dans la file d'attente"

POSITION=$(curl -s -X GET "$BASE_URL/api/queue/$PATIENT_ID/position" \
  -H "$AUTH")
echo "Position: $POSITION"

QUEUE=$(curl -s -X GET "$BASE_URL/api/queue?service=CONSULTATION" \
  -H "$AUTH")
echo "File d'attente: $QUEUE"
echo ""

# ---------------------------------------------
# Étape 7 : Appel du patient par le médecin
# ---------------------------------------------
echo ">>> Étape 7 : Appel du patient"

CALL=$(curl -s -X POST "$BASE_URL/api/queue/call-next" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"service":"CONSULTATION"}')
echo "Patient appelé: $CALL"
echo ""

# ---------------------------------------------
# Étape 8 : Consultation médicale
# ---------------------------------------------
echo ">>> Étape 8 : Création de la consultation"

CONSULT=$(curl -s -X POST "$BASE_URL/api/consultations" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"patientId\":\"$PATIENT_ID\",\"rdvId\":\"$RDV_ID\",\"medecinId\":\"U002\",\"constantes\":{\"TA\":\"120/80\",\"pouls\":\"72\",\"temperature\":\"37.2\",\"poids\":\"70\",\"taille\":\"170\",\"spo2\":\"98\"},\"motif\":\"Controle general\"}")

CONSULT_ID=$(echo "$CONSULT" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
echo "Consultation ID: $CONSULT_ID"

# Saisie diagnostic et compte rendu
CONSULT_UPDATED=$(curl -s -X PUT "$BASE_URL/api/consultations/$CONSULT_ID" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"diagnostic":"Gastro-enterite aigue","compteRendu":"Patient presente des douleurs abdominales depuis 3 jours. Pas de fievre. Bon etat general."}')
echo "Consultation mise a jour: OK"
echo ""

# ---------------------------------------------
# Étape 9 : Prescription d'examens
# ---------------------------------------------
echo ">>> Étape 9 : Prescription d'examens (Laboratoire)"

PRESC_EXAM=$(curl -s -X POST "$BASE_URL/api/prescriptions" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"consultationId\":\"$CONSULT_ID\",\"type\":\"EXAMEN\",\"details\":{\"examens\":[\"NFS\",\"GLYCEMIE\",\"SEROLOGIE\"]}}")

echo "Prescription examen: $PRESC_EXAM"
echo ""

# ---------------------------------------------
# Étape 10 : Prescription de médicaments
# ---------------------------------------------
echo ">>> Étape 10 : Prescription de médicaments (Pharmacie)"

PRESC_MED=$(curl -s -X POST "$BASE_URL/api/prescriptions" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"consultationId\":\"$CONSULT_ID\",\"type\":\"MEDICAMENT\",\"details\":{\"medicaments\":[{\"medicamentId\":\"MED002\",\"nom\":\"Amoxicilline 500mg\",\"posologie\":\"1 cp 3x/jour\",\"duree\":7,\"quantite\":21},{\"medicamentId\":\"MED003\",\"nom\":\"Omeprazole 20mg\",\"posologie\":\"1 cp/jour\",\"duree\":14,\"quantite\":14}]}}")

echo "Prescription medicament: $PRESC_MED"
echo ""

# ---------------------------------------------
# Étape 11 : Terminer la consultation
# ---------------------------------------------
echo ">>> Étape 11 : Finalisation de la consultation"

TERMINER=$(curl -s -X POST "$BASE_URL/api/consultations/$CONSULT_ID/terminer" \
  -H "$AUTH")
echo "Consultation terminée: $TERMINER"
echo ""

# ---------------------------------------------
# Étape 12 : Laboratoire — Consultation des demandes
# ---------------------------------------------
echo ">>> Étape 12 : Laboratoire — Demandes en attente"

LABO_DEMANDES=$(curl -s -X GET "$BASE_URL/api/labo/demandes?statut=EN_ATTENTE" \
  -H "$AUTH")
echo "Demandes labo: $LABO_DEMANDES"
echo ""

# ---------------------------------------------
# Étape 13 : Laboratoire — Saisie résultats
# ---------------------------------------------
echo ">>> Étape 13 : Laboratoire — Saisie des résultats"

# Récupérer l'ID de la demande d'analyse
ANALYSE_ID=$(echo "$LABO_DEMANDES" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)

if [ -n "$ANALYSE_ID" ]; then
  RESULTATS=$(curl -s -X POST "$BASE_URL/api/labo/demandes/$ANALYSE_ID/resultats" \
    -H "$AUTH" -H "Content-Type: application/json" \
    -d '{"resultats":{"NFS":{"hemoglobine":"14.5 g/dL","leucocytes":"7800","plaquettes":"250000"},"GLYCEMIE":{"glycemie":"0.95 g/L","interpretation":"Normal"},"SEROLOGIE":{"resultat":"Negatif"}},"laborantinId":"U003"}')
  echo "Résultats saisis: $RESULTATS"

  # Validation
  VALIDATION=$(curl -s -X POST "$BASE_URL/api/labo/demandes/$ANALYSE_ID/valider" \
    -H "$AUTH" -H "Content-Type: application/json" \
    -d '{"validateurId":"U003"}')
  echo "Résultats validés: $VALIDATION"
fi
echo ""

# ---------------------------------------------
# Étape 14 : Pharmacie — Traitement ordonnance
# ---------------------------------------------
echo ">>> Étape 14 : Pharmacie — Délivrance"

PHARMA_ORDOS=$(curl -s -X GET "$BASE_URL/api/pharma/ordonnances?statut=EN_ATTENTE" \
  -H "$AUTH")
echo "Ordonnances en attente: $PHARMA_ORDOS"

ORDO_ID=$(echo "$PHARMA_ORDOS" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)

if [ -n "$ORDO_ID" ]; then
  DELIVRANCE=$(curl -s -X POST "$BASE_URL/api/pharma/ordonnances/$ORDO_ID/delivrer" \
    -H "$AUTH" -H "Content-Type: application/json" \
    -d '{"pharmacienId":"U004"}')
  echo "Médicaments délivrés: $DELIVRANCE"
fi
echo ""

# ---------------------------------------------
# Étape 15 : Facturation
# ---------------------------------------------
echo ">>> Étape 15 : Génération de la facture"

FACTURE=$(curl -s -X POST "$BASE_URL/api/facturation/generer" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"patientId\":\"$PATIENT_ID\",\"consultationId\":\"$CONSULT_ID\",\"actes\":[{\"description\":\"Consultation Generaliste\",\"quantite\":1,\"prixUnitaire\":5000},{\"description\":\"NFS\",\"quantite\":1,\"prixUnitaire\":3500},{\"description\":\"Glycemie\",\"quantite\":1,\"prixUnitaire\":2500},{\"description\":\"Serologie\",\"quantite\":1,\"prixUnitaire\":5000},{\"description\":\"Amoxicilline 500mg\",\"quantite\":21,\"prixUnitaire\":150},{\"description\":\"Omeprazole 20mg\",\"quantite\":14,\"prixUnitaire\":200}]}")

FACTURE_ID=$(echo "$FACTURE" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
echo "Facture ID: $FACTURE_ID"
echo "Facture: $FACTURE"
echo ""

# ---------------------------------------------
# Étape 16 : Paiement Mobile Money
# ---------------------------------------------
echo ">>> Étape 16 : Paiement Mobile Money (Orange Money)"

PAIEMENT=$(curl -s -X POST "$BASE_URL/api/facturation/payer" \
  -H "$AUTH" -H "Content-Type: application/json" \
  -d "{\"factureId\":\"$FACTURE_ID\",\"modePaiement\":\"ORANGE_MONEY\",\"telephone\":\"0701020304\",\"montant\":21900}")

echo "Paiement: $PAIEMENT"
echo ""

# ---------------------------------------------
# Étape 17 : Reçu de caisse
# ---------------------------------------------
echo ">>> Étape 17 : Résumé de caisse"

CAISSE=$(curl -s -X GET "$BASE_URL/api/facturation/caisse?date=$(date +%Y-%m-%d)" \
  -H "$AUTH")
echo "Résumé caisse: $CAISSE"
echo ""

# ---------------------------------------------
# Étape 18 : DME — Vérification mise à jour
# ---------------------------------------------
echo ">>> Étape 18 : Consultation du DME"

DME=$(curl -s -X GET "$BASE_URL/api/patients/$PATIENT_ID/dme" \
  -H "$AUTH")
echo "DME: $DME"
echo ""

# ---------------------------------------------
# Étape 19 : Notifications — Historique
# ---------------------------------------------
echo ">>> Étape 19 : Historique des notifications"

NOTIFS=$(curl -s -X GET "$BASE_URL/api/notifications?patientId=$PATIENT_ID" \
  -H "$AUTH")
echo "Notifications: $NOTIFS"
echo ""

# ---------------------------------------------
# Étape 20 : Reporting — Dashboards
# ---------------------------------------------
echo ">>> Étape 20 : Tableaux de bord direction"

echo "--- Dashboard Files d'attente ---"
curl -s -X GET "$BASE_URL/api/reporting/dashboard/files" -H "$AUTH"
echo ""

echo "--- Dashboard Consultations ---"
curl -s -X GET "$BASE_URL/api/reporting/dashboard/consultations" -H "$AUTH"
echo ""

echo "--- Dashboard Recettes ---"
curl -s -X GET "$BASE_URL/api/reporting/dashboard/recettes" -H "$AUTH"
echo ""

echo "--- Dashboard Stocks ---"
curl -s -X GET "$BASE_URL/api/reporting/dashboard/stocks" -H "$AUTH"
echo ""

# ---------------------------------------------
# Étape 21 : FHIR
# ---------------------------------------------
echo ">>> Étape 21 : Interopérabilité FHIR"

echo "--- FHIR Patient ---"
curl -s -X GET "$BASE_URL/fhir/Patient/$PATIENT_ID" \
  -H "$AUTH" -H "Accept: application/fhir+json"
echo ""

echo "--- FHIR CapabilityStatement ---"
curl -s -X GET "$BASE_URL/fhir/metadata" \
  -H "$AUTH" -H "Accept: application/fhir+json"
echo ""

# ---------------------------------------------
# Étape 22 : Health checks
# ---------------------------------------------
echo ">>> Étape 22 : Health checks"

echo "--- Gateway ---"
curl -s -X GET "$BASE_URL/actuator/health"
echo ""

echo "--- Patient Identity (:8081) ---"
curl -s -X GET "http://localhost:8081/actuator/health"
echo ""

echo "============================================="
echo " DEMO PARCOURS PATIENT TERMINÉE"
echo "============================================="
