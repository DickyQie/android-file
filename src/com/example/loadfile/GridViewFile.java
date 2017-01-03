package com.example.loadfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Administrator on 2016/12/2.
 */

public class GridViewFile extends Activity implements View.OnClickListener {

    private Context context;
    private TextView tv_title,textView;
    private GridView listView;
    private final String MUSIC_PATH = "/";

    //记录当前路径下 的所有文件的数组
    File currentParent;
    //记录当前路径下的所有文件的文件数组
    File[] currentFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        initView();
    }

    public void initView() {
        findViewById(R.id.public_top_img_close).setOnClickListener(this);
        listView=(GridView) findViewById(R.id.gridview);
        textView= (TextView) findViewById(R.id.llss);
        onLoad();
    }


    public void onLoad() {
        ListSongsName();

    }
    private void ListSongsName() {

        //获取系统的SD卡目录
        File root = new File(MUSIC_PATH);
        //如果SD卡存在
        if (root.exists()) {
            currentParent = root;
            currentFiles = root.listFiles();//获取root目录下的所有文件
            //使用当前陆慕下的全部文件，文件夹来填充ListView
            inflateListView(currentFiles);
        }
        //为ListView的列表项的单击事件绑定监视器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //用户点击了文件，则调用手机已安装软件操作该文件
                if (currentFiles[position].isFile()) {
                    onError(currentFiles[position].getPath()+"1");
                    Intent intent = OpenFile.openFile(currentFiles[position].getPath());
                    startActivity(intent);
                } else {

                    //获取currentFiles[position]路径下的所有文件
                    File[] tmp = currentFiles[position].listFiles();
                    if (tmp == null || tmp.length == 0) {
                        Toast.makeText(GridViewFile.this, "空文件夹!", Toast.LENGTH_SHORT).show();
                    }//if
                    else {
                        //获取用户单击的列表项对应的文件夹，设为当前的父文件夹
                        currentParent = currentFiles[position];
                        //保存当前文件夹内的全部问价和文件夹
                        currentFiles = tmp;
                        inflateListView(currentFiles);
                    }
                }
            }
        });




    }


    //更新列表
    private void inflateListView(File[] files) {
        if (files.length == 0)
            Toast.makeText(GridViewFile.this, "sd卡不存在", Toast.LENGTH_SHORT).show();
        else {
            //创建一个List集合,List集合的元素是Map
            List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < files.length; i++) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                //如果当前File是文件夹，使用folder图标；否则使用file图标
                Log.i("path",files[i].getPath());
                Log.i("path",files[i].getName());
                if (files[i].isDirectory()) listItem.put("icon", R.drawable.file_wenjianjia);
                    //else if(files[i].isFi)
                else listItem.put("icon", R.drawable.file_wenjian1);
                listItem.put("fileName", files[i].getName());
                //添加List项
                listItems.add(listItem);
            }
            //创建一个SimpleAdapter
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.acheshi_item, new String[]{"icon", "fileName"},
                    new int[]{R.id.imageView1, R.id.text_path});
            //位ListView设置Adpter
            listView.setAdapter(simpleAdapter);
            try {
                textView.setText("当前路径为：" + currentParent.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onClick(View v) {
        onbey();
    }

    //返回上层菜单
    private void onbey() {
        try {
            if (!MUSIC_PATH.equals(currentParent.getCanonicalPath())) {
                //获取上一层目录
                currentParent = currentParent.getParentFile();
                //列出当前目录下的所有文件
                currentFiles = currentParent.listFiles();
                //再次更新ListView
                inflateListView(currentFiles);
            }
            else{
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("提示")
                        .setMessage("确定要退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onError(Object error) {
        Toast.makeText(getApplicationContext(),error+"",Toast.LENGTH_LONG).show();
    }


    protected void onDestroy() {
        super.onDestroy();
    }



}
