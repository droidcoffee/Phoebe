package phoebe.frame.titlebar;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 如果Activity没有继承BaseActivity同时需要用到common_title, 则title的设置方法如下<br>
 * TitleMgr titleMgr = new TitleMgr(this); <br>
 * titleMgr.initTitle(); <br>
 * titleMgr.setTitle(new TitleRes[] { titleMgr.getBackTitle(), new TitleRes("xxx"), null });
 * 
 * @author coffee<br>
 *         2015-12-23下午1:56:43
 */
public class TitleRes {
	/**
	 * title类型可以是
	 */
	private int type;
	/**
	 * 支持 String | int <br>
	 * 其中 String 代表是文字<br>
	 * int可以是文字也可以是图片
	 */
	private Object resource;

	private View.OnClickListener clickListener;

	//
	public TitleRes(int type, Object resource, OnClickListener clickListener) {
		super();
		this.type = type;
		this.resource = resource;
		this.clickListener = clickListener;
	}

	/**
	 * 适用于标题是文字的。没单击事件
	 * 
	 * @param title
	 */
	public TitleRes(String title) {
		this.type = 0;
		this.resource = title;
		this.clickListener = null;
	}

	/**
	 * 适用于标题是文字的。没单击事件
	 * 
	 * @param title
	 */
	public TitleRes(int titleImage) {
		this.type = 1;
		this.resource = titleImage;
		this.clickListener = null;
	}

	/**
	 * 适用于title右侧文本单击事件
	 * 
	 * @param title
	 * @param clickListener
	 */
	public TitleRes(String title, OnClickListener clickListener) {
		this.type = 0;
		this.resource = title;
		this.clickListener = clickListener;
	}

	public TitleRes(int imageIcon, OnClickListener clickListener) {
		this.type = 1;
		this.resource = imageIcon;
		this.clickListener = clickListener;
	}

	// 
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getResource() {
		return resource;
	}

	public void setResource(Object resource) {
		this.resource = resource;
	}

	public View.OnClickListener getClickListener() {
		return clickListener;
	}

	public void setClickListener(View.OnClickListener clickListener) {
		this.clickListener = clickListener;
	}
}
