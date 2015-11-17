package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.mysimpletweets.TwitterApplication;
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
public class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        populateTimeLine(false);
    }

    private void populateTimeLine(final boolean shouldClear) {

        if (!isNetworkAvailable()) {
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
            }

            client.getMentionsTimeline(new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("DEBUG", json.toString());


                    ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
                    addAll(newTweets);
                    spRefresh.setRefreshing(false);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("ERROR", errorResponse.toString());
                }
            });
        }
    }
}
