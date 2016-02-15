package com.codependent.rx.sampleapi.microservice.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.rx.ObservableTxFactory;

@RestController
@RequestMapping("/api")
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
public class VideoApiApplication {

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
	private VideoClient videoClient;
	
	@RequestMapping(value="/videos/{videoId}", produces="application/json")
    public DeferredResult<VideoInfo> getVideoInfo(@PathVariable Integer videoId) {
		DeferredResult<VideoInfo> dr = new DeferredResult<VideoInfo>();
		Observable<VideoBasicInfo> videoBasicInfo = videoClient.getVideoBasicInfo(videoId);
		Observable<VideoRating> videoRating = videoClient.getVideoRating(videoId);

		Observable.zip(videoBasicInfo, videoRating, (info, rating) -> {
			return new VideoInfo(info, rating);
		}).subscribeOn(Schedulers.io()).subscribe(dr::setResult, dr::setErrorResult);
		
		return dr;
    }
	
	@RequestMapping(value="/videos", method=RequestMethod.POST , consumes="application/json", produces="application/json")
    public DeferredResult<VideoInfo> addVideo(@RequestBody VideoInfo videoInfo) {
		DeferredResult<VideoInfo> dr = new DeferredResult<VideoInfo>();
		Observable<VideoBasicInfo> videoBasicInfo = videoClient.addBasicInfo(new VideoBasicInfo(null, videoInfo.getName(), videoInfo.getLength()));
		videoBasicInfo.subscribe( res -> {
			Observable<VideoRating> ratingObservable = videoClient.addRating(new VideoRating(null, videoInfo.getRating()));
			ratingObservable.subscribe( res2 -> {
				dr.setResult(new VideoInfo(res, res2));
			}, dr::setErrorResult);
		}, dr::setErrorResult);
		return dr;
    }
	
	@RequestMapping(value="/videos/{videoId}", produces="application/json", params="parallel=false")
    public DeferredResult<VideoInfo> getVideoInfoSerialExecution(@PathVariable Integer videoId) {
		DeferredResult<VideoInfo> dr = new DeferredResult<VideoInfo>();
		VideoBasicInfo videoBasicInfo = videoClient.getVideoBasicInfoSync(videoId);
		VideoRating videoRating = videoClient.getVideoRatingSync(videoId);

		Observable.zip(Observable.just(videoBasicInfo), Observable.just(videoRating), (info, rating) -> {
			return new VideoInfo(info, rating);
		}).subscribeOn(Schedulers.io()).subscribe(dr::setResult, dr::setErrorResult);
		
		return dr;
    }
	
	@RequestMapping(value="/videos", method=RequestMethod.POST , consumes="application/json", produces="application/json", params="parallel=false")
    public DeferredResult<VideoInfo> addVideoSyncSerialExecution(@RequestBody VideoInfo videoInfo) {
		DeferredResult<VideoInfo> dr = new DeferredResult<VideoInfo>();
		VideoBasicInfo videoBasicInfo = videoClient.addBasicInfoSync(new VideoBasicInfo(null, videoInfo.getName(), videoInfo.getLength()));
		Observable.just(videoBasicInfo).subscribe( res -> {
			VideoRating ratingObservable = videoClient.addRatingSync(new VideoRating(null, videoInfo.getRating()));
			Observable.just(ratingObservable).subscribe( res2 -> {
				dr.setResult(new VideoInfo(res, res2));
			}, dr::setErrorResult);
		}, dr::setErrorResult);
		return dr;
    }
	
    public static void main(String[] args) {
    	new SpringApplicationBuilder(VideoApiApplication.class).web(true).run(args);
    }
}
