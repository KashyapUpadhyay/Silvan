package com.divum.entity;

import java.util.ArrayList;

public class ConfigEntity {
	
	private String local_ip="";
	private String project_name="";
	private String EMS="";
	private String sensors="";
	private String camera="";
	private String lifestyle="";
	private String panic="";
	private String door_unlock="";
	
	private String type="";
	private String port="";
	private String ip="";
	private String external_ip="";
	
	private String moodName="";
	private String moodID="";
	private String moodImg="";
	private String roomID="";
	private ArrayList<String> listMoods;

	public String getLocal_ip() {
		return local_ip;
	}
	public void setLocal_ip(String local_ip) {
		this.local_ip = local_ip;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getEMS() {
		return EMS;
	}
	public void setEMS(String eMS) {
		EMS = eMS;
	}
	public String getSensors() {
		return sensors;
	}
	public void setSensors(String sensors) {
		this.sensors = sensors;
	}
	public String getCamera() {
		return camera;
	}
	public void setCamera(String camera) {
		this.camera = camera;
	}
	public String getLifestyle() {
		return lifestyle;
	}
	public void setLifestyle(String lifestyle) {
		this.lifestyle = lifestyle;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getExternal_ip() {
		return external_ip;
	}
	public void setExternal_ip(String external_ip) {
		this.external_ip = external_ip;
	}
	public String getMoodName() {
		return moodName;
	}
	public void setMoodName(String moodName) {
		this.moodName = moodName;
	}
	public String getMoodID() {
		return moodID;
	}
	public void setMoodID(String moodID) {
		this.moodID = moodID;
	}
	public String getMoodImg() {
		return moodImg;
	}
	public void setMoodImg(String moodImg) {
		this.moodImg = moodImg;
	}
	public String getRoomID() {
		return roomID;
	}
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	public ArrayList<String> getListMoods() {
		return listMoods;
	}
	public void setListMoods(ArrayList<String> listMoods) {
		this.listMoods = listMoods;
	}
	public String getPanic() {
		return panic;
	}
	public void setPanic(String panic) {
		this.panic = panic;
	}
	public String getDoor_unlock() {
		return door_unlock;
	}
	public void setDoor_unlock(String door_unlock) {
		this.door_unlock = door_unlock;
	}
	
	
	

}
