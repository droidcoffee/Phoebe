package com.collectplus.express;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.shouhuobao.ui.dialog.AppLoading;

import droid.frame.App;
import droid.frame.activity.base.FrameBaseActivity;
import droid.frame.activity.title.TitleRes;
import droid.frame.utils.android.ImmUtils;
import droid.frame.utils.android.VibratorMgr;
import droid.frame.utils.lang.JsonUtils;
import droid.frame.view.pull2refresh.XListView;

/**
 * 添加了一些适应具体界面|业务需求的功能
 * 
 * @author coffee
 * 
 *         2013年12月19日下午7:08:30
 */
public abstract class BaseActivity extends FrameBaseActivity {
	private ViewSwitcher mTitleLeft, mTitleMiddle, mTitleRight;
	/**
	 * 返回到上一页||通用Title
	 */
	protected TitleRes mBackTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private ProgressBar mProgressBar;
	private TextView mEmptyView;

	@Override
	protected void findViewById() {
		initTitle();
		int loadingId = getResources().getIdentifier("loading_progressBar", "id", getPackageName());
		mProgressBar = (ProgressBar) findViewById(loadingId);
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.GONE);
		}
		int emptyId = getResources().getIdentifier("empty_text", "id", getPackageName());
		mEmptyView = (TextView) findViewById(emptyId);
		if (mEmptyView != null) {
			((ViewGroup) mEmptyView.getParent()).setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Thread.setDefaultUncaughtExceptionHandler(((ExpressApplication) getApplication()).getUncaughtExceptionHandler());
	}

	protected int getIdentifier(String type, String name) {
		return getResources().getIdentifier(name, type, getPackageName());
	}

	private void initTitle() {
		int resBack = R.drawable.title_back;
		resBack = getResources().getIdentifier("title_back", "drawable", getPackageName());
		int resLeft = getResources().getIdentifier("title_left_switcher", "id", getPackageName());
		int resMiddle = getResources().getIdentifier("title_middle_switcher", "id", getPackageName());
		int resRight = getResources().getIdentifier("title_right_switcher", "id", getPackageName());
		//
		mTitleLeft = (ViewSwitcher) findViewById(resLeft);
		mTitleMiddle = (ViewSwitcher) findViewById(resMiddle);
		mTitleRight = (ViewSwitcher) findViewById(resRight);
		//
		mBackTitle = new TitleRes(1, resBack, mBackClickListener);
	}

	protected View.OnClickListener mBackClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/**
	 * 显示通用Title<br>
	 * 调用该方法时需要确保子类成功调用了 {@link #findViewById()}
	 */
	public void setCommonTitle(String titleText) {

		setTitle(new TitleRes[] { mBackTitle, new TitleRes(0, titleText, null), null });
	}

	public void setTitle(TitleRes... reses) {
		for (int i = 0; i < reses.length; i++) {
			TitleRes res = reses[i];
			if (i == 0) {
				handleTitle(mTitleLeft, res, 0);
			} else if (i == 1) {
				handleTitle(mTitleMiddle, res, 1);
			} else if (i == 2) {
				handleTitle(mTitleRight, res, 2);
			}
		}
	}

	/**
	 * 设置右侧按钮
	 * 
	 * @param res
	 */
	protected void setTitleRight(TitleRes res) {
		handleTitle(mTitleRight, res, 2);
	}

	/**
	 * 设置组件可见性<br>
	 * 注意该方法需要在setTitle之后调用, 否则无效
	 * 
	 * @param position
	 *            0-左 1-中 2-右
	 * @param visibility
	 *            {@link View#VISIBLE View#GONE}
	 */
	protected void setTitleVisibility(int position, int visibility) {
		if (position == 0) {
			mTitleLeft.setVisibility(visibility);
		} else if (position == 1) {
			mTitleMiddle.setVisibility(visibility);
		} else if (position == 2) {
			mTitleRight.setVisibility(visibility);
		}
	}

	private void handleTitle(View view, TitleRes res, int position) {
		if (view == null) {
			return;
		}
		if (res == null) {
			view.setVisibility(View.INVISIBLE);
			// 让该view占指定资源的空间
			// view.setBackgroundResource(R.drawable.title_back);
			return;
		}
		view.setVisibility(View.VISIBLE);
		// 文本
		if (res.getType() == 0) {
			TextView textView = null;
			// if (position == 0 || position == 2) {
			ViewSwitcher viewSwitcher = (ViewSwitcher) view;
			viewSwitcher.setDisplayedChild(0);
			textView = (TextView) viewSwitcher.getChildAt(0);
			// } else {
			// textView = (TextView) view;
			// }
			if (res.getRes() instanceof Integer) {
				textView.setText(Integer.valueOf(res.getRes().toString()));
			} else {
				textView.setText(String.valueOf(res.getRes()));
			}
		}
		// 图片
		else {
			ImageView imageView = null;
			// if (position == 0 || position == 2) {
			ViewSwitcher viewSwitcher = (ViewSwitcher) view;
			viewSwitcher.setDisplayedChild(1);
			imageView = (ImageView) viewSwitcher.getChildAt(1);
			// } else {
			// imageView = (ImageView) view;
			// }
			imageView.setImageResource(Integer.valueOf(res.getRes().toString()));
		}
		//
		if (res.getClickListener() != null) {
			view.setOnClickListener(res.getClickListener());
		}
	}

	// 隐藏键盘输入法
	public final static int MSG_IMM_HIDE = 1000;
	public final static int MSG_IMM_SHOW = 1000 + 1;

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_IMM_HIDE:
			ImmUtils.hide(getContext());
			break;
		}
		return false;
	}

	// ******************************************************************************************

	public boolean isEmpty(Object str) {
		if (str == null || str.toString().trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isNotEmpty(Object str) {
		return !isEmpty(str);
	}

	public void showLoadingBar() {
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}

	public void cancelLoadingBar() {
		if (mProgressBar != null) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mProgressBar.setVisibility(View.GONE);
				}
			});
		}
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
		} else if (viewGroup instanceof droid.frame.view.pull3refresh.XListView) {
			droid.frame.view.pull3refresh.XListView xListView = (droid.frame.view.pull3refresh.XListView) viewGroup;
			// XListView含有Footer
			if (xListView == null || xListView.getRefreshableView().getAdapter() == null || xListView.getRefreshableView().getAdapter().getCount() <= 2) {
				((View) mEmptyView.getParent()).setVisibility(View.VISIBLE);
				if (isEmpty(emptyText) == false) {
					mEmptyView.setText(emptyText);
				}
			} else {
				((View) mEmptyView.getParent()).setVisibility(View.GONE);
			}
		}
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

	public void showToastError(String jsonStr, String attr) {
		final Object err = JsonUtils.get(jsonStr, attr);
		App.getHandler().post(new Runnable() {
			@Override
			public void run() {
				final Toast toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 200);
				// TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
				// v.setBackgroundColor(color)
				// v.setTextColor(Color.RED);
				if (err == null || "".equals(err)) {
					toast.setText("操作失败");
				} else {
					toast.setText(err.toString());
				}
				toast.show();
			}
		});
	}

	/**
	 * 移动光标到最后
	 */
	public void setCursorToLast(EditText editText) {
		// 光标移动到最后
		Editable editable = editText.getEditableText();
		Selection.setSelection(editable, editable.length());
	}

	/**
	 * 振动
	 */
	public void vibrate() {
		VibratorMgr.vibrate(500);
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
