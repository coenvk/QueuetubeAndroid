package com.arman.queuetube.activities

import android.os.Bundle
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.playlist.PlaylistFragment

class PlaylistActivity : BackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playlistName = intent.getStringExtra(Constants.Fragment.Argument.PLAYLIST_NAME)
        val isEditable = intent.getBooleanExtra(Constants.Fragment.Argument.IS_EDITABLE, true)
        val isShufflable = intent.getBooleanExtra(Constants.Fragment.Argument.IS_SHUFFLABLE, true)
        val isSortable = intent.getBooleanExtra(Constants.Fragment.Argument.IS_SORTABLE, true)
        val isDraggable = intent.getBooleanExtra(Constants.Fragment.Argument.IS_DRAGGABLE, true)
        val bundle = Bundle()
        bundle.putString(Constants.Fragment.Argument.PLAYLIST_NAME, playlistName)
        bundle.putBoolean(Constants.Fragment.Argument.IS_EDITABLE, isEditable)
        bundle.putBoolean(Constants.Fragment.Argument.IS_SHUFFLABLE, isShufflable)
        bundle.putBoolean(Constants.Fragment.Argument.IS_SORTABLE, isSortable)
        bundle.putBoolean(Constants.Fragment.Argument.IS_DRAGGABLE, isDraggable)
        val fragment = PlaylistFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit()
    }

}
