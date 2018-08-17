package com.divum.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.divum.callback.BgThreadCallback;
import com.divum.constants.GetAPI;
import com.divum.parser.BaseParser;
import com.divum.parser.HomeListParser;
import com.divum.silvan.CameraActivity;
import com.divum.silvan.R.string;

public class BackgroundThread /*implements VDBCallback,VDPCallback*/
{
	private Context context;
	private VDBSensor vb;
	private SensorStatus ss;
	private VDPStatus vp;
	private String errorString;
	private String tag="BackgroundThread";
	private BgThreadCallback callback;
	private boolean isLooping=true;
	private boolean showOnetimeCamera=false;
	private SharedPreferences storagePref,configPref;
	private boolean isOneTimeSensor=false;
	int mTimeOut=800;
	Intent intent;
	public BackgroundThread(Context _context,boolean _isLooping,Intent _intent){

		this.context=_context;
		isLooping =_isLooping;
		intent = _intent;
		callback = (BgThreadCallback) context;
		storagePref=context.getSharedPreferences("IP", context.MODE_PRIVATE);
		configPref = context.getSharedPreferences("CONFIG", context.MODE_PRIVATE);	

		App_Variable.STATUS_EMS=configPref.getString("EMS", "1");

		if(isLooping){
			if(App_Variable.STATUS_EMS.equals("1")){
				setVDPTimer();
			}

			setVDBTimer();
		}

	}

	

	private void setVDPTimer() {
		Timer timerVdp=new Timer();
		TimerTask task=new TimerTask() {
			@Override
			public void run() {
				callVDPStatus();
			}
		};

		timerVdp.schedule(task, 0, mTimeOut);

	}

	private void setVDBTimer() {
		Timer timerVdb=new Timer();
		TimerTask task=new TimerTask() {
			@Override
			public void run() {
				callVDBSensorParser();
			}
		};

		timerVdb.schedule(task, 0, mTimeOut);
	}


	public void callSensorStatus(boolean _isLooping) {

		isLooping =_isLooping;
		if(App_Variable.isNetworkAvailable(context)){
			if(ss!=null)
				ss.cancel(true);
			ss=new SensorStatus();
			ss.execute();
		}else{
			//callSensorStatus(isLooping);
		}
	}
	class SensorStatus extends AsyncTask<Void, Void, Void>{

		private String response="";
		private String mSensorUrl="/cgi-bin/scripts/atzi_web_sensorStatus.sh";
		private boolean OneTimeCall;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			App_Variable.SENSOR_STATUS=null;
			OneTimeCall=true;
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			BaseParser parser=new BaseParser(App_Variable.getBaseAPI(context)+mSensorUrl+"?"+System.currentTimeMillis());
			response=parser.getResponse();
			parser=null;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
				//CustomLog.d(tag, "SensorStatusThread:"+response);
			App_Variable.SENSOR_STATUS=response;
			/**
			 * if response =1 means goto sensor page
			 */

			if(OneTimeCall){
				callListParser();
				OneTimeCall=false;
			}

