package com.inmobi.nativead.sample.newsheadline;

import android.view.View;

import com.inmobi.ads.InMobiNative;

import java.lang.ref.WeakReference;

public final class NewsSnippet {
    public String title;
    public String imageUrl;
    public String content;
    public String landingUrl;
    public String description;
    public int progress = 0;
    public WeakReference<InMobiNative> inMobiNative;
}