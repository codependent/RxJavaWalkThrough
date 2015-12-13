package com.codependent.rx.sample4;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import rx.Observable;

import com.codependent.rx.sample4.dao.VideoBasicInfoRepository;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.service.VideoService;
import com.codependent.rx.sample5.VideoApplication;

@SpringBootApplication(scanBasePackageClasses={VideoApplication.class, VideoService.class, VideoBasicInfoRepository.class})
public class Main {

	public static void main(String[] args) {
		
		ConfigurableApplicationContext ac = new SpringApplicationBuilder(VideoApplication.class).web(false).run(args);
		
		VideoService vs = ac.getBean(VideoService.class);
		Observable<VideoBasicInfo> videoBasicInfo = vs.getVideoBasicInfo(2);
		Observable<VideoRating> videoRating = vs.getVideoRating(2);
		Observable<VideoInfo> videoFullInfo = vs.getVideoFullInfo(2);
		
		videoBasicInfo.subscribe( s -> {
			System.out.printf("Loaded video basic info: %s\n", s);
		});
		
		videoRating.subscribe( s -> {
			System.out.printf("Loaded video rating: %s\n", s);
		});
		
		videoFullInfo.subscribe( s -> {
			System.out.printf("Loaded video full info: %s\n", s);
		});
	}

}
