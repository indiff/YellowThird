package com.pear.yellowthird.impl.net;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;

import com.pear.common.utils.net.FileUploadUtils;
import com.pear.common.utils.net.HttpRequest;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.interfaces.ServiceDisposeInterface;
import com.pear.yellowthird.style.vo.BottomNavigationMenuVo;
import com.pear.yellowthird.vo.databases.BillVo;
import com.pear.yellowthird.vo.databases.FriendsVo;
import com.pear.yellowthird.vo.databases.UserVo;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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
     * 当前设备的唯一ID
     */
    private static String gDeviceId = "1";

    /**
     * 服务器的请求地址
     */
    private static final String gServiceHost = "http://192.168.0.104:10086/";

    /**
     *  初始化设备id
     * */
    public static void initDeviceId(Activity activity)
    {
        String androidID = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        gDeviceId = androidID +"_"+ Build.SERIAL;
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
                String response = requestByService(gServiceHost + "redbook/api/resourceType/list?1=1");
                subscriber.onNext(response);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public String queryVideoList(String sql) {
        return null;
    }

    @Override
    public String queryVideoComment(Integer id) {
        return requestByService(gServiceHost + "redbook/api/movie/listComment?id=" + id);
    }

    @Override
    public boolean addVideoComment(String id, String content) {
        String response = requestByService(gServiceHost + "redbook/api/movie/addComment?resourceId=" + id + "&content=" + content);
        return response.equals("true");
    }

    @Override
    public boolean addVideoClickGoodById(Integer id) {
        String response = requestByService(gServiceHost + "redbook/api/movie/like?id=" + id);
        return response.equals("true");
    }

    @Override
    public Observable<Boolean> addVideoUserCommentClickGood(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String response = requestByService(gServiceHost + "redbook/api/movie/commentLike?id=" + id);
                UserVo user = geUserVo(true);
                if (response.equals("true"))
                    subscriber.onNext(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> requestPlayVideo(final Integer id) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String response = requestByService(gServiceHost + "redbook/api/movie/play?id=" + id);
                subscriber.onNext(response);
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
        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... objects) {
                requestByServiceGetRaw(gServiceHost + "redbook/api/picture/view?id=" + id);
                return null;
            }
        }.execute();
        return true;
    }

    /**
     * 向服务器请求数据
     */
    private String requestByService(String url) {
        try {
            String response = requestByServiceGetRaw(url);
            System.out.println("response:" + response);
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");

            if (code == 200)
                return jsonObject.get("data").toString();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 从重复器请求数据，并且不处理结果。直接返回
     */
    private String requestByServiceGetRaw(String url) {
        try {
            /**每一个请求都会全局加上deviceID*/
            boolean hasParam = url.contains("?");
            url += (hasParam ? "&" : "?") + "deviceId=" + gDeviceId;
            System.out.println("url:" + url);
            String response = HttpRequest.sendGet(url);
            System.out.println("response:" + response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
                subscriber.onNext(url);
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
                subscriber.onNext(result.equals("true"));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public String getRechargeWebUrl() {
        return gServiceHost + "redbook/api/pay/index?deviceId=" + gDeviceId;
    }

    @Override
    public Observable<BillVo[]> getBilli() {
        return Observable.create(new Observable.OnSubscribe<BillVo[]>() {
            @Override
            public void call(Subscriber<? super BillVo[]> subscriber) {
                String data = requestByService(gServiceHost + "redbook/api/user/listOrder");
                BillVo[] datas = JsonUtil.write2Class(data, BillVo[].class);
                subscriber.onNext(datas);
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
                    subscriber.onError(new Exception());
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
                subscriber.onNext("true".equals(result));
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
                FriendsVo[] datas = JsonUtil.write2Class(data, FriendsVo[].class);
                subscriber.onNext(datas);
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
                FriendsVo[] datas = JsonUtil.write2Class(data, FriendsVo[].class);
                subscriber.onNext(datas);
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
                FriendsVo[] datas = JsonUtil.write2Class(data, FriendsVo[].class);
                subscriber.onNext(datas);
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
                                subscriber.onError(e.getCause());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                subscriber.onNext(true);
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
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> addFriendComment(final Integer id, final String content) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String result = requestByService(gServiceHost + "redbook/api/friendsHome/addComment?pid=" + id + "&content=" + content);
                if ("true".equals(result))
                    subscriber.onNext(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Boolean addFriendShowCount(final Integer id) {
        new AsyncTask<Object, Object, Object>(){
            @Override
            protected Object doInBackground(Object... objects) {
                requestByServiceGetRaw(gServiceHost + "redbook/api/friendsHome/showCount?id=" + id);
                return null;
            }
        }.execute();
        return true;
    }


}
