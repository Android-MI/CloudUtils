package com.bright.cloudutils.picture;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureUtils {
    public static Bitmap compressImage(String path, int size)
            throws IOException {
        Bitmap bitmap = null;
        InputStream is = new FileInputStream(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        is.close();
        int i = 0;
        while (true) {
            if ((options.outWidth >> i <= 640)
                    && (options.outHeight >> i <= 960)) {
                is = new FileInputStream(path);
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Config.ARGB_8888;
                options.inPurgeable = true;
                options.inInputShareable = true;
                bitmap = BitmapFactory.decodeStream(is, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static Bitmap getBitmapByPath(String path) {
        Bitmap bitmap = null;
        try {
            InputStream is = new FileInputStream(path);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap imageZoom(Bitmap bm, double maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        double mid = b.length / 1024;
        if (mid > maxSize) {
            double i = mid / maxSize;
            bm = zoomImage(bm, bm.getWidth() / Math.sqrt(i), bm.getHeight()
                    / Math.sqrt(i));
        }
        return bm;
    }

    public static Bitmap small(Bitmap bitmap) {
        Bitmap resizeBmp = null;
        if (bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.postScale(0.6f, 0.6f); // 长和宽放大缩小的比例
            resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        return resizeBmp;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 方法说明: 该方法就是对Bitmap形式的图片进行压缩,<br/>
     * 也就是通过设置采样率, 减少Bitmap的像素, 从而减少了它所占用的内存
     *
     * @param path
     * @return void
     * @Title: rotateImage
     */
    public static int getImageOrientation(String path) {

        try {
            ExifInterface exifInterface = new ExifInterface(path);

            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            return orientation;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ExifInterface.ORIENTATION_NORMAL;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix m = new Matrix();
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            m.setRotate(90);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            m.setRotate(180);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            m.setRotate(270);
        } else {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        try {
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
        } catch (OutOfMemoryError ooe) {

            m.postScale(1, 1);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);

        }
        return bitmap;
    }

    public static int readPictureDegree(String path) {
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
                default:
                    degree = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Log.e("旋转角度", "angle=" + angle);
        if (angle > 0) {

            // 创建新的图片
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return resizedBitmap;
        }
        return bitmap;
    }
}
