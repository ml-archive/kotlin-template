package dk.eboks.app.presentation.ui.channels.screens.content.storebox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.util.addAfterTextChangeListener
import dk.eboks.app.util.isValidEmail
import kotlinx.android.synthetic.main.activity_storebox_connect.*
import kotlinx.android.synthetic.main.fragment_storeboxconnect_confirm.*
import kotlinx.android.synthetic.main.fragment_storeboxconnect_userinfo.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 5/14/2018.
 * @author Christian
 * @since 5/14/2018.
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

        setRootFragment(R.id.contentFragmentFl, infoFrag)

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finishAfterTransition()
            }
        }
    }

    override fun showFound() {
        Timber.e("showFound")
        // fragmentManager.beginTransaction().add(R.id.contentFragmentFl, conFrag).commit()
        addFragmentOnTop(R.id.contentFragmentFl, conFrag)
    }

    override fun showNotFound() {
        Timber.e("showNotFound")
        AlertDialog.Builder(this)
            .setMessage(Translation.storeboxlogin.errorNoExistingProfileMessage)
            .setPositiveButton(Translation.storeboxlogin.createUserButton) { dialog, which ->
                presenter.createStoreboxUser()
                dialog.dismiss()
            }
            .setNegativeButton(Translation.storeboxlogin.tryAgainButton) { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showWrongCode() {
        Timber.e("showWrongCode")
        AlertDialog.Builder(this)
            .setTitle(Translation.storeboxverify.wrongConfirmationCodeHeader)
            .setMessage(Translation.storeboxverify.wrongConfirmationCodeMessage)
            .setNegativeButton(Translation.defaultSection.ok) { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showSuccess() {
        Timber.e("showSuccess")
        finish()
    }

    override fun showProgress(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
        // content.visibility = if(!show) View.VISIBLE else View.GONE
    }
}

class UserInfoFragment : Fragment() {
    var presenter: ConnectStoreboxContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_storeboxconnect_userinfo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userinfoEmailTil.editText?.addAfterTextChangeListener {
            val isEmail = it?.isValidEmail() ?: false
            if (!isEmail) {
                userinfoEmailTil.error = Translation.signup.invalidEmail
            }
            userinfoEmailTil.isErrorEnabled = !isEmail

            userinfoContinueBtn.isEnabled = isEmail
        }
        /*
        userinfoPhoneTil.editText?.addAfterTextChangeListener {
            val isEmail = userinfoEmailTil.editText?.text?.isValidEmail()?:false
            userinfoContinueBtn.isEnabled = ( isEmail && it.toString().trim().isNotBlank())
        }
        */

        userinfoContinueBtn.setOnClickListener {
            presenter?.signIn(
                userinfoEmailTil.editText?.text.toString().trim(),
                userinfoPhoneTil.editText?.text.toString().trim()
            )
        }
    }
}

class ConfirmCodeFragment : Fragment() {
    var presenter: ConnectStoreboxContract.Presenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_storeboxconnect_confirm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectCodeTil.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                presenter?.confirm(v.text.toString().trim())
            }
            false
        }
    }
}