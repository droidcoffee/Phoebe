package phoebe.sample.fragment;

import phoebe.frame.R;
import phoebe.frame.fragment.BaseFragment;
import phoebe.sample.activity.TitleDemoActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.home, container, false);
		super.onCreateView(inflater, container, savedInstanceState);
		layout.findViewById(R.id.home_title_sample).setOnClickListener(this);
		return layout;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_title_sample:
			Intent intent = new Intent();
			intent.setClass(getActivity(), TitleDemoActivity.class);
			startActivity(intent);
			break;
		}

	}
}
