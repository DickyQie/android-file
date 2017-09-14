package com.zhangqie.versionupdate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhangqie.versionupdate.ui.CenterDialog;
import com.zhangqie.versionupdate.ui.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CenterDialog.OnCenterItemClickListener {

    CenterDialog centerDialog;

    String url="";//网址
    Map<String,String>  map=new HashMap<>();//参数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initView();
    }

    private void initView(){


        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {

            }
            @Override
            public void onResponse(String response, int id) {
                ///解析数据
                String version="服务器版本";
                if (!version.equals(Util.getVersion(MainActivity.this))){
                    centerDialog = new CenterDialog(MainActivity.this,
                            R.layout.dialog_center_layout, new int[]{R.id.dialog_cancel, R.id.dialog_sure},
                            "服务器APK下载链接");
                    centerDialog.setOnCenterItemClickListener(MainActivity.this);
                    centerDialog.show();
                }
            }

        });
    }

    @Override
    public void OnCenterItemClick(CenterDialog dialog, View view, String url) {
        switch (view.getId()){
            case R.id.dialog_sure:
                Util.showDownloadAPK(MainActivity.this,url);
                centerDialog.cancel();//关闭窗口
                break;
            case R.id.dialog_cancel:
                centerDialog.cancel();
                break;
            default:
                break;
        }
    }


}
