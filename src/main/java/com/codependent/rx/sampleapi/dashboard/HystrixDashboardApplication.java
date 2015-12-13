package com.codependent.rx.sampleapi.dashboard;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@EnableHystrixDashboard
@EnableTurbine
@SpringBootApplication(scanBasePackageClasses=HystrixDashboardApplication.class)
public class HystrixDashboardApplication {
	
    public static void main(String[] args) {
    	new SpringApplicationBuilder(HystrixDashboardApplication.class).web(true).run(args);
    }
}
