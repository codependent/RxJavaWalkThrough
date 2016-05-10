package com.codependent.rx.sampleapi.microservice.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rx.Single;

import com.codependent.rx.sample4.dao.VideoBasicInfoRepository;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.rx.ObservableTxFactory;
import com.codependent.rx.sample4.service.VideoService;

@RestController
@EnableJpaRepositories(basePackageClasses=VideoBasicInfoRepository.class)
@EntityScan(basePackageClasses=VideoBasicInfo.class)
@EnableEurekaClient
@SpringBootApplication(scanBasePackageClasses={VideoRatingMicroserviceApplication.class, VideoService.class, ObservableTxFactory.class})
public class VideoRatingMicroserviceApplication {

	@Value("${threadExecutor.corePoolSize:10}")
	private int corePoolSize;
	@Value("${threadExecutor.maxPoolSize:20}")
	private int maxPoolSize;
	
	@Bean
	ObservableTxFactory observableTxFactory() {
	    return new ObservableTxFactory();
	}
	
	@Bean
	public ThreadPoolTaskExecutor executor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		return executor;
	}
	
	@Autowired
	private VideoService videoService;
	
	@RequestMapping(value="/videos/{videoId}", produces="application/json")
    public Single<VideoRating> getVideoRating(@PathVariable Integer videoId) {
		return videoService.getVideoRating(videoId);
	}
	
	@RequestMapping(value="/videos", method=RequestMethod.POST, consumes="application/json", produces="application/json")
    public Single<VideoRating> addVideoInfo(@RequestBody VideoRating rating) {
		return videoService.addVideoRating(rating);
	}
	
    public static void main(String[] args) {
    	new SpringApplicationBuilder(VideoRatingMicroserviceApplication.class).web(true).run(args);
    }
}
