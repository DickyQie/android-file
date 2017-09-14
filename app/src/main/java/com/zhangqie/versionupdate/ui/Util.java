package com.zhangqie.versionupdate.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * Created by zhangqie on 2017/6/16.
 */

public class Util {


    /****
     * 获取移动端版本号
     * 格式可根据比较方式自己设置
     *
     * @param activity
     * @return
     */
    public static final String getVersion(Activity activity) {
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            String version =info.versionCode+"."+info.versionName;
            return  version;
        } catch (Exception e) {
            return "";
        }
    }

    /****
     * 服务器下载APK文件
     * @param context
     * @param url
     */
    public static void showDownloadAPK(final Context context, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .get()
                        .url(url)
                        .build()
                        .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"cg.apk") {//保存路径      APK名称
                            @Override
                            public void onError(Call call, Exception e, int id) {
                            }

                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);
                            }

                            @Override
                            public void onResponse(File response, int id) {
                                showSelectAPK(context);
                            }
                        });
            }
        }).start();

    }

    /***
     * 调起安装APP窗口  安装APP
     * @param context
     */
    private static void showSelectAPK(Context context){
        File fileLocation = new File(Environment.getExternalStorageDirectory(), "cg.apk");//APK名称
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(fileLocation), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
