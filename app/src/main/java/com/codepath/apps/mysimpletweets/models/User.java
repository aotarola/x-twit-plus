package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aotarolaalvarad on 11/7/15.
 */
public class User {

    private long uid;
    private String name;
    private String screenName;
    private String profileImageUrl;

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public User(){
        name = "";
        uid = -1;
        screenName = "";
        profileImageUrl = "";
    }

    public static User fromJson(JSONObject json){
        User user = new User();

        try {
            user.setName(json.getString("name"));
            user.setUid(json.getLong("id"));
            user.setScreenName(json.getString("screen_name"));
            user.setProfileImageUrl(json.getString("profile_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
