package com.arman.queuetube.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.arman.queuetube.config.Constants
import com.arman.queuetube.fragments.PlaylistFragment

class PlaylistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setupActionBar()

        val playlistName = intent.getStringExtra(Constants.Fragment.Argument.PLAYLIST_NAME)
        val bundle = Bundle()
        bundle.putString(Constants.Fragment.Argument.PLAYLIST_NAME, playlistName)
        val fragment = PlaylistFragment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit()
    }

    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
