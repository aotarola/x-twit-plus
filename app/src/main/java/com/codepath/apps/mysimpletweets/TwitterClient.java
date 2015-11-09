package com.codepath.apps.mysimpletweets;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "hi1IKAYtSmEk5wRC2yfAdE6RE";       // Change this
	public static final String REST_CONSUMER_SECRET = "EBTIMtDpF9l7f10CaHkwaxms5dYREZjDlpLWxWc1FrkiVFNU7y"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)
    public static final int PAGE_SIZE = 25;
    public static final int DEFAULT_TWEET_ID = 1;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(long lastTweetId, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/home_timeline.json");

		RequestParams params = new RequestParams();

		params.put("count", PAGE_SIZE);

		if(lastTweetId > 0){
			params.put("max_id", lastTweetId);
		}
		else {
			params.put("since", DEFAULT_TWEET_ID);
		}

		getClient().get(apiUrl, params, handler);
	}

	public void postStatus(String status, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");

		RequestParams params = new RequestParams();

        params.put("status", status);

        getClient().post(apiUrl, params, handler);

	}

    public void getMyProfile(AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("account/verify_credentials.json");

        RequestParams params = new RequestParams();

        params.put("skip_status", "true");

        getClient().get(apiUrl, params, handler);
    }

}