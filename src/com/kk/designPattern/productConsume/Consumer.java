package com.kk.designPattern.productConsume;

/**
 * 商品消费者模型
 */
public class Consumer implements Runnable {

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ProductStorage.getInstance().consume();
		}

	}
}
