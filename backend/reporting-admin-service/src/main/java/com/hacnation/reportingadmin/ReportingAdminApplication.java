package com.hacnation.reportingadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.hacnation.reportingadmin", "com.hacnation.common"})
@EntityScan("com.hacnation.reportingadmin.domain.model")
@EnableDiscoveryClient
public class ReportingAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportingAdminApplication.class, args);
    }
}
