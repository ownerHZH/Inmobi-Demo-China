package com.inmobi.nativead.sample.twocreatives;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.nativead.sample.PlacementId;
import com.inmobi.nativead.sample.R;
import com.squareup.picasso.Picasso;

/**
 * Created by jerry.hu on 06/12/17.
 */

public class SingleStrandFragment extends Fragment
        implements InMobiNative.NativeAdListener{
    private static final String TAG = SingleStrandFragment.class.getSimpleName();

    private ViewGroup mContainer;
    private View mAdView;

    private InMobiNative mInMobiNative;
    private InMobiNative mInMobiNative2;
    private InMobiNative.NativeAdListener listener = new InMobiNative.NativeAdListener() {
        @Override
        public void onAdLoadSucceeded(@NonNull final InMobiNative inMobiNative) {
            View adView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_native_ad, null);

            ImageView icon = (ImageView) adView.findViewById(R.id.adIcon);
            TextView title = (TextView) adView.findViewById(R.id.adTitle);
            TextView description = (TextView) adView.findViewById(R.id.adDescription);
            Button action = (Button) adView.findViewById(R.id.adAction);
            FrameLayout content = (FrameLayout) adView.findViewById(R.id.adContent);
            RatingBar ratingBar = (RatingBar) adView.findViewById(R.id.adRating);

            Picasso.with(getActivity())
                    .load(inMobiNative.getAdIconUrl())
                    .into(icon);
            title.setText(inMobiNative.getAdTitle());
            description.setText(inMobiNative.getAdDescription());
            action.setText(inMobiNative.getAdCtaText());
            content.addView(inMobiNative.getPrimaryViewOfWidth(getActivity(), content, mContainer,
                    200));

            float rating  = inMobiNative.getAdRating();
            if (rating != 0) {
                ratingBar.setRating(rating);
            }
            ratingBar.setVisibility(rating != 0 ? View.VISIBLE : View.GONE);

            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inMobiNative.reportAdClickAndOpenLandingPage();
                }
            });

            mContainer.addView(adView);
        }

        @Override
        public void onAdLoadFailed(@NonNull InMobiNative inMobiNative, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
        }

        @Override
        public void onAdFullScreenDismissed(InMobiNative inMobiNative) {}

        @Override
        public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {}

        @Override
        public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {}

        @Override
        public void onUserWillLeaveApplication(InMobiNative inMobiNative) {}

        @Override
        public void onAdImpressed(@NonNull InMobiNative inMobiNative) {
        }

        @Override
        public void onAdClicked(@NonNull InMobiNative inMobiNative) {}

        @Override
        public void onMediaPlaybackComplete(@NonNull InMobiNative inMobiNative) {}

        @Override
        public void onAdStatusChanged(@NonNull InMobiNative inMobiNative) {}
    };

    public static String getTitle() {
        return "Custom Placement";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_integration, container, false);
        mContainer = (ViewGroup) view.findViewById(R.id.container);

        final SwipeRefreshLayout swipeRefreshLayout = SwipeRefreshLayoutWrapper.getInstance(getActivity(),
                new SwipeRefreshLayoutWrapper.Listener() {
                    @Override
                    public boolean canChildScrollUp() {
                        return false;
                    }

                    @Override
                    public void onRefresh() {
                        reloadAd();
                    }
                });
        swipeRefreshLayout.addView(mContainer);
        return swipeRefreshLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createStrands();
        Log.d(TAG, "Requesting ad");
        loadAd();
    }

    private void createStrands() {

        mInMobiNative = new InMobiNative(getActivity(), PlacementId.YOUR_PLACEMENT_ID_HEADERLINE, this);
        mInMobiNative.setDownloaderEnabled(true);

    }

    @Override
    public void onDestroyView() {
        mInMobiNative.destroy();
        super.onDestroyView();
    }

    private void loadAd() {
        mInMobiNative.load();
        loadExtraAdIntoView();
    }

    private void clearAd() {
        mContainer.removeAllViews();
        mInMobiNative.destroy();
    }

    private void reloadAd() {
        clearAd();
        createStrands();
        loadAd();
    }

    private View loadAdIntoView(@NonNull final InMobiNative inMobiNative) {
        View adView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_native_ad, null);

        ImageView icon = (ImageView) adView.findViewById(R.id.adIcon);
        TextView title = (TextView) adView.findViewById(R.id.adTitle);
        TextView description = (TextView) adView.findViewById(R.id.adDescription);
        Button action = (Button) adView.findViewById(R.id.adAction);
        FrameLayout content = (FrameLayout) adView.findViewById(R.id.adContent);
        RatingBar ratingBar = (RatingBar) adView.findViewById(R.id.adRating);

        Picasso.with(getActivity())
                .load(inMobiNative.getAdIconUrl())
                .into(icon);
        title.setText(inMobiNative.getAdTitle());
        description.setText(inMobiNative.getAdDescription());
        action.setText(inMobiNative.getAdCtaText());
        content.addView(inMobiNative.getPrimaryViewOfWidth(getActivity(), content, mContainer,
                content.getWidth()));

        float rating  = inMobiNative.getAdRating();
        if (rating != 0) {
            ratingBar.setRating(rating);
        }
        ratingBar.setVisibility(rating != 0 ? View.VISIBLE : View.GONE);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInMobiNative.reportAdClickAndOpenLandingPage();
            }
        });

        return adView;
    }

    private void loadExtraAdIntoView() {
        mInMobiNative2 = new InMobiNative(getActivity(), PlacementId.YOUR_PLACEMENT_ID_HEADERLINE, listener);
        mInMobiNative2.load();
    }

    @Override
    public void onAdLoadSucceeded(@NonNull InMobiNative inMobiNative) {
        //Pass the old ad view as the first parameter to facilitate view reuse.
        mAdView = loadAdIntoView(inMobiNative);
        if (mAdView == null) {
            Log.d(TAG, "Could not render Strand!");
        } else {
            mContainer.addView(mAdView);
        }
    }

    @Override
    public void onAdLoadFailed(@NonNull InMobiNative inMobiNative, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
        Log.d(TAG, "Ad Load failed (" + inMobiAdRequestStatus.getMessage() + ")");
    }

    @Override
    public void onAdFullScreenDismissed(InMobiNative inMobiNative) {

    }

    @Override
    public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {
        Log.d(TAG, "Ad going fullscreen.");
    }

    @Override
    public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {

    }

    @Override
    public void onUserWillLeaveApplication(InMobiNative inMobiNative) {

    }

    @Override
    public void onAdImpressed(@NonNull InMobiNative inMobiNative) {
        Log.d(TAG, "Impression recorded successfully");
    }

    @Override
    public void onAdClicked(@NonNull InMobiNative inMobiNative) {
        Log.d(TAG, "Ad clicked");
    }

    @Override
    public void onMediaPlaybackComplete(@NonNull InMobiNative inMobiNative) {

    }

    @Override
    public void onAdStatusChanged(@NonNull InMobiNative inMobiNative) {
        Log.d(TAG, "onAdStatusChanged : " + inMobiNative.getDownloader().getDownloadStatus());
    }
}
