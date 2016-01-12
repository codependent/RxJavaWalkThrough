package com.codependent.rx.samplescada;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ScadaApplication {
	
	public static void main(String[] args) {
    	new SpringApplicationBuilder(ScadaApplication.class).web(true).run(args);
    }
	
	
	
}