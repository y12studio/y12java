package tw.y12.beyes.googl;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface GgService {
	@POST("/urlshortener/v1/url")
	void shortenUrl(@Body GgRequest requestBody, Callback<GgResponse> callback);
}
