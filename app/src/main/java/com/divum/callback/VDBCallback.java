package com.divum.callback;

import android.content.Intent;

public interface VDBCallback {

	void VDBResponse(String response,Intent intent);

	void VDBException(String exception);

}
