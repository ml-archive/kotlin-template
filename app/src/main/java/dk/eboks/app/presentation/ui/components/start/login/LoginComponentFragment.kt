package dk.eboks.app.presentation.ui.components.start.login

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import dk.eboks.app.util.guard
import dk.eboks.app.util.isValidCpr
import dk.eboks.app.util.isValidEmail
import kotlinx.android.synthetic.main.fragment_login_component.*
import kotlinx.android.synthetic.main.include_toolnar.*
import timber.log.Timber
import javax.inject.Inject
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide



/**
 * Created by bison on 09-02-2018.
 */
class LoginComponentFragment : BaseFragment(), LoginComponentContract.View {

    private var emailCprIsValid = false
    private var passwordIsValid = false
    var handler = Handler()

    @Inject
    lateinit var presenter : LoginComponentContract.Presenter

    @Inject
    lateinit var formatter: EboksFormatter

    var showGreeting : Boolean = true
    var currentProvider: LoginProvider? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_login_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        getBaseActivity()?.setToolbar(R.drawable.red_navigationbar, Translation.logoncredentials.title, null, {
            hideKeyboard(view)
            (activity as StartActivity).onBackPressed()
        })

        redOptionTv.setOnClickListener {
            hideKeyboard(this.view)
            getBaseActivity()?.openComponentDrawer(ForgotPasswordComponentFragment::class.java)
        }

