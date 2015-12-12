package com.codependent.rx.sample6.microservice.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;

@RestController
@RequestMapping("/api")
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
public class VideoApiApplication {

	@Autowired
	private VideoClient videoClient;
	
	@RequestMapping(value="/videos/{videoId}", produces="application/json")
    public VideoInfo getVideoInfo(@PathVariable Integer videoId) {
		VideoBasicInfo videoBasicInfo = videoClient.getVideoBasicInfo(videoId);
		VideoRating videoRating = videoClient.getVideoRating(videoId);
		return new VideoInfo(videoBasicInfo, videoRating);
    }
	
    public static void main(String[] args) {
    	new SpringApplicationBuilder(VideoApiApplication.class).web(true).run(args);
    }
}
