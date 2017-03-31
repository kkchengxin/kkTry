package com.kk.rainbow.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {
	public static boolean mkdir(String paramString) {
		File localFile = new File(paramString);
		if (!localFile.isDirectory()) {
			if (localFile.isFile())
				return false;
			return localFile.mkdirs();
		}
		return false;
	}

	public static boolean isDirectory(String paramString) {
		if (StringUtil.isEmpty(paramString))
			return false;
		return new File(paramString).isDirectory();
	}

	public static String getFilenameByCurrtime(String paramString) {
		return DateUtil.getCurrTime(paramString);
	}

	public static String getFix(File paramFile) {
		if (paramFile != null) {
			String str = paramFile.getName();
			int i = str.lastIndexOf(".");
			return i >= 0 ? str.substring(i) : "";
		}
		return "";
	}

	public static File isFileExist(String paramString) {
		File localFile = new File(paramString);
		if (localFile.exists())
			return localFile;
		return null;
	}

	public static byte[] getBytes(String paramString) throws IOException {
		byte[] arrayOfByte = null;
		FileInputStream localFileInputStream = new FileInputStream(paramString);
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		int i;
		while ((i = localFileInputStream.read()) != -1)
			localByteArrayOutputStream.write(i);
		arrayOfByte = localByteArrayOutputStream.toByteArray();
		localByteArrayOutputStream.flush();
		localByteArrayOutputStream.close();
		localFileInputStream.close();
		return arrayOfByte;
	}

	public static void main(String[] paramArrayOfString) {
		mkdir("D:\\upload\\20110622\\cn.kuwo.player\\");
	}
}