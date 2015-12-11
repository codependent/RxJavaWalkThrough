package com.codependent.rx.sample4.dto;

public class VideoInfo {

	private VideoBasicInfo basicInfo;
	private VideoRating rating;
	
	public VideoInfo(VideoBasicInfo basicInfo, VideoRating rating) {
		super();
		this.basicInfo = basicInfo;
		this.rating = rating;
	}
	public Integer getLength() {
		return basicInfo!= null ? basicInfo.getLength() : null;
	}
	public Integer getId() {
		return basicInfo!= null ? basicInfo.getId() : null;
	}
	public String getName() {
		return basicInfo!= null ? basicInfo.getName() : null;
	}
	public Integer getRating() {
		return rating!=null ? rating.getRating() : null;
	}
	@Override
	public String toString() {
		return "VideoFullInfo [basicInfo=" + basicInfo + ", rating=" + rating
				+ "]";
	}
	
}
