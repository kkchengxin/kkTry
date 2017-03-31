package com.kk.rainbow.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String getCurrTime(String paramString) {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
				paramString);
		return localSimpleDateFormat.format(new Date());
	}
}