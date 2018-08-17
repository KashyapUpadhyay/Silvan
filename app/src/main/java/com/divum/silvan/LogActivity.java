package com.divum.silvan;

import com.divum.callback.ResponseCallback;
import com.divum.constants.GetAPI;
import com.divum.parser.GetResponseAPI;
import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LogActivity extends Activity implements OnClickListener,ResponseCallback{

	private GetResponseAPI gr;
	private TextView txtLog;
	private TextView tab_sensor,tab_profile,tab_door_unlock;
	private ImageView img_tab_sensor_bottom,img_tab_profile_bottom,img_tab_door_unlock_bottom;
	private ImageView img_slider;
	private TextView txtNoLog;
	boolean logvalue=true;
	private RelativeLayout log_sensor,log_profile,log_door_unlock;

	/*	public LogActivity() {
		super(R.string.app_name);
		// TODO Auto-generated constructor stub
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_view);
		
		GetAPI.BASE_URL=App_Variable.getBaseAPI(LogActivity.this);
		

		/**
		 * Slider action
		 */
		img_slider=(ImageView)findViewById(R.id.img_slider);
		img_slider.setOnClickListener(this);

		/*
		 * set title
		 */
		TextView txt_header=(TextView)findViewById(R.id.txt_header);
		txt_header.setText("LOG");

		/*
		 * layout for log view
		 */
		log_sensor=(RelativeLayout)findViewById(R.id.log_sensor);	
		log_profile=(RelativeLayout)findViewById(R.id.log_profile);


		if(App_Variable.STATUS_SENSORS.equals("1")){
			log_sensor.setVisibility(View.VISIBLE);
			log_profile.setVisibility(View.VISIBLE);
		}else{
			log_sensor.setVisibility(View.GONE);
			log_profile.setVisibility(View.GONE);
		}

		log_door_unlock=(RelativeLayout)findViewById(R.id.log_door_unlock);

		if(App_Variable.STATUS_DOOR_UNLOCK.equals("1")){
			log_door_unlock.setVisibility(View.VISIBLE);
		}else{
			log_door_unlock.setVisibility(View.GONE);
		}
		/*
		 * event for log view
		 */
		tab_sensor=(TextView) findViewById(R.id.tab_sensor);
		tab_sensor.setOnClickListener(this);

		tab_profile=(TextView) findViewById(R.id.tab_profile);
		tab_profile.setOnClickListener(this);

		tab_door_unlock=(TextView) findViewById(R.id.tab_door_unlock);
		tab_door_unlock.setOnClickListener(this);

		/*
		 * set bottom view for each tab
		 */
		img_tab_sensor_bottom=(ImageView) findViewById(R.id.img_tab_sensor_bottom);
		img_tab_sensor_bottom.setVisibility(View.VISIBLE);

		img_tab_profile_bottom=(ImageView) findViewById(R.id.img_tab_profile_bottom);
		img_tab_profile_bottom.setVisibility(View.GONE);

		img_tab_door_unlock_bottom=(ImageView) findViewById(R.id.img_tab_door_unlock_bottom);
		img_tab_door_unlock_bottom.setVisibility(View.GONE);


		txtNoLog =(TextView) findViewById(R.id.txtNoLog);
		txtNoLog.setVisibility(View.GONE);

		txtLog=(TextView)findViewById(R.id.txtLog);
		if(App_Variable.STATUS_SENSORS.equals("1")){
			callAPI(GetAPI.BASE_URL+GetAPI.LOG_SENSOR+"?"+System.currentTimeMillis());
		}else if(App_Variable.STATUS_DOOR_UNLOCK.equals("1")){
			callAPI(GetAPI.BASE_URL+GetAPI.LOG_DOOR_UNLOCK+"?"+System.currentTimeMillis());
		}


	}

	@Override
	protected void onPause() {
		App_Variable.appMinimize=0;
		CustomLog.show("Log", "pause:"+App_Variable.appMinimize);
		SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
		boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);
		if(checkvalue) {

			if(logvalue){
	         Intent intent = new Intent(this, UnlockActivity.class);
	         startActivity(intent);
             }
		  }
		super.onPause();
	}

	@Override
	protected void onResume() {
		App_Variable.appMinimize=1;
		CustomLog.show("Log", "resume:"+App_Variable.appMinimize);

		super.onResume();
	}
	
	/*@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		CustomLog.latest("onUserLeaveHint");
		App_Variable.pauseStage=1;
		super.onUserLeaveHint();
	}*/

	private void callAPI(String url) {
		if(App_Variable.isNetworkAvailable(LogActivity.this)){
			if(gr!=null)
				gr.cancel(true);
			gr=new GetResponseAPI(this,true);
			gr.execute(url);
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}

	public void setResponse(String response,String exception) {
		if(!exception.equals(""))
			App_Variable.ShowErrorToast(exception, LogActivity.this);
		else{
			if(response!=null){
				if(response.trim().equals("")){
					txtNoLog.setVisibility(View.VISIBLE);
				}else{
					txtLog.setText(response);
					txtLog.setVisibility(View.VISIBLE);
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_sensor:
			img_tab_door_unlock_bottom.setVisibility(View.GONE);
			img_tab_profile_bottom.setVisibility(View.GONE);
			img_tab_sensor_bottom.setVisibility(View.VISIBLE);
			txtNoLog.setVisibility(View.GONE);

			callAPI(GetAPI.BASE_URL+GetAPI.LOG_SENSOR+"?"+System.currentTimeMillis());
			break;
		case R.id.tab_profile:
			img_tab_door_unlock_bottom.setVisibility(View.GONE);
			img_tab_profile_bottom.setVisibility(View.VISIBLE);
			img_tab_sensor_bottom.setVisibility(View.GONE);
			txtNoLog.setVisibility(View.GONE);

			callAPI(GetAPI.BASE_URL+GetAPI.LOG_PROFILE+"?"+System.currentTimeMillis());
			break;
		case R.id.tab_door_unlock:
			img_tab_door_unlock_bottom.setVisibility(View.VISIBLE);
			img_tab_profile_bottom.setVisibility(View.GONE);
			img_tab_sensor_bottom.setVisibility(View.GONE);
			txtNoLog.setVisibility(View.GONE);

			callAPI(GetAPI.BASE_URL+GetAPI.LOG_DOOR_UNLOCK+"?"+System.currentTimeMillis());
			break;
		case R.id.img_slider:
			//			if(sm!=null)
			//				sm.toggle();

			/*Intent intent=new Intent(LogActivity.this,HomeActivity.class);		
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("position", -1);
			intent.putExtra("room", "");
			intent.putExtra("screen", "others");
			startActivity(intent);*/
			logvalue=false;
			finish();


			break;
		default:
			break;
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event){
		logvalue=false;
		moveTaskToBack(false);
		finish();
		return super.onKeyDown(keyCode, event);
	}


}
