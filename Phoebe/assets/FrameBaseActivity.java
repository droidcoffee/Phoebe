package droid.frame.activity.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import droid.frame.App;
import droid.frame.activity.ActivityMgr;
import droid.frame.activity.HandlerMgr;
import droid.frame.systembar.SystemBarConfig;
import droid.frame.utils.android.Alert;

/**
 * activity的基类
 * 
 * @author coffee<br>
 *         2014年9月23日上午11:51:37
 */
public abstract class FrameBaseActivity extends Activity implements Handler.Callback {

	// 推荐用方法访问该变量 getContext()
	private Activity context;
	/**
	 * 是否需要加入到 {@link ActivityMgr}中管理
	 */
	protected boolean activityToMgr = true;

	/**
	 * 如果子类需要requestWindowFeature，则重置该变量为true。然后重新设置
	 */
	protected boolean resetWindowFeature = false;
	/**
	 * 如果子类需要设置全屏等Window flag, 则可以重置该变量 {@link Window#setFlags(int, int)}
	 */
	protected boolean resetWindowFlags = false;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		if (resetWindowFeature == false) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		//
		if (activityToMgr) {
			ActivityMgr.push(this);
		}
		if (resetWindowFlags) {
			// 设置4.4状态栏
			SystemBarConfig.setWindowFlags(context);
		}
		findViewById();
	}

	/**
	 * 获取页面控件对象d
	 */
	protected abstract void findViewById();

	public Activity getContext() {
		return context;
	}

	public Handler getHandler() {
		return App.getHandler();
	}

	@Override
	public void startActivity(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		super.startActivity(intent);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		super.startActivityForResult(intent, requestCode);
	}

	protected boolean isResume = false;

	@Override
	protected void onResume() {
		super.onResume();
		isResume = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		isResume = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 设置页面状态
		if (activityToMgr) {
			ActivityMgr.remove(this);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 处理各个Activity的请求message
	 * 
	 * @param msg
	 * @return 如果返回true则该消息会停止继续分发<br>
	 *         {@link HandlerMgr#sendMessage(int, int, Object, int...)}
	 */
	@Override
	public boolean handleMessage(Message msg) {

		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public void setFinishOnTouchOutside(boolean finish) {
		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
			super.setFinishOnTouchOutside(finish);
		}
	}

	/**
	 * 项目中用到的通用toast<br>
	 * 子类可以根据需要重载覆盖
	 */
	public void showToast(final String message) {
		Alert.toast(message);
	}

	public void showToast(final int message) {
		Alert.toast(message);
	}
}