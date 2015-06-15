package com.lims.kewaiban.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author Luke
 * @date 2015年6月15日下午3:10:45
 * @description
 */
@DatabaseTable(tableName = "tb_user")
public class User {
	@DatabaseField(id = true)
	public int id;
	@DatabaseField
	public String name;
}
