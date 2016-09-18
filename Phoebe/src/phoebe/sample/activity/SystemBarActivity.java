package phoebe.sample.activity;

import android.os.Bundle;
import phoebe.frame.R;
import phoebe.frame.activity.BaseActivity;
import phoebe.frame.systembar.SystemBarConfig;
import phoebe.frame.titlebar.TitleRes;

/**
 * 沉浸式状态栏演示
 * 
 * @author coffee<br>
 *         2015-12-28下午2:18:38
 */
public class SystemBarActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		SystemBarConfig.setStatusBarStyle(this, android.R.color.holo_red_light);
		SystemBarConfig.showSystemStatusBar(this, android.R.color.holo_red_light);
	}

	@Override
	protected void findViewById() {
		// SystemBarConfig.setWindowFlags(this);
		setContentView(R.layout.systembar_demo);
		super.findViewById();
		getAppTitle().setTitle(null, new TitleRes("沉浸式状态栏演示"), null);
	}
}
