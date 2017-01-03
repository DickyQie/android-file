# Android根据文件路径加载指定文件
<h2>Android根据指定的文件路径加载指定文件格式（图片格式 png, gif,jpg jpeg）的文件相关信息的列表。</h2> 
<p>如图：</p> 
<p>&nbsp; &nbsp; &nbsp; &nbsp;<img alt="" height="536" src="https://static.oschina.net/uploads/space/2017/0103/104810_cPRq_2945455.png" width="337"></p> 
<p>代码：</p> 
<pre><code class="language-java">public class Util {
	
	/****
	 * 计算文件大小
	 * 
	 * @param length
	 * @return
	 */
	public static String ShowLongFileSzie(Long length) {
		if (length &gt;= 1048576) {
			return (length / 1048576) + "MB";
		} else if (length &gt;= 1024) {
			return (length / 1024) + "KB";
		} else if (length &lt; 1024) {
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
	
	public static List&lt;AddFileInfo&gt; getSDPathFrom() {
        // 图片列表
        List&lt;AddFileInfo&gt; imagePathList = new ArrayList&lt;AddFileInfo&gt;();
        // 得到sd卡内image文件夹的路径 
        String filePath = Environment.getExternalStorageDirectory().toString() + File.separator+"BigNoxHD/cache/";
        //得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i &lt; files.length; i++) {
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
		if (width &gt; w || height &gt; h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int)scale;
		WeakReference&lt;Bitmap&gt; weak = new WeakReference&lt;Bitmap&gt;(BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}
}
</code></pre> 
<p>MainActivity.java</p> 
<pre><code class="language-java">public class MainActivity extends Activity {

	private ListView mListview;
	private List&lt;AddFileInfo&gt; list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mListview = (ListView) findViewById(R.id.listview);
		list = Util.getSDPathFrom();
		mListview.setAdapter(new Adapter(MainActivity.this));
	}

	class Adapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context context;

		public Adapter(Context context) {
			this.context = context;
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
				convertView = inflater.inflate(
						R.layout.item_mytask_file_listview, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AddFileInfo info = (AddFileInfo) getItem(position);
			holder.img.setImageBitmap(Util.convertToBitmap(info.getPath(), 99, 99));
			holder.tv_name.setText("文件名称：" + info.getName());
			holder.size.setText("文件大小：" + info.getSize());
			holder.time.setText("文件创建时间：" + info.getTime());
			return convertView;
		}

	}

	class ViewHolder {

		private TextView tv_name;
		private TextView size;
		private TextView time;
		private ImageView img;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.img);
			tv_name = (TextView) view.findViewById(R.id.item_file_name);
			size = (TextView) view.findViewById(R.id.item_file_size);
			time = (TextView) view.findViewById(R.id.item_file_time);
		}
	}
}
</code></pre> 
<p>不要忘记在AndroidManifest.xml加权限哦！</p> 
<pre><code class="language-html"> &lt;!-- SD卡权限 --&gt;
    &lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /&gt;
    &lt;uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" /&gt;</code></pre> 
<span id="OSC_h2_2"></span>
<h2>&nbsp;</h2> 
