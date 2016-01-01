package phoebe.frame.util;

/**
 * 日志打印
 * 
 * @author coffee <br>
 * 
 *         2015-12-16 下午9:39:41
 */
public class Log {

	/**
	 * 日志的开关, false 表示不打印日志
	 */
	private static boolean open = false;

	public static boolean isOpen() {
		return open;
	}

	public static void setOpen(boolean open) {
		Log.open = open;
	}

	public static void d(Object tag, Object msg) {
		if (open == false) {
			return;
		}
		tag = handleMsgOrTag(tag);
		msg = handleMsgOrTag(msg);
		android.util.Log.d(String.valueOf(tag), String.valueOf(msg));
	}

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

	public static void i(Object tag, Object msg) {
		if (open == false) {
			return;
		}
		tag = handleMsgOrTag(tag);
		msg = handleMsgOrTag(msg);
		android.util.Log.i(String.valueOf(tag), String.valueOf(msg));
	}
	
	public static void w(Object tag, Object msg, Throwable throwable) {
		if (open == false) {
			return;
		}
		tag = handleMsgOrTag(tag);
		msg = handleMsgOrTag(msg);
		android.util.Log.w(String.valueOf(tag), String.valueOf(msg), throwable);
	}

	public static void e(Object tag, Object msg, Throwable throwable) {
		if (open == false) {
			return;
		}
		tag = handleMsgOrTag(tag);
		msg = handleMsgOrTag(msg);
		android.util.Log.e(String.valueOf(tag), String.valueOf(msg), throwable);
	}
}
