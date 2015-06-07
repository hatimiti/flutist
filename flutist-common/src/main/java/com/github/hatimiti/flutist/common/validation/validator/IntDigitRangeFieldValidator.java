package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.util._Str;
import com.github.hatimiti.flutist.common.validation.Vval;

/**
 * 整数の範囲チェックを行う．
 * 最小桁以上 かつ 最大桁以下であるかどうか．
 * 指定された文字列が整数かどうかも合わせてチェックする．
 * 「-123」などは3桁と判断してチェックする(マイナス記号は桁数に含めない)．
 * @author hatimiti
 */
public class IntDigitRangeFieldValidator extends BaseFieldValidator {

	public static final String INT_DIGIT_RANGE_FIELD_VALIDATOR_KEY =
		"valid.int.digit.range";

	/** 最小桁 */
	protected int min;
	/** 最大桁 */
	protected int max;

	public IntDigitRangeFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	protected boolean checkSpecifically(Vval value) {
		return checkIntDigitRange(value.getValues()[0], this.min, this.max);
	}

	public static boolean checkIntDigitRange(
			String value, int min, int max) {

		if (!IntMaxDigitFieldValidator.checkIntMaxDigit(value, max)) {
			return false;
		}

		String _value = value;

		_value = _Str.convertToHalfSizeNumString(_value);

		// マイナス記号があるかどうか
		int minus = 0;
		if (_value.charAt(0) == '-') {
			minus++;
		}

		return min <= (_value.length() - minus);
	}

	protected String getDefaultMessageKey() {
		return INT_DIGIT_RANGE_FIELD_VALIDATOR_KEY;
	}

	public IntDigitRangeFieldValidator setMin(int min) {
		this.min = min;
		return this;
	}

	public IntDigitRangeFieldValidator setMax(int max) {
		this.max = max;
		return this;
	}

}
