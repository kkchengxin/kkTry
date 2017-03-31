package com.kk;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 两个ArrayList合并，并且去掉重复数据的算法 思路： ①取出两个list中公共的元素 ②分别从两个list中把公共的元素去掉
 * ③将去掉公共元素的两个list合并就去掉了重复的数据
 */
public class Test {
	public static void main(String[] args) {
//		List list1 = new ArrayList();
//		list1.add(1);
//		list1.add(2);
//		list1.add(3);
//		list1.add(4);
//		list1.add(5);
//		list1.add(6);
//		List list2 = new ArrayList();
//		list2.add(4);
//		list2.add(5);
//		list2.add(6);
//		list2.add(7);
//		list2.add(8);
//		list2.add(9);
//		// temp用来保存两者共有的数据
//		List temp = new ArrayList(list1);
//		/**
//		 * 目前temp中包含1.2.3.4.5.6
//		 */
//		System.out.println("temp="+temp);
//		// retainAll(Collection<?> c)
//		// 仅在列表中保留指定 collection 中所包含的元素
//		// 这里的意思是temp只保留list2中有的元素
//		temp.retainAll(list2);
//		/**
//		 * 目前temp中仅有list1和list2中的公共元素4.5.6
//		 */
//		System.out.println("temp1="+temp);
//		// 从list1和list2中去掉两者共同有的数据
//		list1.removeAll(temp);
//		list2.removeAll(temp);
//
//		List list3 = new ArrayList();
//		list3.add(list1);
//		list3.add(list2);
//		System.out.println("list3="+list3);
//		System.out.println("list4="+list1.addAll(list2));
//		System.out.println("list4="+list1.addAll(list2).);
		
//		Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        Date time = calendar.getTime();
//        Date time = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String fromTime = df.format(time);
//        String toTime = fromTime + " 23:59:59";
//        
//		System.out.println("fromTime=="+fromTime);
//		System.out.println("toTime=="+toTime);
		
//		 long a = new Date().getTime();
//		 long b = 1000*3600;
//		 long a =10;
//		 long b = 3;
//		 int x = (int) (a%b);
//		 System.out.println(a % b);
//		 System.out.println(x);
		
		Long i=(long)0;
		long j=(long)3;
		System.out.println(i);
		System.out.println(j);
		
	} 
}
