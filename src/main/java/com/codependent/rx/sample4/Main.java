package com.codependent.rx.sample4;

import rx.Observable;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoFullInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.service.VideoService;
import com.codependent.rx.sample4.service.VideoServiceImpl;

public class Main {

	public static void main(String[] args) {
		VideoService vs = new VideoServiceImpl();
		Observable<VideoBasicInfo> videoBasicInfo = vs.getVideoBasicInfo(2);
		Observable<VideoRating> videoRating = vs.getVideoRating(2);
		Observable<VideoFullInfo> videoFullInfo = vs.getVideoFullInfo(2);
		
		videoBasicInfo.subscribe( s -> {
			System.out.printf("Loaded video basic info: %s\n", s);
		});
		
		videoRating.subscribe( s -> {
			System.out.printf("Loaded video rating: %s\n", s);
		});
		
		videoFullInfo.subscribe( s -> {
			System.out.printf("Loaded video full info: %s\n", s);
		});
	}

}
