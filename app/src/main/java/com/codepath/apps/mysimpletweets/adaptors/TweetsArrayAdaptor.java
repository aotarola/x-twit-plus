package com.codepath.apps.mysimpletweets.adaptors;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;
import com.codepath.apps.mysimpletweets.fragments.ComposeFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by aotarolaalvarad on 11/7/15.
 */
public class TweetsArrayAdaptor
        extends RecyclerView.Adapter<TweetsViewHolder> {

    private List<Tweet> tweets;


    public TweetsArrayAdaptor(List<Tweet> tweets) {
        this.tweets = tweets;
    }


    @Override
    public TweetsViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        TweetsViewHolder viewHolder = new TweetsViewHolder(contactView);
        viewHolder.setContext(context);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TweetsViewHolder viewHolder, int position) {
        // Get the data model based on position
        viewHolder.setTweet(tweets.get(position));
        viewHolder.process();

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

}
