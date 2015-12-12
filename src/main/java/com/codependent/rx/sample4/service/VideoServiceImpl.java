package com.codependent.rx.sample4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rx.Observable;

import com.codependent.rx.sample4.dao.VideoBasicInfoRepository;
import com.codependent.rx.sample4.dao.VideoRatingRepository;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;

@Service
@Transactional
public class VideoServiceImpl implements VideoService{

	@Autowired
	private VideoBasicInfoRepository basicInfoRepo;
	
	@Autowired
	private VideoRatingRepository ratingRepo;

	public Observable<VideoBasicInfo> getVideoBasicInfo(Integer videoId){
		return Observable.create( s -> {
			s.onNext(basicInfoRepo.findOne(videoId));
		});
	}
	
	public Observable<VideoRating> getVideoRating(Integer videoId){
		return Observable.create( s -> {
			s.onNext(ratingRepo.findOne(videoId));
		});
	}
	
	public Observable<VideoInfo> getVideoFullInfo(Integer videoId){
		return Observable.zip(getVideoBasicInfo(videoId), getVideoRating(videoId), (VideoBasicInfo basicInfo, VideoRating rating) -> {
			return new VideoInfo(basicInfo, rating);
		});
	}
}
