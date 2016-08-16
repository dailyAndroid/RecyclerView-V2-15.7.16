package com.example.hwhong.recyclerviewv2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by hwhong on 7/13/16.
 */
public class ListRowViewHolder extends RecyclerView.ViewHolder{

    protected NetworkImageView thumbnail;
    protected TextView title;
    protected TextView subreddit;
    protected TextView author;
    protected TextView url;
    protected RelativeLayout relativeLayout;


    public ListRowViewHolder(View view) {
        super(view);
        thumbnail = (NetworkImageView) view.findViewById(R.id.imageView);
        title = (TextView) view.findViewById(R.id.articleTitle);
        subreddit = (TextView) view.findViewById(R.id.subreddit);
        author = (TextView) view.findViewById(R.id.author);
        url = (TextView) view.findViewById(R.id.url);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        view.setClickable(true);
    }


}
