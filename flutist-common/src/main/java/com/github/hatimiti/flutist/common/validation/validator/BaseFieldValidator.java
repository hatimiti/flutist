package com.github.hatimiti.flutist.common.validation.validator;

import static java.util.Objects.*;

import com.github.hatimiti.flutist.common.validation.ValidationMessage;
import com.github.hatimiti.flutist.common.validation.ValidationMessages;
import com.github.hatimiti.flutist.common.validation.Vval;

public abstract class BaseFieldValidator implements Validator {

	protected ValidationMessages messages;
	protected String templateMessageKey;

	protected BaseFieldValidator(ValidationMessages messages) {
		requireNonNull(messages);
		this.messages = messages;
		this.templateMessageKey = getDefaultMessageKey();
	}

	protected BaseFieldValidator key(String templateMessageKey) {
		requireNonNull(messages);
		this.templateMessageKey = templateMessageKey;
		return this;
	}
	
	@Override
	public boolean check(
			final Vval value,
			final String property,
			final Object... params) {

		boolean result = checkSpecifically(value);
		if (!result) {
			addMessage(property, params);
		}
		return result;
	}

	protected void addMessage(final String property, final Object... params) {
		addMessage(false, property, params);
	}

	protected void addMessage(
			final boolean isGlobal,
			final String property,
			final Object... params) {
		
		messages.add(property, new ValidationMessage(this.templateMessageKey, params));
	}
	
	protected abstract boolean checkSpecifically(Vval value);
	protected abstract String getDefaultMessageKey();
}
