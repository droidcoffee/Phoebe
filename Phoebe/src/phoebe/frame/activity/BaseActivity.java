package phoebe.frame.activity;

import phoebe.frame.util.ActivityMgr;
import android.app.Activity;
import android.os.Bundle;

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

	/**
	 * 初始化View相关的
	 */
	protected void findViewById() {
		
	}

	protected void setTitle(){
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityMgr.remove(this);
	}

}
