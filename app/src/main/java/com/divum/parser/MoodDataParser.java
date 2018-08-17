package com.divum.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.divum.entity.MoodEntity;
import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;

public class MoodDataParser {

	private String errorString="";
	private HashMap<String, MoodEntity> hashMood=null;
	private HashMap<String, String> hashFindMood=null;
	private HashMap<String, String> hashMoodAppliance=null;
	private HashMap<String, String> hashMoodApplRoom=null;

	public MoodDataParser(String urlString,Context context) {

		try {
			hashMood=new HashMap<String, MoodEntity>();
			hashFindMood=new HashMap<String, String>();
			hashMoodAppliance=new HashMap<String, String>();
			hashMoodApplRoom=new HashMap<String, String>();
			
			BaseParser parser=new BaseParser(urlString);
			errorString=parser.getException();
			JSONObject obj=parser.getJsonObject();

			//			String response=App_Variable.getDataFromFile("data.txt", context);
			//			JSONObject obj = new JSONObject(response);

			if(obj!=null)
				GetData(obj);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getException(){
		return errorString;
	}

	/**
	 * @return @hashMood contains key = which mood like mood11,mood22 and value= mood values like area,mood
	 */
	public HashMap<String, MoodEntity> getHashMoodData(){
		return hashMood;
	}

	/**
	 * @return @hashFindMood contains key= area+moodname and values= which mood
	 */
	public HashMap<String, String> getHashMoodFind(){
		return hashFindMood;
	}

	/**
	 * @return hashMoodAppliance contains key= dimmer or light channel id and values= which one like dimmer r light r ac
	 */
	public HashMap<String, String> getHashMoodAppliance(){
		return hashMoodAppliance;
	}
	
	public HashMap<String, String> getHashMoodApplianceRoom(){
		return hashMoodApplRoom;
	}


	private void GetData(JSONObject obj) throws JSONException {

		if(obj.has("moods")){
			JSONObject obj_mood=obj.getJSONObject("moods");
			Iterator<?> iterator= obj_mood.keys();
			while (iterator.hasNext()) {
				String mood = (String) iterator.next();
				//System.out.println("mood:"+mood);

				JSONArray ary_mood=obj_mood.getJSONArray(mood);
				int size=ary_mood.length();
				MoodEntity entity=new MoodEntity();

				for (int i = 0; i < size; i++) {
					if(i==0){
						entity.setScript(ary_mood.getString(i));
					}else if(i==1){
						entity.setMood(ary_mood.getString(i));
					}else if(i==2){
						JSONObject obj2=new JSONObject(ary_mood.getString(i));
						entity.setArea(obj2.getString("area"));

						entity.setMoodName(obj2.getString("name"));

						//System.out.println(obj2.getString("name"));
					}
				}

				hashMood.put(mood, entity);

				hashFindMood.put(entity.getArea()+"&&"+entity.getMoodName(), mood);			
			}
		}

		if(obj.has("appliances")){
			CustomLog.parser("appliances");
			JSONObject obj_appliances=obj.getJSONObject("appliances");
			Iterator<?> iterator= obj_appliances.keys();
			while (iterator.hasNext()) {
				String roomID = (String) iterator.next();
				//System.out.println("appliances id:"+roomID);
				JSONArray ary_appliances=obj_appliances.getJSONArray(roomID);
				int size=ary_appliances.length();
				for (int i = 0; i < size; i++) {
					ary_appliances.getString(i);
					if(i==2){
						JSONObject objAppliance=new JSONObject(ary_appliances.getString(i));

						if(objAppliance.has("nickname")){
							String name=objAppliance.getString("nickname");
							//System.out.println("appliances name:"+name);
							hashMoodAppliance.put(roomID, name);
							
							String area=objAppliance.getString("area");
							
							hashMoodApplRoom.put(area+"&&"+name, roomID);
						}

						

						//	System.out.println("appliances name:"+obj2.getString("nickname"));
					}
				}

			}

		}

	}

}
