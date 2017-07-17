package com.becdoor.teanotes.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.global.AppStatic;

public class VersionS extends Service {

	public static String filePath = Environment.getExternalStorageDirectory()
			.getPath() + "/djnote/downloadfile/";
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			if (intent.getBooleanExtra("isUpdateDownload", false)) {// 是否下载更新
				mIsDisconnect = false;
				doOnSuccess("");
			} else {
				stopSelf();
			}
		}
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void doOnSuccess(String result) {
		if (isDownloading) {
			Toast.makeText(getApplicationContext(), "正在下载...",
					Toast.LENGTH_SHORT).show();
			return;
		}
		int lastSlashIndex = AppStatic.getInstance().down_url.lastIndexOf("/");
		downloadFileName = AppStatic.getInstance().down_url
				.substring(lastSlashIndex + 1);
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		File apk_file = new File(filePath, downloadFileName);
		if (apk_file.exists()) {
			apk_file.delete();
		}
		downloadFileWithProgress(AppStatic.getInstance().down_url, apk_file);
	}

	@Override
	public void onDestroy() {
		if (notificationManager != null) {
			notificationManager.cancel(225);
			notificationManager = null;
		}
		if (myBroadcastReceiver != null) {
			unregisterReceiver(myBroadcastReceiver);
			myBroadcastReceiver = null;
		}
		super.onDestroy();
	};

	/** 通知 */
	private NotificationManager notificationManager;
	private NotificationCompat.Builder builder;

	/** 初始化通知 */
	private void initNotification(File file) {
		registerMyBroadcast(file);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
		builder = new NotificationCompat.Builder(this)
				.setTicker("正在下载新版本...")
				.setSmallIcon(R.drawable.icon)
				.setContentTitle("正在下载安装包...")
				.setContentText("已下载：0.00%(移除通知停止下载)")
				.setContentIntent(contentIntent)
				.setDeleteIntent(
						PendingIntent.getBroadcast(this, 0, new Intent(this
								.getClass().getName()), 0))// 为关闭按钮注册广播接收器
		;
		notificationManager.notify(225, builder.build());
	}

	/** 保存的文件名 */
	private String downloadFileName = "";

	/**
	 * 进度下载
	 * 
	 * @param url
	 *            下载网址
	 *            文件保存的路径
	 *            文件名
	 */
	private void downloadFileWithProgress(String url, final File file) {
		initNotification(file);
		download(url, file);
	}

	/** 注册通知关闭按钮事件广播 */
	public void registerMyBroadcast(File file) {
		if (myBroadcastReceiver != null) {
			unregisterReceiver(myBroadcastReceiver);
			myBroadcastReceiver = null;
		}
		myBroadcastReceiver = new MyBroadcastReceiver(file);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(this.getClass().getName());
		registerReceiver(myBroadcastReceiver, intentFilter);
	}

	/** 关闭下载广播实例 */
	private MyBroadcastReceiver myBroadcastReceiver = null;

	/** 通知上的关闭按钮（停止下载并关闭）使用的广播接收器 */
	private class MyBroadcastReceiver extends BroadcastReceiver {
		private File file;

		public MyBroadcastReceiver(File file) {
			this.file = file;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				notificationManager.cancel(225);
				stopDownload(file);
			}
		}
	}

	/**
	 * 是否停止下载
	 */
	private boolean mIsDisconnect = false;

	/** 停止下载的后续操作 */
	private void stopDownload(File file) {
		mIsDisconnect = true;
		if (file.exists() && file.isFile())
			file.delete();
		stopSelf();
	}

	private double mProgressCount = -1;

	private void download(final String download_url, final File file) {
		final DecimalFormat df1 = new DecimalFormat("0.00%");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					file.createNewFile();
					URL url = new URL(download_url);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					if (conn.getResponseCode() == 200) {
						// 获取文件的大小
						float size = conn.getContentLength();
						InputStream is = conn.getInputStream();
						FileOutputStream fos = new FileOutputStream(file);
						byte[] buffer = new byte[1024];
						int len = 0;
						int count = 0;
						while ((len = is.read(buffer)) != -1) {
							if (mIsDisconnect) {
								conn.disconnect();
							}
							fos.write(buffer, 0, len);
							count += len;
							double progress = (double) count / size;
							if (progress > 1.0) {
								updateNotify("100%");
							} else if (progress - mProgressCount > 0.01) {
								mProgressCount = progress;
								updateNotify(df1.format(progress));
							}
						}
						fos.flush();
						is.close();
						fos.close();
						if (file.isFile()) {
							Message msg = new Message();
							msg.what = 1;
							msg.obj = file.getAbsolutePath();
							mHandler.sendMessage(msg);
						} else {
							mHandler.sendEmptyMessage(0);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	/**
	 * 是否正在下载
	 */
	private boolean isDownloading = false;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				stopSelf();
				mProgressCount = -1;
				Toast.makeText(getApplicationContext(), "下载成功！",
						Toast.LENGTH_SHORT).show();
				installAPK(getApplicationContext(), msg.obj.toString());
			} else if (msg.what == 2) {
				isDownloading = true;
			} else {
				mProgressCount = -1;
				Toast.makeText(getApplicationContext(), "下载失败！",
						Toast.LENGTH_SHORT).show();
			}
		};
	};

	/**
	 * 调用安装
	 * 
	 * @param context
	 * @param apkFilePath
	 * @return
	 */
	public boolean installAPK(Context context, String apkFilePath) {
		File file = new File(apkFilePath);
		if (file.exists()) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
			return true;
		}
		return false;
	}

	private void updateNotify(String progress) {
		if (builder != null && notificationManager != null) {
			builder.setContentText("已下载：" + progress + "(移除通知停止下载)");
			notificationManager.notify(225, builder.build());
		}
	}
}
