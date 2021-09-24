package com.mli.utils;

import org.springframework.data.domain.Pageable;

import com.mli.model.PageInfoModel;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public class PageableUtil {

	public static PageInfoModel getPageMetaInfo(Pageable pageable,int queryResultsSize, Long totalRecord) {
		PageInfoModel infoVM = new PageInfoModel();
		infoVM.setTotalElements(totalRecord + "");
		infoVM.setSize(pageable.getPageSize() + "");
		if (totalRecord % pageable.getPageSize() == 0) {
			infoVM.setTotalPages(totalRecord / pageable.getPageSize() + "");
		} else {
			infoVM.setTotalPages((totalRecord / pageable.getPageSize() + 1) + "");
		}
		infoVM.setNumberOfElements(queryResultsSize + "");
		infoVM.setNumber((pageable.getPageNumber())+"");
		return infoVM;
	}
}
