package com.divum.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.divum.callback.ResponseCallback;
import com.divum.constants.GetAPI;
import com.divum.entity.MoodEntity;
import com.divum.parser.MoodDataParser;
import com.divum.silvan.LogActivity;
import com.divum.silvan.MoodActivity;
import com.divum.silvan.callback.MoodDataCallback;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class GetMooodData extends AsyncTask<Void, Void, Void>{
	private String response="";
	private String errorString="";

	private Context context;
	private boolean _isLoadVisible;
//	private HashMap<String, MoodEntity> hashMood;
//	private HashMap<String, String> hashMoodFind;
	
	private MoodDataCallback callback;
	MoodDataParser mp;
	private ProgressDialog progress;
	public GetMooodData(Context _context, boolean isloading) {
		context = _context;
		_isLoadVisible = isloading;
		callback = (MoodDataCallback) context;
		GetAPI.BASE_URL=App_Variable.getBaseAPI(context);
		
		progress=new ProgressDialog(context);
	}
	
	public void setProgress(ProgressDialog _progress) {
		progress=_progress;
		
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if(_isLoadVisible){
			progress.setMessage("Please wait..");
			progress.setCanceledOnTouchOutside(false);
			progress.show();
		}
		
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		mp=new MoodDataParser(GetAPI.BASE_URL+GetAPI.MOOD_DATA+"?"+System.currentTimeMillis(),context);
		errorString=mp.getException().trim();
		
//		hashMood=mp.getHashMoodData();
//		hashMoodFind=mp.getHashMoodFind();
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {

		/*if(_isLoadVisible){
			progress.dismiss();
		}*/
		if(!errorString.equals("")){
			App_Variable.ShowErrorToast(errorString, context);
		}else{

			callback.getResponseMoodData(mp.getHashMoodData(),mp.getHashMoodFind(),mp.getHashMoodAppliance(),mp.getHashMoodApplianceRoom());
			/*Set entrySet = hashMood.entrySet();
			Iterator iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				//System.out.println("key scdule:"+entry.getKey()+","+entry.getValue());
				MoodEntity entity=hashMood.get(entry.getKey());
				//System.out.println("area:"+entity.getArea()+","+entity.getMoodName());

			}*/
		}
	}

	
	
	
	
}
