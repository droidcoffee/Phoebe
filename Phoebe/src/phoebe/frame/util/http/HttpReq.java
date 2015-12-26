package phoebe.frame.util.http;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * HttpReq是一个网络请求的封装类,主要包括 网络请求的地址{@link #url}、网络请求的参数 {@link #params} <br>
 * 执行该网络请求的网络加载类 {@link #httpClient}或者自定义加载类 {@link #httpRunnable} <br>
 * 以及网络加载的回调 {@link #httpCallback}组成<br>
 * 
 * 每一个HttpReq都关联一个{@link HttpClient}对象。用于处理该网络请求 <br>
 * 
 * 当然如果框架中的HttpClient满足不了需求, 也可以继承{@link URLRunnable}接口来自定义实现<br>
 * 
 * {@link HttpLoader}中有一个线程池专门处理HttpReq<br>
 * 
 * @author coffee<br>
 *         2015-12-26上午11:42:23
 */
public class HttpReq {

	/**
	 * 每个请求都有唯一标示
	 */
	private int id;

	/**
	 * http请求类型, 默认是文本类型<br>
	 * 1 为图片类型的请求
	 */
	private int type = 0;

	/**
	 * 请求链接(如果是文本请求, 该参数可能为空)
	 */
	private String url;

	/**
	 * 框架中用到的预留的key {@link HttpParam}
	 */
	private HashMap<String, Object> params;

	/**
	 * 请求状态, 加载中1,未开始0,加载失败-1
	 */
	private int state;

	/**
	 * 与该http相关
	 */
	private WeakReference<HttpClient> httpClient;

	/**
	 * 如果该http请求需要特殊处理的话，可以扩展http加载类
	 */
	private HttpRunnable httpRunnable;
	/**
	 * http请求的回调
	 */
	private HttpCallback httpCallback;

	private int arg1, arg2;

	public HttpReq(String url, HttpCallback httpCallback) {
		this.url = url;
		this.type = 1;
		this.httpCallback = httpCallback;
	}

	/**
	 * 文本请求
	 * 
	 * @param params
	 * @param messageId
	 */
	public HttpReq(HashMap<String, Object> params, int id) {
		this.params = params;
		this.type = 0;
		this.id = id;
	}

	public HttpReq(HashMap<String, Object> params, int id, HttpCallback httpCallback) {
		this(params, id);
		this.httpCallback = httpCallback;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	/**
	 * 图片类型--请求只需要比较Url<br>
	 * 文本类型--请求除了比较Url, 还需要比较id<br>
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpReq other = (HttpReq) obj;
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		if (this.type == 0) {
			if (id != other.id) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param paramsName
	 *            {@link HttpParam}
	 * @return
	 */
	public boolean getParam(String paramsName) {
		if (params != null) {
			return Boolean.TRUE.equals(params.get(paramsName));
		}
		return false;
	}

	// **** 以下是setter getter *****
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HashMap<String, Object> getParams() {
		return params;
	}

	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public WeakReference<HttpClient> getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(WeakReference<HttpClient> httpClient) {
		this.httpClient = httpClient;
	}

	public HttpRunnable getHttpRunnable() {
		return httpRunnable;
	}

	public void setHttpRunnable(HttpRunnable httpRunnable) {
		this.httpRunnable = httpRunnable;
	}

	public HttpCallback getHttpCallback() {
		return httpCallback;
	}

	public void setHttpCallback(HttpCallback httpCallback) {
		this.httpCallback = httpCallback;
	}

	public int getArg1() {
		return arg1;
	}

	public void setArg1(int arg1) {
		this.arg1 = arg1;
	}

	public int getArg2() {
		return arg2;
	}

	public void setArg2(int arg2) {
		this.arg2 = arg2;
	}
}