			super.onPostExecute(result);
		}

	}

	public void callVDPStatus() {
		if(App_Variable.isNetworkAvailable(context)){
			if(vp!=null)
				vp.cancel(true);
			vp=new VDPStatus();
			vp.execute();
		}else{
			//callVDPStatus();
		}

	}
	
	public String VDB_TYPE="Local";
	
	private String mStaticVDB="",mLocalVDB="",mNetWork="";
	private String getVDBURL() {
		String VdbIP="";
		SharedPreferences storagePref=context.getSharedPreferences("IP", context.MODE_PRIVATE);
		App_Variable.ipType=storagePref.getString("ipType", "None").trim();
		mStaticVDB = storagePref.getString("static_vdb", "");
		mLocalVDB = storagePref.getString("local_vdb", "");
		//	storagePref.getString("local_vdb", "").trim();
		//	storagePref.getString("static_vdb", "").trim();

		CustomLog.camera("iptype"+App_Variable.ipType);
		if(App_Variable.ipType.equalsIgnoreCase("local")){
			VdbIP = mLocalVDB;
			if(VdbIP==null || VdbIP.isEmpty()){
			//	Toast.makeText(context, "local vdb ip address empty", Toast.LENGTH_SHORT).show();
			}
		}else {
			VdbIP = mStaticVDB;
			if(VdbIP==null && VdbIP.isEmpty()){
			//	Toast.makeText(context, "static vdb ip address empty", Toast.LENGTH_SHORT).show();
			}
		}
		/*SharedPreferences storagePref=context.getSharedPreferences("IP", context.MODE_PRIVATE);
		mStaticVDB = storagePref.getString("static_vdb", "");
		mLocalVDB = storagePref.getString("local_vdb", "");
		mNetWork = storagePref.getString("network", "");
		
		if(mNetWork.equals("3g")){
			App_Variable.m3G_CONNECTED=true;
			App_Variable.mWIFI_CONNECTED=false;
		}else{
			App_Variable.m3G_CONNECTED=false;
			App_Variable.mWIFI_CONNECTED=true;
		}
		
		CustomLog.camera("ran App_Variable.m3G_CONNECTED:"+VDB_TYPE);
		if(App_Variable.m3G_CONNECTED){
			VDB_TYPE="Static";
			//CustomLog.camera("ran VDB_TYPE:"+VDB_TYPE);
			if(mStaticVDB.equals("")){
				//Toast.makeText(CameraActivity.this, "Static vdb ip address empty", Toast.LENGTH_SHORT).show();
			}else{
				//CustomLog.camera("ran mStaticVDB:"+mStaticVDB);
				VdbIP = mStaticVDB;
			}
		}else if(App_Variable.mWIFI_CONNECTED){
			if(VDB_TYPE.equalsIgnoreCase("static")){
				VdbIP=checkStaticIP(VdbIP);

			}else{
				if(mLocalVDB.equals("")){
					VdbIP=checkStaticIP(VdbIP);
				}else{
					VDB_TYPE ="Local";
					VdbIP = mLocalVDB;
				}
			}
		}*/

		return VdbIP;
	}
	
	private String checkStaticIP(String VdbIP) {
		if(mStaticVDB.equals("")){
			//Toast.makeText(CameraActivity.this, "local or static vdb ip address empty", Toast.LENGTH_SHORT).show();
		}else{
			VDB_TYPE ="Static";
			VdbIP = mStaticVDB;
		}
		return VdbIP;

	}


	class VDPStatus extends AsyncTask<Void, Void, Void>{

		private String response="";
		private String exception="";
		private String mVDPUrl="/goform/device?cmd=get";
		private String baseUrl="";
		@Override
		protected void onPreExecute() {
			//App_Variable.STATUS_EMS =null;
			baseUrl= App_Variable.getBaseAPIHost(getVDBURL());
		
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			
			//new VDPParser(context, storagePref.getString("local_ip", "").trim()+mVDPUrl);
			if(App_Variable.vdb_status_path!=null&&!App_Variable.vdb_status_path.trim().isEmpty()){
				CustomLog.camera("VdpUrl->"+baseUrl+App_Variable.vdb_status_path);
				BaseParser parser=new BaseParser(baseUrl+App_Variable.vdb_status_path);
				parser.getResponseVDPCallback(mTimeOut, context, VDB_TYPE, intent);
			}else {
				CustomLog.camera("VdpUrl->" + baseUrl + mVDPUrl);
				BaseParser parser = new BaseParser(baseUrl + mVDPUrl);
				parser.getResponseVDPCallback(mTimeOut, context, VDB_TYPE, intent);
			}
			return null;
		}
		
	}
	
	/*@Override
	public void VDPResponse(String response) {
		CustomLog.debug("VDBSTATUS1->"+response);
		*//**
		 * Only for vdb,If the 5th line has alarmin.status=1, then need to go the VDB page
		 *//*

		if(response!=null){
			response=response.trim();
			if(response.contains("alarmin.status=1")){
				if(!showOnetimeCamera){
					CustomLog.error("Camera", "VDBSTATUS1->:"+"camra inside");
					showOnetimeCamera=true;
					callback.showCameraView();
				}					
			}

			if(response.contains("alarmin.status=0")){
				showOnetimeCamera=false;
			}
		}
		
	}


	@Override
	public void VDPException(String trim) {
		// TODO Auto-generated method stub
		
	}
*/



	public void callVDBSensorParser() {
		//GetAPI.BASE_URL=storagePref.getString("local_ip", "").trim();

		if(App_Variable.isNetworkAvailable(context)){
			if(vb!=null)
				vb.cancel(true);
			vb=new VDBSensor();
			vb.execute();
		}else{
			//callVDBSensorParser();
		}
	}
	//private String mVDBStatus="";
	class VDBSensor extends AsyncTask<Void, Void, Void>{

		private String exception="";
		private String mVDBUrl="/web/data/getVDBStatus.txt";
		private String baseUrl="";


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//mVDBStatus="";
			baseUrl = App_Variable.getBaseAPI(context);
			CustomLog.sensor("sensor baseurl->:"+baseUrl);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//CustomLog.check(tag, "VDBSTATUS0->:"+(storagePref.getString("local_ip", "").trim()+mVDBUrl+"?"+System.currentTimeMillis()));
			//new VDBParser(context,storagePref.getString("local_ip", "").trim()+mVDBUrl+"?"+System.currentTimeMillis());
			BaseParser parser=new BaseParser(baseUrl+mVDBUrl+"?"+System.currentTimeMillis());
			parser.getResponseVDBCallback(mTimeOut,context,intent);
			return null;
		}

		
	}


	/*@Override
	public void VDBResponse(String mVDBStatus) {
		// TODO Auto-generated method stub
		CustomLog.e(tag, "sensor VDBSTATUS0->:"+mVDBStatus+";;;;;;");
		*//**
		 * If VDBSTATUS=1, navigate VDB page
		 * If VDBSTATUS=7, navigate Sensor page
		 * If VDBSTATUS=0, ignore
		 * 			
		 *//*

		if(mVDBStatus==null){
			mVDBStatus="0";
		}


		if(!isOneTimeSensor){
			isOneTimeSensor=true;
			if(mVDBStatus.equals("1")){
				CustomLog.show(tag, "VDBSTATUS0->:"+"camra inside");
				callback.showCameraView();
			}else if(mVDBStatus.equals("7")){
				CustomLog.show(tag, "VDBSTATUS0->:"+"inside");
				callback.ShowHomeView(true);
			}

		}

		if(mVDBStatus.equals("0")){
			isOneTimeSensor=false;
		}

	}


	@Override
	public void VDBException(String exception) {
		// TODO Auto-generated method stub
         *//**
          * throw toast
          *//*
	}
*/





	private void callListParser() {
		if(App_Variable.isNetworkAvailable(context)){
			//listOfCity=new ArrayList<CityEntity>();

			new HomeList().execute();
		}else{

			//showTyrAgain("city");
			//progress.setVisibility(View.GONE);
			Toast.makeText(context, "No Network connection", Toast.LENGTH_SHORT).show();

		}
	}



	class HomeList extends AsyncTask<Void, Void, Void>{

		private String mHomeUrl="/web/data/area.json";
		@Override
		protected void onPreExecute() {

			errorString="";
			App_Variable.hashRoomType=null;
			App_Variable.hashRoomOptions=null;
			App_Variable.hashAC=null;
			App_Variable.hashSensor=null;
			App_Variable.hashtype=null;
			App_Variable.hashtabname=null;
			App_Variable.hashEleurl=null;
			App_Variable.vdb_video_path=null;
			App_Variable.vdb_status_path=null;
			//progress.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {

			HomeListParser cp=new HomeListParser(App_Variable.getBaseAPI(context)+mHomeUrl,context);
			//	HomeListParser cp=new HomeListParser(GetAPI.HOME_DUMMY_URL,context);
			App_Variable.hashRoomType=cp.getData();
			App_Variable.hashRoomOptions=cp.getDetailData();
			App_Variable.hashAC=cp.getACData();
			App_Variable.hashSensor= cp.getSensorData();
			App_Variable.hashtype=cp.getType();
			App_Variable.hashEleurl=cp.getEleurl();
			App_Variable.hashtabname=cp.gettabname();
			CustomLog.parser("size ac::"+App_Variable.hashAC.size());
			errorString=cp.getException();
			cp=null;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//progress.setVisibility(View.GONE);
			if(!errorString.isEmpty()){
				Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
			}else{
				callback.ShowHomeView(false,"");
			}



			super.onPostExecute(result);
		}

	}

	






}

/*boolean isHomeRunning=true;
Handler mHomeListHandler = new Handler();
private void setHomeListLoopDelay() throws InterruptedException {
	new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isHomeRunning) {
				try {
					isHomeRunning=false;
					Thread.sleep(100);
					mHomeListHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							// Write your code here to update the UI.
							//CustomLog.show("BgThread after vdpstatus");

							callListParser();
						}
					});
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}).start(); 
}

boolean isVDBRunning=true;
	private Handler mVDBHandler=new Handler();
	private void setVDBDelay() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isVDBRunning) {

					isVDBRunning=false;
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mVDBHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							// Write your code here to update the UI.
							//CustomLog.show("BgThread after vdpstatus");

							callVDBSensorParser();
						}
					});

				}
			}
		}).start(); 

	}

boolean isVDPRunning=true;
	Handler mVDPHandler = new Handler();
	private void setVDPLoopDelay() throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isVDPRunning) {
					try {
						isVDPRunning=false;
						Thread.sleep(200);
						mVDPHandler.post(new Runnable() {

							@Override
							public void run() {
								// Write your code here to update the UI.
								callVDPStatus();
							}
						});
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start(); 
	}

 */
