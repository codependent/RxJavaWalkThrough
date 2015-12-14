package com.codependent.rx.sample4.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="VIDEO_BASIC_INFO")
public class VideoBasicInfo {

	@Id
    @GeneratedValue
	private Integer id;
	private String name;
	private Integer length;
	
	public VideoBasicInfo(){}
	
	public VideoBasicInfo(Integer id, String name, Integer length) {
		super();
		this.id = id;
		this.name = name;
		this.length = length;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "VideoBasicInfo [id=" + id + ", name=" + name + ", length="
				+ length + "]";
	}
	
}
