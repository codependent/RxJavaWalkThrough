package com.codependent.rx.sample5;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.codependent.rx.sample4.service.VideoService;

@SpringBootApplication(scanBasePackageClasses={VideoApplication.class, VideoService.class})
public class VideoApplication {

	public static void main(String[] args) {
    	new SpringApplicationBuilder(VideoApplication.class).web(true).run(args);
    }
	
}
