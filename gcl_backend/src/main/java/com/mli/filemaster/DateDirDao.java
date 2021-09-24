package com.mli.filemaster;

import com.mli.dao.GenericDAO;

public interface DateDirDao extends GenericDAO<DateDirEntity> {

	DateDirEntity findByDate(String date);
}
