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
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pear.yellowthird.activitys.R;

/** Manages the {@link ExoPlayer}, the IMA plugin and all video playback. */
/* package */ final class PlayerManager implements AdsMediaSource.MediaSourceFactory {

  private final DataSource.Factory manifestDataSourceFactory;
  private final DataSource.Factory mediaDataSourceFactory;

  private SimpleExoPlayer player;
  private long contentPosition;

  String url;

  public PlayerManager(Context context,String url) {
    this.url=url;
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
    player = ExoPlayerFactory.newSimpleInstance(
            new DefaultRenderersFactory(context),
            trackSelector,
            new FullLoadControl());

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
