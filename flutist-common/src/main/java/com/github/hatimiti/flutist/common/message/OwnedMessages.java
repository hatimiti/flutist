package com.github.hatimiti.flutist.common.message;

import java.util.Objects;


public class OwnedMessages extends AppMessages {

	protected String owner;

	public OwnedMessages(String owner) {
		Objects.requireNonNull(owner);
		this.owner = owner;
	}
	
	public OwnedMessages(String owner, AppMessage message) {
		super(message);
		Objects.requireNonNull(owner);
		this.owner = owner;
	}
	
	public OwnedMessages(
			String owner,
			AppMessageLevel level, String key, Object... params) {
		super(level, key, params);
		Objects.requireNonNull(owner);
		this.owner = owner;
	}

	public OwnedMessages(
			String owner,
			AppMessageLevel level, boolean isResource, String keyOrMessage, Object... params) {
		super(level, isResource, keyOrMessage, params);
		Objects.requireNonNull(owner);
		this.owner = owner;
	}
	
	public String getOwner() {
		return this.owner;
	}

	@Override
	protected AppMessages createInstance() {
		return new OwnedMessages(this.owner);
	}

}
