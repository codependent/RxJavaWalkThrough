package com.codependent.rx.sample4.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${videoService.getVideoBasicInfo.delay:}")
	private Integer basicInfoDelay;
	
	@Value("${videoService.getVideoRating.delay:}")
	private Integer videoRatingDelay;

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
		Observable<VideoBasicInfo> obs = Observable.create( s -> {
			VideoBasicInfo videoBasicInfo = transactionTemplate.execute( status -> {
				VideoBasicInfo v = basicInfoRepo.findOne(videoId);
				return v;
			});
			s.onNext(videoBasicInfo);
			s.onCompleted();
		});
		return (basicInfoDelay!=null) ? obs.delay(basicInfoDelay, TimeUnit.MILLISECONDS) : obs;
	}
	
	public Observable<VideoRating> getVideoRating(Integer videoId){
		Observable<VideoRating> obs =  Observable.create( s -> {
			VideoRating videoRating = transactionTemplate.execute( status -> {
				VideoRating v = ratingRepo.findOne(videoId);
				return v;
			});
			s.onNext(videoRating);
			s.onCompleted();
		});
		return (videoRatingDelay!=null) ? obs.delay(videoRatingDelay, TimeUnit.MILLISECONDS) : obs;
	}
	
	public Observable<VideoInfo> getVideoFullInfo(Integer videoId){
		return Observable.zip(getVideoBasicInfo(videoId), getVideoRating(videoId), (VideoBasicInfo basicInfo, VideoRating rating) -> {
			return new VideoInfo(basicInfo, rating);
		});
	}

}
