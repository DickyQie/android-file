package com.example.fileoperation;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class Util {
	
	/****
	 * 计算文件大小
	 * 
	 * @param length
	 * @return
	 */
	public static String ShowLongFileSzie(Long length) {
		if (length >= 1048576) {
			return (length / 1048576) + "MB";
		} else if (length >= 1024) {
			return (length / 1024) + "KB";
		} else if (length < 1024) {
			return length + "B";
		} else {
			return "0KB";
		}
	}
	
	/***
	 * 
	 * 更具路径得到该路径下的全部图片信息
	 * @return
	 */
	
	public static List<AddFileInfo> getSDPathFrom() {
        // 图片列表
        List<AddFileInfo> imagePathList = new ArrayList<AddFileInfo>();
        // 得到sd卡内image文件夹的路径 
        String filePath = Environment.getExternalStorageDirectory().toString() + File.separator+"BigNoxHD/cache/";
        //得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsFile(file.getPath())) {
            	String time = new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date(file.lastModified()));
            	AddFileInfo info=new AddFileInfo(file.getName(), Util.ShowLongFileSzie(file.length()), time, file.getAbsolutePath());
                imagePathList.add(info);
            }
        }
        // 返回得到的图片列表
        return imagePathList;
    }

	/****
	 * 验证文件格式
	 * @param fName
	 * @return
	 */
	public static boolean checkIsFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")|| FileEnd.equals("jpeg") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }
	
	/****
	 * 根据文件路径获取图片
	 * 其中w和h你需要转换的大小
	 * @param path 
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap convertToBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int)scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}
}
