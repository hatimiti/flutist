package com.github.hatimiti.flutist.common.validation.validator;

import java.util.regex.Pattern;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.validation.Vval;


/**
 * 正規表現による値チェックを行うバリデータクラス
 * @author hatimiti
 */
public class RegexFieldValidator extends BaseFieldValidator {


	public static final String REGEX_FIELD_VALIDATOR_KEY =
		"valid.invalid";

	/**
	 * 正規表現
	 */
	protected String regex = ".*";

	public RegexFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	@Override
	protected boolean checkSpecifically(Vval value) {
		return checkRegex(value.getValues()[0], this.regex);
	}

	/**
	 * 引数として渡された値が整数かどうかをチェックする．
	 * @param value チェック対象文字列
	 * @return
	 * 		整数に変換可能な文字列であれば true<br>
	 * 		整数に変換できない文字列の場合は false を返す．
	 */
	public static boolean checkRegex(String value, String regex) {
		if (_Obj.isEmpty(value)) {
			return true;
		}
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(value).matches();
	}

	public RegexFieldValidator regex(String regex) {
		this.regex = regex;
		return this;
	}

	public String getRegex() {
		return this.regex;
	}

	@Override
	protected String getDefaultMessageKey() {
		return REGEX_FIELD_VALIDATOR_KEY;
	}

}
