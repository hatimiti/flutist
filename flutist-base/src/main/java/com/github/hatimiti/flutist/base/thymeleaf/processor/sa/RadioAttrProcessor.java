package com.github.hatimiti.flutist.base.thymeleaf.processor.sa;

import static java.lang.String.format;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;

import com.github.hatimiti.flutist.base.thymeleaf.processor.JAbstractAttrProcessor;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.util._Str;

/**
 * @author hatimiti
 * 
 */
public class RadioAttrProcessor extends JAbstractAttrProcessor {

	public static final String PROCESSOR_NAME = "radio";

	public RadioAttrProcessor() {
		super(PROCESSOR_NAME);
	}

	@Override
	protected ProcessorResult executeAttribute(
			Arguments arguments,
			Element element,
			String attributeName) {

		String part1 = getAttributeValuePart(0);
		String part2 = getAttributeValuePart(1);
		part2 = "." + (_Obj.isEmpty(part2) ? "val" : part2);

		final String nameResult = _Str.toEmpty(eval(format("|%s|", part1)));
		final String valResult = _Str.toEmpty(eval(format("${%s%s}", nameResult, part2)));

		final String value = element.getAttributeValue("value");
		
		element.setAttribute("name", nameResult + part2);
		if (valResult.equals(value)) {
			element.setAttribute("checked", "checked");
		}
		
		element.removeAttribute(attributeName);
		return ProcessorResult.OK;
	}

}
