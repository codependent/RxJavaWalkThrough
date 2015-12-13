package com.codependent.rx.sample4.service;

import rx.Observable;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;

public interface VideoService {

	Observable<VideoBasicInfo> addVideoBasicInfo(VideoBasicInfo videoBasicInfo);
	Observable<VideoRating> addVideoRating(VideoRating videoRating);
	Observable<VideoBasicInfo> getVideoBasicInfo(Integer videoId);
	Observable<VideoRating> getVideoRating(Integer videoId);
	Observable<VideoInfo> getVideoFullInfo(Integer videoId);

}
