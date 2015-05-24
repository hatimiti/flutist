package com.github.hatimiti.flutist.common.validation;

import static java.util.Objects.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationMessages implements Serializable {

	protected Map<String, List<ValidationMessage>> messages;
	
	public ValidationMessages() {
		this.messages = new HashMap<>();
	}
	
	public ValidationMessages(String property, String key, Object... params) {
		this(property, false, key, params);
	}

	public ValidationMessages(String property, boolean isResource, String keyOrMessage, Object... params) {
		add(property, new ValidationMessage(isResource, keyOrMessage, params));
	}
	
	public void add(String property, ValidationMessage message) {
		requireNonNull(property);
		requireNonNull(message);
		getBy(property).add(message);
	}
	
	public List<ValidationMessage> getCopyBy(String property) {
		requireNonNull(property);
		return new ArrayList<>(getBy(property));
	}
	
	public boolean hasMessages() {
		return !this.messages.isEmpty();
	}
	
	public boolean isEmpty() {
		return !hasMessages();
	}
	
	protected List<ValidationMessage> getBy(String property) {
		return this.messages.merge(property, new ArrayList<>(), (o, n) -> n);
	}
	
}
