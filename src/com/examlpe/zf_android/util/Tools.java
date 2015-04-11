package com.examlpe.zf_android.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Tools {
	static int index = 0;
	static int index2 = 0;
	static ArrayList<ImageView> imageviewList0;
	private static Bitmap bm = null;

    public static int cityId() {
        return 1;
    }

	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;

	}

	public static void set_bk(final String url, final ImageView imageView) {

		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					Drawable drawable = (Drawable) msg.obj;
					imageView.setBackgroundDrawable(drawable);
					break;
				default:
					break;
				}
			}
		};

		Thread thread = new Thread() {
			public void run() {
				Drawable face_drawable;
				try {
					Log.i("linshi------------", url);
					URL myurl = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) myurl
							.openConnection();
					conn.setConnectTimeout(6000);
					conn.setDoInput(true);
					conn.setUseCaches(false);
					conn.connect();
					InputStream is = conn.getInputStream();

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = false;
					// options.outWidth = 159;
					// options.outHeight = 159;
					options.inSampleSize = 1;
					bm = BitmapFactory.decodeStream(is, null, options);
					Log.i("linshi", bm.getWidth() + "---" + bm.getHeight());
					is.close();
					if (bm != null) {
						Log.i("linshi",
								bm.getWidth() + "---2---" + bm.getHeight());
						face_drawable = new BitmapDrawable(bm);
						Message msg = new Message();
						msg.what = 0;
						msg.obj = face_drawable;
						mHandler.sendMessage(msg);

					}

				} catch (Exception e) {
					Log.i("linshi", "dd");
					// Log.i("linshi", url);
				}

			}
		};

		thread.start();

	}

	public static void set_bk_createRoundConerImage(final int contactId,
			final String url, final ImageView imageView) {

		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					Bitmap bitmap = (Bitmap) msg.obj;
					imageView.setImageDrawable(new BitmapDrawable(
							createRoundConerImage(bitmap)));
					break;
				default:
					break;
				}
			}
		};

		Thread thread = new Thread() {
			public void run() {
				try {
					Log.i("linshi------------", url);
					URL myurl = new URL(url);
					// initTrustSSL();
					// �������
					HttpURLConnection conn = (HttpURLConnection) myurl
							.openConnection();
					conn.setConnectTimeout(6000);// ���ó�ʱ
					conn.setDoInput(true);
					conn.setUseCaches(false);// ������
					conn.connect();
					InputStream is = conn.getInputStream();// ���ͼƬ�������
					// bm =decodeSampledBitmapFromStream(is,150,150);

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = false;
					// options.outWidth = 159;
					// options.outHeight = 159;
					options.inSampleSize = 1;
					bm = BitmapFactory.decodeStream(is, null, options);
					Log.i("linshi", bm.getWidth() + "---" + bm.getHeight());
					is.close();
					if (bm != null) {
						Log.i("linshi",
								bm.getWidth() + "---2---" + bm.getHeight());
						// File f = new File(Urlinterface.head_pic, contactId +
						// "");
						//
						// if (f.exists()) {
						// f.delete();
						// }
						// if (!f.getParentFile().exists()) {
						// f.getParentFile().mkdirs();
						// }
						// Log.i("linshi", "----1");
						// FileOutputStream out = new FileOutputStream(f);
						// Log.i("linshi", "----6");
						// bm.compress(Bitmap.CompressFormat.PNG, 90, out);
						// out.flush();
						// out.close();
						// Log.i("linshi", "�Ѿ�����");

						Message msg = new Message();// ����Message ����
						msg.what = 0;
						msg.obj = bm;
						mHandler.sendMessage(msg);

					}

				} catch (Exception e) {
					Log.i("linshi", "�����쳣");
					// Log.i("linshi", url);
				}

			}
		};

		thread.start();

	}

	/**
	 * ת��ͼƬ��Բ��
	 * 
	 * @param bitmap
	 *            ����Bitmap����
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
		paint.setAntiAlias(true);// ���û����޾��

		canvas.drawARGB(0, 0, 0, 0); // ������Canvas
		paint.setColor(color);

		// ���������ַ�����Բ,drawRounRect��drawCircle
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//
		// ��Բ�Ǿ��Σ���һ������Ϊͼ����ʾ���򣬵ڶ�������͵��������ֱ���ˮƽԲ�ǰ뾶�ʹ�ֱԲ�ǰ뾶��
		canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// ��������ͼƬ�ཻʱ��ģʽ,�ο�http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // ��Mode.SRC_INģʽ�ϲ�bitmap���Ѿ�draw�˵�Circle

		return output;
	}

	// �ж�sd���Ƿ����
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	// �ж�����
	public static boolean isConnect(Context context) {

		// ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ��?
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {

				// ��ȡ�������ӹ���Ķ���
				NetworkInfo info = connectivity.getActiveNetworkInfo();

				if (info != null && info.isConnected()) {
					// �жϵ�ǰ�����Ƿ��Ѿ�����
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log.v("error", e.toString());
		}
		return false;
	}

	/**
	 * ���ԭͼ���Բ��
	 * 
	 * @param source
	 * @return
	 */
	public static Bitmap createRoundConerImage(Bitmap source) {
		int width = source.getWidth();
		int height = source.getHeight();
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, width, height);
		canvas.drawRoundRect(rect, 10f, 10f, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	public static void initTrustSSL() {
		try {
			SSLContext sslCtx = SSLContext.getInstance("TLS");
			sslCtx.init(null, new TrustManager[] { new X509TrustManager() {
				// do nothing, let the check pass.
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			} }, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sslCtx
					.getSocketFactory());
			HttpsURLConnection
					.setDefaultHostnameVerifier(new HostnameVerifier() {
						public boolean verify(String hostname,
								SSLSession session) {
							return true;
						}
					});
		} catch (Exception E) {
		}
	}

	public static String del_tag(String str) {// ȥ��HTML��ǩ
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(str);
		String content = m_html.replaceAll(""); // ����html��ǩ
		return content;
	}

	/**
	 * ����ֻ�ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public static int dip2px(Context context, float dpValue, int size) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * size * scale + size);
	}

	/**
	 * ����ֻ�ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * ��õ�ǰʱ�� hh��mm
	 * */
	public static String getHourAndMin() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date());
	}

	/**
	 * �������뷨
	 * */
	public static void hideInputMethod(Activity act) {
		InputMethodManager imm = (InputMethodManager) act
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (isOpen) {
			imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

//	public static ArrayList getExpressDictList(Context context) {
//		Gson gson = new Gson();
//		SharedPreferences preferences = context.getSharedPreferences(
//				cn.com.besttone.merchant.config.Config.SHARED,
//				Context.MODE_PRIVATE);
//		ArrayList<ExpressDict> items = new ArrayList<ExpressDict>();
//		items = gson.fromJson(preferences.getString("ExpressDictList", ""),
//				new TypeToken<List<ExpressDict>>() {
//				}.getType());
//		return items;
//	}
	
//	public static MyDialog showLoading( Activity act,
//			String str) {
//		View view = act.getLayoutInflater().inflate(R.layout.loading_main, null);
//		ImageView iv = (ImageView) view.findViewById(R.id.loading_iv);
//		final AnimationDrawable loadingDw = ((AnimationDrawable) iv
//				.getBackground());
//		// loadingDw.start();
//		iv.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
//			@Override
//			public boolean onPreDraw() {
//				loadingDw.start();
//				return true; // ����Ҫ�����true����
//			}
//		});
//		TextView tv = (TextView) view.findViewById(R.id.loading_tv);
//		tv.setText(str);
//		MyDialog builder = new MyDialog(act, 3, view, R.style.mydialog);
//		builder.setCanceledOnTouchOutside(false);
//		builder.show();
//		return builder;
//	}

}
