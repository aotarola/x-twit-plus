package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aotarolaalvarad on 11/7/15.
 */
public class User implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
        dest.writeString(this.name);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
    }

    protected User(Parcel in) {
        this.uid = in.readLong();
        this.name = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
