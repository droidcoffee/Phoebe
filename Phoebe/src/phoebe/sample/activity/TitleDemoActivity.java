package phoebe.sample.activity;

import android.os.Bundle;
import phoebe.frame.R;
import phoebe.frame.activity.BaseActivity;

/**
 * title 演示
 * 
 * @author coffee <br>
 *         2015-12-20 下午6:54:07
 */
public class TitleDemoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void findViewById() {
		setContentView(R.layout.title_demo);
		super.findViewById();

		setTitle("返回主页", "这是一个Title", "下一个界面");
	}

}
