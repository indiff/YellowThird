package com.pear.yellowthird.impl.net;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.pear.android.app.GlobalApplication;
import com.pear.common.utils.net.FileUploadUtils;
import com.pear.common.utils.net.HttpRequest;
import com.pear.common.utils.strings.JasyptUtils;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.databases.FastShakeDatabases;
import com.pear.yellowthird.config.SystemConfig;
import com.pear.yellowthird.factory.ServiceDisposeFactory;
import com.pear.yellowthird.interfaces.ServiceDisposeInterface;
import com.pear.yellowthird.vo.databases.BillVo;
import com.pear.yellowthird.vo.databases.CommentVo;
import com.pear.yellowthird.vo.databases.FastShakeVo;
import com.pear.yellowthird.vo.databases.FriendsVo;
import com.pear.yellowthird.vo.databases.ImageIntroduceVo;
import com.pear.yellowthird.vo.databases.TalkComment;
import com.pear.yellowthird.vo.databases.UserVo;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 服务器的具体交互实现
 */

public class ServiceDisposeImpl implements ServiceDisposeInterface {

    /**
     * 日记
     */
    private static Logger log = Logger.getLogger(ServiceDisposeImpl.class);

    /**
     * 当前设备的唯一ID
     */
    private static String gDeviceId = "3";

    /**
     * 服务器的请求地址
     */
    //private static final String gServiceHost = "http://smalltadpole.net/";
    //等买了国内的域名再换吧。可能有部分网络解析这个域名不顺利，我自己虚拟机都试过好多次。
    //也有可能是香港公网抖动的问题。两种都有
    private static String gServiceHost;//= "http://36.255.220.149/";

    /**
     * 007 =10<br>
     luck =20<br>
     jack =30<br>
     35=40<br>
     * */

    /**所属主人*/
    private static String MASTER="2";

    /**来源 备注*/
    private static String SOURCE_MEMO="0";

    private Handler mainHandler;

