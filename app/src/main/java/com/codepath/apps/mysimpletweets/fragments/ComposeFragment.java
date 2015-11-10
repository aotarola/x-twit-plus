package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.interfaces.ComposeFragmentListener;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;
// ...

public class ComposeFragment extends DialogFragment {

	private TextView tvCharCount;
	private EditText etStatus;
	private Button btnTwit;
    private User user;
    private ImageView ivProfileImage;
	public final static int MAX_TWIT = 140;


    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			//This sets a textview to the current length
			tvCharCount.setText(String.valueOf(MAX_TWIT - s.length()));
		}

		public void afterTextChanged(Editable s) {
		}
	};

	public ComposeFragment() {
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

	public static ComposeFragment newInstance(String title) {
		ComposeFragment frag = new ComposeFragment();
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
        user = args.getParcelable("user");

		return inflater.inflate(R.layout.fragment_compose, container);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AppDialogTheme);
		// Get field from view
		etStatus = (EditText) view.findViewById(R.id.etStatus);
		tvCharCount = (TextView) view.findViewById(R.id.tvCharCount);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        btnTwit = (Button) view.findViewById((R.id.btnTwit));

        etStatus.addTextChangedListener(mTextEditorWatcher);


        btnTwit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ComposeFragmentListener listener = (ComposeFragmentListener) getActivity();
                listener.onComposeFinish(etStatus.getText().toString(), -1);
                dismiss();
            }
        });

        Picasso.with(view.getContext()).load(user.getProfileImageUrl()).into(ivProfileImage);


        //etStatus.setOnKeyListener();
		// Show soft keyboard automatically and request focus to field
		etStatus.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}
}