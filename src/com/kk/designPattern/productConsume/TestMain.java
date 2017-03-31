package com.kk.designPattern.productConsume;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestMain {
	public static void main(String[] args) {
		startProductThread();
		startConsumThread();
	}

	/**
	 * 开启生产者线程
	 */
	public static void startProductThread() {
		System.out.println("--生产者线程执行开始--");
		int pThreadSize = 10;
		ExecutorService pool = Executors.newFixedThreadPool(pThreadSize);
		for (int i = 0; i < pThreadSize; i++) {
			Producter productThread = new Producter();
			Thread thread = new Thread(productThread);
			pool.execute(thread);
		}
		System.out.println("--生产者线程执行结束--");
	}

	/**
	 * 开启消费者线程
	 */
	public static void startConsumThread() {
		System.out.println("--消费者线程执行开始--");
		int pThreadSize = 10;
		ExecutorService pool = Executors.newFixedThreadPool(pThreadSize);
		for (int i = 0; i < pThreadSize; i++) {
			Consumer consumeThread = new Consumer();
			Thread thread = new Thread(consumeThread);
			pool.execute(thread);
		}
		System.out.println("--消费者线程执行结束--");
	}

}
