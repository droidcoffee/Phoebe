package droid.frame.fragment;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import droid.frame.systembar.SystemBarConfig;

/**
 * 生命周期 FragmentTransaction.add(resId, Fragment) 的时候依次执行 onAttach-->onCreate-->onCreateView -->onActivityCreated <br>
 * <br>
 * [可以理解为：当activity对象创建的时候会执行onAttch <br>
 * 当"开始"执行onCreate的时候会执行onCreateView, onCreate执行"完成"会执行onActivityCreated] <br>
 * <br>
 * 销毁的过程依次执行 <br>
 * onPause-->onDestroyView-->onDestroy-->onDetach
 * 
 * @author coffee
 * 
 */
public abstract class FrameBaseFragment extends Fragment implements android.os.Handler.Callback {

	private final String TAG = "BaseFragment";

	/**
	 * Fragment 所在的 FragmentActivity<br>
	 * 禁止直接使用 {@link super#getActivity()}
	 */
	@Deprecated
	protected Activity context;

	protected FrameBaseFragment fragment;

	/**
	 * 是否可编辑，即frame所在的activity是否获有焦点<br>
	 * 只有执行完onResume才为true<br>
	 * onCreate onStart 状态都是false <br>
	 */
	protected boolean isResume = false;

	/**
	 * 如果该值为true则可以addToBackStack
	 */
	private boolean isToBackStack = false;
	/**
	 * 用于传参
	 */
	protected Bundle mArgs = new Bundle();
	/**
	 * 设计思想来自 {@link Message#obj}<br>
	 * 主要用于传参
	 */
	public Object obj;

	/**
	 * Fragment的contentView 主要用于 {@link #getView()}
	 */
	private View contentView;

	/**
	 * 是否需要适配4.4以上的SystemBar {@link SystemBarConfig}
	 */
	protected boolean isFitSystemBar = false;

	public FrameBaseFragment() {
		// 创建的额时候添加一个不为空的bundle参数集
		setArguments(new Bundle());
	}

	/**
	 * 处理Handler的Message
	 */
	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

	/****************** fragment 后退栈相关的 *********************/
	public void setToBackStack(boolean isToBackStack) {
		this.isToBackStack = isToBackStack;
	}

	public boolean isToBackStack() {
		return isToBackStack;
	}

	// TODO 参数设置相关
	/*****************************************************************/
	/**
	 * 因为构造方法已经保证了arguments不为空{@link #FrameBaseFragment()}<br>
	 * 所以重载次方法
	 * 
	 * @param bundle
	 */
	public void setArguments(Bundle bundle) {
		if (bundle == null) {
			//
			return;
		} else {
			super.setArguments(bundle);
		}
	}

	public void putArgument(String key, Object value) {
		if (value instanceof String) {
			this.mArgs.putString(key, String.valueOf(value));
		} else if (value instanceof Integer) {
			this.mArgs.putInt(key, Integer.valueOf(value.toString()));
		} else {
			// 这里面根据情况添加其他类型的数据
			// if (value instanceof Integer) {
			if (value instanceof Serializable) {
				this.mArgs.putSerializable(key, (Serializable) value);
			}
		}
		this.setArguments(mArgs);
	}

	public Object getArgument(String key) {
		Bundle args = getArguments();
		if (args != null) {
			return args.get(key);
		}
		return null;
	}

	/**
	 * {@link super#getActivity()}
	 * 
	 * @return {@link FragmentActivity}
	 */
	public Activity getContext() {
		return context;
	}

	public Fragment getFragment() {
		return this.fragment;
	}

	@Override
	public final View getView() {
		if (super.getView() != null) {
			return super.getView();
		} else {
			return this.contentView;
		}
	}

	public void setView(View layout) {
		this.contentView = layout;
	}

	public View findViewById(int id) {
		return getView().findViewById(id);
	}

	/**
	 * @return false将不会继续传递back事件
	 */
	public boolean onBackPressed() {
		return true;
	}

	/******************************* 以下是 Fragment 生命周期 *********************************/
	/**
	 * 当fragment被加入到activity时调用（在这个方法中可以获得所在的activity
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
		Log.d(TAG, "onAttach");
	}

	/**
	 * 该项目中onCreate主要用来初始化mHandler等非layout相关的操作
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate " + this);
		fragment = this;
		FragmentMgr.addToContainer(this);
		Log.d("fragment:onCreate ", FragmentMgr.getAllFragments() + "");
	}

	/**
	 * 注意： 子类重载该类的时候需要，在第一行代码设置变量layoutResId <br>
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//
		if (isFitSystemBar) {
			SystemBarConfig.setInsets(context, view);
		}
	}

	/**
	 * Fragment可见。 Fragment在一个运行中的activity中并且可见。
	 */
	@Override
	public void onResume() {
		super.onResume();
		this.isResume = true;
		Log.d(TAG, "onResume " + this);
	}

	/**
	 * Fragment可见,但是失去焦点。 另一个activity处于最顶层，但是fragment所在的activity并没有被完全覆盖（顶层的activity是半透明的或不占据整个屏幕）。
	 */
	@Override
	public void onPause() {
		super.onPause();
		this.isResume = false;
		Log.d(TAG, "onPause" + this);
	}

	@Override
	public void onDestroy() {
		FragmentMgr.remove(this);//
		FragmentMgr.removeFromContainer(this);
		Log.d(TAG, "onDestroy " + this);
		Log.d("fragment:ondestroy", FragmentMgr.getAllFragments() + "");
		super.onDestroy();
	}

	/******************************* 以上是生命周期 End *********************************/

	@Override
	public void startActivity(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		super.startActivity(intent);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		super.startActivityForResult(intent, requestCode);
	}
}
