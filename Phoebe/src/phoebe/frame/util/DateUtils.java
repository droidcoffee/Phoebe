package phoebe.frame.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类 主要负责处理时间java.util.Date与String类型的转换
 * 
 * @author coffee <br>
 *         2015-12-31 下午8:22:36
 */
public class DateUtils {
	// private static Logger log = Logger.getLogger(DateUtils.class.toString());
	/**
	 * 默认的格式化格式为 yyyy-MM-dd HH:mm:ss
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

	public static SimpleDateFormat getDateFormat(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		return sdf;
	}

	/**
	 * 格式化日期类型 ，返回字符串
	 * 
	 * @param : 传入参数
	 * @return 返回一个字符串
	 */
	public static String format(Object value) {
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解析字符串,返回日期
	 * 
	 * @param
	 * @return 返回日期
	 */
	public static Date parse(String value) {
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.parse(value);
		} catch (Exception e) {
			try {
				sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
				sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
				return sdf.parse(value.toString());
			} catch (Exception ex) {
				try {
					sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
					sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
					return sdf.parse(value.toString());
				} catch (Exception exc) {
					exc.printStackTrace();
					// log.warning("不能格式化指定的值: " + value);
				}
			}
			return null;
		}
	}

	// 获取当前日期-时间
	public String getCurDateTime(String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 是否是今天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(final Date date) {
		Date today = new Date();
		return date.getTime() >= dayBegin(today).getTime() //
				&& date.getTime() <= dayEnd(today).getTime();
	}

	/**
	 * 获取指定时间的那天 00:00:00.000 的时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date dayBegin(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取指定时间的那天 23:59:59.999 的时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date dayEnd(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
}
