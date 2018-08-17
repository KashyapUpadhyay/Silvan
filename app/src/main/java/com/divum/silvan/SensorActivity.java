package com.divum.silvan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.divum.adapter.SensorAdapter;
import com.divum.callback.ResponseCallback;
import com.divum.callback.SensorCallback;
import com.divum.constants.GetAPI;
import com.divum.customview.ExpandableHeightGridView;
import com.divum.parser.BaseParser;
import com.divum.parser.GetResponseAPI;
import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SensorActivity extends Activity implements OnClickListener, ResponseCallback,SensorCallback{

	private String tag="SensorActivity";
	private ExpandableHeightGridView gridview_sensor;
	private SensorStatus ss;
	private ProgressDialog progress;
	private ImageView img_slider;
	private TextView txtAck;
	String[] aryKey;

	private GetResponseAPI gr;
	private SensorAdapter adapter;
	private boolean loadingVisible=true,sensorLoading=true;
	private String[] sensorResponse;
	private int screenIndex =-1;
	private int noOfCol=4;
	private TextView txtNoSensor;
	//private SharedPreferences storagePref;
    boolean sensorvalue=true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_status_view);
		App_Variable.getConfigData(SensorActivity.this);

		App_Variable.isSensorPage=true;

		CustomLog.debug("11111111");
		App_Variable.appMinimize=1;
		//storagePref=SensorActivity.this.getSharedPreferences("IP", SensorActivity.this.MODE_PRIVATE);
		GetAPI.BASE_URL=App_Variable.getBaseAPI(SensorActivity.this);
		/**
		 * Slider action
		 */
		img_slider=(ImageView)findViewById(R.id.img_slider);
		img_slider.setOnClickListener(this);

		/*
		 * set title
		 */
		TextView txt_header=(TextView)findViewById(R.id.txt_header);
		txt_header.setText("Sensor Status");

		/*
		 * set acknowledge
		 */
		txtAck=(TextView) findViewById(R.id.txtAck);
		txtAck.setOnClickListener(this);
		/*if(App_Variable.hashSensor.size()==0){
			txtAck.setVisibility(View.GONE);
		}else{
			txtAck.setVisibility(View.VISIBLE);
		}*/

		progress = new ProgressDialog(SensorActivity.this);
		progress.setMessage("Loading..");
		progress.setCanceledOnTouchOutside(false);

		gridview_sensor=(ExpandableHeightGridView)findViewById(R.id.gridview_sensor);

		txtNoSensor=(TextView) findViewById(R.id.txtNoSensor);
		txtNoSensor.setVisibility(View.GONE);


		/* check if coming from starting itself or normal way */
		Intent intent =getIntent();
		if(intent!=null){
			screenIndex=intent.getIntExtra("section", -1);
		}

		if(App_Variable.hashSensor!=null) {
			int sensorAreaSize = App_Variable.hashSensor.size();
			aryKey = new String[sensorAreaSize];
			Set entrySet = App_Variable.hashSensor.entrySet();
			Iterator iterator = entrySet.iterator();
			int count = 0;
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				//CustomLog.debug("key ac:"+entry.getKey()+","+entry.getValue());
				aryKey[count] = entry.getKey() + "";
				count++;

			}
		}


		CustomLog.show("screenIndex:"+screenIndex+","+App_Variable.hashSensor);

		if(App_Variable.hashSensor!=null){
			if(screenIndex==-1){
				if(App_Variable.hashSensor.size()==0){
					txtNoSensor.setVisibility(View.VISIBLE);
					txtAck.setVisibility(View.GONE);
				}else{
					txtAck.setVisibility(View.VISIBLE);
					callSensorStatus();
				}
			}
			else{
				if(App_Variable.hashSensor.size()==0){
					txtNoSensor.setVisibility(View.VISIBLE);
					txtAck.setVisibility(View.GONE);
				}else{
					txtAck.setVisibility(View.VISIBLE);
					ShowStatus(App_Variable.SENSOR_STATUS);
				}
			}
		}else{
			callSensorStatus();
		}


	}

	Handler mHandler = new Handler();
	private boolean firstTime=true;;

	public void CallTimer() throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (sensorLoading) {
					try {
						Thread.sleep(500);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// Write your code here to update the UI.
								callSensorStatus();
							}
						});
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start(); 
	}

	public void callSensorStatus() {
		if(App_Variable.isNetworkAvailable(SensorActivity.this)){
			if(ss!=null)
				ss.cancel(true);
			ss=new SensorStatus();
			ss.execute();
		}else{
			App_Variable.ShowNoNetwork(SensorActivity.this);
			sensorLoading=false;
		}
	}



	@Override
	protected void onPause() {
		App_Variable.appMinimize=0;
		CustomLog.show(tag, "pause:"+App_Variable.appMinimize);
		App_Variable.isSensorPage=false;

		CustomLog.debug("pause");
		if(ss!=null)
			ss.cancel(true);

		sensorLoading=false;
		SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
		boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);
		if(checkvalue) {

			if(sensorvalue) {
			Intent intent = new Intent(this, UnlockActivity.class);
			startActivity(intent);
		}}
		super.onPause();
	}

	@Override
	protected void onResume() {
		CustomLog.debug("resume");
		App_Variable.isSensorPage=true;
		App_Variable.appMinimize=1;
		CustomLog.show(tag, "resume:"+App_Variable.appMinimize);
		//sensorvalue=false;
		sensorLoading=true;
		try {
			if(App_Variable.hashSensor.size()>0){
				CallTimer();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onResume();
	}


	class SensorStatus extends AsyncTask<Void, Void, Void>{

		private String exception="";
		private String statusUrl="/cgi-bin/scripts/atzi_web_sensorStatus.sh";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			if(loadingVisible)
				progress.show();

		}

		//private String response="";
		@Override
		protected Void doInBackground(Void... params) {

			BaseParser parser=new BaseParser(App_Variable.getBaseAPI(SensorActivity.this)+statusUrl+"?"+System.currentTimeMillis());
			parser.getResponseSensorCallback(2000, SensorActivity.this);
			//			response=parser.getResponse();
			//			exception=parser.getException().trim();
			return null;
		}

		/*@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if(!exception.equals("")){
				App_Variable.ShowErrorToast(exception, SensorActivity.this);
				progress.dismiss();
			}else{
				progress.dismiss();
				CustomLog.show(tag, "SensorStatus:"+response);

				ShowStatus(response);
			}


			super.onPostExecute(result);
		}*/

	}

	@Override
	public void SensorStatusResponse(final String response) {

		CustomLog.show(tag, "SensorStatus:" + response);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				progress.dismiss();
				System.out.println("sensor Api response:" + response);
				//App_Variable.ShowErrorToast(response, SensorActivity.this);
				ShowStatus(response);
			}
		});

	}

	@Override
	public void SensorStatusException(final String exception) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				App_Variable.ShowErrorToast(exception, SensorActivity.this);
			}
		});
		progress.dismiss();

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_slider:
			//			if(sm!=null)
			//				sm.toggle();
			if(screenIndex==-1){
				sensorvalue=false;
				finish();
			}else{
				sensorvalue=false;
				Intent intentHome=new Intent(SensorActivity.this,HomeActivity.class);
				intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intentHome.putExtra("position", -1);
				intentHome.putExtra("room", "");
				intentHome.putExtra("radiant_value",false);
				intentHome.putExtra("screen", "others");
				startActivity(intentHome);
				finish();
			}
			break;
		case R.id.txtAck:
			/*
			 * If any of the sensor is in blue color, those sensors to be made to normal state. 
			 * So we will find blue color sensor status(=2).
			 * Then appending those sensor to acknowledge api.
			 */

			String blueStatus="";
			int size=sensorResponse.length;
			for (int i = 0; i < size; i++) {
				if(sensorResponse[i].endsWith("2")){
					String[] split=sensorResponse[i].split("=");
					blueStatus+=split[0]+"+";
				}
			}
			CustomLog.debug("blue==="+blueStatus);


			callAPI(GetAPI.BASE_URL+GetAPI.SENSOR_ACK+"+"+blueStatus);
			break;

		default:
			break;
		}

	}
	static int myLastVisiblePos=1;
	@SuppressWarnings("null")
	public void ShowStatus(String response) {
		txtNoSensor.setVisibility(View.GONE);
		if(response!=null&&!response.isEmpty()){
			App_Variable.SENSOR_STATUS=response;
			sensorResponse=response.split("\n");
			CustomLog.show(tag, "SensorStatus:"+sensorResponse.length);
			//adapter=null;

			if(sensorResponse.length==0){
				txtNoSensor.setVisibility(View.VISIBLE);
			}
			sensorResponse=checkAnyNullStatus();
			//CustomLog.show(tag,"sensorresponse array"+sensorResponse);
			//System.out.println("sensorresponse array"+sensorResponse);

			if(firstTime){
				if(App_Variable.hashSensor.size()>0) {
					adapter = new SensorAdapter(SensorActivity.this, sensorResponse, aryKey);
					gridview_sensor.setAdapter(adapter);
					firstTime = false;
				}
			}else{
				adapter.setResponse(sensorResponse,aryKey);
			}
			//myLastVisiblePos = gridview_sensor.getFirstVisiblePosition();
			CustomLog.check("myLastVisiblePos:" + myLastVisiblePos);

		}else{
			txtNoSensor.setVisibility(View.VISIBLE);
		}

		loadingVisible=false;


	}

	/**
	 * 
	 * @return sensor response without null values
	 */
	private String[] checkAnyNullStatus() {
		int count=0;
		int length=sensorResponse.length;
		for (int i = 0; i < length; i++) {
			String[] split=sensorResponse[i].split("=");
			if(App_Variable.hashSensor.get(split[0])==null){
				count++;
			}				
		}

		if(count>0)
		{
			int countSensor=0;
			String[] response=new String[length-count];
			for (int i = 0; i < length; i++) {
				//System.out.println("sensorResponce"+sensorResponse[i]);

				String[] split=sensorResponse[i].split("=");
				if(App_Variable.hashSensor.get(split[0])!=null){
					System.out.println("sensorResponce"+split[0]);
					response[countSensor]=sensorResponse[i];
					countSensor++;
				}				
			}
			return response;
		}

		return sensorResponse;
	}

	private void callAPI(String url) {
		if(App_Variable.isNetworkAvailable(SensorActivity.this)){
			if(gr!=null)
				gr.cancel(true);
			gr=new GetResponseAPI(this,false);
			gr.execute(url);
		}else{
			App_Variable.ShowNoNetwork(SensorActivity.this);
		}
	}

	@Override
	public void setResponse(String response,String ex) {
		CustomLog.d(tag, "response::"+response);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		CustomLog.debug("onKeyDown back::"+screenIndex);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			CustomLog.debug("ranjith time detail back");
			if(screenIndex==-1){
				sensorvalue=false;
				finish();

			}else
			{
				sensorvalue=false;
				Intent intent=new Intent(SensorActivity.this,HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("position", -1);
				intent.putExtra("room", "");
				intent.putExtra("screen", "others");
				startActivity(intent);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}



}
