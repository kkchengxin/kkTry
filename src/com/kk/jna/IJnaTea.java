package com.kk.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * 调用c/c++方法
 * @author kk
 */

public interface IJnaTea extends Library {
	/**
	 * 
	 *（1）windows下dll项目下，而不是bin输出目录下。
	 *（2）linux下so文件放在src下（如果找不到报找不到so文件错误，按下面方法设置：）
	 *     将so文件所在的目录设置到环境变量LD_LIBRARY_PATH中：vim /etc/profile
                              在export PATH USER LOGNAME MAIL HOSTNAME HISTSIZE INPUTRC下面加入export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${你的so目录}
                             之后，你可以检查一下设置的起没起效果：echo $LD_LIBRARY_PATH，如果出现你设置的内容就对了，如果没有，你可以重新打开一个窗口再查一下
	 */
	IJnaTea INSTANCE = (IJnaTea) Native.loadLibrary("tea",IJnaTea.class);

	// 与C代码映射的函数
	public Pointer encryptData(byte[] stringBuffer, int[] key, int length,Pointer len);

	public Pointer decryptData(byte[] stringBuffer, int[] key, int length,Pointer len);

}
