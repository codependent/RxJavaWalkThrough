package com.codependent.rx.sample4.dto;

public class VideoFullInfo {

	private VideoBasicInfo basicInfo;
	private VideoRating rating;
	
	public VideoFullInfo(VideoBasicInfo basicInfo, VideoRating rating) {
		super();
		this.basicInfo = basicInfo;
		this.rating = rating;
	}
	public Integer getLength() {
		return basicInfo.getLength();
	}
	public Integer getId() {
		return basicInfo.getId();
	}
	public String getName() {
		return basicInfo.getName();
	}
	public Integer getRating() {
		return rating.getRating();
	}
	@Override
	public String toString() {
		return "VideoFullInfo [basicInfo=" + basicInfo + ", rating=" + rating
				+ "]";
	}
	
}
