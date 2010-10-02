package vnfoss2010.smartshop.serverside.authentication;

public class SessionObject {
	public String username;
	public String sessionId;
	public long timeStamp;

	public SessionObject() {
		this.username = "";
		sessionId = "";
		timeStamp = 0;
	}

	public SessionObject(String username, String sessionId, long timeStamp) {
		set(username, sessionId, timeStamp);
	}
	
	public void set(String username, String sessionid, long timeStamp) {
		this.username = username;
		this.sessionId = sessionid;
		this.timeStamp = timeStamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionObject other = (SessionObject) obj;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SessionObject [username=" + username + ", sessionId="
				+ sessionId + ", timeStamp=" + timeStamp + "]";
	}
}
