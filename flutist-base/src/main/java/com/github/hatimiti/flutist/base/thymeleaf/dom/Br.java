package com.github.hatimiti.flutist.base.thymeleaf.dom;

import org.thymeleaf.dom.Text;

public class Br extends JNode {

	public Br() {
		super(new Text("<br />", false));
	}

}
