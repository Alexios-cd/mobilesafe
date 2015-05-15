package com.fwj.mobilesafe.uitils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.WindowManager;

/**
 * ============================================================
 * 
 * 版权 ：鼎开互联集团 版权所有 (c) 2014
 * 
 * 版本 ：var1.3
 * 
 * 创建日期 ：2014-6-6 上午11:07:01
 * 
 * 描述 ： 图片缓存类，封装了网络获取图片，本地缓存，内存缓存
 * 
 * 修订历史 ： ============================================================
 **/

public class ImageUtil {
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 10000;

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			left = 0;
			top = 0;
			right = width;
			bottom = width;
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

		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
		paint.setColor(color);

		// 以下有两种方法画圆,drawRounRect和drawCircle
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//
		// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		canvas.drawCircle(roundPx, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

		return output;
	}

	/**
	 * 
	 * @param bitmap
	 *            原图
	 * @param edgeLength
	 *            希望得到的正方形部分的边长
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
		if (null == bitmap || edgeLength <= 0) {
			return null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		if (widthOrg > edgeLength && heightOrg > edgeLength) {
			// 压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math
					.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				return null;
			}

			// 从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
						edgeLength, edgeLength);
				scaledBitmap.recycle();
			} catch (Exception e) {
				return null;
			}
		}

		return result;
	}

	/** 从给定的路径加载图片，并指定是否自动旋转方向 */
	@SuppressWarnings("deprecation")
	public static Bitmap loadBitmap(String imgpath, Context context) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgpath, options);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth(); // 屏幕宽度
		int height = manager.getDefaultDisplay().getHeight(); // 屏幕高度
		options.inSampleSize = computeSampleSize(options, -1, width * height);
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(imgpath, options);

		int digree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(imgpath);
		} catch (IOException e) {
			e.printStackTrace();
			exif = null;
		}
		if (exif != null) {
			// 读取图片中相机方向信息
			int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);
			// 计算旋转角度
			switch (ori) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				digree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				digree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				digree = 270;
				break;
			default:
				digree = 0;
				break;
			}
		}
		if (digree != 0) {
			// 旋转图片
			Matrix m = new Matrix();
			m.postRotate(digree);
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					m, true);
		}
		return bm;
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

	/**
	 * JPG图片缓存
	 * 
	 * @param imagePath
	 * @param bitmap
	 * @throws IOException
	 */
	public static void saveImageJpeg(String imagePath, Bitmap bitmap)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		byte[] b = bos.toByteArray();
		saveImage(imagePath, b);
		bos.flush();
		bos.close();
	}

	/**
	 * PNG图片缓存
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */

	public static void saveImagePng(String imagePath, Bitmap bitmap)
			throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] b = bos.toByteArray();
		saveImage(imagePath, b);
		bos.flush();
		bos.close();
	}

	/**
	 * 缓存其他图片
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */
	public static void saveImage(String imagePath, byte[] buffer)
			throws IOException {
		File f = new File(imagePath);
		if (!f.exists()) {
			File parentFile = f.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			f.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(imagePath);
		fos.write(buffer);
		fos.flush();
		fos.close();
	}

	/**
	 * 压缩图片
	 * 
	 * @param bmp
	 * @param f
	 * @param size
	 */
	public static void compressBmpToFile(Bitmap bmp, File f, float size) {
		if (!f.exists()) {
			File parentFile = f.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			try {
				f.createNewFile();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int options = 80;
				bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
				while (baos.toByteArray().length / 1024.0f > (size * 1024)) {
					baos.reset();
					options -= 10;
					bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
				}
				try {
					FileOutputStream fos = new FileOutputStream(f);
					fos.write(baos.toByteArray());
					fos.flush();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从本地获取图片
	 * 
	 * @param imagePath
	 * @return Bitmap
	 */
	public static Bitmap getBitmapFromLocal(String imagePath, Context context) {
		File file = new File(imagePath);
		if (file.exists()) {
			Bitmap bitmap = loadBitmap(imagePath, context);
			file.setLastModified(System.currentTimeMillis());
			return bitmap;
		}
		return null;
	}

	/**
	 * 从一个网址获得图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String url, Context context) {
		Bitmap bitmap = null;

		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			byte[] data = getBytes(conn.getInputStream());
			bitmap = getUsableBitmap(data, context);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 读取流获得byte[]
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static byte[] getBytes(InputStream is) throws IOException {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024]; // 用数据装
		int len = -1;
		while ((len = is.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		// 关闭流一定要记得。
		return outstream.toByteArray();
	}

	/**
	 * Bitmap转换为byte数组
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap b) {
		byte[] buff = null;
		if (b == null) {
			return null;
		}

		ByteArrayOutputStream o = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, 100, o);
		buff = o.toByteArray();
		return buff;
	}

	/**
	 * Bitmap转换为byte数组
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] bitmapToByte(String path) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = -1;
		byte[] buffer = new byte[1024];
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(path));
			while ((len = in.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
				baos.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (null != in)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return baos.toByteArray();
	}

	/**
	 * 获得图片路径
	 * 
	 * @param intent
	 * @return
	 */
	public static String getFromIntent(Uri uri) {
		Cursor c = UIUtils.getContext().getContentResolver()
				.query(uri, new String[] { "_data" }, null, null, null);
		c.moveToNext();
		return c.getString(0);
	}

	/**
	 * 调用系统剪裁
	 * 
	 * @param activity
	 * @param uri
	 * @param outuri
	 * @param requestCode
	 */
	public static void CropImageUri(Activity activity, Uri uri, Uri outuri,
			int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 2);
		// intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", outputX);
		// intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 选择相册里面
	 * 
	 * @param activity
	 */
	public static void openPIC(Activity activity, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		activity.startActivityForResult(intent, requestCode);
		// activity.startActivityForResult(intent, 2);
	}

	/**
	 * 从byte[]获得bitmap
	 * 
	 * @param data
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap getUsableBitmap(byte[] data, Context context) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth(); // 屏幕宽度
		int height = manager.getDefaultDisplay().getHeight(); // 屏幕高度
		options.inSampleSize = computeSampleSize(options, -1, width * height);
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory
				.decodeByteArray(data, 0, data.length, options);
		return bm;
	}

	// /**
	// * 从路径获得bitmap
	// *
	// * @param path
	// * @return
	// */
	// public static Bitmap getUsableBitmap(String path, Context context) {
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inJustDecodeBounds = true;
	// BitmapFactory.decodeFile(path, options);
	// WindowManager manager = (WindowManager)
	// context.getSystemService(context.WINDOW_SERVICE);
	// int width = manager.getDefaultDisplay().getWidth(); // 屏幕宽度
	// int height = manager.getDefaultDisplay().getHeight(); // 屏幕高度
	// options.inSampleSize = computeSampleSize(options, -1, width * height);
	// options.inJustDecodeBounds = false;
	// Bitmap bm = BitmapFactory.decodeFile(path, options);
	// return bm;
	// }

	/**
	 * 旋转图片，使图片保持正确的方向。
	 * 
	 * @param bitmap
	 *            原始图片
	 * @param degrees
	 *            原始图片的角度
	 * @return Bitmap 旋转后的图片
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
		if (degrees == 0 || null == bitmap) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		if (null != bitmap) {
			bitmap.recycle();
		}
		return bmp;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}


	/**
	 * 缩放图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap imageZoom(Context context, Bitmap bitmap, int w, int h) {
		// 获取 这个图片的宽和高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		w = UIUtils.dip2px(w);
		h = UIUtils.dip2px(h);
		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) w) / width;
		float scaleHeight = ((float) h) / height;
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}

	/* 下面工具类是新添加的 */
	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
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
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 *            圆角的弧度
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}

}