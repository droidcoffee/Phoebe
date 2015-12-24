package phoebe.frame.fragment;

import phoebe.frame.R;
import phoebe.frame.titlebar.TitleRes;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class BaseFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		return null;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		
	}
	
	
	private ViewSwitcher[] titleSwitcher;

	/**
	 * 初始化View
	 */
	protected void findViewById() {
		titleSwitcher = new ViewSwitcher[3];
		titleSwitcher[0] = (ViewSwitcher) findViewById(R.id.app_title_left_switcher);
		titleSwitcher[1] = (ViewSwitcher) findViewById(R.id.app_title_middle_switcher);
		titleSwitcher[2] = (ViewSwitcher) findViewById(R.id.app_title_right_switcher);
	}

	protected void setTitle(TitleRes leftTitle, TitleRes middleTitle, TitleRes rightTitle) {
		TitleRes[] reses = new TitleRes[] { leftTitle, middleTitle, rightTitle };
		for (int i = 0; i < reses.length; i++) {
			TitleRes res = reses[i];
			ViewSwitcher switcher = titleSwitcher[i];
			if (res == null) {
				switcher.setVisibility(View.INVISIBLE);
			} else {
				switcher.setVisibility(View.VISIBLE);
				// 触发单击事件的View
				View clickView = null;
				// 文字
				if (res.getType() == 0) {
					switcher.setDisplayedChild(0);// 显示TextView
					//
					Object resource = res.getResource();
					String title = resource instanceof Integer ? getResources().getString((Integer) resource) : String.valueOf(resource);
					((TextView) switcher.getChildAt(0)).setText(title);
					clickView = switcher.getChildAt(0);
				} else {
					switcher.setDisplayedChild(1);// 显示ImageView
					//
					int imageResource = (Integer) res.getResource();
					((ImageView) switcher.getChildAt(1)).setImageResource(imageResource);
					clickView = switcher.getChildAt(1);
				}
				// 设置title的单击事件
				clickView.setOnClickListener(res.getClickListener());
			}
		}
	}

	
	protected View findViewById(int id){
		return getView().findViewById(id);
	}
}
