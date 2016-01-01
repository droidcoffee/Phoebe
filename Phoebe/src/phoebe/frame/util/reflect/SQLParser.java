package phoebe.frame.util.reflect;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import phoebe.frame.util.annotation.Bean;
import phoebe.frame.util.annotation.Column;
import phoebe.frame.util.annotation.Id;
import phoebe.frame.util.annotation.Transient;
import android.database.Cursor;

/**
 * 数据库解析工具
 * 
 * @author coffee <br>
 *         2015-12-31 下午9:08:38
 */
public class SQLParser extends TParser {

	private static Logger logger = Logger.getLogger("sqlite");

	static {
		logger.setLevel(Level.INFO);
	}

	/**
	 * 生成建表语句
	 * 
	 * @param <T>
	 * @param beanClass
	 * @return
	 */
	public static <T> String generateTableSql(Class<T> beanClass) {
		Field[] fields = beanClass.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS " + getTableName(beanClass) + "(\n");
		for (Field field : fields) {
			Transient nullMap = field.getAnnotation(Transient.class);
			if (nullMap != null) {
				continue;
			}
			// 非基本数据类型 ， 也非String类型
			if (!field.getType().isPrimitive() && field.getType() != String.class) {
				continue;
			}
			Column column = field.getAnnotation(Column.class);
			String columnName = field.getName();
			if (column != null && !"".equals(column.name())) {
				columnName = column.name();
			}
			sql.append("\t" + columnName);
			switch (getMappedType(field.getType())) {
			case Type.Integer:
			case Type.Long:
				sql.append(" INTEGER");
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					sql.append(" PRIMARY KEY");
					if (id != null && id.isAuto()) {
						sql.append(" AUTOINCREMENT ");
					}
				}
				break;
			case Type.Float:
				sql.append(" FLOAT ");
				break;
			case Type.Date:
				sql.append(" DATETIME");
				break;
			case Type.String:
				int len = 255;
				sql.append(" VARCHAR(" + len + ")");
				break;
			}
			sql.append(",\n");
		}
		sql.deleteCharAt(sql.length() - 2);
		sql.append(")\n");
		return sql.toString();
	}

	/**
	 * 获取表名
	 */
	public static <T> String getTableName(Class<T> beanClass) {
		Bean bean = beanClass.getAnnotation(Bean.class);
		if (bean != null) {
			return bean.name();
		} else {
			return beanClass.getSimpleName();
		}
	}

	/**
	 * 获取列名
	 */
	public static <T> String getColumnName(Class<T> clazz, Field field) {
		Column column = field.getAnnotation(Column.class);
		if (column != null && column.name().length() > 0) {
			return column.name();
		} else {
			return field.getName();
		}
	}

