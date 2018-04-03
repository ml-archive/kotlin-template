package dk.eboks.app.presentation.ui.components.profile.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.util.OnLanguageChangedListener
import kotlinx.android.synthetic.main.fragment_profile_myinformation_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import java.util.*
import javax.inject.Inject

class MyInfoComponentFragment : BaseFragment(), MyInfoComponentContract.View, OnLanguageChangedListener {
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
        menuSave?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menuSave?.setOnMenuItemClickListener { item: MenuItem ->
            presenter.save()
            true
        }

    }

    override fun onResume() {
        super.onResume()
        NStack.addLanguageChangeListener(this)
    }

    override fun onPause() {
        NStack.removeLanguageChangeListener(this)
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
}