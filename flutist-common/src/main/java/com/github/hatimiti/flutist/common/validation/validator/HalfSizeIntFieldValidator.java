package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.validation.Vval;


public class HalfSizeIntFieldValidator extends BaseFieldValidator {

	public static final String HALF_SIZE_INT_FIELD_VALIDATOR_KEY =
		"valid.half.size.int";

	public HalfSizeIntFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	protected boolean checkSpecifically(Vval value) {
		return checkHalfSizeInt(value.getValues()[0]);
	}

	public static boolean checkHalfSizeInt(String value) {
		return HalfSizeFieldValidator.checkHalfSize(value)
				&& IntFieldValidator.checkInt(value);
	}

	@Override
	protected String getDefaultMessageKey() {
		return HALF_SIZE_INT_FIELD_VALIDATOR_KEY;
	}

}
