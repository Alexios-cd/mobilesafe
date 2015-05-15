package com.fwj.mobilesafe.domain;
/**
 * 黑名单的实体对象
 * @author yu
 *
 */
public class BlankNumInfo {
	private String num;
	private int mode;
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	@Override
	public String toString() {
		return "BlankNumInfo [num=" + num + ", mode=" + mode + "]";
	}
	public BlankNumInfo(String num, int mode) {
		super();
		this.num = num;
		this.mode = mode;
	}
	public BlankNumInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
