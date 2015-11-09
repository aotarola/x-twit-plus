package com.codepath.apps.mysimpletweets.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aotarolaalvarad on 11/7/15.
 */
public class TweetsArrayAdaptor  extends
        RecyclerView.Adapter<TweetsArrayAdaptor.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvUserName;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvDateAgo;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvDateAgo = (TextView) itemView.findViewById(R.id.tvDateAgo);
        }
    }

    public TweetsArrayAdaptor(Context context, List<Tweet> tweets) {
        this.mTweets = tweets;
        this.context = context;
    }


    @Override
    public TweetsArrayAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TweetsArrayAdaptor.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Tweet tweet = mTweets.get(position);

        // Set item views based on the data model
        ImageView ivProfileImage = viewHolder.ivProfileImage;
        TextView tvUserName = viewHolder.tvUserName;
        TextView tvScreenName = viewHolder.tvScreenName;
        TextView tvBody = viewHolder.tvBody;
        TextView tvDateAgo = viewHolder.tvDateAgo;

        tvDateAgo.setText(tweet.getRelativeTimeAgo());
        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText(Html.fromHtml("\u0040" + tweet.getUser().getScreenName()));
        tvBody.setText(Html.fromHtml(tweet.getBody()));

        ivProfileImage.setImageResource(android.R.color.transparent);;

        Picasso.with(context).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

}
