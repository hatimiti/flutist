package com.github.hatimiti.flutist.base.action;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ModifierUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.action.S2RequestProcessor;
import org.seasar.struts.config.S2ActionMapping;

import com.github.hatimiti.flutist.base.support.annotation.Condition;
import com.github.hatimiti.flutist.common.util._Ref;

public class JRequestProcessor extends S2RequestProcessor {

    private static final char NESTED_DELIM = '.';

    private static final char INDEXED_DELIM = '[';

//    private static final char INDEXED_DELIM2 = ']';

    private static final char MAPPED_DELIM = '(';

    private static final char MAPPED_DELIM2 = ')';

	@Override
	protected Action processActionCreate(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping)
					throws IOException {

		Action action = null;
		try {
			action = new JActionWrapper(((S2ActionMapping) mapping));
		} catch (Exception e) {
			log.error(getInternal().getMessage("actionCreate",
					mapping.getPath()), e);
			response
			.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					getInternal().getMessage("actionCreate",
							mapping.getPath()));
			return null;
		}
		action.setServlet(this.servlet);
		return action;
	}

	@Override
	protected void setProperty(Object bean, String name, Object value) {
		if (bean == null) {
			return;
		}
		int nestedIndex = name.indexOf(NESTED_DELIM);
		int indexedIndex = name.indexOf(INDEXED_DELIM);
		int mappedIndex = name.indexOf(MAPPED_DELIM);

		// hatimiti 追加ここから
		// @Condition のついていないフィールドにはリクエストをセットしない
		if (0 < nestedIndex) {
			Field topField = _Ref.getFieldByName(bean, name.substring(0, nestedIndex));
			if (topField != null && topField.getAnnotation(Condition.class) == null) {
				return;
			}
		}
		// hatimiti 追加ここまで

		if (nestedIndex < 0 && indexedIndex < 0 && mappedIndex < 0) {
			setSimpleProperty(bean, name, value);
		} else {
			int minIndex = minIndex(minIndex(nestedIndex, indexedIndex),
					mappedIndex);
			if (minIndex == nestedIndex) {
				setProperty(getSimpleProperty(bean, name.substring(0,
						nestedIndex)), name.substring(nestedIndex + 1), value);
			} else if (minIndex == indexedIndex) {
				IndexParsedResult result = parseIndex(name
						.substring(indexedIndex + 1));
				if (StringUtil.isEmpty(result.name)) {
					setIndexedProperty(bean, name.substring(0, indexedIndex),
							result.indexes, value);
				} else {

					Object _bean = getIndexedProperty(bean, name.substring(0,
							indexedIndex), result.indexes);
					setProperty(_bean, result.name, value);
				}
			} else {
				int endIndex = name.indexOf(MAPPED_DELIM2, mappedIndex);
				setProperty(bean, name.substring(0, mappedIndex) + "."
						+ name.substring(mappedIndex + 1, endIndex)
						+ name.substring(endIndex + 1), value);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void setSimpleProperty(Object bean, String name, Object value) {
		if (bean instanceof Map) {
			setMapProperty((Map<?, ?>) bean, name, value);
			return;
		}
		BeanDesc beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
		if (!beanDesc.hasPropertyDesc(name)) {
			return;
		}
		PropertyDesc pd = beanDesc.getPropertyDesc(name);

		// hatimiti 追加ここから
		boolean isConditionField = pd.getField().getAnnotation(Condition.class) != null;
		Field writable = _Ref.getFieldByName(pd, "writable");
		try {
			writable.setBoolean(pd, isConditionField || pd.isWritable());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		// hatimiti 追加ここまで

		if (!pd.isWritable()) {
			return;
		}
		if (pd.getPropertyType().isArray()) {
			pd.setValue(bean, value);
		} else if (List.class.isAssignableFrom(pd.getPropertyType())) {
			List<String> list = ModifierUtil.isAbstract(pd.getPropertyType()) ? new ArrayList<String>()
					: (List<String>) ClassUtil
					.newInstance(pd.getPropertyType());
			list.addAll(Arrays.asList((String[]) value));
			pd.setValue(bean, list);
		} else if (value == null) {
			pd.setValue(bean, null);
		} else if (value instanceof String[]) {
			String[] values = (String[]) value;
			pd.setValue(bean, values.length > 0 ? values[0] : null);
		} else {
			pd.setValue(bean, value);
		}
	}

}
