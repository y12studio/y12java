package tw.y12.beyes;

public interface ThirdPartyService {

	public interface PushUrlCallback {
		void onResult(String urlId, String urlData);
	}

	void pushThirdPartyData(byte[] target, PushUrlCallback cb);

}
