package tw.y12.beyes.googl;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import org590c.ProtoOpReturn;
import org590c.ProtoOpReturn.OpReturn;
import org590c.ProtoOpReturn.OpReturn.Tag;
import org590c.ProtoOpReturn.OpReturn.Type;
import tw.y12.beyes.ThirdPartyService;
import tw.y12.beyes.ThirdPartyService.PushUrlCallback;
import tw.y12.beyes.YUtils;

public class ThirdPartyServiceGooglImplTest {
	
	private CountDownLatch lock = new CountDownLatch(1);

	@Test
	public void testPushThirdPartyData() {
		ThirdPartyService tps = new ThirdPartyServiceGooglImpl();
		Tag tag = Tag.newBuilder().setTagInt32(1900).setTagString("hello 590c")
				.build();
		OpReturn opr = ProtoOpReturn.OpReturn.newBuilder().setType(Type.TAG)
				.setTag(tag).build();
		byte[] pbbytes = YUtils.wrap590c(opr.toByteArray());
		
		tps.pushThirdPartyData(pbbytes, new PushUrlCallback() {
			
			@Override
			public void onResult(String urlId, String urlData) {
				//System.out.println(urlId);
				//System.out.println(urlData);
				assertNotNull(urlId);
				assertTrue(urlId.indexOf("http://goo.gl")==0);
				assertNotNull(urlData);
				assertEquals("http://590c.org/gg!#WQwIARIPCgpoZWxsbyA1OTBjEOwO", urlData);
				lock.countDown();
			}
		});
		
		try {
			lock.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
