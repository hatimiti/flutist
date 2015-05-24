package com.github.hatimiti.flutist.common.domain.type;

import java.util.ArrayList;

public class ArrayListType<O>
		extends ListType<O, ArrayList<O>> {

	public ArrayListType() {
		super(new ArrayList<O>());
	}
	
	public ArrayListType(ArrayList<O> list) {
		super(list);
	}

}
