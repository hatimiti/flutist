package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.validation.Vval;

/**
 * 最小文字数チェック[半角]を行うバリデータクラスです．
 * @author hatimiti
 */
public abstract class MinLengthWithHalfSizeFieldValidator extends BaseFieldValidator {

	public static final String MIN_LENGTH_WITH_HALF_SIZE_FIELD_VALIDATOR_KEY =
		"valid.min.length.with.half.size";

	/** 最大文字数 */
	protected int min;

	public MinLengthWithHalfSizeFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	protected boolean checkSpecifically(Vval value) {
		return checkMinLengthWithHalfSize(value.getValues()[0], this.min);
	}

	public static boolean checkMinLengthWithHalfSize(String value, int min) {
		return HalfSizeFieldValidator.checkHalfSize(value)
				&& value.length() >= min;
	}

	public MinLengthWithHalfSizeFieldValidator setMin(int min) {
		this.min = min;
		return this;
	}

}
