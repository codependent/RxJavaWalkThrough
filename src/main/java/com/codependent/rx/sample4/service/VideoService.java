package com.codependent.rx.sample4.service;

import rx.Observable;
import rx.Single;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;

public interface VideoService {

	Single<VideoBasicInfo> addVideoBasicInfo(VideoBasicInfo videoBasicInfo);
	Single<VideoRating> addVideoRating(VideoRating videoRating);
	Single<VideoBasicInfo> getVideoBasicInfo(Integer videoId);
	Single<VideoRating> getVideoRating(Integer videoId);
	Single<VideoInfo> getVideoFullInfo(Integer videoId);

}
