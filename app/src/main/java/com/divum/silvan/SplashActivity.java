package com.divum.silvan;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.divum.callback.HomeListCallback;
import com.divum.callback.ProfileCallback;
import com.divum.constants.GetAPI;
import com.divum.entity.ConfigEntity;
import com.divum.imagedownloder.ImageCacheDetails;
import com.divum.imagedownloder.LoadImageView;
import com.divum.parser.ConfigParser;
import com.divum.utils.App_Variable;
import com.divum.utils.CustomDialog;
import com.divum.utils.CustomLog;
import com.divum.utils.GetProfile;
import com.divum.utils.HomeList;

//import com.crittercism.app.Crittercism;

public class SplashActivity extends ActionBarActivity implements ProfileCallback,HomeListCallback{

	//private ProgressDialog progressDialog;
	private String errorString;

	private String tag="SplashActivity";
	private ProgressBar progress;
	//	private VDBSensor vb;
	//	private SensorStatus ss;
	//	private VDPStatus vp;
	private SharedPreferences storagePref;
	private GetProfile gp;
	private ConfigBg cp;

	private ImageView img_splash;
	private LoadImageView loadImage;
	private boolean _isNetworkConnection=false;
	public static Context context;
	public boolean isunlock=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		//		//        .detectDiskReads()
		//		//        .detectDiskWrites()
		//		.detectNetwork()   // or .detectAll() for all detectable problems
		//		.penaltyDeath()
		//		.build());
		super.onCreate(savedInstanceState);


		setContentView(R.layout.splash_view);
		App_Variable.appMinimize=1;

