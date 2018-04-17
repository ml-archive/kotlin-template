package dk.eboks.app.presentation.ui.components.profile.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.profile.drawer.FingerPrintComponentFragment
import dk.eboks.app.presentation.ui.components.profile.myinfo.MyInfoComponentFragment
import dk.eboks.app.presentation.ui.components.start.signup.AcceptTermsComponentFragment
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.screens.profile.ProfileActivity
import kotlinx.android.synthetic.main.fragment_profile_main_component.*
import kotlinx.android.synthetic.main.include_profile_bottom.*
import timber.log.Timber
import javax.inject.Inject

class ProfileInfoComponentFragment : BaseFragment(),
                                     ProfileInfoComponentContract.View {
    @Inject
    lateinit var presenter: ProfileInfoComponentContract.Presenter

    var toolbarTitle = ""

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater?.inflate(R.layout.fragment_profile_main_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupVersionNumber()
        setupCollapsingToolbar()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadUserData()
    }

    private fun setupCollapsingToolbar() {
        profileDetailCTL.isTitleEnabled = false

        profileDetailABL.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange + verticalOffset < 200) {
                profileDetailTB.title = toolbarTitle
            } else {
                profileDetailTB.title = ""
            }
        }


        profileDetailTB.setNavigationOnClickListener {
            activity.finish()
        }
    }

    private fun setupListeners() {
        val profileActivity = if (activity is ProfileActivity) {
            (activity as ProfileActivity)
        } else {
            null
        }


        profileDetailContainerMyInformation.setOnClickListener {
            Timber.d("profileDetailContainerMyInformation Clicked")
            getBaseActivity()?.addFragmentOnTop(
                    R.id.profileActivityContainerFragment,
                    MyInfoComponentFragment()
            )
        }

        profileDetailSwFingerprint.setOnClickListener {


            Timber.d("Fingerprint: Toggled -> %s", profileDetailSwFingerprint.isChecked)
        }

        profileDetailSwKeepSignedIn.setOnClickListener {
            Timber.d("Signed In: Toggled -> %s", profileDetailSwKeepSignedIn.isChecked)
        }

        profileDetailContainerTerms.setOnClickListener {
            Timber.d("profileDetailContainerTerms Clicked")
            val frag = AcceptTermsComponentFragment()
            val args = Bundle()
            args.putBoolean("justShow", true)
            frag.arguments = args
            getBaseActivity()?.addFragmentOnTop(R.id.profileActivityContainerFragment, frag)
        }

        profileDetailContainerPrivacy.setOnClickListener {
            Timber.d("profileDetailContainerPrivacy Clicked")
            profileActivity?.showPrivacy()
        }

        profileDetailContainerHelp.setOnClickListener {
            Timber.d("profileDetailContainerHelp Clicked")
            profileActivity?.showHelp()
        }

        profileDetailBtnSignout.setOnClickListener {
            Timber.d("profileDetailBtnSignout Clicked")
            presenter.doLogout()
        }
    }

    override fun setName(name: String) {
        Timber.d("setName: %s", name)
        toolbarTitle = name
        profileDetailNameTv.text = name
        profileDetailTB.title = name
    }

    override fun setProfileImage(url: String?) {
        Timber.d("setProfileImage: %s", url)

        Glide.with(context)
                .load(url)
                .into(profileDetailIv)
    }

    override fun setVerified(verified: Boolean) {
        profileDetailRegisterTB.isChecked = verified
        if (verified) {
            profileDetailRegisterTB.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.icon_48_checkmark_white,
                    0
            )
        } else {
            profileDetailRegisterTB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
        profileDetailRegisterTB.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.icon_48_checkmark_white,
                        0
                )
                getBaseActivity()?.openComponentDrawer(VerificationComponentFragment::class.java)
            }
        }
    }

    override fun setFingerprintEnabled(enabled: Boolean, lastProviderId: String?) {
        profileDetailSwFingerprint.isChecked = enabled
        profileDetailSwFingerprint.setOnCheckedChangeListener { compoundButton, b ->
            Log.d("DEBUG", "setFingerprintEnabled $enabled")

            if (b) {
                getBaseActivity()?.openComponentDrawer(FingerPrintComponentFragment::class.java)
            } else {
                presenter.enableUserFingerprint(false)
            }
        }
    }

    override fun setKeepMeSignedIn(enabled: Boolean) {
        profileDetailSwKeepSignedIn.isChecked = enabled
        profileDetailSwKeepSignedIn.setOnCheckedChangeListener { compoundButton, b ->

        }
    }

    private fun setupVersionNumber() {
        profileDetailTvVersion.text = "${BuildConfig.VERSION_NAME} (build ${BuildConfig.VERSION_CODE})"
    }
}