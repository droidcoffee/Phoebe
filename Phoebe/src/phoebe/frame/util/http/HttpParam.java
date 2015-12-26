package phoebe.frame.util.http;
public interface HttpParam {

	/**
	 * 默认不缓存数据<br>
	 * 如果缓存数据,则会保存返回的数据到json文件中
	 */
	final String cache = "cache";
	
	/**
	 * 是否是post请求(文本)
	 * 
	 */
	final String postText = "post";
	
	/**
	 * 是否是post请求(图片)
	 */
	final String postImage = "postImage";
	
	/**
	 * 是否需要toast网络不可用的提示
	 */
	final String toastNet = "toastNet";
}