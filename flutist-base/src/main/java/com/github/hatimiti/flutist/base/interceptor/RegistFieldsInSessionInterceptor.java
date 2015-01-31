package com.github.hatimiti.flutist.base.interceptor;

import java.lang.reflect.Field;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.slf4j.Logger;

import com.github.hatimiti.flutist.base.interceptor.supports.Session;
import com.github.hatimiti.flutist.base.support.annotation.Condition;
import com.github.hatimiti.flutist.base.util._Container;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.util._Ref;

/**
 * 対象 Action の @ActionForm が付加された Form のフィールドの内、
 * @Condition(session = true) が付加されたものをセッションに登録、またはセッションから取得して
 * セットする．
 *
 * @Condition の属性 prefRequest が true の場合は、リクエストパラメータの値が優先される．
 * prefRequest が true の場合でも、リクエストパラメータが送信されてきていない場合は、
 * Session から値を設定する．
 *
 * @author m-kakimi
 */
public class RegistFieldsInSessionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -4764627478894962478L;

	/**
	 * ログ出力用オブジェクト取得
	 */
	protected static final Logger logger = _Obj.getLogger();

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// セッションの値をフィールドにセット
		processSession(invocation, true);
		// アクションメソッド実行
		Object result = invocation.proceed();
		// フィールドの値をセッションにセット
		processSession(invocation, false);

		return result;
	}

	private void processSession(MethodInvocation invocation, boolean isSetSessionToBean) throws Exception {

		Class<?> actionClass = getTargetClass(invocation);
		Object action = invocation.getThis();

		Field[] actionFields = actionClass.getDeclaredFields();
		for (Field actionField : actionFields) {
			actionField.setAccessible(true);

			if (_Container.isViewComponent(actionField)) {
				Object bean = actionField.get(action);

				setup4Session(isSetSessionToBean, bean, "", false);
			}
		}
	}

	protected boolean setup4Session(boolean isSetSessionToBean, Object origBean, String path, boolean isSessionParent)
			throws IllegalAccessException {

		Class<?> beanClass = origBean.getClass();
		List<Field> beanFields = _Ref.getAllFields(beanClass);

		for (Field beanField : beanFields) {

			beanField.setAccessible(true);
			Condition condition = beanField.getAnnotation(Condition.class);
			Session session = beanField.getAnnotation(Session.class);

			Object bean = beanField.get(origBean);

			boolean isSessionField = (session != null)
					|| (condition != null && condition.session());

			if (!isSessionField && !isSessionParent) {
				continue;
			}

			if (bean instanceof FormType) {
				setup4Session(isSetSessionToBean, bean, path, isSessionField);
				continue;
			}

			String beanFieldName = beanField.getName();

			// クラス完全修飾名_フィールド名 をセッションのキーとする
			String sessionKey = path + "_" + origBean.getClass().getName() + "_" + beanFieldName;

			if (isSetSessionToBean) {
				// フィールド名取得

				if (condition != null && condition.prefRequest()
						|| isSessionField && session.prefRequest()) {

					// リクエスト値を優先する場合
					String param = getHttpServletRequest().getParameter(beanFieldName);

					if (param != null) {

						try {
							beanField.set(origBean, param);
						} catch (Exception e) {
							logger.error(
									"リクエスト値をセッション登録する場合は、フィールド("
									+ beanFieldName
									+ ") の型は " + String.class.getName()
									+ " 、もしくは " + FormType.class.getName()
									+ " である必要があります．", e);
							throw e;
						}

						continue;
					}
				}
				// セッションの値を取得
				Object sessionValue = _Container.getHttpSession().getAttribute(sessionKey);
				if (sessionValue != null && bean == null) {
					// セッションの値をセット
					beanField.set(origBean, sessionValue);
				}

			} else {
				// セッションにBeanの値をセット
				getHttpSession().setAttribute(sessionKey, beanField.get(origBean));
			}
		}

		return false;
	}

}
