package com.kk.jna;

import org.apache.log4j.Logger;

import com.sun.jna.Pointer;

/**
 * @author kk
 */
public class CnblogsJna {
	private static final Logger logger = Logger.getLogger(
			CnblogsJnakk.class.getName());
	//密匙
	private static int[] key = { 10, 20, 30, 40 };
	/**
	 * 加密
	 * @param Code 
	 * @return
	 */
	public static byte[] encryp(String Code){
		System.out.println();
		int length = Code.getBytes().length;
		System.out.println("length==" + length);
		Pointer codE = ICnblogsJnakk.INSTANCE.encryptString(Code, key, length);
		length = length + (length % 8 == 0 ? 0 : 8 - length % 8);
		byte[] bytesE = codE.getByteArray(0, length);
		if(logger.isDebugEnabled()){
			System.out.println("bytesE:" + bytesE.length);
			for (int i = 0; i < bytesE.length; i++) {
				System.out.print(bytesE[i]);
				System.out.print(",");
			}
			System.out.println();
		}
		return bytesE;
	}
	
	public static byte[] encryp(byte[] code){
		String str =  new String(code);
		return encryp(str);
	}
	
	/**
	 * 解密
	 * @param code
	 * @return
	 */
	public static byte[] decrypt(byte[] code){
		int length = code.length;
		Pointer codD = ICnblogsJnakk.INSTANCE.decryptString(code, key, length);
		byte[] bytesD = codD.getByteArray(0, length);
		if(logger.isDebugEnabled()){
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
		
		String Code = "abc123即将底层存储的Unicode码解析为charset编码格式的字节数组方式11121kk";
		byte[] encryp = encryp(Code);
		byte[] encryp2 = encryp(Code.getBytes());
		byte[] decrypt = decrypt(encryp);
		byte[] decrypt2 = decrypt(encryp);
		
		System.out.println("原文：" + Code);
		System.out.println("密钥：" + key);
		System.out.println("密文1：" + new String(encryp));
		System.out.println("密文2：" + new String(encryp2));
		System.out.println("解密1：" + new String(decrypt));
		System.out.println("解密2：" + new String(decrypt2));
		
		
	}

}