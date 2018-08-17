package com.divum.callback;

public interface DimmerLightCallback {

	void DimmerStatusResponse(String response);

	void DimmerStatusException(String string);

}
