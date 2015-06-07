package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.validation.Vval;


/**
 * 整数の最大値チェックを行う．
 * 最大値以下であるかどうか．
 * 指定された文字列が整数かどうかも合わせてチェックする．
 * @author hatimiti
 */
public class IntMaxValueFieldValidator extends BaseFieldValidator {

	public static final String INT_MAX_VALUE_FIELD_VALIDATOR_KEY =
		"valid.int.max.value";

	/** 最大数 */
	protected int max;

	public IntMaxValueFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	protected boolean checkSpecifically(Vval value) {
		return checkIntMaxValue(value.getValues()[0], this.max);
	}

	public static boolean checkIntMaxValue(String value, int max) {

		// 整数かどうかチェック
		if (!IntFieldValidator.checkInt(value)) {
			return false;
		}

		int intVal = Integer.parseInt(value);

		return intVal <= max;
	}

	protected String getDefaultMessageKey() {
		return INT_MAX_VALUE_FIELD_VALIDATOR_KEY;
	}

	public IntMaxValueFieldValidator setMax(int max) {
		this.max = max;
		return this;
	}

}
