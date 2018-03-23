package dk.eboks.app.presentation.ui.screens.debug.user

import android.os.Bundle
import android.widget.ArrayAdapter
import dk.eboks.app.R
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.start.welcome.WelcomeComponentFragment
import dk.eboks.app.util.guard
import kotlinx.android.synthetic.main.activity_debug_user.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

class DebugUserActivity : BaseActivity(), DebugUserContract.View {
    @Inject lateinit var presenter: DebugUserContract.Presenter

    var currentUser : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_debug_user)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
        presenter.setup()
        focusThiefTv.requestFocus()
        createBtn.setOnClickListener {
            currentUser?.let { saveUser(it) }.guard {
                createUser()
            }
        }
    }

    private fun setupTopBar()
    {
        mainTb.title = "Create User"
        mainTb.subtitle = "DEBUG"
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun createUser()
    {
        val provider = loginProviderSpr.selectedItem as LoginProvider
        val name = nameEt.text.toString().trim()
        var email : String? = emailEt.text.toString().trim()
        if(email.isNullOrBlank())
            email = null
        var cpr : String? = cprEt.text.toString().trim()
        if(cpr.isNullOrBlank())
            cpr = null
        val verified = verifiedSw.isChecked
        val fingerprint = fingerPrintSw.isChecked
        presenter.createUser(provider, name, email, cpr, verified, fingerprint)
    }

    private fun saveUser(user : User)
    {
        user.lastLoginProvider = (loginProviderSpr.selectedItem as LoginProvider).id
        user.name = nameEt.text.toString().trim()
        user.email = emailEt.text.toString().trim()

        if(user.email.isNullOrBlank())
            user.email = null
        user.cpr = cprEt.text.toString().trim()
        if(user.cpr.isNullOrBlank())
            user.cpr = null
        user.verified = verifiedSw.isChecked
        user.hasFingerprint = fingerPrintSw.isChecked
        presenter.saveUser(user)
    }

    override fun showUser(user: User) {
        currentUser = user
        for(i in 0 until loginProviderSpr.adapter.count)
        {
            user.lastLoginProvider?.let {
                if((loginProviderSpr.adapter.getItem(i) as LoginProvider).id == it) {
                    loginProviderSpr.setSelection(i)
                    return@let
                }
            }
        }
        nameEt.setText(user.name)
        user.email?.let { emailEt.setText(it) }
        user.cpr?.let { cprEt.setText(it) }
        verifiedSw.isChecked = user.verified
        fingerPrintSw.isChecked = user.hasFingerprint
        currentUser?.let {
            createBtn.text = "Save"
        }
    }

    override fun close(gotoUsers : Boolean) {
        WelcomeComponentFragment.shouldGotoUsersOnResume = gotoUsers
        finish()
    }

    override fun showLoginProviderSpinner(providers : List<LoginProvider>)
    {
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter : ArrayAdapter<LoginProvider> = ArrayAdapter(this, android.R.layout.simple_spinner_item, providers)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        loginProviderSpr.adapter = adapter
    }
}
