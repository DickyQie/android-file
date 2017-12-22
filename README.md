###Android----- 版本更新和 服务器下载新版本APK并安装 
   <p>前段时间有朋友问我版本更新的问题，所以来写一篇版本更新和APK下载并安装的博客。</p> 
<p>版本更新，几乎在所有的项目中都用的到，一般是这样的流程，当进入APP首页是便会检测版本是否为最新版本，不是则提示你下载更新；</p> 
<p>版本更新需要后台和移动端共同来完成，后台一般用一个接口 来返回给移动端最新版本的信息，移动端接收信息后和APP自身版本比较，如果不相同则提示用户更新 APP。</p> 
<p>所以当移动端开发者做完并更新版本后 打包提交到后台，后台更新APP版本信息，就可以完成版本更新了。</p> 
<p>看看我的项目的效果图：</p> 
<p>　　　<img alt="" height="564" src="https://static.oschina.net/uploads/img/201709/15140337_xlaO.gif" width="350"></p> 
<p>&nbsp;</p> 
<p><strong><span style="color:#800000">首页进来开始检测&nbsp; 提示更新APP，点击确定，下载APK文件，之后跳转安装见面，点击安装，安装完成后，清除安装包。</span></strong></p> 
<p>&nbsp;</p> 
<p>获取移动端版本号</p> 
<pre><code class="language-java">/****
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
    }</code></pre> 
<p>网络请求数据解析比较，弹框提示 下载更新</p> 
<pre><code class="language-java"> String version="服务器版本";
 if (!version.equals(Util.getVersion(MainActivity.this))){
        centerDialog = new CenterDialog(MainActivity.this,
          R.layout.dialog_center_layout, new int[]{R.id.dialog_cancel, R.id.dialog_sure},
          "服务器APK下载链接");
        centerDialog.setOnCenterItemClickListener(MainActivity.this);
        centerDialog.show();
    }</code></pre> 
<p>&nbsp;</p> 
<p>之后就根据链接，下载APK，并安装</p> 
<pre><code class="language-java">/****
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

    }</code></pre> 
<pre><code class="language-java">/***
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
    }</code></pre> 
<p>&nbsp;</p> 
<p>我的代码是写的比较简单，不过功能是可以实现的，你可以自己封装。网络请求和下载文件可用于其他方式实现，<strong><span style="color:#800000">测试接口我删除了的，</span></strong></p> 
<p>&nbsp;</p> 
