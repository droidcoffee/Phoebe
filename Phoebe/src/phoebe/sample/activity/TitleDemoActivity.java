package phoebe.sample.activity;

import phoebe.frame.R;
import phoebe.frame.activity.BaseActivity;
import phoebe.frame.titlebar.TitleRes;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

		// setTitle("返回主页", "这是一个Title", "下一个界面");
		TitleRes left = new TitleRes("left", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(TitleDemoActivity.this, "click left", Toast.LENGTH_SHORT).show();
				//showToast("click left");
			}
		});
		TitleRes middle = new TitleRes("middle", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(TitleDemoActivity.this, "click middle", Toast.LENGTH_SHORT).show();
				//showToast("click middle");
			}
		});
		TitleRes right = new TitleRes("right", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(TitleDemoActivity.this, "click right", Toast.LENGTH_SHORT).show();
				//showToast("click right");
			}
		});
		//
		getAppTitle().setTitle(left, middle, right);
	}

}
