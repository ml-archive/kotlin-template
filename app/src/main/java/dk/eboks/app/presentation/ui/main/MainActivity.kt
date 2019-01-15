package dk.eboks.app.presentation.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.channels.components.overview.ChannelOverviewComponentFragment
import dk.eboks.app.presentation.ui.home.screens.HomeFragment
import dk.eboks.app.presentation.ui.mail.components.foldershortcuts.FolderShortcutsComponentFragment
import dk.eboks.app.presentation.ui.notimplemented.screens.ComingSoonFragment
import dk.eboks.app.presentation.ui.senders.screens.overview.SerdersOverviewFragment
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainNavigator {

    private var shownFragment: Fragment? = null

    private val homeFragment: HomeFragment
        get() = findFragment() ?: HomeFragment()
    private val channelOverviewComponentFragment: ChannelOverviewComponentFragment
        get() = findFragment() ?: ChannelOverviewComponentFragment()

    private val sendersOverviewFragment: SerdersOverviewFragment
        get() = findFragment() ?: SerdersOverviewFragment()

    private val uploadOverviewComponentFragment: UploadOverviewComponentFragment
        get() = findFragment() ?: UploadOverviewComponentFragment()

    private val folderShortcutsComponentFragment: FolderShortcutsComponentFragment
        get() = findFragment() ?: FolderShortcutsComponentFragment()

    private val csSender: ComingSoonFragment
        get() = findFragment(ComingSoonSenderTag) ?: ComingSoonFragment.newInstance(
            Translation.senders.comingSoonHeader,
            Translation.senders.comingSoonMessage
        )
    private val csUpload: ComingSoonFragment
        get() = findFragment(ComingSoonUploadTag) ?: ComingSoonFragment.newInstance(
            Translation.uploads.comingSoonHeader,
            Translation.uploads.comingSoonMessage
        )

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener {
        showMainSection(Section.fromId(it.itemId))
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        mainNavigationBnv.run {
            menu.getItem(0).title = Translation.mainnav.homeButton
            menu.getItem(1).title = Translation.mainnav.mailButton
            menu.getItem(2).title = Translation.mainnav.sendersButton
            menu.getItem(3).title = Translation.mainnav.channelsButton
            menu.getItem(4).title = Translation.mainnav.uploadsButton
            setOnNavigationItemSelectedListener(navListener)
        }
    }

    override fun showMainSection(section: Section) {
        mainNavigationBnv.setOnNavigationItemSelectedListener(null)
        mainNavigationBnv.selectedItemId = section.id
        when (section) {
            Section.Home -> setMainFragment(homeFragment)
            Section.Mail -> setMainFragment(folderShortcutsComponentFragment)
            Section.Channels -> setMainFragment(channelOverviewComponentFragment)
            Section.Senders -> {
                if (BuildConfig.ENABLE_SENDERS) setMainFragment(sendersOverviewFragment)
                else setMainFragment(csSender, ComingSoonSenderTag)
            }
            Section.Uploads -> {
                if (BuildConfig.ENABLE_UPLOADS) {
                    setMainFragment(uploadOverviewComponentFragment)
                } else {
                    setMainFragment(csUpload, ComingSoonUploadTag)
                }
            }
        }
        mainNavigationBnv.setOnNavigationItemSelectedListener(navListener)
    }

    private fun setMainFragment(fragment: Fragment, tag: String? = null) {
        if (shownFragment == fragment) return
        // Clear back stack and avoid pop animations
        clearBackStack()
        val ft = supportFragmentManager.beginTransaction()

        // We hide/show the fragments normally, add() only once
        if (!fragment.isAdded) {
            ft.add(R.id.fragmentHolderLayout, fragment, tag ?: fragment::class.java.simpleName)
        }
        ft.show(fragment)

        // Hide currently shown fragment
        shownFragment?.let {
            ft.hide(it)
        }

        ft.commit()

        // Save for later
        shownFragment = fragment
    }

    companion object {
        private const val ComingSoonSenderTag = "csSender"
        private const val ComingSoonUploadTag = "csUpload"
    }
}
