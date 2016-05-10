package com.codependent.rx.sample5.controller;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.service.SyncVideoService;
import com.codependent.rx.sample4.service.VideoService;

import rx.Single;

@RestController
@RequestMapping("/videos")
public class VideoController {

	@Autowired
	private VideoService videoService;
	
	@Autowired
	private SyncVideoService syncvideoService;
	
	@RequestMapping(value="/{videoId}", produces="application/json", params="sync=true")
	public VideoInfo getVideoInfo(@PathVariable Integer videoId, @RequestParam(required=false) String filter){
		VideoInfo videoInfo = null;
		if("basicInfo".equalsIgnoreCase(filter)){
			Single<VideoBasicInfo> videoBasicInfo = videoService.getVideoBasicInfo(videoId);
			videoInfo = new VideoInfo(videoBasicInfo.toBlocking().value(), null);
		}else if("rating".equalsIgnoreCase(filter)){
			Single<VideoRating> videoRating = videoService.getVideoRating(videoId);
			videoInfo = new VideoInfo(null, videoRating.toBlocking().value());
		}else{
			Single<VideoInfo> videoFullInfo = videoService.getVideoFullInfo(videoId);
			videoInfo = videoFullInfo.toBlocking().value();
		}
		return videoInfo;
	}
	
	@RequestMapping(value="/{videoId}", produces="application/json", params="sync2=true")
	public VideoInfo getVideoInfo2(@PathVariable Integer videoId, @RequestParam(required=false) String filter){
		VideoInfo videoInfo = null;
		if("basicInfo".equalsIgnoreCase(filter)){
			VideoBasicInfo videoBasicInfo = syncvideoService.getVideoBasicInfo(videoId);
			videoInfo = new VideoInfo(videoBasicInfo, null);
		}else if("rating".equalsIgnoreCase(filter)){
			VideoRating videoRating = syncvideoService.getVideoRating(videoId);
			videoInfo = new VideoInfo(null, videoRating);
		}else{
			VideoInfo videoFullInfo = syncvideoService.getVideoFullInfo(videoId);
			videoInfo = videoFullInfo;
		}
		return videoInfo;
	}
	
	@RequestMapping(value="/{videoId}", produces="application/json", params="async=true")
	public DeferredResult<VideoInfo> getVideoInfoAsync(@PathVariable Integer videoId, @RequestParam(required=false) String filter){
		DeferredResult<VideoInfo> videoInfo = new DeferredResult<VideoInfo>();
		if("basicInfo".equalsIgnoreCase(filter)){
			Single<VideoBasicInfo> videoBasicInfo = videoService.getVideoBasicInfo(videoId);
			videoBasicInfo.subscribe( m -> videoInfo.setResult(new VideoInfo(m, null)), videoInfo::setErrorResult );
		}else if("rating".equalsIgnoreCase(filter)){
			Single<VideoRating> videoRating = videoService.getVideoRating(videoId);
			videoRating.subscribe( m -> videoInfo.setResult(new VideoInfo(null, m)), videoInfo::setErrorResult );
		}else{
			Single<VideoInfo> videoFullInfo = videoService.getVideoFullInfo(videoId);
			videoFullInfo.subscribe( videoInfo::setResult, videoInfo::setErrorResult );
		}
		return videoInfo;
	}
	
	@RequestMapping(value="/{videoId}", produces="application/json", params="async2=true")
	public Callable<VideoInfo> getVideoInfoAsync2(@PathVariable Integer videoId, @RequestParam(required=false) String filter){
		return () -> {
			VideoInfo videoInfo = null;
			if("basicInfo".equalsIgnoreCase(filter)){
				Single<VideoBasicInfo> videoBasicInfo = videoService.getVideoBasicInfo(videoId);
				videoInfo = new VideoInfo(videoBasicInfo.toBlocking().value(), null);
			}else if("rating".equalsIgnoreCase(filter)){
				Single<VideoRating> videoRating = videoService.getVideoRating(videoId);
				videoInfo = new VideoInfo(null, videoRating.toBlocking().value());
			}else{
				Single<VideoInfo> videoFullInfo = videoService.getVideoFullInfo(videoId);
				videoInfo = videoFullInfo.toBlocking().value();
			}
			return videoInfo;
		};
	}
	
	@RequestMapping(value="/{videoId}", produces="application/json", params="async3=true")
	public Single<VideoInfo> getVideoInfoAsync3(@PathVariable Integer videoId, @RequestParam(required=false) String filter){
		Single<VideoInfo> observable = null;
		if("basicInfo".equalsIgnoreCase(filter)){
			observable = videoService.getVideoBasicInfo(videoId).map( o -> new VideoInfo(o, null));
		}else if("rating".equalsIgnoreCase(filter)){
			observable = videoService.getVideoRating(videoId).map( o -> new VideoInfo(null, o));
		}else{
			observable = videoService.getVideoFullInfo(videoId);
		}
		return observable;
	}
	
}
