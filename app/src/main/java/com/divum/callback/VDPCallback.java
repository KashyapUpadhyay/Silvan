package com.divum.callback;

import android.content.Intent;

public interface VDPCallback {

	void VDPResponse(String trim,Intent intent);

	void VDPException(String trim, String vdbType);

}
