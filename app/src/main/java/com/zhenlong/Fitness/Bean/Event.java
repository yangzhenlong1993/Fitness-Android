package com.zhenlong.Fitness.Bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
	private Integer eventid;

	private String title;

	private String category;

	private String locationname;

	private Double longtitude;

	private Double latitude;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+11")
	private Date starttime;

	private Integer eventinterval;

	private Integer eventcount;

	//1 ������ɣ�0����δ��ɣ��κ��½���event״̬��ӦΪ0
	private Integer doneornot = 0;

	private Integer duration;

	public Integer getEventid() {
		return eventid;
	}

	public void setEventid(Integer eventid) {
		this.eventid = eventid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category == null ? null : category.trim();
	}

	public String getLocationname() {
		return locationname;
	}

	public void setLocationname(String locationname) {
		this.locationname = locationname == null ? null : locationname.trim();
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getEventinterval() {
		return eventinterval;
	}

	public void setEventinterval(Integer eventinterval) {
		this.eventinterval = eventinterval;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Integer getEventcount() {
		return eventcount;
	}

	public void setEventcount(Integer eventcount) {
		this.eventcount = eventcount;
	}

	public Integer getDoneornot() {
		return doneornot;
	}

	public void setDoneornot(Integer doneornot) {
		this.doneornot = doneornot;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}