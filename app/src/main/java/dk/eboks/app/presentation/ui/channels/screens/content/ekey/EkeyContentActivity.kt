package dk.eboks.app.presentation.ui.channels.screens.content.ekey

import android.app.FragmentManager
import android.os.Bundle
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentFragment
import timber.log.Timber
import javax.inject.Inject

class EkeyContentActivity : BaseActivity(), EkeyContentContract.View {
    @Inject
    lateinit var presenter: EkeyContentContract.Presenter

    private var keys: ArrayList<BaseEkey>? = null
    var channel: Channel? = null
    var isDestroyable: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_ekey_content)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        channel = intent.getParcelableExtra("channel")
        showLoading(true)
        presenter.getData()
    }

    override fun showKeys(keys: ArrayList<BaseEkey>) {
        this.keys = keys
        showLoading(false)
        setRootFragment(R.id.content, EkeyComponentFragment.newInstance(keys))
    }

    override fun showPinView(isCreate: Boolean) {
        showLoading(false)
        clearBackStackAndSetToPin(isCreate)
    }

    override fun onGetMasterkeyError(viewError: ViewError) {
        Timber.d("error: ${viewError.message}")

        showErrorDialog(viewError)
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            content.visibility = View.GONE
            ekeyActivityProgressBar.visibility = View.VISIBLE
        } else {
            content.visibility = View.VISIBLE
            ekeyActivityProgressBar.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (!isDestroyed && isDestroyable)
                finish()
        }
    }

    fun clearBackStackAndSetToPin(isCreate: Boolean) {
        isDestroyable = false
        supportFragmentManager.popBackStack(
            backStackRootTag,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

        addFragmentOnTop(R.id.content, EkeyPinComponentFragment.newInstance(isCreate), false)
        isDestroyable = true
        Timber.d("BACKSTACK ADD")
    }

    fun getVault(): MutableList<BaseEkey>? {
        return keys
    }

    fun refreshClearAndShowMain() {
        supportFragmentManager.popBackStack(
            backStackRootTag,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        presenter.getData()
    }

    fun refreshAndShowMain() {
        presenter.getData()
    }

    fun setPin(pin: String) {
        presenter.pin = pin
    }

    override fun onResume() {
        super.onResume()
        if (shouldFinish) {
            shouldFinish = false
            finish()
        }
    }

    companion object {
        var shouldFinish: Boolean = false
    }
}
