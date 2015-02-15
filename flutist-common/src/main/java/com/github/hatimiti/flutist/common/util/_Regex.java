package com.github.hatimiti.flutist.common.util;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 正規表現ユーティリティクラス
 * @author m-kakimi
 */
public final class _Regex {

	/**
	 * private コンストラクタ
	 */
	private _Regex() {}

	public static String[] ESCAPE_CHARS = {
		"\\", "*", "+", ".", "?", "{", "}", "(", ")", "[", "]", "^", "$", "-", "|", "/"
	};
	
	public static Pattern ESCAPE_PATTERN = Pattern.compile("(["
			+ Stream.of(ESCAPE_CHARS)
				.map(it -> "\\" + it)
				.collect(Collectors.joining())
		+ "])");
	
	
	public static String escape(String value) {
		return ESCAPE_PATTERN.matcher(value).replaceAll("\\\\$1");
	}
}
