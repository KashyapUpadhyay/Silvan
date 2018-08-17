package com.divum.callback;

public interface ProfileCallback {

	void callHomeList(boolean isRefresh);

	void callProfileExceptionPopup(String exception, String typeOfIP);

}
