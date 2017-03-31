package com.kk.designPattern.productConsume;

public class Producter implements Runnable {

	/**
	 * 商品生产者模型
	 */

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ProductStorage.getInstance().product();
		}
	}

}
