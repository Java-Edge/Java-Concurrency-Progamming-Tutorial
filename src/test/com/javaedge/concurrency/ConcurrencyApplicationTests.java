package test.com.javaedge.concurrency;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConcurrencyApplicationTests {

	@Test
	public void test() {
		Thread thread = new Thread();
		thread.start();
		thread.start();
	}

}
