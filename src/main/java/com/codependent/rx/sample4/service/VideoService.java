package com.codependent.rx.sample4.service;

import rx.Observable;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoFullInfo;
import com.codependent.rx.sample4.dto.VideoRating;

public interface VideoService {

	Observable<VideoBasicInfo> getVideoBasicInfo(Integer videoId);
	Observable<VideoRating> getVideoRating(Integer videoId);
	Observable<VideoFullInfo> getVideoFullInfo(Integer videoId);
	
}
