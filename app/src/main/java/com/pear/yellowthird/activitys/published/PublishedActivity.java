package com.pear.yellowthird.activitys.published;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.base.CommonHeadActivity;
import com.pear.yellowthird.adapter.PublishedImageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 发表说说
 * */
public class PublishedActivity extends CommonHeadActivity {

	/**准备上传的图片集合视图*/
	private GridView imageListView;

	/**图片集合的适配器*/
	private PublishedImageAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_published);

		initHeadBar("写说说","发表",sendListener);
		Init();
	}

	/**
	 * 初始化   
	 * */
	public void Init() {
		imageListView = findViewById(R.id.image_list);
		imageListView.setSelector(new ColorDrawable(Color.TRANSPARENT));

		adapter = new PublishedImageAdapter(this);
		adapter.update();
		imageListView.setAdapter(adapter);

		/**监听图片集合的点击事件*/
		imageListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				/**点中了添加选项，直接跳转到图片目录浏览待添加页面*/
				if (position == Bimp.bmp.size()) {
					startActivity(
							new Intent(
									PublishedActivity.this,
									ChooseCataloguePicActivity.class));
				}
				/** 暂时不提供编辑修改功能
				else {
					Intent intent = new Intent(PublishedActivity.this, PhotoActivity.class );
					intent.putExtra("ID", position);
					startActivity(intent);
				}
				*/
			}
		});

	}


	/**
	 * 发表说说
	 * */
	View.OnClickListener sendListener= new OnClickListener() {
		@Override
		public void onClick(View view) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < Bimp.drr.size(); i++) {
				String Str = Bimp.drr.get(i).substring(
						Bimp.drr.get(i).lastIndexOf("/") + 1,
						Bimp.drr.get(i).lastIndexOf("."));
				list.add(FileUtils.SDPATH+Str+".JPEG");
				File fii=new File(list.get(i).toString());
				Log.d("Pu", "str=="+fii.getName());
				Log.d("Pu", "str=="+list.get(i).toString());
			}
			// 高清的压缩图片全部就在  list 路径里面了
			// 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
			// 完成上传服务器后 .........
			FileUtils.deleteDir();
		}
	};



}
