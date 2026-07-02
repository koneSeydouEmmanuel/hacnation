package com.hacnation.accueil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AccueilApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccueilApplication.class, args);
    }
}
