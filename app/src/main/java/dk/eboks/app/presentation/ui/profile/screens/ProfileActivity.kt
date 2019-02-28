package dk.eboks.app.presentation.ui.profile.screens

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentFragment
import javax.inject.Inject

class ProfileActivity : BaseActivity(), dk.eboks.app.profile.ProfileContract.View {
    @Inject
    lateinit var presenter: dk.eboks.app.profile.ProfileContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    this.finishAfterTransition()
            }
        }
        val frag = ProfileInfoComponentFragment()
        setRootFragment(R.id.profileActivityContainerFragment, frag)
    }

    /*
    override fun finish() {
        Timber.e("Running finish")
        super.finish()
        overridePendingTransition(0, R.anim.slide_down)
    }

    override fun finishAfterTransition() {
        super.finishAfterTransition()
        overridePendingTransition(0, R.anim.slide_down)
    }

    override fun supportFinishAfterTransition() {
        super.supportFinishAfterTransition()
        overridePendingTransition(0, R.anim.slide_down)
    }
    */
}
