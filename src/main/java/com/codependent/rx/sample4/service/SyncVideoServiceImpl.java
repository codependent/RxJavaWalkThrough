package com.codependent.rx.sample4.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codependent.rx.sample4.dao.VideoBasicInfoRepository;
import com.codependent.rx.sample4.dao.VideoRatingRepository;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;

@Service
@Transactional
public class SyncVideoServiceImpl implements SyncVideoService{

	@Autowired
	private VideoBasicInfoRepository basicInfoRepo;
	
	@Autowired
	private VideoRatingRepository ratingRepo;
	
	@Value("${videoService.getVideoBasicInfo.delay:}")
	private Integer basicInfoDelay;
	
	@Value("${videoService.getVideoRating.delay:}")
	private Integer videoRatingDelay;
	
	@Override
	public VideoBasicInfo addVideoBasicInfo(VideoBasicInfo videoBasicInfo) {
		VideoBasicInfo basicInfo = basicInfoRepo.save(videoBasicInfo);
		return basicInfo;
	}
	
	@Override
	public VideoRating addVideoRating(VideoRating videoRating) {
		VideoRating rating = ratingRepo.save(videoRating);
		return rating;
	}
	
	public VideoBasicInfo getVideoBasicInfo(Integer videoId){
		VideoBasicInfo v = basicInfoRepo.findOne(videoId);
		try {
			Thread.sleep(basicInfoDelay);
		} catch (Exception e) {}
		return v;
	}
	
	public VideoRating getVideoRating(Integer videoId){
		VideoRating v = ratingRepo.findOne(videoId);
		try {
			Thread.sleep(videoRatingDelay);
		} catch (Exception e) {}
		return v;
	}
	
	public VideoInfo getVideoFullInfo(Integer videoId){
		VideoBasicInfo videoBasicInfo = getVideoBasicInfo(videoId);
		VideoRating videoRating = getVideoRating(videoId);
		return new VideoInfo(videoBasicInfo, videoRating);
	}

}
