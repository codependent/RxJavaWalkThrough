package com.codependent.rx.sample6.microservice.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import rx.Observable;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;

@Service
public class VideoClient {

	@Autowired
	private EurekaClient discoveryClient;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@HystrixCommand
	public Observable<VideoBasicInfo> getVideoBasicInfo(Integer videoId){
		return new ObservableResult<VideoBasicInfo>() {
			@Override
			public VideoBasicInfo invoke() {
				InstanceInfo ii = discoveryClient.getNextServerFromEureka("VIDEOINFO-MICROSERVICE", false);
				String homePageUrl = ii.getHomePageUrl();
				ResponseEntity<VideoBasicInfo> info = restTemplate.getForEntity(homePageUrl+"/videos/"+videoId, VideoBasicInfo.class);
				return info.getBody();
			}
		};
	}
	
	@HystrixCommand
	public Observable<VideoRating> getVideoRating(Integer videoId){
		return new ObservableResult<VideoRating>() {
			@Override
			public VideoRating invoke() {
				InstanceInfo ii = discoveryClient.getNextServerFromEureka("VIDEORATING-MICROSERVICE", false);
				String homePageUrl = ii.getHomePageUrl();
				ResponseEntity<VideoRating> info = restTemplate.getForEntity(homePageUrl+"/videos/"+videoId, VideoRating.class);
				return info.getBody();
			}
		};
	}
}
