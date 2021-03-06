package phoebe.frame.fragment;

import phoebe.frame.dialog.AppLoading;
import phoebe.frame.titlebar.AppTitle;
import phoebe.frame.titlebar.TitleMgr;
import phoebe.frame.util.Log;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BaseFragment extends Fragment {

	private AppTitle appTitle;

	private AppLoading mLoadingDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("fragment:onCreateView", getView());
		return null;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.d("fragment:onCreateView", getView());
		//
		appTitle = new TitleMgr(getActivity(), getView());
		appTitle.initTitle();
	}

	protected View findViewById(int id) {
		return getView().findViewById(id);
	}

	protected void showLoadingDialog(String message) {
		if (mLoadingDialog != null) {
			mLoadingDialog.setMessage(message);
		} else {
			mLoadingDialog = new AppLoading(getActivity());
			mLoadingDialog.setMessage(message);
		}
	}

	protected void cancelLoadingDialog() {
		if (mLoadingDialog != null) {
			mLoadingDialog.cancel();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		cancelLoadingDialog();
		mLoadingDialog = null;
	}

	protected void showToast(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}

	protected void startActivity(Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), cls);
		super.startActivity(intent);
	}
	

	public AppTitle getAppTitle() {
		return appTitle;
	}

	public void setAppTitle(AppTitle appTitle) {
		this.appTitle = appTitle;
	}
}
