package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.util.addAfterTextChangeListener
import dk.eboks.app.util.isValidEmail
import kotlinx.android.synthetic.main.fragment_storeboxconnect_confirm.*
import kotlinx.android.synthetic.main.fragment_storeboxconnect_userinfo.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
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
        Timber.i("showFound")
        supportFragmentManager.beginTransaction().replace(R.id.content, conFrag).commit()

//        AlertDialog.Builder(this)
//                .setMessage(Translation.storeboxlogin.errorAlreadyExistsMessage)
//                .setPositiveButton(Translation.storeboxlogin.signInButton) { dialog, which ->
//                    dialog.dismiss()
//                }
//                .setNegativeButton(Translation.defaultSection.ok) { dialog, which ->
//                    dialog.dismiss()
//                }
//                .create()
//                .show()
    }

    override fun showNotFound() {
        Timber.i("showNotFound")
        AlertDialog.Builder(this)
                .setMessage(Translation.storeboxlogin.errorNoExistingProfileMessage)
                .setPositiveButton(Translation.storeboxlogin.createUserButton) { dialog, which ->
                    dialog.dismiss()
                }
                .setNegativeButton(Translation.storeboxlogin.tryAgainButton) { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    override fun showWrongCode() {
        Timber.i("showWrongCode")
        AlertDialog.Builder(this)
                .setMessage(Translation.activationcode.invalidActivationCode)
                .setNegativeButton(Translation.defaultSection.ok) { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    override fun showSuccess() {
        Timber.i("showSuccess")
        finish()
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
            val isEmail = it?.isValidEmail()?:false
            if(!isEmail) {
                userinfoEmailTil.error = Translation.signup.invalidEmail
            }
            userinfoEmailTil.isErrorEnabled = !isEmail

            userinfoContinueBtn.isEnabled = ( isEmail && userinfoPhoneTil.editText?.text.toString().trim().isNotBlank() )
        }
        userinfoPhoneTil.editText?.addAfterTextChangeListener {
            val isEmail = userinfoEmailTil.editText?.text?.isValidEmail()?:false

            userinfoContinueBtn.isEnabled = ( isEmail && it.toString().trim().isNotBlank())
        }

        userinfoContinueBtn.setOnClickListener {
            presenter?.signIn(userinfoEmailTil.editText?.text.toString().trim(), userinfoPhoneTil.editText?.text.toString().trim())
        }
    }
}

class ConfirmCodeFragment : Fragment() {
    var presenter: ConnectStoreboxContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_storeboxconnect_confirm, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectCodeTil.editText?.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_GO) {
                presenter?.confirm(v.text.toString().trim())
            }
            false
        }
    }
}