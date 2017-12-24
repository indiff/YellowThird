package com.pear.yellowthird.activitys.published;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.base.CommonHeadActivity;
import com.pear.yellowthird.adapter.ChooseImageAdapter;
import com.pear.yellowthird.vo.ImageItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**选择待发表的图片*/
public class ChooseImageActivity extends CommonHeadActivity {


	/**当前目录下的图片集合*/
	List<ImageItem> dataList;

	/**列表视图*/
	GridView imageListView;

	/**自定义的列表适配器*/
	ChooseImageAdapter adapter;

	/**读取本地图片的助手类*/
	AlbumHelper helper;

	/**选中完图片，点击完成*/
	Button completeButton;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ChooseImageActivity.this, "最多选择9张图片", 400).show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sub_published_choose_image_list);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				ChooseCataloguePicActivity.EXTRA_IMAGE_LIST);

		initView();
		initHeadBar("相册","完成");
	}

	/**
	 * 初始化待选择的视图
	 */
	private void initView() {
		imageListView = findViewById(R.id.image_list);
		imageListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ChooseImageAdapter(ChooseImageActivity.this, dataList,
				mHandler);
		imageListView.setAdapter(adapter);

		adapter.setTextCallback(new ChooseImageAdapter.TextCallback() {
			public void onListen(int count) {
				rightTitleView.setText("完成(" + count + ")");
			}
		});

		imageListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.head_right:
				completeChoose();
		}
	}
	/**
	 * 点击完成选择图片
	 * */
	private void completeChoose() {
		ArrayList<String> list = new ArrayList<String>();
		Collection<String> c = adapter.map.values();
		Iterator<String> it = c.iterator();
		for (; it.hasNext();) {
			list.add(it.next());
		}
		/***/
		for (int i = 0; i < list.size(); i++) {
			if (Bimp.drr.size() < 9) {
				Bimp.drr.add(list.get(i));
			}
		}
		if (Bimp.act_bool) {
			//Intent intent = new Intent(ChooseImageActivity.this,PublishedActivity.class);
			//startActivity(intent);
			/**finish完也是回到发说说哪里了*/
			Bimp.act_bool = false;
		}
		finish();
	};

}
