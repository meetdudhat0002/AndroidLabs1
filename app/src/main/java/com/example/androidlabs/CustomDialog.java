package com.example.androidlabs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CustomDialog extends AppCompatDialogFragment {
    private EditText editTextMsg;
    //private EditText editTextPassword;
    private CustomDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate( R.layout.custom_dialog, null);

        builder.setView(view)
                .setTitle("New Message")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.create().dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtMsg = editTextMsg.getText().toString();
                        //String password = editTextPassword.getText().toString();
                        //listener.applyTexts(username, password);
                        listener.applyTexts(txtMsg);
                    }
                });

        editTextMsg = view.findViewById(R.id.txtMsg);
        //editTextPassword = view.findViewById(R.id.edit_password);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CustomDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement CustomDialogListener");
        }
    }

    public interface CustomDialogListener {
        //void applyTexts(String username, String password);
        void applyTexts(String msg);
    }
}
