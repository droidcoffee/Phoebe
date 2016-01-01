package phoebe.sample.activity;

import phoebe.frame.activity.BaseActivity;
import phoebe.frame.util.HandlerMgr;
import phoebe.frame.util.reflect.JsonParser;
import phoebe.sample.bean.LoginBean;
import phoebe.sample.logic.MsgId;
import android.os.Bundle;
import android.os.Message;

/**
 * json解析
 * 
 * @author coffee <br>
 *         2016-1-1 上午11:02:17
 */
public class JsonDemoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showLoadingDialog("加载Json");
		// AppHttp.getInstance().test();
		Message msg = Message.obtain();
		msg.what = MsgId.json_parser;
		msg.obj = "{\"status\":1, \"username\":\"coffee\"}";
		HandlerMgr.sendMessage(msg, 1000);
	}

	@Override
	public boolean handleMessage(final Message msg) {
		switch (msg.what) {
		case MsgId.json_parser:
			cancelLoadingDialog();
			final LoginBean login = new JsonParser().parse(msg.obj + "", LoginBean.class);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showToast(login.getStatus() + " " + login.getUsername());
				}
			});
			break;
		default:
			break;
		}
		return super.handleMessage(msg);
	}
}
