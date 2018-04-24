package com.zsoft.signala.Hubs;

import com.zsoft.signala.Hubs.HubInvokeCallback;
import com.zsoft.signala.Hubs.HubOnDataCallback;

import java.util.Collection;

import org.json.JSONArray;

/// <summary>
///  A client side proxy for a server side hub.
/// </summary>
public interface IHubProxy {
	
	public void On(String eventName, HubOnDataCallback callback);
	public void Invoke(String method, Collection<?> args, HubInvokeCallback callback);
	public void Invoke(String method, JSONArray args, HubInvokeCallback callback);

        //JToken this[String name] { get; set; }
}
