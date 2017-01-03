package com.example.fileoperation;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/****
 * 
 * 更具指定路径获取对应文件
 * 
 */

public class MainActivity extends Activity {

	private ListView mListview;
	private List<AddFileInfo> list;

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
