package phoebe.frame.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import phoebe.frame.util.Log;
import phoebe.frame.util.annotation.Column;

/**
 * Json解析工具
 * 
 * @author coffee <br>
 *         2015-12-31 下午9:09:08
 */
public class JsonParser extends TParser{

	/**
	 * 默认按照 {@link Column解析}
	 */
	private boolean byColumn = true;

	/**
	 * k : class type <br>
	 * v (k : 列(column); v: 字段)
	 */
	private static Hashtable<Class<?>, Map<String, Field>> cache = new Hashtable<Class<?>, Map<String, Field>>();

	/**
	 * 列明(非字段名)-字段
	 * 
	 * @param beanClass
	 * @return
	 */
	private synchronized Map<String, Field> getColumnMap(Class<?> beanClass) {
		if (cache.get(beanClass) == null || cache.get(beanClass).size() == 0) {
			cache.put(beanClass, new HashMap<String, Field>());
			for (Field field : beanClass.getDeclaredFields()) {
				String columnName = "";
				if (byColumn) {
					Column column = field.getAnnotation(Column.class);
					if (column != null) {
						if (!"".equals(column.json())) {
							columnName = column.json();
						} else {
							columnName = column.name();
						}
					} else {
						columnName = field.getName();
					}
				} else {
					columnName = field.getName();
				}
				cache.get(beanClass).put(columnName, field);
			}
		}
		return cache.get(beanClass);
	}

