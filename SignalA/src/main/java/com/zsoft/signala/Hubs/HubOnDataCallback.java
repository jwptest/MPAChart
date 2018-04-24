package com.zsoft.signala.Hubs;

import org.json.JSONArray;

public abstract class HubOnDataCallback {
	public abstract void OnReceived(JSONArray args);
}
