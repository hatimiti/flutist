package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.validation.Vval;


/**
 * 整数チェックを行うバリデータクラス
 * @author hatimiti
 */
public class IntFieldValidator extends BaseFieldValidator {

	public static final String INT_FIELD_VALIDATOR_KEY =
		"valid.int";

	public IntFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	@Override
	protected boolean checkSpecifically(Vval value) {
		return checkInt(value.getValues()[0]);
	}

	/**
	 * 引数として渡された値が整数かどうかをチェックする．
	 * @param value チェック対象文字列
	 * @return
	 * 		整数に変換可能な文字列であれば true<br>
	 * 		整数に変換できない文字列の場合は false を返す．
	 */
	public static boolean checkInt(String value) {
		if (_Obj.isEmpty(value)) {
			return true;
		}
		try {
			new Integer(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected String getDefaultMessageKey() {
		return INT_FIELD_VALIDATOR_KEY;
	}

}
