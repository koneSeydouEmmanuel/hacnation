package com.hacnation.dme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmeApplication.class, args);
    }
}
