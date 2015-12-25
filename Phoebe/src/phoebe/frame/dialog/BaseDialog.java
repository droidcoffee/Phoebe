package phoebe.frame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

/**
 * 项目中用到的对话框的基础父类
 * 
 * @author coffee<br>
 *         2015-12-25下午4:28:34
 */
public class BaseDialog extends Dialog {

	public BaseDialog(Context context) {
		super(context);
	}

	public BaseDialog(Context context, int layout) {
		super(context);
		setContentView(layout);
	}

	public BaseDialog(Context context, int theme, int layout) {
		super(context, theme);
		setContentView(layout);
	}

	@Override
	public void show() {
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		// win.getDecorView().setMinimumWidth((int) (win.getWindowManager().getDefaultDisplay().getWidth() * 0.6));// 设置dialog的宽度
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// lp.horizontalMargin = 300;
		win.setAttributes(lp);
		win.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		win.setLayout(lp.width, lp.height);
		super.show();
	}
}
