package com.codepath.apps.mysimpletweets.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by aotarolaalvarad on 11/14/15.
 */
public class TweetsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    private ImageView ivProfileImage;
    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvBody;
    private TextView tvDateAgo;
    private Context context;

    private Tweet tweet;

    public TweetsViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
        tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
        tvBody = (TextView) itemView.findViewById(R.id.tvBody);
        tvDateAgo = (TextView) itemView.findViewById(R.id.tvDateAgo);
    }

    @Override
    public void onClick(View view) {
        TimelineActivity timeLineActivity = (TimelineActivity)view.getContext();
        timeLineActivity.showTweetDetailedView(view, tweet);
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    public void process() {

        tvDateAgo.setText(tweet.getRelativeTimeAgo());
        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText(Html.fromHtml("\u0040" + tweet.getUser().getScreenName()));
        tvBody.setText(Html.fromHtml(tweet.getBody()));

        ivProfileImage.setImageResource(android.R.color.transparent);

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(true)
                .build();

        Picasso.with(context)
                .load(tweet.getUser().getProfileImageUrl())
                .transform(transformation)
                .into(ivProfileImage);

    }
}
