/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pear.yellowthird.activitys.video;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.C.ContentType;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pear.yellowthird.activitys.R;

/** Manages the {@link ExoPlayer}, the IMA plugin and all video playback. */
/* package */ public final class PlayerManager implements AdsMediaSource.MediaSourceFactory {

  private final DataSource.Factory manifestDataSourceFactory;
  private final DataSource.Factory mediaDataSourceFactory;

  private SimpleExoPlayer player;
  private long contentPosition;

  /**
   * 缓存进度控制
   * */
  public enum LoadingBuffer
  {
    fluencyLoadingBuffer, /**比较快速的*/
    spareFlowLoadingBuffer/**节省流量的*/
  }

  LoadingBuffer loadingBuffer;

  String url;

  public PlayerManager(Context context,String url) {
    this(context,url,LoadingBuffer.fluencyLoadingBuffer);
  }

  public PlayerManager(Context context,String url,LoadingBuffer loadingBuffer) {
    this.url=url;
    this.loadingBuffer=loadingBuffer;
    manifestDataSourceFactory =
            new DefaultDataSourceFactory(
                    context, Util.getUserAgent(context, context.getString(R.string.app_name)));
    mediaDataSourceFactory =
            new DefaultDataSourceFactory(
                    context,
                    Util.getUserAgent(context, context.getString(R.string.app_name)),
                    new DefaultBandwidthMeter());
  }

  public void init(Context context, SimpleExoPlayerView simpleExoPlayerView) {
    // Create a default track selector.
    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory =
        new AdaptiveTrackSelection.Factory(bandwidthMeter);
    TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

    // Create a player instance.

    /*,new FullLoadControl() 如果全部加载，三级片百分百报内存溢出。我日，这里只能期望华为云中途不要关掉连接了，否则我就死定了。没法搞的定了*/
    //全部加载。流量费你给的起吗？
    //其实这里控制好maxBufferMs和minBufferMs的差值就好了，其实就是一直在云端查数据

    player = ExoPlayerFactory.newSimpleInstance(
            new DefaultRenderersFactory(context),
            trackSelector,
            getLoadControl());

    //ExoPlayerFactory.newSimpleInstance(context, trackSelector);

    // Bind the player to the view.
    simpleExoPlayerView.setPlayer(player);

    // Produces DataSource instances through which media data is loaded.
    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
        Util.getUserAgent(context, context.getString(R.string.app_name)));

    // This is the MediaSource representing the content media (i.e. not the ad).
    String contentUrl =url;// "http://rmcdn.2mdn.net/MotifFiles/html/1248596/android_1330378998288.mp4";
    MediaSource contentMediaSource =
        new ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(contentUrl));

    // Prepare the player with the source.
    player.seekTo(contentPosition);
    player.prepare(contentMediaSource);
  }

  /**
   * 获取缓存控制算法
   * */
  DefaultLoadControl getLoadControl()
  {

    /**比较流畅的可以播放的缓存设置*/
    int minBufferMs=1000*8;
    int maxBufferMs=1000*14;
    if(loadingBuffer==LoadingBuffer.spareFlowLoadingBuffer)
    {
      /**这里设置在笑也没有用了，好像默认值最少会加在30秒。而且是不固定的*/
      minBufferMs=1000*4;
      maxBufferMs=1000*6;
    }

    return new DefaultLoadControl(
            new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE),
            minBufferMs,
            maxBufferMs,
            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
            DefaultLoadControl.DEFAULT_TARGET_BUFFER_BYTES,
            DefaultLoadControl.DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS);
  }

  public void reset() {
    if (player != null) {
      contentPosition = player.getContentPosition();
      player.release();
      player = null;
    }
  }

  public void release() {
    if (player != null) {
      player.release();
      player = null;
    }
  }

  // AdsMediaSource.MediaSourceFactory implementation.

  @Override
  public MediaSource createMediaSource(
          Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
    @ContentType int type = Util.inferContentType(uri);
    switch (type) {
      case C.TYPE_OTHER:
        return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(uri, handler, listener);
      case C.TYPE_SS:
      default:
        throw new IllegalStateException("Unsupported type: " + type);
    }
  }

  @Override
  public int[] getSupportedTypes() {
    return new int[] {C.TYPE_DASH, C.TYPE_HLS, C.TYPE_OTHER};
  }

  public SimpleExoPlayer getPlayer() {
    return player;
  }

}
