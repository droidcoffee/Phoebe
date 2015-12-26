package phoebe.frame.util.http;

/**
 * 网络加载的回调接口
 * 
 * @author coffee<br>
 *         2015-12-26下午2:56:16
 */
public abstract class HttpCallback {

	public void onFailed(HttpReq req) {

	}

	/**
	 * 加载成功
	 * 
	 * @param result
	 *            bitmap或者String
	 */
	public abstract void onSuccess(Object result);
}