package com.hacnation.bloc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.hacnation")
@EnableDiscoveryClient
public class BlocApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlocApplication.class, args);
    }
}
