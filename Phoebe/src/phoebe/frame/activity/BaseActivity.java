package phoebe.frame.activity;

import phoebe.frame.titlebar.TitleMgr;
import phoebe.frame.titlebar.TitleRes;
import phoebe.frame.util.ActivityMgr;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

/**
 * app activity的父类<br>
 * 
 * @author coffee <br>
 *         2015-12-17 下午11:27:53
 */
public abstract class BaseActivity extends Activity {

	private TitleMgr titleMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityMgr.push(this);

		findViewById();
	}

	/**
	 * 初始化View
	 */
	protected void findViewById() {
		titleMgr = new TitleMgr(getContext(), findViewById(android.R.id.content));
		titleMgr.findViewById();
	}

	protected void setTitle(TitleRes leftTitle, TitleRes middleTitle, TitleRes rightTitle) {
		titleMgr.setTitle(leftTitle, middleTitle, rightTitle);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityMgr.remove(this);
	}

	protected Context getContext() {
		return this;
	}

	protected void showToast(String message) {
		Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
	}
}
