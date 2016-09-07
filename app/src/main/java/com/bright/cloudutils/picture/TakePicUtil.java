package com.bright.cloudutils.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理工具类
 */
public class TakePicUtil {
    @SuppressWarnings("unused")
    private Context context;

    public TakePicUtil(Context context) {
        this.context = context;
    }

    /**
     * 压缩图片并转码
     *
     * @param bitmap  原图
     * @param maxSize 压缩到的最大值 kb
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap, double maxSize) {
        // public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                int options = 100;
                while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于maxSize
                    // kb,大于继续压缩
                    baos.reset();// 重置baos即清空baos
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                    options -= 5;// 每次都减少5
                }

                // ByteArrayInputStream isBm = new ByteArrayInputStream(
                // baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
                // Bitmap bitmapC = BitmapFactory.decodeStream(isBm, null,
                // null);//把ByteArrayInputStream数据生成图片
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 文件转为base64
     *
     * @param filePath
     */
    public static String fileToBase64(String filePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;

        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return Base64.encodeToString(data, Base64.DEFAULT);// 返回Base64编码过的字节数组字符串
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static String zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        ByteArrayOutputStream baos = null;
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        if (bitmap != null) {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bitmapBytes = baos.toByteArray();
        String result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        return result;
    }

    /**
     * 按大小设置图片
     *
     * @param pathName     图片路径
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @return
     */
    public static Bitmap compressBySize(String pathName, int targetWidth,
                                        int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }

    /**
     * 压缩图片
     *
     * @param filePath 图片存储路径
     */
    public static Bitmap getSmallBitmap(String filePath) {

        Bitmap bm = compressBySize(filePath, 320, 480);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;

    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 255, 255, 255);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 合成两个bitmap
     *
     * @param foreground
     * @return
     */
    // public Bitmap toConformBitmap(Bitmap foreground) {
    // Drawable back = activity.getResources().getDrawable(
    // R.drawable.bitmap_withe);
    // Bitmap background = Utils.drawableToBitmap(back);
    // if (background == null || foreground == null) {
    // return null;
    // }
    //
    // int fgWidth = foreground.getWidth();
    // int fgHeight = foreground.getHeight();
    // // create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
    // Bitmap newbmp = Bitmap
    // .createBitmap(fgWidth, fgHeight, Config.ARGB_8888);
    // Canvas cv = new Canvas(newbmp);
    // // draw bg into
    // cv.drawBitmap(background, 0, 0, null);// 在 0，0坐标开始画入bg
    // // draw fg into
    // cv.drawBitmap(foreground, 0, 0, null);// 在 0，0坐标开始画入fg ，可以从任意位置画入
    // // save all clip
    // cv.save(Canvas.ALL_SAVE_FLAG);// 保存
    // // store
    // cv.restore();// 存储
    // return newbmp;
    // }

    // public static Bitmap zoomImageToBmp(Context context, double newWidth,
    // double newHeight, int maxSize, String strName, Uri path) {
    // Bitmap photo = null;
    // BitmapFactory.Options opts = new BitmapFactory.Options();
    //
    // if (!CommonUtil.isEmptyOrNull(path)) {
    // try {
    //
    // // ContentResolver resolver = context.getContentResolver();
    // // photo = MediaStore.Images.Media.getBitmap(resolver, path);
    //
    // ContentResolver resolver = context.getContentResolver();
    // String[] filePathColumn = { MediaStore.Images.Media.DATA };
    // Cursor cursor = resolver.query(path, filePathColumn, null,
    // null, null);
    // cursor.moveToFirst();
    // int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
    // String picturePath = cursor.getString(columnIndex);
    // cursor.close();
    //
    // InputStream is = new FileInputStream(picturePath);
    // opts.inTempStorage = new byte[100 * 1024];
    // opts.inPreferredConfig = Bitmap.Config.RGB_565;
    // opts.inSampleSize = 4;
    // opts.inInputShareable = true;
    // photo = BitmapFactory.decodeStream(is, null, opts);
    //
    // } catch (IOException e) {
    // e.printStackTrace();
    // return null;
    // }
    //
    // } else if (!CommonUtil.isEmptyOrNull(strName)) {
    // // photo = BitmapFactory.decodeFile(FileUtil.getDefultPath() +
    // // strName);
    // // int degree = readPictureDegree(FileUtil.getDefultPath() +
    // // strName);
    // // photo = rotateBitmap(photo, degree);
    // try {
    // InputStream is = new FileInputStream(FileUtil.getDefultPath()
    // + strName);
    // opts.inTempStorage = new byte[100 * 1024];
    // opts.inPreferredConfig = Bitmap.Config.RGB_565;
    // opts.inSampleSize = 4;
    // opts.inInputShareable = true;
    // photo = BitmapFactory.decodeStream(is, null, opts);
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // return null;
    // }
    //
    // } else {
    // return null;
    // }
    //
    // ByteArrayOutputStream baos = null;
    // // 获取这个图片的宽和高
    // float width = photo.getWidth();
    // float height = photo.getHeight();
    // // 创建操作图片用的matrix对象
    // Matrix matrix = new Matrix();
    // // 计算宽高缩放率
    // float scaleWidth = ((float) newWidth) / width;
    // // float scaleHeight = ((float) newHeight) / height;
    // // 缩放图片动作
    // matrix.postScale(scaleWidth, scaleWidth);
    // Bitmap bitmap = Bitmap.createBitmap(photo, 0, 0, (int) width,
    // (int) height, matrix, true);
    // if (bitmap != null) {
    // baos = new ByteArrayOutputStream();
    // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    //
    // int options = 90;
    // while (baos.toByteArray().length / 1024 > maxSize) { //
    // 循环判断如果压缩后图片是否大于maxSize
    // // kb,大于继续压缩
    // baos.reset();// 重置baos即清空baos
    // bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//
    // 这里压缩options%，把压缩后的数据存放到baos中
    // options -= 10;// 每次都减少10
    // }
    // }
    // File f = new File(FileUtil.getDefultPath(), strName);
    // if (f.exists()) {
    // f.delete();
    // }
    // try {
    // FileOutputStream out = new FileOutputStream(f);
    // // src.compress(Bitmap.CompressFormat.PNG, 100, out);
    // out.write(baos.toByteArray());
    // out.flush();
    // out.close();
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // return bitmap;
    // }
}
