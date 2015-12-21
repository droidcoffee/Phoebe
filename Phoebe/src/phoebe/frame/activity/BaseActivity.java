package phoebe.frame.activity;

import phoebe.frame.R;
import phoebe.frame.util.ActivityMgr;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * app activity的父类<br>
 * 
 * @author coffee <br>
 *         2015-12-17 下午11:27:53
 */
public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityMgr.push(this);

		findViewById();
	}

	private ViewSwitcher mLeftSwitcher;
	private ViewSwitcher mMiddleSwitcher;
	private ViewSwitcher mRightSwitcher;

	/**
	 * 初始化View
	 */
	protected void findViewById() {
		mLeftSwitcher = (ViewSwitcher) findViewById(R.id.app_title_left_switcher);
		mMiddleSwitcher = (ViewSwitcher) findViewById(R.id.app_title_middle_switcher);
		mRightSwitcher = (ViewSwitcher) findViewById(R.id.app_title_right_switcher);

	}

	protected void setTitle(String left, String middle, String right) {
		((TextView) mLeftSwitcher.getChildAt(0)).setText(left);
		((TextView) mMiddleSwitcher.getChildAt(0)).setText(middle);
		((TextView) mRightSwitcher.getChildAt(0)).setText(right);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityMgr.remove(this);
	}

}
