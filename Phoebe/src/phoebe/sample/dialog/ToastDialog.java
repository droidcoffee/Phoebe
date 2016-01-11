package phoebe.sample.dialog;

import phoebe.frame.R;
import phoebe.frame.dialog.BaseDialog;
import android.content.Context;

/**
 * Toast风格的dialog 动画加载完成以后自动关闭
 * 
 * @author coffee <br>
 *         2016-1-11下午2:41:07
 */
public class ToastDialog extends BaseDialog {

	public ToastDialog(Context context) {
		super(context);
		//
		setContentView(R.layout.app_toast_dialog);
	}

	/**
	 * 
	 */
	public void show() {

	}

}
