package com.divum.utils;

import com.divum.callback.HomeListCallback;
import com.divum.callback.ProfileCallback;
import com.divum.constants.GetAPI;
import com.divum.parser.HomeListParser;
import com.divum.silvan.SplashActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

public class HomeList extends AsyncTask<Void, Void, Void>{

	private String errorString;
	private Context context;
	private HomeListCallback callback;
	private ProgressDialog progress=null;
	private String whichScreen="";
	public HomeList(Context _context) {
		context=_context;

		callback=(HomeListCallback) context;
		GetAPI.BASE_URL=App_Variable.getBaseAPI(context);
	}
	public HomeList(Context _context,boolean loading) {
		context=_context;

		callback=(HomeListCallback) context;

		progress=new ProgressDialog(context);
		GetAPI.BASE_URL=App_Variable.getBaseAPI(context);
	}
	public HomeList(Context _context, String string) {
		context=_context;
		whichScreen=string;

		callback=(HomeListCallback) context;

		progress=new ProgressDialog(context);
		GetAPI.BASE_URL=App_Variable.getBaseAPI(context);
	}
	@Override
	protected void onPreExecute() {

		if(progress!=null){
			progress.setMessage("Please wait..");
			progress.setCanceledOnTouchOutside(false);
			progress.show();
		}
		App_Variable.hashRoomType=null;
		App_Variable.hashRoomOptions=null;
		App_Variable.hashAC=null;
		App_Variable.hashSensor=null;
		App_Variable.hashNickName=null;
		App_Variable.hashtype=null;
		App_Variable.hashListCamera=null;
		App_Variable.hashMoodType=null;
		App_Variable.hashMoodRooms=null;
		App_Variable.hashEleurl=null;
		App_Variable.hashtabname=null;
		App_Variable.vdb_video_path=null;
		App_Variable.vdb_status_path=null;

		errorString="";
		super.onPreExecute();
	}
	@Override
	protected Void doInBackground(Void... arg0) {

		HomeListParser cp=new HomeListParser(GetAPI.BASE_URL+GetAPI.HOME_URL,context);		
		//HomeListParser cp=new HomeListParser(GetAPI.HOME_DUMMY_URL,context);



		App_Variable.hashRoomType=cp.getData();
		App_Variable.hashRoomOptions=cp.getDetailData();
		App_Variable.hashAC=cp.getACData();
		App_Variable.hashSensor=cp.getSensorData();
		App_Variable.hashNickName=cp.getNickName();
		App_Variable.hashtype=cp.getType();
		//App_Variable.hashCamera=cp.getCamera();
		App_Variable.hashListCamera=cp.getListCamera();
		App_Variable.hashMoodType=cp.getListMoods();
		App_Variable.hashMoodRooms=cp.getMoodRooms();
		App_Variable.hashEleurl=cp.getEleurl();
		App_Variable.hashtabname=cp.gettabname();

		CustomLog.resume("size ac::"+App_Variable.hashAC.size());

		//CustomLog.resume("App_Variable.hashNickName:"+App_Variable.hashNickName);


		errorString=cp.getException();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub

		if(progress!=null){
			progress.dismiss();
		}
		if(!errorString.equals("")){

			callback.callExceptionPopup(errorString);
			//App_Variable.ShowErrorToast(errorString,SplashActivity.this);
		}else{

			callback.StartBGService(context,whichScreen);

			//	finish();same
			/*
			Intent intent=new Intent(SplashActivity.this,HomeActivity.class);		
			intent.putExtra("position", -1);
			intent.putExtra("room", "");
			intent.putExtra("screen", "splash");
			startActivity(intent);
			finish();*/



		}

		super.onPostExecute(result);
	}

}
