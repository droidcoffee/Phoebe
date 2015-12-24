package phoebe.frame.fragment;

import phoebe.frame.titlebar.TitleMgr;
import phoebe.frame.titlebar.TitleRes;
import phoebe.frame.util.Log;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {

	private TitleMgr titleMgr;

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
		titleMgr = new TitleMgr(getActivity(), getView());
		titleMgr.findViewById();
	}

	protected void setTitle(TitleRes leftTitle, TitleRes middleTitle, TitleRes rightTitle) {
		titleMgr.setTitle(leftTitle, middleTitle, rightTitle);
	}

	protected View findViewById(int id) {
		return getView().findViewById(id);
	}
}
