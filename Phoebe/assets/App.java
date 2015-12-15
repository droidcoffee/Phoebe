package droid.frame;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;

/**
 * Application类的基础子类。一般情况下需要配置SqlSentence
 * 
 * @author coffee<br>
 *         2014年7月15日下午6:13:25
 */
public class App extends Application {
	private static Context context;

	/**
	 * 项目中用到的全局Handler
	 */
	protected static Handler mHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		onCreateHandler();
		// Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
	}

	/**
	 * 子类可以根据需要扩展handler的实现
	 */
	protected void onCreateHandler() {
		mHandler = new Handler() {
		};
	}

	public static Context getContext() {
		return context;
	}

	public static Handler getHandler() {
		return mHandler;
	}

	public static String getMetaData(String key) {
		try {
			ApplicationInfo appInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
			String metaData = appInfo.metaData.getString(key);
			return metaData;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
