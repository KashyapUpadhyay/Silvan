package com.divum.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;

public class MoodConfigParser {
	private String errorString="";
	private ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> listMood;
	private LinkedHashMap<String, String> hashdata;
	private LinkedHashMap<String, LinkedHashMap<String, String>> hashMoodConfig;
	public MoodConfigParser(String urlString) {
		try {
			listMood =new ArrayList<LinkedHashMap<String,LinkedHashMap<String,String>>>();
			hashMoodConfig =new LinkedHashMap<String, LinkedHashMap<String,String>>();
			BaseParser parser=new BaseParser(urlString);
			

			JSONObject obj=parser.getMoodJsonObject();

			errorString=parser.getException();
			App_Variable.MOOD_RESPONSE=parser.getJsonResponse();
			
			CustomLog.camera("App_Variable.MOOD_RESPONSE:"+App_Variable.MOOD_RESPONSE);

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

	public ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> getMoodConfigList(){
		return listMood;
	}

	private void GetData(JSONObject obj) throws JSONException {

		Iterator<?> iterator= obj.keys();
		if(iterator!=null){
			while (iterator.hasNext()) {
				String mood = (String) iterator.next();	
				JSONObject obj_mood=obj.getJSONObject(mood);
				hashdata =new LinkedHashMap<String, String>();

				Iterator<?> iteratorMood=obj_mood.keys();
				while (iteratorMood.hasNext()) {
					String key=(String) iteratorMood.next();	
					hashdata.put(key, obj_mood.getString(key));
				}

				hashMoodConfig.put(mood, hashdata);

			}
		}
		listMood.add(hashMoodConfig);

	}

}
