package com.codepath.apps.mysimpletweets.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adaptors.TweetsArrayAdaptor;
import com.codepath.apps.mysimpletweets.decorators.DividerItemDecoration;
import com.codepath.apps.mysimpletweets.fragments.ComposeFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetDetailFragment;
import com.codepath.apps.mysimpletweets.interfaces.ComposeFragmentListener;
import com.codepath.apps.mysimpletweets.listeners.CustomRecyclerViewListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity implements ComposeFragmentListener {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdaptor aTweets;
    private RecyclerView rvTweets;
    private User currentUser;
    private LinearLayout bottomLayout;
    private SwipeRefreshLayout spRefresh;
    Toolbar toolbar;

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
        bottomLayout = (LinearLayout) findViewById(R.id.llBottom);
        setSupportActionBar(toolbar);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdaptor(this, tweets);

        spRefresh = (SwipeRefreshLayout) findViewById(R.id.spRefresh);

        spRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                populateTimeLine(true);
            }
        });

        rvTweets.setAdapter(aTweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setOnScrollListener(new CustomRecyclerViewListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int current_page) {
                populateTimeLine(false);
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

        rvTweets.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvTweets.setHasFixedSize(true);
        rvTweets.setItemAnimator(new DefaultItemAnimator());

        client = TwitterApplication.getRestClient();
        getCurrentUserInfo();
        populateTimeLine(false);
    }


    private void hideViews() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) bottomLayout.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        bottomLayout.animate().translationY(bottomLayout.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        bottomLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        MenuItem miGoTop = menu.findItem(R.id.miGoTop);
        miGoTop.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                rvTweets.smoothScrollToPosition(0);
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

    private void populateTimeLine(final boolean shouldClear) {

        if(!isNetworkAvailable()){
            Toast.makeText(TimelineActivity.this, "Loaded from cache", Toast.LENGTH_SHORT)
                    .show();

            List<Tweet> queryResults = new Select().from(Tweet.class)
                    .orderBy("uid ASC").limit(100).execute();

            tweets.addAll(queryResults);
            aTweets.notifyDataSetChanged();
            return;
        } else {

            if (shouldClear) {
                tweets.clear();
                lastTweetId = 0;
            }

            client.getHomeTimeline(lastTweetId, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("DEBUG", json.toString());


                    ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
                    if (lastTweetId > 0) {
                        tweets.remove(0);
                    }
                    tweets.addAll(newTweets);
                    aTweets.notifyDataSetChanged();
                    spRefresh.setRefreshing(false);

                    lastTweetId = Tweet.getLastTweet(tweets).getUid();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("ERROR", errorResponse.toString());
                }
            });
        }
    }

    public void OnComposeClick(View view) {

        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance("Some Title");
        Bundle args = new Bundle();
        args.putParcelable("user", currentUser);
        composeFragment.setArguments(args);
        composeFragment.show(fm, "compose_fragment");
    }

    public void showTweetDetailedView(View view, Tweet tweet) {
        FragmentManager fm = getSupportFragmentManager();
        TweetDetailFragment tweetDetailFragment = TweetDetailFragment.newInstance("Some Title");
        Bundle args = new Bundle();
        args.putParcelable("tweet", tweet);
        tweetDetailFragment.setArguments(args);
        tweetDetailFragment.show(fm, "tweet_detail_fragment");
    }

    @Override
    public void onComposeFinish(final String status, long replyId) {
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

        client.postStatus(status, replyId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d("DEBUG", json.toString());
                Tweet tweet = new Tweet();
                tweet.setBody(status);
                tweets.add(0, tweet);
                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

}
