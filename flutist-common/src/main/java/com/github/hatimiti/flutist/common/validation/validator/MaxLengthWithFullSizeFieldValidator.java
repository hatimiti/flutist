package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.validation.Vval;

/**
 * 最大文字数チェック[全角]を行うバリデータクラス．
 * @author hatimiti
 */
public class MaxLengthWithFullSizeFieldValidator extends BaseFieldValidator {

	public static final String MAX_LENGTH_WITH_FULL_SIZE_FIELD_VALIDATOR_KEY =
		"valid.max.length.with.full.size";

	/** 最大文字数 */
	protected int max;

	public MaxLengthWithFullSizeFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	protected boolean checkSpecifically(Vval value) {
		return checkMaxLengthWithFullSize(value.getValues()[0], this.max);
	}

	public static boolean checkMaxLengthWithFullSize(String value, int max) {
		return FullSizeFieldValidator.checkFullSize(value)
				&& value.length() <= max;
	}

	protected String getDefaultMessageKey() {
		return MAX_LENGTH_WITH_FULL_SIZE_FIELD_VALIDATOR_KEY;
	}

	public MaxLengthWithFullSizeFieldValidator setMax(int max) {
		this.max = max;
		return this;
	}

}
