package com.hacnation.soins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.hacnation")
@EnableDiscoveryClient
public class SoinsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoinsApplication.class, args);
    }
}
