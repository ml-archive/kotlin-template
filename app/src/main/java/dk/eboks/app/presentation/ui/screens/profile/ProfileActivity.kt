package dk.eboks.app.presentation.ui.screens.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.profile.edit.ProfileInfoComponentFragment
import timber.log.Timber
import javax.inject.Inject

class ProfileActivity : BaseActivity(), ProfileContract.View {
    @Inject
    lateinit var presenter: ProfileContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
        setRootFragment(R.id.profileActivityContainerFragment, ProfileInfoComponentFragment())

    }

    fun showMyInformationFragment() {
        //TODO setup fragment¬
    }

    fun showTerms() {
        //TODO setup fragment
    }

    fun showHelp() {
        //TODO setup fragment
    }

    fun showPrivacy() {
        //TODO setup fragment
    }

}
