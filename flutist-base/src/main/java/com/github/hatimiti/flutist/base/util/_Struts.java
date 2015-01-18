package com.github.hatimiti.flutist.base.util;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.util.RequestUtil;

/**
 * Strutsに関する操作を行うユーティリティクラス．
 * 
 * @author hatimiti
 */
public final class _Struts {

	public static ActionMessages getErrors() {
		return (ActionMessages) RequestUtil.getRequest().getAttribute(
				Globals.ERROR_KEY);
	}
	
	@SuppressWarnings("unchecked")
	public static Iterator<ActionMessage> getErrorsIterator(String property) {
		ActionMessages m = getErrors();
		if (m == null) {
			return emptyListIterator();
		}
		return property == null ? m.get() : m.get(property);
	}
	
	public static List<String> getErrorStrings(String property) {
		List<String> r = new ArrayList<String>();
		for (Iterator<ActionMessage> i = getErrorsIterator(property); i.hasNext();) {
			ActionMessage m = i.next();
			r.add(m.isResource() ? /* TODO */"" : m.getKey());
		}
		return r;
	}

}
