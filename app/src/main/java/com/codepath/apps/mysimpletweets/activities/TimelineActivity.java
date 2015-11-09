package com.codepath.apps.mysimpletweets.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adaptors.TweetsArrayAdaptor;
import com.codepath.apps.mysimpletweets.interfaces.ComposeFragmentListener;
import com.codepath.apps.mysimpletweets.listeners.CustomRecyclerViewListener;
import com.codepath.apps.mysimpletweets.listeners.EndlessRecyclerOnScrollListener;
import com.codepath.apps.mysimpletweets.listeners.HidingScrollListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class TimelineActivity extends AppCompatActivity implements ComposeFragmentListener {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdaptor aTweets;
    private RecyclerView lvTweets;
    private User currentUser;
    Toolbar toolbar;

    private ImageButton ibCompose;

    private long lastTweetId = 0;

    private void getCurrentUserInfo(){
        if(!isNetworkAvailable()){
            Toast.makeText(TimelineActivity.this, "Network unavailable :(", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        client.getMyProfile(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());
                currentUser = User.fromJson(json);
                Toast.makeText(TimelineActivity.this, "goood", Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String body, Throwable throwable) {
                Log.e("ERROR", body.toString());
                Log.e("ERROR", headers.toString());
                Toast.makeText(TimelineActivity.this, "epaa", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ibCompose = (ImageButton) findViewById(R.id.ibCompose);
        lvTweets = (RecyclerView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdaptor(this, tweets);
        lvTweets.setAdapter(aTweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lvTweets.setLayoutManager(linearLayoutManager);
        lvTweets.setOnScrollListener(new CustomRecyclerViewListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int current_page) {
                populateTimeLine();
            }

            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

        client = TwitterApplication.getRestClient();
        getCurrentUserInfo();
        populateTimeLine();
    }


    private void hideViews() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ibCompose.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        ibCompose.animate().translationY(ibCompose.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        ibCompose.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        MenuItem miGoTop = menu.findItem(R.id.miGoTop);
        miGoTop.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
            lvTweets.smoothScrollToPosition(0);
            return true;
            }
        });

        return true;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void populateTimeLine() {

        if(!isNetworkAvailable()){
            Toast.makeText(TimelineActivity.this, "Network unavailable :(", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        client.getHomeTimeline(lastTweetId, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
                if(lastTweetId > 0){
                    tweets.remove(0);
                }
                tweets.addAll(newTweets);
                aTweets.notifyDataSetChanged();

                lastTweetId = Tweet.getLastTweet(tweets).getUid();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("ERROR", errorResponse.toString());
            }
        });
    }

    public void OnComposeClick(View view) {

        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment editNameDialog = ComposeFragment.newInstance("Some Title");
        editNameDialog.show(fm, "compose_fragment");
    }

    @Override
    public void onComposeFinish(final String status) {
        if(!isNetworkAvailable()){
            Toast.makeText(TimelineActivity.this, "Network unavailable :(", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Tweet tweet = new Tweet(getResources().getConfiguration().locale);
        tweet.setUser(currentUser);
        tweet.setBody(status);
        tweets.add(0, tweet);
        aTweets.notifyDataSetChanged();

//        client.postStatus(status, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                Log.d("DEBUG", json.toString());
//                Tweet tweet = new Tweet();
//                tweet.setBody(status);
//                tweets.add(0, tweet);
//                aTweets.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("DEBUG", errorResponse.toString());
//            }
//        });
    }

}
