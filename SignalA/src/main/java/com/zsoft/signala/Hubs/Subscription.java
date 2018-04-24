package com.zsoft.signala.Hubs;

import org.json.JSONObject;

public abstract class Subscription {

	public abstract void OnReceived(JSONObject args);

}
