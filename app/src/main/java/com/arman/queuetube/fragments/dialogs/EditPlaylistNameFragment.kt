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
import com.arman.queuetube.config.Constants
import com.arman.queuetube.listeners.OnDialogDismissListener
import com.arman.queuetube.modules.playlists.json.GsonPlaylistHelper

class EditPlaylistNameFragment : DialogFragment() {

    private var editText: EditText? = null
    private var errorText: TextView? = null

    private var currentName: String? = null
    var onDialogDismissListener: OnDialogDismissListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        if (arguments != null) {
            this.currentName = arguments.getString(Constants.Fragment.Argument.PLAYLIST_NAME, "")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_edit_playlist_name, null)
        this.editText = view.findViewById(R.id.edit_playlist_name_dialog_input) as EditText
        if (this.currentName != null) {
            this.editText?.setText(this.currentName!!, TextView.BufferType.EDITABLE)
        }
        this.errorText = view.findViewById(R.id.edit_playlist_name_dialog_error_text) as TextView
        val builder = AlertDialog.Builder(activity)
        builder
                .setTitle("Edit playlist name")
                .setPositiveButton("OK") { _, _ -> }
                .setNegativeButton("Cancel") { _, _ -> }
                .setView(view)
        val dialog = builder.create()
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE) as Button
            button.setOnClickListener {
                val text = editText!!.text.toString()
                if (text.isEmpty() || this.currentName == null) {
                    errorText!!.visibility = View.VISIBLE
                } else {
                    val bundle = Bundle()
                    bundle.putString(Constants.Fragment.Argument.PLAYLIST_NAME, text)
                    this.onDialogDismissListener!!.onDialogDismiss(bundle)

                    if (!GsonPlaylistHelper.editName(this.currentName!!, text)) {
                        errorText!!.visibility = View.VISIBLE

                        bundle.putString(Constants.Fragment.Argument.PLAYLIST_NAME, this.currentName!!)
                        this.onDialogDismissListener!!.onDialogDismiss(bundle)
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