package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.AppMessages;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.validation.Vval;

/**
 * 必須入力チェックを行うバリデータクラス．
 * @author hatimiti
 */
public class RequiredFieldValidator extends BaseFieldValidator {

	/**
	 * 必須チェックバリデータのキー文字列定数．
	 */
	public static final String REQUIRED_FIELD_VALIDATOR_KEY =
		"valid.required";

	public RequiredFieldValidator(AppMessages messages) {
		super(messages);
	}
	
	@Override
	protected boolean checkSpecifically(Vval value) {
		return checkRequired(value.getValues()[0]);
	}

	/**
	 * 必須チェック．<br>
	 * 指定された文字列が 空文字 または null かどうかをチェックする．
	 * @param value チェック対象文字列
	 * @return 空文字 または null の場合に false,
	 * 	そうでない場合は trOue を返す．
	 */
	public static boolean checkRequired(final String value) {
		return _Obj.isNotEmpty(value);
	}

	@Override
	protected String getDefaultMessageKey() {
		return REQUIRED_FIELD_VALIDATOR_KEY;
	}
}
