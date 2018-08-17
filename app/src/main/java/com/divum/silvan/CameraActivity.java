package com.divum.silvan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.divum.callback.HomeListCallback;
import com.divum.callback.PingCallback;
import com.divum.constants.GetAPI;
import com.divum.entity.HomeEntity;
import com.divum.mjpeg.MjpegInputStream;
import com.divum.mjpeg.MjpegView;
import com.divum.utils.App_Variable;
import com.divum.utils.CommonPingAuthCallBackURL;
import com.divum.utils.CommonPingAuthURL;
import com.divum.utils.CommonPingURL;
import com.divum.utils.CustomLog;
import com.divum.utils.HomeList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;

public class CameraActivity extends Activity implements OnClickListener,HomeListCallback,PingCallback{

	private TextView tab_door_phone;
	private TextView tab_survillence;
	private RelativeLayout layout_door_phone,layout_survillence;
	private ImageView img_tab_door_phone;
	private View img_tab_survillence;
	private LinearLayout camera_door_phone;
	private LinearLayout camera_survillence;
	private ImageView img_slider;
	private MjpegView videoview;
	private ProgressDialog dialog;
	private TextView txtWatch,txtDoorUnlock1;//txtCancel
	private TextView txtAnswer,txtDoorUnlock2;//txtReject
	private LinearLayout layoutWatch,layoutAnswer;

	private DoRead dr;
	//private ArrayList<String> list_rooms=new ArrayList<String>();
	private LinearLayout layout_camera_room;
	private String section="door";
	private MjpegView video_survillence_camera;
	private int screenIndex=0;
	private String screen="camera";
	private int vdbTrigger=0;
	private boolean isAnswer;

	private String urlStream="";
	private HorizontalScrollView scroll_camera_room;
	boolean isTimerRunning=true;
	public String VDB_TYPE="Local";
	//	public CameraActivity() {
	//		super(R.string.app_name);
	//		// TODO Auto-generated constructor stub
	//	}

	private long time = 10000;

