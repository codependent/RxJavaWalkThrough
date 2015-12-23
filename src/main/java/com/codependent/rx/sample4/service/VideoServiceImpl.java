package com.codependent.rx.sample4.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import com.codependent.rx.sample4.dao.VideoBasicInfoRepository;
import com.codependent.rx.sample4.dao.VideoRatingRepository;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.rx.ObservableTxFactory;

@Service
public class VideoServiceImpl implements VideoService{

	@Autowired
	private VideoBasicInfoRepository basicInfoRepo;
	
	@Autowired
	private VideoRatingRepository ratingRepo;
	
	@Autowired
	private ObservableTxFactory observableTxFactory;
	
	@Autowired
	private TransactionTemplate  transactionTemplate;
	
	@Value("${videoService.getVideoBasicInfo.delay:}")
	private Integer basicInfoDelay;
	
	@Value("${videoService.getVideoRating.delay:}")
	private Integer videoRatingDelay;
	
	@Autowired
	private TaskExecutor executor;
	
	private Scheduler scheduler;
	
	@PostConstruct
	protected void initializeScheduler(){
		scheduler = Schedulers.from(executor);
	}

	@Override
	public Observable<VideoBasicInfo> addVideoBasicInfo(VideoBasicInfo videoBasicInfo) {
		Observable<VideoBasicInfo> obs = Observable.create( s -> {
			VideoBasicInfo savedBasic = transactionTemplate.execute( status -> {
				VideoBasicInfo basicInfo = basicInfoRepo.save(videoBasicInfo);
				return basicInfo;
			});
			s.onNext(savedBasic);
			s.onCompleted();
		});
		return obs.subscribeOn(scheduler);
	}
	
	@Override
	public Observable<VideoRating> addVideoRating(VideoRating videoRating) {
		Observable<VideoRating> obs =  Observable.create( s -> {
			VideoRating savedRating = transactionTemplate.execute( status -> {
				VideoRating rating = ratingRepo.save(videoRating);
				return rating;
			});
			s.onNext(savedRating);
			s.onCompleted();
		});
		return obs.subscribeOn(scheduler);
	}
	
	public Observable<VideoBasicInfo> getVideoBasicInfo(Integer videoId){
		Observable<VideoBasicInfo> obs = observableTxFactory.create( s -> {
			if(basicInfoDelay!=null){
				try {
					Thread.sleep(basicInfoDelay);
				} catch (Exception e) {}
			}
			VideoBasicInfo v = basicInfoRepo.findOne(videoId);
			s.onNext(v);
			s.onCompleted();
		});
		return obs.subscribeOn(scheduler);
	}
	
	public Observable<VideoRating> getVideoRating(Integer videoId){
		Observable<VideoRating> obs =  Observable.create( s -> {
			if(videoRatingDelay!=null){
				try {
					Thread.sleep(videoRatingDelay);
				} catch (Exception e) {}
			}
			VideoRating videoRating = transactionTemplate.execute( status -> {
				VideoRating v = ratingRepo.findOne(videoId);
				return v;
			});
			s.onNext(videoRating);
			s.onCompleted();
		});
		return obs.subscribeOn(scheduler);
	}
	
	public Observable<VideoInfo> getVideoFullInfo(Integer videoId){
		return Observable.zip(getVideoBasicInfo(videoId), getVideoRating(videoId), (VideoBasicInfo basicInfo, VideoRating rating) -> {
			return new VideoInfo(basicInfo, rating);
		});
	}

}
