package com.github.hatimiti.flutist.base.thymeleaf.processor.sa;

import static com.github.hatimiti.flutist.base.util._Struts.getErrorsIterator;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;

import com.github.hatimiti.flutist.base.thymeleaf.processor.JAbstractAttrProcessor;
import com.github.hatimiti.flutist.common.util._Obj;

/**
 * 入力エラー時のCSSクラスを指定する。
 * @author hatimiti
 *
 */
public class ErrorsStyleClassProcessor extends JAbstractAttrProcessor {

	public static final String PROCESSOR_NAME = "errclass";

	public ErrorsStyleClassProcessor() {
		super(PROCESSOR_NAME);
	}

	@Override
	protected ProcessorResult executeAttribute(
			Arguments arguments, Element element, String attributeName) {

		if (_Obj.isNotEmpty(getErrorsIterator(element.getAttributeValue("name")))) {
			
			final String value = element.getAttributeValue(attributeName);
			Attribute styleClass = element.getAttributeMap().get("class");
			
			element.setAttribute("class",
					styleClass == null ? value : styleClass.getValue() + " " + value);
		}

		element.removeAttribute(attributeName);
		
		return ProcessorResult.OK;
	}

}
