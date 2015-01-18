package com.github.hatimiti.flutist.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.github.hatimiti.flutist.base.util._Container;

/**
 * アクセス時間を設定＋リセットするFilter
 *
 * @author hatimiti
 */
public class AccessDateFilter implements Filter {

	/**
	 * フィルタ初期化処理
	 */
	@Override
	public void init(FilterConfig config) {
	}

	/**
	 * Actionのリクエスト時間を取得する
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		_Container.setAccessDate();
		try {
			chain.doFilter(request, response);
		} finally {
			_Container.resetAccessDate();
		}
	}

	/**
	 * フィルタシャットダウン処理
	 */
	@Override
	public void destroy() {
	}

}
