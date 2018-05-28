package dk.eboks.app.presentation.ui.components.start.login

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.dialogs.CustomFingerprintDialog
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import dk.eboks.app.util.KeyboardUtils
import dk.eboks.app.util.guard
import dk.eboks.app.util.isValidCpr
import dk.eboks.app.util.isValidEmail
import dk.nodes.arch.domain.executor.SignalDispatcher.signal
import dk.nodes.locksmith.core.models.FingerprintDialogEvent.*
import kotlinx.android.synthetic.main.fragment_login_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by bison on 09-02-2018.
 */
class LoginComponentFragment : BaseFragment(), LoginComponentContract.View {

    private var emailCprIsValid = false
    private var passwordIsValid = false
    var handler = Handler()

    @Inject
    lateinit var presenter: LoginComponentContract.Presenter

    @Inject
    lateinit var formatter: EboksFormatter

    var showGreeting: Boolean = true
    var currentProvider: LoginProvider? = null
    var currentUser: User? = null

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater?.inflate(R.layout.fragment_login_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupTopBar()
        presenter.setup()

        arguments?.let { args ->
            showGreeting = args.getBoolean("showGreeting", true)
        }

        continueBtn.setOnClickListener {
            onContinue()
        }
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
        Timber.d("onResume")
        setupCprEmailListeners()
        setupPasswordListener()

    }

    override fun onPause() {
        Timber.d("onPause")
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }


    private fun hideKeyboard(view: View?) {
        val inputMethodManager = getBaseActivity()?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun onContinue() {
        Timber.i("onContinue with ${currentUser?.name}")
        currentUser?.let { user ->
            currentProvider?.let { provider ->
                presenter.updateLoginState(
                        user,
                        provider.id,
                        passwordEt.text.toString().trim(),
                        activationCode = null
                )
                presenter.login()
                continuePb.visibility = View.VISIBLE
                continueBtn.isEnabled = false
            }
        }.guard {
            createUser(false)
//            getBaseActivity()?.setRootFragment(R.id.containerFl, UserCarouselComponentFragment())
        }
    }

    override fun proceedToApp() {
        //Timber.v("Signal - login_condition")
        signal("login_condition") // allow the eAuth2 authenticator to continue

        (activity as StartActivity).startMain()
    }


    override fun setupView(loginProvider: LoginProvider?, user: User?, altLoginProviders: List<LoginProvider>) {
        Timber.i("SetupView called loginProvider = $loginProvider user = $user altProviders = $altLoginProviders")
        continuePb.visibility = View.INVISIBLE

        loginProvider?.let { provider ->
            currentProvider = provider
            currentUser = user
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
            userLl.visibility = View.GONE
        }
        setupAltLoginProviders(altLoginProviders)

        user?.let {
            if (BuildConfig.DEBUG && "3110276111" == it.cpr) {
                cprEmailEt.setText("3110276111")
                passwordEt.setText("147258369")
                setContinueButton()
            }
        }
    }

    private fun createUser(verified: Boolean) {
        val emailOrCpr = cprEmailEt.text?.toString()?.trim() ?: ""
        if (emailOrCpr.isNotBlank()) {
            if (emailOrCpr.contains("@")) {
                presenter.createUserAndLogin(email = emailOrCpr, cpr = null, verified = verified)
            } else {
                presenter.createUserAndLogin(email = null, cpr = emailOrCpr, verified = verified)
            }

            getBaseActivity()?.setRootFragment(R.id.containerFl, UserCarouselComponentFragment())
        }
    }

    override fun showActivationCodeDialog() {
        continuePb.visibility = View.INVISIBLE
        continueBtn.isEnabled = true

        getBaseActivity()?.openComponentDrawer(ActivationCodeComponentFragment::class.java)
    }

    override fun showError(viewError: ViewError) {
        continuePb.visibility = View.INVISIBLE
        continueBtn.isEnabled = true

        showErrorDialog(viewError)
    }

    override fun addFingerPrintProvider() {
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val li = LayoutInflater.from(context)
            val v = li.inflate(R.layout.viewholder_login_provider, loginProvidersLl, false)
            v.findViewById<ImageView>(R.id.iconIv).setImageResource(R.drawable.ic_fingerprint)
            v.findViewById<TextView>(R.id.nameTv).text = Translation.logoncredentials.logonWithProvider.replace(
                    "[provider]",
                    Translation.profile.fingerprint
            )
            v.findViewById<TextView>(R.id.descTv).visibility = View.GONE

            v.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showFingerprintDialog()
                }
            }

