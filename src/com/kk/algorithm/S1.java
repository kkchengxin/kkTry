package com.kk.algorithm;

/**
 * 第17 题：http://blog.csdn.net/randyjiawenjie/article/details/6305816
 * 
 * 题目：在一个字符串中找到第一个只出现一次的字符。如输入abaccdeff，则输出b。
 * 
 * 分析：这道题是2006 年google 的一道笔试题。
 * 
 * 
 * 时间换空间，类似前面时间CSDN讨论的一道华为的面试题：在O(n)时间内，得出一个字符串每一个字符出现的次数
 * 
 * 类似；
 * 
 * 用到这道题，可以不只是统计只出现一次的；还可以统计多次出现的
 * 
 * @author kk
 * 
 */
public class S1 {
	public static char firstOne(String s) {
		char result = '0';
		char temp;
		int[] num = new int[52];
		for (int i = 0; i < s.length(); i++) {
			temp = s.charAt(i);
			if (temp >= 'a' && temp <= 'z') {
				num[temp - 'a']++;
			} else if (temp >= 'A' && temp <= 'Z') {
				num[temp - 'A' + 26]++;
			}
		}
		for (int i = 0; i < num.length; i++) {
			if (num[i] == 1) {
				if (i >= 0 && i <= 26) {
					result = (char) (i + 'a');
				} else
					result = (char) (i - 26 + 'A');
				break;
			}
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "abaccdeff";
		char c = firstOne(s);
		System.out.println(c);
		
		System.out.println( (int)'A');
		System.out.println("A="+ 'b');
		
	}
}
