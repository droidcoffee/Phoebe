package phoebe.frame.util.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import phoebe.frame.util.Log;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 通用的http工具类<br>
 * 每次网络请求由一个单独的HttpClient对象处理<br>
 * 每个HttpClient不管是get还是post都关联一个HttpReq或者HttpPost对象
 * 
 * @author coffee
 */
public class HttpClient {
	private final String TAG = HttpClient.class.getSimpleName();
	// private static String encode = "UTF-8";

	private static String PROXY_SERVER = "10.0.0.172";

	// 检查网络类型
	private static int netType;

	// 网络类型
	private final static int WAP = 1; // wap网络
	private final static int NET = 2; // net网络
	private final static int NONE = 0; // 不可用

	private Object currHttp;

	// User agent strings.
	protected static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_7; en-us)" + " AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0" + " Safari/530.17";
	protected static final String IPHONE_USERAGENT = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us)" + " AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0"
			+ " Mobile/7A341 Safari/528.16";

	/**
	 * 设置网络类型 * netWrokInfo.getExtraInfo() [X86] NetworkInfo: type: ETH[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: true [WIFI]
	 * NetworkInfo: type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: true [CMNET]NetworkInfo: type: MOBILE[EDGE], state:
	 * CONNECTED/CONNECTED, reason: dataEnabled, extra: cmnet, roaming: false, failover: false, isAvailable: true [联通3G]NetworkInfo: type: mobile[HSPA], state: CONNECTED/CONNECTED, reason:
	 * dataEnabled, extra: 3gnet, roaming: false, failover: false, isAvailable: true
	 * 
	 * @param context
	 */
	public synchronized static void setNetworkType(Activity context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		String extraInfo = (netWrokInfo.getExtraInfo() + "").toLowerCase(Locale.getDefault());

		if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
			Toast.makeText(context, "当前的网络不可用，请开启\n网络", Toast.LENGTH_LONG).show();
			netType = NONE;
		} else if ("null".equals(netWrokInfo)) {
			// wifi
			netType = NET;
		} else if (extraInfo.contains("wap")) {
			netType = WAP;
		} else if (extraInfo.contains("net")) {
			netType = NET;
		}
	}

	// 检查网络类型
	public synchronized static boolean checkNetworkStatus(final Activity context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		connManager.getActiveNetworkInfo();
		// 网络状态
		boolean netSataus = false;
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null) {
			netSataus = info.isAvailable();
		}
		return netSataus;
	}

	// 关闭http请求
	public void abort() {
		if (this.currHttp != null) {
			if (this.currHttp instanceof HttpGet) {
				HttpGet get = (HttpGet) this.currHttp;
				get.abort();
			} else if (this.currHttp instanceof HttpPost) {
				HttpPost post = (HttpPost) this.currHttp;
				post.abort();
			}
		}
	}

	/**
	 * post方式提交数据
	 * 
	 * @param baseUrl
	 *            ： url
	 * @param paramsMap
	 *            : 参数集
	 * @param enc
	 *            : 编码方式
	 */
	public Object post(String baseUrl, Map<String, Object> paramsMap) {
		String doc = "";
		try {
			// 设置超时机制
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15 * 1000);
			HttpConnectionParams.setSoTimeout(httpParams, 15 * 1000);
			//
			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			//
			org.apache.http.client.HttpClient httpClient = new DefaultHttpClient(httpParams);
			// 为了解决百度定位出现的问题
			// Invalid cookie header: "Set-Cookie: BAIDUID=F90FEF7DF914D7C9490F26E609D8B61D:FG=1; max-age=31536000; expires=Wed,
			HttpClientParams.setCookiePolicy(httpClient.getParams(), org.apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY);
			if (netType == WAP) {
				HttpHost proxy = new HttpHost(PROXY_SERVER);
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			HttpPost post = new HttpPost(baseUrl);
			this.currHttp = post;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (String paramName : paramsMap.keySet()) {
				Object paramValue = paramsMap.get(paramName);
				if (paramValue == null) {
					continue;
				}
				params.add(new BasicNameValuePair(paramName, paramValue.toString()));
			}
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			post.setEntity(ent);

			HttpResponse responsePOST = httpClient.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();
			if (resEntity != null) {
				doc = EntityUtils.toString(resEntity);
				Log.i(TAG, doc);
			}
		} catch (java.net.SocketTimeoutException e) {
			return MsgID.http_time_out;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public String postWithImage(String url, Map<String, Object> paramsMap) {
		String doc = "";
		// DefaultHttpClient httpClient = new DefaultHttpClient();
		// HttpContext localContext = new BasicHttpContext();
		// HttpPost httpPost = new HttpPost(url);

		// try {
		// MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		//
		// /* example for setting a HttpMultipartMode */
		// builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		//
		// for (String paramName : paramsMap.keySet()) {
		// String paramValue = paramsMap.get(paramName);
		// if (paramValue == null) {
		// continue;
		// }
		// // 文件
		// if (paramValue.startsWith("/")) {
		// builder.addPart(paramName, new FileBody(new File(paramValue)));
		// } else {
		// String encodeValue = URLEncoder.encode(paramValue, "UTF-8");
		// builder.addPart(paramName, new StringBody(encodeValue, ContentType.TEXT_PLAIN));
		// }
		// }
		// //
		// httpPost.setEntity(builder.build());
		// HttpResponse response = httpClient.execute(httpPost, localContext);
		// HttpEntity resEntity = response.getEntity();
		// if (resEntity != null) {
		// doc = EntityUtils.toString(resEntity);
		// Log.i(TAG, doc);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return doc;
	}

	/**
	 * 上传文件
	 * 
	 * @param requestUrl
	 * @param file
	 * @return
	 */
	public static String postFile(String requestUrl, File file) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String newName = file.getName();
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// 允许Input、Output，不使用Cache
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			// 设置以POST方式进行传送
			con.setRequestMethod("POST");
			// 设置RequestProperty
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			// 构造DataOutputStream流
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; " + "name=\"file1\";fileName=\"" + newName + "\"" + end);
			ds.writeBytes(end);
			// 构造要上传文件的FileInputStream流
			FileInputStream fis = new FileInputStream(file);
			// 设置每次写入1024bytes
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			// 从文件读取数据至缓冲区
			while ((length = fis.read(buffer)) != -1) {
				// 将资料写入DataOutputStream中
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			// 关闭流
			fis.close();
			ds.flush();
			// 获取响应流
			InputStream is = con.getInputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				sb.append((char) ch);
			}
			// 关闭DataOutputStream
			ds.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * @param linkUrl
	 * @throws IOException
	 *             java.net.UnknownHostException: Host is unresolved: not-a-legal-address:80
	 * @param returnType
	 *            : type==0返回字符串 type==1 返回byte[]数组
	 * @return 返回408 请求超时
	 */
	public Object get(String linkUrl, Integer... returnType) {
		try {
			if (linkUrl == null || linkUrl.trim().equals("")) {
				return "";
			}
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15 * 1000);
			HttpConnectionParams.setSoTimeout(httpParams, 15 * 1000);
			DefaultHttpClient httpclient = new DefaultHttpClient();
			if (netType == WAP) {
				// 设置代理
				HttpHost proxy = new HttpHost(PROXY_SERVER, 80);
				httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			// 设置http请求
			HttpGet req = new HttpGet(linkUrl);
			this.currHttp = req;
			HttpResponse rsp = httpclient.execute(req);
			StatusLine line = rsp.getStatusLine();
			if (line.getStatusCode() != HttpStatus.SC_OK) {
				return "";
			}
			HttpEntity entity = rsp.getEntity();

			if (entity != null) {
				if (returnType.length > 0 && returnType[0] == 1) {
					return EntityUtils.toByteArray(entity);
				} else {
					String doc = EntityUtils.toString(entity);
					return doc;
				}
			}
			httpclient.getConnectionManager().shutdown();
		} catch (SocketTimeoutException e) {
			return 408;
		} catch (Exception e) {
			e.printStackTrace();
			// 如果wifi有问题 . 即是连上了wifi但是不能访问internet会抛如下异常
			// org.apache.http.conn.HttpHostConnectException: Connection to
			// http://192.168.226.30 refused
		}
		return "";
	}

	public static String getParamsString(Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		for (String key : params.keySet()) {
			String value = params.get(key) + "";
			sb.append(key);
			sb.append("=");
			sb.append(value);
			sb.append("&");
		}
		String result = sb.toString();
		return result;
	}

	/**
	 * 获取编码后的变量值
	 * 
	 * @return
	 */
	public static String getEncodeParam(String param) {
		if (param == null) {
			return null;
		}
		try {
			String encodeStr = URLEncoder.encode(param, "UTF-8");
			return encodeStr;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return param;
	}
}