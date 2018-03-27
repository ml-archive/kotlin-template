package dk.eboks.app.presentation.ui.screens.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import dk.eboks.app.presentation.ui.screens.profile.ProfileActivity
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class HomeActivity : BaseActivity(), HomeContract.View {
    @Inject lateinit var presenter: HomeContract.Presenter

    val onLanguageChange : (Locale)->Unit = { locale ->
        Timber.e("Locale changed to locale")
        updateTranslation()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_home)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
        updateTranslation()
    }

    private fun updateTranslation() {
        mainTb.title = Translation.home.windowHeader
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        val menuProfile = mainTb.menu.add("_profile")
        menuProfile.setIcon(R.drawable.ic_profile)
        menuProfile.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuProfile.setOnMenuItemClickListener { item: MenuItem ->
            startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
            true
        }

    }

    override fun getNavigationMenuAction(): Int {
        return R.id.actionHome
    }
}
