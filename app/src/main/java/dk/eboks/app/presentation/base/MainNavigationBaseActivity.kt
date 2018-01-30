package dk.eboks.app.presentation.base

import android.app.Activity
import android.content.Intent
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.ui.mail.MailOverviewActivity
import dk.eboks.app.presentation.ui.main.MainActivity
import dk.eboks.app.presentation.ui.splash.SplashActivity
import dk.eboks.app.util.disableShiftingMode
import kotlinx.android.synthetic.main.include_navigation_view.*

/**
 * Created by bison on 30/01/18.
 */
abstract class MainNavigationBaseActivity : BaseActivity() {
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupMainNavigation()
    }

    private fun setupMainNavigation()
    {
        mainNavigationBnv.inflateMenu(R.menu.main)
        var menu = mainNavigationBnv.menu
        menu.findItem(R.id.actionHome).title = Translation.mainnav.homeButton
        menu.findItem(R.id.actionMail).title = Translation.mainnav.mailButton
        menu.findItem(R.id.actionSenders).title = Translation.mainnav.sendersButton
        menu.findItem(R.id.actionChannels).title = Translation.mainnav.channelsButton
        menu.findItem(R.id.actionUploads).title = Translation.mainnav.uploadsButton
        mainNavigationBnv.disableShiftingMode()
        mainNavigationBnv.selectedItemId = currentMenuItem
        /*
        if(firstRun)
        {
            firstRun = false
            currentMenuItem = mainNavigationBnv.selectedItemId
        }
        */

        mainNavigationBnv.setOnNavigationItemSelectedListener { item ->
            var activityCls : Class<out Activity>? = null
            when(item.itemId)
            {
                R.id.actionHome -> {
                    activityCls = MainActivity::class.java
                    currentMenuItem = R.id.actionHome
                }
                R.id.actionMail -> {
                    activityCls = MailOverviewActivity::class.java
                    currentMenuItem = R.id.actionMail
                }
                else -> { }
            }
            startActivity(Intent(this, activityCls))
            false
        }
    }

    companion object {
        var currentMenuItem = 0
        var firstRun = true
    }

}