            loginProvidersLl.addView(v)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showFingerprintDialog() {
        val customFingerprintDialog = CustomFingerprintDialog(context)

        customFingerprintDialog.setOnFingerprintDialogEventListener {
            customFingerprintDialog.dismiss()

            when (it) {
                CANCEL -> {
                    // Do nothing?
                }
                SUCCESS -> {
                    currentUser?.let { user ->
                        currentProvider?.let { provider ->
                            // TODO add credentials
                            presenter.updateLoginState(user, provider.id, "todo", "todo")
                            presenter.login()
                        }
                    }
                }
                ERROR_CIPHER,
                ERROR_ENROLLMENT,
                ERROR_HARDWARE,
                ERROR_SECURE,
                ERROR -> {
                    showErrorDialog(
                            ViewError(
                                    Translation.error.genericTitle,
                                    Translation.androidfingerprint.errorGeneric,
                                    true,
                                    false
                            )
                    )
                }
            }
        }

        customFingerprintDialog.onUsePasswordBtnListener = {
            // Todo add use password section
        }

        customFingerprintDialog.show()
    }

    private fun setupUserView(user: User?) {

        continueBtn.visibility = View.GONE
        passwordTil.visibility = View.VISIBLE

        KeyboardUtils.addKeyboardToggleListener(activity, KeyboardUtils.SoftKeyboardToggleListener {
            if (it) {
                loginProvidersLl.visibility = View.GONE
                continueBtn.visibility = View.GONE
            } else {
                loginProvidersLl.visibility = View.VISIBLE
                continueBtn.visibility = View.VISIBLE
            }
        })

        // setting profile view
        userNameTv.text = user?.name
        userEmailCprTv.text = user?.emails?.firstOrNull()?.value

        var options = RequestOptions()
        options.error(R.drawable.ic_profile_placeholder)
        options.placeholder(R.drawable.ic_profile_placeholder)
        options.transforms(CenterCrop(), RoundedCorners(30))
        Glide.with(context)
                .load(user?.avatarUri)
                .apply(options)
                .into(userAvatarIv)

    }

    private fun setupViewForProvider(user: User?) {
        currentProvider?.let { provider ->
            when (provider.id) {
                "email" -> {
                    setupUserView(user)
                    cprEmailEt.inputType = InputType.TYPE_CLASS_TEXT and InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    cprEmailTil.visibility = View.GONE
                    userLl.visibility = View.VISIBLE

                }
                "cpr" -> {
                    user?.let { setupUserView(it) }
                    cprEmailEt.inputType = InputType.TYPE_CLASS_NUMBER
                    userLl.visibility = View.VISIBLE
                    cprEmailTil.visibility = View.VISIBLE
                    cprEmailTil.hint = Translation.logoncredentials.ssnHeader
                }
                else -> {
                    getBaseActivity()?.addFragmentOnTop(
                            R.id.containerFl,
                            provider.fragmentClass?.newInstance()
                    )
                }
            }
        }
    }

    private fun setupAltLoginProviders(providers: List<LoginProvider>) {

        loginProvidersLl.removeAllViews()
        loginProvidersLl.visibility = View.VISIBLE
        if (currentUser?.hasFingerprint ?: false) {
            addFingerPrintProvider()
        }
        val li = LayoutInflater.from(context)

        for (provider in providers) {
            val v = li.inflate(R.layout.viewholder_login_provider, loginProvidersLl, false)

            // setting icon
            if (provider.icon != -1) {
                v.findViewById<ImageView>(R.id.iconIv).setImageResource(provider.icon)
            }

            //header
            v.findViewById<TextView>(R.id.nameTv).text = Translation.logoncredentials.logonWithProvider.replace(
                    "[provider]",
                    provider.name
            )

            //description
            provider.description?.let {
                v.findViewById<TextView>(R.id.descTv).text = it
            }.guard {
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
        cprEmailEt.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus -> }

        cprEmailEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                cprEmailTil.error = null
                setContinueButton()
                handler.postDelayed({
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
        } else {
            cprEmailTil.error = Translation.logoncredentials.invalidCprorEmail
        }

        if (!passwordIsValid && !passwordEt.text.isNullOrBlank()) {
            passwordTil.error = Translation.logoncredentials.invalidPassword
        } else {
            passwordTil.error = null
        }
    }

    private fun setContinueButton() {

        emailCprIsValid = (cprEmailEt.text.isValidEmail() || cprEmailEt.text.isValidCpr())
        passwordIsValid = (!passwordEt.text.isNullOrBlank())

        currentProvider?.let { provider ->
            if (provider.id == "cpr") {
                val enabled = (emailCprIsValid && passwordIsValid)
                continueBtn.isEnabled = enabled
            }
            if (provider.id == "email") {
                continueBtn.isEnabled = passwordIsValid
            }
        }.guard {
            val enabled = (emailCprIsValid && passwordIsValid)
            continueBtn.isEnabled = enabled
        }

    }

//    private fun doUserLogin() {
//        currentUser?.let { user ->
//            currentProvider?.let { provider ->
//                presenter.updateLoginState(user, provider.id, "todo")
//                presenter.login() // TODO add credentials
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        KeyboardUtils.removeAllKeyboardToggleListeners()
    }
}

