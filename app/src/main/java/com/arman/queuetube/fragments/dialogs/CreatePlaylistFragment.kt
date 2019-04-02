package com.arman.queuetube.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.arman.queuetube.R
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper

class CreatePlaylistFragment : DialogFragment() {

    private var editText: EditText? = null
    private var errorText: TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_create_playlist, null)
        this.editText = view.findViewById(R.id.create_playlist_dialog_input) as EditText
        this.errorText = view.findViewById(R.id.create_playlist_dialog_error_text) as TextView
        val builder = AlertDialog.Builder(activity)
        builder
                .setTitle("Create a new playlist")
                .setPositiveButton("OK") { _, _ -> }
                .setNegativeButton("Cancel") { _, _ -> }
                .setView(view)
        val dialog = builder.create()
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE) as Button
            button.setOnClickListener {
                val text = editText!!.text.toString()
                if (text.isEmpty()) {
                    errorText!!.visibility = View.VISIBLE
                } else {
                    if (!GsonPlaylistHelper.writeNewIfNotFound(text)) {
                        errorText!!.visibility = View.VISIBLE
                    } else {
                        errorText!!.visibility = View.GONE
                        dialog.dismiss()
                    }
                }
            }
        }
        return dialog
    }

}
