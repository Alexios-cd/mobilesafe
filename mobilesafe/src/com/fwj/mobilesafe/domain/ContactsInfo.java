package com.fwj.mobilesafe.domain;
/**
 * 联系人的实体对象
 * @author yu
 *
 */
public class ContactsInfo {
	// 电话号码
	private String num;
	private String name;
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ContactsInfo(String num, String name) {
		super();
		this.num = num;
		this.name = name;
	}
	public ContactsInfo() {
		super();
	}
	@Override
	public String toString() {
		return "ContactsInfo [num=" + num + ", name=" + name + "]";
	}
	
}
