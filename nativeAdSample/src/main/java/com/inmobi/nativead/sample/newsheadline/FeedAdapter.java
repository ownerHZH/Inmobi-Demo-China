package com.inmobi.nativead.sample.newsheadline;

import com.inmobi.ads.InMobiNative;
import com.inmobi.nativead.sample.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter<NewsSnippet> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<NewsSnippet> mItems;
    private NativeProvider mNativeProvider;
    private LinearLayout container_view;
    private ViewHolder mViewHolder;

    public FeedAdapter(Context context, List<NewsSnippet> items, NativeProvider nativeProvider) {
        super(context, R.layout.news_headline_view, items);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mNativeProvider = nativeProvider;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (null == convertView || null == convertView.getTag()) {
            convertView = mInflater.inflate(R.layout.news_headline_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.headline = (TextView) convertView.findViewById(R.id.caption);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.icon = (SimpleDraweeView) convertView.findViewById(R.id.photo);
            viewHolder.tag = (TextView) convertView.findViewById(R.id.sponsored);
            viewHolder.con_view=(LinearLayout)convertView.findViewById(R.id.container_view);
            viewHolder.btn = (Button)convertView.findViewById(R.id.btn);
            viewHolder.pb = (ProgressBar) convertView.findViewById(R.id.pb);
            viewHolder.pb.setMax(100);
            viewHolder.pb.setVisibility(View.INVISIBLE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final NewsSnippet newsSnippet = mItems.get(position);
        viewHolder.headline.setText(newsSnippet.title);
        viewHolder.content.setText(newsSnippet.description);
        viewHolder.icon.setImageURI(Uri.parse(newsSnippet.imageUrl));
        viewHolder.tag.setVisibility(View.VISIBLE);
        viewHolder.tag.setText("Sponsored");
        Log.e("Adapter==",newsSnippet.imageUrl);
        if(null==newsSnippet.inMobiNative||null==newsSnippet.inMobiNative.get())
        {
            viewHolder.con_view.setVisibility(View.GONE);
            viewHolder.tag.setVisibility(View.GONE);
            viewHolder.btn.setVisibility(View.GONE);
            viewHolder.pb.setVisibility(View.INVISIBLE);
        } else{
            viewHolder.tag.setVisibility(View.VISIBLE);
            viewHolder.tag.setText("Sponsored");
            viewHolder.con_view.setVisibility(View.VISIBLE);
            viewHolder.con_view.removeAllViews();

            viewHolder.con_view.addView(newsSnippet.inMobiNative.get().getPrimaryViewOfWidth(viewHolder.con_view,parent,1));

            if(newsSnippet.inMobiNative.get().isAppDownload()){
                if(newsSnippet.inMobiNative.get().getDownloader().getDownloadStatus() == InMobiNative.Downloader.STATE_DOWNLOADED){
                    viewHolder.btn.setText("打开");
                    viewHolder.pb.setProgress(100);
                    viewHolder.pb.setVisibility(View.VISIBLE);
                    viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            newsSnippet.inMobiNative.get().reportAdClickAndOpenLandingPage();
                        }
                    });
                    viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            newsSnippet.inMobiNative.get().reportAdClickAndOpenLandingPage();
                        }
                    });
                }else {
                    viewHolder.btn.setText("下载");
                    viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(viewHolder,newsSnippet.inMobiNative.get());
                        }
                    });
                    viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(viewHolder,newsSnippet.inMobiNative.get());
                        }
                    });
                }
                viewHolder.btn.setVisibility(View.VISIBLE);
            }else {
                viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newsSnippet.inMobiNative.get().reportAdClickAndOpenLandingPage();
                    }
                });
            }

        }

        convertView.setTag(R.id.container_view,position);

        return convertView;
    }

    protected void showDialog(final ViewHolder viewHolder,final InMobiNative nativeAd) {
        mViewHolder = viewHolder;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确认下载吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                viewHolder.pb.setVisibility(View.VISIBLE);
                nativeAd.reportAdClickAndOpenLandingPage();
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(nativeAd.getDownloader().getDownloadStatus() == InMobiNative.Downloader.STATE_DOWNLOADING){
                            viewHolder.pb.setProgress(nativeAd.getDownloader().getDownloadProgress());
                            handler.postDelayed(this,200);
                        }else {
                            viewHolder.pb.setProgress(100);
                            viewHolder.btn.setText("打开");
                            handler.removeCallbacks(this);
                        }
                    }
                };
                handler.postDelayed(runnable,300);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public class ViewHolder {
        TextView headline;
        TextView content;
        TextView tag;
        SimpleDraweeView icon;
        LinearLayout con_view;
        ProgressBar pb;
        Button btn;
    }
}