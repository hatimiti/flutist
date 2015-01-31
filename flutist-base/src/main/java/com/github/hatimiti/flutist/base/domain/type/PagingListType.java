package com.github.hatimiti.flutist.base.domain.type;


public class PagingListType<O>
		extends ListType<O, PagingResultBean<O>> {

	public PagingListType(PagingResultBean<O> list) {
		super(list);
	}

}
