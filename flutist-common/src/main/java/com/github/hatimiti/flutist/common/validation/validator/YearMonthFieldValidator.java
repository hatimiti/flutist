package com.github.hatimiti.flutist.common.validation.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.validation.Vval;

/**
 * 日付(年月)形式チェックを行うバリデータクラス．
 * 例：2008/01, 2008/1
 * @author hatimiti
 */
public class YearMonthFieldValidator extends BaseFieldValidator {

	public static final String YM_FIELD_VALIDATOR_KEY = "valid.date.ym";

	/**
	 * 区切り文字列「/」「-」など
	 */
	protected String delimiter = "";

	public YearMonthFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	@Override
	protected boolean checkSpecifically(Vval value) {
		return checkYearMont(value.getValues()[0], this.delimiter);
	}

	/**
	 * 区切り文字列｢""｣に使用
	 * @return
	 */

	public static boolean checkYearMont(String value, String delimiter) {

		if (_Obj.isEmpty(value)) {
			return true;
		}

		// yyyy/MM の形式かを判断する．
		Pattern pattern =
			Pattern.compile(
					"^[0-9|０-９]{4}"
					+ delimiter
					+ "[0-9|０-９]{1,2}");

		if (!pattern.matcher(value).matches()) {
			return false;
		}

		// 妥当な日付かをチェックする．
		SimpleDateFormat fmt
			= new SimpleDateFormat(getFormat(delimiter));
		// 2008/13/01 のような日付は ParseException とする．
		fmt.setLenient(false);

		try {
			fmt.parse(value + delimiter + "01");
			return true;
		} catch (ParseException e) {
			return false;
		}

	}

	protected static String getFormat(final String dlmt) {
		return "yyyy" + dlmt + "MM" + dlmt + "dd";
	}

	@Override
	protected String getDefaultMessageKey() {
		return YM_FIELD_VALIDATOR_KEY;
	}

	public YearMonthFieldValidator delimiter(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}

	public String getDelimiter() {
		return this.delimiter;
	}

}
