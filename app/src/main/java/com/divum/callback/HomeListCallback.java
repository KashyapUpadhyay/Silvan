package com.divum.callback;

import android.content.Context;

public interface HomeListCallback {

	void callExceptionPopup(String errorString);

	void StartBGService(Context context, String whichScreen);

}
