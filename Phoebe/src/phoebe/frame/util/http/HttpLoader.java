package phoebe.frame.util.http;

import java.lang.ref.WeakReference;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import phoebe.frame.util.HandlerMgr;
import phoebe.frame.util.Log;
import android.os.Message;

/**
 * 网络加载
 * 
 * @author coffee<br>
 *         2015-12-26上午11:23:00
 */
public class HttpLoader {

	private static Vector<HttpReq> reqs = new Vector<HttpReq>();

	private static HttpLoader instance;

	private ExecutorService executorService;

	private HttpLoader() {
		executorService = Executors.newCachedThreadPool();
	}

	public static HttpLoader getInstance() {
		if (instance == null) {
			instance = new HttpLoader();
		}
		return instance;
	}

	public void sendReq(final HttpReq req) {
		if (req.getParam(HttpParam.toastNet) == false) {
			// 如果指定不需要提示的话,则跳过网络是否可用的检查
		} else {
			// if (NetReceiver.isConnected() == false) {
			// Message msg = Message.obtain();
			// msg.what = MsgID.net_unavailable;
			// msg.obj = "网络不可用";
			// HandlerMgr.removeCallbacksAndMessages();
			// HandlerMgr.sendMessage(msg, 1000);
			// return;
			// }
		}

		// 如果线程池中存在该请求
		boolean has = false;
		for (int i = 0; i < reqs.size(); i++) {
			if (reqs.get(i).getId() == req.getId() && req.getState() != HttpReqState.STATE_FAILED) {
				has = true;
				break;
			}
		}
		// 该请求中线程池中已经存在
		if (has) {
			Log.d("http:loader", "请求已经存在" + req);
			return;
		} else {
			reqs.add(0, req); //
			executorService.execute(new ReqRunnable(req));
		}

	}

	class ReqRunnable implements Runnable {
		private HttpReq req;

		public ReqRunnable(HttpReq req) {
			this.req = req;
		}

		@Override
		public void run() {
			req.setState(HttpReqState.STATE_START);
			// 先从缓存中获取

			Object objResult = null;
			if (req.getHttpRunnable() != null) {
				objResult = req.getHttpRunnable().run();
			}
			// 默认的数据加载方式
			else {
				Log.d("http:url_" + req.getId(), req.getUrl() + "\n" + req.getParams());
				// 文本
				if (req.getType() == 0) {
					// post方式发送请求
					if (req.getParam(HttpParam.postText)) {
						HttpClient client = new HttpClient();
						// req.setHttpClient(client);
						objResult = client.post(req.getUrl(), req.getParams());
					} else {
						String url = req.getUrl();
						if (url.endsWith("?") == false) {
							url = url + "?";
						}
						String linkUrl = url + HttpClient.getParamsString(req.getParams());
						HttpClient client = new HttpClient();
						objResult = client.get(linkUrl);
					}
				}
				// 处理图片
				else if (req.getType() == 1) {

				}

			}
			Log.d("http:url_" + req.getId(), objResult);
			// 先移除请求,
			// 因为在notifyMessage的时候Activity#handleMessage中处理dialog以后。线程会卡住
			boolean success = reqs.remove(req);
			if (success == false) {// 从缓存中删除失败
				Log.e("http:state", "HttpReq 删除失败 " + req, null);
				Log.d("http:state [all]", reqs);
				req.setState(HttpReqState.STATE_FAILED);
			}
			// http请求超时
			if (Integer.valueOf(MsgID.http_time_out).equals(objResult)) {
				// 通知界面请求超时
				// HandlerMgr.sendMessage(req.getId(), MsgID.http_time_out, req.getId());
			} else {
				String result = objResult + "";
				notifyMessage(req, result);
			}
			if (req.getHttpCallback() != null) {
				if (objResult == null) {
					req.getHttpCallback().onSuccess(objResult);
				} else {
					req.getHttpCallback().onFailed(req);
				}
			}
		}
	}

	private void notifyMessage(HttpReq req, String result) {
		//
		Message msg = Message.obtain();
		msg.what = req.getId();
		msg.obj = result;
		msg.arg1 = req.getArg1();
		msg.arg2 = req.getArg2();
		//
		// Looper.prepare();
		HandlerMgr.sendMessage(msg, 0);
	}

	/**
	 * 移除http请求
	 * 
	 * @param reqId
	 */
	public void removeRequest(int reqId) {
		HttpReq req = new HttpReq(null, reqId);
		Log.d("http", "remove [ReqID] " + reqId);
		WeakReference<HttpClient> refHttp = req.getHttpClient();
		if (refHttp != null && refHttp.get() != null) {
			refHttp.get().abort();
		}
		reqs.remove(req);
	}

	public void removeAllRequest() {
		for (HttpReq req : reqs) {
			Log.d("http", "remove [ReqID] " + req.getId());
			WeakReference<HttpClient> refHttp = req.getHttpClient();
			if (refHttp != null && refHttp.get() != null) {
				refHttp.get().abort();
			}
		}
		reqs.clear();
	}

}
