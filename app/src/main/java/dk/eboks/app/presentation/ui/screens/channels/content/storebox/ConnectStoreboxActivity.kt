package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.util.addAfterTextChangeListener
import kotlinx.android.synthetic.main.fragment_storeboxconnect_userinfo.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by Christian on 5/14/2018.
 * @author   Christian
 * @since    5/14/2018.
 */
class ConnectStoreboxActivity : BaseActivity(), ConnectStoreboxContract.View {

    val infoFrag = UserInfoFragment()
    val conFrag = ConfirmCodeFragment()

    @Inject
    lateinit var presenter: ConnectStoreboxContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_storebox_connect)
        component.inject(this)

        mainTb.title = Translation.storeboxlogin.title
        mainTb.setNavigationIcon(R.drawable.icon_48_close_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            finish()
        }

        presenter.onViewCreated(this, lifecycle)

        infoFrag.presenter = presenter
        conFrag.presenter = presenter

        supportFragmentManager.beginTransaction()
                .add(R.id.content, infoFrag)
                .commit()
    }

    override fun showFound() {
    }

    override fun showNotFound() {
    }

}

class UserInfoFragment : Fragment() {
    var presenter: ConnectStoreboxContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_storeboxconnect_userinfo, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userinfoEmailTil.editText?.addAfterTextChangeListener {
            userinfoContinueBtn.isEnabled = ( it.toString().trim().isNotBlank() && userinfoPhoneTil.editText?.text.toString().trim().isNotBlank() )
        }
        userinfoPhoneTil.editText?.addAfterTextChangeListener {
            userinfoContinueBtn.isEnabled = ( it.toString().trim().isNotBlank() && userinfoEmailTil.editText?.text.toString().trim().isNotBlank() )
        }

        userinfoContinueBtn.setOnClickListener {
            presenter?.signIn(userinfoEmailTil.editText?.text.toString().trim(), userinfoPhoneTil.editText?.text.toString().trim())
        }
    }
}

class ConfirmCodeFragment : Fragment() {
    var presenter: ConnectStoreboxContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_storeboxconnect_userinfo, container, false)
    }
}