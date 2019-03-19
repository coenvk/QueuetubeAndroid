package com.arman.queuetube.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arman.queuetube.R;
import com.arman.queuetube.modules.playlists.JSONPlaylistHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreatePlaylistFragment extends DialogFragment {

    private EditText editText;
    private TextView errorText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_playlist_dialog_fragment, null);
        this.editText = (EditText) view.findViewById(R.id.create_playlist_dialog_input);
        this.errorText = (TextView) view.findViewById(R.id.create_playlist_dialog_error_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Create new playlist...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = (Button) dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text = editText.getText().toString();
                        if (text.isEmpty()) {
                            errorText.setVisibility(View.VISIBLE);
                        } else {
                            if (!JSONPlaylistHelper.writeNewIfNotFound(text)) {
                                errorText.setVisibility(View.VISIBLE);
                            } else {
                                errorText.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        }
                    }
                });
            }
        });
        return dialog;
    }

}
