package com.test.ThreadLocal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * http://wobfei.iteye.com/blog/765207 下面通过这样一个例子来说明：模拟一个游戏，预先随机设定一个[1,
 * 10]的整数，然后每个玩家去猜这个数字，每个玩家不知道其他玩家的猜测结果，看谁用最少的次数猜中这个数字。
 * 这个游戏确实比较无聊，不过这里恰好可以把每个玩家作为一个线程
 * ，然后用ThreadLocal来记录玩家猜测的历史记录，这样就很容易理解ThreadLocal的作用。
 * 
 * Judge：用来设定目标数字以及判断猜测的结果。
 * Player：每个Player作为一个线程，多个Player并行地去尝试猜测，猜中时线程终止。
 * Attempt：具有ThreadLocal字段和猜测动作静态方法的类，ThreadLocal用于保存猜过的数字。
 * Record：保存历史记录的数据结构，有一个List<Integer>字段。
 * 
 * ThreadLocal为了可以兼容各种类型的数据，实际的内容是再通过set和get操作的对象，详见Attempt的getRecord()。
 * 运行的时候，每个Player
 * Thread都是去调用Attemp.guess()方法，进而操作同一个ThreadLocal变量history，但却可以保存每个线程自己的数据
 * ，这就是ThreadLocal的作用。
 * 
 * @author kk
 * 
 */
public class ThreadLocalTest {
	public static void main(String[] args) {
//		Judge.prepare();
		new Player(1).start();
		new Player(2).start();
		new Player(3).start();
	}
}

class Judge {
	public static int MAX_VALUE = 10;
	private static int targetValue;

	public static void prepare() {
		Random random = new Random();
		targetValue = random.nextInt(MAX_VALUE) + 1;
	}

	public static boolean judge(int value) {
		return value == targetValue;
	}
}

class Player extends Thread {
	private int playerId;

	public Player(int playerId) {
		this.playerId = playerId;
	}

	@Override
	public void run() {
		boolean success = false;
		while (!success) {
			int value = Attempt.guess(Judge.MAX_VALUE);
			success = Judge.judge(value);
			System.out.println(String.format("Plyaer %s Attempts %s and %s",
					playerId, value, success ? " Success" : "Failed"));
		}
		Attempt.review(String
				.format("[IFNO] Plyaer %s Completed by ", playerId));
	}
}

class Attempt {
	private static ThreadLocal<Record> history = new ThreadLocal<Record>();

	public static int guess(int maxValue) {
		Record record = getRecord();
		Random random = new Random();
		int value = 0;
		do {
			value = random.nextInt(maxValue) + 1;
		} while (record.contains(value));
		record.save(value);
		return value;
	}

	public static void review(String info) {
		System.out.println(info + getRecord());
	}

	private static Record getRecord() {
		Record record = history.get();
		if (record == null) {
			record = new Record();
			history.set(record);
		}
		return record;
	}
}

class Record {
	private List<Integer> attemptList = new ArrayList<Integer>();;

	public void save(int value) {
		attemptList.add(value);
	}

	public boolean contains(int value) {
		return attemptList.contains(value);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(attemptList.size() + " Times: ");
		int count = 1;
		for (Integer attempt : attemptList) {
			buffer.append(attempt);
			if (count < attemptList.size()) {
				buffer.append(", ");
				count++;
			}
		}
		return buffer.toString();
	}
}