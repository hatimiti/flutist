package com.github.hatimiti.flutist.base.thymeleaf.dialect.sa;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import com.github.hatimiti.flutist.base.thymeleaf.processor.sa.ErrorsMessageProcessor;
import com.github.hatimiti.flutist.base.thymeleaf.processor.sa.ErrorsStyleClassProcessor;
import com.github.hatimiti.flutist.base.thymeleaf.processor.sa.RadioAttrProcessor;
import com.github.hatimiti.flutist.base.thymeleaf.processor.sa.TextAttrProcessor;

public class SADialect extends AbstractDialect {

	@Override
	public String getPrefix() {
		return "sa";
	}
	
	@Override
	public Set<IProcessor> getProcessors() {

		HashSet<IProcessor> processors = new HashSet<IProcessor>();
		processors.add(new ErrorsMessageProcessor());
		processors.add(new ErrorsStyleClassProcessor());
		processors.add(new TextAttrProcessor());
		processors.add(new RadioAttrProcessor());
		return processors;
	}
	
}
