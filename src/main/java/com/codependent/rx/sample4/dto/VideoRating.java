package com.codependent.rx.sample4.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="VIDEO_RATING")
public class VideoRating {

	@Id
    @GeneratedValue
    @Column(name="VIDEO_ID")
	private Integer videoId;
	
	private Integer rating;
	
	public VideoRating(){}
	
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
