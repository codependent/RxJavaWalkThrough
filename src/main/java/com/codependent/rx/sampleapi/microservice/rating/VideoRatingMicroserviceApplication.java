package com.codependent.rx.sampleapi.microservice.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rx.Observable;

import com.codependent.rx.sample4.dao.VideoBasicInfoRepository;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.service.VideoService;

@RestController
@EnableJpaRepositories(basePackageClasses=VideoBasicInfoRepository.class)
@EntityScan(basePackageClasses=VideoBasicInfo.class)
@EnableEurekaClient
@SpringBootApplication(scanBasePackageClasses={VideoRatingMicroserviceApplication.class, VideoService.class})
public class VideoRatingMicroserviceApplication {

	@Autowired
	private VideoService videoService;
	
	@RequestMapping(value="/videos/{videoId}", produces="application/json")
    public Observable<VideoRating> getVideoRating(@PathVariable Integer videoId) {
		return videoService.getVideoRating(videoId);
	}
	
	@RequestMapping(value="/videos", method=RequestMethod.POST, consumes="application/json", produces="application/json")
    public Observable<VideoRating> addVideoInfo(@RequestBody VideoRating rating) {
		return videoService.addVideoRating(rating);
	}
	
    public static void main(String[] args) {
    	new SpringApplicationBuilder(VideoRatingMicroserviceApplication.class).web(true).run(args);
    }
}
