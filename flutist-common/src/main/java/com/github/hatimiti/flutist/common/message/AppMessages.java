package com.github.hatimiti.flutist.common.message;

import static java.util.Objects.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.hatimiti.flutist.common.domain.type.ArrayListType;

public class AppMessages implements Serializable {

	protected Map<String, AppMessageList> messages;
	
	public AppMessages() {
		this.messages = new HashMap<>();
	}
	
	public AppMessages(String property, String key, Object... params) {
		this(property, false, key, params);
	}

	public AppMessages(String property, boolean isResource, String keyOrMessage, Object... params) {
		add(property, new AppMessage(isResource, keyOrMessage, params));
	}
	
	public void add(String property, AppMessage message) {
		requireNonNull(property);
		requireNonNull(message);
		getBy(property).add(message);
	}
	
	public List<AppMessage> copyMessageListOf(String property) {
		requireNonNull(property);
		return new AppMessageList(getBy(property));
	}
	
	public boolean hasMessages() {
		return !this.messages.isEmpty();
	}
	
	public boolean isEmpty() {
		return !hasMessages();
	}
	
	protected List<AppMessage> getBy(String property) {
		return this.messages.merge(property, new AppMessageList(), (o, n) -> o);
	}
	
	protected static class AppMessageList 
			extends ArrayListType<AppMessage> {
		
		protected AppMessageList() {
		}
		protected AppMessageList(List<AppMessage> list) {
			super(list);
		}
	}
	
}
