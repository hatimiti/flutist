package com.github.hatimiti.flutist.base.thymeleaf.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nz.net.ultraq.web.thymeleaf.LayoutDialect;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.github.hatimiti.flutist.base.thymeleaf.dialect.sa.JUtilityDialect;
import com.github.hatimiti.flutist.base.thymeleaf.dialect.sa.SADialect;

/**
 * @author hatimiti
 */
public class ThymeleafServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected TemplateResolver resolver = getTemplateResolver();
	
	protected String prefix = "/WEB-INF/view/";
	protected String suffix = ".html";
	protected String templateMode = "HTML5";
	protected boolean isCacheable = false;
	protected long cacheTTLMs = 60000L;
	protected String characterEncoding = "UTF-8";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		initDefault(config);
		resolver.setPrefix(prefix);
		resolver.setSuffix(suffix);
		resolver.setTemplateMode(templateMode);
		resolver.setCacheable(isCacheable);
		resolver.setCacheTTLMs(cacheTTLMs);
		resolver.setCharacterEncoding(characterEncoding);
		
		resolver.initialize();
	}
	
	protected void initDefault(ServletConfig config) {
		if (isNotEmpty(config.getInitParameter("prefix"))) {
			this.prefix = config.getInitParameter("prefix");
		}
		if (isNotEmpty(config.getInitParameter("suffix"))) {
			this.suffix = config.getInitParameter("suffix");
		}
		if (isNotEmpty(config.getInitParameter("templateMode"))) {
			this.templateMode = config.getInitParameter("templateMode");
		}
		if (isNotEmpty(config.getInitParameter("isCacheable"))) {
			this.isCacheable = Boolean.parseBoolean(
					config.getInitParameter("isCacheable"));
		}
		if (isNotEmpty(config.getInitParameter("cacheTTLMs"))) {
			this.cacheTTLMs = Long.parseLong(
					config.getInitParameter("cacheTTLMs"));
		}
		if (isNotEmpty(config.getInitParameter("characterEncoding"))) {
			this.characterEncoding = config.getInitParameter("characterEncoding");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doService(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doService(req, resp);
	}

	protected void doService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding(resolver.getCharacterEncoding());

		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(resolver);
		engine.addDialect(new LayoutDialect());
		engine.addDialect(new SADialect());
		engine.addDialect(new JUtilityDialect());

		WebContext ctx = new WebContext(request, response, getServletContext());
		String templateName = getTemplateName(request);
		String result = engine.process(templateName, ctx);

		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(result);
		} finally {
			out.close();
		}
	}

	protected String getTemplateName(HttpServletRequest request) {
		String requestPath = request.getRequestURI().replaceFirst("^.*/thymeleaf", "");
		return requestPath
				.replaceAll(this.prefix, "")
				.replaceAll(this.suffix, "");
	}
	
	protected TemplateResolver getTemplateResolver() {
		return new ServletContextTemplateResolver();
	}
	
	protected boolean isNotEmpty(String s) {
		return s != null && !s.isEmpty();
	}
}
