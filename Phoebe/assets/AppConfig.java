package com.collectplus.express.logic;

import droid.frame.Config;
import droid.frame.XmppConfig;

public class AppConfig extends droid.frame.Config {

	private static boolean isTest = true;

	// app xmpp心跳,用于检测xmpp连接的有效性
	private static final int xmppBeat = 1000 * 60 * 2;

	static {
		init();
	}

	/**
	 * {@link ExpressApplication#onCreate()的先初始化全局变量}
	 */
	public static void init() {
		if (isTest) {
			Config.setPrintLog(true);
			XmppConfig.configServer(xmppHostTest, xmppPort, xmppBeat);
		} else {
			Config.setPrintLog(false);
			XmppConfig.configServer(xmppHost, xmppPort, xmppBeat);
		}
	}

	// xmpp服务器
	private static final String xmppHost = "msg.emove.shouhuobao.com";//
	private static final String xmppHostTest = "182.92.191.123";
	// xmpp端口号
	private static final int xmppPort = 5222;

	private static final String SERVER_URL_WX = "http://weixin.bhi.shouhuobao.com/emove-weixin/";
	private static final String SERVER_URL_WX_test = "http://test.weixin.bhi.shouhuobao.com/emove-weixin/";

	private static final String SERVER_URL_test = "http://test.bhi.shouhuobao.com:9980/bihi-appserv/";
	// private static final String SERVER_URL_test = "http://10.100.0.226:8088/";
	private static final String SERVER_URL = "http://bhi.shouhuobao.com:9980/bihi-appserv/";

	// public static final String URL = "http://10.100.0.139:8081/user-server/";

	public static final String getServerUrl() {
		if (isTest) {
			return SERVER_URL_test;
		}
		return SERVER_URL;
	}

	/**
	 * 微信分享的URL
	 * 
	 * @return
	 */
	public static final String getServerWXUrl() {
		if (isTest) {
			return SERVER_URL_WX_test;
		}
		return SERVER_URL_WX;
	}

	public static int getBadiuLocBeat() {
		return 1000 * 60 * 5; // 5分钟
	}

}