    public ServiceDisposeImpl() {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    //所有的备份域名
    private List<String> allHostList = new ArrayList<String>() {{
        boolean localPcTest = false;
        if (localPcTest)
        {
            String localHost="http://192.168.0.109:8080/";
            //gServiceHost=localHost;
            add("127.0.0.1:8080/");
            add(localHost);
        }else {
            add("http://kedouxiaoapi.top/");
            add("http://xiaokedoutop.top/");
            add("http://kedouxiao.com/");

            //虚拟机域名转不了ip。不知道部分网络会不会。就是只有虚拟机会。
            add("http://36.255.220.149/");
            add("http://128.1.136.193/");
            add("http://smallkedou.cn/");
        }
    }};

    /**
     * 初始化设备id
     */
    public static void initDeviceId(Activity activity) {
        String androidID = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        gDeviceId = androidID + "_" + Build.SERIAL;
        //gDeviceId = "e5730ad1177c48679b_98897a314c385137";
        log.info("gDeviceId" + gDeviceId);
    }

    /**
     * 弹窗提示错误信息
     */
    private void errorCommonTip() {
        /*
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GlobalApplication.getContext(),
                        "跟服务器请求中，服务器处理改指令出错",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        */
    }

    /**
     * 出现错误
     */
    private void debugErrorCommonTip(final String errorMsg) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(GlobalApplication.getContext(),
                        /*errorMsg*/"网络不给力，请检查网络设置",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

    }

    /**
     * 缓存的用户信息
     */
    private UserVo cacheUserVo;

    @Override
    public Observable<String> queryMainMenu() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String response = requestByService(
                        gServiceHost + "redbook/api/resourceType/list?version=" + SystemConfig.VERSION
                        +"&master="+MASTER+"&sourceMemo="+SOURCE_MEMO
                );
                if (!TextUtils.isEmpty(response))
                    subscriber.onNext(response);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> queryVideoComment(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                final String response = requestByService(gServiceHost + "redbook/api/movie/listComment?id=" + id);
                if (!TextUtils.isEmpty(response))
                    subscriber.onNext(response);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> addVideoComment(final String id, final String content) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String response = "";
                try {
                    response = requestByService(gServiceHost + "redbook/api/movie/addComment?resourceId=" + id + "&content=" + URLEncoder.encode(content, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(response))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> addVideoClickGoodById(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                final String response
                        = requestByService(gServiceHost + "redbook/api/movie/like?id=" + id);
                if (!TextUtils.isEmpty(response))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> addVideoUserCommentClickGood(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String response = requestByService(gServiceHost + "redbook/api/movie/commentLike?id=" + id);
                if (!TextUtils.isEmpty(response))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> requestPlayVideo(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String response = requestByService(gServiceHost + "redbook/api/movie/play?id=" + id + "&version=" + SystemConfig.VERSION);
                if (!TextUtils.isEmpty(response))
                    subscriber.onNext(response);
                else {
                    errorCommonTip();
                    subscriber.onError(new Throwable());
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> requestJumpPlayVideo(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String response = requestByService(gServiceHost + "redbook/api/movie/highTide?id=" + id);
                if (!TextUtils.isEmpty(response))
                    subscriber.onNext(response);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public String queryByUrl(String url) {
        return requestByService(url);
    }

    @Override
    public Boolean addImageShowCount(final Integer id) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                requestByService(gServiceHost + "redbook/api/picture/view?id=" + id);
                return null;
            }
        }.execute();
        return true;
    }


    /**
     * 获取用户数据
     *
     * @param forceNew 是否强制刷新数据，如果是false，则会从本地缓存中去
     */
    private UserVo geUserVo(boolean forceNew) {
        if (!forceNew && null != cacheUserVo)
            return cacheUserVo;
        String response = requestByService(gServiceHost + "redbook/api/user/add?1=1");
        if (!TextUtils.isEmpty(response))
            cacheUserVo = JsonUtil.write2Class(response, UserVo.class);

        return cacheUserVo;
    }

    @Override
    public Observable<UserVo> getUser() {
        return Observable.create(new Observable.OnSubscribe<UserVo>() {
            @Override
            public void call(Subscriber<? super UserVo> subscriber) {
                UserVo user = geUserVo(false);
                if (null != user)
                    subscriber.onNext(user);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserVo> refreshUser() {
        return Observable.create(new Observable.OnSubscribe<UserVo>() {
            @Override
            public void call(Subscriber<? super UserVo> subscriber) {
                UserVo user = geUserVo(true);
                if (null != user)
                    subscriber.onNext(user);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> randomUserIcon() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String url = requestByService(gServiceHost + "redbook/api/user/randomIcon");
                if (!TextUtils.isEmpty(url))
                    subscriber.onNext(url);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> changeUserName(final String name) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String result = requestByService(gServiceHost + "redbook/api/user/changeName?name=" + name);
                if (!TextUtils.isEmpty(result))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public String getRechargeWebUrl() {
        return gServiceHost + "redbook/api/pay/index?deviceId=" + gDeviceId + "&random=" + System.currentTimeMillis();
    }

    @Override
    public String getSharedWebUrl() {
        return gServiceHost + "redbook/api/shared/index?deviceId=" + gDeviceId + "&random=" + System.currentTimeMillis();
    }

    @Override
    public Observable<BillVo[]> getBilli() {
        return Observable.create(new Observable.OnSubscribe<BillVo[]>() {
            @Override
            public void call(Subscriber<? super BillVo[]> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/user/listOrder");
                if (!TextUtils.isEmpty(data)) {
                    BillVo[] datas = JsonUtil.write2Class(data, BillVo[].class);
                    subscriber.onNext(datas);
                } else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> clickVote(final int voteId, final int subSelectId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String data = requestByService(gServiceHost
                        + "redbook/api/vote/vote?vid=" + voteId + "&sid=" + subSelectId);
                if (data.equals("true"))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> addImageClickGood(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String result = requestByService(gServiceHost + "redbook/api/picture/like?id=" + id);
                if (!TextUtils.isEmpty(result))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<FriendsVo[]> queryFriendList() {
        return Observable.create(new Observable.OnSubscribe<FriendsVo[]>() {
            @Override
            public void call(Subscriber<? super FriendsVo[]> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/friendsHome/listData?init=1");
                if (!TextUtils.isEmpty(data)) {
                    FriendsVo[] datas = JsonUtil.write2Class(data, FriendsVo[].class);
                    subscriber.onNext(datas);
                } else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<FriendsVo[]> queryMoreFriendList(final Integer lastId) {
        return Observable.create(new Observable.OnSubscribe<FriendsVo[]>() {
            @Override
            public void call(Subscriber<? super FriendsVo[]> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/friendsHome/listData?orientation=1&id=" + lastId);
                if (!TextUtils.isEmpty(data)) {
                    FriendsVo[] datas = JsonUtil.write2Class(data, FriendsVo[].class);
                    subscriber.onNext(datas);
                } else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<FriendsVo[]> refreshNewHeadFriendList(final Integer firstId) {
        return Observable.create(new Observable.OnSubscribe<FriendsVo[]>() {
            @Override
            public void call(Subscriber<? super FriendsVo[]> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/friendsHome/listData?orientation=0&id=" + firstId);
                if (!TextUtils.isEmpty(data)) {
                    FriendsVo[] datas = JsonUtil.write2Class(data, FriendsVo[].class);
                    subscriber.onNext(datas);
                } else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> publishFriendTalk(final String content, final List<String> images) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                FileUploadUtils.uploadImg(
                        gServiceHost + "redbook/api/friendsHome/add",
                        content,
                        gDeviceId,
                        images,
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                errorCommonTip();
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            }
                        }
                );
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> friendClickGood(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String result = requestByService(gServiceHost + "redbook/api/friendsHome/goodCount?id=" + id);
                if ("true".equals(result))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> addFriendComment(final Integer id, final String content) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String result = "";
                try {
                    result = requestByService(gServiceHost + "redbook/api/friendsHome/addComment?pid=" + id + "&content=" + URLEncoder.encode(content, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if ("true".equals(result))
                    subscriber.onNext(true);
                else
                    errorCommonTip();

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Boolean addFriendShowCount(final Integer id) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                requestByService(gServiceHost + "redbook/api/friendsHome/showCount?id=" + id);
                return null;
            }
        }.execute();
        return true;
    }

    @Override
    public Observable<String> getNewsVersion() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/apk/version");
                if (!TextUtils.isEmpty(data))
                    subscriber.onNext(data);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void sendAppCrashServer(final String content) {
        log.error(content);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = HttpRequest.sendPost(
                            gServiceHost + "redbook/api/eventApi/addErrorLog",
                            "deviceId=" + gDeviceId + "&content=" + URLEncoder.encode(content, "utf-8"));
                    System.out.println("result:" + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取最快连上服务器的host
     */
    public void autoChooseGoodService(final Runnable success) {
        /**
         * 只选一个可以的就可以了
         * */
        class JustChooseOnce {
            boolean hasChoose=false;
            boolean hasFailTip=false;
            Object lock=new Object();

            public void isGood(String ip) {
                synchronized (lock) {
                    if(hasChoose)
                    {
                        System.out.println("already has good ip :"+ip);
                        return;
                    }
                    hasChoose=true;
                    gServiceHost = ip;
                    success.run();
                    System.out.println("test good ip："+ip);
                }
            }
            public void isFail(String ip)
            {
                synchronized (lock)
                {
                    if(hasChoose||hasFailTip)
                        return;
                    hasFailTip=true;
                    Toast.makeText(GlobalApplication.getContext(),
                            "网络不给力，请切换4g或wifi再试试！",
                            Toast.LENGTH_LONG).show();
                }
            }
        };
        final JustChooseOnce justOnce= new JustChooseOnce();
        for(int i=0;i<allHostList.size();i++)
        {
            final String testHost=allHostList.get(i);
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    String data = requestByService(
                            testHost + "redbook/api/verify/handshake");
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Action1<String>() {
                                @Override
                                public void call(String data) {
                                    if (!TextUtils.isEmpty(data) && "true".equals(data)) {
                                        log.error("测试 testHost成功:" + testHost);
                                        justOnce.isGood(testHost);
                                    } else {
                                        log.error("测试 testHost失败:" + testHost);
                                        justOnce.isFail(testHost);
                                    }
                                }
                            });
        }
    }



    @Override
    public Observable<FastShakeVo[]> getFastShakeList() {
        return Observable.create(new Observable.OnSubscribe<FastShakeVo[]>() {
            @Override
            public void call(Subscriber<? super FastShakeVo[]> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/fast_shake/list");
                if (!TextUtils.isEmpty(data))
                {
                    FastShakeVo[] list = JsonUtil.write2Class(data, FastShakeVo[].class);
                    subscriber.onNext(list);
                } else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> getFastShakeCommentById(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/fast_shake/listComment?id="+id);
                if (!TextUtils.isEmpty(data))
                    subscriber.onNext(data);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> addFastShakeComment(final Integer userId,final Integer pid,final String content) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String data = "";
                try {
                    data = requestByService(gServiceHost + "redbook/api/fast_shake/addComment?user.id="+userId+"&pid="+pid+"&content="+URLEncoder.encode(content, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(data)&&"true".equals(data))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> addFastShakeLove(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/fast_shake/likeFastShake?id="+id);
                if (!TextUtils.isEmpty(data)&&"true".equals(data))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> addFastShakeShowCount(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/fast_shake/addPlayCount?id="+id);
                if (!TextUtils.isEmpty(data)&&"true".equals(data))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> addFastShakeCommentGoodCount(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/fast_shake/addCommentGoodCount?id="+id);
                if (!TextUtils.isEmpty(data)&&"true".equals(data))
                    subscriber.onNext(true);
                else
                    errorCommonTip();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 向服务器请求数据
     */
    private String requestByService(String url, int timeoutMilli) {
        try {
            /**每一个请求都会全局加上deviceID*/
            boolean hasParam = url.contains("?");

            url += (hasParam ? "&" : "?") + "deviceId=" + gDeviceId + getDebugPublicTimeParam();
            url+="&host="+gServiceHost;
            log.info("url:" + url);

            /*
            long randomTime=System.currentTimeMillis();
            url+="&randomTime="+randomTime;
            */
            //get对中文支持不好，全部改为post
            //String response = HttpRequest.sendGet(url);
            String[] postParams = url.split("\\?");
            String response = HttpRequest.sendPost(postParams[0], postParams[1], timeoutMilli);

            //TODO 云端还没接
            //不加密了，云端一大堆bug
            //response=JasyptUtils.decode(response);
            log.info("response:" + response);
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200)
                return jsonObject.get("data").toString();
            log.error("response error " + jsonObject.get("msg").toString());

            debugErrorCommonTip(
                    "服务器拒绝该指令\n " +
                            "url:" + url + "\n" +
                            "response:" + response);
            return "";
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            debugErrorCommonTip(
                    "服务器异常\n " +
                            "url:" + url + "\n" +
                            "服务器异常:" + e.getMessage());
            return "";
        }
    }

    /**
     * 向服务器请求数据
     */
    private String requestByService(String url) {
        return requestByService(url, HttpRequest.TIME_OUT_SECONDS*1000);
    }

    /**
     * 获取调试的发布时间
     */
    public String getDebugPublicTimeParam() {
        /**是否启用调试时间*/
        Date queryDate = new Date(SystemConfig.getInstance().getDebugTimeSwitch() ? SystemConfig.getInstance().getQueryTime() : System.currentTimeMillis());
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(queryDate);
        return "&publishTime=" + dateFormat.replace(" ","%20");
    }

}
