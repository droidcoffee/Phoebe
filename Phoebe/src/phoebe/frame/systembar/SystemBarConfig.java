package phoebe.frame.systembar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 4.4开始支持状态栏、导航栏背景色修改等
 * 
 * @author coffee<br>
 *         2015-12-28下午1:53:44
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class SystemBarConfig {

	/**
	 * 设置状态栏样式, 可以在 Activity#onCreate之前直接调用 <br>
	 * 需要注意一个问题: Activity中不能requestWindowFeature(Window.FEATURE_NO_TITLE) <br>
	 * 同时application的theme中也不能设置<item name="android:windowNoTitle">true</item> <br>
	 * 如果之前遗留的代码已经设置了以上属性。那么layout的最顶层view需要加android:fitsSystemWindows=true
	 * 
	 * @param context
	 * @param statusBarResourceId
	 *            颜色值资源id
	 */
	public static void setStatusBarStyle(Activity context, int statusBarResourceId) {
		Window w = context.getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//
		SystemBarTintManager tintManager = new SystemBarTintManager(context);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(statusBarResourceId);
	}

	public static void setWindowFlags(Activity context) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 状态栏背景颜色ID
			int statusBarId = context.getResources().getIdentifier("app_status_bar_bg", "color", context.getPackageName());
			if (statusBarId != 0) {
				setStatusBarStyle(context, statusBarId);
			} else {
				//
				final WindowManager.LayoutParams attrs = context.getWindow().getAttributes();
				attrs.flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				context.getWindow().setAttributes(attrs);
				context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			}
			// 导航栏背景
			int navigationBar = context.getResources().getIdentifier("app_navigation_bar_bg", "color", context.getPackageName());
			if (navigationBar != 0) {
				Window w = context.getWindow();
				w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			} else {
				final WindowManager.LayoutParams attrs = context.getWindow().getAttributes();
				attrs.flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
				context.getWindow().setAttributes(attrs);
				context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			}
		}
	}

	/**
	 * 设置Fragment的样式
	 * 
	 * @param context
	 * @param view
	 */
	public static void setInsets(Activity context, View view) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return;
		}
		// 状态栏背景颜色ID
		int statusBarId = context.getResources().getIdentifier("app_status_bar_bg", "color", context.getPackageName());
		if (statusBarId != 0) {
			SystemBarTintManager tintManager = new SystemBarTintManager(context);
			SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
			view.setPadding(0, config.getPixelInsetTop(false), config.getPixelInsetRight(), config.getPixelInsetBottom());
		}
	}
}
