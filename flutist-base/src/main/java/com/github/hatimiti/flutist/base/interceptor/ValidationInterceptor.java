package com.github.hatimiti.flutist.base.interceptor;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.InvalidCancelException;
import org.apache.struts.validator.Resources;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.util.MethodUtil;
import org.seasar.struts.config.S2ActionMapping;
import org.seasar.struts.config.S2ExecuteConfig;
import org.seasar.struts.config.S2ValidationConfig;
import org.seasar.struts.enums.SaveType;
import org.seasar.struts.util.ActionFormUtil;
import org.seasar.struts.util.S2ActionMappingUtil;
import org.seasar.struts.util.S2ExecuteConfigUtil;
import org.seasar.struts.util.ServletContextUtil;
import org.slf4j.Logger;

import com.github.hatimiti.flutist.base.util._Container;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.util._Path;

/**
 * 入力チェックをインタセプタ化したもの．
 * これにより、入力チェックを他のインタセプタとの順番を調整できる．
 * @author m-kakimi
 *
 */
public class ValidationInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -4764627478894962478L;

	/**
	 * ログ出力用オブジェクト取得
	 */
	protected static final Logger logger = _Obj.getLogger();

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		HttpServletRequest request = _Container.getHttpServletRequest();

		S2ActionMapping actionMapping = S2ActionMappingUtil.getActionMapping();

		Object actionForm = actionMapping.getActionForm();

		S2ExecuteConfig executeConfig = S2ExecuteConfigUtil
				.findExecuteConfig(_Path.getActionPath(
						invocation.getThis().getClass()), invocation.getMethod().getName());

		ActionForward forward
			= processValidate(request, actionForm, executeConfig, actionMapping);

		if (forward == null) {
			return invocation.proceed();
		}

		//return forward.getPath().replaceAll(_Container.getViewRootPath(), "");
		return executeConfig.getInput();
	}

	protected ActionForward processValidate(
			HttpServletRequest request,
			Object actionForm,
			S2ExecuteConfig executeConfig,
			S2ActionMapping actionMapping)
					throws IOException, ServletException, InvalidCancelException {

		List<S2ValidationConfig> validationConfigs = executeConfig.getValidationConfigs();

		if (validationConfigs == null) {
			return null;
		}

		ActionMessages errors = new ActionMessages();

		for (S2ValidationConfig cfg : validationConfigs) {
			if (cfg.isValidator()) {
				ActionMessages errors2 = validateUsingValidator(
						request, actionMapping, executeConfig);
				if (errors2 != null && !errors2.isEmpty()) {
					errors.add(errors2);
					if (executeConfig.isStopOnValidationError()) {
						return processErrors(errors, request, actionMapping, executeConfig);
					}
				}
			} else {
				Object target = actionForm;
				if (cfg.getValidateMethod().getDeclaringClass()
						.isAssignableFrom(
								actionMapping.getComponentDef().getComponentClass())) {
					target = actionMapping.getAction();
				}
				ActionMessages errors2
					= (ActionMessages) MethodUtil.invoke(cfg.getValidateMethod(), target, null);
				if (errors2 != null && !errors2.isEmpty()) {
					errors.add(errors2);
					if (executeConfig.isStopOnValidationError()) {
						return processErrors(errors, request, actionMapping, executeConfig);
					}
				}
			}
		}

		if (!errors.isEmpty()) {
			return processErrors(errors, request, actionMapping, executeConfig);
		}

		return null;
	}

	protected ActionMessages validateUsingValidator(
			HttpServletRequest request,
			S2ActionMapping actionMapping,
			S2ExecuteConfig executeConfig) {

		ServletContext application = ServletContextUtil.getServletContext();
		ActionMessages errors = new ActionMessages();
		String validationKey = actionMapping.getName() + "_" + executeConfig.getMethod().getName();
		Validator validator = Resources.initValidator(validationKey,
				ActionFormUtil.getActionForm(request, actionMapping),
				application, request, errors, 0);
		try {
			validator.validate();
		} catch (ValidatorException e) {
			throw new RuntimeException(e);
		}

		return errors;
	}

	protected ActionForward processErrors(
			ActionMessages errors,
			HttpServletRequest request,
			S2ActionMapping actionMapping,
			S2ExecuteConfig executeConfig) {

		if (executeConfig.getSaveErrors() == SaveType.REQUEST) {
			request.setAttribute(Globals.ERROR_KEY, errors);
		} else {
			request.getSession().setAttribute(Globals.ERROR_KEY, errors);
		}

		return actionMapping.createForward(executeConfig.resolveInput(actionMapping));
	}

}
