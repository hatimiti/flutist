package com.github.hatimiti.flutist.common.validation.validator;

import static com.github.hatimiti.flutist.common.message.AppMessageLevel.*;
import static java.util.Objects.*;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.message.OwnedMessages;
import com.github.hatimiti.flutist.common.validation.Vval;

public abstract class BaseFieldValidator implements Validator {

	protected AppMessagesContainer container;
	protected String templateMessageKey;

	protected BaseFieldValidator(AppMessagesContainer container) {
		requireNonNull(container);
		this.container = container;
		this.templateMessageKey = getDefaultMessageKey();
	}

	protected BaseFieldValidator key(String templateMessageKey) {
		requireNonNull(templateMessageKey);
		this.templateMessageKey = templateMessageKey;
		return this;
	}
	
	@Override
	public boolean check(
			final Vval value,
			final String owner,
			final Object... params) {

		boolean result = checkSpecifically(value);
		if (!result) {
			addMessage(owner, params);
		}
		return result;
	}

	protected void addMessage(final String owner, final Object... params) {
		addMessage(false, owner, params);
	}

	protected void addMessage(
			final boolean isGlobal,
			final String owner,
			final Object... params) {
		
		container.add(new OwnedMessages(owner, ERROR, this.templateMessageKey, params));
	}
	
	protected abstract boolean checkSpecifically(Vval value);
	protected abstract String getDefaultMessageKey();
}
