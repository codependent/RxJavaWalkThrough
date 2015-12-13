package com.codependent.rx.sample4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import rx.Observable;

import com.codependent.rx.sample4.dao.VideoBasicInfoRepository;
import com.codependent.rx.sample4.dao.VideoRatingRepository;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;

@Service
public class VideoServiceImpl implements VideoService{

	@Autowired
	private VideoBasicInfoRepository basicInfoRepo;
	
	@Autowired
	private VideoRatingRepository ratingRepo;
	
	@Autowired
	private TransactionTemplate  transactionTemplate;

	@Override
	public Observable<VideoBasicInfo> addVideoBasicInfo(VideoBasicInfo videoBasicInfo) {
		return Observable.create( s -> {
			VideoBasicInfo savedBasic = transactionTemplate.execute( status -> {
				VideoBasicInfo basicInfo = basicInfoRepo.save(videoBasicInfo);
				return basicInfo;
			});
			s.onNext(savedBasic);
		});
	}
	
	@Override
	public Observable<VideoRating> addVideoRating(VideoRating videoRating) {
		return Observable.create( s -> {
			VideoRating savedRating = transactionTemplate.execute( status -> {
				VideoRating rating = ratingRepo.save(videoRating);
				return rating;
			});
			s.onNext(savedRating);
		});
	}
	
	public Observable<VideoBasicInfo> getVideoBasicInfo(Integer videoId){
		return Observable.create( s -> {
			VideoBasicInfo videoBasicInfo = transactionTemplate.execute( status -> {
				VideoBasicInfo v = basicInfoRepo.findOne(videoId);
				return v;
			});
			s.onNext(videoBasicInfo);
		});
	}
	
	public Observable<VideoRating> getVideoRating(Integer videoId){
		return Observable.create( s -> {
			VideoRating videoRating = transactionTemplate.execute( status -> {
				VideoRating v = ratingRepo.findOne(videoId);
				return v;
			});
			s.onNext(videoRating);
		});
	}
	
	public Observable<VideoInfo> getVideoFullInfo(Integer videoId){
		return Observable.zip(getVideoBasicInfo(videoId), getVideoRating(videoId), (VideoBasicInfo basicInfo, VideoRating rating) -> {
			return new VideoInfo(basicInfo, rating);
		});
	}

}
