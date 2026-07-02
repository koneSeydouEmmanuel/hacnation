package com.hacnation.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("patient-identity-service", r -> r
                        .path("/api/patients/**")
                        .uri("lb://patient-identity-service"))
                .route("dme-service-api", r -> r
                        .path("/api/patients/*/dme/**")
                        .uri("lb://dme-service"))
                .route("dme-service-fhir", r -> r
                        .path("/fhir/Patient/**", "/fhir/metadata")
                        .uri("lb://dme-service"))
                .route("rdv-service-api", r -> r
                        .path("/api/rdv/**")
                        .uri("lb://rdv-service"))
                .route("rdv-service-fhir", r -> r
                        .path("/fhir/Appointment/**")
                        .uri("lb://rdv-service"))
                .route("fileattente-service", r -> r
                        .path("/api/queue/**")
                        .uri("lb://fileattente-service"))
                .route("consultation-service-api", r -> r
                        .path("/api/consultations/**")
                        .uri("lb://consultation-service"))
                .route("consultation-service-fhir", r -> r
                        .path("/fhir/Observation/**")
                        .uri("lb://consultation-service"))
                .route("prescription-service-api", r -> r
                        .path("/api/prescriptions/**")
                        .uri("lb://prescription-service"))
                .route("prescription-service-fhir", r -> r
                        .path("/fhir/ServiceRequest/**")
                        .uri("lb://prescription-service"))
                .route("laboratoire-service-api", r -> r
                        .path("/api/labo/**")
                        .uri("lb://laboratoire-service"))
                .route("laboratoire-service-fhir", r -> r
                        .path("/fhir/DiagnosticReport/**")
                        .uri("lb://laboratoire-service"))
                .route("pharmacie-service", r -> r
                        .path("/api/pharma/**")
                        .uri("lb://pharmacie-service"))
                .route("facturation-service", r -> r
                        .path("/api/facturation/factures/**", "/api/facturation/generer")
                        .uri("lb://facturation-service"))
                .route("caisse-service", r -> r
                        .path("/api/facturation/payer", "/api/facturation/paiements/**", "/api/facturation/caisse/**")
                        .uri("lb://caisse-service"))
                .route("accueil-service", r -> r
                        .path("/api/admission", "/api/admission/*")
                        .uri("lb://accueil-service"))
                .route("hospitalisation-service", r -> r
                        .path("/api/admission/hospitalisations/**", "/api/admission/lits/**")
                        .uri("lb://hospitalisation-service"))
                .route("urgences-service", r -> r
                        .path("/api/admission/urgences/**")
                        .uri("lb://urgences-service"))
                .route("soins-service", r -> r
                        .path("/api/admission/soins/**")
                        .uri("lb://soins-service"))
                .route("bloc-service", r -> r
                        .path("/api/admission/bloc/**")
                        .uri("lb://bloc-service"))
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri("lb://notification-service"))
                .route("reporting-admin-service", r -> r
                        .path("/api/reporting/**", "/api/admin/**", "/api/crm/**")
                        .uri("lb://reporting-admin-service"))
                .build();
    }
}
