package com.pear.yellowthird.impl.net;

import com.pear.common.utils.net.HttpRequest;
import com.pear.common.utils.strings.JsonUtil;
import com.pear.yellowthird.interfaces.ServiceDisposeInterface;
import com.pear.yellowthird.style.vo.BottomNavigationMenuVo;
import com.pear.yellowthird.vo.databases.BillVo;
import com.pear.yellowthird.vo.databases.UserVo;

import org.json.JSONObject;

import java.util.List;

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
    private static final String gDeviceId = "1";

    /**
     * 服务器的请求地址
     */
    private static final String gServiceHost = "http://192.168.0.104:10086/";

    /**
     * 缓存的用户信息
     * */
    private UserVo cacheUserVo;

    @Override
    public Observable<String> queryMainMenu() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String response = requestByService(gServiceHost + "redbook/api/resourceType/list?1=1");
                //String response="[{\"id\":7,\"pid\":0,\"title\":\"社区\",\"description\":\"社区\",\"icon\":\"community\",\"dataType\":\"raw\",\"style\":\"sub_tab_menu\",\"dataSource\":\"\",\"data\":\"[{\"data\":\"[{\"id\":1,\"title\":\"App简介与优势\",\"content\":\"Siri是苹果公司在其产品iPhone4S，iPad 3及以上版本手机和Mac上应用的一项智能语音控制功能。Siri可以令iPhone4S及以上手机（iPad 3以上平板）变身为一台智能化机器人，利用Siri用户可以通过手机读短信、介绍餐厅、询问天气、语音设置闹钟等。\\r\\n\",\"imageUri\":\"https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image\\u0026quality\\u003d100\\u0026size\\u003db4000_4000\\u0026sec\\u003d1511963508\\u0026di\\u003df2074431a404a274b5eb8b650a63e3d4\\u0026src\\u003dhttp://image.tianjimedia.com/uploadImages/2015/215/45/04L5VRR21C5W.jpg\"},{\"id\":2,\"title\":\"收费模式\",\"content\":\"\",\"imageUri\":\"https://timgsa.baidu.com/timg?image\\u0026quality\\u003d80\\u0026size\\u003db9999_10000\\u0026sec\\u003d1509558934456\\u0026di\\u003d26bd04cba698b74bf9e09521806492cc\\u0026imgtype\\u003d0\\u0026src\\u003dhttp%3A%2F%2Fimage.l99.com%2Fad8%2F1437453022715_5swgd5.jpg\"},{\"id\":3,\"title\":\"我要成为会员\",\"content\":\"您可以通过云网进行充值成为普通VIP会员，普通VIP会员申请不限制充值数额。可以通过缴纳30元会员费，直接升级成为新浪原创白金VIP会员\"}]\",\"dataSource\":\"raw\",\"dataType\":\"query_intro\",\"description\":\"简介\",\"id\":8,\"pid\":7,\"sql\":\"http://192.168.0.104:10086/redbook/api/news/list/8\",\"style\":\"news\",\"title\":\"简介\"}]\"}]";
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
        return requestByService(gServiceHost + "redbook/api/movie/listComment?id="+id);
    }

    @Override
    public boolean addVideoComment(String id, String content) {
        String response = requestByService(gServiceHost + "redbook/api/movie/addComment?resourceId="+id+"&content="+content);
        return response.equals("true");
    }

    @Override
    public boolean addVideoClickGoodById(Integer id) {
        String response = requestByService(gServiceHost + "redbook/api/movie/like?id="+id);
        return response.equals("true");
    }

    @Override
    public boolean addVideoUserCommentClickGood(Integer id) {
        String response = requestByService(gServiceHost + "redbook/api/movie/commentLike?id="+id);
        return response.equals("true");
    }

    @Override
    public String queryByUrl(String url) {
        return requestByService(url);
    }

    /**
     * 向服务器请求数据
     */
    private String requestByService(String url) {
        try {
            /**每一个请求都会全局加上deviceID*/

            boolean hasParam=url.contains("?");
            url+=(hasParam?"&":"?")+"deviceId=" + gDeviceId;
            System.out.println("url:"+url);
            String response = HttpRequest.sendGet(url);

            System.out.println("response:"+response);
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
     * 获取用户数据
     * @param forceNew 是否强制刷新数据，如果是false，则会从本地缓存中去
     */
    private UserVo geUserVo(boolean forceNew)
    {
        if(!forceNew && null!=cacheUserVo)
            return cacheUserVo;
        String response =  requestByService(gServiceHost + "redbook/api/user/add?1=1");
        cacheUserVo=JsonUtil.write2Class(response,UserVo.class);
        return cacheUserVo;
    }

    @Override
    public Observable<UserVo> getUser() {
        return Observable.create(new Observable.OnSubscribe<UserVo>() {
            @Override
            public void call(Subscriber<? super UserVo> subscriber) {
                UserVo user=geUserVo(false);
                if(null!=user)
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
                UserVo user=geUserVo(true);
                if(null!=user)
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
                String url =  requestByService(gServiceHost + "redbook/api/user/randomIcon");
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
                String result =  requestByService(gServiceHost + "redbook/api/user/changeName?name="+name);
                subscriber.onNext(result.equals("true"));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public String getRechargeWebUrl() {
        return gServiceHost+"redbook/api/pay/index?deviceId="+gDeviceId;
    }

    @Override
    public Observable<BillVo[]> getBilli() {
        return Observable.create(new Observable.OnSubscribe<BillVo[]>() {
            @Override
            public void call(Subscriber<? super BillVo[]> subscriber) {
                String data =  requestByService(gServiceHost + "redbook/api/user/listOrder");
                BillVo[] datas = JsonUtil.write2Class(data, BillVo[].class);
                subscriber.onNext(datas);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> clickVote(final int voteId,final int subSelectId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String data =  requestByService(gServiceHost
                        + "redbook/api/vote/vote?vid="+voteId+"&sid="+subSelectId);
                if(data.equals("true"))
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
                String result =  requestByService(gServiceHost + "redbook/api/picture/like?id="+id);
                subscriber.onNext("true".equals(result));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> publishFriendTalk(String content, List<String> images) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Thread.sleep(5*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(true);
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }


}
