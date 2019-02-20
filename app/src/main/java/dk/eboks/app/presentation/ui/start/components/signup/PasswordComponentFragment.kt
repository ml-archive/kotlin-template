package dk.eboks.app.presentation.ui.start.components.signup

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import dk.eboks.AppPatterns
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.dpToPx
import dk.eboks.app.util.visible
import kotlinx.android.synthetic.main.fragment_signup_password_component.*
import kotlinx.android.synthetic.main.fragment_signup_password_component.view.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class PasswordComponentFragment : BaseFragment(), SignupComponentContract.PasswordView {

    @Inject lateinit var presenter: SignupComponentContract.Presenter

    private var passwordValid = false
    private var repeatPasswordValid = false
    var handler = Handler()

    private val passwordRegex by lazy {
        Regex(AppPatterns.PasswordRegex)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return inflater.inflate(R.layout.fragment_signup_password_component, container, false)
            .apply {
                viewTreeObserver.addOnGlobalLayoutListener {
                    val r = Rect()
                    getWindowVisibleDisplayFrame(r)
                    val heightDiff = rootView.height - (r.bottom - r.top)
                    if (heightDiff > dpToPx(100)) {
                        this.scrollView.fullScroll(View.FOCUS_DOWN)
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        continueBtn.setOnClickListener { onContinueClicked() }
        setupTopBar()
    }

    override fun onResume() {
        super.onResume()
        setupPasswordListeners()
        setupRepeatPasswordListener()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onPause()
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = Translation.signup.title
        mainTb.setBackgroundColor(
            ContextCompat.getColor(
                context ?: return,
                R.color.backgroundColor
            )
        )
        mainTb.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    private fun setupRepeatPasswordListener() {
        repeatPasswordEt.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                comparePasswords()
                setErrorMessages()
            }

        repeatPasswordEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(repeatPassword: Editable?) {
                handler.removeCallbacksAndMessages(null)
                repeatPasswordTil.error = null
                comparePasswords()
                handler.postDelayed({
                    setErrorMessages()
                }, 1200)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setErrorMessages() {
        if (!passwordEt.text.isNullOrBlank() && !passwordValid) {
            passwordTil.error = Translation.signup.invalidPassword
        } else {
            passwordTil.error = null
        }

        if (!repeatPasswordEt.text.isNullOrBlank() && !repeatPasswordValid) {
            repeatPasswordTil.error = Translation.signup.invalidPasswordMatch
        } else {
            repeatPasswordTil.error = null
        }
    }

    private fun setupPasswordListeners() {
        passwordEt.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                comparePasswords()
                setErrorMessages()
            }
        }

        passwordEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(password: Editable?) {
                handler.removeCallbacksAndMessages(null)
                passwordTil.error = null
                comparePasswords()
                handler.postDelayed({
                    setErrorMessages()
                }, 1200)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun comparePasswords() {
        passwordValid = isValidPassword(passwordEt.text.toString().trim())
        repeatPasswordValid =
            passwordEt.text.toString().trim() == repeatPasswordEt.text.toString().trim()
        continueBtn.isEnabled = passwordValid && repeatPasswordValid
    }

    private fun isValidPassword(password: CharSequence): Boolean {
        return passwordRegex.matches(password)
    }

    override fun showProgress(show: Boolean) {
        content.visible = !show
        progress.visible = show
    }

    private fun onContinueClicked() {
        // (activity as StartActivity).showLogo(false)
        presenter.setPassword(passwordEt.text.toString().trim())
        showProgress(true)
        content.postDelayed({
            showProgress(false)
            getBaseActivity()?.addFragmentOnTop(
                R.id.containerFl,
                SignupVerificationComponentFragment(),
                true
            )
        }, 1000)
    }
}