		context =this;
		//Crittercism.initialize(getApplicationContext(), "55e7f9d4d224ac0a00ed3c08");



	}

	private void functionSplash() {
		String network=App_Variable.getNetwork(SplashActivity.this);
		//System.out.println("Network::: "+network);

		if(!network.trim().equals("")){
			_isNetworkConnection=true;
			SharedPreferences storagePref = getSharedPreferences("IP", MODE_PRIVATE);
			SharedPreferences.Editor storeConfig=storagePref.edit();

			if(network.equals("3g")){
				storeConfig.putString("network","3g");
				App_Variable.m3G_CONNECTED=true;
				Toast.makeText(SplashActivity.this, network.toUpperCase()+" Connected", Toast.LENGTH_SHORT).show();
				App_Variable.ipType="static";
			}else{
				storeConfig.putString("network","wifi");
				App_Variable.mWIFI_CONNECTED=true;
				Toast.makeText(SplashActivity.this, network+" connected", Toast.LENGTH_SHORT).show();
				App_Variable.ipType="local";
			}
			storeConfig.commit();
		}
		else{
			_isNetworkConnection=false;

			App_Variable.m3G_CONNECTED=false;
			App_Variable.mWIFI_CONNECTED=false;

		}

		if(_isNetworkConnection){

			if(loadImage==null){
				progress=(ProgressBar)findViewById(R.id.progress);

				storagePref=getSharedPreferences("IP", MODE_PRIVATE);

				CustomLog.latest("onCreate in splash");
				img_splash =(ImageView)findViewById(R.id.img_splash);

				ImageCacheDetails.ImageStore(this);
				loadImage=new LoadImageView(this);

				App_Variable.CONFIG_NUMBER=storagePref.getString("config_no", "");
				GetAPI.BASE_URL=storagePref.getString("local_ip", "").trim();
				GetAPI.BASE_PUBLIC_URL=storagePref.getString("static_ip", "").trim();
				App_Variable.ipType=storagePref.getString("ipType", "None").trim();



				CustomLog.resume("local:" + GetAPI.BASE_URL);
				CustomLog.resume("static:"+GetAPI.BASE_PUBLIC_URL);
				CustomLog.resume("ipType:"+App_Variable.ipType);
				if(App_Variable.CONFIG_NUMBER.equals("")) {
					Intent in = new Intent(SplashActivity.this, GoogleActivity.class);
					startActivity(in);
				}
				/*try {
					setTimeDelay();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		}

	}

	@Override
	protected void onStart() {
		CustomLog.latest("start in splash");

		super.onStart();
	}

	@Override
	protected void onResume() {
		CustomLog.latest("onResume in splash");
		functionSplash();
		SharedPreferences sharedPreferences = getSharedPreferences("SUGGESTION_PREF", MODE_PRIVATE);
		boolean b=sharedPreferences.getBoolean("value",false);
		if(_isNetworkConnection){
		if(b==true)
		{
			try {
				setTimeDelay();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}}}
		else {
			App_Variable.ShowNoNetworkPopUp(this, "splash");
		}
		super.onResume();
	}


	private void ShowIPAddress() {

		progress.setVisibility(View.GONE);


		String ipLocal=storagePref.getString("local_ip", "");
		String ipStatic=storagePref.getString("static_ip", "");

		//showIPDialog(SplashActivity.this);
		if(ipLocal.length()==0 && ipStatic.length()==0)
			showIPDialog(SplashActivity.this);
		else{
			GetAPI.BASE_URL =ipLocal.trim();
			GetAPI.BASE_PUBLIC_URL =ipStatic.trim();
			//callHomeList(false);

			callProfile(App_Variable.ipType);
		}

	}
	Handler mHandler = new Handler();
	boolean isConfigPopup=true;
	public void setTimeDelay() throws InterruptedException {
		CustomLog.latest("setTimeDelay");
		if(App_Variable.CONFIG_NUMBER.equals("")){
			ShowCofigPopUp(SplashActivity.this);
		}else{

			ShowNextSplash();
		}
	}

	CustomDialog configAlert;
	EditText txtConfigNumber;
	private void ShowCofigPopUp(final Context context) {
		configAlert = new CustomDialog(context,R.style.customDialogTheme);//customDialogTheme
		configAlert.setCanceledOnTouchOutside(false);
		configAlert.setContentView(R.layout.config_dialog_view);
		//configAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		txtConfigNumber=(EditText)configAlert.findViewById(R.id.txtConfigNumber);
		txtConfigNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {


				GetConfigNumber();
				// TODO Auto-generated method stub
				return false;
			}
		});

		Button btn_config_ok=(Button)configAlert.findViewById(R.id.btn_config_ok);

		btn_config_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				GetConfigNumber();

				//display_web_content(detail[0]);
			}
		});
		Button btnDemo=(Button)configAlert.findViewById(R.id.btn_demo);
		btnDemo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				App_Variable.CONFIG_NUMBER="0000";
				StorageIPAddr("demo");
				App_Variable.ipType="Local";
				configAlert.dismiss();

				ShowNextSplash();

			}
		});



		configAlert.show();

	}

	protected void GetConfigNumber() {
		final String number=txtConfigNumber.getText().toString().trim();
		if(number.equals("")){
			Toast.makeText(SplashActivity.this, "Please enter your config number", Toast.LENGTH_SHORT).show();
		}else if(number.length()!=4){
			txtConfigNumber.setError("Enter valid config number");
		}
		else{
			//System.out.println("1");
			configAlert.dismiss();

			App_Variable.CONFIG_NUMBER=number;

			new Thread(new Runnable() {

				@Override
				public void run() {
				//	System.out.println("2");
					// TODO Auto-generated method stub
					SharedPreferences.Editor store=storagePref.edit();
					store.putString("config_no",number);
					store.commit();

					StorageIPAddr("normal");
					//progress.setVisibility(View.VISIBLE);

					//progress.setVisibility(View.GONE);
				}
			}).start();

			ShowNextSplash();


		}
	}
	private void ShowNextSplash() {
		progress.setVisibility(View.VISIBLE);

		SharedPreferences storagePref = getSharedPreferences("CONFIG", MODE_PRIVATE);
		boolean isConfig=storagePref.getBoolean("IsConfig", false);

		if(!isConfig){
			CallConfigParser(App_Variable.CONFIG_NUMBER);
		}else{
			App_Variable.getConfigData(SplashActivity.this);

			String url= "https://s3.amazonaws.com/silvanlabs/apps/has/"+App_Variable.CONFIG_NUMBER+"/images/android_hdpi/splash_image.jpg";
			LoadImageView.SplashImage(url, img_splash,SplashActivity.this);
		}

		if(App_Variable.CONFIG_NUMBER.equals("1001")){
			//	img_splash.setBackgroundResource(R.drawable.splash_image_sobha);
		}else if(App_Variable.CONFIG_NUMBER.equals("1002")){
			//	img_splash.setBackgroundResource(R.drawable.splash_image_chennaisilks);
		}else{
			//App_Variable.CONFIG_NUMBER="0000";
			//	img_splash.setBackgroundResource(R.drawable.splash_image_sobha);
		}

		TextView txtVersion=(TextView)findViewById(R.id.txtVersion);
		txtVersion.setTextColor(getResources().getColor(R.color.white));



		//SetTimeAnotherDelay();
	}

	private void CallConfigParser(String configNumber){
		if(App_Variable.isNetworkAvailable(SplashActivity.this)){
			if(cp!=null)
				cp.cancel(true);

			cp=new ConfigBg(configNumber);
			cp.execute();
		}else{
			showNoConnectionPopUp();

			//	Toast.makeText(getApplicationContext(), "No Network connection", Toast.LENGTH_SHORT).show();
		}
	}

	private void showNoConnectionPopUp() {
		AlertDialog alertDialog=new AlertDialog.Builder(this)
				.setMessage(App_Variable.NoConnectionMsg)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						//showIPDialog(SplashActivity.this);
						CallConfigParser(App_Variable.CONFIG_NUMBER);

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				})
				.create();
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
		WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
		//wmlp.gravity = Gravity.TOP | Gravity.LEFT;
		wmlp.y=120;
		alertDialog.show();

	}

	class ConfigBg extends AsyncTask<Void, Void, Void>{
		private String exception="";
		private ConfigEntity entityConfig;
		private String configNumber="";

		public ConfigBg(String _configNumber) {
			configNumber= _configNumber;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ConfigParser parser =new ConfigParser(GetAPI.CONFIG_URL+configNumber+GetAPI.CONFIG_URL1,SplashActivity.this);
			exception = parser.getException();
			entityConfig = parser.getConfigData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			if(!exception.equals("")){
				//App_Variable.ShowErrorToast(exception, SplashActivity.this);
				showNoConnectionPopUp();
			}else{
				if(entityConfig!=null){

					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							SharedPreferences storagePref = getSharedPreferences("CONFIG", MODE_PRIVATE);
							SharedPreferences.Editor storeConfig=storagePref.edit();
							storeConfig.putString("EMS",entityConfig.getEMS());
							storeConfig.putString("CAMERA",entityConfig.getCamera());
							storeConfig.putString("LIFESTYLE",entityConfig.getLifestyle());
							storeConfig.putString("SENSOR",entityConfig.getSensors());
							storeConfig.putString("PANIC",entityConfig.getPanic());
							storeConfig.putString("DOOR_UNLOCK",entityConfig.getDoor_unlock());
							storeConfig.putBoolean("IsConfig", true);
							storeConfig.commit();

							CustomLog.show("entityConfig.getPanic():"+entityConfig.getPanic());

							App_Variable.getConfigData(SplashActivity.this);
						}
					}).start();
				}

				String url= GetAPI.Splash_URL1+App_Variable.CONFIG_NUMBER+GetAPI.Splash_URL2;
				LoadImageView.SplashImage(url, img_splash,SplashActivity.this);
			}

			//			App_Variable.STATUS_EMS = "0";
			//			App_Variable.STATUS_CAMERA = "1";
			//			App_Variable.STATUS_LIFESTYLE = "1";
			//			App_Variable.STATUS_SENSORS = "1";
			//			App_Variable.STATUS_PANIC="1";
			//			App_Variable.STATUS_DOOR_UNLOCK="1";




			super.onPostExecute(result);
		}
	}


	boolean ipPopup=true;
	private void SetTimeAnotherDelay() {
		CustomLog.latest("setTimeDelay");
		progress.setVisibility(View.GONE);

		String view=storagePref.getString("view", "");
		if(view.equals("demo")){
			callProfile(App_Variable.ipType);
		}else{
			ShowIPAddress();
		}


	}
	CustomDialog alert;
	EditText txtIPAddress,txtIPStatic,txtLocalIPVDB,txtStaticIPVDB;
	private void showIPDialog(final Context context) {

		// TODO Auto-generated method stub

		alert = new CustomDialog(context,R.style.customDialogTheme);//customDialogTheme
		alert.setCanceledOnTouchOutside(false);
		alert.setContentView(R.layout.ipdialog_view);
		//alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		txtIPAddress=(EditText)alert.findViewById(R.id.txtIPAddress);

		txtIPStatic=(EditText)alert.findViewById(R.id.txtIPStatic);

		txtLocalIPVDB=(EditText)alert.findViewById(R.id.txtLocalIPVDB);
		txtStaticIPVDB=(EditText)alert.findViewById(R.id.txtStaticIPVDB);
		txtStaticIPVDB.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				GetIPAddress();
				// TODO Auto-generated method stub
				return false;
			}
		});

		Button btn_ok=(Button)alert.findViewById(R.id.btn_ok);

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				GetIPAddress();

				//display_web_content(detail[0]);
			}
		});



		alert.show();


	}
	//String IP="",IPStatic=""/*,IPExternal=""*/;


	private void GetIPAddress() {


		CustomLog.debug("GetAPI.BASE_URL:"+GetAPI.BASE_URL+"GetAPI.BASE_URL");
		if(!txtIPAddress.getText().toString().trim().endsWith("/")) {
			GetAPI.BASE_URL = txtIPAddress.getText().toString().trim();
		}
		else{
			txtIPAddress.setError("Enter valid Local IP");
		}
		if(!txtIPStatic.getText().toString().trim().endsWith("/")) {
			GetAPI.BASE_PUBLIC_URL = txtIPStatic.getText().toString().trim();
		}else
		{
			txtIPAddress.setError("Enter valid public IP");
		}
		if(!txtLocalIPVDB.getText().toString().trim().endsWith("/")) {
			GetAPI.BASE_LOCAL_VDB = txtLocalIPVDB.getText().toString().trim();
		}else{
			txtIPAddress.setError("Enter valid LocalVDB IP");
		}
		if(!txtStaticIPVDB.getText().toString().trim().endsWith("/")) {
			GetAPI.BASE_STATIC_VDB = txtStaticIPVDB.getText().toString().trim();
		}else{
			txtIPAddress.setError("Enter valid PublicVDB IP");
		}
		//IPExternal =txtIPExternal.getText().toString().trim();
		//IP="192.168.1.231";

		if(CheckIPEmpty()){
			Toast.makeText(SplashActivity.this, "Please enter ip address", Toast.LENGTH_SHORT).show();
		}else {
			if (CheckVDBEmpty()) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(SplashActivity.this)
						.setMessage("VDB is not set.");
				builder1.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						showIPDialog(SplashActivity.this);
						//dialog.dismiss();
					}
				})
						.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										String ipAddr = "";
										if (App_Variable.m3G_CONNECTED) {
											if (GetAPI.BASE_PUBLIC_URL.equals("")) {
												Toast.makeText(SplashActivity.this, "Please enter static ip address", Toast.LENGTH_SHORT).show();
												return;
											} else {
												App_Variable.ipType = "Static";
												ipAddr = GetAPI.BASE_PUBLIC_URL;
											}
										} else if (App_Variable.mWIFI_CONNECTED) {
											if (GetAPI.BASE_URL.equals("")) {
												Toast.makeText(SplashActivity.this, "Please enter local ip address", Toast.LENGTH_SHORT).show();
												return;
											} else {
												App_Variable.ipType = "Local";
												ipAddr = GetAPI.BASE_URL;
											}
										}


										StorageIPAddr(ipAddr);
										callProfile(App_Variable.ipType);
										dialog.dismiss();

									}
								}).create()
						          .show();
			}else{

			String ipAddr = "";
			if (App_Variable.m3G_CONNECTED) {
				if (GetAPI.BASE_PUBLIC_URL.equals("")) {
					Toast.makeText(SplashActivity.this, "Please enter static ip address", Toast.LENGTH_SHORT).show();
					return;
				} else {
					App_Variable.ipType = "Static";
					ipAddr = GetAPI.BASE_PUBLIC_URL;
				}
			} else if (App_Variable.mWIFI_CONNECTED) {
				if (GetAPI.BASE_URL.equals("")) {
					Toast.makeText(SplashActivity.this, "Please enter local ip address", Toast.LENGTH_SHORT).show();
					return;
				} else {
					App_Variable.ipType = "Local";
					ipAddr = GetAPI.BASE_URL;
				}
			}


			StorageIPAddr(ipAddr);
			callProfile(App_Variable.ipType);


			/*	if (CheckValidateIP(ipAddr)) {
				StorageIPAddr(ipAddr);
				callProfile();
			}else{
				Toast.makeText(SplashActivity.this, "Please enter valid ip address", Toast.LENGTH_SHORT).show();

			}*/


		}

	}}








	private void StorageIPAddr(String ipAddr) {
		/*if(!ipAddr.startsWith("http")){
			ipAddr ="http://"+ipAddr;
		}
		if(ipAddr.endsWith("/"))
			ipAddr =ipAddr.substring(0,ipAddr.length()-1);*/


		//GetAPI.BASE_URL = ipAddr;
		if(alert!=null){
			alert.dismiss();
		}

		// TODO Auto-generated method stub
		SharedPreferences.Editor store=storagePref.edit();
		if(ipAddr.equals("demo")){
			store.putString("view", "demo");
		}else{
			store.putString("local_ip", GetAPI.BASE_URL);
			store.putString("static_ip", GetAPI.BASE_PUBLIC_URL);
			store.putString("ipType", App_Variable.ipType);
			store.putString("local_vdb", GetAPI.BASE_LOCAL_VDB);
			store.putString("static_vdb", GetAPI.BASE_STATIC_VDB);
			store.putString("view", "normal");
		}
		store.commit();

	}



	private boolean CheckIPEmpty() {
		if(GetAPI.BASE_PUBLIC_URL.equals("")&&GetAPI.BASE_URL.equals("")){//&&IPExternal.equals("")
			return true;
		}

		return false;
	}
	private boolean CheckVDBEmpty(){
		if (GetAPI.BASE_LOCAL_VDB.equals("")&&GetAPI.BASE_STATIC_VDB.equals("")){
			return true;
		}
		return false;
	}



	private void callProfile(String typeIP) {
		if(App_Variable.isNetworkAvailable(SplashActivity.this)){
			if(gp!=null)
				gp.cancel(true);
			progress.setVisibility(View.VISIBLE);
			gp=new GetProfile(SplashActivity.this,false,typeIP);
			gp.execute();
		}else{
			App_Variable.ShowNoNetwork(this);
			//Toast.makeText(getApplicationContext(), "No Network connection", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void callHomeList(boolean isRefresh) {
		progress.setVisibility(View.GONE);

		if(App_Variable.isNetworkAvailable(SplashActivity.this)){
			//listOfCity=new ArrayList<CityEntity>();
			progress.setVisibility(View.VISIBLE);
			HomeList home=new HomeList(SplashActivity.this);
			home.execute();
		}else{

			//showTyrAgain("city");
			progress.setVisibility(View.GONE);
			App_Variable.ShowNoNetwork(this);
			//Toast.makeText(getApplicationContext(), "No Network connection", Toast.LENGTH_SHORT).show();

		}

	}

	@Override
	public void callProfileExceptionPopup(String exception,String ipType) {
		if(App_Variable.m3G_CONNECTED){
			App_Variable.ipType="None";
			progress.setVisibility(View.GONE);
			showErrorPopup(exception);
		}else{
			if(ipType.equalsIgnoreCase("local")){
				if(GetAPI.BASE_PUBLIC_URL.equals("")){
					progress.setVisibility(View.GONE);
					showErrorPopup(exception);
				}else{
					//App_Variable.ShowErrorToast("Local IP not working, so switiching to Static IP", SplashActivity.this);
					App_Variable.ipType="Static";
					StorageIPAddr(GetAPI.BASE_PUBLIC_URL);
					callProfile(App_Variable.ipType);
				}
			}else{
				App_Variable.ipType="None";
				progress.setVisibility(View.GONE);
				showErrorPopup(exception);
			}
		}

	}

	@Override
	public void callExceptionPopup(String exception) {
		progress.setVisibility(View.GONE);
		showErrorPopup(exception);

	}

	private void showErrorPopup(String exception) {
		String errorMsg=exception.trim().toLowerCase();
		if(errorMsg.contains("host")){
			errorMsg=App_Variable.MSG_CONNECT_EXCEPTION;
		}else if(errorMsg.contains("connect")){
			errorMsg=App_Variable.MSG_CONNECT_EXCEPTION;
		}else{
			errorMsg=App_Variable.MSG_CONNECT_EXCEPTION;
		}

		AlertDialog alertDialog=new AlertDialog.Builder(this)
				.setMessage(errorMsg)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						showIPDialog(SplashActivity.this);

					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				})
				.create();
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();

	}


		@Override
	public void StartBGService( Context context,String whichScreen) {

		StartService(context);

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//String page=data.getExtras().getString("page");
		if(resultCode==10)
		{
			Intent intentHome=new Intent(context,HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentHome.putExtra("position", -1);
			intentHome.putExtra("room", "");
			intentHome.putExtra("screen", "splash");
			intentHome.putExtra("nextPage", "");
			startActivity(intentHome);
			finish();

		}
	}


	private void StartService(final Context context) {

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				//showToast(msg.what);

				CustomLog.camera("camera sensor VDBSTATUS1-> Service handleMessage:" + msg.what+","+App_Variable.appMinimize);
				CustomLog.camera(tag, "camera VDBSTATUS1 sensor StartBGService:" + App_Variable.appMinimize);

				SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
				boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);



				//System.out.println("camera isunlock value"+isunlock);
					if(msg.what==0){
					if(App_Variable.appMinimize==1){

							if (checkvalue) {
								Intent in = new Intent(SplashActivity.this, UnlockActivity.class);
								startActivityForResult(in, 100);
							} else {
								Intent intentHome = new Intent(context, HomeActivity.class);
								intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intentHome.putExtra("position", -1);
								intentHome.putExtra("room", "");
								intentHome.putExtra("screen", "splash");
								intentHome.putExtra("nextPage", "");
								startActivity(intentHome);
								finish();

							}
						}


				}else if(msg.what==1){
					/* sensor */
					if(App_Variable.appMinimize==1){

							if (!App_Variable.isSensorPage) {
								Intent intentHome = new Intent(context, HomeActivity.class);
								intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intentHome.putExtra("position", -1);
								intentHome.putExtra("room", "");
								intentHome.putExtra("screen", "splash");
								intentHome.putExtra("nextPage", "sensor");
								startActivity(intentHome);
								finish();
							}

					}

				}else if(msg.what==2){
					/* camera */
					if(App_Variable.appMinimize==1){
						/*Intent intentHome=new Intent(context,HomeActivity.class);	
						intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intentHome.putExtra("position", -1);
						intentHome.putExtra("room", "");
						intentHome.putExtra("screen", "camera");
						startActivity(intentHome);
						finish();*/
						App_Variable.hashListCamera=null;

						Intent intentCamera=new Intent(context,CameraActivity.class);
						intentCamera.putExtra("page", 1);
						intentCamera.putExtra("screen", "vdb");
						intentCamera.putExtra("vdb_trigger", 1);
						startActivity(intentCamera);
					}
				}

				if(progress!=null)
					progress.setVisibility(View.GONE);
			}
		};

		App_Variable._isServiceStarted=true;
		final Intent intent = new Intent(context, MyService.class);
		final Messenger messenger = new Messenger(handler);
		intent.putExtra("messenger", messenger);
		startService(intent);
		CustomLog.camera("camera Service called");


	}






	private void showToast(int messageId) {
		Toast.makeText(this, "Message  " + messageId, Toast.LENGTH_SHORT).show();
	}



	public boolean CheckSensorStatus() {
		if(App_Variable.SENSOR_STATUS!=null||!App_Variable.SENSOR_STATUS.isEmpty()){
			String[] sensorResponse=App_Variable.SENSOR_STATUS.trim().split("\n");
			int length = sensorResponse.length;
			for (int i = 0; i < length; i++) {
				//String[] split=sensorResponse[i].split("=");
				if(sensorResponse[i].endsWith("1"))
					return true;
				else if(sensorResponse[i].endsWith("2"))
					return true;
			}
		}else{
			return false;
		}

		return false;

	}

	public void notifyUser(){

		NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		intent.putExtra("position", -1);
		intent.putExtra("room", "");
		intent.putExtra("screen", "splash");

		//use the flag FLAG_UPDATE_CURRENT to override any notification already there
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new Notification(R.drawable.ic_launcher, "Silvan", System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;

		notification.setLatestEventInfo(this, "Silvan", "Your sensor is on", contentIntent);
		//10 is a random number I chose to act as the id for this notification
		notificationManager.notify(10, notification);
	}

	public void NextScreen() {

		SetTimeAnotherDelay();
	}


}

