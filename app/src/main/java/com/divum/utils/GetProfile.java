package com.divum.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.divum.callback.ProfileCallback;
import com.divum.constants.GetAPI;
import com.divum.parser.BaseParser;

public class GetProfile extends AsyncTask<Void, Void, Void> {

    private Context context;
    private ProfileCallback callback;
    private String exception = "";
    private boolean isRefresh;
    private String typeOfIP = "";

    public GetProfile(Context _context, boolean _isRefresh) {
        context = _context;
        isRefresh = _isRefresh;

        callback = (ProfileCallback) context;
        GetAPI.BASE_URL = App_Variable.getBaseAPI(context);

    }

    public GetProfile(Context _context, boolean _isRefresh, String _typeOfIp) {
        context = _context;
        isRefresh = _isRefresh;
        typeOfIP = _typeOfIp;

        callback = (ProfileCallback) context;

        GetAPI.BASE_URL = App_Variable.getBaseAPI(context);

    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        String url = App_Variable.getBaseAPI(context) + GetAPI.PROFILE_URL;
        CustomLog.camera("profile->" + url);

        BaseParser parser = new BaseParser(url);
//		Toast.makeText(context, "prof " + url, Toast.LENGTH_SHORT).show();
        System.out.println("profile url" + url);
        App_Variable.PROFILE = parser.getResponse();
        exception = parser.getException();

        return null;

    }

    @Override
    protected void onPostExecute(Void result) {
        CustomLog.camera("PROFILE:" + App_Variable.PROFILE + "," + exception);
        if (App_Variable.PROFILE == null) {
            App_Variable.PROFILE = "";
        }
        super.onPostExecute(result);


        if (exception.equals("")) {
            callback.callHomeList(isRefresh);
            //callListParser();
        } else {
            //	App_Variable.ShowErrorToast(exception,SplashActivity.this);
            //callExceptionPopup(exception);
//            if(App_Variable.STATUS_SENSORS.equals("0")){
//                callback.callHomeList(isRefresh);
//            }
            callback.callProfileExceptionPopup(exception, typeOfIP);

        }

    }

}
