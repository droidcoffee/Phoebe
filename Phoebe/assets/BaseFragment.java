package com.collectplus.express;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shouhuobao.ui.dialog.AppLoading;

import droid.frame.App;
import droid.frame.activity.title.TitleMgr;
import droid.frame.activity.title.TitleRes;
import droid.frame.fragment.FrameBaseFragment;
import droid.frame.utils.android.SharedPref;
import droid.frame.view.pull2refresh.XListView;

/**
 * 非public.用的时候直接拷贝到子项目中。用法同 {@link BaseActivity} 一样
 * 
 * @author coffee<br>
 *         2014年9月19日下午3:42:50
 */
public abstract class BaseFragment extends FrameBaseFragment {

	private TitleMgr titleMgr;

	private ProgressBar mProgressBar;
	private TextView mEmptyView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		titleMgr = new TitleMgr(this);
	}

	/**
	 * 注意这个方法需要在{@link #setView(View)}之后调用
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		titleMgr.initTitle();
		//
		int loadingId = getResources().getIdentifier("loading_progressBar", "id", getContext().getPackageName());
		mProgressBar = (ProgressBar) findViewById(loadingId);
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}
		int emptyId = getResources().getIdentifier("empty_text", "id", getContext().getPackageName());
		mEmptyView = (TextView) findViewById(emptyId);
		if (mEmptyView != null) {
			((ViewGroup) mEmptyView.getParent()).setVisibility(View.GONE);
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (getContext() != null && getContext().getApplication() != null) {
			Thread.setDefaultUncaughtExceptionHandler(((ExpressApplication) getContext().getApplication()).getUncaughtExceptionHandler());
		}
	}

	/**
	 * 显示通用Title<br>
	 * 调用该方法时需要确保子类成功调用了 {@link #findViewById()}
	 */
	public void setCommonTitle(String titleText) {
		setTitle(new TitleRes[] { titleMgr.getBackTitle(), new TitleRes(0, titleText, null), null });
	}

	public void setTitle(TitleRes... reses) {
		for (int i = 0; i < reses.length; i++) {
			TitleRes res = reses[i];
			if (i == 0) {
				titleMgr.handleTitle(titleMgr.getTitleLeft(), res, 0);
			} else if (i == 1) {
				titleMgr.handleTitle(titleMgr.getTitleMiddle(), res, 1);
			} else if (i == 2) {
				titleMgr.handleTitle(titleMgr.getTitleRight(), res, 2);
			}
		}
	}

	public boolean isEmpty(Object str) {
		if (str == null || str.toString().trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public ExpressApplication getApp() {
		return (ExpressApplication) getContext().getApplication();
	}

	public TitleMgr getTitleMgr() {
		return titleMgr;
	}

	public void showLoadingBar() {
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}

	public void cancelLoadingBar() {
		getContext().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mProgressBar != null) {
					mProgressBar.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * 该方法主要考虑到一个界面里多个EmptyView
	 * 
	 * @param emptyText
	 */
	protected void setEmptyTextView(TextView emptyText) {
		this.mEmptyView = emptyText;
	}

	public void setEmptyView(ViewGroup viewGroup, String emptyText) {
		if (mEmptyView == null) {
			return;
		}
		//
		if (viewGroup instanceof ListView || viewGroup instanceof GridView) {
			AbsListView mAbsListView = (AbsListView) viewGroup;
			if (mAbsListView == null || mAbsListView.getAdapter() == null || mAbsListView.getAdapter().getCount() == 0) {
				((View) mEmptyView.getParent()).setVisibility(View.VISIBLE);
				if (isEmpty(emptyText) == false) {
					mEmptyView.setText(emptyText);
				}
			} else {
				((View) mEmptyView.getParent()).setVisibility(View.GONE);
			}
		} else if (viewGroup instanceof XListView) {
			XListView xListView = (XListView) viewGroup;
			// XListView含有Footer
			if (xListView == null || xListView.getListView().getAdapter() == null || xListView.getListView().getAdapter().getCount() <= 1) {
				((View) mEmptyView.getParent()).setVisibility(View.VISIBLE);
				if (isEmpty(emptyText) == false) {
					mEmptyView.setText(emptyText);
				}
			} else {
				((View) mEmptyView.getParent()).setVisibility(View.GONE);
			}
		}
	}

	public void showToast(final String content) {
		App.getHandler().post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void setSharedPref(String key, String value) {
		SharedPref.put(key, value);
	}

	protected String getSharedPref(String key) {
		return SharedPref.get(key);
	}

	public boolean handleMessage(final Message msg) {

		return false;
	}

	/**
	 * ********* 加载框相关 **********
	 */
	public void showLoadingDialog(final String dialogMessage) {
		getContext().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				cancelLoadingDialog();
				AppLoading appLoading = AppLoading.getInstance(getContext());
				appLoading.setMessage(dialogMessage);
				appLoading.show();
			}
		});
	}

	protected void cancelLoadingDialog() {
		getContext().runOnUiThread(new Runnable() {
			public void run() {
				AppLoading appLoading = AppLoading.getInstance(getContext());
				if (appLoading != null && appLoading.isShowing()) {
					appLoading.dismiss();
				} else {
					appLoading.destroy();
				}
			}
		});
	}

	public void setButtonEnableStyle(Button button, boolean enable) {
		if (enable) {
			button.setBackgroundColor(getResources().getColor(R.color.button_color));
			button.setTextColor(getResources().getColor(R.color.button_text_color));
		} else {
			button.setBackgroundColor(getResources().getColor(R.color.button_disabled_color));
			button.setTextColor(getResources().getColor(R.color.button_disabled_text_color));
		}
	}
}
