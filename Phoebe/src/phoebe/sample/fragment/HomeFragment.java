package phoebe.sample.fragment;

import phoebe.frame.R;
import phoebe.frame.fragment.BaseFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends BaseFragment {
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.home, container, false);
		super.onCreateView(inflater, container, savedInstanceState);
		return layout;
	}
}
