package com.fwj.mobilesafe.uitils;

import java.util.List;

public class ListUtils {
	/**
	 * 判断集合是否为空（list为null，或者list长度为0，此时list均视为空）
	 * 
	 * @param list
	 * @return
	 */
	public static <T> boolean isEmty(List<T> list) {
		if (null == list) {
			return true;
		} else if (list.size() == 0) {
			return true;
		} else {
			return false;
		}

	}
}
