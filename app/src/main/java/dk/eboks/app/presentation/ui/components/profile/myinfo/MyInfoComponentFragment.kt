package dk.eboks.app.presentation.ui.components.profile.myinfo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.profile.drawer.EmailVerificationComponentFragment
import dk.eboks.app.presentation.ui.components.profile.drawer.PhoneVerificationComponentFragment
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.util.OnLanguageChangedListener
import kotlinx.android.synthetic.main.fragment_profile_myinformation_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MyInfoComponentFragment : BaseFragment(), MyInfoComponentContract.View, OnLanguageChangedListener, TextWatcher {
    @Inject
    lateinit var presenter: MyInfoComponentContract.Presenter
    var menuSave : MenuItem? = null

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater?.inflate(R.layout.fragment_profile_myinformation_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
        onLanguageChanged(NStack.language)
        presenter.setup()
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            activity.onBackPressed()
        }

        menuSave = mainTb.menu.add(Translation.defaultSection.save)
        menuSave?.isEnabled = false
        menuSave?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menuSave?.setOnMenuItemClickListener { item: MenuItem ->
            presenter.save()
            true
        }

    }

    private fun attachListeners()
    {
        nameEt.addTextChangedListener(this)
        primaryMailEt.addTextChangedListener(this)
        secondaryMailEt.addTextChangedListener(this)
        mobilEt.addTextChangedListener(this)
        newsletterSw.setOnCheckedChangeListener { compoundButton, b ->
            afterTextChanged(null)
        }

        verifyEmailBtn.setOnClickListener {
            getBaseActivity()?.openComponentDrawer(EmailVerificationComponentFragment::class.java)
        }

        verifySecondaryEmailBtn.setOnClickListener {
            getBaseActivity()?.openComponentDrawer(EmailVerificationComponentFragment::class.java)
        }

        verifyMobileNumberBtn.setOnClickListener {
            getBaseActivity()?.openComponentDrawer(PhoneVerificationComponentFragment::class.java)
        }

    }

    private fun detachListeners()
    {
        nameEt.removeTextChangedListener(this)
    }

    override fun onResume() {
        super.onResume()
        NStack.addLanguageChangeListener(this)
        attachListeners()
    }

    override fun onPause() {
        NStack.removeLanguageChangeListener(this)
        detachListeners()
        super.onPause()
    }

    override fun onLanguageChanged(locale: Locale) {
        mainTb.title = Translation.myInformation.title

        nameEt.setFloatingLabelText(Translation.myInformation.name)
        primaryMailEt.setHelperText(Translation.myInformation.notifyByEmailText)
        primaryMailEt.setFloatingLabelText(Translation.myInformation.primaryEmail)

        secondaryMailEt.setFloatingLabelText(Translation.myInformation.secondaryEmail)

        mobilEt.setHelperText(Translation.myInformation.notifyBySMSText)
        mobilEt.setFloatingLabelText(Translation.myInformation.mobileNumber)
        menuSave?.title = Translation.defaultSection.save
    }

    override fun setName(name: String) {
        nameEt.setText(name)
    }

    override fun setPrimaryEmail(email: String) {
        primaryMailEt.setText(email)
    }

    override fun setSecondaryEmail(email: String) {
        secondaryMailEt.setText(email)
    }

    override fun setMobileNumber(mobile: String) {
        mobilEt.setText(mobile)
    }

    override fun setNewsletter(b: Boolean) {
        newsletterSw.isChecked = b
    }

    override fun getName(): String {
        return nameEt.text.toString().trim()
    }

    override fun getPrimaryEmail(): String {
        return primaryMailEt.text.toString().trim()
    }

    override fun getSecondaryEmail(): String {
        return secondaryMailEt.text.toString().trim()
    }

    override fun getMobileNumber(): String {
        return mobilEt.text.toString().trim()
    }

    override fun getNewsletter(): Boolean {
        return newsletterSw.isChecked
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if(show) View.VISIBLE else View.GONE
    }

    override fun setSaveEnabled(enabled: Boolean) {
        menuSave?.isEnabled = enabled
    }

    override fun afterTextChanged(p0: Editable?) {
        menuSave?.isEnabled = true
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}