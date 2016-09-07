package com.bright.cloudutils.dpi;

import android.content.Context;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * 屏幕密码，单位转换工具集
 *
 * @author CollCloud_小米
 * @ClassName DPIUtil
 * @Description
 */
public class DPIUtil {
    private static Display defaultDisplay;
    private static float mDensity = 160.0F;

    /**
     * 获取屏幕密度
     *
     * @Title getDensity
     */
    public static float getDensity() {
        return mDensity;
    }

    /**
     * 获取高度
     */
    @SuppressWarnings("deprecation")
    public static int getHeight(Context context) {
        return getDefaultDisplay(context).getHeight();
    }

    /**
     * 获取宽度
     */
    @SuppressWarnings("deprecation")
    public static int getWidth(Context context) {
        return getDefaultDisplay(context).getWidth();
    }

    public static Display getDefaultDisplay(Context context) {
        if (defaultDisplay == null)
            defaultDisplay = ((WindowManager) context
                    .getSystemService("window")).getDefaultDisplay();
        return defaultDisplay;
    }

    public static int dip2px(float paramFloat) {
        return (int) (0.5F + paramFloat * mDensity);
    }

    public static int px2dip(Context paramContext, float paramFloat) {
        return (int) (0.5F + paramFloat / mDensity);
    }

    public static int px2sp(Context paramContext, float paramFloat) {
        return (int) (0.5F + paramFloat
                / paramContext.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 设定屏幕密度
     *
     * @param paramFloat 密度值
     */
    public static void setDensity(float paramFloat) {
        mDensity = paramFloat;
    }
}
