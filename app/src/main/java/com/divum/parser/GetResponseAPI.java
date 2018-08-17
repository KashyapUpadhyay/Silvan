package com.divum.parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.divum.callback.ResponseCallback;
import com.divum.silvan.SensorActivity;
import com.divum.utils.CustomLog;


public class GetResponseAPI extends AsyncTask<String, Void, Void>{

	private String response="";
	private String tag="GetResponse";
	private Context context;
	private boolean isLoading;
	private String exception="";
	private ProgressDialog progress;

	private ResponseCallback callback;
	public GetResponseAPI(Context _context,boolean _isLoading) {
		this.context=_context;
		this.isLoading=_isLoading;
		callback = (ResponseCallback) context;

		progress = new ProgressDialog(context);
		progress.setMessage("Loading..");
		progress.setCanceledOnTouchOutside(false);

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if(isLoading)
			progress.show();
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		BaseParser parser=new BaseParser(params[0]);
		response=parser.getResponse();
		exception=parser.getException().trim();
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		CustomLog.d(tag, "PROFILE:"+response);
		//txt_homeProfile.setText(App_Variable.PROFILE.toUpperCase());
		if(isLoading)
			progress.dismiss();
		callback.setResponse(response,exception);
		//((LogActivity) context).setResponse(response);
		super.onPostExecute(result);
	}

}