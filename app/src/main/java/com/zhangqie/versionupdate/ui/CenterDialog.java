package com.zhangqie.versionupdate.ui;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zhangqie.versionupdate.R;


/**
 * @describe 自定义居中弹出dialog
 */
public class CenterDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private int layoutResID;

    private String url;

    /**
     * 要监听的控件id
     */
    private int[] listenedItems;

    private OnCenterItemClickListener listener;

    public CenterDialog(Context context, int layoutResID, int[] listenedItems, String url) {
        super(context, R.style.dialog_custom);
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
        this.url=url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        //window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
        setContentView(layoutResID);
        WindowManager windowManager = ((Activity) context).getWindowManager();// 宽度全屏
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*4/5; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true); // 点击Dialog外部消失
        for (int id : listenedItems) {
            findViewById(id).setOnClickListener(this);
        }
    }

    public interface OnCenterItemClickListener {

        void OnCenterItemClick(CenterDialog dialog, View view, String url);

    }

    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        dismiss();
        listener.OnCenterItemClick(this, view,url);
    }
}

