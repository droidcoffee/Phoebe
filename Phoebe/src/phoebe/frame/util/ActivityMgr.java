package phoebe.frame.util;

import java.util.ArrayList;
import java.util.Stack;

import phoebe.frame.activity.BaseActivity;

/**
 * activity 管理
 * 
 * @author coffee <br>
 *         2015-12-20 上午10:42:22
 */
public class ActivityMgr {

	public static Stack<BaseActivity> activices = new Stack<BaseActivity>();

	/**
	 * 将Activity的引用加入到Stack
	 * 
	 * @param activity
	 */
	public static void push(BaseActivity activity) {
		Log.d("activity:onCreate", activity);
		activices.push(activity);
		Log.d("activity:all", activices);
	}

	/**
	 * 从Stack中移除引用
	 * 
	 * @param activity
	 */
	public static void remove(BaseActivity activity) {
		Log.d("activity:onDestroy", activity);
		activices.remove(activity);
		Log.d("activity:all", activices);
	}

	/**
	 * 获取static中栈顶的Activity
	 * 
	 * @return
	 */
	public static BaseActivity getTopActivity() {
		return activices.peek();
	}

	/**
	 * 获取所有的Activity
	 * 
	 * @return 返回的是一份对象拷贝。防止遍历的时候抛异常
	 */
	public static ArrayList<BaseActivity> getAllActivities() {
		ArrayList<BaseActivity> alls = new ArrayList<BaseActivity>(activices);
		return alls;
	}
}
