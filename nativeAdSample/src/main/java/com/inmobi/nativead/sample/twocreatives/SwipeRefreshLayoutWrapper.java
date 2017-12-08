package com.inmobi.nativead.sample.twocreatives;

/**
 * Created by jerry.hu on 06/12/17.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup;

final class SwipeRefreshLayoutWrapper {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    interface Listener {
        boolean canChildScrollUp();

        void onRefresh();
    }

    static SwipeRefreshLayout getInstance(@NonNull Context context,
                                          @NonNull final Listener listener) {
        final SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(context) {

            @Override
            public boolean canChildScrollUp() {
                return listener.canChildScrollUp();
            }
        };
        swipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                HANDLER.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRefresh();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        return swipeRefreshLayout;
    }
}

