package com.divum.silvan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.divum.adapter.ACLevelAdapter;
import com.divum.adapter.FanLevelAdapter;
import com.divum.adapter.HomeListAdapter;
import com.divum.adapter.SettingsAdapter;
import com.divum.callback.DimmerLightCallback;
import com.divum.callback.HomeListCallback;
import com.divum.callback.ProfileCallback;
import com.divum.callback.UpdateProileCallback;
import com.divum.constants.GetAPI;
import com.divum.customview.ExpandableHeightGridView;
import com.divum.entity.ConfigEntity;
import com.divum.entity.HomeEntity;
import com.divum.entity.MoodEntity;
import com.divum.parser.BaseParser;
import com.divum.parser.ConfigParser;
import com.divum.silvan.callback.MoodDataCallback;
import com.divum.utils.App_Variable;
import com.divum.utils.BackgroundThread;
import com.divum.utils.CommonPingURL;
import com.divum.utils.CustomDialog;
import com.divum.utils.CustomLog;
import com.divum.utils.GetMooodData;
import com.divum.utils.GetProfile;
import com.divum.utils.HomeList;
import com.divum.utils.UpdateProfile;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("NewApi")
public class HomeActivity extends SlidingFragmentActivity implements OnClickListener, MoodDataCallback, ProfileCallback, HomeListCallback,UpdateProileCallback,DimmerLightCallback/*ProfileUpdateCallback ,BgThreadCallback,*/{


	//private LinkedHashMap<String, ArrayList<String>> hashRoomType=null; HomeActivity.class
	//private Hashtable<String, ArrayList<Hashtable<String, HomeEntity>>> hashRoomOptions=null;
	private String[] settings_value={"Sensor Status","Refresh","Log","Profile","Mood","Ip","About us","Pincode settings"};
	SlidingMenu sm ;
	private ArrayList<Hashtable<String, HomeEntity>> listData;

	private RelativeLayout homeView/*,profileView*/;

	private String tag="HomeActivity";
	private LinearLayout layoutCamera,layoutPanic,layoutHooter,layoutVDB;
	private LinearLayout layoutProfile;
	public boolean radiant_value=true;
	public boolean isActivityResult=false;
    private ProgressBar progressBar;
	private TextView txt_homeProfile;
	private ImageView img_profile;
	private ImageView img_slider;
	private LinearLayout tab_layout;
   // private ArrayList<String> listtype;
	private GetProfile gp;
	public  int currenttab=0;
   // public boolean Notabsys=false,Notabac=false,Notabmedia=false,Notabele=false;
	private GetRoomStatus grs;
	//private SlidingMenu sm;
	private TextView tab_system;
	private TextView tab_ac;
	private TextView tab_media;
	private TextView tab_electronics;
	private String _whichSection="";
	private int _whichPosition=0;
	private HashMap hashDimmerStatus=null;
	private LinearLayout view_system,view_ac;
	private ScrollView view_media;
	private WebView view_electricals;
	private LinearLayout view_tv,view_stb;
	private LinearLayout view_settings/*,view_log*/;


	private ArrayList<String> list_system=new ArrayList<String>();
	private ArrayList<String> list_ac=new ArrayList<String>();
	private ArrayList<String> list_media=new ArrayList<String>();
	private ArrayList<String> list_electricals=new ArrayList<String>();
	private LinkedHashMap<String, String> listhashtype;
	private LinkedHashMap<String, String>ListEleurl;
	private LinkedHashMap<String, String>Listtabname;

	private String EleURL;

	private ArrayList<String> listDimmers=new ArrayList<String>();
	private ArrayList<String> listCurtains=new ArrayList<String>();
	private ArrayList<String> listLights=new ArrayList<String>();
    private String listtype;
	private ArrayList<String> listFanLvlKey,listAcLvlKey,listFanLvlValue,listAcLvlValue;
	private String[] moodNames={"Master On","Master Off","Morning","Evening","Dinner"};
	private int[] moodIndex={1,2,3,4,5};

    int k=App_Variable.hashtype.size();


	//private HashMap<String, MoodEntity> hashMood;
	//private HashMap<String, String> hashMoodFind;
	private BackgroundThread bgProcess;
	private ProgressDialog progressDialog;

	private String screen="";
	private SharedPreferences STORAGE;
	private boolean _refreshingDimmerStatus=true,isCall=true,_isUpdateProfile=true;

	private  boolean _isUpdateManual=false;
	private ImageView imgMoodOn,imgMoodOff,imgMoodMorning,imgMoodEvening,imgMoodDinner;

	private ImageView img_tab_system_bottom;
	private ImageView img_tab_ac_bottom;
	private ImageView img_tab_media_bottom;
	private ImageView img_tab_ele_bottom;

	private LinearLayout header_system;
	private RelativeLayout header_ac,header_media,header_electronics,webview_lay;
	private TextView txtNoSensorHome;
	//private LinkedHashMap<String, String> hashNickName=null;
	private Timer timer;
	private String nextPage;
	// whenever click on or off button _isChangeManualIcon become true and manually change icon
	/*public HomeActivity() {
		super(R.string.app_name);
		// TODO Auto-generated constructor stub 11-07 18:11:34.070: I/System.out(19793): App_Variable.STATUS_SENSORS:1

	}*/
	private SharedPreferences storagePref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//try{

		super.onCreate(savedInstanceState);
		App_Variable.appMinimize=1;
		App_Variable.getConfigData(HomeActivity.this);
		storagePref=getSharedPreferences("IP", MODE_PRIVATE);

		HomeListAdapter.selectedPos=0;


		//hashMoodFind=App_Variable.hashMoodFind;

		setBehindContentView(R.layout.slide_menu);
		setContentView(R.layout.home_view);

		SharedPreferences pref_time=getSharedPreferences("TimeDiff", MODE_PRIVATE);
		SharedPreferences.Editor editor_start1 =pref_time.edit();
		editor_start1.putString("page", "const");
		editor_start1.commit();


		STORAGE=getSharedPreferences("IP", MODE_PRIVATE);
		GetAPI.BASE_URL=App_Variable.getBaseAPI(HomeActivity.this);

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		hashDimmerStatus=new HashMap();
		txtNoSensorHome = (TextView) findViewById(R.id.txtNoSensorHome);
		txtNoSensorHome.setVisibility(View.GONE);

		txt_homeProfile=(TextView)findViewById(R.id.txt_homeProfile);
		img_profile=(ImageView) findViewById(R.id.img_profile);
		displayProfile();

		screen=getIntent().getStringExtra("screen");
		Bundle bundle=getIntent().getExtras();
		nextPage=bundle.getString("nextPage", "");

		progressDialog=new ProgressDialog(this);
		//	progressDialog.setMessage("Loading ...");
		//	progressDialog.show();

		if(screen.equalsIgnoreCase("splash")){
			if(App_Variable.STATUS_SENSORS.equals("1")){
				AlertGotoSensor();
			}
		}else if(screen.equalsIgnoreCase("camera")){
			CameraView();
		}else if(screen.equalsIgnoreCase("others")){
			callProfile(false,false,"");
		}


		/**
		 * Fragment
		 */
		FragmentTransaction frag = this.getSupportFragmentManager().beginTransaction();
		//SlideViewer mFrag = new SlideViewer("home");
		frag.replace(R.id.menu_slide, SlideViewer.newInstance("home"));
		frag.commit();

