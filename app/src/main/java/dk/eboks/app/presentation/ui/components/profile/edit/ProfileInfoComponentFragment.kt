package dk.eboks.app.presentation.ui.components.profile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.profile.ProfileActivity
import kotlinx.android.synthetic.main.fragment_profile_main_component.*
import kotlinx.android.synthetic.main.include_profile_bottom.*
import timber.log.Timber
import javax.inject.Inject

class ProfileInfoComponentFragment : BaseFragment(),
                                     ProfileInfoComponentContract.View {
    @Inject
    lateinit var presenter: ProfileInfoComponentContract.Presenter

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

    private fun setupCollapsingToolbar() {
        profileDetailCTL.isTitleEnabled = false

        profileDetailABL.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange + verticalOffset < 200) {
                profileDetailTB.title = "_profile name"
            } else {
                profileDetailTB.title = ""
            }
        }

        profileDetailRegisterTB.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.icon_48_checkmark_white,
                        0
                )
            } else {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
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
            profileActivity?.showMyInformationFragment()
        }

        profileDetailSwFingerprint.setOnClickListener {
            Timber.d("Fingerprint: Toggled -> %s", profileDetailSwFingerprint.isChecked)
        }

        profileDetailSwKeepSignedIn.setOnClickListener {
            Timber.d("Signed In: Toggled -> %s", profileDetailSwKeepSignedIn.isChecked)
        }

        profileDetailContainerTerms.setOnClickListener {
            Timber.d("profileDetailContainerTerms Clicked")
            profileActivity?.showTerms()
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


    override fun setName(name: String?) {
        Timber.d("setName: %s", name)
        profileDetailNameTv.text = name
        profileDetailTB.title = name
    }

    override fun setProfileImage(url: String?) {
        Timber.d("setProfileImage: %s", url)

        Glide.with(context)
                .load(url)
                .into(profileDetailIv)
    }

    private fun setupVersionNumber() {
        profileDetailTvVersion.text = "${BuildConfig.VERSION_NAME} (build ${BuildConfig.VERSION_CODE})"
    }
}