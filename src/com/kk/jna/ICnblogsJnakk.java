package com.kk.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * 
 * http://www.cnblogs.com/gulvzhe/archive/2012/06/29/2569333.html
 * 
 * @author kk
 */

public interface ICnblogsJnakk extends Library {

	/**
	 * 
	 * 接口实例
	 * 
	 * dll或者so文件放在当前路径是在项目下，而不是bin输出目录下。
	 */

	ICnblogsJnakk INSTANCE = (ICnblogsJnakk) Native.loadLibrary("libtea",
			ICnblogsJnakk.class);

	// 与C代码映射的函数
	// public Memory encryptString(byte[] stringBuffer,int[] key,int length);//不好使
	// public Memory decryptString(Memory stringBuffer,int[] key,int length);
	//public Pointer decryptString(Pointer stringBuffer, int[] key, int length);//好使
	
	public Pointer encryptString(String stringBuffer, int[] key, int length);

	public Pointer decryptString(byte[] stringBuffer, int[] key, int length);

}