        arguments?.let { args ->
            showGreeting = args.getBoolean("showGreeting", true)
        }
        setupCprEmailListeners()
        setupPasswordListener()

    }


    private fun hideKeyboard(view: View?) {
        val inputMethodManager = getBaseActivity()?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun setupView(loginProvider: LoginProvider?, user: User?, altLoginProviders: List<LoginProvider>) {
        Timber.e("SetupView called loginProvider = $loginProvider user = $user altProviders = $altLoginProviders")
        loginProvider?.let { provider->
            currentProvider = provider
            setupViewForProvider(user)
            headerTv.visibility = View.GONE
            detailTv.visibility = View.GONE
        }.guard {
            // no provider given setup for cpr/email (mobile access)
            headerTv.visibility = View.VISIBLE
            detailTv.visibility = View.VISIBLE
            cprEmailTil.visibility = View.VISIBLE
            passwordTil.visibility = View.VISIBLE

            if (BuildConfig.DEBUG) {
                debugCreateBtn.visibility = View.VISIBLE
                debugCreateBtn.setOnClickListener {
                    createUser(false)
                }

                debugCreateVerifiedBtn.visibility = View.VISIBLE
                debugCreateVerifiedBtn.setOnClickListener {
                    createUser(true)
                }
            }

        }
        setupAltLoginProviders(altLoginProviders)
    }

    private fun createUser(verified : Boolean)
    {
        val emailOrCpr = cprEmailEt.text?.toString()?.trim() ?: ""
        if (emailOrCpr.isNotBlank()) {
            if(emailOrCpr.contains("@"))
                presenter.createUserAndLogin(email = emailOrCpr, cpr = null, verified = verified)
            else
                presenter.createUserAndLogin(email = null, cpr = emailOrCpr, verified = verified)

            if (showGreeting) {
                fragmentManager.beginTransaction().remove(this@LoginComponentFragment).replace(R.id.containerFl, UserCarouselComponentFragment(), UserCarouselComponentFragment::class.java.simpleName).commit()
                //(activity as StartActivity).replaceFragment(UserCarouselComponentFragment())
                /*
                fragmentManager.findFragmentByTag(WelcomeComponentFragment::class.java.simpleName)?.let { frag->
                    fragmentManager.beginTransaction().remove(frag).commitNowAllowingStateLoss()
                    //fragmentManager.beginTransaction().replace(R.id.containerFl, UserCarouselComponentFragment(), UserCarouselComponentFragment::class.java.simpleName).commitNow()
                }
                */
            } else {
                (activity as StartActivity).onBackPressed()
            }
        }
    }

    override fun addFingerPrintProvider() {
        val li = LayoutInflater.from(context)
        val v = li.inflate(R.layout.viewholder_login_provider, loginProvidersLl, false)
        v.findViewById<ImageView>(R.id.iconIv).setImageResource(R.drawable.ic_fingerprint)
        v.findViewById<TextView>(R.id.nameTv).text = "_Logon with Fingerprint"
        v.setOnClickListener {
            // TODO fingerprinty stuff
        }
        loginProvidersLl.addView(v)
    }

    private fun setupUserView(user : User)
    {
        userLl.visibility = View.VISIBLE
        userNameTv.text = user.name
        var emailCpr = ""
        user.email?.let { emailCpr = it }
        user.cpr?.let { emailCpr = formatter.formatCpr(it) }
        userEmailCprTv.text = emailCpr

        Glide.with(context)
                .load(user.avatarUri)
                .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(30)))
                .into(userAvatarIv)
    }

    private fun setupViewForProvider(user : User?)
    {
        currentProvider?.let { provider->
            when(provider.id)
            {
                "email" -> {
                    user?.let { setupUserView(it) }
                    cprEmailTil.visibility = View.GONE
                    passwordTil.visibility = View.VISIBLE
                }
                "cpr" -> {
                    user?.let { setupUserView(it) }
                    cprEmailTil.visibility = View.VISIBLE
                    cprEmailTil.hint = "_Social security number"
                    passwordTil.visibility = View.VISIBLE
                }
                "nemid" -> {
                    // TODO popup nemid webview
                }
                "idporten" -> {
                    // TODO popup idporten webview
                }
                "bankid_no" -> {
                    // TODO popup bank id no
                }
                "bankid_se" -> {
                    // TODO popup bank id se
                }
                else -> {
                    // show some kind of error? this shouldnt really happen since its handled in the function calling this}
                }
            }
        }
    }

    private fun setupAltLoginProviders(providers: List<LoginProvider>)
    {
        if(providers.isEmpty())
        {
            loginProvidersLl.visibility = View.GONE
            return
        }
        loginProvidersLl.visibility = View.VISIBLE
        val li = LayoutInflater.from(context)

        val listener = View.OnClickListener {
            getBaseActivity()?.openComponentDrawer(ActivationCodeComponentFragment::class.java)

        }
        for(provider in providers)
        {
            val v = li.inflate(R.layout.viewholder_login_provider, loginProvidersLl, false)
            if(provider.icon != -1)
                v.findViewById<ImageView>(R.id.iconIv).setImageResource(provider.icon)
            v.findViewById<TextView>(R.id.nameTv).text = "_Logon with ${provider.name}"
            provider.description?.let { v.findViewById<TextView>(R.id.descTv).text = it }.guard {
                v.findViewById<TextView>(R.id.descTv).visibility = View.GONE
            }
            v.setOnClickListener(listener)
            loginProvidersLl.addView(v)
        }
    }

    private fun setupPasswordListener() {
        passwordEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(password: Editable?) {
                setContinueButton()
                handler?.postDelayed({
                    setErrorMessages()
                }, 1200)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupCprEmailListeners() {
        cprEmailEt.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {

            }
        }

        cprEmailEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                cprEmailTil.error = null
                setContinueButton()
                handler?.postDelayed({
                    setErrorMessages()
                }, 1200)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setErrorMessages() {
        if (emailCprIsValid) {
            cprEmailTil.error = null
        } else  {
            cprEmailTil.error = Translation.logoncredentials.invalidCprorEmail
        }

        if (!passwordIsValid && !passwordEt.text.isNullOrBlank()){
            passwordTil.error = Translation.logoncredentials.invalidPassword
        }else {
            passwordTil.error = null
        }
    }

    private fun setContinueButton() {
        emailCprIsValid = (cprEmailEt.text.isValidEmail() || cprEmailEt.text.isValidCpr())
        passwordIsValid = (!passwordEt.text.isNullOrBlank())


        debugCreateBtn.isEnabled = (emailCprIsValid && passwordIsValid)
        debugCreateVerifiedBtn.isEnabled = (emailCprIsValid && passwordIsValid)
    }

    override fun setupTranslations() {
        headerTv.text = Translation.logoncredentials.topLabel
        detailTv.text = Translation.logoncredentials.topSublabel
        cprEmailTil.hint = Translation.logoncredentials.emailOrSSNHeader
        passwordTil.hint = Translation.logoncredentials.passwordfieldHeader
        redOptionTv.visibility = View.VISIBLE
        redOptionTv.text = Translation.logoncredentials.forgotPasswordButton
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}