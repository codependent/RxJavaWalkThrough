package com.codependent.rx.sample4.dto;

public class VideoInfo {

	private VideoBasicInfo basicInfo = new VideoBasicInfo();
	private VideoRating rating = new VideoRating();
	
	public VideoInfo() {}
	
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
	public void setRating(Integer rating) {
		this.rating = new VideoRating(null, rating);
	}
	public void setLength(Integer length) {
		this.basicInfo.setLength(length);
	}
	public void setId(Integer id) {
		this.basicInfo.setId(id);
	}
	public void setName(String name) {
		this.basicInfo.setName(name);
	}
	@Override
	public String toString() {
		return "VideoFullInfo [basicInfo=" + basicInfo + ", rating=" + rating
				+ "]";
	}
	
}
