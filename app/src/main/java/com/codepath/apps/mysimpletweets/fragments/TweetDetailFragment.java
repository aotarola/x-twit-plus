package com.codepath.apps.mysimpletweets.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.interfaces.ComposeFragmentListener;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TweetDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TweetDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TweetDetailFragment extends DialogFragment {

    private Tweet tweet;
    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvBody;
    private ImageView ivProfileImage;


    public TweetDetailFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    public static TweetDetailFragment newInstance(String title) {
        TweetDetailFragment frag = new TweetDetailFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        // request a window without the title
        frag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        tweet = args.getParcelable("tweet");
        return inflater.inflate(R.layout.fragment_tweet_detail, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AppDialogTheme);

        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvScreenName = (TextView) view.findViewById(R.id.tvScreenName);
        tvBody = (TextView) view.findViewById(R.id.tvBody);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText(Html.fromHtml("\u0040" + tweet.getUser().getScreenName()));
        tvBody.setText(Html.fromHtml(tweet.getBody()));

        Picasso.with(view.getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
