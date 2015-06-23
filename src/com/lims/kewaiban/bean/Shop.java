package com.lims.kewaiban.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author Luke
 * @date 2015年6月15日下午3:10:11
 * @description
 */
@DatabaseTable(tableName = "tb_shop")
public class Shop {
	@DatabaseField(id = true)
	public int id;
	@DatabaseField
	public String name;
	@DatabaseField
	public String address;
	@DatabaseField
	public String info;
	@DatabaseField
	public String mark;
	@DatabaseField
	public String lon;
	@DatabaseField
	public String lat;
	@DatabaseField
	public String pic;
	@DatabaseField
	public String type;
	@DatabaseField
	public String email;
	@DatabaseField
	public String phone;
	@DatabaseField
	public String foundcondition;
	@DatabaseField
	public String tab;
	@DatabaseField
	public String province;
	@DatabaseField
	public String city;
	@DatabaseField
	public String district;

}
