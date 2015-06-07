package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.validation.Vval;

/**
 * 最大バイト数チェックを行うバリデータクラス．
 * @author hatimiti
 */
public class MaxBytesFieldValidator extends BaseFieldValidator {

	/**
	 * 最大バイト数チェックバリデータのキー文字列定数．
	 */
	public static final String MAX_BYTES_FIELD_VALIDATOR_KEY =
		"valid.max.bytes";

	/**
	 * 最大バイト数
	 */
	protected int max;

	public MaxBytesFieldValidator(AppMessagesContainer container) {
		super(container);
	}

	protected boolean checkSpecifically(Vval value) {
		return checkMaxBytes(value.getValues()[0], this.max);
	}

	/**
	 * 最大バイト数チェック．<br>
	 * 指定された文字列のバイト数が最大バイト数以下であるかチェックする．
	 * @param value チェック対象文字列
	 * @param max 最大バイト数
	 * @return 最大バイト数以下であれば true そうでなければ false を返す．
	 */
	public static boolean checkMaxBytes(String value, int max) {
		byte[] bytes = value.getBytes();
		return bytes.length <= max;
	}

	/**
	 * 最大バイト数を設定する．
	 * @param max 最大バイト数
	 */
	public MaxBytesFieldValidator setMax(int max) {
		this.max = max;
		return this;
	}

	@Override
	protected String getDefaultMessageKey() {
		return MAX_BYTES_FIELD_VALIDATOR_KEY;
	}

}
