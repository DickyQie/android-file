package menu.filedemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ProgressDialog dialog;
    private ListView mListview;
    private Context context;
    private List<AddFileInfo> list=new ArrayList<AddFileInfo>();
    private String filePath = Environment.getExternalStorageDirectory().toString() + File.separator;
    private static Adapter adapter;
    private ACache aCache;
    private String fileDate="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListview=(ListView) findViewById(R.id.listview);
        context=this;
        aCache=ACache.get(this);
        onLoad();
    }
    public void onLoad() {
        adapter=new Adapter(MainActivity.this);
        String string=aCache.getAsString("file");
        if(string==null)
        {
            showProgress();
            new MyThread().start();
        }else{
            String[] str=string.split(",");

            for (int i=0;i<str.length;i++)
            {
                Log.i("file",str[i]);
                File f = new File(str[i]);
                if(f.exists()) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(f);
                        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(f.lastModified()));
                        AddFileInfo info = new AddFileInfo(f.getName(), Long.valueOf(fis.available()), time, false, f.getAbsolutePath());
                        fileDate += f.getAbsolutePath() + ",";
                        list.add(info);
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }
        mListview.setOnItemClickListener(onItemClickListener);
        mListview.setAdapter(adapter);
    }

    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(OpenFile.openFile(list.get(position).getPath()));
        }
    };



    public class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                doSearch(filePath);
                Thread.sleep(2000);
                Message msg=new Message();
                msg.what=1;
                msg.obj=1;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                dismissProgress();
                adapter.notifyDataSetChanged();
                aCache.put("file",fileDate.substring(0,(fileDate.length()-1)),600);
            }
        }
    };


    /****
     *计算文件大小
     * @param length
     * @return
     */
    public static String ShowLongFileSzie(Long length)
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



    /****
     * 递归算法获取本地文件
     * @param path
     */
    private void doSearch( String path) {
        File file = new File(path);

        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                for (File f : fileArray) {

                    if (f.isDirectory()) {
                        doSearch(f.getPath());
                    }
                    else {
                        if(f.getName().endsWith(".ppt") || f.getName().endsWith(".pptx") || f.getName().endsWith(".docx")
                                || f.getName().endsWith(".xls") || f.getName().endsWith(".doc"))
                        {
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(f);
                                String time=new SimpleDateFormat("yyyy-MM-dd").format(new Date(f.lastModified()));
                                AddFileInfo  info=new AddFileInfo(f.getName(),Long.valueOf(fis.available()),time,false,f.getAbsolutePath());
                                list.add(info);
                                fileDate += f.getAbsolutePath() + ",";
                                Log.i("url",f.getAbsolutePath()+"--"+f.getName()+"---"+fis.available()+"--");
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

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    class Adapter extends BaseAdapter
    {
        private int[] img_word=new int[]{R.mipmap.word,R.mipmap.xls,R.mipmap.ppt};
        private LayoutInflater inflater;
        public Adapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.item_mytask_file_listview, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AddFileInfo info=(AddFileInfo) getItem(position);
            if(info.getName().endsWith(".doc") || info.getName().endsWith(".docx"))
            {
                holder.iv_img.setImageResource(img_word[0]);
            }
            else if(info.getName().endsWith(".xls"))
            {
                holder.iv_img.setImageResource(img_word[1]);
            }else{
                holder.iv_img.setImageResource(img_word[2]);
            }
            holder.tv_name.setText(info.getName());
            holder.size.setText(ShowLongFileSzie(info.getSize()));
            holder.time.setText(info.getTime());
            return convertView;
        }


    }
    class ViewHolder {

        private ImageView iv_img;
        private TextView tv_name;
        private TextView size;
        private TextView time;
        public ViewHolder(View view) {
            iv_img = (ImageView) view.findViewById(R.id.item_file_img);
            tv_name = (TextView) view.findViewById(R.id.item_file_name);
            size = (TextView) view.findViewById(R.id.item_file_size);
            time = (TextView) view.findViewById(R.id.item_file_time);
        }
    }


    /***
     * 启动
     */
    public void showProgress()
    {
        if(dialog==null)
        {
            dialog=new ProgressDialog(MainActivity.this);
        }
        dialog.showMessage("正在加载");
    }

    /***
     * 关闭
     */
    public void  dismissProgress()
    {
        if(dialog==null)
        {
            dialog=new ProgressDialog(this);
        }
        dialog.dismiss();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
