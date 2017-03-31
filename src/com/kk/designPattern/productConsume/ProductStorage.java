package com.kk.designPattern.productConsume;

/**
 * 
 http://aq1js622.blog.chinaunix.net/uid-29140694-id-4107880.html
	 （1）生产者仅仅在仓储未满时候生产，仓满则停止生产。
	 （2）消费者仅仅在仓储有产品时候才能消费，仓空则等待。
	 （3）当消费者发现仓储没产品可消费时候会通知生产者生产。
	 （4）生产者在生产出可消费产品时候，应该通知等待的消费者去消费。
	   下面通过一个例子描述下生产者消费者模式的使用。 
   说明：
	　对于本例，要说明的是当发现不能满足生产或者消费条件的时候，调用对象的wait方法，wait方法的作用是释放当前线程的所获得的锁，
	     并调用 对象的notifyAll() 方法，通知(唤醒)该对象上其他等待线程，使得其继续执行。这样，整个生产者、消费者线程得以正确的协作执行。
	  notifyAll() 方法，起到的是一个通知作用，不释放锁，也不获取锁。只是告诉该对象上等待的线程“可以竞争执行了，都醒来去执行吧”。
	　本例仅仅是生产者消费者模型中最简单的一种表示，本例中，如果消费者消费的仓储量达不到满足，而又没有生产者，则程序会一直处于等待
	     状态，这当 然是不对的。实际上可以将此例进行修改，修改为，根据消费驱动生产，同时生产兼顾仓库，如果仓不满就生产，并对每次最大消
	     费量做个限制，这样就不存在此问 题了，当然这样的例子更复杂，更难以说明这样一个简单模型。
 * 
 */
/**
 * 库存商品管理类
 */
public class ProductStorage {

	/**
	 * 最大库存量
	 */
	public static final int Maximum = 100;

	/**
	 * 当前库存量
	 */
	public static int Currentimum = 50;

	/**
	 * 库存管理实例
	 */
	private static ProductStorage instance;

	private ProductStorage() {
	}

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static ProductStorage getInstance() {
		if (instance == null) {
			instance = new ProductStorage();
		}
		return instance;
	}

	/**
	 * 生产产品
	 */
	public synchronized void product() {
		while (Currentimum >= Maximum / 2) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Currentimum++;
		System.out.println("当前线程：" + Thread.currentThread().getName()
				+ "--生产者生产了一个商品，当前库存量：" + Currentimum);
		notifyAll();
	}

	/**
	 * 消费产品
	 */
	public synchronized void consume() {
		while (Currentimum <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Currentimum--;
		System.out.println("当前线程：" + Thread.currentThread().getName()
				+ "--消费者消费了一个商品，当前库存量：" + Currentimum);
		notifyAll();
	}
}
