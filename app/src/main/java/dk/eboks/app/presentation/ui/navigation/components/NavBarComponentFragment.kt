package dk.eboks.app.presentation.ui.navigation.components

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.main.MainActivity
import dk.eboks.app.presentation.ui.main.Section
import dk.eboks.app.util.disableShiftingMode
import kotlinx.android.synthetic.main.fragment_navbar_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
@Deprecated("Use MainActivity")
class NavBarComponentFragment : BaseFragment(), NavBarComponentContract.View {

    @Inject lateinit var presenter: NavBarComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navbar_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupMainNavigation()
    }

    private fun setupMainNavigation() {
        mainNavigationBnv.inflateMenu(R.menu.main)
        val menu = mainNavigationBnv.menu
        menu.findItem(R.id.actionHome).title = Translation.mainnav.homeButton
        menu.findItem(R.id.actionMail).title = Translation.mainnav.mailButton
        menu.findItem(R.id.actionSenders).title = Translation.mainnav.sendersButton
        menu.findItem(R.id.actionChannels).title = Translation.mainnav.channelsButton
        menu.findItem(R.id.actionUploads).title = Translation.mainnav.uploadsButton
        mainNavigationBnv.disableShiftingMode()

        getBaseActivity()?.let {
            val menuId = it.getNavigationMenuAction()
            if (menuId != -1)
                currentMenuItem = menuId
        }

        mainNavigationBnv.selectedItemId = currentMenuItem

        mainNavigationBnv.setOnNavigationItemSelectedListener { item ->
            startActivity(
                MainActivity.createIntent(
                    context ?: return@setOnNavigationItemSelectedListener false,
                    Section.fromId(item.itemId)
                )
            )
            currentMenuItem = item.itemId
            false
        }
    }

    companion object {
        var currentMenuItem = 0

        fun gotoChannels(activity: Activity) {
            activity.startActivity(MainActivity.createIntent(activity, Section.Channels))
            currentMenuItem = R.id.actionChannels
        }

        fun gotoMail(activity: Activity) {
            currentMenuItem = R.id.actionMail
            activity.startActivity(MainActivity.createIntent(activity, Section.Mail))
        }

        fun gotoInbox(activity: Activity) {
            currentMenuItem = R.id.actionMail
            activity.startActivity(MainActivity.createIntent(activity, Section.Inbox))
        }
    }
}