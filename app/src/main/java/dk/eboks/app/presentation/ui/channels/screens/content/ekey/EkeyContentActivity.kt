package dk.eboks.app.presentation.ui.channels.screens.content.ekey

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentFragment
import javax.inject.Inject

class EkeyContentActivity : BaseActivity(), EkeyContentContract.View {
    @Inject lateinit var presenter: EkeyContentContract.Presenter

    private var keys: MutableList<BaseEkey>? = null
    var shouldRefresh = false
    var pin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_ekey_content)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        if (intent.getBooleanExtra("showPin",true)){
            addFragmentOnTop(R.id.content,EkeyPinComponentFragment(),false)
//            setRootFragment(R.id.content, EkeyPinComponentFragment())
        } else {
            setRootFragment(R.id.content, EkeyComponentFragment())
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (!isDestroyed)
                finish()
        }
    }

    fun setVault(keyList: MutableList<BaseEkey>) {
        keys = keyList
    }

    fun getVault(): MutableList<BaseEkey>? {
        return keys
    }
}
