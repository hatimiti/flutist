package com.github.hatimiti.flutist.base.util;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.servlet.S2ContainerServlet;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.ResponseUtil;
import org.seasar.struts.util.ServletContextUtil;

import com.github.hatimiti.flutist.base.dto.AccessUserDto;
import com.github.hatimiti.flutist.common.util._Date;
import com.github.hatimiti.flutist.common.util._Str;

/**
 * jsp/servlet コンテナを起動している時に使用可能なユーティリティクラス．
 * @author hatimiti
 *
 */
public final class _Container {

	/*
	 * private コンストラクタ
	 */
	private _Container() { }

	public static final String REDIRECT = ";redirect=true";

	public static final String WEB_INF_DIR_PATH = "/WEB-INF";

	/**
	 * ActionMessages エラーが存在するかどうかを判定する．
	 * @return
	 */
	public static boolean hasErrors() {
		return ActionMessagesUtil.hasErrors(getHttpServletRequest());
	}

	public static void saveErrors(final ActionMessages messages) {
		if (messages == null) {
			return;
		}
		ActionMessagesUtil.addErrors(getHttpServletRequest(), messages);
	}

	public static void saveErrorsInSession(final ActionMessages messages) {
		if (messages == null) {
			return;
		}
		ActionMessagesUtil.addErrors(getHttpSession(), messages);
	}

	public static void saveMessage(final ActionMessages messages) {
		if (messages == null) {
			return;
		}
		ActionMessagesUtil.addMessages(getHttpServletRequest(), messages);
	}

	public static void saveMessageInSession(final ActionMessages messages) {
		if (messages == null) {
			return;
		}
		ActionMessagesUtil.addMessages(getHttpSession(), messages);
	}

	/** アクセス時間保持(Web側で使用される日時) */
	private static final ThreadLocal<Date> ACCESS_DATE = new ThreadLocal<Date>() {
		@Override
		protected Date initialValue() {
			return new Date();
		};
	};

	/**
	 * アクセス時間を取得
	 */
	public static Date getAccessDate() {
		Date accessTime = ACCESS_DATE.get();
		if (accessTime == null) {
			return new Date();
		}
		return accessTime;
	}

	/**
	 * アクセス時間を取得(Timestamp)
	 */
	public static Timestamp getAccessTimestamp() {
		return new Timestamp(getAccessDate().getTime());
	}

	/**
	 * アクセス時間を yyyymmdd の形式で取得
	 */
	public static String getAccessYyyyMmDd() {
		try {
			return _Date.formatYyyyMmDd(getAccessDate());
		} catch (Exception e) {
			return _Str.EMPTY;
		}
	}

	/**
	 * アクセス時間を yyyymm の形式で取得
	 */
	public static String getAccessYyyyMm() {
		try {
			return _Date.formatYyyyMm(getAccessDate());
		} catch (Exception e) {
			return _Str.EMPTY;
		}
	}

	/**
	 * アクセス時間を YyyyMmDdHhMiSs の形式で取得
	 */
	public static String getAccessYyyyMmDdHhMiss() {
		try {
			return _Date.formatYyyyMmDdHhMiSs(getAccessDate());
		} catch (Exception e) {
			return _Str.EMPTY;
		}
	}

	/**
	 * アクセス時間を設定<br>
	 * filter が呼び出すため、実装者が呼び出す必要はない．
	 */
	public static void setAccessDate() {
		ACCESS_DATE.set(new Date());
	}

	/**
	 * アクセス時間を削除<br>
	 * filter が呼び出すため、実装者が呼び出す必要はない．
	 */
	public static void resetAccessDate() {
		ACCESS_DATE.remove();
	}

	/**
	 * アプリケーションルートパスを取得する．
	 * @return アプリケーションルートフォルダパス
	 */
//	public static final String getRootPath() {
//		return getServletContext().getRealPath("/");
//	}

	public static String getContextPath() {
		return getHttpServletRequest().getContextPath();
	}

	public static String getContextRootPath() {
		return ServletContextUtil.getServletContext().getRealPath("");
	}

	public static String getViewRootPath() {
		return ServletContextUtil.getViewPrefix();
	}

	public static HttpServletRequest getHttpServletRequest() {
//		(HttpServletRequest) S2ContainerServlet.getContainer().getExternalContext().getRequest()
		return RequestUtil.getRequest();
	}

	public static HttpServletResponse getHttpServletResponse() {
//		(HttpServletResponse) S2ContainerServlet.getContainer().getExternalContext().getResponse();
		return ResponseUtil.getResponse();
	}

	public static void addCookie(final Cookie cookie) {
		_Container.getHttpServletResponse().addCookie(cookie);
	}

	public static HttpSession getHttpSession() {
		return getHttpServletRequest().getSession();
	}

	public static boolean isViewComponent(final Field field) {
		ActionForm af = field.getAnnotation(ActionForm.class);
//		ViewHelper vf = field.getAnnotation(ViewHelper.class);
		return af != null;
//		return af != null || vf != null;
	}

	public static AccessUserDto getAccessUserDto() {
		return getComponent(AccessUserDto.class);
	}

	/**
	 * seasar 管理下のオブジェクトを取得する．
	 * @return 指定したオブジェクト
	 */
	@SuppressWarnings("unchecked")
	public static <D> D getComponent(Class<D> clazz) {
		S2Container container = S2ContainerServlet.getContainer();
		return (D) container.getComponent(clazz);
	}

	public static Object getComponent(String className) {
		S2Container container = S2ContainerServlet.getContainer();
		return container.getComponent(className);
	}

}
