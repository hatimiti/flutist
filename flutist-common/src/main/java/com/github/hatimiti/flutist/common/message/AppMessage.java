package com.github.hatimiti.flutist.common.message;

import static java.util.Objects.*;

import java.io.Serializable;

public class AppMessage implements Serializable {

	/**
	 * <p>The message key or literal message. If isResource is true, this field means literal message.</p>
	 */
	protected String keyOrMessage = null;

	/**
	 * <p>The replacement values for this mesasge.</p>
	 */
	protected Object params[] = null;

	/**
	 * <p>Indicates whether the key is taken to be as a  bundle key [true] or literal value [false].</p>
	 */
	protected boolean isResource = true;

	public AppMessage(String key, Object... params) {
		this(false, key, params);
	}

	public AppMessage(boolean isResource, String keyOrMessage, Object... params) {
		requireNonNull(keyOrMessage);
		this.isResource = isResource;
		this.keyOrMessage = keyOrMessage;
		this.params = params;
	}
	
	public String getKeyOrMessage() {
		return this.keyOrMessage;
	}
	
	public boolean isResource() {
		return this.isResource;
	}

	/**
	 * <p>Returns a String in the format: key[value1, value2, etc].</p>
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();
		buff.append(this.keyOrMessage);
		buff.append("[");
		if (this.params != null) {
			for (int i = 0; i < this.params.length; i++) {
				buff.append(this.params[i]);
				// don't append comma to last entry
				if (i < this.params.length - 1) {
					buff.append(", ");
				}
			}
		}
		buff.append("]");
		return buff.toString();
	}

}
