package dk.eboks.app.presentation.ui.components.profile.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.profile.drawer.FingerPrintComponentFragment
import dk.eboks.app.presentation.ui.components.profile.myinfo.MyInfoComponentFragment
import dk.eboks.app.presentation.ui.components.start.signup.AcceptTermsComponentFragment
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.screens.profile.ProfileActivity
import dk.eboks.app.util.setVisible
import dk.nodes.filepicker.FilePickerActivity
import dk.nodes.filepicker.FilePickerConstants
import dk.nodes.filepicker.uriHelper.FilePickerUriHelper
import kotlinx.android.synthetic.main.fragment_profile_main_component.*
import kotlinx.android.synthetic.main.include_profile_bottom.*
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ProfileInfoComponentFragment : BaseFragment(),
                                     ProfileInfoComponentContract.View {
    @Inject
    lateinit var presenter: ProfileInfoComponentContract.Presenter

    var toolbarTitle = ""

    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 4532

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

        if(BuildConfig.ENABLE_PROFILE_PICTURE) {
            profileDetailIv.setOnClickListener {
                acquireUserImage()
            }
        }
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

        // Show our fingerprint stuff only if we are above API M
        profileDetailFingerprintContainer.setVisible(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)

        profileDetailRegisterTB.textOn = Translation.senders.registered
        profileDetailRegisterTB.textOff = Translation.profile.verifyButton
        profileDetailRegisterTB.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.icon_48_checkmark_white,
                        0
                )
                getBaseActivity()?.openComponentDrawer(VerificationComponentFragment::class.java)
            } else {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
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

    private fun acquireUserImage()
    {
        val intent = Intent(activity, FilePickerActivity::class.java)
        intent.putExtra(FilePickerConstants.CAMERA, true)
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            data?.let {
                val file = FilePickerUriHelper.getFile(activity, data)
                val uri = FilePickerUriHelper.getUri(data)
                setProfileImageLocal(file)
            }
        }
    }

    override fun setName(name: String) {
        Timber.d("setName: %s", name)
        toolbarTitle = name
        profileDetailNameTv.text = name
        profileDetailTB.title = name
    }

    override fun setProfileImage(url: String?) {
        Timber.d("setProfileImage: $url")

        var options = RequestOptions()
        options.error(R.drawable.ic_profile)
        options.placeholder(R.drawable.ic_profile)
        options.transforms(CenterCrop(), RoundedCorners(30))
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(profileDetailIv)
    }

    private fun setProfileImageLocal(imgfile: File) {
        Timber.d("setProfileImageLocal: $imgfile")

        var options = RequestOptions()
        options.error(R.drawable.ic_profile)
        options.placeholder(R.drawable.ic_profile)
        options.transforms(CenterCrop(), RoundedCorners(30))
        Glide.with(context)
                .load(Uri.fromFile(imgfile))
                .apply(options)
                .into(profileDetailIv)
    }

    override fun setVerified(verified: Boolean) {
        profileDetailRegisterTB.isChecked = verified
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