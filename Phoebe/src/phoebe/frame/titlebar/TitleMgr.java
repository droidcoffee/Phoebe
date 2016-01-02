package phoebe.frame.titlebar;

import phoebe.frame.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * 因为Fragment和Activity没有共同的父类 ，但是他们各自的父类里又有一些TitleBar相同的业务逻辑处理<br>
 * 所以我创建了该类
 * 
 * @author coffee <br>
 *         2015-12-24 下午10:26:10
 */
public class TitleMgr implements AppTitle {

	/**
	 * TitleBar的上下文
	 */
	private Context context;
	/**
	 * TitleBar所在的顶层View容易
	 */
	private View contentView;

	/**
	 * 三个Title
	 */
	private ViewSwitcher[] titleSwitcher;

	public TitleMgr(Context context, View contenView) {
		this.context = context;
		this.contentView = contenView;
	}

	private Resources getResources() {
		return context.getResources();
	}

	private View findViewById(int id) {
		return contentView.findViewById(id);
	}

	/**
	 * 初始化title bar
	 */
	@Override
	public void initTitle() {
		titleSwitcher = new ViewSwitcher[3];
		titleSwitcher[0] = (ViewSwitcher) findViewById(R.id.app_title_left_switcher);
		titleSwitcher[1] = (ViewSwitcher) findViewById(R.id.app_title_middle_switcher);
		titleSwitcher[2] = (ViewSwitcher) findViewById(R.id.app_title_right_switcher);
	}

	@Override
	public void setTitle(TitleRes leftTitle, TitleRes middleTitle, TitleRes rightTitle) {
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
}
