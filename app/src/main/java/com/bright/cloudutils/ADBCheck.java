package com.bright.cloudutils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

/**
 * USB调试模式
 * 
 * @ClassName: ADBCheck
 * @Description: 判断程序是否开启 USB调试模式。
 * @author CollCloud_小米
 */
public class ADBCheck {
	private static final int ENABLE = 1;
	private static final int DISABLE = 0;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static boolean isADBEnabled(ContentResolver contentResolver) {
		boolean result = true;
		try {
			int adbState = -1;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				adbState = Settings.Global.getInt(contentResolver,
						Settings.Global.ADB_ENABLED);
			} else {
				adbState = Settings.Secure.getInt(contentResolver,
						Settings.Secure.ADB_ENABLED);
			}
			switch (adbState) {
			case ENABLE:
				result = true;
				break;
			case DISABLE:
				result = false;
				break;
			default:
				result = false;
				break;
			}
		} catch (SettingNotFoundException e) {
		}
		if (!isDebuggable()) {
			return result;
		} else {
			return false;
		}
	}

	public static boolean isDebuggable() {
		return BuildConfig.DEBUG;
	}
}