/*public void callVDBSensorParser() {
	if(App_Variable.isNetworkAvailable(SplashActivity.this)){
		if(vb!=null)
			vb.cancel(true);
		vb=new VDBSensor();
		vb.execute();
	}else{
		//callVDBSensorParser();
	}
}
class VDBSensor extends AsyncTask<Void, Void, Void>{

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.VDBSTATUS_URL+"?"+System.currentTimeMillis());
		App_Variable.VDBSTATUS=parser.getResponse();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		CustomLog.d(tag, "VDBSTATUS:"+App_Variable.VDBSTATUS);
 *//**
 * If VDBSTATUS=1, navigate VDB page
 * If VDBSTATUS=7, navigate Sensor page
 * If VDBSTATUS=0, ignore
 *//*
		callSensorStatus();
		super.onPostExecute(result);
	}
}

public void callSensorStatus() {
	if(App_Variable.isNetworkAvailable(SplashActivity.this)){
		if(ss!=null)
			ss.cancel(true);
		ss=new SensorStatus();
		ss.execute();
	}else{
		//callSensorStatus();
	}
}
class SensorStatus extends AsyncTask<Void, Void, Void>{

	private String response="";
	@Override
	protected Void doInBackground(Void... params) {
		BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.SENSORSTATUS_URL+"?"+System.currentTimeMillis());
		response=parser.getResponse();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		CustomLog.d(tag, "SensorStatus:"+response);
		App_Variable.SENSOR_STATUS=response;
  *//**
 * if response =1 means goto sensor page
 *//*
		//callVDPStatus();

		callListParser();

		super.onPostExecute(result);
	}

}

public void callVDPStatus() {
	if(App_Variable.isNetworkAvailable(SplashActivity.this)){
		if(vp!=null)
			vp.cancel(true);
		vp=new VDPStatus();
		vp.execute();
	}else{
		//callVDPStatus();
	}

}

class VDPStatus extends AsyncTask<Void, Void, Void>{

	private String response="";
	@Override
	protected Void doInBackground(Void... params) {
		BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.VDPSTATUS_URL);
		response=parser.getResponse();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		CustomLog.d(tag, "VDPStatus:"+response);
   *//**
 * Only for vdb,If the 5th line has alarmin.status=1, then need to go the VDB page
 *//*
		callVDBSensorParser();

		super.onPostExecute(result);
	}

}*/

/*private void HMAc() {
	// date_default_timezone_set("UTC");
	long currentTime=System.currentTimeMillis()/1000;
	String clientId="1234";
	String privateKey="Arun";

	System.out.println("time:"+currentTime);


	try {
		System.out.println("hashkey:"+hmacSha1(clientId+""+currentTime,privateKey));
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	System.out.println("");


}

private String hmacSha1(String value, String key)
		throws UnsupportedEncodingException, NoSuchAlgorithmException,
		InvalidKeyException {
	String type = "HmacSHA1";
	SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
	Mac mac = Mac.getInstance(type);
	mac.init(secret);
	byte[] bytes = mac.doFinal(value.getBytes());
	return bytesToHex(bytes);
}

private final static char[] hexArray = "0123456789abcdef".toCharArray();

private static String bytesToHex(byte[] bytes) {
	char[] hexChars = new char[bytes.length * 2];
	int v;
	for (int j = 0; j < bytes.length; j++) {
		v = bytes[j] & 0xFF;
		hexChars[j * 2] = hexArray[v >>> 4];
		hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	}
	return new String(hexChars);
}*/