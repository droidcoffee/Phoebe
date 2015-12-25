package phoebe.frame.dialog;

import phoebe.frame.R;
import android.content.Context;


public class AppLoading extends BaseDialog {

	/**
	 * 需要传入一个Activity对象
	 * 
	 * @param context
	 */
	public AppLoading(Context context) {
		super(context,R.style.app_loading_theme, R.layout.app_loading);
	}
	
	
}
