package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.HomeTimeLineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetDetailFragment;
import com.codepath.apps.mysimpletweets.interfaces.ShowUserProfileListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.apache.http.Header;
public class TimelineActivity extends AppCompatActivity implements ShowUserProfileListener {

    private User currentUser;
    private ViewPager vpPager;
    Toolbar toolbar;
    private TwitterClient client;

    private void getCurrentUserInfo(){

        client.getMyProfile(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                currentUser = User.fromJson(json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String body, Throwable throwable) {
                Toast.makeText(TimelineActivity.this, body.toString(), Toast.LENGTH_LONG);
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

        vpPager = (ViewPager) findViewById(R.id.viewpager);

        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
        client = TwitterApplication.getRestClient();
        getCurrentUserInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        MenuItem miGoTop = menu.findItem(R.id.miGoTop);
        miGoTop.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               // TweetsListFragment tweetsListFragment = (TweetsListFragment) vpPager.getAdapter();



                //tweetsListFragment.scrollToTop();
                return true;
            }
        });

        return true;
    }

    public void onProfileView(MenuItem mi){
        Intent i = new Intent(this, ProfileActivity.class);

        startActivity(i);
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
    public void onShowUserProfile(String screenName) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", screenName);
        startActivity(i);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new HomeTimeLineFragment();
            }
            else if (position == 1){
                return new MentionsTimelineFragment();
            }
            else{
                return null;
            }
        }


        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return tabTitles[position];
        }
    }

}
