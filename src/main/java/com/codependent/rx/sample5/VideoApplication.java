package com.codependent.rx.sample5;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.codependent.rx.sample4.dao.VideoBasicInfoRepository;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.service.VideoService;

@EnableJpaRepositories(basePackageClasses=VideoBasicInfoRepository.class)
@EntityScan(basePackageClasses=VideoBasicInfo.class)
@SpringBootApplication(scanBasePackageClasses={VideoApplication.class, VideoService.class, VideoBasicInfoRepository.class})
public class VideoApplication {

	public static void main(String[] args) {
    	new SpringApplicationBuilder(VideoApplication.class).web(true).run(args);
    }
	
}
