package com.arman.queuetube.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddToPlaylistFragment extends DialogFragment {

    private List<Integer> selectedItems;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.selectedItems = new ArrayList<>();
        CharSequence[] playlists = new CharSequence[]{};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Save video to...")
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
                .setMultiChoiceItems(playlists, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            AddToPlaylistFragment.this.selectedItems.add(i);
                        } else if (AddToPlaylistFragment.this.selectedItems.contains(i)) {
                            AddToPlaylistFragment.this.selectedItems.remove(Integer.valueOf(i));
                        }
                    }
                });
        return builder.create();
    }

}
