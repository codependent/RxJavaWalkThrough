package com.codependent.rx.sample4.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import rx.Observable;

import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;

@Service
public class VideoServiceImpl implements VideoService{

	private Map<Integer, VideoBasicInfo> videoInfos = new HashMap<Integer, VideoBasicInfo>();
	private Map<Integer, VideoRating> videoRatings = new HashMap<Integer, VideoRating>();
	
	public VideoServiceImpl(){
		videoInfos.put(1, new VideoBasicInfo(1, "Star Wars - Episode 4", 110));
		videoInfos.put(2, new VideoBasicInfo(2, "Star Wars - Episode 5", 120));
		videoInfos.put(3, new VideoBasicInfo(3, "Star Wars - Episode 6", 115));
		videoInfos.put(4, new VideoBasicInfo(4, "Star Wars - Episode 1", 121));
		videoInfos.put(5, new VideoBasicInfo(5, "Star Wars - Episode 2", 122));
		videoInfos.put(6, new VideoBasicInfo(6, "Star Wars - Episode 3", 117));
		
		videoRatings.put(1, new VideoRating(1, 9));
		videoRatings.put(2, new VideoRating(2, 10));
		videoRatings.put(3, new VideoRating(3, 9));
		videoRatings.put(4, new VideoRating(4, 6));
		videoRatings.put(5, new VideoRating(5, 7));
		videoRatings.put(6, new VideoRating(6, 8));
	}
	
	public Observable<VideoBasicInfo> getVideoBasicInfo(Integer videoId){
		return Observable.create( s -> {
			s.onNext(videoInfos.get(videoId));
		});
	}
	
	public Observable<VideoRating> getVideoRating(Integer videoId){
		return Observable.create( s -> {
			s.onNext(videoRatings.get(videoId));
		});
	}
	
	public Observable<VideoInfo> getVideoFullInfo(Integer videoId){
		return Observable.zip(getVideoBasicInfo(videoId), getVideoRating(videoId), (VideoBasicInfo basicInfo, VideoRating rating) -> {
			return new VideoInfo(basicInfo, rating);
		});
	}
}
