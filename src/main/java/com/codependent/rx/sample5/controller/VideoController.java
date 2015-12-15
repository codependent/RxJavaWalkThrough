package com.codependent.rx.sample5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.service.VideoService;

@RestController
@RequestMapping("/videos")
public class VideoController {

	@Autowired
	private VideoService videoService;
	
	@Autowired
	private TaskExecutor executor;
	
	@RequestMapping(value="/{videoId}", produces="application/json")
	public VideoInfo getVideoInfo(@PathVariable Integer videoId, @RequestParam(required=false) String filter){
		VideoInfo videoInfo = null;
		if("basicInfo".equalsIgnoreCase(filter)){
			Observable<VideoBasicInfo> videoBasicInfo = videoService.getVideoBasicInfo(videoId);
			videoInfo = new VideoInfo(videoBasicInfo.toBlocking().first(), null);
		}else if("rating".equalsIgnoreCase(filter)){
			Observable<VideoRating> videoRating = videoService.getVideoRating(videoId);
			videoInfo = new VideoInfo(null, videoRating.toBlocking().first());
		}else{
			Observable<VideoInfo> videoFullInfo = videoService.getVideoFullInfo(videoId);
			videoInfo = videoFullInfo.toBlocking().first();
		}
		return videoInfo;
	}
	
	@RequestMapping(value="/{videoId}", produces="application/json", params="async=true")
	public DeferredResult<VideoInfo> getVideoInfoAsync(@PathVariable Integer videoId, @RequestParam(required=false) String filter){
		DeferredResult<VideoInfo> videoInfo = new DeferredResult<VideoInfo>();
		if("basicInfo".equalsIgnoreCase(filter)){
			Observable<VideoBasicInfo> videoBasicInfo = videoService.getVideoBasicInfo(videoId);
			videoBasicInfo.subscribeOn(Schedulers.from(executor)).subscribe( m -> videoInfo.setResult(new VideoInfo(m, null)), videoInfo::setErrorResult );
		}else if("rating".equalsIgnoreCase(filter)){
			Observable<VideoRating> videoRating = videoService.getVideoRating(videoId);
			videoRating.subscribeOn(Schedulers.from(executor)).subscribe( m -> videoInfo.setResult(new VideoInfo(null, m)), videoInfo::setErrorResult );
		}else{
			Observable<VideoInfo> videoFullInfo = videoService.getVideoFullInfo(videoId);
			videoFullInfo.subscribeOn(Schedulers.from(executor)).subscribe( videoInfo::setResult, videoInfo::setErrorResult );
		}
		return videoInfo;
	}
	
}
