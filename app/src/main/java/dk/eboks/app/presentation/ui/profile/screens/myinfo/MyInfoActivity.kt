package dk.eboks.app.presentation.ui.profile.screens.myinfo

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.profile.components.myinfo.MyInfoComponentFragment
import dk.eboks.app.profile.presentation.ui.screens.MyInfoContract
import javax.inject.Inject

class MyInfoActivity : BaseActivity(), MyInfoContract.View {
    @Inject lateinit var presenter: MyInfoContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_profile_myinfo)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        val channel = intent?.extras?.getParcelable<Channel>("channel")
        val fragment = MyInfoComponentFragment()
        val args = Bundle()
        args.putParcelable("channel", channel)
        fragment.arguments = args
        setRootFragment(R.id.contentFl, fragment)

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
    }
}
