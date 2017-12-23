package com.pear.yellowthird.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.published.Bimp;
import com.pear.yellowthird.activitys.published.BitmapCache;
import com.pear.yellowthird.vo.ImageItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 发表说说 的选择图片适配器
 * */
public class ChooseImageAdapter extends BaseAdapter {

	final String TAG = getClass().getSimpleName();

	/**选中项变动回调*/
	private TextCallback textcallback = null;

	/**活动中的activity*/
	Activity activity;

	/**当前目录下所拥有的所有图片*/
	List<ImageItem> dataList;


	/**这个是什么鬼*/
	public Map<String, String> map = new HashMap<String, String>();

	/**图片缓存工厂*/
	BitmapCache cache;

	/**主线程的handle*/
	private Handler mHandler;

	/**已选中的所有图片总数*/
	private int selectTotal = 0;

	BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					imageView.setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};


	/**已选择的张数监听接口*/
	public static interface TextCallback {
		public void onListen(int count);
	}

	public void setTextCallback(TextCallback listener) {
		textcallback = listener;
	}

	public ChooseImageAdapter(Activity act, List<ImageItem> list, Handler mHandler) {
		this.activity = act;
		dataList = list;
		cache = new BitmapCache();
		this.mHandler = mHandler;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}


	class Holder {

		/**图片名称*/
		private TextView nameView;

		/**图片的略缩图*/
		private ImageView imageView;

		/**是否已选中当前的图片*/
		private ImageView isSelectedView;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(activity, R.layout.sub_published_choose_image_list_line, null);
			holder.imageView =  convertView.findViewById(R.id.image);
			holder.isSelectedView = convertView
					.findViewById(R.id.is_selected);
			holder.nameView = convertView
					.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ImageItem item = dataList.get(position);

		holder.nameView.setTag(item.imagePath);
		cache.displayBmp(holder.imageView, item.thumbnailPath, item.imagePath,
				callback);
		if (item.isSelected) {
			holder.isSelectedView.setImageResource(R.drawable._select_image_tip);
			holder.nameView.setBackgroundResource(R.drawable.friend_image_select_name);
		} else {
			holder.isSelectedView.setImageResource(-1);
			holder.nameView.setBackgroundColor(0x00000000);
		}
		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String path = dataList.get(position).imagePath;

				if ((Bimp.drr.size() + selectTotal) < 9) {
					item.isSelected = !item.isSelected;
					if (item.isSelected) {
						holder.isSelectedView
								.setImageResource(R.drawable._select_image_tip);
						holder.nameView.setBackgroundResource(R.drawable.friend_image_select_name);
						selectTotal++;
						if (textcallback != null)
							textcallback.onListen(selectTotal);
						map.put(path, path);

					} else if (!item.isSelected) {
						holder.isSelectedView.setImageResource(-1);
						holder.nameView.setBackgroundColor(0x00000000);
						selectTotal--;
						if (textcallback != null)
							textcallback.onListen(selectTotal);
						map.remove(path);
					}
				} else if ((Bimp.drr.size() + selectTotal) >= 9) {
					if (item.isSelected == true) {
						item.isSelected = !item.isSelected;
						holder.isSelectedView.setImageResource(-1);
						selectTotal--;
						map.remove(path);

					} else {
						Message message = Message.obtain(mHandler, 0);
						message.sendToTarget();
					}
				}
			}

		});

		return convertView;
	}



	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
