package com.divum.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.divum.constants.GetAPI;
import com.divum.entity.ConfigEntity;
import com.divum.entity.HomeEntity;
import com.divum.entity.MoodEntity;
import com.divum.silvan.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App_Variable extends Application{

	//public static final String MSG_HOST_EXCEPTION = "Please verify your ip address";
	//public static final String MSG_CONNECT_EXCEPTION = "Connection is Timeout";

	public static final String MSG_CONNECT_EXCEPTION="System not reachable. So Please verify your ip address";//check your connectivity or 

	//public static final String NONETWORK = "No Network Connection";

	public static final String NONETWORK = "Please check your internet connection";

	public static String NoConnectionMsg="Problem connecting to the Internet. Please check your internet connection and try again.";

	public static LinkedHashMap<String, ArrayList<String>> hashRoomType=null;
	public static Hashtable<String, ArrayList<Hashtable<String, HomeEntity>>> hashRoomOptions=null;	
	public static LinkedHashMap<String, LinkedHashMap<String, String>> hashAC=null;
	public static LinkedHashMap<String, String> hashSensor=null;
	public static LinkedHashMap<String, String> hashNickName=null;
	public static LinkedHashMap<String, String> hashtype=null;
	//public static LinkedHashMap<String, String> hashCamera=null;

	public static HashMap<String, MoodEntity> hashMoodData;
	public static ArrayList<HomeEntity> hashListCamera;

	public static LinkedHashMap<String, ConfigEntity> hashMoodType=null;
	public static LinkedHashMap<String, ConfigEntity> hashMoodRooms=null;
	public static LinkedHashMap<String,String>hashEleurl=null;
	public static LinkedHashMap<String,String>hashtabname=null;

	//public static String VDBSTATUS="";
	public static String PROFILE="";
	public static int CURRENT_TAB=1;

	public static int SYSTEM=1;
	public static int AC=2;
	public static int MEDIA=3;
	public static int ELECTRICALS=4;
  //  public static String EleURL="";
	public static boolean _isServiceStarted=false;

	//	public static boolean OneTimeCall=false;

	//public static int fragmentStorage=0;
	public static String SENSOR_STATUS="";
	public static String MOOD_RESPONSE="";

	public static final String ImageColor_default = "Silvan/images";
	public static int appMinimize=0;

	public static String CONFIG_NUMBER="";
	public static final boolean DEVELOPER_MODE = false;
	//protected static Object context;


	public static String STATUS_EMS="0";
	public static String STATUS_CAMERA="1";
	public static String STATUS_LIFESTYLE="1";
	public static String STATUS_SENSORS="1";
	public static String STATUS_PANIC="1";
	public static String STATUS_DOOR_UNLOCK="1";

	public static String vdb_video_path="";
	public static String vdb_status_path="";

	public static final Pattern IP_ADDRESS
	= Pattern.compile(
			"((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
					+ "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
					+ "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
					+ "|[1-9][0-9]|[0-9]))");

	public static final Pattern IPV6_ADDRESS
	= Pattern.compile(
			"((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
					+ "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
					+ "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
					+ "|[1-9][0-9]|[0-9])\\:(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]))");

	public static boolean m3G_CONNECTED = false;
	public static boolean mWIFI_CONNECTED = false;
	public static String ipType="None";

	public static boolean isSensorPage=false;

	@SuppressWarnings("unused")
	public void onCreate() {
		if (DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		System.out.println("app started");

		super.onCreate();



		//CreateMoodsFind();
		//CreateMoods();

		initImageLoader(getApplicationContext());
	}





	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		System.out.println("app terminated");
		App_Variable.appMinimize=1;
		super.onTerminate();
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs() // Remove for release app
		.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}


	private  HashMap<String, String> CreateMoodsFind() {
		HashMap<String, String> hashMoodFind=new HashMap<String, String>();
		hashMoodFind.put("1&&Master On", "mood11");
		hashMoodFind.put("1&&Master Off", "mood12");
		hashMoodFind.put("1&&Morning", "mood13");
		hashMoodFind.put("1&&Evening", "mood14");
		hashMoodFind.put("1&&Dinner", "mood15");

		hashMoodFind.put("2&&Master On", "mood21");
		hashMoodFind.put("2&&Master Off", "mood22");
		hashMoodFind.put("2&&Morning", "mood23");
		hashMoodFind.put("2&&Evening", "mood24");
		hashMoodFind.put("2&&Dinner", "mood25");

		hashMoodFind.put("3&&Master On", "mood31");
		hashMoodFind.put("3&&Master Off", "mood32");
		hashMoodFind.put("3&&Morning", "mood33");
		hashMoodFind.put("3&&Evening", "mood34");
		hashMoodFind.put("3&&Dinner", "mood35");

		hashMoodFind.put("4&&Master On", "mood41");
		hashMoodFind.put("4&&Master Off", "mood42");
		hashMoodFind.put("4&&Morning", "mood43");
		hashMoodFind.put("4&&Evening", "mood44");
		hashMoodFind.put("4&&Dinner", "mood45");

		hashMoodFind.put("5&&Master On", "mood51");
		hashMoodFind.put("5&&Master Off", "mood52");
		hashMoodFind.put("5&&Morning", "mood53");
		hashMoodFind.put("5&&Evening", "mood54");
		hashMoodFind.put("5&&Dinner", "mood55");

		hashMoodFind.put("6&&Master On", "mood61");
		hashMoodFind.put("6&&Master Off", "mood62");
		hashMoodFind.put("6&&Morning", "mood63");
		hashMoodFind.put("6&&Evening", "mood64");
		hashMoodFind.put("6&&Dinner", "mood65");

		return hashMoodFind;

		/*hashMoodFind.put("LivingRoom-GF&&Master On", "mood11");
		hashMoodFind.put("LivingRoom-GF&&Master Off", "mood12");
		hashMoodFind.put("LivingRoom-GF&&Morning", "mood13");
		hashMoodFind.put("LivingRoom-GF&&Evening", "mood14");
		hashMoodFind.put("LivingRoom-GF&&Dinner", "mood15");

		hashMoodFind.put("LivingRoom-FF&&Master On", "mood21");
		hashMoodFind.put("LivingRoom-FF&&Master Off", "mood22");
		hashMoodFind.put("LivingRoom-FF&&Morning", "mood23");
		hashMoodFind.put("LivingRoom-FF&&Evening", "mood24");
		hashMoodFind.put("LivingRoom-FF&&Dinner", "mood25");

		hashMoodFind.put("Master BedRoom-GF&&Master On", "mood31");
		hashMoodFind.put("Master BedRoom-GF&&Master Off", "mood32");
		hashMoodFind.put("Master BedRoom-GF&&Morning", "mood33");
		hashMoodFind.put("Master BedRoom-GF&&Evening", "mood34");
		hashMoodFind.put("Master BedRoom-GF&&Dinner", "mood35");

		hashMoodFind.put("Master BedRoom-FF&&Master On", "mood41");
		hashMoodFind.put("Master BedRoom-FF&&Master Off", "mood42");
		hashMoodFind.put("Master BedRoom-FF&&Morning", "mood43");
		hashMoodFind.put("Master BedRoom-FF&&Evening", "mood44");
		hashMoodFind.put("Master BedRoom-FF&&Dinner", "mood45");

		hashMoodFind.put("BedRoom-GF&&Master On", "mood51");
		hashMoodFind.put("BedRoom-GF&&Master Off", "mood52");
		hashMoodFind.put("BedRoom-GF&&Morning", "mood53");
		hashMoodFind.put("BedRoom-GF&&Evening", "mood54");
		hashMoodFind.put("BedRoom-GF&&Dinner", "mood55");

		hashMoodFind.put("BedRoom-FF&&Master On", "mood61");
		hashMoodFind.put("BedRoom-FF&&Master Off", "mood62");
		hashMoodFind.put("BedRoom-FF&&Morning", "mood63");
		hashMoodFind.put("BedRoom-FF&&Evening", "mood64");
		hashMoodFind.put("BedRoom-FF&&Dinner", "mood65");

		//Extra
		hashMoodFind.put("Living Room-GF&&Master On", "mood11");
		hashMoodFind.put("Living Room-GF&&Master Off", "mood12");
		hashMoodFind.put("Living Room-GF&&Morning", "mood13");
		hashMoodFind.put("Living Room-GF&&Evening", "mood14");
		hashMoodFind.put("Living Room-GF&&Dinner", "mood15");

		hashMoodFind.put("Living Room-FF&&Master On", "mood21");
		hashMoodFind.put("Living Room-FF&&Master Off", "mood22");
		hashMoodFind.put("Living Room-FF&&Morning", "mood23");
		hashMoodFind.put("Living Room-FF&&Evening", "mood24");
		hashMoodFind.put("Living Room-FF&&Dinner", "mood25");


		hashMoodFind.put("Bed Room-GF&&Master On", "mood51");
		hashMoodFind.put("Bed Room-GF&&Master Off", "mood52");
		hashMoodFind.put("Bed Room-GF&&Morning", "mood53");
		hashMoodFind.put("Bed Room-GF&&Evening", "mood54");
		hashMoodFind.put("Bed Room-GF&&Dinner", "mood55");

		hashMoodFind.put("Bed Room-FF&&Master On", "mood61");
		hashMoodFind.put("Bed Room-FF&&Master Off", "mood62");
		hashMoodFind.put("Bed Room-FF&&Morning", "mood63");
		hashMoodFind.put("Bed Room-FF&&Evening", "mood64");
		hashMoodFind.put("Bed Room-FF&&Dinner", "mood65");

		//one more extra
		//Extra
		hashMoodFind.put("Living-GF&&Master On", "mood11");
		hashMoodFind.put("Living-GF&&Master Off", "mood12");
		hashMoodFind.put("Living-GF&&Morning", "mood13");
		hashMoodFind.put("Living-GF&&Evening", "mood14");
		hashMoodFind.put("Living-GF&&Dinner", "mood15");

		hashMoodFind.put("Living-FF&&Master On", "mood21");
		hashMoodFind.put("Living-FF&&Master Off", "mood22");
		hashMoodFind.put("Living-FF&&Morning", "mood23");
		hashMoodFind.put("Living-FF&&Evening", "mood24");
		hashMoodFind.put("Living-FF&&Dinner", "mood25");

		hashMoodFind.put("MBR-GF&&Master On", "mood31");
		hashMoodFind.put("MBR-GF&&Master Off", "mood32");
		hashMoodFind.put("MBR-GF&&Morning", "mood33");
		hashMoodFind.put("MBR-GF&&Evening", "mood34");
		hashMoodFind.put("MBR-GF&&Dinner", "mood35");

		hashMoodFind.put("MBR-FF&&Master On", "mood41");
		hashMoodFind.put("MBR-FF&&Master Off", "mood42");
		hashMoodFind.put("MBR-FF&&Morning", "mood43");
		hashMoodFind.put("MBR-FF&&Evening", "mood44");
		hashMoodFind.put("MBR-FF&&Dinner", "mood45");


		hashMoodFind.put("BR-GF&&Master On", "mood51");
		hashMoodFind.put("BR-GF&&Master Off", "mood52");
		hashMoodFind.put("BR-GF&&Morning", "mood53");
		hashMoodFind.put("BR-GF&&Evening", "mood54");
		hashMoodFind.put("BR-GF&&Dinner", "mood55");

		hashMoodFind.put("BR-FF&&Master On", "mood61");
		hashMoodFind.put("BR-FF&&Master Off", "mood62");
		hashMoodFind.put("BR-FF&&Morning", "mood63");
		hashMoodFind.put("BR-FF&&Evening", "mood64");
		hashMoodFind.put("BR-FF&&Dinner", "mood65");*/



	}

	private void CreateMoods() {
		hashMoodData=new HashMap<String, MoodEntity>();

		hashMoodData.put("mood11", createMoodEntity("LivingRoom-GF","Master On"));
		hashMoodData.put("mood12", createMoodEntity("LivingRoom-GF","Master Off"));
		hashMoodData.put("mood13", createMoodEntity("LivingRoom-GF","Morning"));
		hashMoodData.put("mood14", createMoodEntity("LivingRoom-GF","Evening"));
		hashMoodData.put("mood15", createMoodEntity("LivingRoom-GF","Dinner"));

		hashMoodData.put("mood21", createMoodEntity("LivingRoom-FF","Master On"));
		hashMoodData.put("mood22", createMoodEntity("LivingRoom-FF","Master Off"));
		hashMoodData.put("mood23", createMoodEntity("LivingRoom-FF","Morning"));
		hashMoodData.put("mood24", createMoodEntity("LivingRoom-FF","Evening"));
		hashMoodData.put("mood25", createMoodEntity("LivingRoom-FF","Dinner"));

		hashMoodData.put("mood31", createMoodEntity("Master BedRoom-GF","Master On"));
		hashMoodData.put("mood32", createMoodEntity("Master BedRoom-GF","Master Off"));
		hashMoodData.put("mood33", createMoodEntity("Master BedRoom-GF","Morning"));
		hashMoodData.put("mood34", createMoodEntity("Master BedRoom-GF","Evening"));
		hashMoodData.put("mood35", createMoodEntity("Master BedRoom-GF","Dinner"));

		hashMoodData.put("mood41", createMoodEntity("Master BedRoom-FF","Master On"));
		hashMoodData.put("mood42", createMoodEntity("Master BedRoom-FF","Master Off"));
		hashMoodData.put("mood43", createMoodEntity("Master BedRoom-FF","Morning"));
		hashMoodData.put("mood44", createMoodEntity("Master BedRoom-FF","Evening"));
		hashMoodData.put("mood45", createMoodEntity("Master BedRoom-FF","Dinner"));

		hashMoodData.put("mood51", createMoodEntity("BedRoom-GF","Master On"));
		hashMoodData.put("mood52", createMoodEntity("BedRoom-GF","Master Off"));
		hashMoodData.put("mood53", createMoodEntity("BedRoom-GF","Morning"));
		hashMoodData.put("mood54", createMoodEntity("BedRoom-GF","Evening"));
		hashMoodData.put("mood55", createMoodEntity("BedRoom-GF","Dinner"));

		hashMoodData.put("mood61", createMoodEntity("BedRoom-FF","Master On"));
		hashMoodData.put("mood62", createMoodEntity("BedRoom-FF","Master Off"));
		hashMoodData.put("mood63", createMoodEntity("BedRoom-FF","Morning"));
		hashMoodData.put("mood64", createMoodEntity("BedRoom-FF","Evening"));
		hashMoodData.put("mood65", createMoodEntity("BedRoom-FF","Dinner"));


	}

	private MoodEntity createMoodEntity(String area, String mood) {
		MoodEntity entity=new MoodEntity();
		entity.setArea(area);
		entity.setMoodName(mood);
		return entity;
	}

	public static void ShowNoNetworkPopUp(final Context context,final String whichScreen){

		/*LayoutInflater inflater=getLayoutInflater();
		View vi=inflater.inflate(R.layout.popup_txt_view, (ViewGroup) getCurrentFocus());
		TextView txtPop=(TextView)vi.findViewById(R.id.txtPopUp);
		txtPop.setText(msg);*/
		AlertDialog alertDialog=new AlertDialog.Builder(context)
		.setMessage(context.getString(R.string.no_network_txt))
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {                   
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();	
				context.startActivity(new Intent(Settings.ACTION_SETTINGS));

			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {                   
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();	
				if(whichScreen.equals("splash")){
					((Activity) context).finish();
				}
			}
		})
		.create();  
		alertDialog.setCancelable(false); 
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setInverseBackgroundForced(true);
		WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
		//wmlp.gravity = Gravity.TOP | Gravity.LEFT;
		wmlp.y=120;
		alertDialog.show();

	}

	//	public static boolean isHomePage=true;
	public static boolean isNetworkAvailable(Context activity) {
		ConnectivityManager cm =
				(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm!=null){
			NetworkInfo netInfo = cm.getActiveNetworkInfo();		
			if (!(netInfo != null && netInfo.isConnectedOrConnecting())) {
				return false;
			}else 
			{
				return true;
			}
		}else{
			return false;
		}
	}

	public static String getNetwork(Context con)  {

		try{
			ConnectivityManager connectivityManager = (ConnectivityManager)       
					con.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(connectivityManager!=null){
				NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo mobileInfo = 
						connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (wifiInfo.isConnected()) {
					return "wifi";
				}else if(mobileInfo.isConnected()){
					return "3g";
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		/**
		 * if it is 3g, app should work on static api. if static api not working fine use local api
		 * if it is wifi, app should work on local api. 
		 */
		return "";
	}

	public static boolean CheckValidateIP(String ipAddr) {
		Matcher ipv4Matcher = App_Variable.IP_ADDRESS.matcher(ipAddr);
		Matcher ipv6matcher = App_Variable.IPV6_ADDRESS.matcher(ipAddr);
		if (ipv4Matcher.matches()||ipv6matcher.matches()){
			return true;
		}
		return false;
	}
	public static int get_dp(Context _context,int dp) {
		Resources r = _context.getResources();
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());

		return px;
	}

	public static boolean isNumeric(String str)
	{
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		return str.length() == pos.getIndex();
	}


	public static String getNumberOfMonth(String _whichMonth) 
	{
		if(_whichMonth.equals("Jan")){
			return "01";
		}else  if(_whichMonth.equals("Feb")){
			return "02";
		}else  if(_whichMonth.equals("Mar")){
			return "03";
		}else  if(_whichMonth.equals("Apr")){
			return "04";
		}else  if(_whichMonth.equals("May")){
			return "05";
		}else  if(_whichMonth.equals("Jun")){
			return "06";
		}else  if(_whichMonth.equals("Jul")){
			return "07";
		}else  if(_whichMonth.equals("Aug")){
			return "08";
		}else  if(_whichMonth.equals("Sep")){
			return "09";
		}else  if(_whichMonth.equals("Oct")){
			return "10";
		}else  if(_whichMonth.equals("Nov")){
			return "11";
		}else  if(_whichMonth.equals("Dec")){
			return "12";
		}
		return null;
	}

	public static String getfMonth(int _whichMonth) 
	{
		if(_whichMonth==1){
			return "Jan";
		}else if(_whichMonth==2){
			return "Feb";
		}else  if(_whichMonth==3){
			return "Mar";
		}else  if(_whichMonth==4){
			return "Apr";
		}else  if(_whichMonth==5){
			return "May";
		}else  if(_whichMonth==6){
			return "Jun";
		}else  if(_whichMonth==7){
			return "Jul";
		}else  if(_whichMonth==8){
			return "Aug";
		}else  if(_whichMonth==9){
			return "Sep";
		}else  if(_whichMonth==10){
			return "Sep";
		}else  if(_whichMonth==11){
			return "Nov";
		}else  if(_whichMonth==12){
			return "Dec";
		}
		return null;
	}

	public static String getDay(int _whichMonth) 
	{
		if(_whichMonth==1){
			return "Monday";
		}else if(_whichMonth==2){
			return "Tuesday";
		}else  if(_whichMonth==3){
			return "Wednesday";
		}else  if(_whichMonth==4){
			return "Thursday";
		}else  if(_whichMonth==5){
			return "Friday";
		}else  if(_whichMonth==6){
			return "Saturday";
		}else  if(_whichMonth==7){
			return "Sunday";
		}
		return "";
	}

	public static void showIPDialog(final Context context) {
		// TODO Auto-generated method stub

		final CustomDialog alert = new CustomDialog(context,R.style.customDialogTheme);//customDialogTheme
		alert.setCanceledOnTouchOutside(true);		
		alert.setContentView(R.layout.ipdialog_view);		

		final EditText txtIPAddress=(EditText)alert.findViewById(R.id.txtIPAddress);
		Button btn_ok=(Button)alert.findViewById(R.id.btn_ok);

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(txtIPAddress.getText().equals("")){
					Toast.makeText(context, "Please enter ip address", Toast.LENGTH_SHORT).show();
				}else{
					GetAPI.BASE_URL =txtIPAddress.getText().toString().trim();
					if(!GetAPI.BASE_URL.startsWith("http")){
						GetAPI.BASE_URL ="http://"+GetAPI.BASE_URL;
					}
					if(GetAPI.BASE_URL.endsWith("/"))
						GetAPI.BASE_URL =GetAPI.BASE_URL.substring(0,GetAPI.BASE_URL.length()-1);


					alert.dismiss();
				}

				//display_web_content(detail[0]);
			}
		});		

		alert.show();
	}

	/**
	 * 
	 * @param old_string
	 * @return if give "dimmer" return "Dimmer"
	 */
	public static String convertString(String old_string){
		String new_string = "";
		char newchar;

		char[] arrays = old_string.toCharArray();

		for(int j = 0; j < arrays.length; j ++){
			if(j==0){
				newchar = Character.toUpperCase(arrays[j]);
			}
			else{
				newchar = arrays[j];
			}
			new_string = new_string + newchar;
		}

		return new_string;
	}

	public static String getAppendZero(String text) {

		if(text.length()==1){
			text= "0"+text;
		}

		return text;
	}

	public static void ShowErrorToast(String exception, Context context) {
		String errorMsg=exception.trim().toLowerCase();

		if(errorMsg.contains("host")){
			Toast.makeText(context, App_Variable.MSG_CONNECT_EXCEPTION, Toast.LENGTH_SHORT).show();
		}else if(errorMsg.contains("connect")){
			Toast.makeText(context, App_Variable.MSG_CONNECT_EXCEPTION, Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, exception, Toast.LENGTH_SHORT).show();
		}

	}

	public static String getBaseAPI(Context context){//;;;;;;
		String baseUrl="";
		SharedPreferences storagePref=context.getSharedPreferences("IP", context.MODE_PRIVATE);
		String whichView=storagePref.getString("view", "");
		if(whichView.equals("demo")){
			baseUrl = GetAPI.DEMO_URL;
		}else{
	//		App_Variable.ipType =storagePref.getString("ipType", "None");

			if(App_Variable.mWIFI_CONNECTED)
				if(App_Variable.ipType.equalsIgnoreCase("local")) {
					baseUrl = App_Variable.getBaseAPIHost(storagePref.getString("local_ip", ""));
				}else {
					baseUrl=App_Variable.getBaseAPIHost(storagePref.getString("static_ip", ""));
				}
			else if(App_Variable.m3G_CONNECTED)
				baseUrl=App_Variable.getBaseAPIHost(storagePref.getString("static_ip", ""));

			GetAPI.BASE_LOCAL_VDB = storagePref.getString("local_vdb", "");
			GetAPI.BASE_STATIC_VDB = storagePref.getString("static_vdb", "");
		}

		return baseUrl;
	}

	public static String getBaseAPIHost(String ipAddr){
		if(!ipAddr.startsWith("http")){
			ipAddr ="http://"+ipAddr;
		}
		if(ipAddr.endsWith("/"))
			ipAddr =ipAddr.substring(0,ipAddr.length()-1);
		return ipAddr;
	}

	public static void ShowNoNetwork(final Context context) {
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(context, context.getString(R.string.no_network_txt), Toast.LENGTH_SHORT).show();
			}
		});

	}
	
	public static String getDemoValue(Context context ){
		
		SharedPreferences storagePref=context.getSharedPreferences("IP", context.MODE_PRIVATE);
		return storagePref.getString("view", "");
		
	}


	/*public static void ShowNoNetwork(final Context context,String msg){
		AlertDialog alertDialog=new AlertDialog.Builder(context)
		.setMessage(NONETWORK)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {                   
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();	
				context.startActivity(new Intent(Settings.ACTION_SETTINGS));
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
	}*/


	public static String getDataFromFile(String path,Context context){
		InputStream input;
		String text="";
		try {
			input = context.getAssets().open(path);

			// myData.txt can't be more than 2 gigs.
			int size = input.available();
			byte[] buffer = new byte[size];
			input.read(buffer);
			input.close();

			// byte buffer into a string
			text = new String(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("txt:"+text);
		return text;
	}



	public static String[] GetHomeValue() {

		String[] value={"HOOTER","CAMERA","VDB","PROFILE","PANIC"};
		int totalSize=0;
		int size= value.length;
		String[] newValue=new String[size];
		for (int i = 0; i < size; i++) {
			if(i==0){
				if(App_Variable.STATUS_SENSORS.equals("0")){
					newValue[i]="";
				}else{
					totalSize++;
					newValue[i]=value[i];
				}
			}else if(i==1){
				if(App_Variable.STATUS_CAMERA.equals("0")){
					newValue[i]="";
				}
				else{
					totalSize++;
					newValue[i]=value[i];
				}
			}else if(i==2){
				if(App_Variable.STATUS_EMS.equals("0")){
					newValue[i]="";
				}
				else{
					totalSize++;
					newValue[i]=value[i];
				}
			}else if(i==3){
				if(App_Variable.STATUS_SENSORS.equals("0")){
					newValue[i]="";
				}else{
					totalSize++;
					newValue[i]=value[i];
				}
			}else if(i==4){
				if(App_Variable.STATUS_PANIC.equals("0")){
					newValue[i]="";
				}
				else{
					totalSize++;
					newValue[i]=value[i];
				}
			}
		}
		int count=0;
		String[] originalValue=new String[totalSize];
		for (int i = 0; i < size; i++) {
			if(!newValue[i].equals("")){
				originalValue[count]=newValue[i];
				count++;
			}

		}

		return originalValue;

	}


	public static String[] GetSettingsValue() {

		String[] value={"Sensor Status","Refresh","Log","Profile","Mood","Ip","About us","Pincode settings"};
		int totalSize=0;
		int size= value.length;
		String[] newValue=new String[size];
		for (int i = 0; i < size; i++) {
			if(i==0){
				if(App_Variable.STATUS_SENSORS.equals("0")){
					newValue[i]="";
				}else{
					totalSize++;
					newValue[i]=value[i];
				}
			}else if(i==1){
				if(App_Variable.STATUS_LIFESTYLE.equals("0")){
					newValue[i]="";
				}
				else{
					totalSize++;
					newValue[i]=value[i];
				}
			}else if(i==2){
				if(App_Variable.STATUS_SENSORS.equals("0")&&App_Variable.STATUS_DOOR_UNLOCK.equals("0")){
					newValue[i]="";
				}
				else{
					totalSize++;
					newValue[i]=value[i];
				}
			}else if(i==3){
				if(App_Variable.STATUS_SENSORS.equals("0")){
					newValue[i]="";
				}else{
					totalSize++;
					newValue[i]=value[i];
				}
			}else if(i==4){
				if(App_Variable.STATUS_LIFESTYLE.equals("0")){
					newValue[i]="";
				}
				else{
					totalSize++;
					newValue[i]=value[i];
				}
			}else {
				totalSize++;
				newValue[i]=value[i];
			}
		}
		int count=0;
		String[] originalValue=new String[totalSize];
		for (int i = 0; i < size; i++) {
			if(!newValue[i].equals("")){
				originalValue[count]=newValue[i];
				count++;
			}

		}

		return originalValue;

	}






	public static void getConfigData(Context context) {
		SharedPreferences configPref = context.getSharedPreferences("CONFIG", MODE_PRIVATE);	
		App_Variable.STATUS_EMS=configPref.getString("EMS", "1");
		App_Variable.STATUS_CAMERA=configPref.getString("CAMERA", "1");
		App_Variable.STATUS_LIFESTYLE=configPref.getString("LIFESTYLE", "1");
		App_Variable.STATUS_SENSORS=configPref.getString("SENSOR", "1");
		App_Variable.STATUS_PANIC=configPref.getString("PANIC", "1");
		App_Variable.STATUS_DOOR_UNLOCK=configPref.getString("DOOR_UNLOCK", "1");

		CustomLog.show("App_Variable.STATUS_PANIC:"+App_Variable.STATUS_PANIC);		
		CustomLog.show("App_Variable.STATUS_DOOR_UNLOCK:"+App_Variable.STATUS_DOOR_UNLOCK);		
		CustomLog.show("App_Variable.STATUS_SENSORS:"+App_Variable.STATUS_SENSORS);		
	}



	/*public static void StorePause(Context context, int pos) {
		SharedPreferences STORAGE=context.getSharedPreferences("Mini", MODE_PRIVATE);
		SharedPreferences.Editor store=STORAGE.edit();
		store.putInt("pause", pos);
		store.commit();

	}

	public static int ReteivePause(Context context){
		SharedPreferences STORAGE=context.getSharedPreferences("Mini", MODE_PRIVATE);
		CustomLog.show("state ret:"+STORAGE.getInt("pause", 0));
		return STORAGE.getInt("pause", 0);
	}*/





}
