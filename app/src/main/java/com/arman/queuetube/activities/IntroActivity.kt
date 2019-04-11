package com.arman.queuetube.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arman.queuetube.R
import com.arman.queuetube.util.IntroPreferenceManager
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment

class IntroActivity : AppIntro2() {

    private lateinit var preferenceManager: IntroPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.preferenceManager = IntroPreferenceManager(this)
        if (!this.preferenceManager.isFirstLaunch()) {
            launchMain()
            finish()
        }

        addSlide(AppIntro2Fragment.newInstance("Slide 1", "Here goes a description", R.drawable.ic_album_white_48dp, Color.BLUE, Color.WHITE, Color.WHITE))
        addSlide(AppIntro2Fragment.newInstance("Slide 2", "Here goes a description", R.drawable.ic_at_white_48dp, Color.BLUE, Color.WHITE, Color.WHITE))
        addSlide(AppIntro2Fragment.newInstance("Slide 3", "Here goes a description", R.drawable.ic_playlist_check_white_48dp, Color.BLUE, Color.WHITE, Color.WHITE))

        setFadeAnimation()
    }

    private fun launchMain() {
        this.preferenceManager.setFirstLaunch(false)
        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
        finish()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        launchMain()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        launchMain()
    }

}