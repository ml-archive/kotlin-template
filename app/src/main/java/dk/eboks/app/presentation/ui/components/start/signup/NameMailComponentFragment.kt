package dk.eboks.app.presentation.ui.components.start.signup

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_signup_name_mail_component.*
import javax.inject.Inject
import dk.eboks.app.domain.models.Translation
import android.util.Patterns
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.Spanned
import android.text.TextPaint
import android.graphics.Color
import android.os.Handler
import android.text.style.ClickableSpan
import android.text.SpannableString
import android.view.inputmethod.InputMethodManager
import dk.eboks.app.util.isValidEmail
import kotlinx.android.synthetic.main.include_toolbar.*


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
        setupTermsText()
        setupTopBar()
    }

    override fun onResume() {
        super.onResume()
        setupNameListeners()
        setupEmailListeners()
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.red_navigationbar)
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
                    emailTil.error = Translation.signup.invalidEmail
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
                },1200)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupNameListeners() {
        nameEt.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (nameEt.text.toString().trim().isNullOrBlank() && !hasFocus) {
                    nameTil.error = Translation.signup.invalidName
                    nameValid = false
                    checkContinueBtn()
                }
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

    private fun setupTermsText() {
        var termsText = Translation.signup.termsClickAbleText
        var startIndex = termsText.indexOf("[", 0, false)
        var endIndex = termsText.indexOf("]", 0, false) - 1
        termsText = termsText.replace("[", "", false)
        termsText = termsText.replace("]", "", false)

        val ss = SpannableString(termsText)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                getBaseActivity()?.addFragmentOnTop(R.id.containerFl, TermsComponentFragment(), true)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        showTermsTv.text = ss
        showTermsTv.movementMethod = LinkMovementMethod.getInstance()
        showTermsTv.highlightColor = Color.TRANSPARENT


    }

    private fun checkContinueBtn() {
        continueBtn.isEnabled = (emailValid && nameValid)
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun setupTranslations() {
        headerTv.text = Translation.signup.nameEmailHeader
        detailTv.text = Translation.signup.nameEmailDetail
        continueBtn.text = Translation.signup.continueButton
        nameTil.hint = Translation.signup.nameHint
        emailTil.hint = Translation.signup.emailHint
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
            getBaseActivity()?.addFragmentOnTop(R.id.containerFl, PasswordComponentFragment(), true)
        }, 1000)
    }

}