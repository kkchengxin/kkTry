package com.kk.rainbow.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class StringUtil {
	public static boolean isEmpty(String paramString) {
		return (paramString == null) || (paramString.trim().equals(""))
				|| (paramString.trim().equalsIgnoreCase("null"));
	}

	public static String doEmpty(String paramString) {
		return isEmpty(paramString) ? "" : paramString;
	}

	public static byte[] objectToBytes(Object paramObject) throws IOException {
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(
				localByteArrayOutputStream);
		localObjectOutputStream.writeObject(paramObject);
		return localByteArrayOutputStream.toByteArray();
	}

	public static Object bytesToObject(byte[] paramArrayOfByte)
			throws IOException, ClassNotFoundException {
		ObjectInputStream localObjectInputStream = new ObjectInputStream(
				new ByteArrayInputStream(paramArrayOfByte));
		return localObjectInputStream.readObject();
	}

	public static String toStringSplit(String[] paramArrayOfString,
			String paramString) {
		if (paramArrayOfString == null)
			return null;
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < paramArrayOfString.length; i++)
			localStringBuffer
					.append(i < paramArrayOfString.length - 1 ? paramArrayOfString[i]
							+ paramString
							: paramArrayOfString[i]);
		return localStringBuffer.toString();
	}

	public static String toStringSplit(Collection<String> paramCollection,
			String paramString) {
		if ((paramCollection == null) || (paramCollection.isEmpty()))
			return null;
		StringBuffer localStringBuffer = new StringBuffer();
		int i = 0;
		Iterator localIterator = paramCollection.iterator();
		while (localIterator.hasNext()) {
			String str = (String) localIterator.next();
			localStringBuffer.append(i < paramCollection.size() - 1 ? str
					+ paramString : str);
			i++;
		}
		return localStringBuffer.toString();
	}

	public static String initMD5(String paramString) {
		MessageDigest localMessageDigest = null;
		StringBuffer localStringBuffer = null;
		try {
			localMessageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
		}
		localStringBuffer = new StringBuffer();
		localStringBuffer.setLength(0);
		byte[] arrayOfByte = localMessageDigest.digest(paramString.getBytes());
		for (int i = 0; i < arrayOfByte.length; i++)
			localStringBuffer.append(toHex(arrayOfByte[i]));
		return localStringBuffer.toString();
	}

	private static String toHex(byte paramByte) {
		String str1 = "0123456789ABCDEF";
		char[] arrayOfChar = new char[2];
		arrayOfChar[0] = str1.charAt((paramByte & 0xF0) >> 4);
		arrayOfChar[1] = str1.charAt(paramByte & 0xF);
		String str2 = new String(arrayOfChar);
		str1 = null;
		arrayOfChar = null;
		return str2;
	}

	public static boolean isImageFullUrl(String paramString) {
		return isEmpty(paramString) ? false
				: paramString
						.matches("^http(s)?://([a-zA-Z0-9/&=?.%])+.(jpg|gif|jpeg|png)$");
	}

	public static void main(String[] paramArrayOfString) {
		System.out.println(Base64.encode("我自己"));
		HashSet localHashSet = new HashSet();
		localHashSet.add("4068");
		System.out.println(toStringSplit(localHashSet, "\\|"));
		String str1 = "4068";
		String[] arrayOfString = str1.split("\\|");
		System.out.println(arrayOfString.length);
		String str2 = "111111";
		System.out.println(initMD5(str2));
	}

	public static class Base64 {
		private static final char[] BASE64_ENCODING_TABLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
				.toCharArray();
		private static final byte[] BASE64_DECODING_TABLE = { -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55,
				56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3,
				4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
				21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30,
				31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46,
				47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };

		public static final String encode(byte[] paramArrayOfByte,
				int paramInt1, int paramInt2) {
			if (paramArrayOfByte == null)
				return null;
			StringBuffer localStringBuffer = new StringBuffer();
			int[] arrayOfInt = new int[3];
			int i = paramInt1 + paramInt2;
			while (paramInt1 < i) {
				arrayOfInt[0] = (paramArrayOfByte[(paramInt1++)] & 0xFF);
				if (paramInt1 == paramArrayOfByte.length) {
					localStringBuffer
							.append(BASE64_ENCODING_TABLE[(arrayOfInt[0] >>> 2 & 0x3F)]);
					localStringBuffer
							.append(BASE64_ENCODING_TABLE[(arrayOfInt[0] << 4 & 0x3F)]);
					localStringBuffer.append('=');
					localStringBuffer.append('=');
					break;
				}
				arrayOfInt[1] = (paramArrayOfByte[(paramInt1++)] & 0xFF);
				if (paramInt1 == paramArrayOfByte.length) {
					localStringBuffer
							.append(BASE64_ENCODING_TABLE[(arrayOfInt[0] >>> 2 & 0x3F)]);
					localStringBuffer
							.append(BASE64_ENCODING_TABLE[((arrayOfInt[0] << 4 | arrayOfInt[1] >>> 4) & 0x3F)]);
					localStringBuffer
							.append(BASE64_ENCODING_TABLE[(arrayOfInt[1] << 2 & 0x3F)]);
					localStringBuffer.append('=');
					break;
				}
				arrayOfInt[2] = (paramArrayOfByte[(paramInt1++)] & 0xFF);
				localStringBuffer
						.append(BASE64_ENCODING_TABLE[(arrayOfInt[0] >>> 2 & 0x3F)]);
				localStringBuffer
						.append(BASE64_ENCODING_TABLE[((arrayOfInt[0] << 4 | arrayOfInt[1] >>> 4) & 0x3F)]);
				localStringBuffer
						.append(BASE64_ENCODING_TABLE[((arrayOfInt[1] << 2 | arrayOfInt[2] >>> 6) & 0x3F)]);
				localStringBuffer
						.append(BASE64_ENCODING_TABLE[(arrayOfInt[2] & 0x3F)]);
			}
			return localStringBuffer.toString();
		}

		public static final String encode(byte[] paramArrayOfByte) {
			return encode(paramArrayOfByte, 0, paramArrayOfByte.length);
		}

		public static final String encode(String paramString) {
			return encode(paramString.getBytes());
		}

		public static final byte[] decode(String paramString) {
			if (paramString == null)
				return null;
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			byte[] arrayOfByte = paramString.getBytes();
			int[] arrayOfInt = new int[4];
			int i = 0;
			while (i < arrayOfByte.length) {
				do
					arrayOfInt[0] = BASE64_DECODING_TABLE[arrayOfByte[(i++)]];
				while ((i < arrayOfByte.length) && (arrayOfInt[0] == -1));
				if (arrayOfInt[0] == -1)
					break;
				do
					arrayOfInt[1] = BASE64_DECODING_TABLE[arrayOfByte[(i++)]];
				while ((i < arrayOfByte.length) && (arrayOfInt[1] == -1));
				if (arrayOfInt[1] == -1)
					break;
				localByteArrayOutputStream.write(arrayOfInt[0] << 2 & 0xFF
						| arrayOfInt[1] >>> 4 & 0xFF);
				do {
					arrayOfInt[2] = arrayOfByte[(i++)];
					if (arrayOfInt[2] == 61)
						return localByteArrayOutputStream.toByteArray();
					arrayOfInt[2] = BASE64_DECODING_TABLE[arrayOfInt[2]];
				} while ((i < arrayOfByte.length) && (arrayOfInt[2] == -1));
				if (arrayOfInt[2] == -1)
					break;
				localByteArrayOutputStream.write(arrayOfInt[1] << 4 & 0xFF
						| arrayOfInt[2] >>> 2 & 0xFF);
				do {
					arrayOfInt[3] = arrayOfByte[(i++)];
					if (arrayOfInt[3] == 61)
						return localByteArrayOutputStream.toByteArray();
					arrayOfInt[3] = BASE64_DECODING_TABLE[arrayOfInt[3]];
				} while ((i < arrayOfByte.length) && (arrayOfInt[3] == -1));
				if (arrayOfInt[3] == -1)
					break;
				localByteArrayOutputStream.write(arrayOfInt[2] << 6 & 0xFF
						| arrayOfInt[3]);
			}
			return localByteArrayOutputStream.toByteArray();
		}
	}
}