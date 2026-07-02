package com.hacnation.pharmacie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.hacnation")
@EnableDiscoveryClient
@EnableScheduling
public class PharmacieApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacieApplication.class, args);
    }
}
