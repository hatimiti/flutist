package com.github.hatimiti.flutist.base.interceptor.supports;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 空白トリムに対するアノテーション．
 * @author hatimiti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DoTrim {

	/** 空白トリムタイプ */
	TrimType value() default TrimType.BOTH;

	/** 全角スペースもトリムするかどうか */
	boolean trimFullSpace() default true;
}
