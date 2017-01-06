# Android遍历获取Office格式（Word，Excel，PPT，PDF）的文件并打开
 <p>此案例主要是模仿QQ加载WPS（Word，Excel，PPT）本地文件可打开查看，使用ListView加载，使用线程扫描SD卡下所有目录加载指定的Word，Excel，PPT等格式的文件，ListView列表显示，点击Item则调用系统应用打开。</p> 
<p>效果图：</p> 
<p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <img alt="" height="514" src="https://static.oschina.net/uploads/img/201701/06155541_qkRq.gif" width="294">&nbsp;&nbsp;</p> 
<p>代码：</p> 
<p>&nbsp;</p> 
<pre><code class="language-java">public class MainActivity extends AppCompatActivity {

    public ProgressDialog dialog;
    private ListView mListview;
    private Context context;
    private List&lt;AddFileInfo&gt; list=new ArrayList&lt;AddFileInfo&gt;();
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

            for (int i=0;i&lt;str.length;i++)
            {
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
        public void onItemClick(AdapterView&lt;?&gt; parent, View view, int position, long id) {
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
        if(length&gt;=1048576)
        {
            return (length/1048576)+"MB";
        }
        else if(length&gt;=1024)
        {
            return (length/1024)+"KB";
        }
        else if(length&lt;1024) {
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
}</code></pre> 
<p>&nbsp; &nbsp;不要忘记在AndroidManifest.xml加权限哦！</p> 
<pre><code class="language-html"> &lt;!-- SD卡权限 --&gt;
    &lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /&gt;
    &lt;uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" /&gt;</code></pre> 
