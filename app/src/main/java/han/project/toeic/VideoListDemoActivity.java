/*
 * Copyright 2012 Google Inc. All Rights Reserved.
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
package han.project.toeic;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * A sample Activity showing how to manage multiple YouTubeThumbnailViews in an adapter for display
 * in a List. When the list items are clicked, the video is played by using a YouTubePlayerFragment.
 * <p>
 * The demo supports custom fullscreen and transitioning between portrait and landscape without
 * rebuffering.
 */
@TargetApi(13)
public final class VideoListDemoActivity extends Activity implements OnFullscreenListener {

  /** The duration of the animation sliding up the video in portrait. */
  private static final int ANIMATION_DURATION_MILLIS = 300;
  /** The padding between the video list and the video in landscape orientation. */
  private static final int LANDSCAPE_VIDEO_PADDING_DP = 5;

  /** The request code when calling startActivityForResult to recover from an API service error. */
  private static final int RECOVERY_DIALOG_REQUEST = 1;

  private VideoListFragment listFragment;
  private VideoFragment videoFragment;

  private View videoBox;
  private View closeButton;

  private boolean isFullscreen;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.video_list_demo);

    listFragment = (VideoListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
    videoFragment =
        (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);

    videoBox = findViewById(R.id.video_box);
    closeButton = findViewById(R.id.close_button);

    videoBox.setVisibility(View.INVISIBLE);

    layout();

    checkYouTubeApi();
  }

  private void checkYouTubeApi() {
    YouTubeInitializationResult errorReason =
        YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
    if (errorReason.isUserRecoverableError()) {
      errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
    } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
      String errorMessage =
          String.format(getString(R.string.error_player), errorReason.toString());
      Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RECOVERY_DIALOG_REQUEST) {
      // Recreate the activity if user performed a recovery action
      recreate();
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    layout();
  }

  @Override
  public void onFullscreen(boolean isFullscreen) {
    this.isFullscreen = isFullscreen;

    layout();
  }

  /**
   * Sets up the layout programatically for the three different states. Portrait, landscape or
   * fullscreen+landscape. This has to be done programmatically because we handle the orientation
   * changes ourselves in order to get fluent fullscreen transitions, so the xml layout resources
   * do not get reloaded.
   */
  private void layout() {
    boolean isPortrait =
        getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

    listFragment.getView().setVisibility(isFullscreen ? View.GONE : View.VISIBLE);
    listFragment.setLabelVisibility(isPortrait);
    closeButton.setVisibility(isPortrait ? View.VISIBLE : View.GONE);

    if (isFullscreen) {
      videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
      setLayoutSize(videoFragment.getView(), MATCH_PARENT, MATCH_PARENT);
      setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
    } else if (isPortrait) {
      setLayoutSize(listFragment.getView(), MATCH_PARENT, MATCH_PARENT);
      setLayoutSize(videoFragment.getView(), MATCH_PARENT, WRAP_CONTENT);
      setLayoutSizeAndGravity(videoBox, MATCH_PARENT, WRAP_CONTENT, Gravity.BOTTOM);
    } else {
      videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
      int screenWidth = dpToPx(getResources().getConfiguration().screenWidthDp);
      setLayoutSize(listFragment.getView(), screenWidth / 4, MATCH_PARENT);
      int videoWidth = screenWidth - screenWidth / 4 - dpToPx(LANDSCAPE_VIDEO_PADDING_DP);
      setLayoutSize(videoFragment.getView(), videoWidth, WRAP_CONTENT);
      setLayoutSizeAndGravity(videoBox, videoWidth, WRAP_CONTENT,
          Gravity.RIGHT | Gravity.CENTER_VERTICAL);
    }
  }

  public void onClickClose(@SuppressWarnings("unused") View view) {
    listFragment.getListView().clearChoices();
    listFragment.getListView().requestLayout();
    videoFragment.pause();
    ViewPropertyAnimator animator = videoBox.animate()
        .translationYBy(videoBox.getHeight())
        .setDuration(ANIMATION_DURATION_MILLIS);
    runOnAnimationEnd(animator, new Runnable() {
      @Override
      public void run() {
        videoBox.setVisibility(View.INVISIBLE);
      }
    });
  }

  @TargetApi(16)
  private void runOnAnimationEnd(ViewPropertyAnimator animator, final Runnable runnable) {
    if (Build.VERSION.SDK_INT >= 16) {
      animator.withEndAction(runnable);
    } else {
      animator.setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          runnable.run();
        }
      });
    }
  }

  /**
   * A fragment that shows a static list of videos.
   */
  public static final class VideoListFragment extends ListFragment {

    private static final List<VideoEntry> VIDEO_LIST;
    static {
      List<VideoEntry> list = new ArrayList<VideoEntry>();
      list.add(new VideoEntry("Lesson 1 Contracts Hợp đồng", "_bNeQIcCHYs"));
      list.add(new VideoEntry("Lesson 2 Marketing Thị trường", "RMiGyC67kJ8"));
      list.add(new VideoEntry("Lesson 3 Warranties Bảo hành", "qU9qCldVEec"));
      list.add(new VideoEntry("Lesson 4 Business planning Kế hoach kinh doanh", "uby-Y9m07Ls"));
      list.add(new VideoEntry("Lesson 5 Conferences Hội nghị", "UMBwgTa2xt8"));
      list.add(new VideoEntry("Lesson 6 Computer Máy tính", "_FWLs42p_ls"));
      list.add(new VideoEntry("Lesson 7 Office Technology Công nghệ văn phòng", "cvloiAXBeGs"));
      list.add(new VideoEntry("Lesson 8 Office Procedures Quy trình làm việc", "rzEOmqjXg6c"));
      list.add(new VideoEntry("Lesson 9 Electronics Thiết bị điện tử", "q1E-eI3TdGs"));
      list.add(new VideoEntry("Lesson 10 Correspondence Thư tín thương mại", "dKZ0aU-DAig"));
      list.add(new VideoEntry("Lesson 11 Job advertising and Recruiting Quảng cáo việc làm và tuyển dụng", "DgjRy757K80"));
      list.add(new VideoEntry("Lesson 12 Applying and Interviewing Xin việc và phỏng vấn", "Epemzo9_9qs"));
      list.add(new VideoEntry("Lesson 13 Hiring and Training Tuyển dụng và đào tạo", "LFwRlnfG7I4"));
      list.add(new VideoEntry("Lesson 14 Salaries and Benifits Lương bổng và lợi ích", "w6MIEvxLQMM"));
      list.add(new VideoEntry("Lesson 15 Promotions, Pensions and Awards Đề bạt, tiền trợ cấp và khen thưởng", "hgpdOn4-KMs"));
      list.add(new VideoEntry("Lesson 16 Shopping Mua sắm", "fByIYGo05SU"));
      list.add(new VideoEntry("Lesson 17 Ordering Supplies Đặt hàng", "vt0ocCNuUqI"));
      list.add(new VideoEntry("Lesson 18 Shipping Vận chuyển", "90oWpOHX8YQ"));
      list.add(new VideoEntry("Lesson 19 Invoices Hóa đơn", "VkbHfwT83JA"));
      list.add(new VideoEntry("Lesson 20 Inventory Kiểm kê hàng hóa", "k9iixIAv92s"));
      list.add(new VideoEntry("Lesson 21 Banking Ngân hàng", "9V15qh7bWNg"));
      list.add(new VideoEntry("Lesson 22 Accounting Kế toán", "lg2Z38IqByU"));
      list.add(new VideoEntry("Lesson 23 Investment Đầu tư", "ZgNP6mGV7-0"));
      list.add(new VideoEntry("Lesson 24 Taxes Thuế", "t9dHN7KrH4I"));
      list.add(new VideoEntry("Lesson 25 Financial Statements Báo cáo tài chính", "LIJ5Hd9yx3A"));
      list.add(new VideoEntry("Lesson 26 Property and Departments Trụ sở và các phòng ban", "j2C96f1hv3M"));
      list.add(new VideoEntry("Lesson 27 Board Meetings and Committees Uỷ ban và Hội đồng quản trị", "0wF9BKslmLc"));
      list.add(new VideoEntry("Lesson 28 Quality Control Kiểm soát chất lượng", "dGhURC99GNA"));
      list.add(new VideoEntry("Lesson 29 Product Development Phát triển sản phẩm", "RJxHDxaRm4c"));
      list.add(new VideoEntry("Lesson 30 Renting & Leasing Thuê và cho thuê văn phòng", "6ocgZwT8o1g"));
      list.add(new VideoEntry("Lesson 31 Selecting a Restaurant Chọn nhà hàng", "ANmRQRRBuoU"));
      list.add(new VideoEntry("Lesson 32 Eating Out Ăn ngoài", "VrASA71rFYw"));
      list.add(new VideoEntry("Lesson 33 Ordering Lunch Đặt bữa trưa", "XvqLcTkaXto"));
      list.add(new VideoEntry("Lesson 34 Cooking as a Career Nghề nấu ăn", "6_QOrkcZZis"));
      list.add(new VideoEntry("Lesson 35 Events Sự kiện", "7Mw-0VEC23s"));
      list.add(new VideoEntry("Lesson 36 General Travel Nói chung về du lịch", "IA24adcoAJg"));
      list.add(new VideoEntry("Lesson 37 Airlines Hàng không", "00nYQSU1fdc"));
      list.add(new VideoEntry("Lesson 38 Trains Tàu hỏa", "5flkcMcIanQ"));
      list.add(new VideoEntry("Lesson 39 Hotels Khách sạn", "VF4YH746X_w"));
      list.add(new VideoEntry("Lesson 40 Car Rental Thuê xe", "FJNHIeNhh4M"));
      list.add(new VideoEntry("Lesson 41 Movies Phim ảnh", "jme_1wGBRZU"));
      list.add(new VideoEntry("Lesson 42 Theater Rạp chiếu phim", "e-MVAO2ytlQ"));
      list.add(new VideoEntry("Lesson 43 Music Âm nhạc", "nnDuQqSE94I"));
      list.add(new VideoEntry("Lesson 44 Museums Bảo tàng", "O4Q8UkC-Soo"));
      list.add(new VideoEntry("Lesson 45 Media Truyền thông", "pEGOOIqr59E"));
      list.add(new VideoEntry("Lesson 46 Doctor’s Office Phòng khám", "Tn_GekQW5pg"));
      list.add(new VideoEntry("Lesson 47 Dentist’s Office Phòng nha sĩ", "eHS1ig4z-9A"));
      list.add(new VideoEntry("Lesson 48 Health Insurance Bảo hiểm y tế", "ZC3OReR4YXI"));
      list.add(new VideoEntry("Lesson 49 Hospitals Bệnh viện", "TI7Ga645bZ4"));
      list.add(new VideoEntry("Lesson 50 Pharmacy Hiệu thuốc, phòng dược", "00nYQSU1fdc"));
      list.add(new VideoEntry("Lesson 38 Trains Tàu hỏa", "fe7Ugyl8Fuc"));
      VIDEO_LIST = Collections.unmodifiableList(list);
    }

    private PageAdapter adapter;
    private View videoBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      adapter = new PageAdapter(getActivity(), VIDEO_LIST);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      videoBox = getActivity().findViewById(R.id.video_box);
      getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
      String videoId = VIDEO_LIST.get(position).videoId;

      VideoFragment videoFragment =
          (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
      videoFragment.setVideoId(videoId);

      // The videoBox is INVISIBLE if no video was previously selected, so we need to show it now.
      if (videoBox.getVisibility() != View.VISIBLE) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
          // Initially translate off the screen so that it can be animated in from below.
          videoBox.setTranslationY(videoBox.getHeight());
        }
        videoBox.setVisibility(View.VISIBLE);
      }

      // If the fragment is off the screen, we animate it in.
      if (videoBox.getTranslationY() > 0) {
        videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
      }
    }

    @Override
    public void onDestroyView() {
      super.onDestroyView();

      adapter.releaseLoaders();
    }

    public void setLabelVisibility(boolean visible) {
      adapter.setLabelVisibility(visible);
    }

  }

  /**
   * Adapter for the video list. Manages a set of YouTubeThumbnailViews, including initializing each
   * of them only once and keeping track of the loader of each one. When the ListFragment gets
   * destroyed it releases all the loaders.
   */
  private static final class PageAdapter extends BaseAdapter {

    private final List<VideoEntry> entries;
    private final List<View> entryViews;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
    private final LayoutInflater inflater;
    private final ThumbnailListener thumbnailListener;

    private boolean labelsVisible;

    public PageAdapter(Context context, List<VideoEntry> entries) {
      this.entries = entries;

      entryViews = new ArrayList<View>();
      thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
      inflater = LayoutInflater.from(context);
      thumbnailListener = new ThumbnailListener();

      labelsVisible = true;
    }

    public void releaseLoaders() {
      for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
        loader.release();
      }
    }

    public void setLabelVisibility(boolean visible) {
      labelsVisible = visible;
      for (View view : entryViews) {
        view.findViewById(R.id.text).setVisibility(visible ? View.VISIBLE : View.GONE);
      }
    }

    @Override
    public int getCount() {
      return entries.size();
    }

    @Override
    public VideoEntry getItem(int position) {
      return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = convertView;
      VideoEntry entry = entries.get(position);

      // There are three cases here
      if (view == null) {
        // 1) The view has not yet been created - we need to initialize the YouTubeThumbnailView.
        view = inflater.inflate(R.layout.video_list_item, parent, false);
        YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
        thumbnail.setTag(entry.videoId);
        thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
      } else {
        YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
        YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(thumbnail);
        if (loader == null) {
          // 2) The view is already created, and is currently being initialized. We store the
          //    current videoId in the tag.
          thumbnail.setTag(entry.videoId);
        } else {
          // 3) The view is already created and already initialized. Simply set the right videoId
          //    on the loader.
          thumbnail.setImageResource(R.mipmap.loading_thumbnail);
          loader.setVideo(entry.videoId);
        }
      }
      TextView label = ((TextView) view.findViewById(R.id.text));
      label.setText(entry.text);
      label.setVisibility(labelsVisible ? View.VISIBLE : View.GONE);
      return view;
    }

    private final class ThumbnailListener implements
        YouTubeThumbnailView.OnInitializedListener,
        YouTubeThumbnailLoader.OnThumbnailLoadedListener {

      @Override
      public void onInitializationSuccess(
          YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
        loader.setOnThumbnailLoadedListener(this);
        thumbnailViewToLoaderMap.put(view, loader);
        view.setImageResource(R.mipmap.loading_thumbnail);
        String videoId = (String) view.getTag();
        loader.setVideo(videoId);
      }

      @Override
      public void onInitializationFailure(
          YouTubeThumbnailView view, YouTubeInitializationResult loader) {
        view.setImageResource(R.mipmap.no_thumbnail);
      }

      @Override
      public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
      }

      @Override
      public void onThumbnailError(YouTubeThumbnailView view, ErrorReason errorReason) {
        view.setImageResource(R.mipmap.no_thumbnail);
      }
    }

  }

  public static final class VideoFragment extends YouTubePlayerFragment
      implements OnInitializedListener {

    private YouTubePlayer player;
    private String videoId;

    public static VideoFragment newInstance() {
      return new VideoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      initialize(DeveloperKey.DEVELOPER_KEY, this);
    }

    @Override
    public void onDestroy() {
      if (player != null) {
        player.release();
      }
      super.onDestroy();
    }

    public void setVideoId(String videoId) {
      if (videoId != null && !videoId.equals(this.videoId)) {
        this.videoId = videoId;
        if (player != null) {
          player.cueVideo(videoId);
        }
      }
    }

    public void pause() {
      if (player != null) {
        player.pause();
      }
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean restored) {
      this.player = player;
      player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
      player.setOnFullscreenListener((VideoListDemoActivity) getActivity());
      if (!restored && videoId != null) {
        player.cueVideo(videoId);
      }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
      this.player = null;
    }

  }

  private static final class VideoEntry {
    private final String text;
    private final String videoId;

    public VideoEntry(String text, String videoId) {
      this.text = text;
      this.videoId = videoId;
    }
  }

  // Utility methods for layouting.

  private int dpToPx(int dp) {
    return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
  }

  private static void setLayoutSize(View view, int width, int height) {
    LayoutParams params = view.getLayoutParams();
    params.width = width;
    params.height = height;
    view.setLayoutParams(params);
  }

  private static void setLayoutSizeAndGravity(View view, int width, int height, int gravity) {
    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
    params.width = width;
    params.height = height;
    params.gravity = gravity;
    view.setLayoutParams(params);
  }

}
