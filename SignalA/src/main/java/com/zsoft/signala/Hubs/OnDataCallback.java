package com.zsoft.signala.Hubs;

import org.json.JSONObject;

public abstract class OnDataCallback {
	public abstract JSONObject Callback(JSONObject result);

}
