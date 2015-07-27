package com.github.hatimiti.flutist.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP プロトコルのためのユーティリティ
 * @author hatimiti
 */
public final class _Http {

	/**
	 * private コンストラクタ
	 */
	private _Http() { }

	/**
	 * Httpレスポンスにダウンロードに必要な値をセットし、
	 * Writerオブジェクトを取得する．
	 * @param res Httpサーブレットレスポンス
	 * @param ce 文字コード 列挙型
	 * @throws IOException 入出力例外
	 */
	public static Writer getWriterForDownload(
			final HttpServletResponse res,
			final CharacterEncoding charEnc,
			final MIMEType mime,
			final String fileName) throws IOException {

		setForDownload(res, charEnc, mime, fileName);
		return res.getWriter();
	}

	public static OutputStream getOutputStreamForDownload(
			final HttpServletResponse res,
			final CharacterEncoding charEnc,
			final MIMEType mime,
			final String fileName) throws IOException {
		setForDownload(res, charEnc, mime, fileName);
		return res.getOutputStream();
	}

	private static void setForDownload(
			final HttpServletResponse res,
			final CharacterEncoding charEnc,
			final MIMEType mime,
			final String fileName) {
		res.setCharacterEncoding(charEnc.toString());
		res.setContentType(mime.getValue() + ";");
		res.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		res.setHeader("Expires", "0");
        res.setHeader("Cache-Control", "must-revalidate, post-check=0,pre-check=0");
        res.setHeader("Pragma", "private");
	}

	public static void write(final OutputStream os, final File file) throws IOException {
		try (
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream out = new BufferedOutputStream(os)) {

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.flush();
		}
	}

	public static void write(final Writer writer, final File file) throws IOException {
		try (
				BufferedWriter bw = new BufferedWriter(writer);
				BufferedReader br = new BufferedReader(new FileReader(file))) {

			while (br.ready()) {
				bw.write(br.readLine());
				bw.write(_Str.LINE_SEPARATOR);
			}
		}
	}

	public static String getRedirectPath(HttpServletRequest request, String action) {
		String requestUrl = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		String redirectPath = requestUrl.replaceAll(contextPath + ".*", contextPath + action);
		return redirectPath;
	}

	/**
	 * リクエストヘッダから User-Agent を取得する。
	 * @param request httpリクエスト
	 * @return ユーザーエージェント
	 */
	public static String getUserAgent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}

	/**
	 * リクエストヘッダから IPアドレス を取得する。
	 * @param request httpリクエスト
	 * @return IPアドレス
	 */
	public static String getRemoteAddress(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

}