	/**
	 * 判断指定class的prop是否被映射到数据库
	 * 
	 * @return 如果被映射，返回true ； 没被映射， 即 nullMap != null 返回false
	 */
	public static <T> boolean isTransient(Class<T> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			Transient nullMap = field.getAnnotation(Transient.class);
			if (nullMap != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 将ResultSet组装成List
	public static <T> ArrayList<T> processResultSetToList(Cursor rs, Class<T> clazz) throws Exception {
		ArrayList<T> ls = new ArrayList<T>();
		Field[] fields = clazz.getDeclaredFields();
		while (rs.moveToNext()) {
			T tt = clazz.newInstance();
			for (Field field : fields) {
				try {
					if (isTransient(clazz, field.getName())) {
						continue;
					}
					String columnName = getColumnName(clazz, field);
					Object value = null;
					int columnIndex = rs.getColumnIndex(columnName);
					try {
						switch (getMappedType(field.getType())) {
						case Type.Long:
							value = Long.valueOf(rs.getLong(columnIndex));
							break;
						case Type.Integer:
							value = Integer.valueOf(rs.getInt(columnIndex));
							break;
						default:
							value = rs.getString(columnIndex);
							break;
						}
					} catch (Exception e) {// 如果仅仅查询Class的部分字段
						if (e.getMessage().matches("Column\\s+'.+?'\\s+not\\s+found.")) {
							switch (getMappedType(field.getType())) {
							case Type.Long:
							case Type.Integer:
								value = 0;
								break;
							default:
								value = null;
								break;
							}
						}
					}
					setValue(tt, field.getName(), value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
			ls.add(tt);
		}
		return ls;
	}

	// 单列查询
	public static List<String> processToStringList(Cursor rs) {
		List<String> lst = new ArrayList<String>();
		while (rs.moveToNext()) {
			String val = rs.getString(0);
			lst.add(val);
		}
		rs.close();
		return lst;
	}

	/**
	 * 判断某Class的某字段是不是主键 如果该主键必须被Id属性注解
	 * 
	 * @param entityClass
	 *            : 实体类
	 * @param fieldName
	 *            : 字段名
	 */
	public static <T> boolean isPrimaryKey(Class<T> entityClass, Field field) {
		try {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断主键是否是自增类型的
	 * 
	 * @param entityClass
	 *            : 实体类
	 * @param fieldName
	 *            : 字段名
	 */
	public static <T> boolean isGenerationTypeAuto(Field field) {
		try {
			Id id = field.getAnnotation(Id.class);
			if (id != null && id.isAuto()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 返回更新实体的命令语句<br>
	 * 支持多主键
	 * 
	 * @param <T>
	 * @param t
	 */
	public static <T> String getUpdateSql(T t) throws Exception {
		StringBuffer sql = new StringBuffer("update ").append(SQLParser.getTableName(t.getClass())).append(" set ");
		long id = 0;
		// 主键值
		ArrayList<Long> values = new ArrayList<Long>();
		// 主键名
		ArrayList<String> pks = new ArrayList<String>();
		Class<?> clazz = t.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Transient nullMap = fields[i].getAnnotation(Transient.class);
			if (nullMap != null) {
				continue;
			}
			String columnName = getColumnName(clazz, fields[i]);
			Object value = getValue(t, fields[i]);
			if (SQLParser.isPrimaryKey(t.getClass(), fields[i])) {
				if (value != null) {
					id = Long.valueOf(value.toString());
					values.add(id);
					pks.add(columnName);
				}
				continue;
			} else {
				if (value == null) {
					continue;// 忽略空值，如果想赋null值， 字符串写成 fieldName=""; 数值型
								// fieldName=0
				}
				switch (getMappedType(fields[i].getType())) {
				case Type.Integer:
				case Type.Long:
				case Type.Float:
				case Type.Double:
					sql.append(columnName).append("=").append(value);
					break;
				case Type.Date:
					value = phoebe.frame.util.DateUtils.format(value.toString());
					sql.append(columnName).append("='").append(value).append("'");
					break;
				case Type.String:
					sql.append(columnName).append("='").append(value).append("'");
					break;
				default:
					continue;
				}
				if (value != null && fields.length > i) {
					sql.append(",");
				}
			}
		}
		if (sql.toString().endsWith(",")) {// 除去末尾的 ,
			sql.deleteCharAt(sql.length() - 1);
		}
		sql.append(" where ");
		for (int i = 0; i < pks.size(); i++) {
			sql.append(pks.get(i)).append("=").append(values.get(i));
			if (i + 1 < pks.size()) {
				sql.append(" and ");
			}
		}
		return sql.toString();
	}

	// 获取插入记录的sql语句
	public static <T> String getInsertSql(T t) throws Exception {
		StringBuffer sql = new StringBuffer("insert into ").append(SQLParser.getTableName(t.getClass())).append(" ");

		Field[] fields = t.getClass().getDeclaredFields();
		// k-v 映射的column名字 : 属性 LinkedHashMap 按照插入的顺序排序
		Map<String, Field> propMap = new LinkedHashMap<String, Field>();
		sql.append("(");

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Transient nullMap = field.getAnnotation(Transient.class);
			// this$开头的是内部类的外部类对象
			if (nullMap != null || field.getName().startsWith("this$")) {
				continue;
			}
			// 非基本数据类型 ， 也非String类型
			// if (!field.getType().isPrimitive() && field.getType() !=
			// String.class) {
			// continue;
			// }
			Column column = field.getAnnotation(Column.class);
			String columnName = field.getName();
			if (column != null && !"".equals(column.name())) {
				columnName = column.name();
			}
			sql.append(columnName);
			propMap.put(columnName, fields[i]);
			if (i + 1 < fields.length) {
				sql.append(",");
			}
		}
		if (sql.toString().endsWith(",")) {
			sql.delete(sql.length() - 1, sql.length());
		}
		sql.append(")values(");
		for (String column : propMap.keySet()) {
			Field field = propMap.get(column);
			if (isGenerationTypeAuto(field)) {
				sql.append("null");
			} else {
				Object fieldValue = getValue(t, field);
				switch (getMappedType(field.getType())) {
				case Type.Integer:
				case Type.Long:
				case Type.Float:
				case Type.Double:
					sql.append(fieldValue);
					break;
				case Type.Date:
					fieldValue = phoebe.frame.util.DateUtils.format(fieldValue);
					sql.append(null == fieldValue ? "null" : "'" + fieldValue + "'");
					break;
				case Type.String:
					sql.append(null == fieldValue ? "null" : "'" + fieldValue + "'");
					break;
				default:
					continue;
				}
			}
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);// 除去sql语句后面最后一个 逗号
		sql.append(")");
		return sql.toString();
	}

	/**
	 * ; 解析日期，返回string
	 */
	public static String parseDate(Object value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		try {
			return sdf.format(value);
		} catch (Exception e) {
			e.printStackTrace();
			// 如果发生异常则回返null
			return null;
		}
	}

	public interface Type {
		byte Byte = 1;
		byte Character = 2;
		byte Short = 3;
		byte Integer = 4;
		byte Long = 5;
		byte Float = 6;
		byte Double = 7;
		byte Boolean = 8;
		byte String = 9;
		byte Date = 10;
		byte Object = 11;
	};

	/**
	 * 支持基本数据类型以及其封装类型
	 * 
	 * @param clazz
	 *            ： 传入 field.getType对象
	 */
	public static int getMappedType(Class<?> fieldType) {
		String type = fieldType.getSimpleName().toLowerCase(Locale.getDefault());
		if (type.contains("long")) {
			return Type.Long;
		} else if (type.contains("int")) {
			return Type.Integer;
		} else if (type.contains("float")) {
			return Type.Float;
		} else if (type.contains("double")) {
			return Type.Double;
		} else if (type.contains("date")) {
			return Type.Date;
		} else if (type.contains("string")) {
			return Type.String;
		}
		return Type.Object;
	}

}
