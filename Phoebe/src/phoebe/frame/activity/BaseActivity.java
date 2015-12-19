package phoebe.frame.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * app activity的父类<br>
 * 
 * @author coffee <br>
 *         2015-12-17 下午11:27:53
 */
public class BaseActivity extends Activity {

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		NetRunnable r = new NetRunnable(handler);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					System.out.println(msg.obj);
				}
			}
		};

		new Thread(r).start();
	}

	private class NetRunnable implements Runnable {
		private Handler handler;

		public NetRunnable(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			// do sth
			Message msg = Message.obtain();
			msg.what = 1;
			msg.obj = "http api result";
			this.handler.sendMessage(msg);
		}
	};

}
