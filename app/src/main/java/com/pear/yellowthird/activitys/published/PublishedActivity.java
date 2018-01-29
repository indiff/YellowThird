package com.pear.yellowthird.activitys.published;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
                    chooseNewImage();
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
     * 选择新的突破
     * */
    private void chooseNewImage()
    {
        new PermissionsRequestInit(this)
                .permissionTipAndRequest(
                        "",
                        "我需要权限才能查看相册哦。\n请给我权限，否则我将不能正常工作",
                        new Runnable() {
                            @Override
                            public void run() {
                                startChooseCatalogue();
                            }

                            /**
                             * 跳转到选择图片的页面
                             * */
                            void startChooseCatalogue()
                            {
                                startActivity(
                                        new Intent(
                                        PublishedActivity.this,
                                        ChooseCataloguePicActivity.class));
                            }

                        }
                );
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
        Editable content= contentEditView.getText();
        if (!TextUtils.isEmpty(content)) {
            int maxLength=2000;
            if(content.length()>=maxLength)
            {
                Toast.makeText(this, "亲，说说文字不能超过"+maxLength+"个字符哦", Toast.LENGTH_SHORT).show();
                return;
            }
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
                        content.toString(),
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

                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
                FileUtils.deleteDir();
            }
        });


    }

}
