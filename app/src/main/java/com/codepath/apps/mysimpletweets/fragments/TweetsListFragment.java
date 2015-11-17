package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.adaptors.TweetsArrayAdaptor;
import com.codepath.apps.mysimpletweets.interfaces.ComposeFragmentListener;
import com.codepath.apps.mysimpletweets.interfaces.ShowUserProfileListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aotarolaalvarad on 11/14/15.
 */
public class TweetsListFragment extends Fragment implements ComposeFragmentListener {


    protected TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdaptor aTweets;

    private RecyclerView rvTweets;
    private SwipeRefreshLayout spRefresh;
    private LinearLayout bottomLayout;
    protected LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);

        bottomLayout = (LinearLayout) view.findViewById(R.id.llBottom);

        rvTweets.setAdapter(aTweets);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(linearLayoutManager);

        //rvTweets.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvTweets.setHasFixedSize(true);
        rvTweets.setItemAnimator(new DefaultItemAnimator());

        spRefresh = (SwipeRefreshLayout) view.findViewById(R.id.spRefresh);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdaptor(tweets);

    }

    public void OnComposeClick(View view) {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance("Some Title");
        Bundle args = new Bundle();
        //args.putParcelable("user", currentUser);
        composeFragment.setArguments(args);
        composeFragment.show(fm, "compose_fragment");
    }

    @Override
    public void onComposeFinish(final String status, long replyId) {
        if(!isNetworkAvailable()){
            Toast.makeText(getActivity(), "Network unavailable :(", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Tweet tweet = new Tweet(getResources().getConfiguration().locale);
        //tweet.setUser(currentUser);
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

    protected void hideViews() {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) bottomLayout.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        bottomLayout.animate().translationY(bottomLayout.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    protected void showViews() {
        bottomLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
        spRefresh.setRefreshing(false);
    }


    public TweetsArrayAdaptor getaTweets() {
        return aTweets;
    }

    public RecyclerView getRvTweets() {
        return rvTweets;
    }

    public SwipeRefreshLayout getSpRefresh() {
        return spRefresh;
    }

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public void scrollToTop(){
        rvTweets.smoothScrollToPosition(0);
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
