package phoebe.sample.activity;

import phoebe.frame.activity.BaseActivity;
import phoebe.sample.logic.AppHttp;
import phoebe.sample.logic.MsgId;
import android.os.Bundle;
import android.os.Message;

/**
 * 网络加载的演示
 * 
 * @author coffee<br>
 *         2015-12-28上午10:29:20
 */
public class HttpDemoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showLoadingDialog("发送网络请求");
		AppHttp.getInstance().test();
	}

	@Override
	public boolean handleMessage(final Message msg) {
		switch (msg.what) {
		case MsgId.test:
			cancelLoadingDialog();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String tip = msg.obj.toString();
					if (tip != null && tip.toString().length() > 100) {
						tip = tip.substring(0, 100);
					}
					showToast(tip);
				}
			});
			break;
		default:
			break;
		}
		return super.handleMessage(msg);
	}
}
