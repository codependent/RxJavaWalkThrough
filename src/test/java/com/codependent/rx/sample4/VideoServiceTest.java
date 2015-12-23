package com.codependent.rx.sample4;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import rx.Observable;
import rx.schedulers.Schedulers;

import com.codependent.rx.sample4.cfg.TestConfiguration;
import com.codependent.rx.sample4.dto.VideoBasicInfo;
import com.codependent.rx.sample4.dto.VideoInfo;
import com.codependent.rx.sample4.dto.VideoRating;
import com.codependent.rx.sample4.service.SyncVideoService;
import com.codependent.rx.sample4.service.VideoService;

@Test
@SpringApplicationConfiguration(classes=TestConfiguration.class)
public class VideoServiceTest extends AbstractTransactionalTestNGSpringContextTests{

	@Autowired
	private VideoService videoService;
	
	@Autowired
	private SyncVideoService videoServiceSync;
	
	public void testAsync() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(3);
		
		Observable<VideoBasicInfo> videoBasicInfo = videoService.getVideoBasicInfo(2);
		Observable<VideoRating> videoRating = videoService.getVideoRating(2);
		Observable<VideoInfo> videoFullInfo = videoService.getVideoFullInfo(2);
		
		videoBasicInfo.subscribe( s -> {
			System.out.printf("Loaded video basic info: %s\n", s);
			Assert.assertEquals(s.getName(), "Star Wars - Episode 2");
			latch.countDown();
		});
		
		videoRating.subscribe( s -> {
			System.out.printf("Loaded video rating: %s\n", s);
			Assert.assertEquals((int)s.getRating(), 10);
			latch.countDown();
		});
		
		videoFullInfo.subscribe( s -> {
			System.out.printf("Loaded video full info: %s\n", s);
			Assert.assertEquals(s.getName(), "Star Wars - Episode 2");
			Assert.assertEquals((int)s.getRating(), 10);
			latch.countDown();
		});
		
		latch.await();
	}
	
	public void testAsync2() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(3);
		
		Observable<VideoBasicInfo> videoBasicInfo = videoService.getVideoBasicInfo(2).subscribeOn(Schedulers.io());
		Observable<VideoRating> videoRating = videoService.getVideoRating(2).subscribeOn(Schedulers.io());
		Observable<VideoInfo> videoFullInfo = videoService.getVideoFullInfo(2).subscribeOn(Schedulers.io());
		
		videoBasicInfo.subscribe( s -> {
			System.out.printf("Loaded video basic info: %s\n", s);
			Assert.assertEquals(s.getName(), "Star Wars - Episode 2");
			latch.countDown();
		});
		
		videoRating.subscribe( s -> {
			System.out.printf("Loaded video rating: %s\n", s);
			Assert.assertEquals((int)s.getRating(), 10);
			latch.countDown();
		});
		
		videoFullInfo.subscribe( s -> {
			System.out.printf("Loaded video full info: %s\n", s);
			Assert.assertEquals(s.getName(), "Star Wars - Episode 2");
			Assert.assertEquals((int)s.getRating(), 10);
			latch.countDown();
		});
		
		latch.await();
	}
	
	public void testSync(){
		VideoBasicInfo videoBasicInfo = videoServiceSync.getVideoBasicInfo(2);
		VideoRating videoRating = videoServiceSync.getVideoRating(2);
		VideoInfo videoFullInfo = videoServiceSync.getVideoFullInfo(2);
		
		Assert.assertEquals(videoBasicInfo.getName(), "Star Wars - Episode 2");
		Assert.assertEquals((int)videoRating.getRating(), 10);
		Assert.assertEquals(videoFullInfo.getName(), "Star Wars - Episode 2");
		Assert.assertEquals((int)videoFullInfo.getRating(), 10);
	}

}
