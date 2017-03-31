//#package test.tea;
//	g++ -c -fPIC -o tea.o Tea.cpp 
//lib --> prefix "libXXX.so" ,required lib
//	g++ -shared -o libtea.so tea.o
//sudo ln -s libtea.so /usr/lib/libtea.so
//run

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

