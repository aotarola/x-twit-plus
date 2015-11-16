package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aotarolaalvarad on 11/7/15.
 */
@Table(name = "Users")
public class User extends Model implements Parcelable {

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "name")
    private String name;

    @Column(name = "screen_name")
    private String screenName;

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "tag_line")
    private String tagLine;

    @Column(name = "followers_count")
    private int followersCount;

    @Column(name = "following_count")
    private int followingCount;

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
        super();
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
            user.setTagLine(json.getString("description"));
            user.setFollowersCount(json.getInt("followers_count"));
            user.setFollowingCount(json.getInt("friends_count"));
            user.setScreenName(json.getString("screen_name"));
            user.setProfileImageUrl(json.getString("profile_image_url"));
            user.save();
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

    public List<Tweet> tweets() {
        return getMany(Tweet.class, "User");
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
