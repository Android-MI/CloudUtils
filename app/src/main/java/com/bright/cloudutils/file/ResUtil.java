package com.bright.cloudutils.file;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Field;

/**
 * 此工具类提供方法，获取其他包下的资源文件。
 * 
 * @ClassName ResourceUtil
 * @Description 方法名：<br/>
 *              getLayoutId() 获取布局文件 ; <br/>
 *              getStringId() 获取String资源文件 ;<br/>
 *              getDrawableId() 获取Drawable文件 ;<br/>
 *              getStyleId() 获取Style文件 ;<br/>
 *              getId() 获取ID文件 ;<br/>
 *              getColorId 获取颜色资源文件 <br/>
 * @author CollCloud_小米
 */
public class ResUtil {

	/**
	 * 获取布局文件
	 * 
	 * @param paramContext
	 *            当前上下文环境
	 * @param paramString
	 *            资源名称
	 * @return int 返回0则没有获取成功，大于0则表示成功。
	 */
	public static int getLayoutId(Context paramContext, String paramString) {
		int res = 0;
		try {
			res = paramContext.getResources().getIdentifier(paramString,
					"layout", paramContext.getPackageName());
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
		return res;
	}

	// public static int getIdByName(Context context, String className, String
	// name) {
	// String packageName = context.getPackageName();
	// Class r = null;
	// int id = 0;
	// try {
	// r = Class.forName(packageName + ".R");
	// Class[] classes = r.getClasses();
	// Class desireClass = null;
	// for (int i = 0; i < classes.length; ++i) {
	// if (classes[i].getName().split("\\$")[1].equals(className)) {
	// desireClass = classes[i];
	// break;
	// }
	// }
	// if (desireClass != null)
	// id = desireClass.getField(name).getInt(desireClass);
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (NoSuchFieldException e) {
	// e.printStackTrace();
	// }
	// return id;
	// }

	/**
	 * 获取String资源文件
	 * 
	 * @param paramContext
	 *            当前上下文环境
	 * @param paramString
	 *            资源名称
	 * @return int 返回0则没有获取成功，大于0则表示成功。
	 */
	public static int getStringId(Context paramContext, String paramString) {
		int res = 0;
		try {
			res = paramContext.getResources().getIdentifier(paramString,
					"string", paramContext.getPackageName());
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 获取Drawable文件
	 * 
	 * @param paramContext
	 *            当前上下文环境
	 * @param paramString
	 *            资源名称
	 * @return int 返回0则没有获取成功，大于0则表示成功。
	 */
	public static int getDrawableId(Context paramContext, String paramString) {
		int res = 0;
		try {
			res = paramContext.getResources().getIdentifier(paramString,
					"drawable", paramContext.getPackageName());
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 获取Style文件
	 * 
	 * @param paramContext
	 *            当前上下文环境
	 * @param paramString
	 *            资源名称
	 * @return int 返回0则没有获取成功，大于0则表示成功。
	 */
	public static int getStyleId(Context paramContext, String paramString) {
		int res = 0;
		try {
			res = paramContext.getResources().getIdentifier(paramString,
					"style", paramContext.getPackageName());
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 获取ID文件
	 * 
	 * @param paramContext
	 *            当前上下文环境
	 * @param paramString
	 *            资源名称
	 * @return int 返回0则没有获取成功，大于0则表示成功。
	 */
	public static int getId(Context paramContext, String paramString) {
		int res = 0;
		try {
			res = paramContext.getResources().getIdentifier(paramString, "id",
					paramContext.getPackageName());
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 获取颜色资源文件
	 * 
	 * @param paramContext
	 *            当前上下文环境
	 * @param paramString
	 *            资源名称
	 * @return int 返回0则没有获取成功，大于0则表示成功。
	 */
	public static int getColorId(Context paramContext, String paramString) {
		int res = 0;
		try {
			res = paramContext.getResources().getIdentifier(paramString,
					"color", paramContext.getPackageName());
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
		return res;
	}

	public static int getAnimId(Context paramContext, String paramString) {
		int res = 0;
		try {
			res = paramContext.getResources().getIdentifier(paramString,
					"anim", paramContext.getPackageName());
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String getString(Context context, String paramString) {
		String string = "";
		string = context.getResources().getString(
				getStringId(context, paramString));
		return string;
	}

	public static Drawable getDrawable(Context context, String paramString) {
		Drawable drawable = null;
		drawable = context.getResources().getDrawable(
				getDrawableId(context, paramString));
		return drawable;
	}

	/**
	 * 对于 context.getResources().getIdentifier 无法获取的数据 , 或者数组 资源反射值
	 * 
	 * @paramcontext
	 * @param name
	 * @param type
	 * @return
	 */
	public static Object getResourceId(Context context, String name, String type) {
		String className = context.getPackageName() + ".R";

		try {
			Class<?> cls = Class.forName(className);
			for (Class<?> childClass : cls.getClasses()) {
				String simple = childClass.getSimpleName();
				if (simple.equals(type)) {
					for (Field field : childClass.getFields()) {
						String fieldName = field.getName();
						if (fieldName.equals(name)) {
							return field.get(fieldName);
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
	 * context.getResources().getIdentifier 无法获取到 styleable 的数据
	 * 
	 * @paramcontext
	 * @param name
	 * @return
	 */
	public static int getStyleableId(Context context, String name) {
		return ((Integer) getResourceId(context, name, "styleable")).intValue();

	}
	public static int getAttrId(Context context, String name) {
		return ((Integer) getResourceId(context, name, "attr")).intValue();
		
	}

	/**
	 * 
	 * 获取 styleable 的 ID 号数组
	 * 
	 * @paramcontext
	 * 
	 * @param name
	 * 
	 * @return
	 */

	public static int[] getStyleableArray(Context context, String name) {

		return (int[]) getResourceId(context, name, "styleable");

	}

}
