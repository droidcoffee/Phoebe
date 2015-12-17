package phoebe.frame;

import phoebe.frame.util.Log;
import android.app.Application;

/**
 * 该类主要针对测试以及线上环境的不同而做一些属性配置操作
 * 
 * @author coffee <br>
 *         2015-12-17 下午8:48:18
 */
public class PhoebeConfig {
	/**
	 * 默认开发环境 false表示线上正式环境
	 */
	private static boolean debug = true;
	/**
	 * app接口服务器地址(线上环境)
	 */
	private static String serverUrl;
	/**
	 * 同上 -- 测试环境
	 */
	private static String serverUrl_test;

	/**
	 * 初始化进行app的配置 一般在 {@link Application#onCreate()}中配置
	 */
	public static void init() {
		if (debug) {
			Log.setOpen(true);
		} else {
			Log.setOpen(false);
		}
	}

	/**
	 * 获取接口服务器的地址
	 * 
	 * @return
	 */
	public static String getServerUrl() {
		if (debug) {
			return serverUrl_test;
		} else {
			return serverUrl;
		}
	}
}
