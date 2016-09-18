package phoebe.frame.systembar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 4.4开始支持状态栏背景色修改等
 * 
 * @author coffee<br>
 *         2015-3-18下午2:36:41
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class SystemBarConfig {

	public static boolean isSupport() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}

	public static void showSystemStatusBar(Activity context, int colorResId) {
		if (isSupport()) {
			// 状态栏背景颜色ID
			Window w = context.getWindow();
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//
			SystemBarTintManager tintManager = new SystemBarTintManager(context);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(colorResId);
		}
	}

	public static void showSystemNavBar(Activity context, int colorResId) {
		if (isSupport()) {
			Window w = context.getWindow();
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	public static void setInsets(Activity context, View view) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return;
		}
		// 状态栏背景颜色ID
		SystemBarTintManager tintManager = new SystemBarTintManager(context);
		SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
		view.setPadding(0, config.getPixelInsetTop(false), config.getPixelInsetRight(), config.getPixelInsetBottom());
	}
}
