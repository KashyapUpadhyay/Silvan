package com.divum.silvan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.divum.utils.App_Variable;

public class MyNetworkReceiver extends BroadcastReceiver{
	
	 /*<receiver android:name="com.divum.silvan.MyNetworkReceiver" >
     <intent-filter>
         <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
     </intent-filter>
 </receiver>*/

	@Override
	public void onReceive(Context context, Intent intent) {

		/*if(isConnected(context)) 
			Toast.makeText(context, "Connected.", Toast.LENGTH_LONG).show();
		else 
			Toast.makeText(context, "Lost connect.", Toast.LENGTH_LONG).show();*/

		//getNetworkName(context);

		String networkConnection=getNetworkName(context);
		//		if(networkConnection==null||networkConnection.equals("null"))
		//			Toast.makeText(context, "Connection Lost.", Toast.LENGTH_LONG).show();
		//		else
		//			Toast.makeText(context, "connecting to "+networkConnection, Toast.LENGTH_LONG).show();
	}

	public boolean isConnected(Context context) {
		ConnectivityManager cm =
				(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
				activeNetwork.isConnected();
		return isConnected;
	}

	public String getNetworkName(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager)       
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager!=null){
			SharedPreferences storagePref=context.getSharedPreferences("IP", context.MODE_PRIVATE);
			Editor editor=storagePref.edit();

			NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileInfo = 
					connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (wifiInfo!=null) {
				if(wifiInfo.isConnected()){
					App_Variable.mWIFI_CONNECTED=true;
					App_Variable.m3G_CONNECTED=false;

					editor.putString("network","wifi");
					editor.putString("ipType", "Local");
					editor.commit();
					return "wifi";
				}
			}else if(mobileInfo!=null){
				if(mobileInfo.isConnected()){
					App_Variable.m3G_CONNECTED=true;
					App_Variable.mWIFI_CONNECTED=false;
					editor.putString("network","3g");
					editor.putString("ipType", "Static");
					editor.commit();
					return "3g";
				}
			}
		}
		return null;
	}
}