package dk.eboks.app.presentation.ui.components.start.signup

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.isValidEmail
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_signup_name_mail_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by bison on 09-02-2018.
 */
class NameMailComponentFragment : BaseFragment(), SignupComponentContract.NameMailView {

    @Inject
    lateinit var presenter: SignupComponentContract.Presenter

    var nameValid = false
    var emailValid = false
    var handler = Handler()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_signup_name_mail_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        continueBtn.setOnClickListener { onContinueClicked() }
        setupTopBar()
        NStack.translate()
    }

    override fun onResume() {
        super.onResume()
        nameEt.hint = ""
        emailEt.hint = ""
        setupNameListeners()
        setupEmailListeners()
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = Translation.signup.title
        mainTb.setNavigationOnClickListener {
            hideKeyboard(view)
            fragmentManager.popBackStack()
        }
    }

    private fun hideKeyboard(view: View?) {
        val inputMethodManager = getBaseActivity()?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun setupEmailListeners() {
        emailEt.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!emailEt.text.isValidEmail() && !hasFocus) {
                    emailValid = false
                    checkContinueBtn()
                }
            }
        }
        emailEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                handler.removeCallbacksAndMessages(null)
                emailTil.error = null
                emailValid = (s?.isValidEmail() ?: false)
                checkContinueBtn()

                handler?.postDelayed({
                    if (!emailValid) {
                        emailTil.error = Translation.signup.invalidEmail
                    }
                }, 1200)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupNameListeners() {
        nameEt.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            nameTil.error = null
            if (nameEt.text.toString().trim().isNullOrBlank() && !hasFocus) {
                nameTil.error = Translation.signup.invalidName
                nameValid = false
                checkContinueBtn()
            }
        }


        nameEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                nameTil.error = null
                if (nameEt.text.toString().trim().isNullOrBlank()) {
                    nameValid = false
                    checkContinueBtn()
                } else {
                    nameValid = true
                    checkContinueBtn()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkContinueBtn() {
        continueBtn.isEnabled = (emailValid && nameValid)
    }

    override fun showProgress(show: Boolean) {
        content.visibility = if (show) View.GONE else View.VISIBLE
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun onContinueClicked() {
        presenter.confirmMail(emailEt.text.toString().trim(), nameEt.text.toString().trim())
        showProgress(true)
    }

    override fun showSignupMail(exists: Boolean) {
        showProgress(false)
        if (!exists) {
            getBaseActivity()?.addFragmentOnTop(R.id.containerFl, PasswordComponentFragment(), true)
        } else {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(Translation.signup.dialogEmailExistsTitle)
            builder.setMessage(Translation.signup.dialogEmailExistsMsg)
            builder.setNegativeButton(Translation.defaultSection.cancel,{ dialogInterface, i ->
                dialogInterface.dismiss()
            })
            builder.setPositiveButton(Translation.signup.dialogEmailExistsPositiveBtn.toUpperCase(),{ dialogInterface, i ->
                getBaseActivity()?.onBackPressed()
            })
            builder.show()
        }
    }

    override fun showSignupMailError(error: ViewError) {
        showProgress(false)
        //todo something went wrong show error ?
        Timber.e("server error " + error)
    }
}