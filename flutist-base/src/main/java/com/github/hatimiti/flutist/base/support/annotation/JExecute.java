package com.github.hatimiti.flutist.base.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.struts.enums.SaveType;

/**
 * Actionの実行メソッドを指定するためのアノテーションです。
 *
 * @author hatimiti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface JExecute {

	/**
	 * バリデータを呼び出すかどうかです。
	 *
	 */
	@Deprecated
	boolean validator() default false;

	/**
	 * 検証メソッドです。
	 *
	 */
	String v() default "";

	/**
	 * 検証エラー時の遷移先です。
	 *
	 */
	String i() default "";

	/**
	 * エラーメッセージの保存場所です。
	 *
	 */
	SaveType saveErrors() default SaveType.REQUEST;

	/**
	 * URLパターンです。
	 *
	 */
	String urlPattern() default "";

	/**
	 * ロールです。複数指定する場合はカンマ(,)で区切ります。
	 *
	 */
	String roles() default "";

	/**
	 * trueの場合、バリデータや検証メソッドで検証エラーがあるとそこで検証がとまります。
	 * falseの場合、検証エラーがあっても後続の検証を続行します。 どちらの場合も検証エラーがあると実行メソッドは呼び出されません。
	 *
	 */
	boolean stopOnValidationError() default true;

	/**
	 * trueの場合、実行メソッドが正常終了したときにセッションからアクションフォームを削除します。
	 *
	 */
	boolean removeActionForm() default false;

	/**
	 * リセットメソッド名です。 デフォルトはresetです。
	 *
	 */
	String reset() default "reset";

	/**
	 * 正常終了時に遷移先にリダイレクトするかどうかです。
	 *
	 */
	boolean r() default false;
}
