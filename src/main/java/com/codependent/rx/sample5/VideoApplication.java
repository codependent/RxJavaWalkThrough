package com.codependent.rx.sample5;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.rx.RxJavaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.codependent.rx.sample4.dao.VideoBasicInfoRepository;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.rx.SingleTxFactory;
import com.codependent.rx.sample4.service.VideoService;

@EnableJpaRepositories(basePackageClasses=VideoBasicInfoRepository.class)
@EntityScan(basePackageClasses=VideoBasicInfo.class)
@Import(RxJavaAutoConfiguration.class)
@SpringBootApplication(scanBasePackageClasses={VideoApplication.class, VideoService.class, VideoBasicInfoRepository.class})
public class VideoApplication {
	
	@Value("${threadExecutor.corePoolSize}")
	private int corePoolSize;
	@Value("${threadExecutor.maxPoolSize}")
	private int maxPoolSize;
	
	@Bean
	public ThreadPoolTaskExecutor executor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		return executor;
	}
	/*
	@Bean
	ObservableTxFactory observableTxFactory() {
	    return new ObservableTxFactory();
	}
	*/
	
	@Bean
	SingleTxFactory singleTxFactory() {
	    return new SingleTxFactory();
	}
	
	public static void main(String[] args) {
    	new SpringApplicationBuilder(VideoApplication.class).web(true).run(args);
    }
	
}
