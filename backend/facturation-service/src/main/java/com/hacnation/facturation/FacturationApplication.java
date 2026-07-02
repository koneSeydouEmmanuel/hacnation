package com.hacnation.facturation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FacturationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacturationApplication.class, args);
    }
}
