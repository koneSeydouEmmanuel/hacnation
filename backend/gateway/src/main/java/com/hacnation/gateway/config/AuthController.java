package com.hacnation.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final WebClient webClient;
    private final String keycloakTokenUrl;
    private final String keycloakAdminUsersUrl;
    private final String clientId;
    private final String patientServiceUrl;

    public AuthController(
            @Value("${keycloak.auth-server-url:http://keycloak:8080}") String authServerUrl,
            @Value("${keycloak.realm:hacnation}") String realm,
            @Value("${keycloak.client-id:hacnation-backend}") String clientId,
            @Value("${services.patient-identity.url:http://patient-identity-service:8081}") String patientServiceUrl) {
        this.keycloakTokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        this.keycloakAdminUsersUrl = authServerUrl + "/admin/realms/" + realm + "/users";
        this.clientId = clientId;
        this.patientServiceUrl = patientServiceUrl;
        this.webClient = WebClient.builder().build();
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody LoginRequest request) {
        return webClient.post()
                .uri(keycloakTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=password&client_id=" + clientId
                        + "&username=" + request.getUsername()
                        + "&password=" + request.getPassword())
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(Map.class)
                                .map(body -> ResponseEntity.ok((Map<String, Object>) body));
                    } else {
                        return response.bodyToMono(String.class)
                                .map(body -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .<Map<String, Object>>body(Map.of("error", "Invalid credentials", "details", body)));
                    }
                })
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(Map.of("error", "Keycloak unavailable: " + e.getMessage()))));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Map<String, Object>>> register(@RequestBody RegisterRequest request) {
        return webClient.post()
                .uri(keycloakTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=password&client_id=" + clientId
                        + "&username=admin&password=admin123")
                .exchangeToMono(response -> response.bodyToMono(Map.class))
                .flatMap(tokenResponse -> {
                    String accessToken = (String) tokenResponse.get("access_token");

                    Map<String, Object> userBody = Map.of(
                            "username", request.getUsername(),
                            "enabled", true,
                            "credentials", new Object[]{
                                    Map.of("type", "password", "value", request.getPassword(), "temporary", false)
                            }
                    );

                    return webClient.post()
                            .uri(keycloakAdminUsersUrl)
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(userBody)
                            .exchangeToMono(response -> {
                                if (response.statusCode().equals(HttpStatus.CREATED)) {
                                    String location = response.headers().asHttpHeaders().getFirst(HttpHeaders.LOCATION);
                                    String userId = location != null ? location.substring(location.lastIndexOf('/') + 1) : null;
                                    return Mono.just(userId);
                                }
                                return Mono.error(new RuntimeException("Echec creation utilisateur Keycloak"));
                            })
                            .flatMap(userId -> {
                                Map<String, Object> patientBody = Map.of(
                                        "nom", request.getNom(),
                                        "prenom", request.getPrenom(),
                                        "telephone", request.getTelephone(),
                                        "email", request.getEmail(),
                                        "dateNaissance", request.getDateNaissance(),
                                        "sexe", request.getSexe()
                                );

                                return webClient.post()
                                        .uri(patientServiceUrl + "/api/patients")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(patientBody)
                                        .exchangeToMono(response -> {
                                            if (response.statusCode().is2xxSuccessful()) {
                                                log.info("Registration reussie pour: {}", request.getUsername());
                                                return Mono.just(ResponseEntity.status(HttpStatus.CREATED)
                                                        .<Map<String, Object>>body(Map.of("message", "User registered", "username", request.getUsername())));
                                            }
                                            return Mono.error(new RuntimeException("Echec creation patient"));
                                        })
                                        .onErrorResume(e -> {
                                            log.error("Echec creation Patient, rollback Keycloak pour: {}", userId);
                                            return deleteKeycloakUser(accessToken, userId)
                                                    .then(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                                            .<Map<String, Object>>body(Map.of("error", "Registration failed"))));
                                        });
                            });
                })
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(Map.of("error", "Keycloak unavailable"))));
    }

    private Mono<Void> deleteKeycloakUser(String accessToken, String userId) {
        return webClient.delete()
                .uri(keycloakAdminUsersUrl + "/" + userId)
                .header("Authorization", "Bearer " + accessToken)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    }
                    return Mono.empty();
                })
                .onErrorResume(e -> {
                    log.error("CRITICAL: Echec rollback Keycloak pour userId: {}", userId);
                    return Mono.empty();
                })
                .then();
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        private String username;
        private String password;
        private java.util.List<String> roles;
        private String nom;
        private String prenom;
        private String telephone;
        private String email;
        private String dateNaissance;
        private String sexe;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public java.util.List<String> getRoles() { return roles; }
        public void setRoles(java.util.List<String> roles) { this.roles = roles; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getPrenom() { return prenom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }
        public String getTelephone() { return telephone; }
        public void setTelephone(String telephone) { this.telephone = telephone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getDateNaissance() { return dateNaissance; }
        public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }
        public String getSexe() { return sexe; }
        public void setSexe(String sexe) { this.sexe = sexe; }
    }
}
