package tw.y12.beyes.googl;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;

public class GgShorter {

	private static final String BASE_URL = "https://www.googleapis.com";
	private GgService service;
	private RestAdapter restAdapter;

	public GgShorter() {
		restAdapter = new RestAdapter.Builder().setEndpoint(
				BASE_URL).build();
		service = restAdapter.create(GgService.class);
	}
	
	public void turnLogFull(){
		this.restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
	}

	public void shortenUrl(final String longUrl,
			final Callback<GgResponse> callback) {
		service.shortenUrl(new GgRequest(longUrl), callback);
	}

}
