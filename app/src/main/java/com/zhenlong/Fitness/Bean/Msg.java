package com.zhenlong.Fitness.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * ͨ�õķ���������
 * 
 * @author zhenlong
 *
 */
public class Msg implements Serializable {

	private int code;
	// ��ʾ��Ϣ
	private String operation;
	// ��Ҫ�Ĳ���
	private int fromId;
	// �����û�
	private int toId;
	// �����û�
	private User userInfo;

	private List<User> users;

	private Coordinate userLocation;
	
	private String shortMessage;
	
	private Event event;

	private int type;

	private List<Event> events;
	
	private List<Participants> participants;

	private byte[] img;

	public static final int INSERT_SUCCESS = 101;
	public static final int UPDATE_SUCCESS = 102;
	public static final int DELETE_SUCCESS = 103;
	public static final int SELECT_SUCCESS = 104;
	public static final int INSERT_FAIL = 201;
	public static final int UPDATE_FAIL = 202;
	public static final int DELETE_FAIL = 203;
	public static final int SELECT_FAIL = 204;
	public static final int TYPE_RECEIVED = 0;//收到的消息
	public static final int TYPE_SENT = 1;//发出去的消息

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public int getToId() {
		return toId;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public User getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public String getShortMessage() {
		return shortMessage;
	}

	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}

	public List<Participants> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participants> participants) {
		this.participants = participants;
	}

	public Coordinate getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(Coordinate userLocation) {
		this.userLocation = userLocation;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

}
