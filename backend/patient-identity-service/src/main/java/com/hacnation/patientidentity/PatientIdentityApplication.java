package com.hacnation.patientidentity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PatientIdentityApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientIdentityApplication.class, args);
    }
}