	private Handler mTimoutHandler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.camera_view);
		App_Variable.getConfigData(CameraActivity.this);
		App_Variable.appMinimize=1;

		screenIndex =getIntent().getIntExtra("page", 0);
		screen=getIntent().getStringExtra("screen");
		vdbTrigger=getIntent().getIntExtra("vdb_trigger", 0);
		CustomLog.camera("vdbTrigger:"+vdbTrigger);




		dialog =new ProgressDialog(this);
		mTimoutHandler = new Handler();

		//SharedPreferences storagePref=getSharedPreferences("IP", MODE_PRIVATE);


		//urlStream=GetAPI.BASE_URL+":84/goform/stream?cmd=get&channel=0";
		/**
		 * Slider action
		 */
		img_slider=(ImageView)findViewById(R.id.img_slider);
		img_slider.setOnClickListener(this);

		/*
		 * set title
		 */
		TextView txt_header=(TextView)findViewById(R.id.txt_header);
		if(screen.equals("camera")){
			txt_header.setText("CAMERA");
		}else{
			txt_header.setText("VDB");
		}

		/*
		 * tab for camera view
		 */
		tab_door_phone=(TextView) findViewById(R.id.tab_door_phone);
		tab_door_phone.setOnClickListener(this);

		tab_survillence=(TextView) findViewById(R.id.tab_survillence);
		tab_survillence.setOnClickListener(this);

		layout_door_phone=(RelativeLayout) findViewById(R.id.layout_door_phone);
		layout_door_phone.setVisibility(View.GONE);

		layout_survillence=(RelativeLayout) findViewById(R.id.layout_survillence);
		layout_survillence.setVisibility(View.GONE);

		if(screen.equals("camera")){
			layout_survillence.setVisibility(View.VISIBLE);
		}else{
			layout_door_phone.setVisibility(View.VISIBLE);
		}

		/*
		 * set bottom view for each tab
		 */
		img_tab_door_phone=(ImageView) findViewById(R.id.img_tab_door_phone);
		img_tab_door_phone.setVisibility(View.VISIBLE);

		img_tab_survillence=(ImageView) findViewById(R.id.img_tab_survillence);
		img_tab_survillence.setVisibility(View.VISIBLE);

		/*
		 * set view for door phone
		 */
		camera_door_phone=(LinearLayout)findViewById(R.id.camera_door_phone);
		camera_door_phone.setVisibility(View.GONE);

		/*
		 * set view for camera survillence
		 */
		camera_survillence=(LinearLayout)findViewById(R.id.camera_survillence);
		camera_survillence.setVisibility(View.GONE);

		if(screen.equals("camera")){
			camera_survillence.setVisibility(View.VISIBLE);
		}else{
			camera_door_phone.setVisibility(View.VISIBLE);
		}

		/*
		 * set depends on screenIndex watch and answer buttons
		 */
		layoutWatch=(LinearLayout) findViewById(R.id.layoutWatch);
		layoutAnswer=(LinearLayout) findViewById(R.id.layoutAnswer);

		layoutWatch.setVisibility(View.GONE);
		layoutAnswer.setVisibility(View.GONE);

		if(screenIndex==0)
			layoutWatch.setVisibility(View.VISIBLE);
		else
			layoutAnswer.setVisibility(View.VISIBLE);


		scroll_camera_room=(HorizontalScrollView) findViewById(R.id.scroll_camera_room);

		/*
		 * create instance for watch and cancel
		 */

		txtDoorUnlock1 =(TextView) findViewById(R.id.txtDoorUnlock1);
		txtDoorUnlock1.setOnClickListener(this);

		if(App_Variable.STATUS_DOOR_UNLOCK.equals("1")){
			txtDoorUnlock1.setVisibility(View.VISIBLE);
		}else{
			txtDoorUnlock1.setVisibility(View.GONE);
		}

		txtWatch =(TextView) findViewById(R.id.txtWatch);
		txtWatch.setOnClickListener(this);

		//txtCancel =(TextView) findViewById(R.id.txtCancel1);
		//txtCancel.setOnClickListener(this);


		/*
		 * create instance for answer and reject
		 */
		txtDoorUnlock2 =(TextView) findViewById(R.id.txtDoorUnlock2);
		txtDoorUnlock2.setOnClickListener(this);
		if(App_Variable.STATUS_DOOR_UNLOCK.equals("1")){
			txtDoorUnlock2.setVisibility(View.VISIBLE);
		}else{
			txtDoorUnlock2.setVisibility(View.GONE);
		}

		txtAnswer =(TextView) findViewById(R.id.txtAnswer);
		txtAnswer.setOnClickListener(this);

		//txtReject =(TextView) findViewById(R.id.txtReject);
		//txtReject.setOnClickListener(this);



		/*
		 * set video view and start live stream
		 */
		videoview=(MjpegView)findViewById(R.id.video_camera);
		video_survillence_camera=(MjpegView)findViewById(R.id.video_survillence_camera);
		CustomLog.camera("screenIndex camera page hashListCamera:"+App_Variable.hashListCamera);




		//callLiveStreamParser();

		//videoview.setSource(MjpegInputStream.read(GetAPI.BASE_URL+GetAPI.CAMERA_LIVE_STREAM));

	}

	String VdpBaseUrl="";

	@Override
	protected void onResume() {
		App_Variable.appMinimize=1;
		GetAPI.BASE_URL=App_Variable.getBaseAPI(CameraActivity.this);

		if(screen.equals("camera")){
			if(App_Variable.hashListCamera==null){
				//screenIndex=1;
				callHomeList();
			}else{
				//screenIndex=0;
				ShowRooms();
			}
		}else{
			VdpBaseUrl=getVDBURL();
			VdpBaseUrl=App_Variable.getBaseAPIHost(VdpBaseUrl);
			CustomLog.resume("vdb_video_path" + App_Variable.vdb_video_path);
			CustomLog.resume("vdb_Status_path"+App_Variable.vdb_status_path);
			if(App_Variable.vdb_video_path!=null&&!App_Variable.vdb_video_path.trim().isEmpty())
			{
				urlStream=VdpBaseUrl+App_Variable.vdb_video_path;
			}
			else {
				urlStream = VdpBaseUrl + GetAPI.CAMERA_LIVE_STREAM;
			}

			CustomLog.camera("urlStream:" + urlStream);

			if(App_Variable.hashListCamera==null){
				//screenIndex=1;
				callHomeList();
			}else{
				//screenIndex=0;
				ShowRooms();
				section ="door";
				callLiveStreamParser(urlStream);
			}
		}


		CustomLog.camera("camera resume"+App_Variable.appMinimize);
		super.onResume();
	}



	private String getVDBURL() {
		//new thing
		String VdbIP="";
		SharedPreferences storagePref=getSharedPreferences("IP", MODE_PRIVATE);
		App_Variable.ipType=storagePref.getString("ipType", "None").trim();
	//	storagePref.getString("local_vdb", "").trim();
	//	storagePref.getString("static_vdb", "").trim();

		CustomLog.camera("iptype"+App_Variable.ipType);
		if(App_Variable.ipType.equalsIgnoreCase("local")){
			VdbIP = GetAPI.BASE_LOCAL_VDB;
			if(VdbIP==null || VdbIP.isEmpty()){
				Toast.makeText(CameraActivity.this, "local vdb ip address empty", Toast.LENGTH_SHORT).show();
			}
		}else {
			VdbIP = GetAPI.BASE_STATIC_VDB;
			if(VdbIP==null && VdbIP.isEmpty()){
				Toast.makeText(CameraActivity.this, "static vdb ip address empty", Toast.LENGTH_SHORT).show();
			}
		}
		/*if(App_Variable.m3G_CONNECTED){
			VDB_TYPE ="Static";
			if(GetAPI.BASE_STATIC_VDB.equals("")){
				Toast.makeText(CameraActivity.this, "Static vdb ip address empty", Toast.LENGTH_SHORT).show();
			}else{
				VdbIP = GetAPI.BASE_STATIC_VDB;
			}
		}else if(App_Variable.mWIFI_CONNECTED){
			if(VDB_TYPE.equalsIgnoreCase("static")){
				VdbIP=checkStaticIP(VdbIP);

			}else{
				if(GetAPI.BASE_LOCAL_VDB.equals("")){
					VdbIP=checkStaticIP(VdbIP);
				}else{
					VDB_TYPE ="Local";
					VdbIP = GetAPI.BASE_LOCAL_VDB;
				}
			}
		}*/

		return VdbIP;
	}



	private String checkStaticIP(String VdbIP) {
		if(GetAPI.BASE_STATIC_VDB.equals("")){
			Toast.makeText(CameraActivity.this, "local or static vdb ip address empty", Toast.LENGTH_SHORT).show();
		}else{
			VDB_TYPE ="Static";
			VdbIP = GetAPI.BASE_STATIC_VDB;
		}
		return VdbIP;

	}



	private void callHomeList() {
		if(App_Variable.isNetworkAvailable(CameraActivity.this)){

			HomeList home=new HomeList(CameraActivity.this);
			home.execute();
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}

	public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			CustomLog.camera("DoRead");
			dialog.setMessage("Please wait while camera loading...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected MjpegInputStream doInBackground(String... url) {
			HttpResponse res;
			DefaultHttpClient httpclient = new DefaultHttpClient();  
			CustomLog.camera("camera mjpg url->"+url[0]);
			try {
				CustomLog.camera("mjpg url->1");
				HttpGet httpget = new HttpGet(url[0]); 
				CustomLog.camera("mjpg url->2");
				httpget.setURI(new URI(url[0]));	
				CustomLog.camera("mjpg url->3");
				res = httpclient.execute(httpget);
				CustomLog.camera("mjpg url->4");
				return new MjpegInputStream(res.getEntity().getContent());              
			} catch (ClientProtocolException e) {
				CustomLog.camera("camera ClientProtocolException:"+e);
			} catch (IOException e) {       
				CustomLog.camera("camera IOException:"+e);
			} catch (Exception e) {
				CustomLog.camera("camera Exception:"+e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(MjpegInputStream result) {
			// TODO Auto-generated method stub
			CustomLog.camera("camera onPostExecute:"+result);
			if(result == null){
				if(screen.equals("camera")){
					Toast.makeText(CameraActivity.this, "Camera is not playing..", Toast.LENGTH_SHORT).show();
				}else{
					if(VDB_TYPE.equalsIgnoreCase("static")){
						Toast.makeText(CameraActivity.this, "Camera is not playing, So please check your VDB IP Address", Toast.LENGTH_SHORT).show();
					}else{
						//App_Variable.ShowErrorToast("Local VDB IP not working, so switiching to Static VDB IP", CameraActivity.this);
						VDB_TYPE="Static";
						VdpBaseUrl=getVDBURL();
						VdpBaseUrl=App_Variable.getBaseAPIHost(VdpBaseUrl);
						if(App_Variable.vdb_video_path!=null&&!App_Variable.vdb_video_path.trim().isEmpty()){
							urlStream=VdpBaseUrl+App_Variable.vdb_video_path;
						}
						else {
							urlStream = VdpBaseUrl + GetAPI.CAMERA_LIVE_STREAM;
						}
						callLiveStreamParser(urlStream);
					}
				}
			}
			//System.out.println("door ranjith:"+section);
			if(section.equals("door")){

				videoview.stopThread();	
				videoview.init(CameraActivity.this);
				//videoview.stt
				videoview.credential("admin","admin");
				videoview.setSource(result);
				videoview.setDisplayMode(MjpegView.SIZE_BEST_FIT);
				videoview.showFps(false);
			}else{
				//				video_survillence_camera.stopThread();	
				//				video_survillence_camera.init(CameraActivity.this);

				video_survillence_camera.setSource(result);
				video_survillence_camera.setDisplayMode(MjpegView.SIZE_BEST_FIT);
				video_survillence_camera.showFps(false);
			}
			dialog.dismiss();

			if(vdbTrigger==1){
				try {
					isTimerRunning=true;
					setTimeOut();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			super.onPostExecute(result);
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		App_Variable.appMinimize=0;
		CustomLog.camera("camera pause"+App_Variable.appMinimize);
		isTimerRunning=false;
		if(videoview!=null)
			videoview.stopPlayback();

		if(video_survillence_camera!=null)
			video_survillence_camera.stopPlayback();
		super.onPause();
	}

	private void callLiveStreamParser(String url) {
		CustomLog.camera("callLiveStreamParser1");
		if(App_Variable.isNetworkAvailable(CameraActivity.this)){
			CustomLog.camera("callLiveStreamParser2");
			if(dr!=null)
				dr.cancel(true);
			CustomLog.camera("callLiveStreamParser3");
			dr=new DoRead();
			dr.execute(url);
		}else{
			dialog.dismiss();
			App_Variable.ShowNoNetwork(this);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		/*case R.id.tab_door_phone:

			section ="door";
			img_tab_door_phone.setVisibility(View.VISIBLE);
			img_tab_survillence.setVisibility(View.GONE);

			camera_door_phone.setVisibility(View.VISIBLE);
			camera_survillence.setVisibility(View.GONE);

			video_survillence_camera.stopThread();	
			video_survillence_camera.setVisibility(View.GONE);

			videoview.stopThread();	
			videoview.init(CameraActivity.this);
			videoview=(MjpegView)findViewById(R.id.video_camera);
			videoview.setVisibility(View.VISIBLE);

			callLiveStreamParser(urlStream);

			//videoview.cameraFps(true);

			break;
		case R.id.tab_survillence:
			section ="survillence";
			img_tab_door_phone.setVisibility(View.GONE);
			img_tab_survillence.setVisibility(View.VISIBLE);

			camera_door_phone.setVisibility(View.GONE);
			camera_survillence.setVisibility(View.VISIBLE);


			videoview.setVisibility(View.GONE);
			video_survillence_camera.setVisibility(View.VISIBLE);


			scroll_camera_room.scrollTo(0, 0);

			break;*/
		case R.id.img_slider:
			//			if(sm!=null)
			//				sm.toggle();
			CustomLog.camera("screenIndex:"+screenIndex);
			if(videoview!=null){
				videoview.stopThread();
				videoview.DestroyThread();
			}
			if(video_survillence_camera!=null){
				video_survillence_camera.stopThread();
				video_survillence_camera.DestroyThread();
			}
			if(dr!=null){
				dr.cancel(true);
			}

			if(screenIndex==0){
				finish();
			}else{
				ShowHomeView();
			}

			break;

		case R.id.txtDoorUnlock1:
			callDoorUnlockPopup();
			break;

		case R.id.txtDoorUnlock2:
			callDoorUnlockPopup();
			break;

		case R.id.txtWatch:
			isAnswer=false;
			time=14000;
			if(txtWatch.getText().equals("Watch")){
				Toast.makeText(this,"VDB call watching",Toast.LENGTH_LONG).show();
				txtWatch.setText("Reject");
				isTimerRunning=true;
				PingAuthCallBackURL(VdpBaseUrl+GetAPI.CAMERA_WATCH,true);
			}else{
				//txtWatch.setText("Watch");
				PingAuthURL(VdpBaseUrl+GetAPI.CAMERA_REJECT,false);
				isTimerRunning=false;
				Toast.makeText(this,"VDB call Rejected",Toast.LENGTH_LONG).show();
				ShowHomeView();
			}
			//11-07 09:21:35.929: I/System.out(29145): getStartOfSequence end:80


			//callLiveStreamParser(urlStream);

			//layoutWatch.setVisibility(View.GONE);
			//layoutAnswer.setVisibility(View.VISIBLE);
			break;

			/*case R.id.txtCancel1:
			PingAuthURL(GetAPI.BASE_URL+GetAPI.CAMERA_REJECT,false);
			ShowHomeView();
			//callLiveStreamParser();
			//ShowHomeView();

			break;*/

		case R.id.txtAnswer:

			isAnswer=true;
			if(txtAnswer.getText().equals("Answer")){
				Toast.makeText(this,"VDB call Accepted",Toast.LENGTH_LONG).show();
				txtAnswer.setText("Reject");
				PingAuthURL(VdpBaseUrl+GetAPI.CAMERA_ANSWER,false);
				time =90000;
				mTimoutHandler.removeCallbacks(r);
				CustomLog.camera("ranjith vdbTrigger :time start:"+time+"ms");
				mTimoutHandler.postDelayed(r, time);
			}else{
				//txtAnswer.setText("Answer");
				PingAuthURL(VdpBaseUrl+GetAPI.CAMERA_REJECT,false);
				isTimerRunning=false;
				Toast.makeText(this,"VDB call Rejected",Toast.LENGTH_LONG).show();
				ShowHomeView();
			}
			//callLiveStreamParser();
			break;

			/*case R.id.txtReject:
			PingAuthURL(GetAPI.BASE_URL+GetAPI.CAMERA_REJECT,false);
			ShowHomeView();
			break;*/

		default:
			break;
		}


	}

	private void ShowHomeView() {
		if(screenIndex==0)
			finish();
		else
			CallHomePage();
	}

	private void ShowRooms() {

		CustomLog.camera("ShowRooms1");

		layout_camera_room=(LinearLayout) findViewById(R.id.layout_camera_room);
		layout_camera_room.removeAllViewsInLayout();

		int size=App_Variable.hashListCamera.size();
		for(int i=0;i<size;i++){
			final HomeEntity entity=App_Variable.hashListCamera.get(i);

			View vi=getLayoutInflater().inflate(R.layout.camera_room_view, null);
			TextView txt_camera_room=(TextView) vi.findViewById(R.id.txt_camera_room);
			txt_camera_room.setText(entity.getCameraRoom());
			//vi.setTag(list_rooms.get(i));
			vi.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					//String rooms=(String)v.getTag();
					//CustomLog.camera("rooms:"+rooms);	

					//video_survillence_camera.stopThread();
					video_survillence_camera.stopThread();	
					video_survillence_camera.init(CameraActivity.this);

					video_survillence_camera=(MjpegView)findViewById(R.id.video_survillence_camera);
					video_survillence_camera.setVisibility(View.VISIBLE);

					CustomLog.latest("camera url:" + entity.getLocalUrl());

					section ="room_camera";
					if(App_Variable.ipType.equals("local")) {
						callLiveStreamParser(entity.getLocalUrl());
						//System.out.println("camera url" + entity.getLocalUrl());
					}else {
						callLiveStreamParser(entity.getExtUrl());
						//System.out.println("camera url" + entity.getExtUrl());
					}


				}
			});

			layout_camera_room.addView(vi);

		}
		CustomLog.camera("ShowRooms2");

	}

	/**
	 * Popup contains data like Please select room
	 */
	private void callDoorUnlockPopup(){
		AlertDialog alertDialog=new AlertDialog.Builder(this)
		.setMessage("You have opted for door unlock.\nWould you like to continue?")
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {                   
			public void onClick(DialogInterface dialog, int which) {
				callDoorUnlockParser();
				dialog.dismiss();	

			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {                   
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();	

			}
		})
		.create();  
		alertDialog.setCancelable(false); 
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}

	protected void callDoorUnlockParser() {
		if(App_Variable.isNetworkAvailable(CameraActivity.this)){
			CommonPingURL puc=new CommonPingURL("others",CameraActivity.this);
			puc.execute(GetAPI.BASE_URL+GetAPI.CAMERA_DOOR_UNLOCK);
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}

	private void CallHomePage() {
		Intent intentHome=new Intent(CameraActivity.this,HomeActivity.class);		
		intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intentHome.putExtra("position", -1);
		intentHome.putExtra("room", "");
		intentHome.putExtra("screen", "others");
		startActivity(intentHome);
		finish();

	}

	protected void PingURL(String activateURL) {
		if(App_Variable.isNetworkAvailable(CameraActivity.this)){
			CommonPingURL puc=new CommonPingURL("others",CameraActivity.this);
			puc.execute(activateURL);
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}
	protected void PingAuthURL(String activateURL,boolean loading) {
		if(App_Variable.isNetworkAvailable(CameraActivity.this)){
			CommonPingAuthURL puc=null;
			if(loading){
				puc=new CommonPingAuthURL("camera",CameraActivity.this);
			}else{
				puc=new CommonPingAuthURL("others",CameraActivity.this);
			}
			puc.execute(activateURL);
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}
	CommonPingAuthCallBackURL puc=null;
	protected void PingAuthCallBackURL(String activateURL,boolean loading) {
		if(App_Variable.isNetworkAvailable(CameraActivity.this)){
			if(puc!=null){
				puc.cancel(true);
				puc=null;
			}
			CustomLog.camera("cameraUrl->"+activateURL);
			if(loading){
				puc=new CommonPingAuthCallBackURL("camera",CameraActivity.this);
			}else{
				puc=new CommonPingAuthCallBackURL("others",CameraActivity.this);
			}
			puc.execute(activateURL);
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}
	/**
	 * @screenIndex, if 1 means call Home activity otherwise finish current activity
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		CustomLog.camera("screenIndex onKeyDown back::"+screenIndex);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			CustomLog.camera("ranjith time detail back:"+screenIndex);

			if(videoview!=null){
				videoview.stopThread();
				videoview.DestroyThread();
			}
			if(video_survillence_camera!=null){
				video_survillence_camera.stopThread();
				video_survillence_camera.DestroyThread();
			}
			if(dr!=null){
				dr.cancel(true);
			}
			if(screenIndex==0){
				finish();
			}else
			{
				CallHomePage();
			}

			//finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void callExceptionPopup(String errorString) {
		App_Variable.ShowErrorToast(errorString, CameraActivity.this);

	}

	@Override
	public void StartBGService(Context context,String whichSection) {
		ShowRooms();
		section ="door";
		callLiveStreamParser(urlStream);
	}


	Runnable r = new Runnable() {
		@Override
		public void run() {	
			if(isTimerRunning){
				if(screenIndex==0){
					finish();
				}else{
					ShowHomeView();
				}
			}

			isTimerRunning=false;
		}
	};
	public void setTimeOut() throws InterruptedException {

		CustomLog.camera("ranjith vdbTrigger :time start:"+time+"ms");

		mTimoutHandler.postDelayed(r, time);
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isTimerRunning) {
					try {

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start(); */
	}



	@Override
	public void startTimeOut() {
		try {
			setTimeOut();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
