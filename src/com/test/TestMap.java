package com.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TestMap {
	
	public final ConcurrentMap<String, Integer> clientChannelMappings = new ConcurrentHashMap<String, Integer>();
	public final Set<Integer> set = new HashSet<Integer>();
	public final int i =0;
	public final String k ;
	public final List lst =new ArrayList();
	
	public TestMap(){
		k="";
	}
	public void kk(){
		clientChannelMappings.put("kk", 1);
		clientChannelMappings.put("kt", 2);
		clientChannelMappings.put(null,null);
		lst.add(11);
		set.add(null);
		
	}
	
	public static void main(String [] agrs){
		TestMap  t =new TestMap();
		t.kk();
		t.clientChannelMappings.put("3", 3);
		boolean containsKey = t.clientChannelMappings.containsKey("kk");
		System.out.println(containsKey);
	    Iterator<String> iterator = t.clientChannelMappings.keySet().iterator();
		while(iterator.hasNext()){
			String next = iterator.next();
			System.out.println(next+"----"+t.clientChannelMappings.get(next));
			System.out.println();
			
		}
	}

}
