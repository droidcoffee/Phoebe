/*
 * @(#)Log.java 11-10-9 下午3:00 CopyRight 2011. All rights reserved
 */
package droid.frame.utils.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import droid.frame.Config;

/**
 * 日志打印
 * 
 * @author coffee <br>
 *         2013-1-11下午3:15:58
 */
public abstract class Log{

	// 相对于sdcard的目录
	private static final String LOG_FILE = "log.txt";

	private static boolean isLogOpen() {
		return Config.isPrintLog();
	}

	/**
	 * 日期格式
	 */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

	private static Object handleMsgOrTag(Object msgOrTag) {
		if (msgOrTag == null) {
			msgOrTag = "[null]";
		} else if (msgOrTag.toString().trim().length() == 0) {
			msgOrTag = "[\"\"]";
		} else {
			msgOrTag = msgOrTag.toString().trim();
		}
		return msgOrTag;
	}

	public static void d(Object tag, Object msg) {
		if (isLogOpen() == false) {
			return;
		}
		tag = handleMsgOrTag(tag);
		msg = handleMsgOrTag(msg);
		android.util.Log.d(String.valueOf(tag), String.valueOf(msg));
		//
		storeLog("d", tag, msg);
	}

	/**
	 * @param obj
	 *            : 可以傳入 Class/String等类型的tag
	 * @param text
	 */
	public static void i(Object tag, Object msg) {
		if (isLogOpen() == false) {
			return;
		}
		tag = handleMsgOrTag(tag);
		msg = handleMsgOrTag(msg);
		android.util.Log.i(String.valueOf(tag), String.valueOf(msg));
		//
		storeLog("i", tag, msg);
	}

	public static void w(Object tag, Object msg, Throwable throwable) {
		if (isLogOpen() == false) {
			return;
		}
		tag = handleMsgOrTag(tag);
		msg = handleMsgOrTag(msg);

		if (throwable == null) {
			android.util.Log.w(String.valueOf(tag), String.valueOf(msg));
		} else {
			android.util.Log.w(String.valueOf(tag), String.valueOf(msg), throwable);
		}
		//
		storeLog("w", tag, msg);
	}

	public static void e(Object tag, Object msg, Throwable throwable) {
		if (isLogOpen() == false) {
			return;
		}
		tag = handleMsgOrTag(tag);
		msg = handleMsgOrTag(msg);
		if (throwable == null) {
			android.util.Log.e(String.valueOf(tag), String.valueOf(msg));
		} else {
			android.util.Log.e(String.valueOf(tag), String.valueOf(msg), throwable);
		}
		//
		storeLog("e", tag, msg);
	}

	public static void f(Object tag, Object str) {
		if (isLogOpen() == false) {
			return;
		}
		storeLog("d", tag, str);
	}

	private static void storeLog(String type, Object tag, Object strErrMsg) {
		// TODO 目前只打印xmpp:开头的
		if ((tag + "").toString().toLowerCase(Locale.getDefault()).startsWith("xmpp:") == false) {
			return;
		}
		File file = openFile(LOG_FILE);
		if (file == null || file.exists() == false) {
			return;
		}
		try {
			// 输出
			FileOutputStream fos = new FileOutputStream(file, true);
			PrintWriter out = new PrintWriter(fos);
			Date dateNow = new Date();
			String dateNowStr = dateFormat.format(dateNow);
			if (type.equals("e")) {
				out.println(dateNowStr + " Error:>>" + tag + "<<  " + strErrMsg + '\r');
			} else if (type.equals("d")) {
				out.println(dateNowStr + " Debug:>>" + tag + "<<  " + strErrMsg + '\r');
			} else if (type.equals("i")) {
				out.println(dateNowStr + " Info:>>" + tag + "<<   " + strErrMsg + '\r');
			} else if (type.equals("w")) {
				out.println(dateNowStr + " Warning:>>" + tag + "<<   " + strErrMsg + '\r');
			} else if (type.equals("v")) {
				out.println(dateNowStr + " Verbose:>>" + tag + "<<   " + strErrMsg + '\r');
			} else if (type.equals("f")) {
				out.println(dateNowStr + " File:>>" + tag + "<<   " + strErrMsg + '\r');
			}
			out.flush();
			out.close();
			out = null;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param name
	 *            文件名
	 * @return 返回文件
	 */
	private static File openFile(String name) {
		String logFilePath = Config.getMainDiar() + "/" + name;
		File logFile = new File(logFilePath);
		// 判断目录是否已经存在
		if (logFile.getParentFile().exists() == false) {
			try {
				if (logFile.getParentFile().mkdirs() == false || logFile.createNewFile() == false) {
					return null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logFile;
	}

}
