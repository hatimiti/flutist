package com.github.hatimiti.flutist.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.util.lang.MethodUtil;

/**
 * リフレクションに関する操作を行うユーティリティクラス．
 * @author hatimiti
 */
public final class _Ref {

	/*
	 * private コンストラクタ
	 */
	private _Ref() { }

	public static Method getMethod(Class<?> targetClass, String methodName, Class<?>... parameterTypes) {
		try {
			return targetClass.getMethod(methodName, parameterTypes);
		} catch (Throwable t) {
			return null;
		}
	}

	/**
	 * 親クラスのフィールドも取得
	 */
	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fieldList = new ArrayList<Field>();

		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null) {
			// 再帰的に親クラスのフィールドを取得
			fieldList.addAll(getAllFields(superClazz));
		}
		fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));

		return fieldList;
	}

	/**
	 * annotationClass のついた 親クラスのフィールドも取得
	 */
	public static List<Field> getAllFields(Class<?> clazz, Class<? extends Annotation> annotaionClass) {
		List<Field> fieldList = new ArrayList<Field>();

		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null) {
			// 再帰的に親クラスのフィールドを取得
			fieldList.addAll(getAllFields(superClazz, annotaionClass));
		}

		for (Field f : clazz.getDeclaredFields()) {
			Object annotaion = f.getAnnotation(annotaionClass);
			if (annotaion != null) {
				fieldList.add(f);
			}
		}

		return fieldList;
	}

	/**
	 * 対象のクラスに指定したアノテーションが付加されているフィールドクラスを取得する．
	 * 複数フィールドが存在する場合は初めに見つけたフィールドクラスを返す．
	 * @param target 走査対象オブジェクト
	 * @param annotaionClass 検索対象アノテーション
	 * @return　検索対象アノテーションが付加されたフィールドが存在すれば、
	 * そのフィールドクラス、そうでなければ null を返す．
	 */
	public static Object getObject(
			Object target,
			Class<? extends Annotation> annotaionClass) {
		try {
			Field field = getField(target, annotaionClass);
			return field.get(target);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 対象のクラスに指定したアノテーションが付加されているフィールドを取得する．
	 * 複数フィールドが存在する場合は初めに見つけたフィールドを返す．
	 * @param target 走査対象オブジェクト
	 * @param annotaionClass 検索対象アノテーション
	 * @return　検索対象アノテーションが付加されたフィールドが存在すれば、
	 * そのフィールド、そうでなければ null を返す．
	 */
	public static Field getField(
			Object target,
			Class<? extends Annotation> annotaionClass) {

		Field[] fields = target.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Annotation annotaion
				= field.getAnnotation(annotaionClass);

			if (annotaion != null) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 対象のクラスの指定した名前のフィールドを取得する．
	 * @param target 走査対象オブジェクト
	 * @param fieldName 検索対象フィールド名
	 * @return　フィールドが存在すればそのフィールド、そうでなければ null を返す．
	 */
	public static Field getFieldByName(
			Object target,
			String fieldName) {
		if (target == null || _Obj.isEmpty(fieldName)) {
			return null;
		}
		Field[] fields = target.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (fieldName.equals(field.getName())) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 対象のクラスに指定したアノテーションが付加されているフィールドが
	 * 存在するかチェックする．
	 * @param target 走査対象オブジェクト
	 * @param annotaionClass 検索対象アノテーション
	 * @return 検索対象アノテーションが付加されたフィールドが存在すれば、
	 * 真、そうでなければ偽の値を返す．
	 */
	public static boolean hasAnnotationInFields(
			Object target,
			Class<? extends Annotation> annotaionClass) {

		Field[] fields = target.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Annotation annotaion
				= field.getAnnotation(annotaionClass);

			if (annotaion != null) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 渡されたオブジェクトのインスタンス変数が
	 * 設定されたマップオブジェクトを取得する．
	 */
	public static Map<String, Object> getFiGeldsMapOf(final Object obj) {

		Map<String, Object> result = new HashMap<String, Object>();

		if (obj == null) {
			return result;
		}

		for (Field field : obj.getClass().getFields()) {
			field.setAccessible(true);
			try {
				result.put(field.getName(), field.get(obj));
			} catch (IllegalArgumentException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		return result;
	}

	public static Object invoke(Method method, Object target, Object... args) {
		return MethodUtil.invoke(method, target, args);
	}

}
