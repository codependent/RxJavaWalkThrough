package com.codependent.rx.sample4.service;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;

public interface SyncVideoService {

	VideoBasicInfo addVideoBasicInfo(VideoBasicInfo videoBasicInfo);
	VideoRating addVideoRating(VideoRating videoRating);
	VideoBasicInfo getVideoBasicInfo(Integer videoId);
	VideoRating getVideoRating(Integer videoId);
	VideoInfo getVideoFullInfo(Integer videoId);

}
