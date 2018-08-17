package com.divum.silvan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.divum.constants.GetAPI;
import com.divum.parser.BaseParser;
import com.divum.utils.App_Variable;
import com.divum.utils.CommonPingURL;
import com.divum.utils.CustomLog;

public class ProfileActivity extends Activity implements OnClickListener{

	private LinearLayout layoutAway;
	private LinearLayout layoutMaid;
	private LinearLayout layoutTerrace;
	private LinearLayout layoutDisarm;
	private String whichProfile="";

	private GetProfileSensorList gs;
	private GetProfileSensorArm gc;
	private GetProfileByPass gb;

	private TextView txt_profile_settings;
	private TextView txt_profile_arm;
	private TextView txt_profile_byPass;

	private String tag="ProfileActivity";
	private String sensorList="";
	private String sensorCheck="";
	private ImageView img_slider;

	private String byPass="";
	private LinearLayout linear_profile_sensor_status;
	private boolean _isByPassClicked=false;

	private boolean _isArmProfile=false;
	private ProgressDialog progressArm,progress;
	//	public ProfileActivity() {
	//		super(R.string.app_name);
	//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profile_horizontal_view);
		App_Variable.appMinimize=1;

		GetAPI.BASE_URL=App_Variable.getBaseAPI(ProfileActivity.this);

		progressArm = new ProgressDialog(ProfileActivity.this);
		progressArm.setMessage("Please wait while the selected profile is being armed..");
		progressArm.setCanceledOnTouchOutside(false);

		progress= new ProgressDialog(ProfileActivity.this);
		progress.setMessage("Please wait while the selected profile open sensors are loading..");
		progress.setCanceledOnTouchOutside(false);


		/**
		 * Slider action
		 */
		img_slider=(ImageView)findViewById(R.id.img_slider);
		img_slider.setOnClickListener(this);

		/*
		 * set title
		 */
		TextView txt_header=(TextView)findViewById(R.id.txt_header);
		txt_header.setText("PROFILE");


		/*
		 * create instance for all profile
		 */
		layoutAway=(LinearLayout) findViewById(R.id.layoutAway);
		layoutAway.setOnClickListener(this);

		layoutMaid=(LinearLayout) findViewById(R.id.layoutMaid);
		layoutMaid.setOnClickListener(this);

		layoutTerrace=(LinearLayout) findViewById(R.id.layoutTerrace);
		layoutTerrace.setOnClickListener(this);

		layoutDisarm=(LinearLayout) findViewById(R.id.layoutDisarm);
		layoutDisarm.setOnClickListener(this);


		/*
		 * create instance for settings, arm and bypass buttons
		 */

		txt_profile_settings=(TextView) findViewById(R.id.txt_profile_settings);
		txt_profile_settings.setOnClickListener(this);

		txt_profile_arm=(TextView) findViewById(R.id.txt_profile_arm);
		txt_profile_arm.setVisibility(View.INVISIBLE);
		txt_profile_arm.setOnClickListener(this);

		txt_profile_byPass=(TextView) findViewById(R.id.txt_profile_byPass);
		txt_profile_byPass.setOnClickListener(this);

