package com.kk.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * 
 * @author kk
 */

public interface ICnblogsJna extends Library {
	/**
	 * 
	 * 接口实例 dll或者so文件放在当前路径是在项目下，而不是bin输出目录下。
	 */

	ICnblogsJna INSTANCE = (ICnblogsJna) Native.loadLibrary("tea",

	ICnblogsJna.class);

	// 与C代码映射的函数
	public Pointer encryptString(String stringBuffer, int[] key, int length);

	public Pointer decryptString(byte[] stringBuffer, int[] key, int length);

}
