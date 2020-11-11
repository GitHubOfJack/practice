package com.jack;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PracticeApplicationTests {

	private Configration configration = new Configration();

	@Test
	void contextLoads() {

	}

	public synchronized void doTest() {
		System.out.println("aaa");
	}

	public void doTestA() {
		synchronized (configration) {
			System.out.println("bbb");
		}
	}

	public void doTestB() {
		synchronized (PracticeApplicationTests.class) {
			System.out.println("ccc");
		}
	}

	public void doTestC() {
		synchronized ("PracticeApplicationTests") {
			System.out.println("ddd");
		}
	}

}
