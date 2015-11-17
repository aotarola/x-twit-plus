package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.listeners.CustomRecyclerViewListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aotarolaalvarad on 11/15/15.
 */
public class HomeTimeLineFragment extends TweetsListFragment {

    private TwitterClient client;
    private long lastTweetId = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        populateTimeLine(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        getRvTweets().setOnScrollListener(new CustomRecyclerViewListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int current_page) {
                populateTimeLine(false);
            }

            @Override
            public void onHide() {
                //hideViews();
            }

            @Override
            public void onShow() {
                //showViews();
            }
        });

        getSpRefresh().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                populateTimeLine(true);
            }
        });

        return view;
    }

    private void populateTimeLine(final boolean shouldClear) {

        if(!isNetworkAvailable()){
            Toast.makeText(getActivity(), "Loaded from cache", Toast.LENGTH_SHORT)
                    .show();

            List<Tweet> queryResults = new Select().from(Tweet.class)
                    .orderBy("uid ASC").limit(100).execute();

            addAll(queryResults);
            spRefresh.setRefreshing(false);
            return;
        } else {

            if (shouldClear) {
                getaTweets().clear();
                lastTweetId = 0;
            }

            client.getHomeTimeline(lastTweetId, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("DEBUG", json.toString());


                    ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
                    if (lastTweetId > 0) {
                        getTweets().remove(0);
                    }
                    addAll(newTweets);
                    spRefresh.setRefreshing(false);
                    lastTweetId = Tweet.getLastTweet(getaTweets().getTweets()).getUid();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("ERROR", errorResponse.toString());
                }
            });
        }
    }
}
