package com.kk.algorithm;

/**
 * 
 * 在做100题目的时候，全排列的算法困扰了很久，虽然网上了搜了一些资料，可是并没有搞懂。今天花了一个下午的时间，从新梳理了一遍，终于弄明白了。
 * 
 * 全排列的算法，递归分析网上都有：
 * 
 * http://www.cnblogs.com/nokiaguy/archive/2008/05/11/1191914.html
 * 
 * 设一组数p = {r1, r2, r3, ... ,rn}, 全排列为perm(p)，pn = p - {rn}。
 * 
 * 因此perm(p) = r1perm(p1), r2perm(p2), r3perm(p3), ... , rnperm(pn)。当n =
 * 1时perm(p} = r1。
 * 
 * 为了更容易理解，将整组数中的所有的数分别与第一个数交换，这样就总是在处理后n-1个数的全排列。
 * 实现java代码如下：
 * 
 * @author kk
 * 
 */
public class S2 {

	public static int total = 0;

	public static void swap(String[] str, int i, int j) {
		String temp = new String();
		temp = str[i];
		str[i] = str[j];
		str[j] = temp;
	}

	public static void arrange(String[] str, int st, int len) {
		if (st == len - 1) {
			for (int i = 0; i < len; i++) {
				System.out.print(str[i] + "  ");
			}
			System.out.println();
			total++;
		} else {
			for (int i = st; i < len; i++) {
				swap(str, st, i);
				arrange(str, st + 1, len);
				swap(str, st, i);
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str[] = { "a", "b", "c" };
		arrange(str, 0, str.length);
		System.out.println(total);
	}

}
