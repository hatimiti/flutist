package com.github.hatimiti.flutist.base.interceptor;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.slf4j.Logger;

import com.github.hatimiti.flutist.base.exception.FlutistRuntimeException;
import com.github.hatimiti.flutist.base.interceptor.supports.DoTrim;
import com.github.hatimiti.flutist.base.support.annotation.Condition;
import com.github.hatimiti.flutist.base.util._Container;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.util._Str;

/**
 * 各種加工用アノテーションを付加されたフィールドに処理を施す．
 * 処理の対象となるクラスは、"@ActionForm"を付加された
 * アクションのフィールドのみである．
 * 　"@Trim"：空白トリム,
 *
 * @author m-kakimi
 */
public class FieldConvertInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ログ出力用オブジェクト取得
	 */
	protected Logger logger = _Obj.getLogger();

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		Class<?> actionClass = getTargetClass(invocation);
		Object action = invocation.getThis();

		Field[] actionFields = actionClass.getDeclaredFields();
		Arrays.stream(actionFields)
			.filter(f -> _Container.isViewComponent(f))
			.forEach(actionField -> {
				actionField.setAccessible(true);
				try {
					process(actionField.get(action));
				} catch (Exception e) {
					throw new FlutistRuntimeException(e);
				}
			});

		return invocation.proceed();
	}

	/**
	 * フィールドに対する加工処理
	 */
	private void process(Object form) {
		Class<?> cls = form.getClass();
		Field[] fields = cls.getDeclaredFields();
		Arrays.stream(fields)
			.forEach(f -> {
				f.setAccessible(true);
				// @Trim に対する処理
				convertTrim(form, f);
				// その他処理
				// ---
			});
	}

	/**
	 * 空白トリム処理
	 * @param obj 処理対象フィールドを保持しているクラス
	 * @param field 処理対象フィールド
	 */
	private void convertTrim(Object form, Field field) {
		
		Condition condition = field.getAnnotation(Condition.class);
		if (condition == null) {
			return;
		}
		
		DoTrim trim = condition.doTrim();
		if (trim == null || !field.getType().equals(String.class)) {
			return;
		}

		try {
			switch (trim.value()) {
			case BOTH:
				field.set(form, _Str.trimBoth((String) field.get(form)));
				break;
			case LEFT:
				field.set(form, _Str.trimLeft((String) field.get(form)));
				break;
			case RIGHT:
				field.set(form, _Str.trimRight((String) field.get(form)));
				break;
			case NONE:
				break;
			}
		} catch (IllegalAccessException e) {
			throw new FlutistRuntimeException(e);
		}
	}

}
