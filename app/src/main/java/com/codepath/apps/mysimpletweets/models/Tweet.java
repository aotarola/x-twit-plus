package com.codepath.apps.mysimpletweets.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by aotarolaalvarad on 11/7/15.
 */
public class Tweet {
    private String body;
    private long uid;
    private User user;
    private String createdAt;

    // Deserialize the JSON

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public String getRelativeTimeAgo() {
        int diffInDays = 0;
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat format = new SimpleDateFormat(twitterFormat);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        String formattedDate = format.format(c.getTime());
        String dateToPresent = "";
        Date d1 = null;
        Date d2 = null;
        try {

            d1 = format.parse(formattedDate);
            d2 = format.parse(getCreatedAt());
            long diff = d1.getTime() - d2.getTime();

            diffInDays = (int) (diff / (1000 * 60 * 60 * 24));
            if (diffInDays > 0) {
                dateToPresent = diffInDays + "d";
            } else {
                int diffHours = (int) (diff / (60 * 60 * 1000));
                if (diffHours > 0) {
                    dateToPresent = diffHours + "h";
                } else {

                    int diffMinutes = (int) ((diff / (60 * 1000) % 60));
                    dateToPresent = diffMinutes + "m";
                }
            }

        } catch (ParseException e) {
            // System.out.println("Err: " + e);
            e.printStackTrace();
        }

        return dateToPresent;
    }

    public static Tweet fromJSON(JSONObject json){
        Tweet tweet = new Tweet();

        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            tweet.createdAt = json.getString("created_at");
            tweet.user = User.fromJson(json.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i=0; i < jsonArray.length(); i++){
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if(tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    public static Tweet getLastTweet(ArrayList<Tweet> tweets) {
        return tweets.get(tweets.size() - 1);
    }
}
