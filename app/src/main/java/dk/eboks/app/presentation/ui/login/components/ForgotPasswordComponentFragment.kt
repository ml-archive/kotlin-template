package dk.eboks.app.presentation.ui.login.components

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.SheetComponentActivity
import dk.eboks.app.util.isValidEmail
import kotlinx.android.synthetic.main.fragment_forgot_password_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ForgotPasswordComponentFragment : BaseFragment(), dk.eboks.app.keychain.presentation.components.ForgotPasswordComponentContract.View {

    @Inject
    lateinit var presenter: dk.eboks.app.keychain.presentation.components.ForgotPasswordComponentContract.Presenter

    var handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        headerTv.requestFocus()
        cancelTv.setOnClickListener {
            (activity as SheetComponentActivity).onBackPressed()
        }

        emailEt.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
                if (validate()) {
                    presenter.resetPassword(emailEt.text.toString().trim())
                    resetPasswordBtn.isEnabled = false
                }
                handled = true
            }
            handled
        }
        resetPasswordBtn.setOnClickListener {
            if (validate()) {
                presenter.resetPassword(emailEt.text.toString().trim())
                resetPasswordBtn.isEnabled = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupEmailListener()
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    private fun validate(): Boolean {
        val isGood = emailEt.text?.isValidEmail() ?: false
        if (isGood) {
            emailTil.error = null
        } else {
            emailTil.error = Translation.forgotpassword.invalidEmail
        }
        return isGood
    }

    private fun setupEmailListener() {
        emailEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                handler.removeCallbacksAndMessages(null)
                emailTil.error = null

                handler.postDelayed({
                    resetPasswordBtn.isEnabled = validate()
                }, 1200)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun showSuccess() {
        resetPasswordBtn.isEnabled = validate()
        (activity as SheetComponentActivity).replaceFragment(ForgotPasswordDoneComponentFragment())
    }

    override fun showError(viewError: ViewError) {
        showErrorDialog(viewError)
        resetPasswordBtn.isEnabled = validate()
    }
}