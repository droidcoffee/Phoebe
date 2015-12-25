package phoebe.frame.dialog;

import phoebe.frame.R;
import android.content.Context;
import android.widget.TextView;

/**
 * app用到的进度加载框
 * 
 * @author coffee <br>
 *         2015-12-25 下午10:11:18
 */
public class AppLoading extends BaseDialog {

	private TextView mMessage;

	/**
	 * 需要传入一个Activity对象
	 * 
	 * @param context
	 */
	public AppLoading(Context context) {
		super(context, R.style.app_loading_theme, R.layout.app_loading);
		mMessage = (TextView) findViewById(R.id.app_loading_message);
	}

	/**
	 * 设置对话框的提示信息
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		mMessage.setText(message);
	}

}
