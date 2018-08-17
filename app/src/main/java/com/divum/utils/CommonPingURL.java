package com.divum.utils;

import com.divum.parser.BaseParser;
import com.divum.silvan.R.string;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class CommonPingURL extends AsyncTask<String, Void, Void>{

	ProgressDialog dialog;
	private String loadingSection="";
	private String hooterMsg="Please wait while hooter is being applied..";
	private String panicMsg="Please wait while panic is being applied..";
	private String moodMsg="Please wait while the selected mood is being applied..";

	private Context context;
	private String exception="";
	public CommonPingURL(String string, Context _context) {
		loadingSection =string;
		context =_context;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if(!loadingSection.equals("others")){
			dialog =new ProgressDialog(context);
			if(loadingSection.equals("hooter"))
				dialog.setMessage(hooterMsg);
			else if(loadingSection.equals("panic"))
				dialog.setMessage(panicMsg);
			else if(loadingSection.equals("moodRoom"))
				dialog.setMessage(moodMsg);	
			
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(String... params) {
		BaseParser parser=new BaseParser(params[0]);
		parser.getResponse();
		exception=parser.getException().trim();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		CustomLog.e("URL:","http://192.168.1.231/cgi-bin/saya_webAPI.sh?/www/cgi-bin/scripts/ completed:"+exception);

		if(!loadingSection.equals("others")){
			dialog.dismiss();
		}
		
		if(!exception.equals(""))
			App_Variable.ShowErrorToast(exception, context);
		super.onPostExecute(result);
	}



}
