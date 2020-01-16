package org.smap.session;

import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import java.util.UUID;


import org.smap.util.Log;


public class SessionData {
	private String sessionKey;
	Map<String,Object> sessionDataMap;	
	
	public SessionData(int key) {
		sessionDataMap = new HashMap<String,Object>();	
		this.sessionKey =key+"";
	}
	
	
	public SessionData() {
		sessionDataMap = new HashMap<String,Object>();	
		this.sessionKey = UUID.randomUUID().toString();
	}
	
	public static SessionData NewSessionDataFromJson(String sessionDataJsonString) {
		Gson gson = new Gson();
		Type jsonType = new TypeToken<SessionData>() {}.getType();
		SessionData sessionData = gson.fromJson(sessionDataJsonString,jsonType);
		return(sessionData);
	}
	
	public boolean doesSessionHaveData() {
		return(! this.sessionDataMap.isEmpty());
	}
	
	public String getJsonString() {
		Gson gson = new Gson();
		Type jsonType = new TypeToken<SessionData>() {}.getType();
		String jsonSTring = gson.toJson(this,jsonType);
		return(jsonSTring);
	}
	


	public boolean doesAttributeExist(String attributeKey) {
		boolean doesAttributeExist = this.sessionDataMap.containsKey(attributeKey);
		return doesAttributeExist;
	}

	
	public <T> T getSessionAttributeByKey(String attributeKey,Class<T> objectClass) {
		T objectFound = null;
		if(sessionDataMap.containsKey(attributeKey)) {
			Gson gson = new Gson();
			String jsonString = sessionDataMap.get(attributeKey).toString();
			objectFound = gson.fromJson(jsonString, objectClass);
		}
		return(objectFound);
	}
	
	public <T> T getSessionAttributeByKey2(String attributeKey) {
		Type jsonType = new TypeToken<T>() {}.getType();
		T objectFound = null;
		if(sessionDataMap.containsKey(attributeKey)) {
			Gson gson = new Gson();
			String jsonString = sessionDataMap.get(attributeKey).toString();

			objectFound = gson.fromJson(jsonString, jsonType);
		}
		
		return(objectFound);

	}
	
	public <T> void  setSessionAttributeWithClass(String attributeKey,T value) {
		Type jsonType = new TypeToken<T>() {}.getType();

		Gson gson = new Gson();
		String jsonString = gson.toJson(value,jsonType);
		this.setAttribute(attributeKey, jsonString);
	}
	
	private void setAttribute(String attributeKey, Object attributeObject) {
		this.sessionDataMap.put(attributeKey, attributeObject);
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}




}
