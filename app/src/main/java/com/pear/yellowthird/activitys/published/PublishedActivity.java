package com.pear.yellowthird.activitys.published;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pear.android.listener.empty.EmptyRunnable;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.base.CommonHeadActivity;
import com.pear.yellowthird.adapter.PublishedImageAdapter;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.init.PermissionsRequestInit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;


/**
 * 发表说说
 */
public class PublishedActivity extends CommonHeadActivity {

    /**
     * 准备上传的图片集合视图
     */
    private GridView imageListView;

    /**
     * 图片集合的适配器
     */
    private PublishedImageAdapter adapter;

    /**
     * 说说内容
     */
    private EditText contentEditView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_published);

        /**新建的就清空上一次的数据*/
        Bimp.clear();
        initHeadBar("写说说", "发表");
        Init();

        /**申请权限*/
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                new PermissionsRequestInit(PublishedActivity.this).init(new EmptyRunnable());
                return null;
            }
        }.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /**更新一下已选中的视图*/
        adapter.update();
    }


    /**
     * 初始化
     */
    public void Init() {
        contentEditView = findViewById(R.id.content);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_right:
                sendTalk();
        }
    }

    /**
     * 发表说说
     */
    public void sendTalk() {
        if (TextUtils.isEmpty(contentEditView.getText())) {
            //Toast.makeText(this, "亲，你的说说都还没写呢", Toast.LENGTH_SHORT).show();
            //return;
            //说说可以为空。微信是可以的
            contentEditView.setText("");
        }
        if (Bimp.drr.isEmpty()) {
            Toast.makeText(this, "亲，赏两张照片呗", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < Bimp.drr.size(); i++) {
            String Str = Bimp.drr.get(i).substring(
                    Bimp.drr.get(i).lastIndexOf("/") + 1,
                    Bimp.drr.get(i).lastIndexOf("."));
            list.add(FileUtils.SDPATH + Str + ".JPEG");
            File fii = new File(list.get(i).toString());
            Log.d("Pu", "str==" + fii.getName());
            Log.d("Pu", "str==" + list.get(i).toString());
        }


        /**发表过程中一直等待*/
        final MaterialDialog progressDialog=new MaterialDialog.Builder(this)
                .title("正在发表说说...")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .show();

        ServiceDisposeFactory.getInstance().getServiceDispose()
                .publishFriendTalk(
                        contentEditView.getText().toString(),
                        list)
                .subscribe(new Action1<Object>() {
            @Override
            public void call(Object data) {
                progressDialog.dismiss();
                Toast.makeText(
                        PublishedActivity.this,
                        "说说发表成功",
                        Toast.LENGTH_SHORT).show();
                PublishedActivity.this.finish();
            }
        });

        // 高清的压缩图片全部就在  list 路径里面了
        // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
        // 完成上传服务器后 .........
        FileUtils.deleteDir();
    }

}
