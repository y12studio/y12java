package tw.y12.beyes.googl;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GgShorterTest {

	private CountDownLatch lock = new CountDownLatch(1);
	
	@Test
	public void testShorter() {
		GgShorter gs = new  GgShorter();
		gs.turnLogFull();
		gs.shortenUrl("http://590c.org/hello/goo.gl", new Callback<GgResponse>() {
			
			@Override
			public void success(GgResponse t, Response response) {
				assertNotNull(t);
				assertEquals(200,response.getStatus());
				assertNotNull(t.getId());
				lock.countDown();
			}
			
			@Override
			public void failure(RetrofitError error) {
				
			}
		});
		
		try {
			lock.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
