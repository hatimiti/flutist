package com.github.hatimiti.flutist.base.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.util.ModifierUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.config.S2ActionMapping;
import org.seasar.struts.config.S2ExecuteConfig;
import org.seasar.struts.config.S2ValidationConfig;
import org.seasar.struts.customizer.ActionCustomizer;
import org.seasar.struts.exception.DuplicateExecuteMethodAndPropertyRuntimeException;
import org.seasar.struts.exception.ExecuteMethodNotFoundRuntimeException;
import org.seasar.struts.exception.IllegalExecuteMethodRuntimeException;
import org.seasar.struts.exception.IllegalValidateMethodRuntimeException;
import org.seasar.struts.exception.IllegalValidatorOfExecuteMethodRuntimeException;
import org.seasar.struts.exception.MultipleAllSelectedUrlPatternRuntimeException;
import org.seasar.struts.exception.UnmatchValidatorAndValidateRuntimeException;

import com.github.hatimiti.flutist.base.support.annotation.JExecute;

public class JActionCustomizer extends ActionCustomizer {

	/**
	 * メソッドの情報をセットアップします。
	 *
	 * @param actionMapping
	 *            アクションマッピング
	 * @param actionClass
	 *            アクションクラス
	 */
	@Override
	@SuppressWarnings("deprecation")
	protected void setupMethod(S2ActionMapping actionMapping,
			Class<?> actionClass) {
		S2ExecuteConfig allSelectedExecuteConfig = null;
		for (Class<?> clazz = actionClass; clazz != Object.class; clazz = clazz
				.getSuperclass()) {
			for (Method m : clazz.getDeclaredMethods()) {
				if (!ModifierUtil.isPublic(m)) {
					continue;
				}
				JExecute execute = m.getAnnotation(JExecute.class);
				if (execute == null) {
					continue;
				}
				if (actionMapping.getExecuteConfig(m.getName()) != null) {
					continue;
				}
				if (m.getParameterTypes().length > 0
						|| m.getReturnType() != String.class) {
					throw new IllegalExecuteMethodRuntimeException(actionClass,
							m.getName());
				}
				if (actionMapping.getActionFormBeanDesc().hasPropertyDesc(
						m.getName())) {
					throw new DuplicateExecuteMethodAndPropertyRuntimeException(
							actionClass, m.getName());
				}
				String input = !StringUtil.isEmpty(execute.i()) ? execute
						.i() : null;
						S2ExecuteConfig executeConfig = createExecuteConfig();
						executeConfig.setMethod(m);
						executeConfig.setSaveErrors(execute.saveErrors());
						executeConfig.setInput(input);
						List<S2ValidationConfig> validationConfigs = new ArrayList<S2ValidationConfig>();
						String validate = execute.v();
						boolean validator = false;
						if (!StringUtil.isEmpty(validate)) {
							BeanDesc actionBeanDesc = actionMapping.getActionBeanDesc();
							BeanDesc actionFormBeanDesc = actionMapping
									.getActionFormBeanDesc();
							for (String name : StringUtil.split(validate, ", ")) {
								if (VALIDATOR.equals(name)) {
									if (!execute.validator()) {
										throw new UnmatchValidatorAndValidateRuntimeException(
												actionClass, m.getName());
									}
									validationConfigs.add(new S2ValidationConfig());
									validator = true;
								} else if (actionFormBeanDesc.hasMethod(name)) {
									Method validateMethod = actionFormBeanDesc
											.getMethod(name);
									if (validateMethod.getParameterTypes().length > 0
											|| !ActionMessages.class
											.isAssignableFrom(validateMethod
													.getReturnType())) {
										throw new IllegalValidateMethodRuntimeException(
												actionClass, validateMethod.getName());

									}
									validationConfigs.add(new S2ValidationConfig(
											validateMethod));
								} else {
									Method validateMethod = actionBeanDesc
											.getMethod(name);
									if (validateMethod.getParameterTypes().length > 0
											|| !ActionMessages.class
											.isAssignableFrom(validateMethod
													.getReturnType())) {
										throw new IllegalValidateMethodRuntimeException(
												actionClass, validateMethod.getName());

									}
									validationConfigs.add(new S2ValidationConfig(
											validateMethod));
								}
							}
						}
						if (!validator && execute.validator()) {
							validationConfigs.add(0, new S2ValidationConfig());
						}
						if (!validationConfigs.isEmpty() && input == null) {
							throw new IllegalValidatorOfExecuteMethodRuntimeException(
									actionClass, m.getName());
						}
						executeConfig.setValidationConfigs(validationConfigs);
						executeConfig.setUrlPattern(execute.urlPattern());
						String roles = execute.roles().trim();
						if (!StringUtil.isEmpty(roles)) {
							executeConfig.setRoles(StringUtil.split(roles, ", "));
						}
						executeConfig.setStopOnValidationError(execute
								.stopOnValidationError());
						executeConfig.setRemoveActionForm(execute.removeActionForm());
						String reset = execute.reset();
						if (!StringUtil.isEmpty(reset)) {
							Method resetMethod = null;
							if ("reset".equals(reset)) {
								resetMethod = actionMapping.getActionFormBeanDesc()
										.getMethodNoException(reset);
							} else {
								resetMethod = actionMapping.getActionFormBeanDesc()
										.getMethod(reset);
							}
							if (resetMethod != null) {
								executeConfig.setResetMethod(resetMethod);
							}
						}
						executeConfig.setRedirect(execute.r());
						if (executeConfig.isUrlPatternAllSelected()) {
							if (allSelectedExecuteConfig != null) {
								throw new MultipleAllSelectedUrlPatternRuntimeException(
										allSelectedExecuteConfig.getUrlPattern(),
										executeConfig.getUrlPattern());
							}
							allSelectedExecuteConfig = executeConfig;
						} else {
							actionMapping.addExecuteConfig(executeConfig);
						}
			}
		}
		if (allSelectedExecuteConfig != null) {
			actionMapping.addExecuteConfig(allSelectedExecuteConfig);
		}
		if (actionMapping.getExecuteConfigSize() == 0) {
			throw new ExecuteMethodNotFoundRuntimeException(actionClass);
		}
	}

}
