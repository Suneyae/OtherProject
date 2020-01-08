package com.thread;

public class ThreadLocalTest implements Runnable {
	String name = null;
	int i = 0;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (; i < 10; i++) {
			// synchronized (this) {
			// name = Thread.currentThread().getName();
			//
			// try {
			// Thread.sleep(10);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// System.out.println("i:" + i + ","
			// + Thread.currentThread().getName() + ":" + name);
			// }

			name = Thread.currentThread().getName();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("i:" + i + ","
					+ Thread.currentThread().getName() + ":" + name);
		}
	}

	public static void main(String[] args) {
		ThreadLocalTest t = new ThreadLocalTest();
		new Thread(t, "AAA").start();
		new Thread(t, "CCC").start();
		
		
		

	}

}
