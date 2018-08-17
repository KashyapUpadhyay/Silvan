package com.divum.constants;

public class GetAPI {
	
	
	public static final String DEMO_URL = "https://s3.amazonaws.com/silvanlabs/temp/saya";
	public static String BASE_URL="";
	public static String BASE_PUBLIC_URL="";
	public static String BASE_LOCAL_VDB="";
	public static String BASE_STATIC_VDB="";
	
	public static String HOME_URL="/web/data/area.json";
	public static String VDBSTATUS_URL="/web/data/getVDBStatus.txt";
	public static String SENSORSTATUS_URL="/cgi-bin/scripts/atzi_web_sensorStatus.sh";
	public static String VDPSTATUS_URL=":84/goform/device?cmd=get";
	public static String PROFILE_URL="/data/currentProfile.txt";
	public static String STATUS_DIMMERLIGHT_URL="/cgi-bin/scripts/atzi_web_lightStatus.sh";
	
	public static String ACTIVATE_DIMMER_CTRL_URL="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/";
	
	public static String LOG_SENSOR="/data/sensorLog.txt";
	public static String LOG_PROFILE="/data/profileLog.txt";
	public static String LOG_DOOR_UNLOCK="/data/doorunlockLog.txt";
	
	public static String HOOTER_ACK="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/atzi_sys_stopHooter.sh++";
	public static String PANIC="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/atzi_web_panicButton.sh++";
	public static String REFRESH="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/atzi_sys_frio.sh";

	public static String ROOM_MOOD_APPLY="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_mood_control.sh+";
	/*
	 * profile
	 */
	public static String PROFILE_LIST="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_profile_defShow.sh+";
	public static String PROFILE_CHECK_ARM="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_profile_check.sh+";
	public static String PROFILE_BYPASS="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_profile_bypassShow.sh+";
	public static String PROFILE_BYPASS_ADD="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_profile_bypassAdd.sh+" ;
	public static String PROFILE_BYPASS_REMOVE="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_profile_bypassRemove.sh+";
	
	public static String PROFILE_ARM="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_profile_arm.sh+";
	//http://192.168.1.231/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_profile_bypassAdd.sh+Away+doorintrusionB0
	/*
	 * profile configuration
	 */
	public static String PROFILE_CONFIG="/data/profile.json";
	public static String PROFILE_CONFIG_APPLY="/cgi-bin/config_manager/post.sh?/www/data/profile.json";
	
	public static String PROFILE_SCHEDULE1="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_sched_setScheduler.sh+";
	public static String PROFILE_SCHEDULE2="/www/cgi-bin/scripts/app_web_profile_arm.sh";
	
	
	public static String PROFILE_STATUS="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_sched_showTasks.sh+/www/cgi-bin/scripts/app_web_profile_arm.sh";
	public static String PROFILE_STATUS_DELETE="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_sched_deleteEntry.sh+";
	
	/*
	 * moood configuration
	 */
	public static String MOOD_DATA="/web/data/data.json";
	public static String MOOD_CONFIG="/data/mood.json";
	public static String MOOD_CONFIG_POST="/cgi-bin/config_manager/post.sh?/www/data/mood.json";
	
	public static String MOOD_SCHEDULE1="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_sched_setScheduler.sh+";
	public static String MOOD_SCHEDULE2="/www/cgi-bin/scripts/app_web_mood_control.sh";
	
	public static String MOOD_STATUS="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_sched_showTasks.sh+/www/cgi-bin/scripts/app_web_mood_control.sh";
	public static String MOOD_DELETE="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_sched_deleteEntry.sh+";
	
	public static String SENSOR_ACK="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/app_web_rio_sensorAcknoledge.sh";
	
	/*
	 * camera functionality
	 */
	public static String CAMERA_LIVE_STREAM="/goform/stream?cmd=get&channel=0";
	public static String CAMERA_WATCH="/goform/ptz?cmd=set&ptzcontrol=2&ptzparam=0";
	public static String CAMERA_ANSWER="/goform/ptz?cmd=set&ptzcontrol=1&ptzparam=0";
	public static String CAMERA_REJECT="/goform/ptz?cmd=set&ptzcontrol=1&ptzparam=1";
	
	public static String CAMERA_DOOR_UNLOCK="/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/atzi_web_doorUnlock.sh++";
	
	public static String Splash_URL1="https://s3.amazonaws.com/silvanlabs/apps/has/";//http://s3.amazonaws.com/divum_test/silvan/
	public static String Splash_URL2="/images/android_hdpi/splash_image.jpg";
	
	public static String CONFIG_URL="https://s3.amazonaws.com/silvanlabs/apps/has/";//http://s3.amazonaws.com/divum_test/silvan/
	public static String CONFIG_URL1="/config.json";
	
	public static String HOME_DUMMY_URL="http://divumapps.com/ClientApps/Silvan/area.json";
	
	
	//-->http://192.168.1.231/web/data/getVDBStatus.txt?1400044749204
	//-->http://192.168.1.231/cgi-bin/scripts/atzi_web_lightStatus.sh?1400045017000
	//-->http://192.168.1.231/data/currentProfile.txt?1400045017014
	//--->http://192.168.1.231/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/atzi_usr_dimmerControl.sh+SD_D401+1+1+0



}