	/**
	 * 获取某个属性的值
	 * 
	 * @param jsonStr
	 * @param attr
	 *            支持attr[attr[attr]]的格式
	 */
	public static Object get(String jsonStr, String attrs) {
		try {
			JSONObject json = new JSONObject(jsonStr);
			String[] seg = attrs.replace("]", "").split("\\[");
			if (seg.length == 1) {
				String attr = seg[0];
				if (json.has(attr)) {
					Object value = json.get(attr);
					return value;
				} else {
					return null;
				}
			} else {
				for (int i = 0; i < seg.length; i++) {
					String attr = seg[i];
					if (json.has(attr)) {
						Object value = json.get(attr);
						if (i < seg.length) {
							return get(value.toString(), attr);
						} else {
							return value;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 不按照 {@link Column}注解解析
	 * 
	 * @param jsonStr
	 * @param attr
	 * @param beanClass
	 * @return
	 */
	public <T> T parseWithNoColumn(String jsonStr, Class<T> beanClass) {
		this.byColumn = false;
		return parse(jsonStr, beanClass);
	}

	/**
	 * 不按照 {@link Column}注解解析
	 * 
	 * @param jsonStr
	 * @param attr
	 * @param beanClass
	 * @return
	 */
	public <T> List<T> parseListWithNoColumn(String jsonStr, String attr, Class<T> beanClass) {
		this.byColumn = false;
		return parseList(jsonStr, attr, beanClass);
	}

	public <T> List<T> parseList(String jsonStr, Class<T> beanClass) {
		try {
			if (jsonStr == null || jsonStr.trim().length() == 0) {
				return null;
			}
			JSONArray json = new JSONArray(jsonStr);
			List<T> arrayObj = parseJsonArray(json, beanClass);
			return arrayObj;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> List<T> parseList(String jsonStr, String attr, Class<T> beanClass) {
		try {
			if (jsonStr == null || jsonStr.trim().length() == 0) {
				return null;
			}
			JSONObject json = new JSONObject(jsonStr);
			if (json.has(attr)) {
				List<T> arrayObj = parseJsonArray(json.getJSONArray(attr), beanClass);
				return arrayObj;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将指定json字符串的指定节点的内容转换成beanClass对象
	 * 
	 * @param jsonStr
	 * @param attr
	 * @param beanClass
	 * @return
	 */
	public <T> T parse(String jsonStr, String attr, Class<T> beanClass) {
		try {
			if (jsonStr == null || jsonStr.trim().length() == 0) {
				return null;
			}
			JSONObject json = new JSONObject(jsonStr);
			if (json.has(attr)) {
				JSONObject attrObj = json.getJSONObject(attr);
				T t = parse(attrObj, beanClass);
				return t;
			} else {
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将指定的Json字符串解析成beanClass对象
	 * 
	 * @param jsonStr
	 * @param beanClass
	 * @return
	 */
	public <T> T parse(String jsonStr, Class<T> beanClass) {
		try {
			if (jsonStr == null || jsonStr.trim().length() == 0) {
				return null;
			}
			Log.d("json:parse", beanClass + "---enter---");
			JSONObject json = new JSONObject(jsonStr);
			return parse(json, beanClass);
		} catch (Exception e) {
			// e.printStackTrace();
			Log.w("json:parser", e.getMessage(), e);
		} finally {
			Log.d("json:parse", beanClass + "---end---");
			// 清空缓存
			// cache.remove(beanClass);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T parse(JSONObject json, Class<T> beanClass) {
		try {
			T obj = beanClass.newInstance();
			Map<String, Object> items = new HashMap<String, Object>();
			for (Iterator<String> it = json.keys(); it.hasNext();) {
				String key = it.next();
				items.put(key, json.get(key));
			}
			for (String columnName : getColumnMap(beanClass).keySet()) {
				Field field = cache.get(beanClass).get(columnName);
				Object value = items.get(columnName);
				// 创建ArrayList
				if (field.getType() == ArrayList.class || field.getType() == List.class) {
					try {
						if (json.has(columnName)) {
							Type arrType = field.getGenericType();
							Class<?> arrClass = type2Class(arrType);
							Object arrayObj = parseJsonArray(json.getJSONArray(columnName), arrClass);
							field.setAccessible(true);
							field.set(obj, arrayObj);
						}
					} catch (JSONException e) {
						// json.getJSONArray 如果元素不存在可能会抛异常
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (field.getType() == Map.class) {
					// json 转换为map
					Type mapMainType = field.getGenericType();
					ParameterizedType parameterizedType = (ParameterizedType) mapMainType;
					Type[] types = parameterizedType.getActualTypeArguments();
					JSONObject mapJson = json.getJSONObject(columnName);
					Class<?> keyClass = type2Class(types[0]);
					Class<?> valueClass = type2Class(types[1]);

					Object map = parse(mapJson, keyClass, valueClass);
					setValue(obj, field.getName(), map);
				} else {
					if (value != null) {
						// 递归
						if (value instanceof JSONObject) {
							value = parse((JSONObject) value, field.getType());
							setValue(obj, field.getName(), value);
						}
						// JSONArray 转化为 String[]
						else if (value instanceof JSONArray) {
							JSONArray arr = (JSONArray) value;
							List<String> list = new ArrayList<String>();
							for (int i = 0; i < arr.length(); i++) {
								list.add(arr.getString(i));
							}
							setValue(obj, field.getName(), list.toArray(new String[] {}));
						} else {
							setValue(obj, field.getName(), value);
						}
					}
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private <T> List<T> parseJsonArray(JSONArray jsonArr, Class<T> beanClass) {
		List<T> lst = new ArrayList<T>();
		for (int i = 0; i < jsonArr.length(); i++) {
			try {
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				lst.add(parse(jsonObj, beanClass));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return lst;
	}

	@SuppressWarnings("unchecked")
	private <K> Map<String, K> parse(JSONObject mapJson, Class<?> keyClass, Class<K> valueClass) {
		Iterator<String> keys = mapJson.keys();
		Map<String, K> map = new HashMap<String, K>();
		while (keys.hasNext()) {
			String key = keys.next();
			K p = null;
			try {
				p = (K) mapJson.get(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			map.put(key, p);
		}
		return map;
	}

	/**
	 * map转化成json字符串
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T, V> String toJson(Map<T, V> map) {
		if (map.size() == 0) {
			return "{}";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (Iterator<T> it = map.keySet().iterator(); it.hasNext();) {
			T key = it.next();
			if (key instanceof Number) {
				sb.append(key);
			} else {
				sb.append("\"").append(key).append("\"");
			}
			sb.append(":");
			V val = map.get(key);
			if (val instanceof Map) {
				sb.append(toJson((Map<T, V>) val));
			} else if (val instanceof Number) {
				sb.append(val);
			} else {
				sb.append("\"").append(val).append("\"");
			}
			if (it.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
