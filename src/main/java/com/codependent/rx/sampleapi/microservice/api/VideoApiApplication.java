package com.codependent.rx.sampleapi.microservice.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import rx.Observable;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;

@RestController
@RequestMapping("/api")
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
public class VideoApiApplication {

	@Autowired
	private VideoClient videoClient;
	
	@RequestMapping(value="/videos/{videoId}", produces="application/json")
    public DeferredResult<VideoInfo> getVideoInfo(@PathVariable Integer videoId) {
		DeferredResult<VideoInfo> dr = new DeferredResult<VideoInfo>();
		Observable<VideoBasicInfo> videoBasicInfo = videoClient.getVideoBasicInfo(videoId);
		Observable<VideoRating> videoRating = videoClient.getVideoRating(videoId);

		Observable.zip(videoBasicInfo, videoRating, (info, rating) -> {
			return new VideoInfo(info, rating);
		}).subscribe(dr::setResult, dr::setErrorResult);
		
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
	
    public static void main(String[] args) {
    	new SpringApplicationBuilder(VideoApiApplication.class).web(true).run(args);
    }
}
