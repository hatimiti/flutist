package com.github.hatimiti.flutist.base.thymeleaf.processor.sa;

import static com.github.hatimiti.flutist.base.util._Struts.getErrorsIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionMessage;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.ProcessorResult;

import com.github.hatimiti.flutist.base.thymeleaf.dom.Br;
import com.github.hatimiti.flutist.base.thymeleaf.processor.JAbstractAttrProcessor;
import com.github.hatimiti.flutist.common.util._Obj;

public class ErrorsMessageProcessor extends JAbstractAttrProcessor {

	public static final String ALL_MESSAGE_KEY = "all";
	public static final String PROCESSOR_NAME = "errors";

	public ErrorsMessageProcessor() {
		super(PROCESSOR_NAME);
	}
	
	@Override
	protected ProcessorResult executeAttribute(
			Arguments arguments, Element element, String attributeName) {

		List<Node> r = getMessageNodes(element, attributeName);
		
		if (_Obj.isEmpty(r)) {
			element.getParent().removeChild(element);
		} else {
			element.setChildren(r);
			element.removeAttribute(attributeName);
		}

		return ProcessorResult.OK;
	}

	protected List<Node> getMessageNodes(Element element, String attributeName) {
		
		List<Node> r = new ArrayList<>();
		
		final String value = element.getAttributeValue(attributeName);
		final boolean isAll = ALL_MESSAGE_KEY.equals(value);
		
		for (Iterator<ActionMessage> i = getErrorsIterator(isAll ? "" : value); i.hasNext();) {
			ActionMessage m = i.next();
			
			r.add(new Text(m.isResource() ? /* TODO */"" : m.getKey()));
			r.add(new Br().toNode());
		}

		return r;
	}
	
}
