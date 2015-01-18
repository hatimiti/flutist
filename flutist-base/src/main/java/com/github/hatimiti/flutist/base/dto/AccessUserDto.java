package com.github.hatimiti.flutist.base.dto;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.github.hatimiti.flutist.common.util._Num;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.util._Str;

/**
 * ログインユーザに関する情報を保持する。
 * @author hatimiti
 *
 */
@Component(instance = InstanceType.SESSION)
public class AccessUserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** ユーザID */
	protected String id = "NONE";

	/** ユーザ名(姓) */
	protected String nameSei;

	/** ユーザ名(名) */
	protected String nameMei;

	/** ログイン済であれば true */
	protected boolean isLogged;

	/** 権限ロールID */
	protected Integer authroleId;

	/** 言語コード */
	protected String langCd;

	public void invalidate() {
		_Obj.copy(new AccessUserDto(), this);
	}

	/*
	 * getter, setter
	 */

	public String getId() {
		return _Str.toEmptyIfEmpty(String.valueOf(this.id));
	}

	public Long getIdL() {
		return _Num.toL_Null(getId());
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return getNameSei() + "　" + getNameMei();
	}

	public String getNameSei() {
		return this.nameSei;
	}

	public void setNameSei(String name) {
		this.nameSei = name;
	}

	public String getNameMei() {
		return nameMei;
	}

	public void setNameMei(String nameMei) {
		this.nameMei = nameMei;
	}

	public boolean isLogged() {
		return this.isLogged;
	}

	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}

	public void setAuthroleId(Integer authroleId) {
		this.authroleId = authroleId;
	}

	public String getLangCd() {
		return this.langCd;
	}

	public void setLangCd(String langCd) {
		this.langCd = langCd;
	}

}
