package com.codependent.rx.sampleapi.microservice.info;

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
import com.codependent.rx.sample4.service.VideoService;

@RestController
@EnableJpaRepositories(basePackageClasses=VideoBasicInfoRepository.class)
@EntityScan(basePackageClasses=VideoBasicInfo.class)
@EnableEurekaClient
@SpringBootApplication(scanBasePackageClasses={VideoInfoMicroserviceApplication.class, VideoService.class})
public class VideoInfoMicroserviceApplication {

	@Autowired
	private VideoService videoService;
	
	@RequestMapping(value="/videos/{videoId}", produces="application/json")
    public Observable<VideoBasicInfo> getVideoInfo(@PathVariable Integer videoId) {
		return videoService.getVideoBasicInfo(videoId);
	}
	
	@RequestMapping(value="/videos", method=RequestMethod.POST, consumes="application/json", produces="application/json")
    public Observable<VideoBasicInfo> addVideoInfo(@RequestBody VideoBasicInfo videoInfo) {
		return videoService.addVideoBasicInfo(videoInfo);
	}
	
    public static void main(String[] args) {
    	new SpringApplicationBuilder(VideoInfoMicroserviceApplication.class).web(true).run(args);
    }
}
