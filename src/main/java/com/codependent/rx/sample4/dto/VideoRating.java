package com.codependent.rx.sample4.dto;

public class VideoRating {

	private Integer videoId;
	private Integer rating;
	
	public VideoRating(Integer videoId, Integer rating) {
		super();
		this.videoId = videoId;
		this.rating = rating;
	}
	public Integer getVideoId() {
		return videoId;
	}
	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	@Override
	public String toString() {
		return "VideoRating [videoId=" + videoId + ", rating=" + rating + "]";
	}
	
}
