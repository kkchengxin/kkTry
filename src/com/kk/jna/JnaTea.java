package com.kk.jna;

//import g.pusher.constants.PusherConstants;

import org.apache.log4j.Logger;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * @author kk
 */
public class JnaTea {
	private static final Logger logger = Logger.getLogger(JnaTea.class
			.getName());
	// 密匙
	private static int[] key;

	static {
//		String keyStr = PusherConstants.container
//				.getProperty(PusherConstants.Keys.pusher_security_key);
//		String keyStr = keyStr == null ? "bb57a,d85af,e0040,e710a" : keyStr;
		String keyStr =  "bb57a,d85af,e0040,e710a";
		String[] keys = keyStr.split(",");
		key = new int[keys.length];
		for (int i = 0; i < key.length; i++) {
			key[i] = Integer.parseInt(keys[i], 16);
		}
	}

	/**
	 * 加密
	 * 
	 * @param Code
	 * @return
	 */
	public static byte[] encrypt(byte[] code) {
		int length = code.length;
		Pointer p = new Memory(4);
		Pointer codE = IJnaTea.INSTANCE.encryptData(code, key, length, p);
		int validateLen = p.getInt(0);
		// length = length + (length % 8 == 0 ? 0 : 8 - length % 8);
		byte[] bytesE = codE.getByteArray(0, validateLen);
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

	/**
	 * 解密
	 * 
	 * @param code
	 * @return
	 */
	public static byte[] decrypt(byte[] code) {
		int length = code.length;
		Pointer p = new Memory(4);
		Pointer codD = IJnaTea.INSTANCE.decryptData(code, key, length, p);
		byte[] bytesD = codD.getByteArray(0, p.getInt(0));
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

	public static void main(String[] args) {

		String code = "abc123即将底层存储的Unicode码解hahah,析为charset编码格式的字节数组方式11121kkgwegwegweghe";
		byte[] codeByte = code.getBytes();
		System.out.println("原文byte：" + codeByte.length);
		for (int i = 0; i < codeByte.length; i++) {
			System.out.print(codeByte[i] + ",");
		}
		System.out.println();
		byte[] encryp2 = encrypt(code.getBytes());
		byte[] decrypt = decrypt(encryp2);
		System.out.println("原文：" + code);
		System.out.println("密钥：" + key);
		System.out.println("原文：" + new String(decrypt));
		System.out.println(decrypt.length);
		for (int i = 0; i < decrypt.length; i++) {
			System.out.print(decrypt[i] + ",");
		}
	}

}