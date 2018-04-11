package dk.eboks.app.presentation.ui.components.profile.drawer

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.isValidCpr
import kotlinx.android.synthetic.main.fragment_profile_enable_fingerprint_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FingerPrintComponentFragment : BaseFragment(), FingerPrintComponentContract.View {

    @Inject
    lateinit var presenter : FingerPrintComponentContract.Presenter

    var cprIsValid = false
    var passwordIsValid = false
    var handler = Handler()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_profile_enable_fingerprint_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setup()

    }

    private fun setup() {
        setEnableStateContinueButton()
        setupPasswordListener()
        setupCprListeners()
        enableBtn.setOnClickListener {
            Toast.makeText(context, "_enable btn clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onResume() {
        setupPasswordListener()
        setupCprListeners()
        super.onResume()
    }

    private fun setEnableStateContinueButton() {
        cprIsValid = (socialSecurityEt.text.isValidCpr())
        passwordIsValid = (!passwordEt.text.isNullOrBlank())

        val enabled = (cprIsValid && passwordIsValid)
        enableBtn.isEnabled = enabled
    }

    private fun setupPasswordListener() {
        passwordEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(password: Editable?) {
                setEnableStateContinueButton()
                handler?.postDelayed({
                    setErrorMessages()
                }, 1200)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupCprListeners() {
        socialSecurityEt.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {

            }
        }

        socialSecurityEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                socialSecurityTil.error = null
                setEnableStateContinueButton()
                handler?.postDelayed({
                    setErrorMessages()
                }, 1200)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setErrorMessages() {
        if (cprIsValid) {
            socialSecurityTil.error = null
        } else  {
            socialSecurityTil.error = Translation.logoncredentials.invalidSSN
        }

        if (!passwordIsValid && !passwordEt.text.isNullOrBlank()){
            passwordTil.error = Translation.logoncredentials.invalidPassword
        }else {
            passwordTil.error = null
        }
    }

}