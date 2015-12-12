package com.codependent.rx.sample6.microservice.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class VideoClient {

	@Autowired
	private EurekaClient discoveryClient;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@HystrixCommand
	public VideoBasicInfo getVideoBasicInfo(Integer videoId){
		InstanceInfo ii = discoveryClient.getNextServerFromEureka("VIDEOINFO-MICROSERVICE", false);
		String homePageUrl = ii.getHomePageUrl();
		VideoBasicInfo info = restTemplate.getForObject(homePageUrl+"/videos/"+videoId, VideoBasicInfo.class);
		return info;
	}
	
	@HystrixCommand
	public VideoRating getVideoRating(Integer videoId){
		InstanceInfo ii = discoveryClient.getNextServerFromEureka("VIDEORATING-MICROSERVICE", false);
		String homePageUrl = ii.getHomePageUrl();
		VideoRating rating = restTemplate.getForObject(homePageUrl+"/videos/"+videoId, VideoRating.class);
		return rating;
	}
}
