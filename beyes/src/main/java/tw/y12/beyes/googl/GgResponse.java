package tw.y12.beyes.googl;

public class GgResponse {
	
	 private String kind;
	    private String id;
	    private String longUrl;

	    public String getKind() {
	        return kind;
	    }

	    public void setKind(final String kind) {
	        this.kind = kind;
	    }

	    public String getId() {
	        return id;
	    }

	    public void setId(final String id) {
	        this.id = id;
	    }

	    public String getLongUrl() {
	        return longUrl;
	    }

	    public void setLongUrl(final String longUrl) {
	        this.longUrl = longUrl;
	    }

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "GgResponse [" + (kind != null ? "kind=" + kind + ", " : "")
					+ (id != null ? "id=" + id + ", " : "")
					+ (longUrl != null ? "longUrl=" + longUrl : "") + "]";
		}
}
