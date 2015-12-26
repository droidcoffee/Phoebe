package phoebe.frame.util;

import java.util.ArrayList;

import phoebe.frame.PhoebeApp;
import phoebe.frame.activity.BaseActivity;
import phoebe.frame.fragment.BaseFragment;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;

/**
 * app中用于UI/非UI线程通信的核心工具类<br>
 * 可以再"UI/非UI线程"往"UI线程"中发信息, 最终接收方为 {@link BaseActivity} {@link BaseFragment} {@link PhoebeApp}
 * 
 * @author coffee<br>
 *         2015-12-26下午4:36:26
 */
public class HandlerMgr {

	public static void sendMessage(int what) {
		int delayMillis = 0;
		final Message msg = Message.obtain();
		msg.what = what;
		sendMessage(msg, delayMillis);
	}

	/**
	 * 如果需要延迟发送的话则delayMillisArgs传入一个非0整数(单位毫秒)
	 * 
	 * @param what
	 *            消息ID {@link MsgType}
	 * @param arg1
	 *            返回结果状态码 .可以为空
	 * @param obj
	 *            返回数据
	 * @param delayMillisArgs
	 */
	public static void sendMessage(int what, int arg1, Object obj, int... delayMillisArgs) {
		int delayMillis = 0;
		if (delayMillisArgs.length > 0) {
			delayMillis = delayMillisArgs[0];
		}
		final Message msg = Message.obtain();
		msg.what = what;
		msg.arg1 = arg1;
		msg.obj = obj;
		sendMessage(msg, delayMillis);
	}

	/**
	 * 将消息发往Application、Activity、Fragment
	 * 
	 * @param orig
	 * @param delayMillis
	 */
	public static void sendMessage(Message orig, int delayMillis) {
		// 发往application
		Message appMessage = Message.obtain(orig);
		PhoebeApp.getHandler().sendMessage(appMessage);
		// 发往Activity
		ArrayList<?> activities = ActivityMgr.getAllActivities();
		for (final Object activity : activities) {
			boolean result = handle(activity, orig, delayMillis);
			if (result == true) {
				break;// 停止继续分发该Message
			} else {
				continue;
			}
		}
		// 发往Fragment
		// ArrayList<?> fragments = FragmentMgr.getAllFragments();
		// for (final Object fragment : fragments) {
		// boolean result = handle(fragment, orig, delayMillis);
		// if (result == true) {
		// break;// 停止继续分发该Message
		// } else {
		// continue;
		// }
		// }
	}

	public static void sendFinishMessage(Class<?> activityClass) {
		ArrayList<?> activities = ActivityMgr.getAllActivities();
		for (final Object activity : activities) {
			if (activityClass.getName().equals(activity.getClass().getName())) {
				if (activity instanceof Activity) {
					((Activity) activity).finish();
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
	}

	public static void removeMessage(int what) {
		PhoebeApp.getHandler().removeMessages(what);
	}

	/**
	 * 删除所有的handler以及message
	 */
	public static void removeCallbacksAndMessages() {
		/**
		 * Remove any pending posts of callbacks and sent messages whose <var>obj</var> is <var>token</var>. If <var>token</var> is null, all callbacks and messages will be removed.
		 */
		PhoebeApp.getHandler().removeCallbacksAndMessages(null);
	}

	private static boolean handle(final Object activityOrFragment, Message orig, int delayMillis) {
		final Message msg = Message.obtain(orig);
		if (delayMillis == 0) {
			boolean result = false;
			if (activityOrFragment instanceof Handler.Callback) {
				((Handler.Callback) activityOrFragment).handleMessage(msg);
			}
			return result;
		} else {
			PhoebeApp.getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if (activityOrFragment instanceof Handler.Callback) {
						((Handler.Callback) activityOrFragment).handleMessage(msg);
					}
				}
			}, delayMillis);
		}
		return false;
	}
}
