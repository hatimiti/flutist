package com.github.hatimiti.flutist.base.thymeleaf.utility;

import java.util.List;

import com.github.hatimiti.flutist.base.util._Struts;

public class SAUtility {

	public List<String> errors() {
		return errors(null);
	}
	
	public List<String> errors(String property) {
		return _Struts.getErrorStrings(property);
	}
	
}
