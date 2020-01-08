package com.thread;

public class ThreadLocalTest3 implements Runnable {
	String name = null;
	int i = 0;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (; i < 10; i++) {

			name = Thread.currentThread().getName();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("i:" + i + ","
			// + Thread.currentThread().getName() + ":" + name);
			System.out.println("i:" + i + "," + Thread.currentThread() + ":"
					+ name);
		}
	}

	public static void main(String[] args) {
		// ThreadLocalTest3 t = new ThreadLocalTest3();
		// new Thread(t, "AAA").start();
		new Thread(new ThreadLocalTest3()).start();

	}

}
