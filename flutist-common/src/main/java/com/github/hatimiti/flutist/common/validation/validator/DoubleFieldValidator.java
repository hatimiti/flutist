package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.validation.Vval;

/**
 * 少数チェックを行うバリデータクラス
 * @author hatimiti
 */
public class DoubleFieldValidator extends BaseFieldValidator {

	public static final String DOUBLE_FIELD_VALIDATOR_KEY =
		"valid.double";

	protected DoubleFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	@Override
	protected boolean checkSpecifically(Vval value) {
		return checkDouble(value.getValues()[0]);
	}

	/**
	 * 引数として渡された値が少数かどうかをチェックする．
	 * @param value チェック対象文字列
	 * @return
	 * 		少数に変換可能な文字列であれば true<br>
	 * 		少数に変換できない文字列の場合は false を返す．
	 */
	public static boolean checkDouble(String value) {
		try {
			new Double(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected String getDefaultMessageKey() {
		return DOUBLE_FIELD_VALIDATOR_KEY;
	}

}
