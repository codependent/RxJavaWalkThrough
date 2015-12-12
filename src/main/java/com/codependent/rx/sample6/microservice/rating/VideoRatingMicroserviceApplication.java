package com.codependent.rx.sample6.microservice.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rx.Observable;

import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.service.VideoService;

@RestController
@EnableEurekaClient
@SpringBootApplication(scanBasePackageClasses={VideoRatingMicroserviceApplication.class, VideoService.class})
public class VideoRatingMicroserviceApplication {

	@Autowired
	private VideoService videoService;
	
	@RequestMapping(value="/videos/{videoId}", produces="application/json")
    public Observable<VideoRating> getVideoRating(@PathVariable Integer videoId) {
		return videoService.getVideoRating(videoId);
	}
	
    public static void main(String[] args) {
    	new SpringApplicationBuilder(VideoRatingMicroserviceApplication.class).web(true).run(args);
    }
}
