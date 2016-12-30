package com.example.basicfiledemo;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;



public class MainActivity extends Activity {

	 private String path="/storage/emulated/0/Android/data/cn.wps.moffice_eng/mm.doc";
	 private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    @SuppressLint("SimpleDateFormat") private void initView() {
		// TODO Auto-generated method stub
    	  mTextView=(TextView)findViewById(R.id.textview);
    	  File f = new File(path);
    	  if(f.exists())
    	  {
	    	  FileInputStream fis = null;
	          try {
	              fis = new FileInputStream(f);
	              String time=new SimpleDateFormat("yyyy-MM-dd").format(new Date(f.lastModified()));
	              System.out.println("文件文件创建时间"+time);
	              System.out.println("文件大小:"+ ShowLongFileSzie(f.length()));//计算文件大小  B,KB,MB,
	              System.out.println("文件大小:"+fis.available()+"B");
	              System.out.println("文件名称：" + f.getName());
	              System.out.println("文件是否存在：" + f.exists());
	              System.out.println("文件的相对路径：" + f.getPath());
	              System.out.println("文件的绝对路径：" + f.getAbsolutePath());
	              System.out.println("文件可以读取：" + f.canRead());
	              System.out.println("文件可以写入：" + f.canWrite());
	              System.out.println("文件上级路径：" + f.getParent());
	              System.out.println("文件大小：" + f.length() + "B");
	              System.out.println("文件最后修改时间：" + new Date(f.lastModified()));
	              System.out.println("是否是文件类型：" + f.isFile());
	              System.out.println("是否是文件夹类型：" + f.isDirectory());
	              mTextView.setText("文件文件创建时间:"+time+"\n"+"文件大小:"+ShowLongFileSzie(f.length())+"\n"+"文件名称：" + f.getName()
	            		  +"\n"+"文件是否存在：" + f.exists()+"\n"+"文件的相对路径：" + f.getPath()+"\n"+"文件的绝对路径：" + f.getAbsolutePath()+"\n"+"文件可以写入：" + f.canWrite()
	            		  +"\n"+"是否是文件夹类型：" + f.isDirectory());
	          } catch (Exception e) {
	              e.printStackTrace();
	          }
    	  }
	}
   
    
    
    /****
     *计算文件大小
     * @param length
     * @return
     */
    public String ShowLongFileSzie(Long length)
    {
        if(length>=1048576)
        {
            return (length/1048576)+"MB";
        }
        else if(length>=1024)
        {
            return (length/1024)+"KB";
        }
        else if(length<1024) {
            return length + "B";
        }else{
            return "0KB";
        }
    }

    
    
    
    
   
}
