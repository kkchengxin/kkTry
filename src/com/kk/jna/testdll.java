package com.kk.jna;

public class testdll {
	 static{
	  System.loadLibrary("Tea"); //注意：不写扩展名，名字要与dll的文件名一致
	 }
	 public native int getValue();
	 public native void setValue(int i);

	 /**
	 Syso
	  * @param args
	  */
	 public static void main(String[] args) {
//	  testdll test=new testdll();
//	  test.setValue(10);
//	  System.out.println(test.getValue());  
		 
		int k = 10%4;
		System.out.println(k);  
	 }
	}


