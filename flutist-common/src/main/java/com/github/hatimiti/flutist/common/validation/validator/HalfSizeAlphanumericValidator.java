package com.github.hatimiti.flutist.common.validation.validator;

import java.util.regex.Pattern;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.validation.Vval;

/**
 * 半角英数文字チェックを行うバリデータクラス．
 * @author f-akamatsu
 */
public class HalfSizeAlphanumericValidator extends BaseFieldValidator {

	public static final String HALF_SIZE_Alphanumeric_VALIDATOR_KEY =
		"valid.half.alpha.size";

	public HalfSizeAlphanumericValidator(AppMessagesContainer container) {
		super(container);
	}

	@Override
	protected boolean checkSpecifically(Vval value) {
		return checkHalfSize(value.getValues()[0]);
	}

	public static boolean checkHalfSize(String value) {
		if (_Obj.isEmpty(value)) {
			return true;
		}
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
		return pattern.matcher(value).matches();
	}

	@Override
	protected String getDefaultMessageKey() {
		return HALF_SIZE_Alphanumeric_VALIDATOR_KEY;
	}
}
