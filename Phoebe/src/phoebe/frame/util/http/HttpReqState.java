package phoebe.frame.util.http;

/**
 * http请求的状态<br>
 * 
 * 目前分四个状态 (未开始, 加载中, 加载成功, 加载失败)
 * 
 * @author coffee<br>
 *         2015-12-26上午11:43:54
 */
public interface HttpReqState {
	/**
	 * http 请求失败
	 */
	public final static int STATE_FAILED = -1;
	
	/**
	 * http 初始状态 {@link HttpReqState}
	 */
	public final static int STATE_PREPARE = 0;
	
	/**
	 * http 请求正在执行
	 */
	public final static int STATE_START = 1;
	
	/**
	 * http 请求处理完毕
	 */
	public final static int STATE_SUCCESS = 2;
}