		linear_profile_sensor_status =(LinearLayout)findViewById(R.id.linear_profile_sensor_status);


	}

	@Override
	protected void onPause() {
		App_Variable.appMinimize=0;
		CustomLog.show(tag, "pause:"+App_Variable.appMinimize);



		super.onPause();
	}

	@Override
	protected void onResume() {
		App_Variable.appMinimize=1;
		CustomLog.show(tag, "resume:"+App_Variable.appMinimize);

		super.onResume();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.layoutAway:
			whichProfile="Away";


			callProfileShow();

			break;
		case R.id.layoutMaid:
			whichProfile="Maid";

			callProfileShow();
			break;
		case R.id.layoutTerrace:
			whichProfile="Terrace";
			callProfileShow();
			break;
		case R.id.layoutDisarm:
			whichProfile="Disarm";
			callProfileShow();
			break;

		case R.id.txt_profile_settings:
			/*
			 * goto profile settings page
			 */
			Intent intent =new Intent(ProfileActivity.this,SettingsProfileActivity.class);
			startActivity(intent);
			break;
		case R.id.txt_profile_arm:
			_isArmProfile=true;
			callProfileCheckArm("ARM");
			break;
		case R.id.txt_profile_byPass:
			_isByPassClicked =true;
			callProfileBypass();
			break;
		case R.id.img_slider:
			//			if(sm!=null)
			//				sm.toggle();

			//callHomePage();

			finish();



			break;
		    default:
			break;


		}

	}

	private void callHomePage() {

		Intent intentHome=new Intent(ProfileActivity.this,HomeActivity.class);		
		intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intentHome.putExtra("position", -1);
		intentHome.putExtra("room", "");
		intentHome.putExtra("screen", "others");
		startActivity(intentHome);
		finish();
	}

	private void callProfileShow() {
		if(App_Variable.hashSensor.size()>0){
			_isByPassClicked =false;
			_isArmProfile =false;
			if(App_Variable.isNetworkAvailable(ProfileActivity.this)){
				linear_profile_sensor_status.removeAllViews();
				linear_profile_sensor_status.removeAllViewsInLayout();

				if(gs!=null)
					gs.cancel(true);
				gs=new GetProfileSensorList();
				gs.execute();
			}else{
				App_Variable.ShowNoNetwork(ProfileActivity.this);
			}
		}else{
			Toast.makeText(this, "No Sensors Available", Toast.LENGTH_SHORT).show();

		}

	}

	class GetProfileSensorList extends AsyncTask<Void, Void, Void>{

		private String exception="";

		@Override
		protected void onPreExecute() {
			progress.setMessage("Please wait while the selected profile open sensors loading..");
			progress.setCanceledOnTouchOutside(false);
			progress.show();
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			sensorList="";
			BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.PROFILE_LIST+whichProfile);
			sensorList=parser.getResponse();
			exception=parser.getException();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			CustomLog.d(tag, "PROFILE LIST:"+sensorList);
			//txt_homeProfile.setText(App_Variable.PROFILE.toUpperCase());

			if(!exception.equals("")){
				progress.dismiss();
				App_Variable.ShowErrorToast(exception, ProfileActivity.this);
			}else
				callProfileCheckArm("Check");
			super.onPostExecute(result);
		}

	}

	public void callProfileCheckArm(String status) {
		if(App_Variable.hashSensor.size()>0){
			if(App_Variable.isNetworkAvailable(ProfileActivity.this)){
				if(gc!=null)
					gc.cancel(true);
				gc=new GetProfileSensorArm();
				gc.execute(status);
			}else{
				if(progress!=null)
					progress.dismiss();
				App_Variable.ShowNoNetwork(ProfileActivity.this);
			}
		}else{
			Toast.makeText(this, "No Sensors Available", Toast.LENGTH_SHORT).show();
		}
	}

	class GetProfileSensorArm extends AsyncTask<String, Void, Void>{

		private String exception="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(_isArmProfile){
				progressArm.setMessage("Please wait while the selected profile is being armed..");
				progressArm.setCanceledOnTouchOutside(false);
				progressArm.show();
			}
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			sensorCheck="";
			String API="";
			if(params[0].equals("Check")){
				API=GetAPI.BASE_URL+GetAPI.PROFILE_CHECK_ARM+whichProfile;
				_isArmProfile=false;
			}else{

				API=GetAPI.BASE_URL+GetAPI.PROFILE_ARM+whichProfile;
				_isArmProfile=true;
			}
			BaseParser parser=new BaseParser(API);
			sensorCheck=parser.getResponse();
			exception=parser.getException();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			CustomLog.d(tag, "PROFILE Check:"+sensorCheck);
			//txt_homeProfile.setText(App_Variable.PROFILE.toUpperCase());

			if(!exception.equals(""))
				App_Variable.ShowErrorToast(exception, ProfileActivity.this);
			else{

				if(!_isArmProfile)
					callProfileBypass();
				else{

					App_Variable.PROFILE = whichProfile;
					progressArm.dismiss();
					callHomePage();
					//	finish();
					//	Toast.makeText(getApplicationContext(), "go to home page", Toast.LENGTH_LONG).show();
				}
			}
			super.onPostExecute(result);
		}

	}

	private void callProfileBypass() {
		if(App_Variable.hashSensor.size()>0){
			if(App_Variable.isNetworkAvailable(ProfileActivity.this)){
				if(gb!=null)
					gb.cancel(true);
				gb=new GetProfileByPass();
				gb.execute();
			}else{

				App_Variable.ShowNoNetwork(ProfileActivity.this);
			}
		}else{
			Toast.makeText(this, "No Sensors Available", Toast.LENGTH_SHORT).show();
		}
	}

	class GetProfileByPass extends AsyncTask<Void, Void, Void>{

		private String exception="";
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.PROFILE_BYPASS+whichProfile);
			byPass=parser.getResponse();
			exception=parser.getException().trim();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			CustomLog.d(tag, "PROFILE ByPass:"+byPass);
			//txt_homeProfile.setText(App_Variable.PROFILE.toUpperCase());
			if(!exception.equals("")){
				if(progress!=null)
					progress.dismiss();
				App_Variable.ShowErrorToast(exception, ProfileActivity.this);
			}else{
				linear_profile_sensor_status.removeAllViews();
				linear_profile_sensor_status.removeAllViewsInLayout();

				if(progress!=null)
					progress.dismiss();

				if(_isByPassClicked){
					displayBypassStatus();
				}else{
					displayProfileSensorStatus();
				}
			}

			super.onPostExecute(result);
		}

	}
	int profileArmLength=0;
	String[] splitProfileCheck=null;
	int length=0;
	String[] status_blue;
	//TextView[] txtEachSetting;
	public void displayProfileSensorStatus() {
		int profileShowLength=0;
		profileArmLength=0;

		int profileBypassLength=0;

		/* spliting profile area status */
		String[] splitShow= sensorList.trim().split("\n\n");
		String[] splitProfileShow=null;
		if(splitShow.length>1){
			//splitProfileShow=splitShow[1].trim().split(" ");
			splitProfileShow=GetSplittedResponse(splitShow[1].trim());
			if(splitProfileShow!=null)
				profileShowLength =splitProfileShow.length;
		}

		/* spliting danger(arm) area status */
		String[] splitCheck=sensorCheck.trim().split("\n\n");
		splitProfileCheck=null;
		if(splitCheck.length>1){
			if(!splitCheck[1].equalsIgnoreCase("ready to arm")){
				splitProfileCheck = GetSplittedResponse(splitCheck[1].trim());
				//splitProfileCheck =splitCheck[1].trim().split(" ");
				if(splitProfileCheck!=null)
					profileArmLength = splitProfileCheck.length;
			}
		}

		/* spliting profile area bypass status */
		String[] splitBypass=byPass.trim().split("\n\n");
		String[] splitProfileBypass=null;

		if(splitBypass.length>1){
			//splitProfileBypass =splitBypass[1].trim().split(" ");
			splitProfileBypass = GetSplittedResponse(splitBypass[1].trim());
			if(splitProfileBypass!=null)
				profileBypassLength = splitProfileBypass.length;
		}

		CustomLog.e("lenth:",profileShowLength+","+profileArmLength+","+profileBypassLength);

		/* grouping bypass status and profileshow staus */
		int totalLength =profileArmLength+profileBypassLength;
		if(!_isArmProfile)
		{
			if(totalLength==0){
				Toast.makeText(getApplicationContext(), "No Active Sensors so you can Arm Profile..!!", Toast.LENGTH_SHORT).show();
			}
		}
		if(totalLength==0){
			txt_profile_arm.setVisibility(View.VISIBLE);
		}else{
			txt_profile_arm.setVisibility(View.INVISIBLE);
		}
		String[] sensorStaus=new String[totalLength];

		for (int i = 0; i < profileArmLength; i++) {				
			sensorStaus[i]=splitProfileCheck[i];				
		}

		int count=0;
		for (int i = profileArmLength; i < totalLength; i++) {		
			sensorStaus[i]=splitProfileBypass[count];	
			count++;
		}

		/* populate profile status in UI */
		length= sensorStaus.length;
		status_blue=new String[length];

		for (int i = 0; i < length; i++) {
			CustomLog.d("all status:",i+","+sensorStaus[i]);
			status_blue[i]="";

			final View vi=getLayoutInflater().inflate(R.layout.profile_sensor_view, null);

			final TextView txtEachSetting = (TextView) vi.findViewById(R.id.txtEachSetting);
			//CustomLog.d("status:",i+","+sensorStaus[i]);

			String keySensor=""+App_Variable.hashSensor.get(sensorStaus[i]);
			if(keySensor!=null){
				txtEachSetting.setText(keySensor);

				ImageView imgEachSetting= (ImageView) vi.findViewById(R.id.imgEachSetting);
				if(sensorStaus[i].contains("doorintrusion")){
					imgEachSetting.setImageResource(R.drawable.sensor_door);
				}else if(sensorStaus[i].contains("panic")){
					imgEachSetting.setImageResource(R.drawable.sensor_panicroom);
				}else if(sensorStaus[i].contains("gasleak")){
					imgEachSetting.setImageResource(R.drawable.sensor_gasleak);
				}else if(sensorStaus[i].contains("motion")){
					imgEachSetting.setImageResource(R.drawable.sensor_motion);
				}else if(sensorStaus[i].contains("smoke")){
					imgEachSetting.setImageResource(R.drawable.sensor_smoke);
				}
				//			if(CheckArm(sensorStaus[i])){
				//				txtEachSetting.setTextColor(Color.parseColor("#FF0000"));
				//			}
				if(i>=profileArmLength){
					txtEachSetting.setTextColor(Color.parseColor("#0000FF"));
					vi.setTag("blue&&"+sensorStaus[i]+"&&"+i);
					status_blue[i]="blue";
				}else{
					txtEachSetting.setTextColor(Color.parseColor("#FF0000"));
					vi.setTag("red&&"+sensorStaus[i]+"&&"+i);
					status_blue[i]="red";
				}

				vi.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						String data=""+v.getTag();
						String[] split=data.split("&&");

						int index = Integer.parseInt(split[2]);
						if(split[0].equals("red")){
							AddBypass(split[1]);
							txtEachSetting.setTextColor(Color.parseColor("#0000FF"));
							vi.setTag("blue&&"+split[1]+"&&"+index);
							status_blue[index]="blue";
						}else if(split[0].equals("blue")){
							RemoveBypass(split[1]);
							txtEachSetting.setTextColor(Color.parseColor("#FF0000"));
							vi.setTag("red&&"+split[1]+"&&"+index);
							status_blue[index]="red";
						}	

						if(!CheckAllRedSensor()){
							txt_profile_arm.setVisibility(View.VISIBLE);
						}else{
							txt_profile_arm.setVisibility(View.INVISIBLE);
						}

					}
				});

				linear_profile_sensor_status.addView(vi);
			}
		}

		if(!CheckAllRedSensor()){
			txt_profile_arm.setVisibility(View.VISIBLE);
		}else{
			txt_profile_arm.setVisibility(View.INVISIBLE);
		}
	}

	protected boolean CheckAllRedSensor() {
		for (int i = 0; i < length; i++) {
			if(status_blue[i].equals("red"))
				return true;
		}
		return false;

	}

	public void displayBypassStatus() {
		int profileBypassLength=0;


		/* spliting profile area bypass status */
		String[] splitBypass=byPass.trim().split("\n\n");
		String[] splitProfileBypass=null;
		if(splitBypass.length>1){
			splitProfileBypass =splitBypass[1].trim().split(" ");
			profileBypassLength = splitProfileBypass.length;
		}

		/* populate profile status in UI */

		for (int i = 0; i < profileBypassLength; i++) {
			CustomLog.d("all bypass status:",i+","+splitProfileBypass[i]);

			final View vi=getLayoutInflater().inflate(R.layout.profile_sensor_view, null);

			final TextView txtEachSetting= (TextView) vi.findViewById(R.id.txtEachSetting);

			String keySensor=""+App_Variable.hashSensor.get(splitProfileBypass[i]);
			if(keySensor!=null){
				txtEachSetting.setText(keySensor);

				ImageView imgEachSetting= (ImageView) vi.findViewById(R.id.imgEachSetting);
				if(splitProfileBypass[i].contains("doorintrusion")){
					imgEachSetting.setImageResource(R.drawable.sensor_door);
				}else if(splitProfileBypass[i].contains("panic")){
					imgEachSetting.setImageResource(R.drawable.sensor_panicroom);
				}else if(splitProfileBypass[i].contains("gasleak")){
					imgEachSetting.setImageResource(R.drawable.sensor_gasleak);
				}else if(splitProfileBypass[i].contains("motion")){
					imgEachSetting.setImageResource(R.drawable.sensor_motion);
				}else if(splitProfileBypass[i].contains("smoke")){
					imgEachSetting.setImageResource(R.drawable.sensor_smoke);
				}

				txtEachSetting.setTextColor(Color.parseColor("#0000FF"));

				vi.setTag("blue&&"+splitProfileBypass[i]+"&&"+i);
				vi.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String data=""+v.getTag();
						String[] split=data.split("&&");

						int index = Integer.parseInt(split[2]);
						if(split[0].equals("red")){
							AddBypass(split[1]);
							txtEachSetting.setTextColor(Color.parseColor("#0000FF"));
							vi.setTag("blue&&"+split[1]+"&&"+index);
						}else if(split[0].equals("blue")){
							RemoveBypass(split[1]);
							txtEachSetting.setTextColor(Color.parseColor("#FF0000"));
							vi.setTag("red&&"+split[1]+"&&"+index);
						}			
					}
				});
				linear_profile_sensor_status.addView(vi);
			}
		}
	}
	/**
	 * add bypass
	 */
	private void AddBypass(String sensorID) {
		if(App_Variable.isNetworkAvailable(ProfileActivity.this)){
			CommonPingURL puc=new CommonPingURL("others",ProfileActivity.this);
			puc.execute(GetAPI.BASE_URL+GetAPI.PROFILE_BYPASS_ADD+whichProfile+"+"+sensorID);
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}

	/**
	 * remove bypass
	 */
	private void RemoveBypass(String sensorID) {
		if(App_Variable.isNetworkAvailable(ProfileActivity.this)){
			CommonPingURL puc=new CommonPingURL("others",ProfileActivity.this);
			puc.execute(GetAPI.BASE_URL+GetAPI.PROFILE_BYPASS_REMOVE+whichProfile+"+"+sensorID);
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}

	/**
	 * 
	 * @param trim
	 */
	private String[] GetSplittedResponse(String trimData) {
		// TODO Auto-generated method stub
		CustomLog.debug("before split::"+trimData);
		String[] split=null;
		if(trimData.contains(" ")){
			CustomLog.debug("space split::");
			split=trimData.split(" ");
		}else if(trimData.contains("\n\n")){
			CustomLog.debug("double newline split::");
			split=trimData.split("\n\n");			
		}else if(trimData.contains("\n")){
			CustomLog.debug("newline split::");
			split=trimData.split("\n");			
		}else{
			split =new String[1];
			split[0] =trimData;
		}

		CustomLog.debug("after split::"+split);

		return split;
	}

	private boolean CheckArm(String sensor) {

		for(int i=0;i<profileArmLength;i++){

			if(splitProfileCheck[i].equalsIgnoreCase(sensor)){
				return true;				
			}
		}

		return false;
	}




}
