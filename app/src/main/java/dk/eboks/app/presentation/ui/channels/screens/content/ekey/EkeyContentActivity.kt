package dk.eboks.app.presentation.ui.channels.screens.content.ekey

import android.app.FragmentManager
import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentFragment
import timber.log.Timber
import javax.inject.Inject

class EkeyContentActivity : BaseActivity(), EkeyContentContract.View {
    @Inject
    lateinit var presenter: EkeyContentContract.Presenter

    private var keys: MutableList<BaseEkey>? = null
    var shouldRefresh = false
    var pin: String? = null
    var channel: Channel? = null
    var isDestroyable: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_ekey_content)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        channel = intent.getSerializableExtra("channel") as Channel?
        if (presenter.getMasterKey() == null) {
            addFragmentOnTop(R.id.content, EkeyPinComponentFragment(), false)
        } else {
            setRootFragment(R.id.content, EkeyComponentFragment())
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed && isDestroyable) {
                    Timber.d("BACKSTACK DESTROY")
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (!isDestroyed && isDestroyable)
                finish()
        }
    }

    fun clearBackStackAndSetToPin() {
        isDestroyable = false
        supportFragmentManager.popBackStack(
                backStackRootTag,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        addFragmentOnTop(R.id.content, EkeyPinComponentFragment(), false)
        isDestroyable = true
        Timber.d("BACKSTACK ADD")
    }

    fun setVault(keyList: MutableList<BaseEkey>) {
        keys = keyList
    }

    fun getVault(): MutableList<BaseEkey>? {
        return keys
    }
}
