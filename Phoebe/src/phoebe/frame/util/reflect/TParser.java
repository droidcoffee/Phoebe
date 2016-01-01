package phoebe.frame.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

/**
 * 反射用到的核心基础工具类
 * 
 * @author coffee <br>
 *         2015-12-31 下午9:08:38
 */
public class TParser {
	/**
	 * 判断给定的field名是否存在于指定的class
	 * 
	 * @param fieldName
	 * @return
	 */
	public static boolean isField(Class<?> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			if (field != null) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 获取泛型的对象类型
	 * 
	 * @param type
	 *            java.util.ArrayList<com.xxx.bean.AddressListBean$AddressBean>
	 * @return com.xxx.bean.AddressListBean.class
	 */
	public static Class<?> type2Class(Type type) {
		//
		String typeStr = type.toString();
		String tmpStr = null;
		if (typeStr.indexOf("<") > 0) {
			tmpStr = typeStr.substring(typeStr.indexOf("<") + 1, typeStr.indexOf(">"));
		} else {
			// 非内部类 ???
			tmpStr = typeStr.substring(6);
		}
		Class<?> clazz = null;
		try {
			clazz = Class.forName(tmpStr);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	/**
	 * 获取某个字段的值
	 */
	public static <T> Object getValue(T obj, Field field) {
		try {
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object setValue(Object obj, String fieldName, Object value) {
		try {
			if (obj == null || value == null) {
				return obj;
			}
			Field field = obj.getClass().getDeclaredField(fieldName);
			if (field != null) {
				Object newVal = value;
				if (field.getType().isPrimitive()) {
					String type = field.getType().toString();
					if (type.contains("long")) {
						newVal = Long.valueOf(value + "");
					} else if (type.contains("int")) {
						newVal = Integer.valueOf(value + "");
					} else if (type.contains("float")) {
						newVal = Float.valueOf(value + "");
					} else if (type.contains("double")) {
						newVal = Double.valueOf(value + "");
					} else if (type.contains("boolean")) {
						newVal = Boolean.valueOf(value + "");
					}
				} else if (field.getType() == String.class) {
					newVal = String.valueOf(value);
				}
				String methodName = "set" + fieldName.substring(0, 1).toUpperCase(Locale.getDefault()) + fieldName.substring(1);
				Method method = obj.getClass().getMethod(methodName, new Class[] { field.getType() });
				method.invoke(obj, newVal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

}
