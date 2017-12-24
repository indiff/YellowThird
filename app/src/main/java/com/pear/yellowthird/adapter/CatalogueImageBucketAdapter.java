package com.pear.yellowthird.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.published.BitmapCache;
import com.pear.yellowthird.vo.ImageBucket;

import java.util.List;


/**
 * 发表说说的得选择图片目录的适配器
 * */
public class CatalogueImageBucketAdapter extends BaseAdapter {
	final String TAG = getClass().getSimpleName();

	Activity act;

	/**
	 * 图片集列表
	 */
	List<ImageBucket> dataList;
	BitmapCache cache;
	BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};

	public CatalogueImageBucketAdapter(Activity act, List<ImageBucket> list) {
		this.act = act;
		dataList = list;
		cache = new BitmapCache();
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	class Holder {

		/**概要略缩图*/
		private ImageView iv;

		/**目录名称*/
		private TextView name;

		/**当前目录下的拥有图片数量*/
		private TextView count;

	}

	@Override
	public View getView(int position, View rootView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder;
		if (rootView == null) {
			holder = new Holder();
			rootView = View.inflate(act, R.layout.sub_published_catalogue_image_line, null);
			holder.iv = (ImageView) rootView.findViewById(R.id.image);
			holder.name = (TextView) rootView.findViewById(R.id.name);
			holder.count = (TextView) rootView.findViewById(R.id.count);
			rootView.setTag(holder);
		} else {
			holder = (Holder) rootView.getTag();
		}
		ImageBucket item = dataList.get(position);
		holder.count.setText("" + item.count);
		holder.name.setText(item.bucketName);
		if (item.imageList != null && item.imageList.size() > 0) {
			String thumbPath = item.imageList.get(0).thumbnailPath;
			String sourcePath = item.imageList.get(0).imagePath;
			holder.iv.setTag(sourcePath);
			cache.displayBmp(holder.iv, thumbPath, sourcePath, callback);
		} else {
			holder.iv.setImageBitmap(null);
			Log.e(TAG, "no images in bucket " + item.bucketName);
		}
		return rootView;
	}

}