		/**
		 * Slider
		 */
		sm = getSlidingMenu();
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setMode(SlidingMenu.LEFT);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);//0.35f
		sm.setBehindScrollScale(0.25f);
		sm.toggle();

		/**
		 * Slider action
		 */
		img_slider=(ImageView)findViewById(R.id.img_slider);
		img_slider.setOnClickListener(this);


		//String url= GetAPI.Splash_URL1+App_Variable.CONFIG_NUMBER+GetAPI.Splash_URL2;
		//LoadImageView.LoadImage(url, img_slider);


		/**
		 * set current date and day
		 */
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			String now = format.format(new Date());
			Date todaydate = format.parse(now);

			String[] strDate=(todaydate+"").split(" ");
			CustomLog.debug("today date::"+todaydate+","+todaydate.getYear());
			TextView txt_date=(TextView)findViewById(R.id.txt_date);
			txt_date.setText(todaydate.getDate()+"-"+(todaydate.getMonth()+1)+"-"+strDate[strDate.length-1]);

			TextView txt_day=(TextView)findViewById(R.id.txt_day);
			txt_day.setText(App_Variable.getDay(todaydate.getDay()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * check room is empty.
		 * If it is not empty navigate to appropriate room view
		 */
		int index=getIntent().getIntExtra("position", 0);
		String room=getIntent().getStringExtra("room");

        int b=0;
		/**
		 * create instance for profile layout
		 */
		layoutProfile=(LinearLayout)findViewById(R.id.layoutProfile);
		layoutProfile.setOnClickListener(this);
		if(App_Variable.STATUS_SENSORS.equals("1")){
			b=1;
			layoutProfile.setVisibility(View.VISIBLE);

		}else{
			layoutProfile.setVisibility(View.GONE);
		}

		/**
		 * create instance for camera layout
		 */
		layoutCamera=(LinearLayout)findViewById(R.id.layoutCamera);
		layoutCamera.setOnClickListener(this);
		if(App_Variable.STATUS_CAMERA.equals("1")){
			 b=1;
			layoutCamera.setVisibility(View.VISIBLE);

		}else{
			layoutCamera.setVisibility(View.GONE);
		}

		layoutVDB=(LinearLayout) findViewById(R.id.layoutVDB);
		layoutVDB.setOnClickListener(this);
		if(App_Variable.STATUS_EMS.equals("1")){
			b=1;
			layoutVDB.setVisibility(View.VISIBLE);
		}else{
			layoutVDB.setVisibility(View.GONE);
		}

		/**
		 * Create instance for panic layout
		 */
		layoutPanic=(LinearLayout)findViewById(R.id.layoutPanic);
		layoutPanic.setOnClickListener(this);
		if(App_Variable.STATUS_PANIC.equals("1")){
			b=1;
			layoutPanic.setVisibility(View.VISIBLE);
		}else{
			layoutPanic.setVisibility(View.GONE);
		}

		/**
		 * create instace for hooter layout
		 */
		layoutHooter=(LinearLayout)findViewById(R.id.layoutHooter);
		layoutHooter.setOnClickListener(this);
		if(App_Variable.STATUS_SENSORS.equals("1")){
			 b=1;
			layoutHooter.setVisibility(View.VISIBLE);
		}else{
			layoutHooter.setVisibility(View.GONE);
		}

		//setHomeView();
		//if(isEmptyFeatue()){
			if(b==1) {
				txtNoSensorHome.setVisibility(View.GONE);
			}
		else{
			txtNoSensorHome.setVisibility(View.VISIBLE);
		}


		/**
		 * set view for profile page
		 */
		//profileView=(RelativeLayout)findViewById(R.id.profileView);

		/**
		 * set tab view
		 */
		tab_layout=(LinearLayout)findViewById(R.id.tab_layout);
		tab_layout.setVisibility(View.GONE);

		tab_system=(TextView)findViewById(R.id.tab_system);
		tab_system.setOnClickListener(this);

		tab_ac=(TextView)findViewById(R.id.tab_ac);
		tab_ac.setOnClickListener(this);

		tab_media=(TextView)findViewById(R.id.tab_media);
		tab_media.setOnClickListener(this);
		tab_electronics=(TextView)findViewById(R.id.tab_electricals);
		tab_electronics.setOnClickListener(this);

		header_system=(LinearLayout) findViewById(R.id.header_sysytem);
		header_ac=(RelativeLayout) findViewById(R.id.header_ac);
		header_media=(RelativeLayout) findViewById(R.id.header_media);
		header_electronics =(RelativeLayout) findViewById(R.id.header_electricals);
		webview_lay =(RelativeLayout) findViewById(R.id.webview_lay);
		webview_lay.setVisibility(View.GONE);

		img_tab_system_bottom=(ImageView) findViewById(R.id.img_tab_system_bottom);
		img_tab_system_bottom.setVisibility(View.VISIBLE);

		img_tab_ac_bottom=(ImageView) findViewById(R.id.img_tab_ac_bottom);
		img_tab_media_bottom=(ImageView) findViewById(R.id.img_tab_media_bottom);
		img_tab_ele_bottom=(ImageView)findViewById(R.id.img_tab_ele_bottom);


		/**
		 * set system view
		 */
		view_system=(LinearLayout)findViewById(R.id.view_system);
		view_system.setVisibility(View.GONE);

		/**
		 * set ac view
		 */
		view_ac=(LinearLayout)findViewById(R.id.view_ac);
		view_ac.setVisibility(View.GONE);
		//progressBar=(ProgressBar)findViewById(R.id.progressBar1);

		/**
		 * set media view
		 */
		view_media =(ScrollView)findViewById(R.id.view_media);
		view_media.setVisibility(View.GONE);

		view_electricals =(WebView)findViewById(R.id.view_electricals);
		view_electricals.setVisibility(View.GONE);


		/**
		 * In media, set tv and stb view
		 */
		view_tv =(LinearLayout) findViewById(R.id.view_tv);
		view_tv.setVisibility(View.GONE);

		view_stb =(LinearLayout) findViewById(R.id.view_stb);
		view_stb.setVisibility(View.GONE);

		/**
		 * set settings view
		 */
		view_settings=(LinearLayout)findViewById(R.id.view_settings);
		view_settings.setVisibility(View.GONE);


		/**
		 * set log view
		 */
		/*view_log=(LinearLayout) findViewById(R.id.view_log);
		view_log.setVisibility(View.GONE);*/

		settings_value=App_Variable.GetSettingsValue();
		if(settings_value.length==0){
			txtNoSensorHome.setVisibility(View.VISIBLE);
		}
		/*else {
			txtNoSensorHome.setVisibility(View.GONE);
		}*/



		ExpandableHeightGridView gridview_settings=(ExpandableHeightGridView)findViewById(R.id.gridview_settings);
		SettingsAdapter adapter=new SettingsAdapter(this);
		gridview_settings.setAdapter(adapter);
		//txtNoSensorHome.setVisibility(View.GONE);
		gridview_settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// TODO Auto-generated method stub
				//view_settings.setVisibility(View.GONE);
				//System.out.println("Sensor Status:::"+view.);
				if(settings_value[position].toLowerCase().equals("sensor status")){
					/*
					 * sensor status
					 */
					radiant_value=false;
					Intent intent=new Intent(HomeActivity.this,SensorActivity.class);
					startActivity(intent);

				}else if(settings_value[position].toLowerCase().equals("refresh")){
					/*
					 * refresh
					 */
                    radiant_value=false;
					CallRefresh();


				}else if(settings_value[position].toLowerCase().equals("log")){
					/*
					 * log
					 */
					radiant_value=false;
					Intent intent=new Intent(HomeActivity.this,LogActivity.class);
					startActivity(intent);
					//view_log.setVisibility(View.VISIBLE);

				}else if(settings_value[position].toLowerCase().equals("profile")){
					/*
					 * profile
					 */
					radiant_value=false;
					Intent intent=new Intent(HomeActivity.this,SettingsProfileActivity.class);
					startActivity(intent);

				}else if(settings_value[position].toLowerCase().equals("mood")){
					/*
					 * mood
					 */
					radiant_value=false;
					Intent intent=new Intent(HomeActivity.this,MoodActivity.class);
					startActivity(intent);

				}else if(settings_value[position].toLowerCase().equals("ip")){
					/*
					 * ip
					 */
					radiant_value=false;
					SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
					boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);
					String whichView=App_Variable.getDemoValue(HomeActivity.this);
					if(whichView.equals("demo")){
						ShowCofigPopUp(HomeActivity.this);
					}else{
						if(checkvalue){
                            //radiant_value=false;
							Intent in = new Intent(HomeActivity.this,UnlockActivity.class);
							in.putExtra("pagetype","ip");
							startActivityForResult(in, 100);
						}else{
							showIPDialog(HomeActivity.this);
						}
					}
				}else if(settings_value[position].toLowerCase().equals("about us")){
					/*
					 * about us
					 */
					radiant_value=false;
					Intent intent=new Intent(HomeActivity.this,AboutUsActivity.class);
					startActivity(intent);
				}
				else if (settings_value[position].toLowerCase().equals("pincode settings"))
				{
					radiant_value=false;
					Intent intent=new Intent(HomeActivity.this,PinsettingActivity.class);
					startActivity(intent);
				}

			}
		});


		//Log.d(tag, "size:"+hashRoomType.size());
		/*
		 * create Mood bottom view instance
		 */
		LinearLayout moodMasterOn =(LinearLayout) findViewById(R.id.moodMasterOn);
		moodMasterOn.setOnClickListener(this);

		LinearLayout moodMasterOff =(LinearLayout) findViewById(R.id.moodMasterOff);
		moodMasterOff.setOnClickListener(this);

		LinearLayout moodMorning =(LinearLayout) findViewById(R.id.moodMorning);
		moodMorning.setOnClickListener(this);

		LinearLayout moodEvening =(LinearLayout) findViewById(R.id.moodEvening);
		moodEvening.setOnClickListener(this);

		LinearLayout moodDinner =(LinearLayout) findViewById(R.id.moodDinner);
		moodDinner.setOnClickListener(this);

		imgMoodOn = (ImageView)findViewById(R.id.imgMoodOn);
		imgMoodOff = (ImageView)findViewById(R.id.imgMoodOff);
		imgMoodMorning = (ImageView)findViewById(R.id.imgMoodMorning);
		imgMoodEvening = (ImageView)findViewById(R.id.imgMoodEvening);
		imgMoodDinner = (ImageView)findViewById(R.id.imgMoodDinner);

		ResetMoodIcon();
		/**
		 * set view for homepage
		 */
		homeView=(RelativeLayout)findViewById(R.id.homeView);
		if(!room.isEmpty()){
			ShowViewSection(index, room);
		}else{

			homeView.setVisibility(View.VISIBLE);
		}

		//hashNickName=App_Variable.hashNickName;

		/*for(Enumeration en = hashRoomType.keys(); en.hasMoreElements();){

			String key=(String)en.nextElement();

			CustomLog.debug("key::"+key);
			ArrayList<String> list_type=hashRoomType.get(key);
			listData=hashRoomOptions.get(key);
			CustomLog.d(key, list_type.size()+"|"+listData.size());

			int listSize=list_type.size();
			for (int i = 0; i < listSize; i++) {
				String type=list_type.get(i);
				CustomLog.debug("type:::::::::::"+type);
				Hashtable<String, HomeEntity> table=listData.get(i);
				HomeEntity entity=table.get(type);
				CustomLog.debug("script:::::::::::"+entity.getScript());
			}
		}*/


		//callRoomStatus();

		//callUpdateProfile();


		//		}catch (Exception e) {
		//			System.out.println("home activity saveinstace:"+e);
		//		}

	}

	private boolean isEmptyFeatue() {
		if(App_Variable.STATUS_EMS.equals("1")||App_Variable.STATUS_CAMERA.equals("1")||App_Variable.STATUS_LIFESTYLE.equals("1")||
				App_Variable.STATUS_SENSORS.equals("1")||App_Variable.STATUS_PANIC.equals("1")||App_Variable.STATUS_DOOR_UNLOCK.equals("1")){

			return false;
		}else{
			return true;
		}
	}

	/*private void setHomeView() {
		// Convert DIPs to pixels
		int SIZE_DIP=150;
		int SPACING_DIP=10;
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mSizePx = (int) Math.floor(SIZE_DIP * metrics.scaledDensity);
		int mSpacingPx = (int) Math.floor(SPACING_DIP * metrics.scaledDensity);

		ExpandableHeightGridView gridview_home=(ExpandableHeightGridView)findViewById(R.id.gridview_home);
		HomeGridAdapter adapterHome=new HomeGridAdapter(this);
		gridview_home.setAdapter(adapterHome);

		// Find out the extra space gridview uses for selector on its sides.
		Rect p = new Rect();
		gridview_home.getSelector().getPadding(p);
		int selectorPadding = p.left + p.right;

		// Determine the number of columns we can fit, given screen width,
		// thumbnail width, and spacing between thumbnails.
		int numColumns = (int) Math.floor(1f * (metrics.widthPixels - selectorPadding + mSpacingPx)
				/ (mSizePx + mSpacingPx));

		int contentWidth = numColumns * mSizePx; // Width of items
		contentWidth += (numColumns - 1) * mSpacingPx; // Plus spaces between items
		contentWidth += selectorPadding; // Plus extra space for selector on sides

		// Now calculate amount of left and right margin so the grid gets
		// centered. This is what we
		// unfortunately cannot do with layout_width="wrap_content"
		// and layout_gravity="center_horizontal"
		int slack = metrics.widthPixels - contentWidth;

		gridview_home.setNumColumns(numColumns);
		gridview_home.setPadding(slack / 2, slack / 2, slack / 2, slack / 2);

	} */
	CustomDialog configAlert;
	EditText txtConfigNumber;
	private void ShowCofigPopUp(final Context context) {

		configAlert = new CustomDialog(context,R.style.customDialogTheme);//customDialogTheme
		configAlert.setCanceledOnTouchOutside(false);
		configAlert.setContentView(R.layout.config_dialog_view);

		TextView txtOr=(TextView)configAlert.findViewById(R.id.txtOr);
		txtOr.setVisibility(View.GONE);

		TextView btnDemo=(Button)configAlert.findViewById(R.id.btn_demo);
		btnDemo.setVisibility(View.GONE);

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

		configAlert.show();

	}

	protected void GetConfigNumber() {
		final String number=txtConfigNumber.getText().toString().trim();
		if(number.equals("")){
			Toast.makeText(HomeActivity.this, "Please enter your config number", Toast.LENGTH_SHORT).show();
		}else{
			System.out.println("1");
			configAlert.dismiss();

			App_Variable.CONFIG_NUMBER=number;

			new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println("2");
					// TODO Auto-generated method stub
					SharedPreferences.Editor store=storagePref.edit();
					store.putString("config_no",number);
					store.commit();

					StorageIPAddr("normal");
					//progress.setVisibility(View.VISIBLE);

					//progress.setVisibility(View.GONE);
				}
			}).start();

			//ShowNextSplash();
			CallConfigParser(App_Variable.CONFIG_NUMBER);


		}
	}
	ConfigBg cp;
	private void CallConfigParser(String configNumber){
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){
			if(cp!=null)
				cp.cancel(true);

			cp=new ConfigBg(configNumber);
			cp.execute();
		}else{
			App_Variable.ShowNoNetwork(HomeActivity.this);

			//	Toast.makeText(getApplicationContext(), "No Network connection", Toast.LENGTH_SHORT).show();
		}
	}

	class ConfigBg extends AsyncTask<Void, Void, Void>{
		private String exception="";
		private ConfigEntity entityConfig;
		private String configNumber="";
		private ProgressDialog progressDlg;

		public ConfigBg(String _configNumber) {
			progressDlg=new ProgressDialog(HomeActivity.this);
			progressDlg.setMessage("Please wait..");
			progressDlg.show();

			configNumber= _configNumber;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ConfigParser parser =new ConfigParser(GetAPI.CONFIG_URL+configNumber+GetAPI.CONFIG_URL1,HomeActivity.this);
			exception = parser.getException();
			entityConfig = parser.getConfigData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			progressDlg.dismiss();
			if(!exception.equals("")){
				//App_Variable.ShowErrorToast(exception, SplashActivity.this);
				App_Variable.ShowNoNetwork(HomeActivity.this);
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
							storeConfig.commit();

							CustomLog.show("entityConfig.getPanic():"+entityConfig.getPanic());

							App_Variable.getConfigData(HomeActivity.this);
						}
					}).start();

					showIPDialog(HomeActivity.this);
				}
			}

			super.onPostExecute(result);
		}
	}

	private void ResetMoodIcon() {
		imgMoodOn.setImageResource(R.drawable.mood_master_unselected);
		imgMoodOff.setImageResource(R.drawable.mood_master_unselected);
		imgMoodMorning.setImageResource(R.drawable.mood_morning_unselected);
		imgMoodEvening.setImageResource(R.drawable.mood_evening_unselected);
		imgMoodDinner.setImageResource(R.drawable.mood_dinner_unselected);
	}

	EditText txtIPAddress,txtIPStatic,txtLocalIPVDB,txtStaticIPVDB;
	CustomDialog alert;
	protected void showIPDialog(final Context context) {

		// TODO Auto-generated method stub

		alert = new CustomDialog(context,R.style.customDialogTheme);//customDialogTheme
		alert.setCanceledOnTouchOutside(true);
		alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		alert.setContentView(R.layout.ipdialog_view);


		SharedPreferences storagePref=getSharedPreferences("IP", MODE_PRIVATE);
		String ipaddr= storagePref.getString("local_ip", "").replace("http://", "");
		CustomLog.camera("base:"+ipaddr);


		txtIPAddress=(EditText)alert.findViewById(R.id.txtIPAddress);
		txtIPAddress.setText(ipaddr);
		txtIPStatic=(EditText)alert.findViewById(R.id.txtIPStatic);
		txtIPStatic.setText(storagePref.getString("static_ip", "").replace("http://", ""));

		txtLocalIPVDB=(EditText)alert.findViewById(R.id.txtLocalIPVDB);
		txtLocalIPVDB.setText(GetAPI.BASE_LOCAL_VDB);

		txtStaticIPVDB=(EditText)alert.findViewById(R.id.txtStaticIPVDB);
		txtStaticIPVDB.setText(GetAPI.BASE_STATIC_VDB);
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
		btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GetIPAddress();

				//display_web_content(detail[0]);
			}
		});

		alert.show();


	}
	String IP="",IPStatic="",VDB="",VDBStatic="";//,IPExternal=""
	protected void GetIPAddress() {
		CustomLog.debug("GetAPI.BASE_URL:"+GetAPI.BASE_URL+"GetAPI.BASE_URL");
		IP =txtIPAddress.getText().toString().trim();
		IPStatic =txtIPStatic.getText().toString().trim();
		VDB=txtLocalIPVDB.getText().toString().trim();
		VDBStatic=txtStaticIPVDB.getText().toString().trim();
//		GetAPI.BASE_LOCAL_VDB=txtLocalIPVDB.getText().toString().trim();
//		GetAPI.BASE_STATIC_VDB =txtStaticIPVDB.getText().toString().trim();
//		//IPExternal =txtIPExternal.getText().toString().trim();
//
//		GetAPI.BASE_URL =txtIPAddress.getText().toString().trim();
//		GetAPI.BASE_PUBLIC_URL =txtIPStatic.getText().toString().trim();
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

		if(CheckIPEmpty()){
			Toast.makeText(HomeActivity.this, "Please enter ip address", Toast.LENGTH_SHORT).show();
		}

		else{
			if(checkVDBEmpty()){
				AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this)
				.setMessage("VDB is not set.");
				builder1.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {

								showIPDialog(HomeActivity.this);
								//dialog.dismiss();
							}
						})
						.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
				         {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								String ipAddr="";
								if(App_Variable.m3G_CONNECTED){
									if(IPStatic.equals("")){
										Toast.makeText(HomeActivity.this, "Please enter static ip address", Toast.LENGTH_SHORT).show();
										return;
									}else{
										App_Variable.ipType="Static";
										ipAddr = IPStatic;
										System.out.println("ipaddr:"+ipAddr);
										//GetAPI.BASE_PUBLIC_URL=IPStat
									}

								}else if(App_Variable.mWIFI_CONNECTED){
									if(IP.equals("")){
										Toast.makeText(HomeActivity.this, "Please enter local ip address", Toast.LENGTH_SHORT).show();
										return;
									}else{
										App_Variable.ipType="Local";
										ipAddr = IP;

									}
								}

			/*if(!IP.equals("")){
				if(!App_Variable.CheckValidateIP(IP)){
					Toast.makeText(HomeActivity.this, "Please enter valid local ip address", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if(!IPStatic.equals("")){
				if(!App_Variable.CheckValidateIP(IPStatic)){
					Toast.makeText(HomeActivity.this, "Please enter valid static ip address", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			 */

								StorageIPAddr(ipAddr);
								callProfile(true,true,App_Variable.ipType);
								dialog.dismiss();
							}
						})


				.create();
				builder1.show();
				//Toast.makeText(HomeActivity.this, "Please enter vdb address", Toast.LENGTH_SHORT).show();
			}else{
			String ipAddr="";
			if(App_Variable.m3G_CONNECTED){
				if(IPStatic.equals("")){
					Toast.makeText(HomeActivity.this, "Please enter static ip address", Toast.LENGTH_SHORT).show();
					return;
				}else{
					App_Variable.ipType="Static";
					ipAddr = IPStatic;
					//GetAPI.BASE_PUBLIC_URL=IPStat
				}

			}else if(App_Variable.mWIFI_CONNECTED){
				if(IP.equals("")){
					Toast.makeText(HomeActivity.this, "Please enter local ip address", Toast.LENGTH_SHORT).show();
					return;
				}else{
					App_Variable.ipType="Local";
					ipAddr = IP;

				}
			}

			/*if(!IP.equals("")){
				if(!App_Variable.CheckValidateIP(IP)){
					Toast.makeText(HomeActivity.this, "Please enter valid local ip address", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			if(!IPStatic.equals("")){
				if(!App_Variable.CheckValidateIP(IPStatic)){
					Toast.makeText(HomeActivity.this, "Please enter valid static ip address", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			 */

			StorageIPAddr(ipAddr);
			callProfile(true,true,App_Variable.ipType);

		}}

	}
	private boolean CheckIPEmpty() {
		if(IP.equals("")&&IPStatic.equals("")){//&&IPExternal.equals("")
			return true;
		}

		return false;
	}
	private boolean checkVDBEmpty(){
		if(VDB.equals("")&&VDBStatic.equals("")){
			return true;
		}
		return false;
	}

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

		if(App_Variable.ipType.equalsIgnoreCase("local")){
			GetAPI.BASE_URL = IP;
		}else if(App_Variable.ipType.equalsIgnoreCase("static")){
			GetAPI.BASE_PUBLIC_URL = IPStatic;
		}

		CustomLog.camera("GetAPI.BASE_LOCAL_VDB"+GetAPI.BASE_LOCAL_VDB);

		// TODO Auto-generated method stub
		SharedPreferences.Editor store= STORAGE.edit();
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


	private String GetIP(){

		if(!IP.equals("")){
			return IP;
		}else if(!IPStatic.equals("")){
			return IPStatic;
		}
		//		else if(!IPExternal.equals("")){
		//			return IPExternal;
		//		}
		return "";
	}

	protected void CallRefresh() {
		// TODO Auto-generated method stub
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){
			HomeListAdapter.selectedPos=0;
			new PingRefresh().execute();
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}

	class PingRefresh extends AsyncTask<Void, Void, Void>{

		private String exception="";
		@Override
		protected void onPreExecute() {
			progressDialog=new ProgressDialog(HomeActivity.this);
			progressDialog.setMessage("Fetching Load Status..");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.REFRESH);
			parser.getResponse();
			exception=parser.getException();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(!exception.equals("")){
				progressDialog.cancel();
				callExceptionPopup(exception);
			}else{

				callProfile(true,false,"");
				//callBgThread();

			}
		}
	}

	@Override
	protected void onResume() {
		//App_Variable.StorePause(HomeActivity.this, 0);
		//resumeActivityLogic
		if(isActivityResult)
			radiant_value=false;
		else
		radiant_value=true;
		System.out.println("ranjith time resume:"+radiant_value);




		App_Variable.appMinimize=1;
		//resumeTimeLogic();

		resumeActivityLogic();

		//		if(!App_Variable._isServiceStarted)
		//			StartService(this);
		super.onResume();
	}

	private void StartService(final Context context) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				//showToast(msg.what);


				CustomLog.error("home","sensor VDBSTATUS1-> Service handleMessage:"+msg.what);

				CustomLog.error(tag, "VDBSTATUS1 sensor StartBGService:"+App_Variable.appMinimize);
				if(msg.what==1||msg.what==0){
					/* sensor */
					if(App_Variable.appMinimize==1){
						Intent intentHome=new Intent(context,HomeActivity.class);
						intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intentHome.putExtra("position", -1);
						intentHome.putExtra("room", "");
						intentHome.putExtra("screen", "splash");
						startActivity(intentHome);
						finish();

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
			}
		};

		/*final Intent intent = new Intent(context, MyService.class);
		final Messenger messenger = new Messenger(handler);
		intent.putExtra("messenger", messenger);
		startService(intent);*/

	}

	private void resumeActivityLogic() {


		CustomLog.show(tag, "resume:"+App_Variable.appMinimize);
		_refreshingDimmerStatus = true;
		_isUpdateProfile =true;

		isCall =true;
		CustomLog.resume("home resume");
		if(App_Variable.hashNickName==null){
			CustomLog.resume("home resume not null");
			HomeListAsyn("same");
		}
		displayProfile();

		callProfileTimer();

	}

	private void resumeTimeLogic(){
		System.out.println("ranjith time on resume");
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now = format.format(new Date());
			Date todaydate = null;
			Long prevTime;
			SharedPreferences pref_time=getSharedPreferences("TimeDiff", MODE_PRIVATE);
			int countTime=pref_time.getInt("count_time", 0);
			prevTime=pref_time.getLong("prev_time", 0);
			String page=pref_time.getString("page", "");
			System.out.println("ranjith time page:::"+page);
			if(page.equals("const")){
				System.out.println("ranjith time constructor");
				SharedPreferences.Editor editor_start =pref_time.edit();
				editor_start.putInt("count_time", 0);
				editor_start.putLong("prev_time", 0);
				editor_start.putString("page", "resume");
				editor_start.commit();

			}else{
				System.out.println("ranjith time count:::"+countTime);
				System.out.println("ranjith time current:::"+prevTime);

				if(countTime%2==0){
					todaydate = format.parse(now);
					System.out.println("ranjith time todaydate-------  "+todaydate+","+todaydate.getTime());
					countTime++;
				}else{
					todaydate=format.parse(now);
					System.out.println("ranjith time compareDate-------  "+todaydate+","+todaydate.getTime());
					countTime++;

				}
				SharedPreferences.Editor editor_start =pref_time.edit();
				editor_start.putInt("count_time", countTime);
				editor_start.putLong("prev_time", todaydate.getTime());
				editor_start.putString("page", "resume");
				editor_start.commit();

				if(prevTime!=0){
					long difference=(prevTime -todaydate.getTime());
					long differenceBack = difference;
					differenceBack = difference / 1000;
					int secs = (int) (differenceBack % 60);
					differenceBack /= 60;
					int mins = (int) (differenceBack % 60);
					System.out.println("ranjith time todaydate secs:::"+Math.abs(mins));
					if(Math.abs(mins)>=5){
						//setResult(99);
						Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();

					}else{

					}
				}
				else{

				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}



	}



	private void callProfileTimer() {
		CustomLog.profile("profileupdate 1");
		TimerTask task=new TimerTask() {

			@Override
			public void run() {

				//				runOnUiThread(new  Runnable() {
				//					public void run() {
				updateProfileHome();
				//					}
				//				});
			}
		};

		timer=new Timer();
		timer.schedule(task, 0, 2000);

	}

	/*private void callBgThread() {
		screen ="splash";
		bgProcess=new BackgroundThread(HomeActivity.this);
		bgProcess.callSensorStatus(false);

	}*/

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		//App_Variable.StorePause(HomeActivity.this,1);
		App_Variable.appMinimize=0;
		System.out.println("ranjith time pause:"+radiant_value);


		/*try {
			SavePauseTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		App_Variable.appMinimize=0;
		CustomLog.show(tag, "pause:"+App_Variable.appMinimize);

		_isUpdateManual = false;
		_refreshingDimmerStatus = false;
		_isUpdateProfile =false;
		isCall =false;

		if(timer!=null){
			//timer.purge();
			timer.cancel();
		}
		SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
		boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);
        if(checkvalue) {
			if (radiant_value) {
				Intent intent = new Intent(this, UnlockActivity.class);
				startActivity(intent);
			}
		}
		super.onPause();


	}



	private void SavePauseTime() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = format.format(new Date());
		SharedPreferences pref_time=getSharedPreferences("TimeDiff", MODE_PRIVATE);
		int countTime=pref_time.getInt("count_time", 0);
		Date todaydate = format.parse(now);
		if(countTime==0){
			countTime=1;
		}else{
			countTime=0;
		}

		SharedPreferences.Editor editor_start =pref_time.edit();
		editor_start.putInt("count_time", countTime);
		editor_start.putLong("prev_time", todaydate.getTime());
		editor_start.commit();

		System.out.println("ranjith time pause time::"+todaydate.getTime());
	}

	private void callDimmerAndLightStatus(boolean resetXPos) {
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){
			if(grs!=null){
				grs.cancel(true);
			}
			grs=new GetRoomStatus(resetXPos);
			grs.execute();
		}
		else{
			App_Variable.ShowNoNetwork(this);
		}
	}
	private boolean resetXPos;
	class GetRoomStatus extends AsyncTask<Void, Void, Void>{

		public GetRoomStatus(boolean _resetXPos) {
			resetXPos=_resetXPos;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(resetXPos){
				webview_lay.setVisibility(View.GONE);
				progressDialog.setMessage("Please wait while room features loading..");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.STATUS_DIMMERLIGHT_URL+"?"+System.currentTimeMillis());
			parser.getResponseDimmerCallback(2000, HomeActivity.this);
			//			response=parser.getTimeOutResponse(2000).trim();
			//			exception=parser.getException().trim();
			//			parser=null;
			return null;
		}

		/*@Override
		protected void onPostExecute(Void result) {
			//Log.d(tag, "RoomStatus:"+response);

			//CustomLog.check("exception::"+exception);
			super.onPostExecute(result);
		}*/
	}

	@Override
	public void DimmerStatusResponse(final String response) {
		/**
		 * response like SD_D401 1 0, SD_D401->id, 1->channel and last value 0-> on or off
		 */

		//App_Variable.ShowErrorToast("Nothing", HomeActivity.this);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				progressDialog.dismiss();
				//view_electricals.setVisibility(View.GONE);
				UpdateDimmerLightStatus(response);
			}
		});


	}

	private void UpdateDimmerLightStatus(String response) {


		CustomLog.e("url", "url->UpdateDimmerLightStatus:" + _isUpdateManual + "," + _whichSection + "," + App_Variable.CURRENT_TAB);
		if(!_isUpdateManual){

			String section=_whichSection.toLowerCase();
			System.out.println("section:"+section);
			list_system=new ArrayList<String>();
			list_ac=new ArrayList<String>();
			list_media=new ArrayList<String>();
			list_electricals=new ArrayList<String>();
			Listtabname=App_Variable.hashtabname;
			ListEleurl=App_Variable.hashEleurl;
			//view_log.setVisibility(View.GONE);

			//profileView.setVisibility(View.GONE);

			if(!section.equals("home")&&!section.equals("settings")){
				_refreshingDimmerStatus = true;
				tab_layout.setVisibility(View.VISIBLE);

				ArrayList<String> listOfItems=App_Variable.hashRoomType.get(_whichSection);

				listData=App_Variable.hashRoomOptions.get(_whichSection);

				Collections.sort(listOfItems);
				GroupingTabData(listOfItems);

				DisplayTabView();


				if(App_Variable.CURRENT_TAB==App_Variable.SYSTEM){
                   if(list_system.size()!=0) {
					   img_tab_ac_bottom.setVisibility(View.GONE);
					   img_tab_media_bottom.setVisibility(View.GONE);
					   img_tab_system_bottom.setVisibility(View.VISIBLE);
					   img_tab_ele_bottom.setVisibility(View.GONE);

					   GetStatusOfDimAndLight(listOfItems, response);
					   GroupingSystemData();
					   DisplaySystemView(resetXPos);
				   }
					else{
					   if((currenttab!=2)&&(currenttab!=3)&&(currenttab!=4)){
						   //App_Variable.CURRENT_TAB=App_Variable.AC;
						   img_tab_ac_bottom.setVisibility(View.VISIBLE);
						   img_tab_media_bottom.setVisibility(View.GONE);
						   img_tab_system_bottom.setVisibility(View.GONE);
						   img_tab_ele_bottom.setVisibility(View.GONE);
						   view_ac.setVisibility(View.VISIBLE);
						   DisplayACView();
					   }else{
						   if(currenttab!=4&&currenttab!=3){
							  // App_Variable.CURRENT_TAB = App_Variable.MEDIA;
						   img_tab_ac_bottom.setVisibility(View.GONE);
						   img_tab_media_bottom.setVisibility(View.VISIBLE);
						   img_tab_system_bottom.setVisibility(View.GONE);
						   img_tab_ele_bottom.setVisibility(View.GONE);

						   view_media.setVisibility(View.VISIBLE);
						   view_system.setVisibility(View.GONE);
						   view_ac.setVisibility(View.GONE);

						   DisplayMediaView();
						   }
						   else
						   {
						   // App_Variable.CURRENT_TAB=App_Variable.ELECTRICALS;
							   img_tab_ele_bottom.setVisibility(View.VISIBLE);
							   img_tab_ac_bottom.setVisibility(View.GONE);
							   img_tab_media_bottom.setVisibility(View.GONE);
							   img_tab_system_bottom.setVisibility(View.GONE);

							   view_media.setVisibility(View.GONE);
							   view_system.setVisibility(View.GONE);
							   view_ac.setVisibility(View.GONE);
							   webview_lay.setVisibility(View.VISIBLE);

							   DisplayWebView();

						   }
					   }
				   }
					//DisplayMoodView();
				}
				else if(App_Variable.CURRENT_TAB==App_Variable.AC){
					img_tab_ac_bottom.setVisibility(View.VISIBLE);
					img_tab_media_bottom.setVisibility(View.GONE);
					img_tab_system_bottom.setVisibility(View.GONE);
					img_tab_ele_bottom.setVisibility(View.GONE);
					view_ac.setVisibility(View.VISIBLE);
					DisplayACView();
				}
				else if(App_Variable.CURRENT_TAB==App_Variable.MEDIA){
					img_tab_ac_bottom.setVisibility(View.GONE);
					img_tab_media_bottom.setVisibility(View.VISIBLE);
					img_tab_system_bottom.setVisibility(View.GONE);
					img_tab_ele_bottom.setVisibility(View.GONE);

					view_media.setVisibility(View.VISIBLE);
					view_system.setVisibility(View.GONE);
					view_ac.setVisibility(View.GONE);

					DisplayMediaView();
				}
               else if (App_Variable.CURRENT_TAB==App_Variable.ELECTRICALS){
					img_tab_ele_bottom.setVisibility(View.VISIBLE);
					img_tab_ac_bottom.setVisibility(View.GONE);
					img_tab_media_bottom.setVisibility(View.GONE);
					img_tab_system_bottom.setVisibility(View.GONE);

					view_media.setVisibility(View.GONE);
					view_system.setVisibility(View.GONE);
					view_ac.setVisibility(View.GONE);
					webview_lay.setVisibility(View.VISIBLE);

					DisplayWebView();

				}
			}else{

				if(section.equals("home")){
					webview_lay.setVisibility(View.GONE);
					homeView.setVisibility(View.VISIBLE);
					_refreshingDimmerStatus = false;
				}else if(section.equals("settings")){
					webview_lay.setVisibility(View.GONE);
					view_settings.setVisibility(View.VISIBLE);
					_refreshingDimmerStatus = false;
				}

				tab_layout.setVisibility(View.GONE);
			}

			_refreshingDimmerStatus=true;
		}

		try{
			if(isCall)
				CallRefreshDimmerStatus();
		}catch (Exception e) {
			CustomLog.e(tag, "dimmer update:"+e);
		}


	}



	@Override
	public void DimmerStatusException(final String exception) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				App_Variable.ShowErrorToast(exception, HomeActivity.this);
			}
		});

	}


	/**
	 * display moods for selected rooms
	 */
	private void DisplayMoodView() {
		/*LinearLayout layout_mood_room=(LinearLayout)findViewById(R.id.layout_mood_room);
		layout_mood_room.removeAllViewsInLayout();
		layout_mood_room.removeAllViews();*/

		ConfigEntity entity= App_Variable.hashMoodRooms.get(_whichSection);
		if(entity!=null){
			String roomID=entity.getRoomID();
			ArrayList<String> listMoods=entity.getListMoods();
			int size=listMoods.size();
			for (int i = 0; i <size; i++) {
				String moodID=listMoods.get(i);

			}

			/*for (int i = 0; i < size; i++) {
				View vi=getLayoutInflater().inflate(R.layout.mood_bottom_view1, null);
				ImageView imgMoodOn=(ImageView)vi.findViewById(R.id.imgMood);
				TextView txtMood = (TextView)vi.findViewById(R.id.txtMood);
				layout_mood_room.addView(vi);
			}
			 */
		}

	}

	Handler mHandler = new Handler();

	private void DisplayMediaView() {
		int sizeMedia= list_media.size();

		view_stb.setVisibility(View.GONE);
		view_tv.setVisibility(View.GONE);

		Hashtable<String, HomeEntity> hashEntity =listData.get(0);

		for (int j = 0; j < sizeMedia; j++) {
			HomeEntity entity =hashEntity.get(list_media.get(j));
			CustomLog.d(tag,"media:"+entity.getId());
			String type=list_media.get(j).toLowerCase();
			if(type.startsWith("stb")){
				view_stb.setVisibility(View.VISIBLE);

				EventForSTB(entity);

			}else if(type.startsWith("tv")){
				view_tv.setVisibility(View.VISIBLE);

				EventForTV(entity);
			}
		}
	}
	private void DisplayWebView(){
		webview_lay.setVisibility(View.VISIBLE);
		view_electricals.setVisibility(View.VISIBLE);
		//System.out.println("EleURL"+EleURL);
		//view_electricals.clearCache(true);
		//view_electricals.loadUrl("");
		view_electricals.clearView();
		progressBar.setVisibility(View.VISIBLE);

		String url=ListEleurl.get(_whichSection);

		//view_electricals.clearView();

		/*view_electricals.getSettings().getJavaScriptEnabled();
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		view_electricals.setWebViewClient(new AppWebViewClients());
		progressBar.setVisibility(View.VISIBLE);
		view_electricals.loadUrl(url);*/
		WebViewClient client = new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				//progressBar.setVisibility(View.GONE);
				view_electricals.loadUrl("javascript:(function() { " +
						"document.getElementsByTagName('body')[0].style.color = 'red'; " +
						"})()");
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view_electricals.clearFormData();
				view_electricals.clearCache(true);
				view_electricals.loadUrl(url);
				return false;
			}
		};

		view_electricals.setWebViewClient(client);
		view_electricals.getSettings().setJavaScriptEnabled(true);
		view_electricals.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
		view_electricals.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		view_electricals.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		view_electricals.loadUrl(url);
		view_electricals.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress >= 100) {
					progressBar.setVisibility(View.GONE);
				}
			}

		});





		isCall=false;
	}


	/*public class AppWebViewClients extends WebViewClient {
		private ProgressBar progressBar;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			progressBar.setVisibility(View.GONE);
		}
	}*/

	public void CallRefreshDimmerStatus() throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//_refreshingDimmerStatus=false;
				CustomLog.debug("refreshing ranjith:"+_refreshingDimmerStatus);
				//_refreshingDimmerStatus=false;
				while (_refreshingDimmerStatus) {
					try {
						_refreshingDimmerStatus=false;

						Thread.sleep(2000);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// Write your code here to update the UI.
								_isUpdateManual = false;
								callDimmerAndLightStatus(false);
							}
						});
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start();
	}

	private ImageView img_tv_onoff,img_tv_up_volume,img_tv_up_channel,img_tv_sound;
	private ImageView img_tv_down_volume,img_tv_down_channel;
	private TextView btn_source_tv,tv_enter;
	private ImageView btn_tv_left,btn_tv_right,btn_tv_up,btn_tv_down;
	private TextView txt_tv_volume,txt_tv_channel;
	private LinearLayout tv_curve;
	private void EventForTV(final HomeEntity entity) {
		img_tv_onoff =(ImageView) findViewById(R.id.img_tv_onoff);
		if(entity.getPwr().equals(""))
			img_tv_onoff.setVisibility(View.INVISIBLE);
		else
			img_tv_onoff.setVisibility(View.VISIBLE);

		img_tv_onoff.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getPwr());
			}
		});

		img_tv_up_volume =(ImageView) findViewById(R.id.img_tv_up_volume);
		if(entity.getVol_up().equals(""))
			img_tv_up_volume.setVisibility(View.INVISIBLE);
		else
			img_tv_up_volume.setVisibility(View.VISIBLE);

		img_tv_up_volume.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getVol_up());
			}
		});


		img_tv_up_channel =(ImageView) findViewById(R.id.img_tv_up_channel);
		if(entity.getCh_up().equals(""))
			img_tv_up_channel.setVisibility(View.INVISIBLE);
		else
			img_tv_up_channel.setVisibility(View.VISIBLE);
		img_tv_up_channel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getCh_up());
			}
		});

		img_tv_sound =(ImageView) findViewById(R.id.img_tv_sound);
		if(entity.getMute().equals(""))
			img_tv_sound.setVisibility(View.INVISIBLE);
		else
			img_tv_sound.setVisibility(View.VISIBLE);
		img_tv_sound.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getMute());
			}
		});


		img_tv_down_volume =(ImageView) findViewById(R.id.img_tv_down_volume);
		if(entity.getVol_dwn().equals(""))
			img_tv_down_volume.setVisibility(View.INVISIBLE);
		else
			img_tv_down_volume.setVisibility(View.VISIBLE);
		img_tv_down_volume.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getVol_dwn());
			}
		});



		img_tv_down_channel =(ImageView) findViewById(R.id.img_tv_down_channel);
		if(entity.getCh_dwn().equals(""))
			img_tv_down_channel.setVisibility(View.INVISIBLE);
		else
			img_tv_down_channel.setVisibility(View.VISIBLE);
		img_tv_down_channel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getCh_dwn());
			}
		});

		txt_tv_volume=(TextView) findViewById(R.id.txt_tv_volume);
		txt_tv_volume.setVisibility(View.VISIBLE);
		txt_tv_channel=(TextView) findViewById(R.id.txt_tv_channel);
		txt_tv_channel.setVisibility(View.VISIBLE);

		if(img_tv_down_volume.getVisibility()==View.INVISIBLE&&img_tv_up_volume.getVisibility()==View.INVISIBLE){
			txt_tv_volume.setVisibility(View.INVISIBLE);
		}
		if(img_tv_down_channel.getVisibility()==View.INVISIBLE&&img_tv_up_channel.getVisibility()==View.INVISIBLE){
			txt_tv_channel.setVisibility(View.INVISIBLE);
		}


		btn_source_tv =(TextView) findViewById(R.id.btn_source_tv);
		if(entity.getSource().equals(""))
			btn_source_tv.setVisibility(View.INVISIBLE);
		else
			btn_source_tv.setVisibility(View.VISIBLE);
		btn_source_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getSource());
			}
		});

		tv_enter =(TextView) findViewById(R.id.tv_enter);
		if(entity.getEnter().equals(""))
			tv_enter.setVisibility(View.INVISIBLE);
		else
			tv_enter.setVisibility(View.VISIBLE);
		tv_enter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getEnter());
			}
		});

		btn_tv_left =(ImageView) findViewById(R.id.btn_tv_left);
		if(entity.getLeft().equals(""))
			btn_tv_left.setVisibility(View.INVISIBLE);
		else
			btn_tv_left.setVisibility(View.VISIBLE);
		btn_tv_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getLeft());
			}
		});

		btn_tv_right =(ImageView) findViewById(R.id.btn_tv_right);
		if(entity.getRight().equals(""))
			btn_tv_right.setVisibility(View.INVISIBLE);
		else
			btn_tv_right.setVisibility(View.VISIBLE);
		btn_tv_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getRight());
			}
		});

		btn_tv_up =(ImageView) findViewById(R.id.btn_tv_up);
		if(entity.getUp().equals(""))
			btn_tv_up.setVisibility(View.INVISIBLE);
		else
			btn_tv_up.setVisibility(View.VISIBLE);
		btn_tv_up.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getUp());
			}
		});

		btn_tv_down =(ImageView) findViewById(R.id.btn_tv_down);
		if(entity.getDown().equals(""))
			btn_tv_down.setVisibility(View.INVISIBLE);
		else
			btn_tv_down.setVisibility(View.VISIBLE);
		btn_tv_down.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getDown());
			}
		});

		tv_curve =(LinearLayout)findViewById(R.id.tv_curve);
		tv_curve.setVisibility(View.VISIBLE);
		if(btn_tv_down.getVisibility()==View.INVISIBLE&&btn_tv_up.getVisibility()==View.INVISIBLE
				&&btn_tv_left.getVisibility()==View.INVISIBLE&&btn_tv_right.getVisibility()==View.INVISIBLE){
			tv_curve.setVisibility(View.INVISIBLE);
		}

	}


	private ImageView img_stb_onoff,img_stb_up_volume,img_stb_up_channel,img_stb_sound;
	private ImageView img_stb_down_volume,img_stb_down_channel;
	private TextView btn_play,btn_pause,btn_source,btn_guide,stb_enter;
	private ImageView btn_stb_left,btn_stb_right,btn_stb_up,btn_stb_down;
	private TextView txt_stb_volume,txt_stb_channel;
	private LinearLayout stb_curve;
	private void EventForSTB(final HomeEntity entity) {
		img_stb_onoff =(ImageView) findViewById(R.id.img_stb_onoff);
		if(entity.getPwr().equals(""))
			img_stb_onoff.setVisibility(View.INVISIBLE);
		else
			img_stb_onoff.setVisibility(View.VISIBLE);

		img_stb_onoff.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getPwr());
			}
		});

		img_stb_up_volume =(ImageView) findViewById(R.id.img_stb_up_volume);
		if(entity.getVol_up().equals(""))
			img_stb_up_volume.setVisibility(View.INVISIBLE);
		else
			img_stb_up_volume.setVisibility(View.VISIBLE);

		img_stb_up_volume.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getVol_up());
			}
		});


		img_stb_up_channel =(ImageView) findViewById(R.id.img_stb_up_channel);
		if(entity.getCh_up().equals(""))
			img_stb_up_channel.setVisibility(View.INVISIBLE);
		else
			img_stb_up_channel.setVisibility(View.VISIBLE);

		img_stb_up_channel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getCh_up());
			}
		});

		img_stb_sound =(ImageView) findViewById(R.id.img_stb_sound);
		if(entity.getMute().equals(""))
			img_stb_sound.setVisibility(View.INVISIBLE);
		else
			img_stb_sound.setVisibility(View.VISIBLE);

		img_stb_sound.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getMute());
			}
		});


		img_stb_down_volume =(ImageView) findViewById(R.id.img_stb_down_volume);
		if(entity.getVol_dwn().equals(""))
			img_stb_down_volume.setVisibility(View.INVISIBLE);
		else
			img_stb_down_volume.setVisibility(View.VISIBLE);

		img_stb_down_volume.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getVol_dwn());
			}
		});



		img_stb_down_channel =(ImageView) findViewById(R.id.img_stb_down_channel);
		if(entity.getCh_dwn().equals(""))
			img_stb_down_channel.setVisibility(View.INVISIBLE);
		else
			img_stb_down_channel.setVisibility(View.VISIBLE);

		img_stb_down_channel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getCh_dwn());
			}
		});

		txt_stb_volume=(TextView) findViewById(R.id.txt_stb_volume);
		txt_stb_volume.setVisibility(View.VISIBLE);
		txt_stb_channel=(TextView) findViewById(R.id.txt_stb_channel);
		txt_stb_channel.setVisibility(View.VISIBLE);

		if(img_stb_down_volume.getVisibility()==View.INVISIBLE&&img_stb_up_volume.getVisibility()==View.INVISIBLE){
			txt_stb_volume.setVisibility(View.INVISIBLE);
		}
		if(img_stb_down_channel.getVisibility()==View.INVISIBLE&&img_stb_up_channel.getVisibility()==View.INVISIBLE){
			txt_stb_channel.setVisibility(View.INVISIBLE);
		}

		btn_play =(TextView) findViewById(R.id.btn_play);
		if(entity.getPlay().equals(""))
			btn_play.setVisibility(View.INVISIBLE);
		else
			btn_play.setVisibility(View.VISIBLE);

		btn_play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getPlay());
			}
		});

		btn_source =(TextView) findViewById(R.id.btn_source);
		if(entity.getSource().equals(""))
			btn_source.setVisibility(View.INVISIBLE);
		else
			btn_source.setVisibility(View.VISIBLE);

		btn_source.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getSource());
			}
		});

		btn_pause =(TextView) findViewById(R.id.btn_pause);
		if(entity.getPause().equals(""))
			btn_pause.setVisibility(View.INVISIBLE);
		else
			btn_pause.setVisibility(View.VISIBLE);
		btn_pause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getPause());
			}
		});



		btn_guide =(TextView) findViewById(R.id.btn_guide);
		if(entity.getGuide().equals(""))
			btn_guide.setVisibility(View.INVISIBLE);
		else
			btn_guide.setVisibility(View.VISIBLE);
		btn_guide.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getGuide());
			}
		});

		stb_enter =(TextView) findViewById(R.id.stb_enter);
		if(entity.getEnter().equals(""))
			stb_enter.setVisibility(View.INVISIBLE);
		else
			stb_enter.setVisibility(View.VISIBLE);
		stb_enter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getEnter());
			}
		});

		btn_stb_left =(ImageView) findViewById(R.id.btn_stb_left);
		if(entity.getLeft().equals(""))
			btn_stb_left.setVisibility(View.INVISIBLE);
		else
			btn_stb_left.setVisibility(View.VISIBLE);
		btn_stb_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getLeft());
			}
		});

		btn_stb_right =(ImageView) findViewById(R.id.btn_stb_right);
		if(entity.getRight().equals(""))
			btn_stb_right.setVisibility(View.INVISIBLE);
		else
			btn_stb_right.setVisibility(View.VISIBLE);
		btn_stb_right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getRight());
			}
		});

		btn_stb_up =(ImageView) findViewById(R.id.btn_stb_up);
		if(entity.getUp().equals(""))
			btn_stb_up.setVisibility(View.INVISIBLE);
		else
			btn_stb_up.setVisibility(View.VISIBLE);
		btn_stb_up.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getUp());
			}
		});

		btn_stb_down =(ImageView) findViewById(R.id.btn_stb_down);
		if(entity.getDown().equals(""))
			btn_stb_down.setVisibility(View.INVISIBLE);
		else
			btn_stb_down.setVisibility(View.VISIBLE);
		btn_stb_down.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonPingURL(GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getDown());
			}
		});

		stb_curve =(LinearLayout)findViewById(R.id.stb_curve);
		stb_curve.setVisibility(View.VISIBLE);
		if(btn_stb_down.getVisibility()==View.INVISIBLE&&btn_stb_up.getVisibility()==View.INVISIBLE
				&&btn_stb_left.getVisibility()==View.INVISIBLE&&btn_stb_right.getVisibility()==View.INVISIBLE){
			stb_curve.setVisibility(View.INVISIBLE);
		}

	}
	protected void CommonPingURL(String URL) {
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){
			CommonPingURL puc=new CommonPingURL("others",HomeActivity.this);
			puc.execute(URL);
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}


	/**
	 * grouping items are ac, system and media
	 */
	public void GroupingTabData(ArrayList<String> listOfItems) {
		int size=listOfItems.size();
       // System.out.println("list type at grouping"+listtype);
		if(size!=0) {
			txtNoSensorHome.setVisibility(View.GONE);
			for (int i = 0; i < size; i++) {
				String item = listOfItems.get(i).toLowerCase().trim();
				if (item.contains("ac"))
					list_ac.add(listOfItems.get(i));
				else if (item.contains("tv") || item.contains("stb"))
					list_media.add(listOfItems.get(i));
				else if ((item.contains("light"))||(item.contains("dimmer"))||(item.contains("curtain"))) {
					System.out.println("list_electricas");
					list_system.add(listOfItems.get(i));
					Collections.sort(list_system);
				} else
					list_electricals.add(listOfItems.get(i));

			}
		}else
		{
			//view_ac.setVisibility(View.GONE);
			txtNoSensorHome.setVisibility(View.VISIBLE);

		}
	}

	/**
	 * depends on item(system,media and ac) tab will display & grouping item(dimmer,light,ac and stb) will sorting
	 */
	public void DisplayTabView() {

		tab_system.setVisibility(View.VISIBLE);
		tab_ac.setVisibility(View.VISIBLE);
		tab_media.setVisibility(View.VISIBLE);
        tab_electronics.setVisibility(View.VISIBLE);

		header_system.setVisibility(View.VISIBLE);
		header_ac.setVisibility(View.VISIBLE);
		header_media.setVisibility(View.VISIBLE);
        header_electronics.setVisibility(View.VISIBLE);
		if(list_system.size()==0){
			tab_system.setVisibility(View.GONE);
			header_system.setVisibility(View.GONE);
			currenttab=1;
		}else
			Collections.sort(list_system);

		if(list_ac.size()==0){
			tab_ac.setVisibility(View.GONE);
			header_ac.setVisibility(View.GONE);
			currenttab=2;
		}else
			Collections.sort(list_ac);

		if(list_media.size()==0){
			tab_media.setVisibility(View.GONE);
			header_media.setVisibility(View.GONE);
			currenttab=3;
		}else
			Collections.sort(list_media);
		if (list_electricals.size()==0){
			tab_electronics.setVisibility(View.GONE);
			header_electronics.setVisibility(View.GONE);
			currenttab=4;
		}else
		{
			String tabname=Listtabname.get(_whichSection);
			tab_electronics.setText(tabname);
			Collections.sort(list_electricals);
		}

	}

	/**
	 * getting status for each dimmer and light
	 */
	public void GetStatusOfDimAndLight(ArrayList<String> listOfItems,String response) {

		int size=listOfItems.size();
		String[] split=response.split("\n");
		System.out.println("RoomStatus:"+split[0]+","+split.length);
		int lengthOfDimmer=split.length;

		hashDimmerStatus=new HashMap<String, String>();


		int sizenow=listOfItems.size()-list_electricals.size();
		for (int i = 0; i < sizenow; i++) {
			String type=listOfItems.get(i).trim();
			Hashtable<String, HomeEntity> table=listData.get(i);
			HomeEntity entity=table.get(type);
			String item=type.toLowerCase();
			if(item.startsWith("dimmer")||item.startsWith("light")){
				for (int j = 0; j < lengthOfDimmer; j++) {
					System.out.println("dimmer split[j]::"+split[j]);
					if(split[j].contains(entity.getId()+" "+entity.getChannel())){
							System.out.println("dimmer type="+type+" , id="+entity.getId()+" , data="+split[j]);
							hashDimmerStatus.put(type, split[j]);
							break;
					}
				}
			}
		}

	}

	/**
	 * In system type has 3 section. These are dimmer, curtain and light.
	 * So grouping those section.
	 */



	public void GroupingSystemData() {
		System.out.println("hash type size:"+k+","+App_Variable.hashtype);

		listDimmers=new ArrayList<String>();
		listCurtains=new ArrayList<String>();
		listLights=new ArrayList<String>();
      	for (int i = 0; i < list_system.size(); i++)
        {
         Collections.sort(list_system);
//CustomLog.debug("system type:" + list_system.get(i) + " , value=" + hashDimmerStatus.get(list_system.get(i)));
			String item=list_system.get(i).trim().toLowerCase();
			//System.out.println("item:" + item);
			String typekey=_whichSection+"&&"+item;
			listhashtype=App_Variable.hashtype;

			String typekey1=listhashtype.get(typekey).toLowerCase();
			//System.out.println("type key:"+typekey1);

				if (typekey1.contains("dimming"))
					listDimmers.add(list_system.get(i));
			       Collections.sort(listDimmers);
			       System.out.println("list dimmers:" + listDimmers);
			 if(typekey1.contains("curtain")){
				listCurtains.add(list_system.get(i));
			Collections.sort(listCurtains);
				 System.out.println("list dimmers:" + listCurtains);
			 }
			else if(typekey1.contains("switching")) {
				 listLights.add(list_system.get(i));
				 Collections.sort(listLights);
				 System.out.println("list dimmers:" + listLights);
			 }
		}






	}


	/**
	 * display system(dimmer, curtain and light) view
	 */
	public void DisplaySystemView(boolean resetXPos){
		if(list_system.size()>0){
			txtNoSensorHome.setVisibility(View.GONE);
			webview_lay.setVisibility(View.GONE);
			view_system.setVisibility(View.VISIBLE);

			DisplayDimmerView(resetXPos);

			DisplayCurtainView(resetXPos);

			DisplayLightView(resetXPos);
		}else{
			txtNoSensorHome.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * display dimmer view
	 */
	public void DisplayDimmerView(boolean resetXPOS) {

		LinearLayout view_dimmer=(LinearLayout)findViewById(R.id.view_dimmer);
		if(listDimmers.size()==0)
			view_dimmer.setVisibility(View.GONE);
		else
			view_dimmer.setVisibility(View.VISIBLE);

		HorizontalScrollView scrollViewDimmer=(HorizontalScrollView)findViewById(R.id.scrollViewDimmer);
		if(resetXPOS)
			scrollViewDimmer.scrollTo(0, 0);//text_select_color

		LinearLayout linearDimmer=(LinearLayout)findViewById(R.id.linearDimmer);
		linearDimmer.removeAllViewsInLayout();
		linearDimmer.removeAllViews();

		int sizeOfDimmer=listDimmers.size();
		for (int i = 0; i < sizeOfDimmer; i++) {
			View view=getLayoutInflater().inflate(R.layout.dimmer_view, null);

			TextView txtDimmer=(TextView)view.findViewById(R.id.txtDimmer);
			//CustomLog.resume("_whichSection:"+_whichSection);
			//CustomLog.latest("nickname:"+hashNickName.get(_whichSection+"&&"+listDimmers.get(i)));
			//CustomLog.latest("original name:"+listDimmers.get(i));
			if(!App_Variable.hashNickName.get(_whichSection+"&&"+listDimmers.get(i)).equals(""))
				txtDimmer.setText(App_Variable.hashNickName.get(_whichSection+"&&"+listDimmers.get(i)));
			else
				txtDimmer.setText(App_Variable.convertString(listDimmers.get(i)));

			/**
			 * set dimmer status on or off
			 */
			String dimmerStatus="";
			final ImageView imgDimmerStatus=(ImageView)view.findViewById(R.id.imgDimmerStatus);

			String dimmerData=(String) hashDimmerStatus.get(listDimmers.get(i));
			//System.out.println("Dimmer data:"+dimmerData+":st"+listDimmers.get(i));
			if(dimmerData!=null){
				dimmerStatus=dimmerData.substring(dimmerData.length()-1);
				if(dimmerStatus.equals("0"))
					imgDimmerStatus.setImageResource(R.drawable.on_icon);
				else
					imgDimmerStatus.setImageResource(R.drawable.off_icon);
			}

			/**
			 * event for dimmer on or off
			 */
			//CustomLog.debug("ranjith on::"+(i+"&&"+dimmerStatus));
			//System.out.println("Dimmer Status:"+dimmerStatus+":st");
			if(dimmerStatus.trim().equals(""))
				dimmerStatus="0";
			imgDimmerStatus.setTag(i+"&&"+dimmerStatus);
			imgDimmerStatus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					String[] tag=(""+v.getTag()).split("&&");
					//					String[] tag;
					//					if(data!=null){
					//						tag=data.split("&&");
					//					}
					//					if(tag!=null){
					CustomLog.debug("click ranjith dimmer1::"+tag+","+tag.length);
					//CustomLog.debug("ranjith dimmer2::"+tag[0]+","+tag[1]);
					int pos=Integer.parseInt(tag[0]);
					Hashtable<String, HomeEntity> tableOfDimmer=listData.get(pos);
					HomeEntity entity=tableOfDimmer.get(listDimmers.get(pos));


					String activateURL="";
					if(tag.length==2){
						if(tag[1].equals("0")){
							CustomLog.debug("click display off");
							//activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getOn();
							imgDimmerStatus.setImageResource(R.drawable.off_icon);
							imgDimmerStatus.setTag(tag[0]+"&&"+"1");
						}else{
							CustomLog.debug("click display on");
							//activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getOff();
							imgDimmerStatus.setImageResource(R.drawable.on_icon);
							imgDimmerStatus.setTag(tag[0]+"&&"+"0");
						}
					}
					activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getTg();
					CustomLog.debug("click on::"+tag[0]+","+tag[1]);


					//_isUpdateManual=true;
					PingURL(activateURL);
					//}
				}
			});



			/**
			 * event for dimmer decrement(-)
			 */
			TextView dimmerMinus=(TextView)view.findViewById(R.id.dimmerMinus);
			dimmerMinus.setTag(i);
			dimmerMinus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int pos=Integer.parseInt(""+v.getTag());
					Hashtable<String, HomeEntity> tableOfDimmer=listData.get(pos);
					HomeEntity entity=tableOfDimmer.get(listDimmers.get(pos));

					CustomLog.debug("click down::"+entity.getDown()+","+entity.getId());
					String activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getDown();
					PingURL(activateURL);
				}
			});

			/**
			 * event for dimmer increment(+)
			 */
			TextView dimmerPlus=(TextView)view.findViewById(R.id.dimmerPlus);
			dimmerPlus.setTag(i);
			dimmerPlus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					int pos=Integer.parseInt(""+v.getTag());
					Hashtable<String, HomeEntity> tableOfDimmer=listData.get(pos);
					HomeEntity entity=tableOfDimmer.get(listDimmers.get(pos));

					CustomLog.debug("click up::"+entity.getUp()+","+entity.getId());
					String activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getUp();
					PingURL(activateURL);

				}
			});
			linearDimmer.addView(view);
		}
	}

	/**
	 * display curtain view
	 */
	public void DisplayCurtainView(boolean resetXPOS){

		LinearLayout view_curtain=(LinearLayout)findViewById(R.id.view_curtain);
		if(listCurtains.size()==0)
			view_curtain.setVisibility(View.GONE);
		else
			view_curtain.setVisibility(View.VISIBLE);

		HorizontalScrollView scrollViewCurtain=(HorizontalScrollView)findViewById(R.id.scrollViewCurtain);
		if(resetXPOS)
			scrollViewCurtain.scrollTo(0, 0);

		LinearLayout linearCurtain=(LinearLayout)findViewById(R.id.linearCurtain);
		linearCurtain.removeAllViewsInLayout();
		linearCurtain.removeAllViews();

		int sizeOfLight=listCurtains.size();
		for (int i = 0; i < sizeOfLight; i++) {
			View view=getLayoutInflater().inflate(R.layout.curtain_view, null);

			TextView txtLight=(TextView)view.findViewById(R.id.txtCurtain);
			//
			if(!App_Variable.hashNickName.get(_whichSection+"&&"+listCurtains.get(i)).equals(""))
				txtLight.setText(App_Variable.hashNickName.get(_whichSection+"&&"+listCurtains.get(i)));
			else
				txtLight.setText(App_Variable.convertString(listCurtains.get(i)));

			/**
			 * event for curtain open
			 */
			ImageView imgCurtainOpen=(ImageView)view.findViewById(R.id.imgCurtainOpen);
			imgCurtainOpen.setTag(i);
			imgCurtainOpen.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int pos=Integer.parseInt(""+v.getTag());
					Hashtable<String, HomeEntity> tableOfDimmer=listData.get(pos);
					HomeEntity entity=tableOfDimmer.get(listCurtains.get(pos));

					CustomLog.debug("open::"+entity.getOpen()+","+entity.getId());
					String activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getOpen();;

					PingURL(activateURL);
				}
			});

			/**
			 * event for curtain close
			 */
			ImageView imgCurtainClose=(ImageView)view.findViewById(R.id.imgCurtainClose);
			imgCurtainClose.setTag(i);
			imgCurtainClose.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int pos=Integer.parseInt(""+v.getTag());
					Hashtable<String, HomeEntity> tableOfDimmer=listData.get(pos);
					HomeEntity entity=tableOfDimmer.get(listCurtains.get(pos));

					System.out.println("close::"+entity.getClose()+","+entity.getId());
					String activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getClose();;

					PingURL(activateURL);
				}
			});

			/**
			 * event for curtain stop
			 */
			ImageView imgCurtainStop=(ImageView)view.findViewById(R.id.imgCurtainStop);
			imgCurtainStop.setTag(i);
			imgCurtainStop.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int pos=Integer.parseInt(""+v.getTag());
					Hashtable<String, HomeEntity> tableOfDimmer=listData.get(pos);
					HomeEntity entity=tableOfDimmer.get(listCurtains.get(pos));

					CustomLog.debug("stop::"+entity.getStop()+","+entity.getId());
					String activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getStop();

					PingURL(activateURL);
				}
			});


			linearCurtain.addView(view);
		}

	}

	/**
	 * display light view
	 */
	public void DisplayLightView(boolean resetXPOS){

		LinearLayout view_light=(LinearLayout)findViewById(R.id.view_light);
		if(listLights.size()==0)
			view_light.setVisibility(View.GONE);
		else
			view_light.setVisibility(View.VISIBLE);

		HorizontalScrollView scrollViewLight=(HorizontalScrollView)findViewById(R.id.scrollViewLight);
		if(resetXPOS)
			scrollViewLight.scrollTo(0, 0);

		LinearLayout linearLight=(LinearLayout)findViewById(R.id.linearLight);
		linearLight.removeAllViewsInLayout();
		linearLight.removeAllViews();

		int sizeOfLight=listLights.size();
		for (int i = 0; i < sizeOfLight; i++) {
			View view=getLayoutInflater().inflate(R.layout.light_view, null);

			TextView txtLight=(TextView)view.findViewById(R.id.txtLight);
			//txtLight.setText(App_Variable.convertString(listLights.get(i)));

			if(!App_Variable.hashNickName.get(_whichSection+"&&"+listLights.get(i)).equals(""))
				txtLight.setText(App_Variable.hashNickName.get(_whichSection+"&&"+listLights.get(i)));
			else
				txtLight.setText(App_Variable.convertString(listLights.get(i)));


			/**
			 * set light status on or off
			 */
			String lightStatus="";
			final ImageView imgLightStatus=(ImageView)view.findViewById(R.id.imgLightStatus);

			String lightData=(String) hashDimmerStatus.get(listLights.get(i));
			//System.out.println("dimmer light data:"+lightData+"st"+listLights.get(i));
			if(lightData!=null){
				lightStatus=lightData.substring(lightData.length()-1);
				if(lightStatus.equals("0"))
					imgLightStatus.setImageResource(R.drawable.on_icon);
				else
					imgLightStatus.setImageResource(R.drawable.off_icon);
			}


			/**
			 * event for dimmer on or off
			 */
			//System.out.println("dimmer light status:"+lightStatus);
			if(lightStatus.trim().equals("")){
				lightStatus = "0";
			}
			imgLightStatus.setTag(i+"&&"+lightStatus);
			imgLightStatus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String[] tag=(""+v.getTag()).split("&&");
					int pos=Integer.parseInt(tag[0]);
					Hashtable<String, HomeEntity> tableOfDimmer=listData.get(pos);
					HomeEntity entity=tableOfDimmer.get(listLights.get(pos));

					CustomLog.debug("click saya_webAPI.sh on::"+entity.getOn()+","+entity.getId());
					String activateURL="";
					if(tag[1].equals("0")){
						CustomLog.debug("click display off");
						imgLightStatus.setImageResource(R.drawable.off_icon);
						imgLightStatus.setTag(tag[0]+"&&"+"1");
						//activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getOn();
					}else{
						CustomLog.debug("click display on");
						imgLightStatus.setImageResource(R.drawable.on_icon);
						imgLightStatus.setTag(tag[0]+"&&"+"0");
						//activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getOff();
					}
					activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getTg();

					//_isUpdateManual=true;
					PingURL(activateURL);
				}
			});

			linearLight.addView(view);
		}

	}

	/**
	 * display ac view
	 */
	private void DisplayACView() {
		//view_ac.setVisibility(View.VISIBLE);
		String section=_whichSection.toLowerCase();

		if(!section.equals("home")&&!section.equals("settings")){
			LinkedHashMap<String, String> hashACData=App_Variable.hashAC.get(_whichSection);

			GroupingACItems(hashACData);

			DisplayACFanView();


		}

	}


	/**
	 * grouping fan level and ac level
	 * sorting fan and ac level key and values
	 */
	private void GroupingACItems(LinkedHashMap<String, String> hashACData) {
		listFanLvlKey=new ArrayList<String>();
		listFanLvlValue=new ArrayList<String>();

		listAcLvlKey=new ArrayList<String>();
		listAcLvlValue=new ArrayList<String>();


		if(hashACData!=null){
			Set entrySet = hashACData.entrySet();
			Iterator iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				//CustomLog.debug("key ac:"+entry.getKey()+","+entry.getValue());
				if(App_Variable.isNumeric(""+entry.getKey())){
					listAcLvlKey.add(""+entry.getKey());
					listAcLvlValue.add(""+entry.getValue());
				}
				if((""+entry.getKey()).startsWith("f")){
					listFanLvlKey.add(""+entry.getKey());
					listFanLvlValue.add(""+entry.getValue());
				}
			}

			/**
			 * sorting fan and ac level key and values
			 */
			Collections.sort(listFanLvlKey);
			Collections.sort(listFanLvlValue);

			/*for (int i = 0; i < listFanLvlKey.size(); i++) {
				CustomLog.debug("fan key:"+listFanLvlKey.get(i)+" ,value:"+listFanLvlValue.get(i));
			}*/

			Collections.sort(listAcLvlKey);
			Collections.sort(listAcLvlValue);

			/*for (int i = 0; i < listAcLvlKey.size(); i++) {
				CustomLog.debug("ac key:"+listAcLvlKey.get(i)+" ,value:"+listAcLvlValue.get(i));
			}*/
		}

	}

	/**
	 * display fan level
	 */
	private void DisplayACFanView() {
		// TODO Auto-generated method stub
		ExpandableHeightGridView gridview_fan=(ExpandableHeightGridView)findViewById(R.id.gridview_fan);
		gridview_fan.removeAllViewsInLayout();

		/**
		 * event for fan level
		 */
		FanLevelAdapter adapterFan=new FanLevelAdapter(this,listFanLvlKey);
		gridview_fan.setAdapter(adapterFan);
		gridview_fan.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Hashtable<String, HomeEntity> tableOfAc=listData.get(0);
				HomeEntity entity=tableOfAc.get(list_ac.get(0));

				CustomLog.debug("");
				String activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+listFanLvlValue.get(position);
				PingURL(activateURL);


			}
		});


		ExpandableHeightGridView gridview_ac=(ExpandableHeightGridView)findViewById(R.id.gridview_ac);
		gridview_ac.removeAllViewsInLayout();

		/**
		 * event for ac level
		 */
		ACLevelAdapter adapterAC=new ACLevelAdapter(this,listAcLvlKey);
		gridview_ac.setAdapter(adapterAC);
		gridview_ac.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Hashtable<String, HomeEntity> tableOfAc=listData.get(0);
				HomeEntity entity=tableOfAc.get(list_ac.get(0));

				CustomLog.debug("");
				String activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+listAcLvlValue.get(position);
				PingURL(activateURL);


			}
		});

		/**
		 * event for ac off
		 */
		ImageView imgACOff=(ImageView)findViewById(R.id.imgACOff);
		imgACOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Hashtable<String, HomeEntity> tableOfAc=listData.get(0);
				HomeEntity entity=tableOfAc.get(list_ac.get(0));

				String activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getOff();
				PingURL(activateURL);
			}
		});

		/**
		 * event for ac on
		 */
		ImageView imgACOn=(ImageView)findViewById(R.id.imgACOn);
		imgACOn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Hashtable<String, HomeEntity> tableOfAc=listData.get(0);
				HomeEntity entity=tableOfAc.get(list_ac.get(0));


				String activateURL=GetAPI.BASE_URL+GetAPI.ACTIVATE_DIMMER_CTRL_URL+entity.getScript()+"+"+entity.getId()+"+"+entity.getOn();
				PingURL(activateURL);
			}
		});

	}

	protected void PingURL(String activateURL) {
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){

			PingURLClass puc=new PingURLClass();
			puc.execute(activateURL);
		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}


	class PingURLClass extends AsyncTask<String, Void, Void>{
		private String exception="";

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			BaseParser parser=new BaseParser(params[0]);
			parser.getResponse();
			exception=parser.getException().trim();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			CustomLog.e("URL:","http://192.168.1.231/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/ completed:"+exception);

			if(!exception.equals(""))
				App_Variable.ShowErrorToast(exception, HomeActivity.this);
			//	callDimmerAndLightStatus();

			super.onPostExecute(result);
		}

	}

	boolean isLoading=false;
	private void callProfile(boolean _isRefresh,boolean _isLoading,String typeOfIP) {
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){
			isLoading=_isLoading;
			if(gp!=null)
				gp.cancel(true);

			if(_isLoading){
				progressDialog.setMessage("Please wait..");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}

			gp=new GetProfile(HomeActivity.this,_isRefresh,typeOfIP);
			gp.execute();
		}else{
			Toast.makeText(getApplicationContext(), App_Variable.NONETWORK, Toast.LENGTH_LONG).show();

		}

	}

	@Override
	public void callHomeList(boolean isRefresh) {
		if(isRefresh){
			HomeListAsyn("next");

		}else{
			displayProfile();
			//callMoodData();
		}

	}

	private void HomeListAsyn(String screen) {
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){
			//listOfCity=new ArrayList<CityEntity>();
			//progress.setVisibility(View.VISIBLE);
			HomeList home=new HomeList(HomeActivity.this,screen);
			home.execute();
		}else{

			//showTyrAgain("city");
			//progress.setVisibility(View.GONE);
			//Toast.makeText(getApplicationContext(), "No Network connection", Toast.LENGTH_SHORT).show();
			App_Variable.ShowNoNetwork(this);
		}

	}

	@Override
	public void StartBGService(Context context,String whichScreen) {

		CustomLog.resume("startBg");
		if(progressDialog!=null){
			progressDialog.cancel();
		}

		if(whichScreen.equals("same")){
			///nothing
			CustomLog.resume("home App_Variable.hashNickName:");
		}else{

			Intent intentHome=new Intent(context,HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentHome.putExtra("position", -1);
			intentHome.putExtra("room", "");
			intentHome.putExtra("nextPage", "");
			if(isLoading)
				intentHome.putExtra("screen", "others");
			else
				intentHome.putExtra("screen", "splash");

			startActivity(intentHome);
			finish();
		}


	}



	GetMooodData gmd;
	private void callMoodData() {
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){
			if(gmd!=null)
				gmd.cancel(true);
			gmd=new GetMooodData(HomeActivity.this,false);
			gmd.execute();

		}else{
			App_Variable.ShowNoNetwork(this);
		}
	}



	@Override
	public void getResponseMoodData(HashMap<String, MoodEntity> _hashMoodData,
			HashMap<String, String> _hashMoodFind, HashMap<String, String> _hashMoodAppliance,HashMap<String, String> _hashMoodApplRoom) {

		//hashMood=_hashMoodData;
		//hashMoodFind=_hashMoodFind;
	}

	private void displayProfile() {
		CustomLog.latest("displayProfile:::"+App_Variable.PROFILE);
		if(txt_homeProfile!=null){
			if(App_Variable.PROFILE!=null){
				txt_homeProfile.setText(App_Variable.PROFILE.toUpperCase());
				txt_homeProfile.invalidate();
			}
			//txt_homeProfile.invalidate();
		}

		if(img_profile!=null){
			CustomLog.latest("displayProfile1:::"+App_Variable.PROFILE);
			String _profile=App_Variable.PROFILE.toLowerCase().trim();
			if(_profile.equals("maid"))
				img_profile.setImageResource(R.drawable.maid_icon);
			else if(_profile.equals("away"))
				img_profile.setImageResource(R.drawable.icon_away);
			else if(_profile.equals("disarm")){
				CustomLog.d(tag, "PROFILE:draw");
				img_profile.setImageResource(R.drawable.disarm_icon);
			}else if(_profile.equals("terrace")){
				img_profile.setImageResource(R.drawable.terrace_icon);
			}
			//img_profile.invalidate();
		}
		if(layoutProfile!=null){
			//layoutProfile.invalidate();
		}

	}

	public void ShowViewSection(int position, String whichSection) {
		try{
			_isUpdateManual =false;
			CustomLog.d(tag, "whichsection:"+whichSection);


			if(whichSection.equalsIgnoreCase("door unlock")){
				radiant_value=false;
				callDoorUnlockPopup();
			}else{
				_whichSection=whichSection.trim();
				CustomLog.d(tag, "whichsection:1");

				App_Variable.CURRENT_TAB=App_Variable.SYSTEM;
				if(sm!=null)

					sm.toggle();

				CustomLog.d(tag, "whichsection:2");
				homeView.setVisibility(View.GONE);

				view_system.setVisibility(View.GONE);
				view_ac.setVisibility(View.GONE);
				view_media.setVisibility(View.GONE);
				view_settings.setVisibility(View.GONE);

				tab_layout.setVisibility(View.GONE);

				CustomLog.d(tag, "whichsection:3");

				if(_whichSection.equalsIgnoreCase("settings")){
					CustomLog.d(tag, "whichsection:"+whichSection);
					webview_lay.setVisibility(View.GONE);
					view_settings.setVisibility(View.VISIBLE);
					txtNoSensorHome.setVisibility(View.GONE);

					_refreshingDimmerStatus = false;
					isCall =false;
				} else if(_whichSection.equalsIgnoreCase("home")){
					webview_lay.setVisibility(View.GONE);
					homeView.setVisibility(View.VISIBLE);
					_refreshingDimmerStatus = false;
					if(isEmptyFeatue()){
						txtNoSensorHome.setVisibility(View.VISIBLE);
					}
					else {
						txtNoSensorHome.setVisibility(View.GONE);
					}

				}
				else{
					_whichPosition = position;

					ResetMoodIcon();
					callDimmerAndLightStatus(true);
				}
			}
		}catch (Exception e) {
			CustomLog.debug("ShowViewSection:::error"+e);
		}

		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}

	/**
	 * Popup contains data like Please select room
	 */
	private void callDoorUnlockPopup(){
		AlertDialog alertDialog=new AlertDialog.Builder(this)
		.setMessage("You have opted for door unlock.\nWould you like to continue?")
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
				boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);
				if(checkvalue==true){
				Intent in = new Intent(HomeActivity.this,UnlockActivity.class);
					in.putExtra("pagetype","doorunlock");
				startActivityForResult(in, 10);
				dialog.dismiss();
				sm.toggle();
				}else {
					callDoorUnlockParser();
				}
				dialog.dismiss();
				sm.toggle();

			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						sm.toggle();
					}
				})
		.create();
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}

	protected void callDoorUnlockParser() {
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){
			System.out.println("ranjith time calldoor"+radiant_value);
			//isActivityResult=true;
			CommonPingURL puc=new CommonPingURL("others",HomeActivity.this);
			puc.execute(GetAPI.BASE_URL+GetAPI.CAMERA_DOOR_UNLOCK);
		}else{
			App_Variable.ShowNoNetwork(this);
		}

	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.layoutProfile:
			///profileView.setVisibility(View.VISIBLE);
			//homeView.setVisibility(View.GONE);

			/* call profile activity */
			radiant_value=false;
			Intent intent =new Intent(HomeActivity.this,ProfileActivity.class);
			startActivity(intent);
			break;
		case R.id.layoutCamera:
			/* call camera activity */
			Intent intentCamera = new Intent(HomeActivity.this,CameraActivity.class);
			intentCamera.putExtra("page", 0);
			intentCamera.putExtra("screen", "camera");
			intentCamera.putExtra("vdb_trigger", 0);
			startActivity(intentCamera);
			break;
		case R.id.layoutVDB:
			/* call camera activity */
			Intent intentVDB = new Intent(HomeActivity.this,CameraActivity.class);
			intentVDB.putExtra("page", 0);
			intentVDB.putExtra("screen", "vdb");
			intentVDB.putExtra("vdb_trigger", 0);
			startActivity(intentVDB);
			break;
		case R.id.layoutHooter:
			/* call hooter activity */
			radiant_value=false;
			callPopup("hooter");



			break;
		case R.id.layoutPanic:
			/* call panic activity */
			radiant_value=false;
			callPopup("panic");



			break;
		case R.id.img_slider:
			sm.toggle();
			break;
		case R.id.tab_system:
			App_Variable.CURRENT_TAB=App_Variable.SYSTEM;

			img_tab_ac_bottom.setVisibility(View.GONE);
			img_tab_media_bottom.setVisibility(View.GONE);
			img_tab_system_bottom.setVisibility(View.VISIBLE);
            img_tab_ele_bottom.setVisibility(View.GONE);

			view_system.setVisibility(View.VISIBLE);
			view_ac.setVisibility(View.GONE);
			view_media.setVisibility(View.GONE);
			webview_lay.setVisibility(View.GONE);
			break;
		case R.id.tab_ac:
			App_Variable.CURRENT_TAB=App_Variable.AC;

			img_tab_ac_bottom.setVisibility(View.VISIBLE);
			img_tab_media_bottom.setVisibility(View.GONE);
			img_tab_system_bottom.setVisibility(View.GONE);
			img_tab_ele_bottom.setVisibility(View.GONE);

			view_system.setVisibility(View.GONE);
			view_media.setVisibility(View.GONE);
			view_ac.setVisibility(View.VISIBLE);
			webview_lay.setVisibility(View.GONE);

			DisplayACView();
			break;
		case R.id.tab_media:
			App_Variable.CURRENT_TAB=App_Variable.MEDIA;

			img_tab_ac_bottom.setVisibility(View.GONE);
			img_tab_media_bottom.setVisibility(View.VISIBLE);
			img_tab_system_bottom.setVisibility(View.GONE);
			img_tab_ele_bottom.setVisibility(View.GONE);

			view_media.setVisibility(View.VISIBLE);
			view_system.setVisibility(View.GONE);
			view_ac.setVisibility(View.GONE);
			webview_lay.setVisibility(View.GONE);

			DisplayMediaView();
			break;
			case R.id.tab_electricals:
				App_Variable.CURRENT_TAB=App_Variable.ELECTRICALS;

				img_tab_ac_bottom.setVisibility(View.GONE);
				img_tab_media_bottom.setVisibility(View.GONE);
				img_tab_system_bottom.setVisibility(View.GONE);
				img_tab_ele_bottom.setVisibility(View.VISIBLE);

				view_media.setVisibility(View.GONE);
				view_system.setVisibility(View.GONE);
				view_ac.setVisibility(View.GONE);
				webview_lay.setVisibility(View.VISIBLE);

				//String url=ListEleurl.get(_whichSection);
				view_electricals.loadUrl("");

				DisplayWebView();
				break;


			case R.id.moodMasterOn:

			CustomLog.d(tag, _whichSection+"&"+moodNames[0]);
			CustomLog.d(tag, "mood"+_whichPosition+""+moodIndex[0]);

			ResetMoodIcon();
			imgMoodOn.setImageResource(R.drawable.mood_master);
			CallRoomMood("mood"+_whichPosition+""+moodIndex[0]);


			break;
		case R.id.moodMasterOff:
			CustomLog.d(tag, _whichSection+"&"+moodNames[1]);
			CustomLog.d(tag, "mood"+_whichPosition+""+moodIndex[1]);

			ResetMoodIcon();
			imgMoodOff.setImageResource(R.drawable.mood_master);
			CallRoomMood("mood"+_whichPosition+""+moodIndex[1]);

			break;
		case R.id.moodMorning:
			CustomLog.d(tag, _whichSection+"&"+moodNames[2]);
			CustomLog.d(tag, "mood"+_whichPosition+""+moodIndex[2]);

			ResetMoodIcon();
			imgMoodMorning.setImageResource(R.drawable.mood_morning);
			CallRoomMood("mood"+_whichPosition+""+moodIndex[2]);

			break;
		case R.id.moodEvening:
			CustomLog.d(tag, _whichSection+"&"+moodNames[3]);
			CustomLog.d(tag, "mood"+_whichPosition+""+moodIndex[3]);

			ResetMoodIcon();
			imgMoodEvening.setImageResource(R.drawable.mood_evening);
			CallRoomMood("mood"+_whichPosition+""+moodIndex[3]);

			break;
		case R.id.moodDinner:
			CustomLog.d(tag, _whichSection+"&"+moodNames[4]);
			CustomLog.d(tag, "mood"+_whichPosition+""+moodIndex[4]);

			ResetMoodIcon();
			imgMoodDinner.setImageResource(R.drawable.mood_dinner);
			CallRoomMood("mood"+_whichPosition+""+moodIndex[4]);
			break;

		default:
			break;
		}

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("ranjith time acitivity:"+radiant_value);
		isActivityResult=false;
		String page=data.getExtras().getString("page1");
		if(resultCode==10) {
			if (page.equals("doorunlock")) {

				callDoorUnlockParser();
			} else if (page.equals("hooter")) {
				CommonPingURL puc = new CommonPingURL("others", HomeActivity.this);
				puc.execute(GetAPI.BASE_URL + GetAPI.HOOTER_ACK);

			} else if (page.equals("panic")) {
				CommonPingURL puc = new CommonPingURL("others", HomeActivity.this);
				puc.execute(GetAPI.BASE_URL + GetAPI.PANIC);
			} else if (page.equals("ip")){
				isActivityResult = true;
				radiant_value=false;
				showIPDialog(HomeActivity.this);

			}
		}


	}


	private void callPopup(final String control){
		AlertDialog alertDialog=new AlertDialog.Builder(this)
		.setMessage("You have opted for "+control+".\nWould you like to continue?")
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
			boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);
			public void onClick(DialogInterface dialog, int which) {
				if(App_Variable.isNetworkAvailable(HomeActivity.this)){
					if(control.equals("panic")){
						if(checkvalue){

							Intent in = new Intent(HomeActivity.this,UnlockActivity.class);
							in.putExtra("pagetype","panic");
							startActivityForResult(in, 10);
						}else {
							CommonPingURL puc = new CommonPingURL("others", HomeActivity.this);
							puc.execute(GetAPI.BASE_URL + GetAPI.PANIC);
						}
					}else if(control.equals("hooter")){
						if(checkvalue){
						Intent in = new Intent(HomeActivity.this,UnlockActivity.class);
                         in.putExtra("pagetype","hooter");
							startActivityForResult(in, 10);
					}else {
							CommonPingURL puc = new CommonPingURL("others", HomeActivity.this);
							puc.execute(GetAPI.BASE_URL + GetAPI.HOOTER_ACK);
						}

				}}else{
					App_Variable.ShowNoNetwork(HomeActivity.this);
				}
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

	private void CallRoomMood(String mood) {
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){
			CommonPingURL puc=new CommonPingURL("moodRoom",HomeActivity.this);
			puc.execute(GetAPI.BASE_URL+GetAPI.ROOM_MOOD_APPLY+mood);
		}else{
			App_Variable.ShowNoNetwork(this);
		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
        radiant_value=false;
			callExitPopup();

		}

		return super.onKeyDown(keyCode, event);
	}

	private void callExitPopup(){
		AlertDialog alertDialog=new AlertDialog.Builder(this)
		.setMessage("Are you sure you want to leave?")
		.setTitle("Exit")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try{

					App_Variable.appMinimize=0;

					finish();
				}catch (Exception e) {
					// TODO: handle exception
					CustomLog.debug("ranjith time fail");
					finish();
				}
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.create();
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}

	@Override
	public void callExceptionPopup(String exception){
		if(progressDialog!=null){
			progressDialog.cancel();
		}
		CustomLog.resume("callExceptionPopup 1");
		showErrorPopup(exception);

	}

	@Override
	public void callProfileExceptionPopup(String exception, String typeOfIP) {

		CustomLog.resume("callProfileExceptionPopup:"+typeOfIP+","+GetAPI.BASE_PUBLIC_URL);

		if(App_Variable.m3G_CONNECTED){
			CustomLog.resume("callProfileExceptionPopup:1 3g");
			if(progressDialog!=null){
				progressDialog.cancel();
			}
			showErrorPopup(exception);
		}else{
			if(typeOfIP.equalsIgnoreCase("local")){
				if(GetAPI.BASE_PUBLIC_URL.equals("")){
					CustomLog.resume("callProfileExceptionPopup:1 static empty");
					if(progressDialog!=null){
						progressDialog.cancel();
					}
					showErrorPopup(exception);
				}else{
					CustomLog.resume("callProfileExceptionPopup:1");
					App_Variable.ipType="Static";
					StorageIPAddr(IPStatic);
					callProfile(true,true,App_Variable.ipType);
				}
			}else{
				CustomLog.resume("callProfileExceptionPopup:1 static else");

				if(progressDialog!=null){
					progressDialog.cancel();
				}
				showErrorPopup(exception);
			}
		}
	}


	/*@Override
	public void ShowHomeView(boolean _isNotification) {

		CustomLog.show("service not ShowHomeView");
		if(_isNotification){
			NotifyUserSensor();
		}
		screen="splash";
		//AlertGotoSensorPopUp();
		//if(App_Variable.pauseStage==0)
		AlertGotoSensor();


	}*/


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
				showIPDialog(HomeActivity.this);

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
	private void AlertGotoSensorPopUp() {
		if(progressDialog!=null)
			progressDialog.dismiss();

		CustomLog.check("service AlertGotoSensorPopUp");

		Intent intent=new Intent(HomeActivity.this,SensorActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("section", 0);
		startActivity(intent);
		finish();

	}
	private void AlertGotoSensor() {
		CustomLog.show("sensor home service AlertGotoSensor:"+App_Variable.SENSOR_STATUS);
		if(nextPage.equalsIgnoreCase("sensor")){
			CustomLog.show("sensor service sensor");
			callSensorPage();
		}else if(CheckSensorStatus()){
			CustomLog.show("sensor service checksensor staus");
			if(screen.equalsIgnoreCase("splash")){
				callSensorPage();
			}else{
				callProfile(false,false,"");
			}
		}else{
			CustomLog.show("sensor service AlertGotoSensor else");
			callProfile(false,false,"");
		}

	}
	private void callSensorPage() {
		if(progressDialog!=null)
			progressDialog.dismiss();

		Intent intent=new Intent(HomeActivity.this,SensorActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("section", 0);
		startActivity(intent);
		finish();
	}

	private void NotifyUserSensor() {
		NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(HomeActivity.this, SensorActivity.class);
		intent.putExtra("section", 0);

		//use the flag FLAG_UPDATE_CURRENT to override any notification already there
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new Notification(R.drawable.ic_launcher, "Silvan", System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;

		notification.setLatestEventInfo(this, "Silvan", "Your sensor is on", contentIntent);
		//10 is a random number I chose to act as the id for this notification
		notificationManager.notify(10, notification);

	}
	public boolean CheckSensorStatus() {
		if(App_Variable.SENSOR_STATUS!=null){
			if(!App_Variable.SENSOR_STATUS.equals("")){
				String[] sensorResponse=App_Variable.SENSOR_STATUS.trim().split("\n");
				int length = sensorResponse.length;
				for (int i = 0; i < length; i++) {
					//String[] split=sensorResponse[i].split("=");
					if(sensorResponse[i].endsWith("1"))
						return true;
					//				else if(sensorResponse[i].endsWith("2"))
					//					return true;
				}
			}
		}else{
			return false;
		}

		return false;

	}

	/*@Override
	public void showCameraView() {

		CustomLog.show("service not showCameraView::");
		//if(App_Variable.pauseStage==0)
		CameraView();

		NotifyUserCamera();

	}*/

	private void CameraView() {
		Intent intentCamera = new Intent(HomeActivity.this,CameraActivity.class);
		intentCamera.putExtra("page", 1);
		intentCamera.putExtra("screen", "vdb");
		intentCamera.putExtra("vdb_trigger", 1);
		startActivity(intentCamera);
	}
	/**
	 * push notification for camera
	 */
	private void NotifyUserCamera() {
		NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
		intent.putExtra("page", 1);
		intent.putExtra("screen", "vdb");
		intent.putExtra("vdb_trigger", 1);

		//use the flag FLAG_UPDATE_CURRENT to override any notification already there
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new Notification(R.drawable.ic_launcher, "Silvan", System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;

		notification.setLatestEventInfo(this, "Silvan", "Someone is on Your Door Step", contentIntent);
		//10 is a random number I chose to act as the id for this notification
		notificationManager.notify(10, notification);

	}
	/*@Override
	public void ShowNormalView() {
		// TODO Auto-generated method stub

	}*/


	UpdateProfile up;
	public void updateProfileHome() {
		if(App_Variable.isNetworkAvailable(HomeActivity.this)){
			CustomLog.profile("profileupdate 2");
			if(up!=null)
				up.cancel(true);
			up=new UpdateProfile(HomeActivity.this);
			up.execute();
		}else{
			//App_Variable.ShowNoNetwork(this);
		}

	}

	/*@Override
	public void updateProfile(String Profile) {
		displayProfile();
	}*/

	@Override
	public void ProfileUpdateResponse(String profile) {
		CustomLog.profile("ProfileUpdate Response profie:"+profile);

		if(profile==null){
			profile="";
		}
		App_Variable.PROFILE=profile;

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				displayProfile();

			}
		});


	}

	@Override
	public void ProfileUpdateException(String ex) {
		CustomLog.profile("ProfileUpdate ex:" + ex);

	}






	/*public void callUpdateProfile() throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//_refreshingDimmerStatus=false;
				while (_isUpdateProfile) {
					try {

						Thread.sleep(5000);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// Write your code here to update the UI.


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







}
