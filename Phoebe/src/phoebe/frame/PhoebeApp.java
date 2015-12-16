package phoebe.frame;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * 这个类中主要做一些全局的配置, 或者整个app广泛用到的方法逻辑<br>
 * 
 * 生命周期从
 * 
 * @author coffee<br>
 *         2015-12-15下午3:28:30
 */
public class PhoebeApp extends Application {
	/**
	 *  app的上下文的引用，主要用于调用系统api的时候传参
	 */
	private static Context context;
	/**
	 * 全局Handler 一般一个app只需要定义一个Handler就搞定了
	 */
	private static Handler handler;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		onCreateHandler();
	}
	
	/**
	 * 子类可以根据具体情况重写handler的实现
	 */
	protected void onCreateHandler(){
		handler = new Handler();
	}
	
	public static Context getContext() {
		return context;
	}
	
	public static Handler getHandler() {
		return handler;
	}
}
