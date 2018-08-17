package com.divum.silvan.callback;

import java.util.HashMap;

import com.divum.entity.MoodEntity;

public interface MoodDataCallback {

	void getResponseMoodData(HashMap<String, MoodEntity> hashMoodData,
			HashMap<String, String> hashMoodFind, HashMap<String, String> hashMap, HashMap<String, String> hashMap2);

}
