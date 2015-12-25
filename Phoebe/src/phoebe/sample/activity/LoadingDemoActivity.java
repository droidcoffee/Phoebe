package phoebe.sample.activity;

import phoebe.frame.activity.BaseActivity;
import android.os.Bundle;

/**
 * loading 对话框展示
 * 
 * @author coffee<br>
 *         2015-12-25下午5:18:51
 */
public class LoadingDemoActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		
//		AppLoading loading = new AppLoading(getContext());
//		
//		loading.show();
		
		showLoadingDialog("加载中");
	}
	
}
