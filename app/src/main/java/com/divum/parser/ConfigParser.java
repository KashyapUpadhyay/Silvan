package com.divum.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.divum.entity.ConfigEntity;

import android.content.Context;

public class ConfigParser {

	private String url="";
	private String errorString="";
	private ConfigEntity entity=null;
	
	public ConfigParser(String urlString, Context context) {
		
		try{
		BaseParser parser=new BaseParser(urlString);
		JSONObject obj=parser.getJsonObject();
		errorString=parser.getException();
		
		if(obj!=null)
			GetData(obj);
		
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String getException(){
		return errorString;
	}
	
	public ConfigEntity getConfigData(){
		return entity;
	}
	

	private void GetData(JSONObject obj) throws JSONException {
		entity =new ConfigEntity();
		
		if(obj.has("local_ip")){
			entity.setLocal_ip(obj.getString("local_ip"));
		}
		
		if(obj.has("project_name")){
			entity.setProject_name(obj.getString("project_name"));
		}
		
		if(obj.has("feature_control")){		
			JSONObject obj_feature=obj.getJSONObject("feature_control");
			if(obj_feature.has("EMS")){
				entity.setEMS(obj_feature.getString("EMS"));
			}
			if(obj_feature.has("camera")){
				entity.setCamera(obj_feature.getString("camera"));
			}
			if(obj_feature.has("sensors")){
				entity.setSensors(obj_feature.getString("sensors"));
			}
			if(obj_feature.has("lifestyle")){
				entity.setLifestyle(obj_feature.getString("lifestyle"));
			}
			if(obj_feature.has("panic")){
				entity.setPanic(obj_feature.getString("panic"));
			}
			if(obj_feature.has("door_unlock")){
				entity.setDoor_unlock(obj_feature.getString("door_unlock"));
			}
		}
		
		if(obj.has("vdb_type")){
			JSONArray ary_vdb=obj.getJSONArray("vdb_type");
			int size_vdb=ary_vdb.length();
			for (int i = 0; i < size_vdb; i++) {
				ConfigEntity entity_config =new ConfigEntity();
				
				JSONObject obj_vdb=ary_vdb.getJSONObject(i);
				if(obj_vdb.has("type")){
					entity_config.setType(obj_vdb.getString("type"));
				}
				if(obj_vdb.has("port")){
					entity_config.setPort(obj_vdb.getString("port"));
				}
				if(obj_vdb.has("ip")){
					entity_config.setIp(obj_vdb.getString("ip"));
				}
				if(obj_vdb.has("external_ip")){
					entity_config.setExternal_ip(obj_vdb.getString("external_ip"));
				}
			}
		}
		
		
		
	}

}
