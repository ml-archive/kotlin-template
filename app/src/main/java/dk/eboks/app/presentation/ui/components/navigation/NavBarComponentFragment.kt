package dk.eboks.app.presentation.ui.components.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.pasta.activity.PastaActivity
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.channels.ChannelsActivity
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewActivity
import dk.eboks.app.presentation.ui.screens.senders.overview.SendersOverviewActivity
import dk.eboks.app.util.disableShiftingMode
import kotlinx.android.synthetic.main.fragment_navbar_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class NavBarComponentFragment : BaseFragment(), NavBarComponentContract.View {

    @Inject
    lateinit var presenter : NavBarComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_navbar_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupMainNavigation()
    }

    override fun setupTranslations() {

    }

    private fun setupMainNavigation()
    {
        mainNavigationBnv.inflateMenu(R.menu.main)
        var menu = mainNavigationBnv.menu
        menu.findItem(R.id.actionHome).title = Translation.mainnav.homeButton
        menu.findItem(R.id.actionMail).title = Translation.mainnav.mailButton
        menu.findItem(R.id.actionSenders).title = Translation.mainnav.sendersButton
        menu.findItem(R.id.actionChannels).title = Translation.mainnav.channelsButton
        //menu.findItem(R.id.actionUploads).title = Translation.mainnav.uploadsButton
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
                    activityCls = PastaActivity::class.java
                    currentMenuItem = R.id.actionHome
                }
                R.id.actionMail -> {
                    activityCls = MailOverviewActivity::class.java
                    currentMenuItem = R.id.actionMail
                }
                R.id.actionChannels -> {
                    activityCls = ChannelsActivity::class.java
                    currentMenuItem = R.id.actionChannels
                }
                R.id.actionSenders -> {
                    activityCls = SendersOverviewActivity::class.java
                    currentMenuItem = R.id.actionSenders
                }
                else -> { }
            }
            activityCls?.let { startActivity(Intent(context, activityCls)) }
            //overridePendingTransition(0, 0)
            false
        }
    }

    companion object {
        var currentMenuItem = 0
        var firstRun = true
    }

}