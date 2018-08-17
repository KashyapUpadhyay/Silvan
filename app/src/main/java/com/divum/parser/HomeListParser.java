package com.divum.parser;

import android.content.Context;

import com.divum.entity.ConfigEntity;
import com.divum.entity.HomeEntity;
import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class HomeListParser {

	private ArrayList<String> list_type=null;
	private ArrayList<Hashtable<String, HomeEntity>> list_entity=null;
	private LinkedHashMap<String,ArrayList<String>> hashData=null;
	private Hashtable<String, HomeEntity> hashEntity=null;
	private Hashtable<String, ArrayList<Hashtable<String, HomeEntity>>> hashTotal=null;

	private LinkedHashMap<String,String> hashNickName=null;
    private LinkedHashMap hashtype=null;
	//private ArrayList<LinkedHashMap<String, String>> listAC=null;
	private LinkedHashMap<String, String> hashAC=null;
	private LinkedHashMap<String, LinkedHashMap<String, String>> hashACData=null;

	private LinkedHashMap<String, String> hashSensor=null;
	private LinkedHashMap<String, ConfigEntity> hashMoodRooms=null;
	private LinkedHashMap<String, ConfigEntity> hashMoodType=null;
	private LinkedHashMap hashEleurl=null;
	private LinkedHashMap hashtabname=null;
	private String errorString="";
	private String type="";
	//	private ArrayList<ConfigEntity> list_mood_config;

	private ArrayList<HomeEntity> listCamera;
	public HomeListParser(String urlString,Context context) {
		try {
			list_type=new ArrayList<String>();
			hashData=new LinkedHashMap<String, ArrayList<String>>();

			list_entity=new ArrayList<Hashtable<String,HomeEntity>>();
			hashEntity=new Hashtable<String, HomeEntity>();
			hashTotal=new Hashtable<String, ArrayList<Hashtable<String,HomeEntity>>>();

			hashACData=new LinkedHashMap<String, LinkedHashMap<String,String>>();

			hashSensor=new LinkedHashMap<String, String>();
			hashNickName=new LinkedHashMap<String, String>();
			hashtype=new LinkedHashMap<>();
			hashEleurl=new LinkedHashMap<>();
			hashtabname=new LinkedHashMap<>();
			//	hashCamera=new LinkedHashMap<String, String>();
			//list_mood_config=new ArrayList<ConfigEntity>();

			listCamera=new ArrayList<HomeEntity>();
			hashMoodRooms =new LinkedHashMap<String, ConfigEntity>();
			hashMoodType =new LinkedHashMap<String, ConfigEntity>();

		 BaseParser parser=new BaseParser(urlString);
		JSONObject obj=parser.getJsonObject();
			errorString=parser.getException();
//	String response=App_Variable.getDataFromFile("area(7).json", context);
//			JSONObject obj = new JSONObject(response);

			if(obj!=null)
				GetData(obj);

			/*Set entrySet = table.entrySet();
			Iterator iterator = entrySet.iterator();

			while (iterator.hasNext()) {

				Entry entry = (Entry) iterator.next();
				System.out.println("key new:"+entry.getKey());
			}*/

		} catch (JSONException e) {
			CustomLog.show("Error:"+e);
			e.printStackTrace();
		}
	}

	public String getException(){
		return errorString;
	}

	public LinkedHashMap<String, ArrayList<String>> getData(){

		return hashData;
	}
	public Hashtable<String, ArrayList<Hashtable<String, HomeEntity>>> getDetailData(){
		return hashTotal;
	}
	public LinkedHashMap<String, LinkedHashMap<String, String>> getACData(){
		return hashACData;
	}
	public LinkedHashMap<String, String> getSensorData(){
		return hashSensor;
	}
	public LinkedHashMap<String, String> getNickName(){
		return hashNickName;
	}
	public LinkedHashMap<String, String> getType(){return hashtype;}
	public LinkedHashMap<String, String> getEleurl(){return hashEleurl;}
	public LinkedHashMap<String, String> gettabname(){return hashtabname;}
	//	public LinkedHashMap<String, String> getCamera(){
	//		return hashCamera;
	//	}
	public ArrayList<HomeEntity> getListCamera(){
		return listCamera;
	}

	public LinkedHashMap<String, ConfigEntity> getListMoods(){
		return hashMoodType;
	}
	public LinkedHashMap<String, ConfigEntity> getMoodRooms(){
		return hashMoodRooms;}


	String key="";
	private void GetData(JSONObject obj) throws JSONException {

		RetrieveRooms(obj);


		RetrieveSensor(obj);


		RetrieveCamera(obj);


		RetrieveMoods(obj);

		RetrieveVDBurl(obj);


	}
	private void RetrieveVDBurl(JSONObject obj) throws JSONException {
		if(obj.has("vdb")){
			JSONObject vdb=obj.getJSONObject("vdb");
			App_Variable.vdb_video_path=vdb.optString("vdb_video_path");
			App_Variable.vdb_status_path=vdb.optString("vdb_status_path");

		}

	}



	/**
	 * retrieve rooms details
	 */
	private void RetrieveRooms(JSONObject obj) throws JSONException {

		if(obj.has("rooms")){

			JSONArray ary_room=obj.getJSONArray("rooms");
			int size=ary_room.length();
			for (int i = 0; i < size; i++) {
				list_type=new ArrayList<String>();

				JSONObject obj_rooms=ary_room.getJSONObject(i);

				Iterator<?> keys = obj_rooms.keys();
				key = (String)keys.next();
               // System.out.println("all keys"+key);
				JSONObject obj_key=obj_rooms.getJSONObject(key);
				Iterator<?> keys_room = obj_key.keys();

				list_entity=new ArrayList<Hashtable<String,HomeEntity>>();
				hashEntity=new Hashtable<String, HomeEntity>();

				while (keys_room.hasNext()) {

					type = (String) keys_room.next();
					list_type.add(type);
					//System.out.println("sub key::"+type);
                    Collections.sort(list_type);
					JSONObject obj_type=obj_key.getJSONObject(type);
					GetTypeData(obj_type,type);

				}
				CustomLog.parser("key:parser:" + key);

				hashData.put(key, list_type);

				//System.out.println("all data" +hashData);
				//table.put(key, list_type);
				hashTotal.put(key, list_entity);
				//System.out.println("all total" + list_entity);

				hashACData.put(key, hashAC);
				//System.out.println("all AC" +hashAC);
			}
		}
	}

	/**
	 * retrieve sensors details
	 * @throws JSONException 
	 */
	private void RetrieveSensor(JSONObject obj) throws JSONException {

		if(obj.has("sensors")){
			JSONObject obj_sensor=obj.getJSONObject("sensors");
			Iterator<?> keys_sensor = obj_sensor.keys();
			ArrayList<String> listKey=new ArrayList<>();

			while (keys_sensor.hasNext()) {
				String type = (String) keys_sensor.next();
				CustomLog.camera("key sensor:" + type + "," + obj_sensor.getString(type));
				listKey.add(type);

				//hashSensor.put(type, obj_sensor.getString(type));
			}

			Collections.sort(listKey);

			for(String key : listKey){
				//String type= listKey.get(count);
				//System.out.println("sensor key orders:"+key);
				hashSensor.put(key, obj_sensor.getString(key));

			}
			CustomLog.camera("key sensor:" +hashSensor);
		}

	}

/*	private void RetrieveSensor(JSONObject obj) throws JSONException {

		if(obj.has("sensors")){
			JSONObject obj_sensor=obj.getJSONObject("sensors");
			Iterator<?> keys_sensor = obj_sensor.keys();

			while (keys_sensor.hasNext()) {
				String type = (String) keys_sensor.next();
				CustomLog.parser("key sensor:"+type+","+obj_sensor.getString(type));

				hashSensor.put(type, obj_sensor.getString(type));
			}
		}

	}*/

	/**
	 * retrieve camera details
	 */
	private void RetrieveCamera(JSONObject obj) throws JSONException {
		if(obj.has("camera"))
		{
			listCamera =new ArrayList<HomeEntity>();
			JSONArray ary_camera=obj.getJSONArray("camera");
			int size=ary_camera.length();
			for (int i = 0; i < size; i++) {
				HomeEntity entity=new HomeEntity();
				JSONObject obj_camera=ary_camera.getJSONObject(i);
				if(obj_camera.has("name")){
					entity.setCameraRoom(obj_camera.getString("name"));
				}
				if(obj_camera.has("local_url")){
					entity.setLocalUrl(obj_camera.getString("local_url"));
				}
				if(obj_camera.has("ext_url")){
					entity.setExtUrl(obj_camera.getString("ext_url"));
				}

				listCamera.add(entity);
				//hashCamera.put(obj_camera.getString("name"), obj_camera.getString("local_url"));
			}
		}

	}

	/**
	 * retrieve mood details
	 * @throws JSONException 
	 */
	private void RetrieveMoods(JSONObject obj) throws JSONException {
		if(obj.has("mood_configuration")){
			JSONObject obj_moodconfig=obj.getJSONObject("mood_configuration");
			if(obj_moodconfig.has("mood_list")){
				JSONArray ary_mood=obj_moodconfig.getJSONArray("mood_list");
				int size_mood=ary_mood.length();
				hashMoodType =new LinkedHashMap<String, ConfigEntity>();
				for (int i = 0; i < size_mood; i++) {
					ConfigEntity entity=new ConfigEntity();

					JSONObject obj_mood=ary_mood.getJSONObject(i);
					if(obj_mood.has("name")){
						entity.setMoodName(obj_mood.getString("name"));
					}
					if(obj_mood.has("id")){
						entity.setMoodID(obj_mood.getString("id"));
					}
					if(obj_mood.has("img_url")){
						entity.setMoodImg(obj_mood.getString("img_url"));
					}
					hashMoodType.put(entity.getMoodID(), entity);
					//	list_mood_config.add(entity);
				}

			}

			if(obj_moodconfig.has("room_list")){
				JSONObject obj_rooms_list=obj_moodconfig.getJSONObject("room_list");
				Iterator<?> keys = obj_rooms_list.keys();
				hashMoodRooms = new LinkedHashMap<String, ConfigEntity>();
				while (keys.hasNext()) {
					String room = (String) keys.next();
					CustomLog.show("mood rooms::"+room);
					ConfigEntity entity=new ConfigEntity();

					JSONObject obj_room = obj_rooms_list.getJSONObject(room);
					if(obj_room.has("room_id")){
						entity.setRoomID(obj_room.getString("room_id"));
					}
					if(obj_room.has("moods")){
						ArrayList<String> list=new ArrayList<String>();
						JSONArray ary_moods=obj_room.getJSONArray("moods");
						int length = ary_moods.length();
						for (int i = 0; i < length; i++) {
							list.add(ary_moods.get(i)+"");
						}
						entity.setListMoods(list);
					}					
					hashMoodRooms.put(room, entity);              
				}
			}
		}


	}
	private void GetTypeData(JSONObject obj_type,String type) throws JSONException {

		HomeEntity entity=new HomeEntity();
		if(obj_type.has("script")){
			entity.setScript(obj_type.getString("script"));
		}
		if(obj_type.has("id")){
			entity.setId(obj_type.getString("id"));

		}
		if(obj_type.has("channel")){
			entity.setChannel(obj_type.getString("channel"));

		}

		if(obj_type.has("nickname")){
			entity.setNickName(obj_type.getString("nickname"));

		}
		if (obj_type.has("type")){
			entity.settype(obj_type.getString("type"));
		}
        if(obj_type.has("url"))
		{
			entity.setEleurl(obj_type.getString("url"));
			entity.setTabname(type);
			hashEleurl.put(key, entity.getEleurl());
			hashtabname.put(key, entity.getTabname());
		}
        hashtype.put(key+"&&"+type,entity.gettype());
		//System.out.println("hashtype:" + hashtype);
		hashNickName.put(key + "&&" + type, entity.getNickName());
		//System.out.println("hash nick name:"+hashNickName);
		if(obj_type.has("command")){
			JSONObject obj_command=obj_type.getJSONObject("command");
			String item=type.toLowerCase();
			if(item.contains("ac")){
				hashAC=new LinkedHashMap<String, String>();

				Iterator<?> ac_keys = obj_command.keys();
				while (ac_keys.hasNext()) {
					String key = (String)ac_keys.next();
					if(App_Variable.isNumeric(key)||key.startsWith("f")){
						hashAC.put(key, obj_command.getString(key));
						CustomLog.parser("ac keys::"+key);
					}
				}
				//listAC.add(hashAC);
			}

			if(obj_command.has("up")){
				entity.setUp(obj_command.getString("up"));
			}
			if(obj_command.has("down")){
				entity.setDown(obj_command.getString("down"));
			}
			if(obj_command.has("tg")){
				entity.setTg(obj_command.getString("tg"));
			}
			if(obj_command.has("on")){
				entity.setOn(obj_command.getString("on"));
			}
			if(obj_command.has("off")){
				entity.setOff(obj_command.getString("off"));
			}
			if(obj_command.has("pwr")){
				entity.setPwr(obj_command.getString("pwr"));
			}
			if(obj_command.has("ch_up")){
				entity.setCh_up(obj_command.getString("ch_up"));
			}
			if(obj_command.has("ch_dwn")){
				entity.setCh_dwn(obj_command.getString("ch_dwn"));
			}
			if(obj_command.has("vol_up")){
				entity.setVol_up(obj_command.getString("vol_up"));
			}
			if(obj_command.has("vol_dwn")){
				entity.setVol_dwn(obj_command.getString("vol_dwn"));
			}
			if(obj_command.has("mute")){
				entity.setMute(obj_command.getString("mute"));
			}
			if(obj_command.has("source")){
				entity.setSource(obj_command.getString("source"));
			}
			if(obj_command.has("enter")){
				entity.setEnter(obj_command.getString("enter"));
			}
			if(obj_command.has("right")){
				entity.setRight(obj_command.getString("right"));
			}
			if(obj_command.has("left")){
				entity.setLeft(obj_command.getString("left"));
			}

			if(obj_command.has("play")){
				entity.setPlay(obj_command.getString("play"));
			}
			if(obj_command.has("pause")){
				entity.setPause(obj_command.getString("pause"));
			}
			if(obj_command.has("guide")){
				entity.setGuide(obj_command.getString("guide"));
			}
			if(obj_command.has("open")){
				entity.setOpen(obj_command.getString("open"));
			}
			if(obj_command.has("close")){
				entity.setClose(obj_command.getString("close"));
			}
			if(obj_command.has("stop")){
				entity.setStop(obj_command.getString("stop"));
			}



			hashEntity.put(type, entity);

			list_entity.add(hashEntity);



		}

	}

}
