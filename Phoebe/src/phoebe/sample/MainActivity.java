package phoebe.sample;

import phoebe.frame.R;
import phoebe.frame.activity.BaseActivity;
import phoebe.frame.util.Log;
import phoebe.sample.fragment.HomeFragment;
import phoebe.sample.fragment.SettingFragment;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 主界面
 * 
 * @author coffee <br>
 *         2015-12-20 上午10:23:01
 */
public class MainActivity extends BaseActivity implements PanelSlideListener {

	private SlidingPaneLayout slidingPaneLayout;
	//
	private DisplayMetrics displayMetrics;
	private int maxMargin = 0;
	
	private static MainActivity instance;

	// 左侧设置界面
	private SettingFragment settingFragment;
	// 主界面
	private HomeFragment homeFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		instance = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}
	
	@Override
	protected void findViewById() {
		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		setContentView(R.layout.main);
		slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.slidingpanellayout);
		settingFragment = new SettingFragment();
		homeFragment = new HomeFragment();
		//
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.add(R.id.slidingpane_menu, settingFragment);
		tx.add(R.id.slidingpane_content, homeFragment);
		tx.commit();

		maxMargin = displayMetrics.heightPixels / 10;
		slidingPaneLayout.setPanelSlideListener(this);
	}

	/**
	 * @param arg0
	 *            view即使id为 slidingpane_content的view
	 */
	@TargetApi(14)
	@Override
	public void onPanelSlide(View arg0, float slideOffset) {
		float scale = 1 - ((1 - slideOffset) * maxMargin * 2) / (float) displayMetrics.heightPixels;

		Log.d("scale", slideOffset + " , " + scale);
		// 14 4.0之前的不处理
		if (VERSION.SDK_INT < VERSION_CODES.ICE_CREAM_SANDWICH) {
			// ignore
		} else {
			scale(scale, slideOffset);
			arg0.setScaleY(1F - (scale - (1 - (maxMargin * 2) / (float) displayMetrics.heightPixels)));
			// arg0.setAlpha(1);
			arg0.setBackgroundColor(Color.parseColor("#fff4f4f4"));
		}
	}

	@TargetApi(14)
	private void scale(float scale, float slideOffset) {
		settingFragment.getView().setScaleX(scale);// 设置缩放的基准点
		settingFragment.getView().setScaleY(scale);// 设置缩放的基准点
		settingFragment.getView().setPivotX(0);// 设置缩放和选择的点
		settingFragment.getView().setPivotY(displayMetrics.heightPixels / 2);
		settingFragment.getView().setAlpha(slideOffset);
	}

	@Override
	public void onPanelClosed(View arg0) {

	}

	@Override
	public void onPanelOpened(View arg0) {
		if (settingFragment != null) {
			settingFragment.onPanelOpened();
		}
	}

	public static void openPane() {
		instance.slidingPaneLayout.openPane();
	}

	public static void closePane() {
		instance.slidingPaneLayout.closePane();
	}

}
