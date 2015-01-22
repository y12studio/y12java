package tw.y12.beyes.googl;

public class GgRequest {

	/**
	 * @param longUrl
	 */
	public GgRequest(String longUrl) {
		this.longUrl = longUrl;
	}

	private String longUrl;

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(final String longUrl) {
		this.longUrl = longUrl;
	}

}
