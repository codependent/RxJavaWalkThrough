package com.codependent.rx.sampleapi.microservice.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;

@Service
public class VideoClient {

	@Autowired
	private EurekaClient discoveryClient;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	//--------------------------
	//----------ASYNC-----------
	//--------------------------
	
	@HystrixCommand
	public Observable<VideoBasicInfo> addBasicInfo(VideoBasicInfo videoInfo){
		return Observable.<VideoBasicInfo>create( (s) -> {
			s.onNext(invokeAddBasicInfo(videoInfo));
			s.onCompleted();
		}).subscribeOn(Schedulers.io());
	}
	
	@HystrixCommand
	public Observable<VideoRating> addRating(VideoRating rating){
		return Observable.<VideoRating>create( (s) -> {
			s.onNext(invokeAddRatingSync(rating));
			s.onCompleted();
		}).subscribeOn(Schedulers.io());
	}
	
	@HystrixCommand(observableExecutionMode=ObservableExecutionMode.EAGER, fallbackMethod="getVideoBasicInfoFallback")
	public Observable<VideoBasicInfo> getVideoBasicInfo(Integer videoId){
		return Observable.<VideoBasicInfo>create( (s) -> {
			s.onNext(invokeGetVideoBasicInfoSync(videoId));
			s.onCompleted();
		}).subscribeOn(Schedulers.io());
	}
	
	protected Observable<VideoBasicInfo> getVideoBasicInfoFallback(Integer videoId, Throwable error){
		VideoBasicInfo videoBasicInfo = new VideoBasicInfo(videoId, "N/A", -1);
		return Observable.just(videoBasicInfo);
	}
	
	@HystrixCommand(observableExecutionMode=ObservableExecutionMode.EAGER, fallbackMethod="getVideoRatingFallback")
	public Observable<VideoRating> getVideoRating(Integer videoId){
		return Observable.<VideoRating>create( (s) -> {
			s.onNext(invokeGetVideoRatingSync(videoId));
			s.onCompleted();
		}).subscribeOn(Schedulers.io());
	}
	
	protected Observable<VideoRating> getVideoRatingFallback(Integer videoId, Throwable error){
		VideoRating rating = new VideoRating(videoId, -1);
		return Observable.just(rating);
	}
	
	
	//-------------------------
	//----------SYNC-----------
	//-------------------------
	
	@HystrixCommand
	public VideoBasicInfo addBasicInfoSync(VideoBasicInfo videoInfo){
		return invokeAddBasicInfo(videoInfo);
	}
	
	@HystrixCommand
	public VideoRating addRatingSync(VideoRating rating){
		return invokeAddRatingSync(rating);
	}
	
	@HystrixCommand(fallbackMethod="getVideoBasicInfoSyncFallback")
	public VideoBasicInfo getVideoBasicInfoSync(Integer videoId){
		return invokeGetVideoBasicInfoSync(videoId);
	}
	
	protected VideoBasicInfo getVideoBasicInfoSyncFallback(Integer videoId){
		return new VideoBasicInfo(videoId, "N/A", -1);
	}
	
	@HystrixCommand(fallbackMethod="getVideoRatingSyncFallback")
	public VideoRating getVideoRatingSync(Integer videoId){
		return invokeGetVideoRatingSync(videoId);
	}
	
	protected VideoRating getVideoRatingSyncFallback(Integer videoId){
		return new VideoRating(videoId, -1);
	}
	
	
	private VideoBasicInfo invokeAddBasicInfo(VideoBasicInfo videoInfo){
		InstanceInfo ii = discoveryClient.getNextServerFromEureka("VIDEOINFO-MICROSERVICE", false);
		String homePageUrl = ii.getHomePageUrl();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");
		HttpEntity<VideoBasicInfo> entity = new HttpEntity<VideoBasicInfo>(videoInfo, headers);
		ResponseEntity<VideoBasicInfo> info = restTemplate.exchange(homePageUrl+"/videos", HttpMethod.POST, entity, VideoBasicInfo.class, new Object[]{});
		return info.getBody();
	}
	
	private VideoRating invokeAddRatingSync(VideoRating rating){
		InstanceInfo ii = discoveryClient.getNextServerFromEureka("VIDEORATING-MICROSERVICE", false);
		String homePageUrl = ii.getHomePageUrl();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type", "application/json");
		HttpEntity<VideoRating> entity = new HttpEntity<VideoRating>(rating, headers);
		ResponseEntity<VideoRating> info = restTemplate.exchange(homePageUrl+"/videos", HttpMethod.POST, entity, VideoRating.class, new Object[]{});
		return info.getBody();
	}
	
	private VideoBasicInfo invokeGetVideoBasicInfoSync(Integer videoId){
		InstanceInfo ii = discoveryClient.getNextServerFromEureka("VIDEOINFO-MICROSERVICE", false);
		String homePageUrl = ii.getHomePageUrl();
		ResponseEntity<VideoBasicInfo> info = restTemplate.getForEntity(homePageUrl+"/videos/"+videoId, VideoBasicInfo.class);
		return info.getBody();
	}
	
	private VideoRating invokeGetVideoRatingSync(Integer videoId){
		InstanceInfo ii = discoveryClient.getNextServerFromEureka("VIDEORATING-MICROSERVICE", false);
		String homePageUrl = ii.getHomePageUrl();
		ResponseEntity<VideoRating> info = restTemplate.getForEntity(homePageUrl+"/videos/"+videoId, VideoRating.class);
		return info.getBody();
	}
}
