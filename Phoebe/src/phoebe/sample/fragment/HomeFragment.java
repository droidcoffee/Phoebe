package phoebe.sample.fragment;

import phoebe.frame.R;
import phoebe.frame.fragment.BaseFragment;
import phoebe.frame.titlebar.TitleRes;
import phoebe.sample.activity.HttpDemoActivity;
import phoebe.sample.activity.JsonDemoActivity;
import phoebe.sample.activity.LoadingDemoActivity;
import phoebe.sample.activity.MeasureDemoActivity;
import phoebe.sample.activity.SystemBarActivity;
import phoebe.sample.activity.TitleDemoActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.home, container, false);
		super.onCreateView(inflater, container, savedInstanceState);
		return layout;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// title演示
		findViewById(R.id.home_title_sample).setOnClickListener(this);
		// 沉浸式状态栏
		findViewById(R.id.home_systembar_sample).setOnClickListener(this);
		// loading
		findViewById(R.id.home_loading_sample).setOnClickListener(this);
		// 网络请求
		findViewById(R.id.home_http_sample).setOnClickListener(this);
		// json解析
		findViewById(R.id.home_json_sample).setOnClickListener(this);
		// View onMeasure
		findViewById(R.id.home_measure_sample).setOnClickListener(this);

		TitleRes left = null;
		TitleRes middle = new TitleRes("首页标题");
		TitleRes right = null;

		getAppTitle().setTitle(left, middle, right);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_title_sample:
			startActivity(TitleDemoActivity.class);
			break;
		case R.id.home_systembar_sample:
			startActivity(SystemBarActivity.class);
			break;
		case R.id.home_loading_sample:
			startActivity(LoadingDemoActivity.class);
			break;
		case R.id.home_http_sample:
			startActivity(HttpDemoActivity.class);
			break;
		case R.id.home_json_sample:
			startActivity(JsonDemoActivity.class);
			break;
		case R.id.home_measure_sample:
			startActivity(MeasureDemoActivity.class);
			break;
		}

	}
}
