package com.divum.silvan;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.divum.constants.GetAPI;
import com.divum.parser.BaseParser;
import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;
import com.divum.utils.GetProfile;
import com.divum.utils.UpdateProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingsProfileActivity extends Activity implements OnClickListener{

	private GetProfileConfig gc;
	private GetProfileConfigApply ga;
	private GetProfileScheduleApply gs;
	private GetProfileStatus gps;
	private GetProfileStausDelete gsd;
	private String tag="SettingsProfileActivity";
	boolean profilevalue=true;
	private LinearLayout layout_config;
	private TextView tab_config,tab_schedule,tab_status;
	private ImageView img_tab_status_bottom,img_tab_schedule_bottom,img_tab_config_bottom;
	private HashMap<String, HashMap<String, String>> hashProfileSettings;
	private HashMap<String, String> hashSensorProfile;
	private Spinner spinner;
	private TextView btn_apply;

	//	private String[] sensor={"Kithchen(gasleak)","MBR-GF(panic)","BedRoom-GF(panic)","MBR-FF(panic)","BedRoom-FF(panic)",
	//			"Main Door(doorintrusion)","Terrace Door(doorintrusion)","ServerRoom Door(doorintrusion)","Server Room(smoke)","Server Room(motion)"};
	private String[] listProfile={"Away","Maid","Terrace","Disarm"};


	//	private String[] keyProfile={"gasleakA0","panicA1","panicA2","panicA3","panicA4","doorintrusionA6","doorintrusionA7",
	//			"doorintrusionB0","smokeB5","motionC3"};

	private String whichProfile="Away";
	private CheckBox[] checkBoxConfig;
	private ProgressDialog progress;


	private LinearLayout layout_profile_config;
	private LinearLayout layout_profile_schedule;
	private ScrollView layout_profile_status;
	private LinearLayout layout_status;

	private TextView txtApplyDate;
	private TextView txtApplyTime;

	private TextView txtApplyDaily;
	private TextView txtApplyOnce;
	private TextView txtNoSensorConfig;

	private Calendar cal;
	private int day;
	private int month;
	private int year;

	private int mHour;
	private int mMinute;

	private int mHourSelected;
	private int mMinSelected;

	private int mDaySelected;
	private int mMonthSelected;

	private String[] status=null;
	private ImageView img_slider;


	//	public SettingsProfileActivity() {
	//		super(R.string.app_name);
	//		// TODO Auto-generated constructor stub
	//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings_profile_view);
		hashProfileSettings=new HashMap<String, HashMap<String,String>>();
		hashSensorProfile=new HashMap<String, String>();

		layout_config=(LinearLayout) findViewById(R.id.layout_config);
		txtNoSensorConfig= (TextView) findViewById(R.id.txtNoSensorConfig);
		txtNoSensorConfig.setVisibility(View.GONE);

		GetAPI.BASE_URL=App_Variable.getBaseAPI(SettingsProfileActivity.this);

		/**
		 * Slider action
		 */
		img_slider=(ImageView)findViewById(R.id.img_slider);
		img_slider.setOnClickListener(this);


		/*
		 * create instance for tab
		 */
		tab_config=(TextView) findViewById(R.id.tab_config);
		tab_config.setOnClickListener(this);

		tab_schedule=(TextView) findViewById(R.id.tab_schedule);
		tab_schedule.setOnClickListener(this);

		tab_status=(TextView) findViewById(R.id.tab_status);
		tab_status.setOnClickListener(this);


		img_tab_config_bottom=(ImageView) findViewById(R.id.img_tab_config_bottom);
		img_tab_config_bottom.setVisibility(View.VISIBLE);

		img_tab_schedule_bottom=(ImageView) findViewById(R.id.img_tab_schedule_bottom);
		img_tab_status_bottom=(ImageView) findViewById(R.id.img_tab_status_bottom);


		/*
		 * set spinner and event
		 */
		spinner=(Spinner) findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listProfile);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				whichProfile = spinner.getSelectedItem().toString();
				SetCheckView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		/*
		 * create insatce fot apply
		 */
		btn_apply=(TextView) findViewById(R.id.btn_apply);
		btn_apply.setOnClickListener(this);

		/*
		 * set config data
		 */


		int length=App_Variable.hashSensor.size();
		if(length==0){
			txtNoSensorConfig.setText("No Sensors Available");
			txtNoSensorConfig.setVisibility(View.VISIBLE);
		}
		if(App_Variable.hashSensor!=null){
			Set entrySet = App_Variable.hashSensor.entrySet();
			Iterator iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();

				View vi=getLayoutInflater().inflate(R.layout.profile_config_view, null);
				TextView txtConfig=(TextView)vi.findViewById(R.id.txtConfig);
				String key=entry.getKey()+"";
				String displayKey=key.substring(0,key.length()-2);
				txtConfig.setText(entry.getValue()+"("+displayKey+")");

				layout_config.addView(vi);
			}
		}
		/*for (int i = 0; i < length; i++) {

			View vi=getLayoutInflater().inflate(R.layout.profile_config_view, null);
			TextView txtConfig=(TextView)vi.findViewById(R.id.txtConfig);
			txtConfig.setText(sensor[i]);

			//CheckBox checkBoxConfig=(CheckBox) vi.findViewById(R.id.checkBoxConfig);

			layout_config.addView(vi);

		}*/

		/*
		 * create instace for profile config view
		 */
		layout_profile_config=(LinearLayout) findViewById(R.id.layout_profile_config);
		layout_profile_config.setVisibility(View.VISIBLE);

		/*
		 * create instance for profile schedule view
		 */
		layout_profile_schedule=(LinearLayout) findViewById(R.id.layout_profile_schedule);
		layout_profile_schedule.setVisibility(View.GONE);

		/*
		 * craete instace for profile schedule buttons
		 */
		txtApplyDate=(TextView) findViewById(R.id.txtApplyDate); 
		txtApplyDate.setOnClickListener(this);

		txtApplyTime=(TextView) findViewById(R.id.txtApplyTime); 
		txtApplyTime.setOnClickListener(this);

		txtApplyDaily=(TextView) findViewById(R.id.txtApplyDaily); 
		txtApplyDaily.setOnClickListener(this);

		txtApplyOnce=(TextView) findViewById(R.id.txtApplyOnce); 
		txtApplyOnce.setOnClickListener(this);

		/*
		 * get current day, month and year
		 */
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);

		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMinute = cal.get(Calendar.MINUTE);


		txtApplyDate.setText(App_Variable.getAppendZero(""+day)+"/"+App_Variable.getAppendZero(""+(month+1))+"/"+year);
		txtApplyTime.setText(App_Variable.getAppendZero(""+mHour)+":"+App_Variable.getAppendZero(""+mMinute));

		mDaySelected=day;
		mMonthSelected=month+1;

		mMinSelected = mMinute;
		mHourSelected = mHour;


		/*
		 * create instance for profile status view
		 */
		layout_profile_status=(ScrollView) findViewById(R.id.layout_profile_status);
		layout_profile_status.setVisibility(View.GONE);

		layout_status=(LinearLayout) findViewById(R.id.layout_status);


		progress=new ProgressDialog(SettingsProfileActivity.this);

		callProfileConfig(true);


	}

	@Override
	protected void onPause() {
		App_Variable.appMinimize=0;
		CustomLog.show(tag, "pause:"+App_Variable.appMinimize);
		SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
		boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);
		if(checkvalue) {

			if(profilevalue) {
			Intent intent = new Intent(this, UnlockActivity.class);
			startActivity(intent);
		}}
		super.onPause();
	}

	@Override
	protected void onResume() {
		App_Variable.appMinimize=1;
		CustomLog.show(tag, "resume:"+App_Variable.appMinimize);

		super.onResume();
	}

	/**
	 * getting sensor status for all profile
	 * @param isLoading 
	 */
	private void callProfileConfig(boolean isLoading) {
		if(App_Variable.isNetworkAvailable(SettingsProfileActivity.this)){
			if(gc!=null)
				gc.cancel(true);
			gc=new GetProfileConfig(isLoading);
			gc.execute();
		}else{
			App_Variable.ShowNoNetwork(SettingsProfileActivity.this);
		}

	}

	class GetProfileConfig extends AsyncTask<Void, Void, Void>{
		private JSONObject response;
		private String exception="";
		private boolean _isLoading=false;

		public GetProfileConfig(boolean isLoading) {
			_isLoading=isLoading;
		}


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(_isLoading){
				progress.setMessage("Please wait while profile configuration loading..");
				progress.setCanceledOnTouchOutside(false);
				progress.show();
			}
			super.onPreExecute();
		}


		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.PROFILE_CONFIG);
			response=parser.getJsonObject();
			exception=parser.getException();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			CustomLog.check(tag, "ProfileConfig:"+response);
			//txt_homeProfile.setText(App_Variable.PROFILE.toUpperCase());

			progress.dismiss();

			if(!exception.equals("")){
				App_Variable.ShowErrorToast(exception, SettingsProfileActivity.this);
			}else{

				try {
					if(response!=null)
						GetResponse(response);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				SetCheckView();
			}

			super.onPostExecute(result);
		}

	}

	/**
	 * 
	 * @param response contains profile config response
	 * @param hashProfile contains key and value for profile name and values of sensor status
	 * @throws JSONException
	 */
	public void GetResponse(JSONObject response) throws JSONException {
		CustomLog.debug("response size:::"+response.length()+","+response);
		hashProfileSettings.clear();

		Iterator<?> keys_room = response.keys();
		while (keys_room.hasNext()) {
			String type = (String) keys_room.next();
			CustomLog.debug("profile::"+type);

			JSONObject obj=response.getJSONObject(type);
			CustomLog.debug("profile size:::"+obj.length());
			hashSensorProfile=new HashMap<String, String>();
			Iterator<?> keys_sensor = obj.keys();

			while (keys_sensor.hasNext()) {
				String key = (String) keys_sensor.next();
				CustomLog.parser("key sensor:"+key+","+obj.getString(key));
				hashSensorProfile.put(key, obj.getString(key));
			}
			hashProfileSettings.put(type, hashSensorProfile);


			/*while (keys_sensor.hasNext()) {
				Entry entry = (Entry) keys_sensor.next();
				CustomLog.debug("sensor::"+entry.getKey()+","+entry.getValue());

				hashSensorProfile.put(entry.getKey()+"", entry.getValue()+"");
			}*/


			/*String[] value=new String[obj.length()];
			int size=obj.length();
			for (int i = 0; i < size; i++) {
				CustomLog.debug("for loop profile::"+keyProfile[i]);
				if(obj.has(keyProfile[i])){
					value[i]=obj.getString(keyProfile[i]);
					CustomLog.debug("value  profile::"+value[i]);
				}
			}

			hashProfileSettings.put(type, value);*/
		}
	}

	/**
	 * if any sensor option is enabled, make it checkbox true
	 * otherwise, make it checkbox false
	 */
	@SuppressWarnings("null")
	public void SetCheckView() {
		// TODO Auto-generated method stub
		layout_config.removeAllViewsInLayout();
		layout_config.removeAllViews();
		int length=App_Variable.hashSensor.size();

		CustomLog.debug("whichProfile::::"+whichProfile);
		HashMap<String, String> hashValue=hashProfileSettings.get(whichProfile);


		/*	CustomLog.debug("value::"+value);
		if(value==null){
			value=new String[length];
			for (int i = 0; i < length; i++) {
				value[i]="disabled";
			}
		}*/

		checkBoxConfig=new CheckBox[length];

		int checkCount=0;
		if(App_Variable.hashSensor!=null){
			Set entrySet = App_Variable.hashSensor.entrySet();
			Iterator iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();

				View vi=getLayoutInflater().inflate(R.layout.profile_config_view, null);
				TextView txtConfig=(TextView)vi.findViewById(R.id.txtConfig);
				String key=entry.getKey()+"";
				String valueStatus=entry.getValue()+"".trim();
				String displayKey=key.substring(0,key.length()-2);
				txtConfig.setText(entry.getValue()+"("+displayKey+")");

				checkBoxConfig[checkCount]=(CheckBox) vi.findViewById(R.id.checkBoxConfig);
				checkBoxConfig[checkCount].setTag(checkCount+"&&"+key);
				checkBoxConfig[checkCount].setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						String[] split=(buttonView.getTag()+"").split("&&");
						int index=Integer.parseInt(split[0]);
						if(isChecked){
							checkBoxConfig[index].setText("Enabled");
						}else{
							checkBoxConfig[index].setText("Disabled");
						}

						CustomLog.check("click:::"+isChecked);

					}
				});

				if(hashValue!=null){
					CustomLog.debug("key:::"+key);

					String status=hashValue.get(key);
					if(status!=null){
						if(status.equalsIgnoreCase("enabled")){
							checkBoxConfig[checkCount].setText("Enabled");
							checkBoxConfig[checkCount].setChecked(true);
						}else{
							checkBoxConfig[checkCount].setText("Disabled");
							checkBoxConfig[checkCount].setChecked(false);

						}
					}

				}

				checkCount++;

				layout_config.addView(vi);
			}
		}

		/*for (int i = 0; i < length; i++) {

			View vi=getLayoutInflater().inflate(R.layout.profile_config_view, null);

			TextView txtConfig=(TextView)vi.findViewById(R.id.txtConfig);
			txtConfig.setText(sensor[i]);

			checkBoxConfig[i]=(CheckBox) vi.findViewById(R.id.checkBoxConfig);
			checkBoxConfig[i].setTag(i);
			checkBoxConfig[i].setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int index=Integer.parseInt(buttonView.getTag()+"");
					if(isChecked){
						checkBoxConfig[index].setText("Enabled");
					}else{
						checkBoxConfig[index].setText("Disabled");
					}

					CustomLog.check("click:::"+isChecked);

				}
			});

			if(value[i].equalsIgnoreCase("enabled")){
				checkBoxConfig[i].setText("Enabled");
				checkBoxConfig[i].setChecked(true);
			}else{
				checkBoxConfig[i].setText("Disabled");
				checkBoxConfig[i].setChecked(false);

			}

			layout_config.addView(vi);

		}*/

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_config:
			img_tab_config_bottom.setVisibility(View.VISIBLE);
			img_tab_schedule_bottom.setVisibility(View.GONE);
			img_tab_status_bottom.setVisibility(View.GONE);

			layout_profile_config.setVisibility(View.VISIBLE);
			layout_profile_schedule.setVisibility(View.GONE);
			layout_profile_status.setVisibility(View.GONE);


			if(App_Variable.hashSensor.size()==0){
				txtNoSensorConfig.setText("No Sensors Available");
				txtNoSensorConfig.setVisibility(View.VISIBLE);
			}

			break;
		case R.id.tab_schedule:
			img_tab_config_bottom.setVisibility(View.GONE);
			img_tab_schedule_bottom.setVisibility(View.VISIBLE);
			img_tab_status_bottom.setVisibility(View.GONE);

			layout_profile_config.setVisibility(View.GONE);
			layout_profile_schedule.setVisibility(View.VISIBLE);
			layout_profile_status.setVisibility(View.GONE);

			txtNoSensorConfig.setVisibility(View.GONE);
			break;
		case R.id.tab_status:
			img_tab_config_bottom.setVisibility(View.GONE);
			img_tab_schedule_bottom.setVisibility(View.GONE);
			img_tab_status_bottom.setVisibility(View.VISIBLE);

			layout_profile_config.setVisibility(View.GONE);
			layout_profile_schedule.setVisibility(View.GONE);
			layout_profile_status.setVisibility(View.VISIBLE);

			txtNoSensorConfig.setVisibility(View.GONE);

			//if(status==null)
			callProfileStatus();


			break;

		case R.id.btn_apply:
			/*
			 * profile config apply
			 */
			callApplyPOST();

			break;

		case R.id.txtApplyDate:
			showDialog(0);

			break;
		case R.id.txtApplyTime:
			showDialog(1);

			break;
		case R.id.txtApplyDaily:
			/*
			 * profile schedule daily apply
			 */
			String urlString=GetAPI.BASE_URL+GetAPI.PROFILE_SCHEDULE1;
			urlString +=App_Variable.getAppendZero(""+mMinSelected)+"%20"+App_Variable.getAppendZero(""+mHourSelected)+"%20-1%20-1%20";
			urlString +=GetAPI.PROFILE_SCHEDULE2+"%20"+whichProfile;

			callScheduleApply(urlString);

			break;

		case R.id.txtApplyOnce:
			/*
			 * profile schedule once apply
			 */
			String urlOnceString=GetAPI.BASE_URL+GetAPI.PROFILE_SCHEDULE1;
			urlOnceString +=App_Variable.getAppendZero(""+mMinSelected)+"%20"+App_Variable.getAppendZero(""+mHourSelected)+"%20"+App_Variable.getAppendZero(""+mDaySelected)+"%20"+App_Variable.getAppendZero(""+mMonthSelected)+"%20";
			urlOnceString +=GetAPI.PROFILE_SCHEDULE2+"%20"+whichProfile;

			callScheduleApply(urlOnceString);

			break;
		case R.id.img_slider:
			//			if(sm!=null)
			//				sm.toggle();
			/*Intent intentHome=new Intent(SettingsProfileActivity.this,HomeActivity.class);		
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentHome.putExtra("position", -1);
			intentHome.putExtra("room", "");
			intentHome.putExtra("screen", "others");
			startActivity(intentHome);*/
			profilevalue=false;
			finish();
			break;

		default:
			break;
		}

	}

	private void callProfileStatus() {
		if(App_Variable.isNetworkAvailable(SettingsProfileActivity.this)){
			if(gps!=null)
				gps.cancel(true);
			gps=new GetProfileStatus();
			gps.execute();
		}else{
			App_Variable.ShowNoNetwork(SettingsProfileActivity.this);
		}
	}

	class GetProfileStatus extends AsyncTask<Void, Void, Void>{

		private String response="";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progress.setMessage("Please wait while profile status loading..");
			progress.setCanceledOnTouchOutside(false);
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.PROFILE_STATUS);
			response=parser.getResponse();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			//Log.d(tag, "apply status:"+response);
			progress.dismiss();
			try{
				status=response.split("\n");
				int length=status.length;
				if(length==0){
					txtNoSensorConfig.setText("No Status Available");
					txtNoSensorConfig.setVisibility(View.VISIBLE);
				}

				layout_status.removeAllViewsInLayout();
				layout_status.removeAllViews();

				for (int i = 0; i < length; i++) {
					if(!status[i].startsWith("Content")&&!status[i].trim().equals("")){

						View vi=getLayoutInflater().inflate(R.layout.profile_status_view, null);

						TextView txtStatusDay=(TextView) vi.findViewById(R.id.txtStatusDay);
						TextView txtStatusTime=(TextView) vi.findViewById(R.id.txtStatusTime);
						TextView txtStatusProfile=(TextView) vi.findViewById(R.id.txtStatusProfile);

						String[] spliStatus=status[i].split(" ");
						int splitLength=spliStatus.length;
						String[] spitMin = null;
						String day = null;
						for (int j = 0; j <splitLength; j++) {
							if(j==0){
								spitMin=spliStatus[j].split(":");
								//System.out.println("MM:"+spitMin[1]);

							}else if(j==1){
								//System.out.println("HH:"+spliStatus[j]);
								txtStatusTime.setText(spliStatus[j]+":"+spitMin[1]);

							}else if(j==2){
								day=spliStatus[j];
								//System.out.println("DD:"+spliStatus[j]);
							}else if(j==3){
								if(day.startsWith("*"))
									txtStatusDay.setText("DAILY");
								else
									txtStatusDay.setText(day+"/"+spliStatus[j]);
								//System.out.println("MM:"+spliStatus[j]);
							}else if(j==splitLength-1){
								//System.out.println("Profile:"+spliStatus[j]);
								txtStatusProfile.setText(spliStatus[j].toUpperCase());
							}

						}
						final ImageView statusDelete=(ImageView) vi.findViewById(R.id.statusDelete);
						statusDelete.setTag(spitMin[0]);
						statusDelete.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								CustomLog.debug("delete id:"+statusDelete.getTag());
								String urlString=GetAPI.BASE_URL+GetAPI.PROFILE_STATUS_DELETE+statusDelete.getTag();
								callProfileStatusDelete(urlString);

							}
						});
						layout_status.addView(vi);
					}
				}
			}catch(Exception e){
				System.out.println("profile staus ex:"+e);
			}

			super.onPostExecute(result);
		}
	}

	private void callProfileStatusDelete(String urlString) {
		if(App_Variable.isNetworkAvailable(SettingsProfileActivity.this)){
			if(gsd!=null)
				gsd.cancel(true);
			gsd=new GetProfileStausDelete();
			gsd.execute(urlString);
		}else{
			App_Variable.ShowNoNetwork(SettingsProfileActivity.this);
		}
	}

	class GetProfileStausDelete extends AsyncTask<String, Void, Void>{

		private String response="";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progress.setMessage("Please wait while scheduling is being removed..");
			progress.setCanceledOnTouchOutside(false);
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			BaseParser parser=new BaseParser(params[0]);
			response=parser.getResponse();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			CustomLog.d(tag, "apply schdule:"+response);
			progress.dismiss();

			callProfileStatus();

			super.onPostExecute(result);
		}
	}


	private void callScheduleApply(String urlString) {
		if(App_Variable.isNetworkAvailable(SettingsProfileActivity.this)){
			if(gs!=null)
				gs.cancel(true);
			gs=new GetProfileScheduleApply();
			gs.execute(urlString);
		}else{
			App_Variable.ShowNoNetwork(SettingsProfileActivity.this);
		}
	}

	class GetProfileScheduleApply extends AsyncTask<String, Void, Void>{

		private String response="";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progress.setMessage("Please wait while applying schedule..");
			progress.setCanceledOnTouchOutside(false);
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			BaseParser parser=new BaseParser(params[0]);
			response=parser.getResponse();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			CustomLog.d(tag, "apply schdule:"+response);
			progress.setMessage("Schedule successfully updated");
			/*progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        dialog.dismiss();
			    }
			});*/
			progress.dismiss();



			super.onPostExecute(result);
		}


	}


	private void callApplyPOST() {
		if(App_Variable.isNetworkAvailable(SettingsProfileActivity.this)){
			if(ga!=null)
				ga.cancel(true);
			ga=new GetProfileConfigApply();
			ga.execute();
		}else{
			App_Variable.ShowNoNetwork(SettingsProfileActivity.this);
		}

	}

	class GetProfileConfigApply extends AsyncTask<Void, Void, Void>{

		private String response="";

		private String payloadText="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			payloadText= getPayload();
			progress.setMessage("Request for configuaration is being sent..");
			progress.setCanceledOnTouchOutside(false);
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.PROFILE_CONFIG_APPLY,payloadText);
			response=parser.getPOSTResponse();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			CustomLog.d(tag, "apply:"+response);

			//progress.dismiss();

			callProfileConfig(false);
			super.onPostExecute(result);
		}

	}

	/**
	 * 
	 * @return text variable contains payload data for apply config api
	 */
	private String getPayload() {
		String text="";

		text="{"+"\""+whichProfile+"\""+":";

		text +="{";
		int length=checkBoxConfig.length;
		for (int i = 0; i < length; i++) {
			String[] split=(checkBoxConfig[i].getTag()+"").split("&&");
			if(checkBoxConfig[i].isChecked()){
				if(i==length-1)
					text+="\""+split[1]+"\""+":"+"\""+"enabled"+"\"";
				else
					text+="\""+split[1]+"\""+":"+"\""+"enabled"+"\""+",";
			}else{
				if(i==length-1)
					text+="\""+split[1]+"\""+":"+"\""+"disabled"+"\"";
				else
					text+="\""+split[1]+"\""+":"+"\""+"disabled"+"\""+",";

			}
		}
		text+="}";

		//int size=hashProfile.size();
		Set entrySet = hashProfileSettings.entrySet();
		Iterator iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			if(!(""+entry.getKey()).equalsIgnoreCase(whichProfile)){
				text +=",";

				text +="\""+entry.getKey()+"\""+":";
				text +="{";
				HashMap<String, String> hashdata=(HashMap<String, String>) entry.getValue();
				Set entrySetdata = hashdata.entrySet();
				Iterator iteratorData = entrySetdata.iterator();
				int size=hashdata.size();
				int count=0;
				while (iteratorData.hasNext()) {
					Entry entryData = (Entry) iteratorData.next();
					if(count==size-1)
						text+="\""+entryData.getKey()+"\""+":"+"\""+entryData.getValue()+"\"";
					else
						text+="\""+entryData.getKey()+"\""+":"+"\""+entryData.getValue()+"\""+",";
					count++;

				}


				/*for (int i = 0; i < size; i++) {
					if(i==size-1)
						text+="\""+keyProfile[i]+"\""+":"+"\""+value[i]+"\"";
					else
						text+="\""+keyProfile[i]+"\""+":"+"\""+value[i]+"\""+",";
				}*/
				text+="}";

			}
		}

		text+="}";

		CustomLog.debug("Payload data:"+text);

		return text;
	}


	protected Dialog onCreateDialog(int id) {
		if(id==0){
			DatePickerDialog dialog =new DatePickerDialog(this, datePickerListener, year, month, day);

			Date min = new Date(year-1900, month, day);
			dialog.getDatePicker().setMinDate(min.getTime());

			return dialog;
		}
		else{
			TimePickerDialog dialog1 = new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,true);
			//dialog1.get
			return dialog1;
		}

	}
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {


		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			/*txtApplyDate.setText(selectedDay + " - " + App_Variable.getfMonth((selectedMonth + 1)) + " - "
					+ selectedYear);*/
			mDaySelected=selectedDay;
			mMonthSelected=selectedMonth + 1;
			txtApplyDate.setText(App_Variable.getAppendZero(""+selectedDay) + "/" + App_Variable.getAppendZero(""+(selectedMonth + 1)) + "/"
					+ selectedYear);

		}
	};
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {

				// the callback received when the user "sets" the TimePickerDialog in the dialog
				public void onTimeSet(TimePicker view, int hourOfDay, int min) {
					//Calendar c = Calendar.getInstance();
					mHourSelected = hourOfDay;
					mMinSelected = min;
					//msec=sec;
					Calendar c=Calendar.getInstance();
					Log.i("ADebugTag", "mHour: " + mHourSelected);
					Log.i("ADebugTag", "hour: " + c.get(Calendar.HOUR_OF_DAY));
					Log.i("ADebugTag", "day: " + c.get(Calendar.DAY_OF_MONTH));
					Log.i("ADebugTag", "month: " + c.get(Calendar.MONTH));
					Log.i("ADebugTag", "mday: " + mDaySelected);
					Log.i("ADebugTag", "mmonth: " + mMonthSelected);
					//Log.i("ADebugTag", "myear: " + mYearSelected);
					Log.i("ADebugTag", "year: " + c.get(Calendar.YEAR));
					if((mHourSelected<=c.get(Calendar.HOUR_OF_DAY))&&(mDaySelected==c.get(Calendar.DAY_OF_MONTH))&&(mMonthSelected==(c.get(Calendar.MONTH)+1))){
						//Toast.makeText(MoodActivity.this,"cant set",Toast.LENGTH_LONG).show();
						if ((mMinSelected<=c.get(Calendar.MINUTE))){
							AlertDialog ad=new AlertDialog.Builder(SettingsProfileActivity.this)
									.setTitle("Time picker")
									.setMessage("you can't set past time")
									.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											//img_tab_schedule_bottom.setVisibility(View.VISIBLE);
											dialog.dismiss();
											showDialog(1);


										}
									})
									.create();
							ad.setIcon(android.R.drawable.ic_dialog_alert);
							ad.show();
						}}
					else
					{
						txtApplyTime.setText(App_Variable.getAppendZero("" + hourOfDay) + ":" + App_Variable.getAppendZero("" + min));

						//onTimeSet(view,hourOfDay,min);
						//mTimeSetListener.TimePickerDialog(this, mTimeSetListener, mHour, mMinute,true);
					}}
			};


	/*private TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {


		// the callback received when the user "sets" the TimePickerDialog in the dialog
		public void onTimeSet(TimePicker view, int hourOfDay, int min) {

			mHourSelected=hourOfDay;
			mMinSelected=min;
			txtApplyTime.setText(App_Variable.getAppendZero(""+hourOfDay) + ":" +App_Variable.getAppendZero(""+min));
		}
	};
*/
	public boolean onKeyDown(int keyCode, KeyEvent event){
		profilevalue=false;
		finish();
		return super.onKeyDown(keyCode, event);
	}

}

