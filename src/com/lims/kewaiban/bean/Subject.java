package com.lims.kewaiban.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author Luke
 * @date 2015��6��15������3:35:55
 * @description
 */
@DatabaseTable(tableName = "tb_subject")
public class Subject {
	@DatabaseField(id = true)
	public int id;
	@DatabaseField
	public String name;
}
