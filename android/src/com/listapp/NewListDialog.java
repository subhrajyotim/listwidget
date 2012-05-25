package com.listapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class NewListDialog extends DialogFragment implements OnEditorActionListener {

    public interface NewListDialogListener {
        void addList (String name);
    }
    
	private EditText editText;

	public NewListDialog() {
		// This constructor intentionally left blank.
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_new_list, container);
		editText = (EditText) root.findViewById(R.id.editText1);
		getDialog().setTitle(R.string.new_list_name);
		editText.setOnEditorActionListener(this);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		editText.requestFocus();
		return root;
	}

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            NewListDialogListener activity = (NewListDialogListener) getActivity();
            activity.addList(editText.getText().toString());
            NewListDialog.this.dismiss();
            return true;
        }
        return false;
    }

}
