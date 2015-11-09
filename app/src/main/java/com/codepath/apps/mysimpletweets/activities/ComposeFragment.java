package com.codepath.apps.mysimpletweets.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;


import com.codepath.apps.mysimpletweets.R;
// ...

public class ComposeFragment extends DialogFragment {

	private EditText mEditText;

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
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_compose, container);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AppDialogTheme);
		// Get field from view
		mEditText = (EditText) view.findViewById(R.id.txt_your_name);
		// Show soft keyboard automatically and request focus to field
		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}
}