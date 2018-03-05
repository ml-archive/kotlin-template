package dk.eboks.app.presentation.ui.components.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import kotlinx.android.synthetic.main.fragment_signup_password_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class PasswordComponentFragment : BaseFragment(), SignupComponentContract.PasswordView {

    @Inject
    lateinit var presenter: SignupComponentContract.Presenter

    var passwordValid = false
    var repeatPasswordValid = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_signup_password_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        continueBtn.setOnClickListener { onContinueClicked() }
        getBaseActivity()?.setToolbar(R.drawable.ic_red_back, Translation.signup.title, null, {
            fragmentManager.popBackStack()
        })
        // password listeners
        passwordEt.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (passwordEt.text.toString().trim().isNullOrBlank() && !hasFocus) {
                    passwordTil.error = Translation.signup.invalidPassword
                    passwordValid = false
                }
                comparePasswords()
            }
        }

        passwordEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(password: Editable?) {
                passwordTil.error = null
                passwordValid = (isValidPassword(password.toString()))
                comparePasswords()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // report password listeners

        repeatPasswordEt.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                comparePasswords()
            }
        }

        repeatPasswordEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(repeatPassword: Editable?) {
                repeatPasswordTil.error = null
                comparePasswords()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun comparePasswords() {
        if (passwordEt.text.isNullOrBlank()) {
            if (repeatPasswordEt.text.isNullOrBlank()) {
                passwordTil.error = null
                repeatPasswordValid = false
            } else {
                passwordTil.error = Translation.signup.invalidPassword
            }
            passwordValid = false
        }
        if (passwordEt.text.toString().trim() == repeatPasswordEt.text.toString().trim()) {
            repeatPasswordValid = true
            repeatPasswordTil.error = null
        } else {
            repeatPasswordValid = false
            if (repeatPasswordEt.text.isNullOrBlank()) {
                repeatPasswordTil.error = null
            } else {
                repeatPasswordTil.error = Translation.signup.invalidPasswordMatch
            }
        }
        continueBtn.isEnabled = (passwordValid && repeatPasswordValid)
    }

    fun isValidPassword(password: CharSequence): Boolean {
        if (password.isNotBlank()) {
            return true
        }
        return false
    }

    override fun setupTranslations() {
        headerTv.text = Translation.signup.passwordHeader
        detailTv.text = Translation.signup.passwordDetail
        continueBtn.text = Translation.signup.continueButton
        passwordTil.hint = Translation.signup.passwordHint
        repeatPasswordTil.hint = Translation.signup.repeatPasswordHint
    }

    override fun showError() {

    }

    override fun showProgress(show: Boolean) {
        content.visibility = if (show) View.GONE else View.VISIBLE
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun onContinueClicked() {
        //(activity as StartActivity).showLogo(false)
        showProgress(true)
        content.postDelayed({
            showProgress(false)
            (activity as StartActivity).replaceFragment(SignupVerificationComponentFragment())
        }, 1000)
    }
}