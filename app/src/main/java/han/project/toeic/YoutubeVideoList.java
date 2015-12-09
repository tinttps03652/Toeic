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

import han.project.util.DeveloperKey;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * A sample Activity showing how to manage multiple YouTubeThumbnailViews in an adapter for display
 * in a List. When the list items are clicked, the video is played by using a YouTubePlayerFragment.
 * <p/>
 * The demo supports custom fullscreen and transitioning between portrait and landscape without
 * rebuffering.
 */
@TargetApi(13)
public final class YoutubeVideoList extends Activity implements OnFullscreenListener {

    /**
     * The duration of the animation sliding up the video in portrait.
     */
    private static final int ANIMATION_DURATION_MILLIS = 300;
    /**
     * The padding between the video list and the video in landscape orientation.
     */
    private static final int LANDSCAPE_VIDEO_PADDING_DP = 5;

    /**
     * The request code when calling startActivityForResult to recover from an API service error.
     */
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
        private static List<VideoEntry> VIDEO_LIST;

        static void newHan(int index) {
            List<VideoEntry> list = new ArrayList<>();
            switch (index) {
                case 0:
                    list.add(new VideoEntry("Hướng dẫn tránh bẫy và chiến thuật PART 1 TOEIC", "x9f9UAiFW9E"));
                    list.add(new VideoEntry("Tổng hợp các chiến thuật trong PART 2 TOEIC", "2Br7bI8yswY"));
                    list.add(new VideoEntry("Hướng dẫn làm Part 3 bài thi TOEIC (Phần 1)", "EbmnQv1a3CE"));
                    list.add(new VideoEntry("Hướng dẫn làm Part 3 bài thi TOEIC (Phần 2)", "qVRVv1NZcts"));
                    list.add(new VideoEntry("Hướng dẫn làm Part 4 bài thi TOEIC (Phần 1 - Paraphrasing)", "uaFvY75yXgg"));
                    list.add(new VideoEntry("Hướng dẫn làm Part 4 bài thi TOEIC (Phần 2 - Voice Message)", "d_MgkWUqHOw"));
                    list.add(new VideoEntry("Hướng dẫn làm Part 4 bài thi TOEIC (Phần 3 - Announcement)", "YJvr7r-UN0A"));
                    list.add(new VideoEntry("Hướng dẫn làm Part 4 bài thi TOEIC (Phần 4 - Advertisement)", "DFfDcLqUtV0"));
                    list.add(new VideoEntry("Hướng dẫn làm Part 5, Part 6 bài thi TOEIC (Nouns – DANH TỪ)", "ZFo1R_VlxGU"));
                    list.add(new VideoEntry("Hướng dẫn làm bài Part 5 (Đại từ- mệnh đề quan hệ) - Phần 1", "-UVJXrS3550"));

                    list.add(new VideoEntry("MẸO THI TOEIC ĐẠT ĐIỂM CAO - Mô tả tranh cô Mai Phương", "sLv_DlBlWmw"));
                    list.add(new VideoEntry("MẸO THI TOEIC - Bài giảng của cô Mai Phương FULL 2", "x70r3SbZv7s"));
                    list.add(new VideoEntry("MẸO THI TOEIC - Bài giảng của cô Mai Phương FULL 3", "8CT2G1cUdRU"));
                    list.add(new VideoEntry("MẸO THI TOEIC CẤP TỐC - KINH NGHIỆM THI TOEIC CẤP TỐC", "YfnFOoEyO6I"));
                    list.add(new VideoEntry("MẸO THI TOEIC ĐẠT ĐIỂM CAO - Phần Part2 30 câu cô Mai Phương", "ecR65MZPNz0"));
                    list.add(new VideoEntry("GIỚI THIỆU VỀ KỲ THI TOEIC", "N2H7IKjHYmQ"));

                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Tính từ - Phần 1", "u1ih-pnE2-k"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Tính từ - Phần 2", "sdwvNGmCyMM"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Trạng từ", "xW20hT5X9T0"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Động từ - Phần 1", "gfmk7a-u8JY"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Động từ - Phần 2", "5eoou8xGHRI"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Câu điều kiện", "1C0y1Pyq_CA"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Danh từ - Phần 1", "Z41G-DCzFbg"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Danh từ - Phần 2", "SpxLv_nohRw"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Mệnh đề quan hệ - Phần 1", "eGO79VSyq1k"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Mệnh đề quan hệ - Phần 2", "EYUc2XcCDbA"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Câu bị động - Phần 1", "oyTQBzMATUA"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Câu bị động - Phần 2", "RxfiaQeP6Gs"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Chữa đề mẫu part 5 (phần 1)", "oTP1nwOqyDc"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Chữa đề mẫu part 5 (phần 2)", "TDYmpqOKtnA"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Chữa đề mẫu part 6 (phần 1)", "x74i3Z2cW6Y"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Chữa đề mẫu part 6 (phần 2)", "83_PFEXc8j0"));
                    list.add(new VideoEntry("Cách tránh bẫy đề thi TOEIC - Chữa đề mẫu part 6 (phần 3)", "MNO4Q5MGJKc"));
                    VIDEO_LIST = Collections.unmodifiableList(list);
                    break;
                case 1:
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
                    VIDEO_LIST = Collections.unmodifiableList(list);
                    break;
                case 2:
                    list.add(new VideoEntry("Lesson 1 Contracts Hợp đồng", "Zec5HDr-Hck"));
                    list.add(new VideoEntry("Lesson 2 Marketing Thị trường", "WJSQAP8b8jg"));
                    list.add(new VideoEntry("Lesson 3 Warranties Bảo hành", "tCtY3v52yo4"));
                    list.add(new VideoEntry("Lesson 4 Business planning Kế hoach kinh doanh", "1HR0M6AnVJI"));
                    list.add(new VideoEntry("Lesson 5 Conferences Hội nghị", "Y9XSeRvnEwU"));
                    list.add(new VideoEntry("Lesson 6 Computer Máy tính", "jSY-GfJyDrI"));
                    list.add(new VideoEntry("Lesson 7 Office Technology Công nghệ văn phòng", "DPKqKYJb4dY"));
                    list.add(new VideoEntry("Lesson 8 Office Procedures Quy trình làm việc", "xe-D2IX2jGo"));
                    list.add(new VideoEntry("Lesson 9 Electronics Thiết bị điện tử", "iAG8jqvKv-k"));
                    list.add(new VideoEntry("Lesson 10 Correspondence Thư tín thương mại", "mmPPxy0bmys"));
                    list.add(new VideoEntry("Lesson 11 Job advertising and Recruiting Quảng cáo việc làm và tuyển dụng", "RZRZHg9ypAc"));
                    list.add(new VideoEntry("Lesson 12 Applying and Interviewing Xin việc và phỏng vấn", "q5EZsH8tfgc"));
                    list.add(new VideoEntry("Lesson 13 Hiring and Training Tuyển dụng và đào tạo", "EXmYeL3OBYc"));
                    list.add(new VideoEntry("Lesson 14 Salaries and Benifits Lương bổng và lợi ích", "A3nIEraDAk0"));
                    list.add(new VideoEntry("Lesson 15 Promotions, Pensions and Awards Đề bạt, tiền trợ cấp và khen thưởng", "wn9a2tLQA-w"));
                    list.add(new VideoEntry("Lesson 16 Shopping Mua sắm", "yTcDNNE6UP0"));
                    list.add(new VideoEntry("Lesson 17 Ordering Supplies Đặt hàng", "AMlPOHIIZyo"));
                    list.add(new VideoEntry("Lesson 18 Shipping Vận chuyển", "iV-X0ILcw-8"));
                    list.add(new VideoEntry("Lesson 19 Invoices Hóa đơn", "72Co-qiQojM"));
                    list.add(new VideoEntry("Lesson 20 Inventory Kiểm kê hàng hóa", "6c26HdWP0dI"));
                    list.add(new VideoEntry("Lesson 21 Banking Ngân hàng", "6hXx46E_nUc"));
                    list.add(new VideoEntry("Lesson 22 Accounting Kế toán", "v_r3itb3ybE"));
                    list.add(new VideoEntry("Lesson 23 Investment Đầu tư", "RNok0BuzsVA"));
                    list.add(new VideoEntry("Lesson 24 Taxes Thuế", "w0aEnZpl6kw"));
                    list.add(new VideoEntry("Lesson 25 Financial Statements Báo cáo tài chính", "p1NS163htk8"));
                    list.add(new VideoEntry("Lesson 26 Property and Departments Trụ sở và các phòng ban", "zmCPbJciVtU"));
                    list.add(new VideoEntry("Lesson 27 Board Meetings and Committees Uỷ ban và Hội đồng quản trị", "JoLWWb1KcXY"));
                    list.add(new VideoEntry("Lesson 28 Quality Control Kiểm soát chất lượng", "qyqnYw_wY1c"));
                    list.add(new VideoEntry("Lesson 29 Product Development Phát triển sản phẩm", "Da2hRhmJ9_s"));
                    list.add(new VideoEntry("Lesson 30 Renting & Leasing Thuê và cho thuê văn phòng", "lNaITU1_9Ao"));
                    list.add(new VideoEntry("Lesson 31 Selecting a Restaurant Chọn nhà hàng", "GLAgxdHckqg"));
                    list.add(new VideoEntry("Lesson 32 Eating Out Ăn ngoài", "_xlT1960mCE"));
                    list.add(new VideoEntry("Lesson 33 Ordering Lunch Đặt bữa trưa", "gVAMrBr56bc"));
                    list.add(new VideoEntry("Lesson 34 Cooking as a Career Nghề nấu ăn", "ztnQsMwruL4"));
                    list.add(new VideoEntry("Lesson 35 Events Sự kiện", "Z5jezxkLt7U"));
                    list.add(new VideoEntry("Lesson 36 General Travel Nói chung về du lịch", "9q5e_7FiRhs"));
                    list.add(new VideoEntry("Lesson 37 Airlines Hàng không", "RTVav9xPadE"));
                    list.add(new VideoEntry("Lesson 38 Trains Tàu hỏa", "yVMfSXx971c"));
                    list.add(new VideoEntry("Lesson 39 Hotels Khách sạn", "EbcWN5uhRt4"));
                    list.add(new VideoEntry("Lesson 40 Car Rental Thuê xe", "uRGhkH--M48"));
                    list.add(new VideoEntry("Lesson 41 Movies Phim ảnh", "9p_0pBCY4kA"));
                    list.add(new VideoEntry("Lesson 42 Theater Rạp chiếu phim", "Q0bpRfdTmL0"));
                    list.add(new VideoEntry("Lesson 43 Music Âm nhạc", "ycEdUlDgF58"));
                    list.add(new VideoEntry("Lesson 44 Museums Bảo tàng", "vRrAbI5fFeM"));
                    list.add(new VideoEntry("Lesson 45 Media Truyền thông", "txu1d4IuQl4"));
                    list.add(new VideoEntry("Lesson 46 Doctor’s Office Phòng khám", "A-wENUaa7QA"));
                    list.add(new VideoEntry("Lesson 47 Dentist’s Office Phòng nha sĩ", "cCAQflZmjiA"));
                    list.add(new VideoEntry("Lesson 48 Health Insurance Bảo hiểm y tế", "XKXRTbPPE7k"));
                    list.add(new VideoEntry("Lesson 49 Hospitals Bệnh viện", "jcVfrofq6io"));
                    list.add(new VideoEntry("Lesson 50 Pharmacy Hiệu thuốc, phòng dược", "33Kq60HWmpE"));
                    VIDEO_LIST = Collections.unmodifiableList(list);
                    break;
                case 3:
                    list.add(new VideoEntry("TOEIC Reading Skills 1: Incomplete Sentences", "2wGawuxa_Lw"));
                    list.add(new VideoEntry("TOEIC Reading Skills 2: Text Completion", "0PpJqBIWqpI"));
                    list.add(new VideoEntry("TOEIC Reading Skills 3: Reading Comprehension", "Q2u00kkorSQ"));
                    list.add(new VideoEntry("TOEIC Reading - Part V", "UcQI9zZ4tJU"));
                    list.add(new VideoEntry("TOEIC Reading - Part V continued", "5NUYXk9QNlg"));
                    list.add(new VideoEntry("TOEIC Reading - Part VI", "Geg8fJnYlhw"));
                    list.add(new VideoEntry("TOEIC Reading - Part VI continued", "J-GvbfmPYqE"));
                    list.add(new VideoEntry("TOEIC Reading - Part VII", "xCnAWKTPzck"));
                    list.add(new VideoEntry("TOEIC Reading - Part VII continued", "dnJlI8gZT18"));
                    list.add(new VideoEntry("TOEIC Reading - Final Review", "WeD89KfzDkE"));
                    list.add(new VideoEntry("UKgate.com - TOEIC Reading (Intermediate)", "o4ND_oZfjKA"));
                    list.add(new VideoEntry("TOEIC LEARNING TIPS (Cleverlearn - American Academy)", "tGkYJo-NaT4"));
                    list.add(new VideoEntry("TOEIC Reading - Part V", "GTmKU5x8YEY"));
                    list.add(new VideoEntry("TOEIC Reading - Part V", "lqKq20Tlfiw"));
                    list.add(new VideoEntry("TOEIC Reading - Part V", "jsGzJQeHzs0"));
                    list.add(new VideoEntry("TOEIC Reading - Part V", "kyKWxTog0tg"));
                    list.add(new VideoEntry("TOEIC Reading - Part V", "G6xOlBo6v6o"));
                    list.add(new VideoEntry("TOEIC Reading - Part V", "07jNkW1B8e0"));
                    list.add(new VideoEntry("TOEIC Reading - Part V", "GTmKU5x8YEY"));
                    list.add(new VideoEntry("TOEIC Reading - Part V", "lqKq20Tlfiw"));
                    list.add(new VideoEntry("TOEIC Reading - Part V", "jsGzJQeHzs0"));
                    VIDEO_LIST = Collections.unmodifiableList(list);
                    break;
                case 4:
                    list.add(new VideoEntry("Answer Keys - 1", "lUJdQh3lW4g"));
                    list.add(new VideoEntry("Answer Keys - 2", "JlvTQlOs9BM"));
                    list.add(new VideoEntry("Answer Keys - 3", "C2Nh-v_odEk"));
                    list.add(new VideoEntry("Answer Keys - 4", "e5wHk2Tpp18"));
                    list.add(new VideoEntry("Answer Keys - 5", "WB1-YzqwquM"));
                    list.add(new VideoEntry("Answer Keys - 6", "1TxFPGLLUKw"));
                    list.add(new VideoEntry("Answer Keys - 7", "wBROJyRILfs"));
                    list.add(new VideoEntry("Answer Keys - 8", "dVheXXfhzEA"));
                    list.add(new VideoEntry("Answer Keys - 9", "qi36VN9T17E"));
                    list.add(new VideoEntry("Answer Keys - 10", "wQQ5fkTfdHA"));
                    list.add(new VideoEntry("Barron's toeic test 1 part 1", "d9ABS0lng30"));
                    list.add(new VideoEntry("Listening Toeic Test 1 (Có đáp án + Transcripts)", "WmLjWfEG-6k"));
                    list.add(new VideoEntry("Full listening Barron Toeic test 2 (Có đáp án + Transcript)", "2rKUTnNsNTg"));
                    list.add(new VideoEntry("Listening Barron's Toeic test 3 - Part 1 (Có đáp án + Transcript)", "G4yPMc3roI4"));
                    list.add(new VideoEntry("Barron's toeic Test 3 - Part 2 (Answer key + Transcripts)", "Mixpn3ZJYgM"));
                    list.add(new VideoEntry("Listening Barron's toeic Test 3 - Part 3 (Có đáp án + Transcripts)", "OvcJ3pCk3BY"));
                    VIDEO_LIST = Collections.unmodifiableList(list);
                    break;
                case 5:
                    list.add(new VideoEntry("Toeic Listening Test - Test 1", "Xu0w8mrgKuI"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 2", "NuB1C-0rvFc"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 3", "lNfWTJgyXZM"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 4", "kyKWxTog0tg"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 5", "G6xOlBo6v6o"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 6", "07jNkW1B8e0"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 7", "GTmKU5x8YEY"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 8", "lqKq20Tlfiw"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 9", "jsGzJQeHzs0"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 10", "im777GKUfqM"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 11", "GZvqt6nEOUM"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 12", "5bg13MoYHk8"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 13", "e-uM-euVVQ0"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 14", "4rb8q35fKyE"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 15", "DV7piHBEyhE"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 16", "nRZXoKKJVS0"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 17", "ZvwUupjuF-s"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 18", "2xotEzAkKk8"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 19", "H1S67-ik2ow"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 20", "c87FNLXj46s"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 21", "qWKetbdS_ZU"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 22", "Wz1qt7pgRHw"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 23", "cn_Nbjf6EvQ"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 24", "ksA1HxFjyQ4"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 25", "nRZXoKKJVS0"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 26", "CUiBYSuAMsM"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 27", "bq8DN034Jfg"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 28", "j-irRX4ults"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 29", "oDS7mxlWvjU"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 30", "p1YCOx1hu6Q"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 31", "OWT2Pxx0xpI"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 32", "zfu2wGib284"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 33", "QWP4wmNMkAE"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 34", "a8kVnNiuPk0"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 35", "Q0heLNjNjw8"));
                    list.add(new VideoEntry("Toeic Listening Test - Test 36", "y1S5wR3DaTA"));
                    VIDEO_LIST = Collections.unmodifiableList(list);
                    break;
                case 6:
                    list.add(new VideoEntry("Ngữ pháp TOEIC cơ bản - Bài 1: Động từ", "bBGmpi8jotE"));
                    list.add(new VideoEntry("Ngữ pháp TOEIC cơ bản - Bài 2a: Trợ động từ - Động từ khiếm khuyết", "G6dOv-72R_g"));
                    list.add(new VideoEntry("Ngữ pháp TOEIC cơ bản - Bài 2b: Trợ động từ - Have, Has", "CxYg2rdYHBQ"));
                    list.add(new VideoEntry("Ngữ pháp TOEIC cơ bản - Bài 2c: Trợ động từ - Be", "TaiO_uKODJY"));
                    list.add(new VideoEntry("Bài giảng ngữ pháp TOEIC - Bài 3: Thể bị động", "YV7EKG_8p0Q"));
                    list.add(new VideoEntry("TOEIC P5 - 4: Tính từ -ed vs. tính từ -ing", "xRYEdiXVrv4"));
                    list.add(new VideoEntry("TOEIC P5 - 5: Giữa TO BE và by: V-ed/V-iii", "Kt8K6ZTgIXA"));
                    list.add(new VideoEntry("TOEIC P5 - 6: Động từ thường vs. Dạng rút gọn mệnh đề quan hệ", "f-86SCJTeK0"));
                    list.add(new VideoEntry("TOEIC P5 - 7: Giữa tính từ sở hữu và giới từ là danh từ", "_kWGlbzmdUA"));
                    list.add(new VideoEntry("TOEIC P5 - 8: Khi nào chọn liên từ?", "64iOk_53uIA"));
                    list.add(new VideoEntry("TOEIC P5 - 9: of my own vs. by myself", "yox7on2Yacc"));
                    list.add(new VideoEntry("TOEIC P5 - 10: giữa to và Verb: là Adverb", "R9N1paw1eI8"));
                    list.add(new VideoEntry("TOEIC P5 - 11: Giữa 'the' và 'possible' là so sánh nhất", "By96frrCjl4"));
                    list.add(new VideoEntry("TOEIC P5 - 12: help do sth | help sth", "jz2TsHkKRMY"));
                    list.add(new VideoEntry("TOEIC P5 - 13: sau giới từ là gì?", "ewC6mm28PPQ"));
                    list.add(new VideoEntry("TOEIC P5 - 14: trường hợp chọn mệnh đề quan hệ rút gọn - bị động", "EAWcIQAooh8"));
                    list.add(new VideoEntry("TOEIC P5 - 16: trước danh từ chính là gì?", "lc_YVb3S9E4"));
                    list.add(new VideoEntry("TOEIC P5 - 17: liên từ vs. giới từ", "GhJHbsd_guU"));
                    VIDEO_LIST = Collections.unmodifiableList(list);
                    break;
                case 7:

                    break;
                case 8:

                    break;
                case 9:

                    break;
                case 10:

                    break;
                case 11:

                    break;
                case 12:

                    break;
                case 13:

                    break;

            }

        }

        private PageAdapter adapter;
        private View videoBox;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            int pos = getActivity().getIntent().getIntExtra("position", -1);
            newHan(pos);
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
            player.setOnFullscreenListener((YoutubeVideoList) getActivity());
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
