package com.arman.queuetube.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.arman.queuetube.R
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper
import kotlinx.android.synthetic.main.dialog_create_playlist.*

class CreatePlaylistFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_create_playlist, null)
        val builder = AlertDialog.Builder(activity)
        builder
                .setTitle("Create a new playlist")
                .setPositiveButton("OK") { _, _ -> }
                .setNegativeButton("Cancel") { _, _ -> }
                .setView(view)
        val dialog = builder.create()
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val text = create_playlist_dialog_input.text.toString()
                if (text.isEmpty()) {
                    create_playlist_dialog_error_text!!.visibility = View.VISIBLE
                } else {
                    if (!GsonPlaylistHelper.writeNewIfNotFound(text)) {
                        create_playlist_dialog_error_text!!.visibility = View.VISIBLE
                    } else {
                        create_playlist_dialog_error_text!!.visibility = View.GONE
                        dialog.dismiss()
                    }
                }
            }
        }
        return dialog
    }

}
