package com.github.hatimiti.flutist.base.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.util.MethodUtil;
import org.seasar.struts.action.ActionWrapper;
import org.seasar.struts.config.S2ActionMapping;
import org.seasar.struts.config.S2ExecuteConfig;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.RequestUtil;

public class JActionWrapper extends ActionWrapper {

	public JActionWrapper(S2ActionMapping actionMapping) {
		super(actionMapping);
	}

	@Override
	protected ActionForward execute(
			HttpServletRequest request,
			S2ExecuteConfig executeConfig) {

		String next = (String) MethodUtil.invoke(executeConfig.getMethod(), this.action, null);

		if (executeConfig.isRemoveActionForm()
				&& !ActionMessagesUtil.hasErrors(request)) {

			if (this.actionMapping.getActionFormComponentDef().getInstanceDef()
					.equals(InstanceDefFactory.SESSION)) {
				RequestUtil.getRequest().getSession().removeAttribute(
						this.actionMapping.getActionFormComponentDef().getComponentName());
			} else {
				RequestUtil.getRequest().removeAttribute(
						this.actionMapping.getActionFormComponentDef().getComponentName());
			}

			RequestUtil.getRequest().removeAttribute(this.actionMapping.getAttribute());
		}

		boolean redirect = executeConfig.isRedirect();

		if (redirect && ActionMessagesUtil.hasErrors(request)) {
			redirect = false;
		}

		// *.html は thymeleafServlet へフォワードする対応
		ActionForward af = this.actionMapping.createForward(next, redirect);
		if (af.getPath().endsWith(".html")) {
			af.setPath("/thymeleaf" + af.getPath());
		}
		return af;
	}

}
