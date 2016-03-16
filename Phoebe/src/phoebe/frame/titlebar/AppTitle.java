package phoebe.frame.titlebar;

/**
 * AppTitle接口
 * 
 * @author coffee <br>
 *         2016-1-2 下午10:17:53
 */
public interface AppTitle {
	/**
	 * 初始化Title view
	 */
	public void initTitle();

	/**
	 * 设置app的title
	 * 
	 * @param leftTitle
	 * @param middleTitle
	 * @param rightTitle
	 */
	public AppTitle setTitle(TitleRes leftTitle, TitleRes middleTitle, TitleRes rightTitle);
}
