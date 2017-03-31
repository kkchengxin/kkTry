package com.kk.jna;
//	g++ -c -fPIC -o tea.o Tea.cpp 
//lib --> prefix "libXXX.so" ,required lib
//	g++ -shared -o libtea.so tea.o
//sudo ln -s libtea.so /usr/lib/libtea.so
//run

/**
 * 动态库实现：
linux:
编译成obj文件：g++ -c -o dll.obj dll.cpp
链接obj，生成dll: g++ -shared -o dll.so dll.obj
g++ -shared -lc -o strcase.dll  lowcase.cpp uppercase.cpp -g
g++ -Wall -shared -o libhellod.dll hellod.cc

windows
编译DLL指令有如下三部曲：
第一步: DLL源文件编译成.o文件
gcc -c dll.cpp -o dll.o

第二步: DLL的.o文件编译成.so或者.dll文件(注意务必以lib开头, 这是gcc的约定)
gcc -shared -o libmyDLL.dll dll.o

第三步: 编译应用程序（务必省略后缀文件名以及lib前缀, gcc的约定）-L不能省
gcc -L./ -lmyDLL -o run app.cpp

 * @author kk
 *
 */
public class TestTea{
	public native int[] encryptString(String s,int[] key,int length ); 

	static {
		System.loadLibrary("tea");
	}

	public static void main(String[] args){
		p(System.getProperty("java.library.path"));		
		int[] result = new TestTea().encryptString("hello",new int[]{10,20},8);
		p("result:"+result);
		p("hello,tea");
	}

	private static void p(String s){
		System.out.println(s);
		
	}
}

