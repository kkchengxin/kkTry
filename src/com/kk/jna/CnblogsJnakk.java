package com.kk.jna;

import org.apache.log4j.Logger;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * @author kk
 */
public class CnblogsJnakk {
	private static final Logger logger = Logger.getLogger(CnblogsJnakk.class
			.getName());

	/**
	 * 入口函数
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 调用动态链库中的decryptString函数
		// ICnblogsJna.INSTANCE.decryptString(stringBuffer, key, length);
		// String encryptString = ICnblogsJna.INSTANCE.encryptString("123eggre",
		// 222, 12);

		String Code = "abc123即将底层存储的Unicode码解析为charset编码格式的字节数组方式";
		if (logger.isDebugEnabled()) {
			byte[] bytes = Code.getBytes();
			System.out.println("Code");
			for (int i = 0; i < bytes.length; i++) {
				System.out.print(bytes[i]);
				System.out.print(",");
			}
		}

		// 加密
		System.out.println();
		int length = Code.getBytes().length;
		System.out.println("length==" + length);
		int[] key = new int[] { 10, 20, 30, 40 };
		System.out.println("java.library.path===="
				+ System.getProperty("java.library.path"));
		// Memory codE = ICnblogsJnakk.INSTANCE.encryptString(bytes, key,
		// length);//Memory不好使
		Pointer codE = ICnblogsJnakk.INSTANCE.encryptString(Code, key, length);
		length = length + (length % 8 == 0 ? 0 : 8 - length % 8);
		byte[] bytesE = codE.getByteArray(0, length);
		// byte[] bytesE = ICnblogsJna.INSTANCE.encryptString(bytes,
		// key,length);
		if (logger.isDebugEnabled()) {
			System.out.println("bytesE:" + bytesE.length);
			for (int i = 0; i < bytesE.length; i++) {
				System.out.print(bytesE[i]);
				System.out.print(",");
			}
			System.out.println();
		}
		// Memory codD = ICnblogsJnakk.INSTANCE.decryptString(codE, key, 8);

		// 解密
		Pointer codD = ICnblogsJnakk.INSTANCE.decryptString(codE, key, length);
		// 直接这种方法也好使
//		 Pointer ptr = new Memory(bytesE.length);
		// ptr.write(0, bytesE, 0, bytesE.length);
		// Pointer codDD = ICnblogsJnakk.INSTANCE.decryptString(ptr, key, l);

		byte[] bytesD = codD.getByteArray(0, length);
		// byte[] bytesD=ICnblogsJna.INSTANCE.decryptString(bytesE, key,
		// 8);//不好使
		System.out.println("bytesD:" + bytesD.length);
		for (int i = 0; i < bytesD.length; i++) {
			System.out.print(bytesD[i]);
			System.out.print(",");
		}
		System.out.println();
		System.out.println("原文：" + Code);
		System.out.println("密钥：" + key);
		System.out.println("密文：" + codE.getString(0));
		System.out.println("解密：" + codD.getString(0));

		// System.out.println("解密codDD：" + codDD.getString(0));
	}

	// 密匙
	private int[] key = { 10, 20, 30, 40 };

	/**
	 * 加密
	 * 
	 * @param Code
	 * @return
	 */
	public byte[] encryp(String Code) {
		System.out.println();
		int length = Code.getBytes().length;
		System.out.println("length==" + length);
		Pointer codE = ICnblogsJnakk.INSTANCE.encryptString(Code, key, length);
		length = length + (length % 8 == 0 ? 0 : 8 - length % 8);
		byte[] bytesE = codE.getByteArray(0, length);
		// byte[] bytesE = ICnblogsJna.INSTANCE.encryptString(bytes,
		// key,length);
		if (logger.isDebugEnabled()) {
			System.out.println("bytesE:" + bytesE.length);
			for (int i = 0; i < bytesE.length; i++) {
				System.out.print(bytesE[i]);
				System.out.print(",");
			}
			System.out.println();
		}
		return bytesE;
	}

	public byte[] encryp(byte[] code) {
		String str = new String(code);
		return this.encryp(str);
	}

	/**
	 * 解密
	 * 
	 * @param code
	 * @return
	 */
	public byte[] decrypt(byte[] code) {
		int length = code.length;
		Pointer codD = ICnblogsJnakk.INSTANCE.decryptString(code, key, length);
		byte[] bytesD = codD.getByteArray(0, length);
		if (logger.isDebugEnabled()) {
			System.out.println("bytesD:" + bytesD.length);
			for (int i = 0; i < bytesD.length; i++) {
				System.out.print(bytesD[i]);
				System.out.print(",");
			}
			System.out.println();
		}
		return bytesD;
	}

	//去掉无效0
	public byte[] validBytes(byte[] score) {
		byte[] tempAry = new byte[score.length];
		int nonZeroCount = 0;
		for (int i = 0, j = 0; i < score.length; i++) {
			if (score[i] != 0) {
				tempAry[j++] = score[i];
				nonZeroCount++;
			}
		}
		byte newScore[] = new byte[nonZeroCount];
		for (int i = 0; i < newScore.length; i++) {
			newScore[i] = tempAry[i];
			System.out.print(newScore[i] + ", ");
		}
		return newScore;
	}
}