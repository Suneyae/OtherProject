package com.thread;

public class ThreadLocalTest2 implements Runnable {
	String name = null;
	ThreadLocal<String> thrL = new ThreadLocal<String>();

	int i = 0;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (; i < 10; i++) {
			thrL.set(Thread.currentThread().getName());

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("i:" + i + ","
					+ Thread.currentThread().getName() + ":" + thrL.get());
		}
	}

	public static void main(String[] args) {
		ThreadLocalTest2 t = new ThreadLocalTest2();
		new Thread(t, "AAA").start();
//		new Thread(t, "CCC").start();

	}

}
