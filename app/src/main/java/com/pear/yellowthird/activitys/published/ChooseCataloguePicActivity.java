package com.pear.yellowthird.activitys.published;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.base.CommonHeadActivity;
import com.pear.yellowthird.adapter.CatalogueImageBucketAdapter;
import com.pear.yellowthird.vo.ImageBucket;

import java.io.Serializable;
import java.util.List;


/**
 * 显示本地的目录图片
 * */
public class ChooseCataloguePicActivity extends CommonHeadActivity {

	public static final String EXTRA_IMAGE_LIST = "image_list";

	/**目录列表*/
	List<ImageBucket> catalogueList;

	/**目录列表视图*/
	GridView catalogueListView;

	/**自定义的目录适配器*/
	CatalogueImageBucketAdapter adapter;

	/**读取本地图片的助手类*/
	AlbumHelper helper;

	/**用于缓存作用*/
	public static Bitmap bimap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_published_catalogue_image_bucket_list);


		initHeadBar("相册");
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		catalogueList = helper.getImagesBucketList(false);

		bimap= BitmapFactory.decodeResource(
				getResources(),
				R.drawable._add_image);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		catalogueListView = findViewById(R.id.image_list);
		adapter = new CatalogueImageBucketAdapter(this, catalogueList);
		catalogueListView.setAdapter(adapter);

		/**跳转到目录下的子图片*/
		catalogueListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
 				Intent intent = new Intent(ChooseCataloguePicActivity.this,
						null);
				intent.putExtra(EXTRA_IMAGE_LIST,
						(Serializable) catalogueList.get(position).imageList);
				startActivity(intent);
				finish();
			}

		});
	}
}
