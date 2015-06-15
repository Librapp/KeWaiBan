package com.lims.kewaiban.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author Luke
 * @date 2015年6月15日下午3:10:36
 * @description
 */
@DatabaseTable(tableName = "tb_teacher")
public class Teacher {
	@DatabaseField(id = true)
	public int id;
	@DatabaseField
	public String name;
}
