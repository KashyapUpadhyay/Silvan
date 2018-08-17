package com.divum.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.divum.callback.ProfileCallback;
import com.divum.constants.GetAPI;
import com.divum.parser.BaseParser;

public class UpdateProfile extends AsyncTask<Void, Void, Void>{

	private Context context;
	private String exception="";
	//private ProfileUpdateCallback callback;

	public UpdateProfile(Context _context) {
		CustomLog.show("UpdateProfile");
		context=_context;
		//callback=(ProfileUpdateCallback) context;
		CustomLog.profile("profileupdate 3");
		GetAPI.BASE_URL=App_Variable.getBaseAPI(context);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		App_Variable.PROFILE="";
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		CustomLog.profile("profileupdate 4");
		BaseParser parser=new BaseParser(GetAPI.BASE_URL+GetAPI.PROFILE_URL+"?"+System.currentTimeMillis());
		parser.getResponseProfileCallback(2000, context);
		CustomLog.profile("profileupdate 5");
//		App_Variable.PROFILE=parser.getTimeOutResponse(2000).trim();
//		exception = parser.getException();
//		parser=null;
		return null;

	}

	/*@Override
	protected void onPostExecute(Void result) {
		CustomLog.debug("PROFILE:"+App_Variable.PROFILE);
		if(App_Variable.PROFILE==null){
			App_Variable.PROFILE="";
		}
		callback.updateProfile(App_Variable.PROFILE);
		super.onPostExecute(result);
		
	}*/

}
