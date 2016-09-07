package com.bright.cloudutils.picture;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * BitMap Utils
 * 
 * @ClassName BitmapUtils
 * @Description
 * @author CollCloud_小米
 */

@SuppressWarnings("unused")
public class BitmapUtils {
	/**
	 * bitmap To base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream byteArrayoutStream = null;
		try {
			if (bitmap != null) {
				byteArrayoutStream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
						byteArrayoutStream);

				byteArrayoutStream.flush();
				byteArrayoutStream.close();

				byte[] bitmapBytes = byteArrayoutStream.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);

			}
		} catch (IOException e) {
		} finally {
			try {
				if (byteArrayoutStream != null) {
					byteArrayoutStream.flush();
					byteArrayoutStream.close();
				}
			} catch (IOException e) {
			}
		}
		return result;
	}

	/**
	 * base64 To bitmap
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		Bitmap bitmap = null;
		try {
			byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		} catch (OutOfMemoryError e) {
		} catch (IllegalArgumentException e) {
		}

		return bitmap;
	}

	public static Bitmap stringToBitmap(String data) {
		Bitmap bitmap = null;
		try {
			byte[] bytes = data.getBytes("UTF-8");
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		} catch (OutOfMemoryError e) {
		} catch (IllegalArgumentException e) {
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 
	 * drawable 对象转为 bitmap
	 * 
	 * @param drawable
	 * @return　Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {

		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 保存图片 bitmap
	 * 
	 * @param picName
	 *            　　file name
	 * @param bm
	 *            bitmap
	 */
	public static void saveBitmap(String picName, Bitmap bm, String file_name) {
		File f = new File(file_name);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public static Drawable bitmapToDrawable(Resources resources, Bitmap bitmap) {
		BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
		Drawable drawable = bitmapDrawable.getCurrent();
		return drawable;
	}

	/**
	 * 从文件路径下取得图片
	 * 
	 * @param filepath
	 * @return bitmap
	 */
	public static Bitmap getImageFromSDCard(String filepath) {
		File file = new File(filepath);
		if (file.exists()) {
			Bitmap bm = BitmapFactory.decodeFile(filepath);
			return bm;
		}
		return null;
	}

	// **************************** android图片压缩总结 ************************ //
	/**
	 * 将图片保存到本地时进行压缩, 即将图片从Bitmap形式变为File形式时进行压缩,<br/>
	 * 特点是: File形式的图片确实被压缩了, 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变 .
	 * 
	 * @Description 方法说明: 该方法是压缩图片的质量, 注意它不会减少图片的像素,比方说, 你的图片是300K的,
	 *              1280*700像素的, 经过该方法压缩后, File形式的图片是在100以下, 以方便上传服务器,
	 *              但是你BitmapFactory.decodeFile到内存中,变成Bitmap时,它的像素仍然是1280*700,
	 *              计算图片像素的方法是 bitmap.getWidth()和bitmap.getHeight(), 图片是由像素组成的,
	 *              每个像素又包含什么呢? 熟悉PS的人知道, 图片是有色相,明度和饱和度构成的.
	 * 
	 *              该方法的官方文档也解释说, 它会让图片重新构造, 但是有可能图像的位深(即色深)和每个像素的透明度会变化,JPEG
	 *              onlysupports opaque(不透明), 也就是说以jpeg格式压缩后,
	 *              原来图片中透明的元素将消失.所以这种格式很可能造成失真
	 * 
	 *              既然它是改变了图片的显示质量, 达到了对File形式的图片进行压缩, 图片的像素没有改变的话,
	 *              那重新读取经过压缩的file为Bitmap时, 它占用的内存并不会少.(不相信的可以试试)
	 * 
	 *              因为: bitmap.getByteCount() 是计算它的像素所占用的内存, 请看官方解释: Returns the
	 *              number of bytes used to store this bitmap's pixels.
	 */
	public static void compressBmpToFile(Bitmap bmp, File file) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 80;// 个人喜欢从80开始,
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			options -= 10;
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将图片从本地读到内存时,进行压缩 ,即图片从File形式变为Bitmap形式<br/>
	 * 特点: 通过设置采样率, 减少图片的像素, 达到对内存中的Bitmap进行压缩
	 * 
	 * @Description 先看一个方法: 该方法是对内存中的Bitmap进行质量上的压缩, 由上面的理论可以得出该方法是无效的,
	 *              而且也是没有必要的,因为你已经将它读到内存中了,再压缩多此一举,
	 *              尽管在获取系统相册图片时,某些手机会直接返回一个Bitmap,但是这种情况下, 返回的Bitmap都是经过压缩的,
	 *              它不可能直接返回一个原声的Bitmap形式的图片, 后果可想而知
	 * @param image
	 */
	private Bitmap compressBmpFromBmp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			options -= 10;
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	/**
	 * 
	 */
	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
		return bitmap;
	}

	private Bitmap compressImageFromAsset(Context context, String srcPath) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(srcPath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;// 只读边,不读内容
			// options.inSampleSize = 4;
			image = BitmapFactory.decodeStream(is, null, options);
			is.close();

			options.inJustDecodeBounds = false;
			int w = options.outWidth;
			int h = options.outHeight;
			float hh = 800f;//
			float ww = 480f;//
			int be = 1;
			if (w > h && w > ww) {
				be = (int) (options.outWidth / ww);
			} else if (w < h && h > hh) {
				be = (int) (options.outHeight / hh);
			}
			if (be <= 0)
				be = 1;
			options.inSampleSize = be;// 设置采样率

			options.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
			options.inPurgeable = true;// 同时设置才会有效
			options.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
			
			InputStream isNew = am.open(srcPath);
			image = BitmapFactory.decodeStream(isNew, null, options);
			isNew.close();
			// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
			// 其实是无效的,大家尽管尝试
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
	// ************* 以下 **************//
		/**
		 * 最大返回maxNumOfPixels = 1280*1280像素的图片
		 * 
		 */
		public static Bitmap getSuitableBitmap(ContentResolver resolver, Uri uri)
				throws FileNotFoundException {
			int maxNumOfPixels = 1280 * 1280;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(resolver.openInputStream(uri), null, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
			opts.inJustDecodeBounds = false;
			try {
				return BitmapFactory.decodeStream(resolver.openInputStream(uri),
						null, opts);
			} catch (OutOfMemoryError err) {
			}
			return null;
		}

		public static int computeSampleSize(BitmapFactory.Options options,
				int minSideLength, int maxNumOfPixels) {
			int initialSize = computeInitialSampleSize(options, minSideLength,
					maxNumOfPixels);
			int roundedSize;
			if (initialSize <= 8) {
				roundedSize = 1;
				while (roundedSize < initialSize) {
					roundedSize <<= 1;
				}
			} else {
				roundedSize = (initialSize + 7) / 8 * 8;
			}
			return roundedSize;
		}

		private static int computeInitialSampleSize(BitmapFactory.Options options,
				int minSideLength, int maxNumOfPixels) {
			double w = options.outWidth;
			double h = options.outHeight;
			int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
					.sqrt(w * h / maxNumOfPixels));
			int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
					Math.floor(w / minSideLength), Math.floor(h / minSideLength));

			if (upperBound < lowerBound) {
				// return the larger one when there is no overlapping zone.
				return lowerBound;
			}
			if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
				return 1;
			} else if (minSideLength == -1) {
				return lowerBound;
			} else {
				return upperBound;
			}

		}
		// ************* 以上 **************//

}
