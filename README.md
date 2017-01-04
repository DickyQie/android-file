# Android加载SD卡目录，文件夹遍历，图片设置，设置文件对应打开方式等

 <p>此案例主要说的是Android使用GridView加载SD卡下所有目录，文件夹多层遍历，文件图标修改，设置文件对应打开方式等功能。</p> 
<p>如图：</p> 
<p>&nbsp; &nbsp; &nbsp; &nbsp; <img alt="" height="402" src="https://static.oschina.net/uploads/space/2017/0103/150534_HRds_2945455.gif" width="311"></p> 
<p>代码：</p> 
<p>&nbsp;</p> 
<pre><code class="language-java">public class GridViewFile extends Activity implements View.OnClickListener {

	private Context context;
	private TextView tv_title, textView;
	private GridView listView;
	private final String MUSIC_PATH = "/";

	// 记录当前路径下 的所有文件的数组
	File currentParent;
	// 记录当前路径下的所有文件的文件数组
	File[] currentFiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		initView();
	}

	public void initView() {
		findViewById(R.id.public_top_img_close).setOnClickListener(this);
		listView = (GridView) findViewById(R.id.gridview);
		textView = (TextView) findViewById(R.id.llss);
		onLoad();
	}

	public void onLoad() {
		ListSongsName();

	}

	private void ListSongsName() {

		// 获取系统的SD卡目录
		File root = new File(MUSIC_PATH);
		// 如果SD卡存在
		if (root.exists()) {
			currentParent = root;
			currentFiles = root.listFiles();// 获取root目录下的所有文件
			// 使用当前陆慕下的全部文件，文件夹来填充ListView
			inflateListView(currentFiles);
		}
		// 为ListView的列表项的单击事件绑定监视器
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView&lt;?&gt; parent, View view,
					int position, long id) {
				// 用户点击了文件，则调用手机已安装软件操作该文件
				if (currentFiles[position].isFile()) {
					onError(currentFiles[position].getPath() + "1");
					Intent intent = OpenFile.openFile(currentFiles[position]
							.getPath());
					startActivity(intent);
				} else {

					// 获取currentFiles[position]路径下的所有文件
					File[] tmp = currentFiles[position].listFiles();
					if (tmp == null || tmp.length == 0) {
						Toast.makeText(GridViewFile.this, "空文件夹!",
								Toast.LENGTH_SHORT).show();
					}// if
					else {
						// 获取用户单击的列表项对应的文件夹，设为当前的父文件夹
						currentParent = currentFiles[position];
						// 保存当前文件夹内的全部问价和文件夹
						currentFiles = tmp;
						inflateListView(currentFiles);
					}
				}
			}
		});

	}

	// 更新列表
	private void inflateListView(File[] files) {
		if (files.length == 0)
			Toast.makeText(GridViewFile.this, "sd卡不存在", Toast.LENGTH_SHORT)
					.show();
		else {
			// 创建一个List集合,List集合的元素是Map
			List&lt;Map&lt;String, Object&gt;&gt; listItems = new ArrayList&lt;Map&lt;String, Object&gt;&gt;();
			for (int i = 0; i &lt; files.length; i++) {
				Map&lt;String, Object&gt; listItem = new HashMap&lt;String, Object&gt;();
				// 如果当前File是文件夹，使用folder图标；否则使用file图标
				Log.i("path", files[i].getPath());
				Log.i("path", files[i].getName());
				if (files[i].isDirectory())
					listItem.put("icon", R.drawable.file_wenjianjia);
				// else if(files[i].isFi)
				else
					listItem.put("icon", R.drawable.file_wenjian1);
				listItem.put("fileName", files[i].getName());
				// 添加List项
				listItems.add(listItem);
			}
			// 创建一个SimpleAdapter
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
					R.layout.acheshi_item, new String[] { "icon", "fileName" },
					new int[] { R.id.imageView1, R.id.text_path });
			// 位ListView设置Adpter
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

	// 返回上层菜单
	private void onbey() {
		try {
			if (!MUSIC_PATH.equals(currentParent.getCanonicalPath())) {
				// 获取上一层目录
				currentParent = currentParent.getParentFile();
				// 列出当前目录下的所有文件
				currentFiles = currentParent.listFiles();
				// 再次更新ListView
				inflateListView(currentFiles);
			} else {
				new AlertDialog.Builder(this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("提示")
						.setMessage("确定要退出吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).create().show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onError(Object error) {
		Toast.makeText(getApplicationContext(), error + "", Toast.LENGTH_LONG)
				.show();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

}
</code></pre> 
<pre><code class="language-java">/**
 * 根据路径调用系统对应的应用打开文件
 */

public class OpenFile {

    public static Intent openFile(String filePath){

        File file = new File(filePath);
        if(!file.exists()) return null;
            /* 取得扩展名 */
        String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase();
            /* 依扩展名的类型决定MimeType */
        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            return getAudioFileIntent(filePath);
        }else if(end.equals("3gp")||end.equals("mp4")){
            return getAudioFileIntent(filePath);
        }else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            return getImageFileIntent(filePath);
        }else if(end.equals("apk")){
            return getApkFileIntent(filePath);
        }else if(end.equals("ppt")){
            return getPptFileIntent(filePath);
        }else if(end.equals("xls")){
            return getExcelFileIntent(filePath);
        }else if(end.equals("doc")){
            return getWordFileIntent(filePath);
        }else if(end.equals("pdf")){
            return getPdfFileIntent(filePath);
        }else if(end.equals("chm")){
            return getChmFileIntent(filePath);
        }else if(end.equals("txt")){
            return getTextFileIntent(filePath,false);
        }else{
            return getAllIntent(filePath);
        }
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent( String param ) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);//动作
        Uri uri = Uri.fromFile(new File(param ));//转换类型
        intent.setDataAndType(uri,"*/*");
        return intent;
    }
    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent( String param ) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent( String param ) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent( String param ){

        Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent( String param ) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent( String param, boolean paramBoolean){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean){
            Uri uri1 = Uri.parse(param );
            intent.setDataAndType(uri1, "text/plain");
        }else{
            Uri uri2 = Uri.fromFile(new File(param ));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }
    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param ));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}

</code></pre> 
<p>不要忘记在AndroidManifest.xml加权限哦！</p> 
<pre><code class="language-html"> &lt;!-- SD卡权限 --&gt;
    &lt;lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /&gt;
    &lt;uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" /&gt;</code></pre> 
