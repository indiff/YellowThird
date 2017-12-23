package com.pear.yellowthird.activitys.fragments.mainSubFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pear.yellowthird.activitys.BillActivity;
import com.pear.yellowthird.activitys.FullImagePageActivity;
import com.pear.yellowthird.activitys.R;
import com.pear.yellowthird.activitys.RechargeActivity;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.vo.databases.UserVo;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.functions.Action1;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 我的账户界面
 */
public class AccountInfoFragment extends Fragment {

    private UserVo user;

    /**
     * 不能直接提供构造器来实现。会出现编译错误。
     * 具体原因请参考 http://blog.csdn.net/chniccs/article/details/51258972
     */
    public static Fragment newInstance(UserVo user) {
        AccountInfoFragment fragment = new AccountInfoFragment();
        fragment.user=user;
        return fragment;
    }

    private View mRootView;

    /**用户的头像*/
    ImageView userHeadIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mRootView)
            return mRootView;

        mRootView = inflater.inflate(R.layout.account_info, null);

        /**用户的头像*/
        {
            userHeadIcon = mRootView.findViewById(R.id.user_head_icon);
            Glide.with(getContext())
                    .load(user.getThumb())
                    .apply(bitmapTransform(new CropCircleTransformation()))
                    .into(userHeadIcon);
            onChangeUserIcon(userHeadIcon);

            /**刷新用户头像*/
            ImageView  refreshHeadIcon= mRootView.findViewById(R.id.refresh_head_icon);
            onChangeUserIcon(refreshHeadIcon);
        }

        /**
         * 用户名称
         * */
        {
            EditText userNameView= mRootView.findViewById(R.id.user_name);
            userNameView.setText(user.getName());
            onChangeUserName(userNameView);
        }

        /**余额*/
        {
            TextView goldView= mRootView.findViewById(R.id.gold);
            goldView.setText(user.getGold()+" 绿币");
        }

        /**充值*/
        {
            LinearLayout rechargeView= mRootView.findViewById(R.id.recharge);
            rechargeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), RechargeActivity.class));
                }
            });
        }

        /**账单*/
        {
            LinearLayout billView= mRootView.findViewById(R.id.bill);
            billView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), BillActivity.class));
                }
            });
        }

        return mRootView;
    }


    /**
     * 用户更换头像
     * */
    void onChangeUserIcon(final ImageView imageView)
    {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceDisposeFactory.getInstance().getServiceDispose().randomUserIcon()
                        .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        Glide.with(getContext())
                                .load(url)
                                .apply(bitmapTransform(new CropCircleTransformation()))
                                .into(userHeadIcon);
                    }
                });;
            }
        });
    }


    /**
     * 用户更换名称
     * */
    void onChangeUserName(final EditText textView)
    {

        /**
         * 监听回车事件
         * */
        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    changeUserName();
                }
                return true;
            }

            /**
             * 更换用户名称
             * */
            void changeUserName()
            {
                final String text=textView.getText().toString();
                if(TextUtils.isEmpty(text)||text.trim().isEmpty())
                {
                    Toast.makeText(getContext(),"名称不能为空",Toast.LENGTH_SHORT).show();
                    return ;
                }
                else if(text.length()>12)
                {
                    Toast.makeText(getContext(),"名称太长了",Toast.LENGTH_SHORT).show();
                    return ;
                }
                ServiceDisposeFactory.getInstance().getServiceDispose().changeUserName(text)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean result) {
                                textView.clearFocus();

                                hideSoftInput(getActivity(),textView);
                                Toast.makeText(getContext(),"修改名称成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                return ;
            }

            /**
             * 必须手动隐藏键盘
             * */
            void hideSoftInput(Activity activity, EditText input)
            {
                InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(),0);
            }

        });
    }


}