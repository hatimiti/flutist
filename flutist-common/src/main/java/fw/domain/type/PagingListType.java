package fw.domain.type;

import org.seasar.dbflute.cbean.PagingResultBean;

public class PagingListType<O>
		extends ListType<O, PagingResultBean<O>> {

	public PagingListType(PagingResultBean<O> list) {
		super(list);
	}

}
