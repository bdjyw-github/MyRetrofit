package com.idophin.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.idophin.farmingtong.BuildConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class BitmapCache {

	private static final boolean DEBUG = BuildConfig.DEBUG;
	private static final String TAG = "BitmapCache";
	public static final String FOLDER_NAME = (new File(Environment.getExternalStorageDirectory(), "Android/data/com.idophin.farmingtong/Cache/")).toString();
	// public static final String IAMGE_CROP_FILE = (new
	// File(Environment.getExternalStorageDirectory(),
	// "Android/data/com.mobo.yueta/Crop/"))
	// .toString();
	static {
		try {
			File f = new File(FOLDER_NAME);
			if (!f.exists()) {
				f.mkdirs();
				File noMediaFile = new File(f, ".nomedia");
				if (!noMediaFile.exists()) {
					noMediaFile.createNewFile();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean cacheFloderExist = false;
	private static HashMap<String, SoftReference<Bitmap>> bitmapCache = new HashMap<String, SoftReference<Bitmap>>();

	public static Bitmap getBitmapFromMemoryCache(String bitmapUrl) {
		SoftReference<Bitmap> sr = bitmapCache.get(bitmapUrl);
		return sr == null ? null : sr.get();
	}

	public static void putBitmapToMemoryCache(String bitmapUrl, Bitmap bitmap) {
		bitmapCache.put(bitmapUrl, new SoftReference<Bitmap>(bitmap));
	}

	public static void putBitmapToSD(String bitmapUrl, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 100, baos);
			putDataInSD(bitmapUrl, baos.toByteArray());
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getBitmapFromCacheWishOutSD(String bitmapUrl) {
		Bitmap bitmap = getBitmapFromMemoryCache(bitmapUrl);
		if (bitmap != null) {
			return bitmap;
		}
		return null;
	}

	public static Bitmap getBitmapFromCache(String bitmapUrl) {
		Bitmap bitmap = getBitmapFromMemoryCache(bitmapUrl);
		if (bitmap != null) {
			return bitmap;
		}
		byte[] data = getDataFromSD(bitmapUrl);
		if (data != null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ALPHA_8;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
			putBitmapToMemoryCache(bitmapUrl, bitmap);
			return bitmap;
		}
		return null;
	}

	public static void deleteCacheFolder() {
		File file = new File(FOLDER_NAME);
		if (file.exists()) {
			deleteFile(file);
		}
	}

	private static void deleteFile(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				if (f.isDirectory()) {
					deleteFile(f);
				}
				f.delete();
			}
		}
	}

	/**
	 * Get bitmap from sdcard if sdcard is mounted.
	 * 
	 * @param bitmapUrl
	 * @return bitmap
	 */
	public static byte[] getDataFromSD(String bitmapUrl) {
		if (bitmapUrl == null || bitmapUrl.length() == 0) {
			return null;
		}
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String bitmapName = getFilePath(bitmapUrl);
			File file = new File(FOLDER_NAME, bitmapName);

			if (file.exists()) {
				try {
					FileInputStream in = new FileInputStream(file);
					try {
						byte[] data = new byte[in.available()];

						in.read(data);

						if (data != null) {
							return data;
						}
					} catch (IOException e) {
						if (DEBUG) {
							e.printStackTrace();
						}
					} finally {
						try {
							if (in != null) {
								in.close();
							}
						} catch (IOException e) {
							if (DEBUG) {
								e.printStackTrace();
							}

						}
					}

				} catch (FileNotFoundException e) {
					if (DEBUG) {
						e.printStackTrace();
					}
				}
			} else {
				if (bitmapUrl.startsWith("/")) { // local path
					file = new File(bitmapUrl);
					if (file.exists() && file.isFile()) {
						try {
							FileInputStream in = new FileInputStream(file);
							try {
								byte[] data = new byte[in.available()];

								in.read(data);

								if (data != null) {
									return data;
								}
							} catch (IOException e) {
								if (DEBUG) {
									e.printStackTrace();
								}
							} finally {
								try {
									if (in != null) {
										in.close();
									}
								} catch (IOException e) {
									if (DEBUG) {
										e.printStackTrace();
									}

								}
							}

						} catch (FileNotFoundException e) {
							if (DEBUG) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else {
			if (DEBUG) {
				Log.e("Cache", "get image failed, sdcard is not mouned + \n");
			}
		}

		return null;
	}

	/**
	 * put bitmap to sd
	 * 
	 * @param bitmapUrl
	 * @param data
	 */
	public static void putDataInSD(String bitmapUrl, byte[] data) {
		if (data == null) {
			return;
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		putDataInSD(bitmapUrl, bais);
	}

	public static void putDataInSD(String bitmapUrl, InputStream is) {
		if (is == null) {
			throw new NullPointerException();
		}

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (!cacheFloderExist) {
				File floder = new File(FOLDER_NAME);

				if (!floder.exists()) {
					floder.mkdirs();
				}

				cacheFloderExist = true;
			}

			String bitmapName = getFilePath(bitmapUrl);// bitmapUrl.substring(bitmapUrl.lastIndexOf("/")
														// + 1);

			Log.d(TAG, "putDataInSD bitmapName:" + bitmapName);

			File file = new File(FOLDER_NAME, bitmapName);

			FileOutputStream os = null;

			if (!file.exists()) {
				try {
					file.createNewFile();
					os = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int readlen;
					while ((readlen = is.read(buf)) >= 0) {
						os.write(buf, 0, readlen);
					}
					os.flush();

					if (DEBUG) {
						Log.d(TAG, "Write the bitmap:" + bitmapName + "to sdcard successed.");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else {
			if (DEBUG) {
				Log.e("BitmapCache", "put image in sdcard failed, sdcard is not mouned");
			}
		}
	}

	public static void removeDataFromSD(String bitmapUrl) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (!cacheFloderExist) {
				File floder = new File(FOLDER_NAME);

				if (!floder.exists()) {
					floder.mkdirs();
				}

				cacheFloderExist = true;
			}

			String bitmapName = getFilePath(bitmapUrl);// bitmapUrl.substring(bitmapUrl.lastIndexOf("/")
														// + 1);
			File file = new File(FOLDER_NAME, bitmapName);
			if (file.exists()) {
				file.delete();
			}
		} else {
			if (DEBUG) {
				Log.e("BitmapCache", "remove image from sd failed, sdcard is not mouned");
			}
		}
	}

	private static String getFilePath(String url) {
		String path = url.substring(url.lastIndexOf("/") + 1);

		return path;
	}
}
