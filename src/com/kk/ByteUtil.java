/**
 * Add by 郭  @genedigi
 * 2014-3-26
 */
package com.kk;

/**
 * @author harrison
 *
 */
public class ByteUtil {
	/**
	 * 将int转byte[]
	 * @param integer
	 * @return 
	 * 2014-3-26 下午7:19:42
	 */
	public static byte[] intToByteArray(final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer
				: integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer >>> (n * 8));

		return (byteArray);
	}
	
	/**
	 * 将byte[]转int
	 * @param b
	 * @return 
	 * 2014-3-26 下午7:20:02
	 */
	public static int byteArrayToInt(byte[] b) {
		return byteArrayToInt(b, 0);
	}

	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}
	
	public static void main (String[] arg) {
//		byte[] a = intToByteArray(80);
//		for (int i = 0; i < a.length; i ++)
//		System.out.print(a[i]);
//		System.out.println();
//		byte[] b = "200".getBytes();
//		System.out.println();
//		for (int i = 0; i < b.length; i ++) 
//			System.out.print(b[i]+";");
//		
		
		byte[] kk = {0,0,29,-85};
		int byteArrayToInt = byteArrayToInt(kk);
		System.out.print("byteArrayToInt===="+byteArrayToInt);
	}
}
