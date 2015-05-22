package com.fwj.mobilesafe.domain;

import android.graphics.drawable.Drawable;
/**
 * 应用程序实体对象
 * @author yu
 *
 */
public class AppInfo {
	private boolean IsLocked;

	public boolean isLocked() {
		return IsLocked;
	}

	public void setIsLocked(boolean isLocked) {
		IsLocked = isLocked;
	}

	private String name;
	private String version;
	private String packageName;
	private Drawable icon;
	private boolean isUser;// 是否是用户程序
	private boolean isSD;// 是否在sd卡安装
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public boolean isUser() {
		return isUser;
	}
	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}
	public boolean isSD() {
		return isSD;
	}
	public void setSD(boolean isSD) {
		this.isSD = isSD;
	}
	@Override
	public String toString() {
		return "AppInfo [name=" + name + ", version=" + version
				+ ", packageName=" + packageName + ", icon=" + icon
				+ ", isUser=" + isUser + ", isSD=" + isSD + "]";
	}
	
	
}
