package tw.y12.beyes.googl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tw.y12.beyes.ThirdPartyService;
import tw.y12.beyes.YUtils;
import tw.y12.beyes.ThirdPartyService.PushUrlCallback;

public class ThirdPartyServiceGooglImpl implements ThirdPartyService {

	private final GgShorter gs;

	public static String PREFIX = "http://590c.org/gg!#";

	public ThirdPartyServiceGooglImpl() {
		this.gs = new GgShorter();
	}

	@Override
	public void pushThirdPartyData(byte[] target, final PushUrlCallback cb) {
		String url = PREFIX + YUtils.BASE64URL.encode(target);
		gs.shortenUrl(url, new Callback<GgResponse>() {
			@Override
			public void success(GgResponse t, Response response) {
				cb.onResult(t.getId(), t.getLongUrl());
			}

			@Override
			public void failure(RetrofitError error) {
				// TODO
			}
		});

	}

}
