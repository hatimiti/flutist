package com.github.hatimiti.flutist.common.util;



/**
 * 数値に対する操作を行うユーティリティクラス．
 * @author hatimiti
 */
public final class _Num {

	/*
	 * private コンストラクタ
	 */
	private _Num() { }

	public static Long toL_Null(String val) {
		try {
			return Long.valueOf(val);
		} catch (Throwable t) {
			return null;
		}
	}

	public static Long toL_Zero(String val) {
		try {
			return Long.valueOf(val);
		} catch (Throwable t) {
			return 0L;
		}
	}

	/**
	 * 渡された文字列を整数型へ変換する．
	 * 変換できない場合は null を返す．
	 */
	public static Integer toI_Null(
			final String value) {
		try {
			return Integer.parseInt(value);
		} catch (Throwable t) {
			return null;
		}
	}

	/**
	 * 渡された文字列を整数型へ変換する．
	 * 変換できない場合は default 値を返す．
	 */
	public static int toI(
			final String value,
			final int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (Throwable t) {
			return defaultValue;
		}
	}

	/**
	 * 渡された値が null かどうかチェックし、null の場合は
	 *  defaultValue に変換して返す．
	 *  @param value 変換対象の値
	 *  @param defaultValue null 時の値
	 *  @return value が null の場合は defaultValue に変換し返す．
	 *  	null でない場合はそのままの値を返す．
	 */
	public static Integer toDefaultValueIfNull(
			final Integer value,
			final int defaultValue) {
		return value == null ? defaultValue : value;
	}

}
