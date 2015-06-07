package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.validation.Vval;


/**
 * 最大文字数チェック[半角]を行うバリデータクラスです．
 * @author hatimiti
 */
public class MaxLengthWithHalfSizeFieldValidator extends BaseFieldValidator {

	public static final String MAX_LENGTH_WITH_HALF_SIZE_FIELD_VALIDATOR_KEY =
		"valid.max.length.with.half.size";

	/** 最大文字数 */
	protected int max;

	public MaxLengthWithHalfSizeFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	protected boolean checkSpecifically(Vval value) {
		return checkMaxLengthWithHalfSize(value.getValues()[0], this.max);
	}

	public static boolean checkMaxLengthWithHalfSize(String value, int max) {
		return HalfSizeFieldValidator.checkHalfSize(value)
				&& value.length() <= max;
	}

	public MaxLengthWithHalfSizeFieldValidator setMax(int max) {
		this.max = max;
		return this;
	}

	@Override
	protected String getDefaultMessageKey() {
		return MAX_LENGTH_WITH_HALF_SIZE_FIELD_VALIDATOR_KEY;
	}

}
