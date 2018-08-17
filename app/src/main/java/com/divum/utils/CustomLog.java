package com.divum.utils;

import android.util.Log;

public class CustomLog {

	public static void d(String tag, String msg){

		//Log.d(tag, msg);
	}

	public static void e(String tag, String msg){
		//Log.e(tag, msg);

	}
	
	public static void error(String tag, String msg){

		//Log.e(tag, msg);
	}

	public static void debug(String msg){
	//	System.out.println(msg);
	}

	public static void sensorLog(String msg){

		//System.out.println(msg);
	}

	public static void parser(String msg){
		//System.out.println(msg);
	}

	public static void check(String msg){
		//System.out.println(msg);
	}

	public static void check(String tag, String msg) {
		//Log.d(tag, msg);

	}//08-20 16:22:25.590: I/System.out(16150): exception::

	public static void latest(String msg){
		//System.out.println(msg);
	}

	public static void latest(String tag, String msg){
		//Log.d(tag, msg);
	}
	
	public static void show(String msg){
		//System.out.println(msg);
	}
	public static void show(String tag,String msg){
		//Log.d(tag, msg);
	}

	public static void profile(String string) {
		//System.out.println(string);
		
	}
	public static void resume(String string) {
		//System.out.println(string);
	}
	public static void camera(String string) {
		//System.out.println(string);
	}
	public static void camera(String tag,String msg) {
		Log.d(tag, msg);
	}
	public static void sensor(String msg){
		//System.out.println(msg);
	}

}
