package dk.eboks.app.presentation.ui.main

import android.content.Context
import android.content.Intent
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
import dk.eboks.app.util.guard
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
            menu.findItem(R.id.actionHome).title = Translation.mainnav.homeButton
            menu.findItem(R.id.actionMail).title = Translation.mainnav.mailButton
            menu.findItem(R.id.actionSenders).title = Translation.mainnav.sendersButton
            menu.findItem(R.id.actionChannels).title = Translation.mainnav.channelsButton
            menu.findItem(R.id.actionUploads).title = Translation.mainnav.uploadsButton
            (intent.getSerializableExtra(PARAM_SECTION) as? Section)?.let(::showMainSection).guard {
                setOnNavigationItemSelectedListener(navListener)
            }
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
                if (BuildConfig.ENABLE_SENDERS) {
                    setMainFragment(sendersOverviewFragment)
                } else {
                    setMainFragment(csSender, ComingSoonSenderTag)
                }
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

    private fun setMainFragment(
        fragment: Fragment,
        tag: String? = null,
        clearBackStack: Boolean = true
    ) {
        if (shownFragment == fragment) return
        // Clear back stack and avoid pop animations

        if (clearBackStack) {
            while (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStackImmediate()
            }
        }
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
        private const val PARAM_SECTION = "section"
        fun createIntent(context: Context, section: Section = Section.Mail): Intent =
            Intent(context, MainActivity::class.java)
                .putExtra(PARAM_SECTION, section)
    }
}
