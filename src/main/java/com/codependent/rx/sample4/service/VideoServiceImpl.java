package com.codependent.rx.sample4.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.codependent.rx.sample4.dao.VideoBasicInfoRepository;
import com.codependent.rx.sample4.dao.VideoRatingRepository;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.rx.SingleTxFactory;

import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

@Service
public class VideoServiceImpl implements VideoService{

	@Autowired
	private VideoBasicInfoRepository basicInfoRepo;
	
	@Autowired
	private VideoRatingRepository ratingRepo;
	
	@Autowired
	private SingleTxFactory singleTxFactory;
	
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
	public Single<VideoBasicInfo> addVideoBasicInfo(VideoBasicInfo videoBasicInfo) {
		Single<VideoBasicInfo> obs = Single.create( s -> {
			VideoBasicInfo savedBasic = transactionTemplate.execute( status -> {
				VideoBasicInfo basicInfo = basicInfoRepo.save(videoBasicInfo);
				return basicInfo;
			});
			s.onSuccess(savedBasic);
		});
		return obs.subscribeOn(scheduler);
	}
	
	@Override
	public Single<VideoRating> addVideoRating(VideoRating videoRating) {
		Single<VideoRating> obs =  Single.create( s -> {
			VideoRating savedRating = transactionTemplate.execute( status -> {
				VideoRating rating = ratingRepo.save(videoRating);
				return rating;
			});
			s.onSuccess(savedRating);
		});
		return obs.subscribeOn(scheduler);
	}
	
	public Single<VideoBasicInfo> getVideoBasicInfo(Integer videoId){
		Single<VideoBasicInfo> obs = singleTxFactory.create( s -> {
			if(basicInfoDelay!=null){
				try {
					Thread.sleep(basicInfoDelay);
				} catch (Exception e) {}
			}
			VideoBasicInfo v = basicInfoRepo.findOne(videoId);
			s.onSuccess(v);
		});
		return obs.subscribeOn(scheduler);
	}
	
	public Single<VideoRating> getVideoRating(Integer videoId){
		Single<VideoRating> obs =  Single.create( s -> {
			if(videoRatingDelay!=null){
				try {
					Thread.sleep(videoRatingDelay);
				} catch (Exception e) {}
			}
			VideoRating videoRating = transactionTemplate.execute( status -> {
				VideoRating v = ratingRepo.findOne(videoId);
				return v;
			});
			s.onSuccess(videoRating);
		});
		return obs.subscribeOn(scheduler);
	}
	
	public Single<VideoInfo> getVideoFullInfo(Integer videoId){
		return Single.zip(getVideoBasicInfo(videoId), getVideoRating(videoId), (VideoBasicInfo basicInfo, VideoRating rating) -> {
			return new VideoInfo(basicInfo, rating);
		});
	}

}
