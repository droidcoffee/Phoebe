package phoebe.sample.logic;

import java.util.HashMap;

import phoebe.frame.util.http.HttpLoader;
import phoebe.frame.util.http.HttpReq;

public class AppHttp {

	private static AppHttp instance;

	private AppHttp() {
	}

	public static AppHttp getInstance() {
		if (instance == null) {
			instance = new AppHttp();
		}
		return instance;
	}

	public void test() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("param1", "value1");
		// 创建一个httpReq对象、保存http的相关信息
		HttpReq req = new HttpReq(params, MsgId.test);
		req.setUrl("http://www.iciba.com/test");
		// 发送请求
		HttpLoader.getInstance().sendReq(req);
	}
}
