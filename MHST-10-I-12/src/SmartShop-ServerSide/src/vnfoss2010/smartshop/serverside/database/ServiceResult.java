package vnfoss2010.smartshop.serverside.database;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * <code>ServiceResult</code> presents a response from <code>RemoteService</code> <br/>
 * <b>Usage</b>: 
 * <ul><li><code>isOK</code> check RPC service call is OK or not
 * <li><code>result</code> developer can use generic type for return type. Before getting the result, call isOK() to make sure everything 's OK 
 * <li><code>message</code> the message server returns to indicate any error here
 * </ul>
 * @author Tam Vo
 */
public class ServiceResult<ResultType> implements IsSerializable{
	private boolean isOK = false;
	private String message = "";
	private ResultType result = null;
	
	/**
	 * Default constructor
	 */
	public ServiceResult() {
	}
	
	/**
	 * @param isOK successes or fails
	 * @param message message return from <code>RemoteService</code>
	 */
	public ServiceResult(boolean isOK, String message) {
		this.isOK = isOK;
		this.message = message;
	}
	
	/**
	 * Specify this <code>ServiceResult</code> successes or not
	 * @param isOK <code>true</code> if successes
	 * <br /><code>false</code> if fails
	 */
	public void setOK(boolean isOK) {
		this.isOK = isOK;
	}

	/**
	 * Determine this <code>ServiceResult</code> is successes or not
	 * @return <code>true</code> if successes
	 * <br /><code>false</code> if fails
	 */
	public boolean isOK() {
		return isOK;
	}

	/**
	 * Change message from <code>RemoteService</code>
	 * @param message <code>message</code> is message from <code>RemoteService</code>
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get message from <code>RemoteService</code>
	 * @return message from <code>RemoteService</code>
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Get object that <code>RemoteService</code> return 
	 * @return object that <code>RemoteService</code> return
	 */
	public ResultType getResult() {
		return result;
	}

	/**
	 * Change object that <code>RemoteService</code> return
	 * @param result <code>result</code> is an object that <code>RemoteService</code> return
	 */
	public void setResult(ResultType result) {
		this.result = result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServiceResult [isOK=" + isOK + ", message=" + message
				+ ", result=" + result + "]";
	}
}
