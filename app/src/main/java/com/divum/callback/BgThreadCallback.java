package com.divum.callback;

import android.content.Intent;

public interface BgThreadCallback {

	void ShowHomeView(boolean _isNotification,String isFrom);

	void showCameraView(Intent intent);

	//void ShowNormalView();

}
