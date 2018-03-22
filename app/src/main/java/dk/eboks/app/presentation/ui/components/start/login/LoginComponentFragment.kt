package dk.eboks.app.presentation.ui.components.start.login

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethod
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
import dk.eboks.app.util.guard
import dk.eboks.app.util.isValidCpr
import dk.eboks.app.util.isValidEmail
import kotlinx.android.synthetic.main.fragment_login_component.*
import timber.log.Timber
import javax.inject.Inject
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import kotlinx.android.synthetic.main.include_toolbar.*


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
    var currentUser: User? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_login_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupTopBar()

        arguments?.let { args ->
            showGreeting = args.getBoolean("showGreeting", true)
        }

        continueBtn.setOnClickListener { onContinue() }
    }


    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.ic_red_close)
        mainTb.title = Translation.logoncredentials.title
        mainTb.setNavigationOnClickListener {
            hideKeyboard(view)
            activity.onBackPressed()
        }

        val menuRegist = mainTb.menu.add(Translation.logoncredentials.forgotPasswordButton)
        menuRegist.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menuRegist.setOnMenuItemClickListener { item: MenuItem ->
            hideKeyboard(this.view)
            getBaseActivity()?.openComponentDrawer(ForgotPasswordComponentFragment::class.java)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        setupCprEmailListeners()
        setupPasswordListener()
        presenter.setup()
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }


    private fun hideKeyboard(view: View?) {
        val inputMethodManager = getBaseActivity()?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    private fun onContinue()
    {
        Timber.e("onContinue")
        currentUser?.let { user ->
            currentProvider?.let { provider ->
                presenter.login(user, provider.id)
            }
        }.guard {
            createUser(false)
        }
    }

    override fun proceedToApp() {
        (activity as StartActivity).startMain()
    }


    override fun setupView(loginProvider: LoginProvider?, user: User?, altLoginProviders: List<LoginProvider>) {
        Timber.e("SetupView called loginProvider = $loginProvider user = $user altProviders = $altLoginProviders")
        loginProvider?.let { provider->
            currentProvider = provider
            headerTv.visibility = View.GONE
            detailTv.visibility = View.GONE
            setupViewForProvider(user)
        }.guard {
            // no provider given setup for cpr/email (mobile access)
            headerTv.visibility = View.VISIBLE
            detailTv.visibility = View.VISIBLE
            cprEmailTil.visibility = View.VISIBLE
            passwordTil.visibility = View.VISIBLE
            continueBtn.visibility = View.VISIBLE
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

            getBaseActivity()?.setRootFragment(R.id.containerFl, UserCarouselComponentFragment())
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
        currentUser = user
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
                    cprEmailEt.inputType = InputType.TYPE_CLASS_TEXT and InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    cprEmailTil.visibility = View.GONE
                    passwordTil.visibility = View.VISIBLE
                    continueBtn.visibility = View.VISIBLE
                }
                "cpr" -> {
                    user?.let { setupUserView(it) }
                    cprEmailEt.inputType = InputType.TYPE_CLASS_NUMBER
                    cprEmailTil.visibility = View.VISIBLE
                    cprEmailTil.hint = "_Social security number"
                    passwordTil.visibility = View.VISIBLE
                    continueBtn.visibility = View.VISIBLE
                }
                else -> {
                    getBaseActivity()?.addFragmentOnTop(R.id.containerFl, provider.fragmentClass?.newInstance())
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
        loginProvidersLl.removeAllViews()
        loginProvidersLl.visibility = View.VISIBLE
        val li = LayoutInflater.from(context)

        for(provider in providers)
        {
            val v = li.inflate(R.layout.viewholder_login_provider, loginProvidersLl, false)
            if(provider.icon != -1)
                v.findViewById<ImageView>(R.id.iconIv).setImageResource(provider.icon)

            v.findViewById<TextView>(R.id.nameTv).text = Translation.logoncredentials.logonWithProvider.replace("[provider]",provider.name)
            provider.description?.let { v.findViewById<TextView>(R.id.descTv).text = it }.guard {
                v.findViewById<TextView>(R.id.descTv).visibility = View.GONE
            }
            v.setOnClickListener {
                presenter.switchLoginProvider(provider)
            }
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

        currentProvider?.let { provider->
            if(provider.id == "cpr")
            {
                val enabled = (emailCprIsValid && passwordIsValid)
                continueBtn.isEnabled = enabled
            }
            if(provider.id == "email")
            {
                continueBtn.isEnabled = passwordIsValid
            }
        }.guard {
            val enabled = (emailCprIsValid && passwordIsValid)
            continueBtn.isEnabled = enabled
        }

    }
}