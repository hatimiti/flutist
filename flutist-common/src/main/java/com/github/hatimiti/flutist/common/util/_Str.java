package com.github.hatimiti.flutist.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import org.seasar.util.lang.StringUtil;

/**
 * 文字列に対する操作を行うユーティリティクラス．
 * @author hatimiti
 */
public final class _Str {

	/**
	 * 空文字を表す定数(="")
	 */
	public static final String EMPTY = "";

	/**
	 * HTTPエスケープ文字(&)を表す文字(="&amp;")
	 */
	public static final String HTTP_AMP = "&amp;";

	/**
	 * 改行文字
	 */
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * 暗号化アルゴリズム SHA-1 の文字列定数("SHA-1")
	 */
	public static final String ENC_SHA1 = "SHA-1";

	/**
	 * 暗号化アルゴリズム MD5 の文字列定数("MD5")
	 */
	public static final String ENC_MD5 = "MD5";

	/**
	 * private コンストラクタ
	 */
	private _Str() { }

	/**
	 * 文字列配列に指定された値が含まれているかを判定する．
	 * @param value 指定値
	 * @param ary 文字列配列
	 * @return ary の中に value が含まれている場合は true,
	 * 含まれていない場合は false を返す．
	 */
	public static boolean contains(String value, String... ary) {

		if (ary == null || value == null) {
			throw new IllegalArgumentException(
				"You must appoint NOT NULL arguments.");
		}

		for (String str : ary) {
			if (str.equals(value)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 文字列を含んでいるかどうか返します。
	 * @param value 文字列
	 * @param searchString 比較する対象となる文字列
	 * @return 文字列を含んでいるかどうか
	 */
	public static boolean contains(String value, String searchString) {
		return StringUtil.contains(value, searchString);
	}

	/**
	 * スタックトレースを文字列として取得します。
	 * @param e 例外およびエラー
	 * @return スタックトレース文字列
	 */
	public static String getStackTraceString(Throwable e) {
		if (e == null) {
			return null;
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		String ret = sw.getBuffer().toString();
		pw.close();
		return ret;
	}

	/**
	 * 整数文字列かどうかを判断する．
	 * 整数文字列の場合は true を返す．
	 */
	public static boolean isFullSizeNumString(String value) {
		Pattern pattern =
			Pattern.compile("^[-]?[1-9|１－９]?[0-9|０-９]+$");
		return pattern.matcher(value).matches();
	}

	/**
	 * 全角整数文字列を半角整数文字列へ変換する．
	 * 与えられた文字列が全角整数文字列でない場合は、
	 * 元の文字列を返す．
	 * @param value 変換対象文字列
	 * @return 半角数字文字列
	 */
	public static String convertToHalfSizeNumString(String value) {

		if (!isFullSizeNumString(value)) {
			return value;
		}

		StringBuffer result = new StringBuffer();

		int count = 0;

		char ch = value.charAt(0);
		if (ch == '-') {
			result.append(ch);
			count++;
		}

		for (int i = count; i < value.length(); i++) {
			ch = value.charAt(i);

			if ('０' <= ch && ch <= '９') {
				ch = (char) (ch - '０' + '0');
			}

			result.append(ch);
		}

		return result.toString();
	}

	/**
	 * SHA-1暗号で、与えられた値を暗号化する．
	 * 暗号化した文字列は16進数で表現されている．
	 * 与えられた値が null または 空文字 の場合は、空文字を返す．
	 * @param value 暗号対象文字列
	 * @return value を暗号化した16進数文字列
	 */
	public static String encryptBySHA1(String value)
			throws NoSuchAlgorithmException {
		return encrypt(ENC_SHA1, value);
	}

	/**
	 * MD5暗号で、与えられた値を暗号化する．
	 * 暗号化した文字列は16進数で表現されている．
	 * 与えられた値が null または 空文字 の場合は、空文字を返す．
	 * @param value 暗号対象文字列
	 * @return value を暗号化した16進数文字列
	 * @throws NoSuchAlgorithmException アルゴリズム存在例外
	 */
	public static String encryptByMD5(String value) {
		try {
			return encrypt(ENC_MD5, value);
		} catch (NoSuchAlgorithmException e) {
			return EMPTY;
		}
	}

	/**
	 * 指定されたアルゴリズムで、与えられた値を暗号化する．
	 * 暗号化した文字列は16進数で表現されている．
	 * 与えられた値が null または 空文字 の場合は、空文字を返す．
	 * @param algorithm 暗号アルゴリズム
	 * @param value 暗号対象文字列
	 * @return value を暗号化した16進数文字列
	 * @throws NoSuchAlgorithmException アルゴリズム存在例外
	 */
	public static String encrypt(
			String algorithm,
			String value) throws NoSuchAlgorithmException {

		if (isEmpty(value)) {
			return EMPTY;
		}

		MessageDigest md5 = MessageDigest.getInstance(algorithm);
		md5.reset();
		md5.update(value.getBytes());

		byte[] digest = md5.digest();

		StringBuilder result = new StringBuilder();

		for (byte b : digest) {
			result.append(Integer.toHexString(0xFF & b));
		}

		return result.toString();
	}

	/**
	 * 指定した、0 からの整数の連番である文字列配列を、
	 * 指定したサイズの長さまで拡張した真偽値配列で返す．<br>
	 * 例：配列 = {0, 1, 4, 7, 9}, サイズ = 10<br>
	 * 　　結果 &gt; t, t, f, f, t, f, f, t, f, t<br>
	 * 使用例としては、Webアプリケーションにおいて、
	 * 複数のチェックボックスからサブミットされた値の配列を、
	 * 指定されたサイズに拡張するのに使用する．<br>
	 * &lt;input type="checkbox"&gt; の value 属性には 0 からの連番を
	 * 指定しておく必要がある。<br>
	 * value に数値以外の値が保持されている場合は、
	 * そのインデックスの値は false となる。
	 * @param values
	 *  0 からの連番(抜け番有)である文字列配列<br>
	 * @param size 拡張するサイズ
	 * @return
	 *  指定したサイズに拡張された真偽値配列を返す。<br>
	 * 値の存在するインデックスには true ,
	 * 値の存在しないインデックスには false を格納している。<br>
	 * values が null の場合は、指定されたサイズの false 配列を返す。
	 */
	public static boolean[] getExtendBoolArray(
			String[] values, int size) {

		final boolean[] results = new boolean[size];

		if (values == null) {
			for (int i = 0; i < size; i++) {
				results[i] = false;
			}

			return results;
		}

		int idx = 0;

		for (int i = 0; i < size; i++) {
			try {
				if (values.length <= idx) {
					results[i] = false;
				} else {
					int value = Integer.parseInt(values[idx]);
					if (value == i) {
						results[i] = true;
						idx++;
					} else {
						results[i] = false;
					}
				}
			} catch (NumberFormatException e) {
				results[i] = false;
			}
		}

		return results;
	}

	/**
	 * 与えられた文字列の先頭を大文字に変換する．<br>
	 * @param value 変換対象文字列
	 * @return 先頭が大文字に変換された文字列
	 */
	public static String toUpperCaseFirstChar(final String value) {
		if (_Str.isEmpty(value)) {
			return value;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(Character.toUpperCase(value.charAt(0)));

		for (int i = 1; i < value.length(); i++) {
			sb.append(value.charAt(i));
		}

		return sb.toString();
	}

	/**
	 * 与えられた文字列の先頭を小文字に変換する．<br>
	 * @param value 変換対象文字列
	 * @return 先頭が小文字に変換された文字列
	 */
	public static String toLowerCaseFirstChar(final String value) {
		if (_Str.isEmpty(value)) {
			return value;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(Character.toLowerCase(value.charAt(0)));

		for (int i = 1; i < value.length(); i++) {
			sb.append(value.charAt(i));
		}

		return sb.toString();
	}

	/**
	 * 与えられた Number が空の場合は 空文字 に変換する．
	 */
	public static String toEmptyIfEmpty(final Number value) {
		return _Obj.isEmpty(value) ? "" : String.valueOf(value);
	}

	/**
	 * 与えられた Number が空の場合は null に変換する．
	 */
	public static String toNullIfEmpty(final Number value) {
		return _Obj.isEmpty(value) ? null : String.valueOf(value);
	}

	/**
	 * 与えられた Number が空の場合は 0 に変換する．
	 */
	public static String toZeroIfEmpty(final Number value) {
		return _Obj.isEmpty(value) ? "0" : String.valueOf(value);
	}

	/**
	 * 与えられた文字列が空の場合は null に変換する．
	 */
	public static String toNullIfEmpty(final String value) {
		return isEmpty(value) ? null : value;
	}

	/**
	 * 与えられた文字列が空の場合は 0 に変換する．
	 */
	public static String toZeroIfEmpty(final String value) {
		return isEmpty(value) ? "0" : value;
	}

	/**
	 * 与えられた文字列が空の場合は空文字に変換する．
	 */
	public static String toEmptyIfEmpty(final String value) {
		return isEmpty(value) ? EMPTY : value;
	}

	/**
	 * 指定された文字列配列を、指定された区切り文字列で連結した文字列を返す．
	 * 文字列A = abc, 文字列B = def, 区切文字 = , の場合、
	 * 返される値は「abc,def」となる．
	 * @param delimiter 区切り文字列
	 * @param strs 連結する文字列配列
	 * @return 区切り文字で連結された文字列
	 */
	public static String getLinkedString(String delimiter, String... strs) {

		String _delimiter = delimiter;

		if (isEmpty(_delimiter)) {
			_delimiter = EMPTY;
		}
		if (strs == null) {
			return EMPTY;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length - 1; i++) {
			sb.append(strs[i]).append(_delimiter);
		}

		sb.append(strs[strs.length - 1]);

		return sb.toString();
	}

	/**
	 * 与えられた文字列が null かどうかチェックし、
	 *  null の場合は空文字に変換し返す．
	 *  @param value チェック対象文字列
	 *  @param null の場合は空文字、そうでない場合はそのまま値を返す．
	 */
	public static String toEmpty(String value) {
		if (value == null) {
			return EMPTY;
		}
		return value;
	}

	/**
	 * 与えられたオブジェクトが null かどうかチェックし、
	 *  null の場合は空文字に変換し返す．
	 * @param obj 変換対象文字列
	 * @return null の場合は空文字、そうでない場合は Object.toString() 値を返す．
	 */
	public static String toEmpty(Object obj) {
		if (obj == null) {
			return EMPTY;
		}
		return obj.toString();
	}

	/**
	 * 与えられた文字列が null かどうかチェックし、
	 *  null の場合はデフォルト文字列に変換し返す．
	 *  @param value チェック対象文字列
	 *  @param def デフォルト文字列
	 *  @param null の場合はデフォルト文字列、そうでない場合はそのまま値を返す．
	 */
	public static String toDefault(final String value, final String def) {
		if (value == null) {
			return def;
		}
		return value;
	}

	/**
	 * SQLのLIKEを使用する際、入力値として
	 * 「%」「％」「_」「＿」が使用できるように
	 * 指定文字列でエスケープを行う．
	 * @param param エスケープさせる文字列
	 * @param escape エスケープ文字を指定
	 * @return エスケープした文字列<br>
	 * param が null の場合は null を返す
	 */
	public static String escapeForSqlLike(String value, char escape) {

		if (value == null) {
			return null;
		}

		String result = value;

		result = result.replace("%", escape + "%");
		result = result.replace("％", escape + "％");
		result = result.replace("_", escape + "_");
		result = result.replace("＿", escape + "＿");

		return result;
	}

	/**
	 * SQLのLIKEを使用する際、入力値として
	 * 「%」「％」「_」「＿」が使用できるように
	 * 「\」を付加させる．
	 * @param param エスケープさせる文字列
	 * @return エスケープした文字列<br>
	 * param が null の場合は null を返す
	 *
	 */
	public static String escapeForSqlLike(final String value) {

		if (value == null) {
			return null;
		}

		return escapeForSqlLike(value, '\\');
	}

	/**
	 * SQLのLIKEを使用する際、入力値として
	 * 「%」「％」「_」「＿」が使用できるように
	 * 「\」を付加させる．<br>
	 * 同時にUTF-8によるエンコードも行う．
	 * @param param エスケープさせる文字列
	 * @return エスケープした文字列<br>
	 * param が null の場合は null を返す
	 *
	 */
	public static String escapeForSqlLikeWithEncode(final String value) {

		if (value == null) {
			return null;
		}

		return escapeForSqlLike(encode(value), '\\');
	}

	/**
	 * 指定した文字列の左側スペースをトリミングする．
	 * 全角/半角スペースの両方をトリムする．
	 * @param value トリム対象文字列
	 * @return トリム後文字列
	 */
	public static String trimLeft(final String value) {
		if (isEmpty(value)) {
			return value;
		}
		return value.replaceAll("^[　| ]+", EMPTY);
	}

	/**
	 * 指定した文字列の右側スペースをトリミングする．
	 * 全角/半角スペースの両方をトリムする．
	 * @param value トリム対象文字列
	 * @return トリム後文字列
	 */
	public static String trimRight(final String value) {
		if (isEmpty(value)) {
			return value;
		}
		return value.replaceAll("[　| ]+$", EMPTY);
	}

	/**
	 * 指定した文字列の左右両側スペースをトリミングする．
	 * 全角/半角スペースの両方をトリムする．
	 * @param value トリム対象文字列
	 * @return トリム後文字列
	 */
	public static String trimBoth(final String value) {
		return trimLeft(trimRight(value));
	}

	/**
	 * 指定した文字配列の左右両側スペースをトリミングする．
	 * 全角/半角スペースの両方をトリムする．
	 * @param values トリム対象文字配列
	 * @return トリム後文字配列
	 */
	public static String[] trimBoth(final String[] values) {
		return trimLeft(trimRight(values));
	}

	/**
	 * 指定した文字配列の左側スペースをトリミングする．
	 * 全角/半角スペースの両方をトリムする．
	 * @param value トリム対象文字配列
	 * @return トリム後文字列
	 */
	public static String[] trimLeft(final String[] values) {
		if (isEmpty(values)) {
			return values;
		}

		String[] result = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			result[i] = values[i].replaceAll("^[　| ]+", EMPTY);
		}
		return result;
	}

	/**
	 * 指定した文字列の右側スペースをトリミングする．
	 * 全角/半角スペースの両方をトリムする．
	 * @param value トリム対象文字列
	 * @return トリム後文字列
	 */
	public static String[] trimRight(final String[] values) {
		if (isEmpty(values)) {
			return values;
		}

		String[] result = new String[values.length];

		for (int i = 0; i < values.length; i++) {
			result[i] = values[i].replaceAll("[　| ]+$", EMPTY);
		}

		return result;
	}

	/**
	 * 指定した文字列を UTF-8 で変換する．
	 * @param value 変換対象の文字列
	 * @return 変換された文字列
	 */
	public static String encode(String value) {

		if (_Str.isEmpty(value)) {
			return value;
		}

		try {
			return new String(value.getBytes("8859_1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	/**
	 * 与えられた文字列をUTF-8でURLエンコードを行う．
	 * @param value エンコード対象の文字列
	 * @return エンコード後の文字列
	 */
	public static String encodeUrl(final String value) {

		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return value;
		}
	}

	/**
	 * 与えられた文字列をUTF-8でURLデコードを行う．
	 * @param value デコード対象の文字列
	 * @return デコード後の文字列
	 */
	public static String decodeUrl(final String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	/**
	 * 与えられた文字列の左側を 0 埋めして返す．<br>
	 * @param value 0 埋め対象文字列
	 * @param length 0 埋め後の桁数を指定する．
	 * @return 左側を 0 埋めされた文字列を返す．
	 */
	public static String paddingZeroLeft(
			final String value,
			final int length) {
		final StringBuilder sb = new StringBuilder(value);
		for (int i = value.length(); i < length; i++) {
			sb.insert(0, "0");
		}
		return sb.toString();
	}


	/**
	 * 与えられた文字列をカンマ(,)の3桁区切りにして返す．<br>
	 * 与えられた文字列が数値形式でなければそのまま返す．
	 * @param value 数値形式の文字列
	 * @return value をカンマで3桁区切りした文字列
	 */
	public static String partitionWithComma(
			final String value) {
		try {
			new Integer(value);
		} catch (NumberFormatException e) {
			return value;
		}
		return value.replaceAll("(?<=[0-9])(?=([0-9]{3})+$)", ",");
	}

	public static String getIntegerOf(BigDecimal value) {
		if (value == null) {
			return "";
		}
		String strval = toEmptyIfEmpty(value);
		int idx = strval.indexOf('.');
		return idx < 0 ? strval : strval.substring(0, idx);
	}

	public static String getDecimalOf(BigDecimal value) {
		if (value == null) {
			return "";
		}
		String strval = toEmptyIfEmpty(value);
		int idx = strval.indexOf('.');
		return idx < 0 ? "0" : strval.substring(idx + 1, strval.length());
	}

	/**
	 *  null または空文字かどうかを判断する．
	 * @param 判定文字列
	 * @return null または空文字の場合は true を返す．<br>
	 */
	private static boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/**
	 * 渡された文字列配列が null または、空配列かどうかを判断する．
	 * @param 判定文字列配列
	 * @return null または空配列の場合は true を返す．<br>
	 */
	private static boolean isEmpty(String[] values) {
		return values == null || values.length <= 0;
	}

	/**
	 * テスト用 main メソッド
	 */
	public static void main(String[] args) throws Exception {

		System.out.println(escapeForSqlLike("％%＿_あいうえお", '\\'));

	}

}
