package dk.eboks.app.presentation.ui.components.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import kotlinx.android.synthetic.main.fragment_signup_name_mail_component.*
import javax.inject.Inject
import dk.eboks.app.domain.models.Translation
import android.util.Patterns
import android.text.TextUtils
import android.view.FocusFinder
import dk.eboks.app.R.id.nameTil
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.text.Spanned
import android.text.TextPaint
import android.content.Intent
import android.graphics.Color
import android.text.style.ClickableSpan
import android.text.SpannableString




/**
 * Created by bison on 09-02-2018.
 */
class NameMailComponentFragment : BaseFragment(), SignupComponentContract.NameMailView {

    @Inject
    lateinit var presenter: SignupComponentContract.Presenter

    var nameValid = false
    var emailValid = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_signup_name_mail_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        continueBtn.setOnClickListener { onContinueClicked() }
//        ----------------------

        val ss = SpannableString("Android is a Software stack")
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                (activity as StartActivity).replaceFragment(TermsComponentFragment())
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableSpan, 22, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

//        val textView = findViewById(R.id.hello) as TextView
        showTermsTv.text = ss
        showTermsTv.movementMethod = LinkMovementMethod.getInstance()
        showTermsTv.highlightColor = Color.TRANSPARENT


//        showTermsTv.setOnClickListener {
//            //(activity as StartActivity).showLogo(false)
//            (activity as StartActivity).replaceFragment(TermsComponentFragment())
//        }
        //        ----------------------
        getBaseActivity()?.setToolbar(R.drawable.ic_red_back, Translation.signup.title, null, {
            fragmentManager.popBackStack()
        })
        //-------------------

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
        //---------------------
        emailEt.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (emailEt.text.toString().trim().isNullOrBlank() && !hasFocus) {
                    emailTil.error = Translation.signup.invalidEmail
                    emailValid = false
                    checkContinueBtn()
                }
            }
        })
        emailEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                emailTil.error = null
                if (isValidEmail(s.toString())) {
                    emailValid = true
                    checkContinueBtn()

                } else {
                    emailValid = false
                    checkContinueBtn()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        //---------------------
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
            (activity as StartActivity).replaceFragment(PasswordComponentFragment())
        }, 1000)
    }
}