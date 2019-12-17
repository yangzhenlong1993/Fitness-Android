package com.zhenlong.Fitness.Bean;

import java.io.Serializable;

public class Participants implements Serializable {
	private Integer eventid;

	private Integer userid;

	private String role;

	public final static String OWNER = "OWNER";
	public final static String MEMBER = "MEMBER";
	// ��ϯ����
	private Integer count = 0;

	private User user;

	private Integer inornot = 0;

	public Integer getEventid() {
		return eventid;
	}

	public void setEventid(Integer eventid) {
		this.eventid = eventid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role == null ? null : role.trim();
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getInornot() {
		return inornot;
	}

	public void setInornot(Integer inornot) {
		this.inornot = inornot;
	}
}