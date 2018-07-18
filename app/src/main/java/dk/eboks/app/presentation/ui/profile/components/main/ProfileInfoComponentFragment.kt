package dk.eboks.app.presentation.ui.profile.components.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.profile.components.HelpFragment
import dk.eboks.app.presentation.ui.profile.components.PrivacyFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.FingerHintComponentFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.FingerPrintComponentFragment
import dk.eboks.app.presentation.ui.profile.components.myinfo.MyInfoComponentFragment
import dk.eboks.app.presentation.ui.profile.screens.ProfileActivity
import dk.eboks.app.presentation.ui.start.components.signup.AcceptTermsComponentFragment
import dk.eboks.app.presentation.ui.start.screens.StartActivity
import dk.eboks.app.util.dpToPx
import dk.eboks.app.util.setVisible
import dk.nodes.filepicker.FilePickerActivity
import dk.nodes.filepicker.FilePickerConstants
import dk.nodes.filepicker.uriHelper.FilePickerUriHelper
import kotlinx.android.synthetic.main.fragment_profile_main_component.*
import kotlinx.android.synthetic.main.include_profile_bottom.*
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import android.content.Context.FINGERPRINT_SERVICE
import android.hardware.fingerprint.FingerprintManager



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

        if (BuildConfig.ENABLE_PROFILE_PICTURE) {
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

        profileDetailRegisterTB.textOn = Translation.senders.registered
        profileDetailRegisterTB.textOff = Translation.profile.verifyButton
    }

    override fun showFingerprintOptionIfSupported() {
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // Show our fingerprint stuff only if we are above API M
//Fingerprint API only available on from Android 6.0 (M)
            val fingerprintManager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            if (fingerprintManager.isHardwareDetected) {
                profileDetailSwFingerprint.setVisible(true)
                // Device doesn't support fingerprint authentication
            }
            else
                fingerprintManager.isHardwareDetected
        }
        else
            profileDetailSwFingerprint.setVisible(false)
    }

    override fun setupListeners(verified : Boolean) {
        val profileActivity = if (activity is ProfileActivity) {
            (activity as ProfileActivity)
        } else {
            null
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
            val isChecked = profileDetailSwFingerprint.isChecked
            Timber.d("Fingerprint: Toggled -> $isChecked")

            if (BuildConfig.ENABLE_FINGERPRINT_NONVERIFIED) {
                if (isChecked) {
                    // show da Finger!
                    getBaseActivity()?.openComponentDrawer(FingerPrintComponentFragment::class.java)
                } else {
                    presenter.enableUserFingerprint(false)
                }
            }
            else
            {
                if (isChecked) {
                    // show da Finger!
                    if(verified)
                        getBaseActivity()?.openComponentDrawer(FingerPrintComponentFragment::class.java)
                    else
                        getBaseActivity()?.openComponentDrawer(FingerHintComponentFragment::class.java)
                } else {
                    if(verified)
                        presenter.enableUserFingerprint(false)
                }
            }
        }

        profileDetailSwKeepSignedIn.setOnClickListener {
            Timber.d("Keep signed In: Toggled -> %s", profileDetailSwKeepSignedIn.isChecked)
            presenter.enableKeepMeSignedIn(profileDetailSwKeepSignedIn.isChecked)
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
            getBaseActivity()?.addFragmentOnTop(R.id.profileActivityContainerFragment, PrivacyFragment())
        }

        profileDetailContainerHelp.setOnClickListener {
            Timber.d("profileDetailContainerHelp Clicked")
            getBaseActivity()?.addFragmentOnTop(R.id.profileActivityContainerFragment, HelpFragment())
        }

        profileDetailBtnSignout.setOnClickListener {
            Timber.d("profileDetailBtnSignout Clicked")
            presenter.doLogout()
        }

        profileDetailContainerFeedback.setOnClickListener {
            Config.getResourceLinkByType("feedback")?.let { link->
                openUrlExternal(link.link.url)
            }
        }
    }

    private fun openUrlExternal(url : String)
    {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (intent.resolveActivity(context.packageManager) != null) {
            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun showFingerprintEnabled(enabled: Boolean, lastProviderId: String?) {
        Timber.d("showFingerprintEnabled $enabled")
        profileDetailSwFingerprint.isChecked = enabled
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
        profileDetailNameTv?.let {
            it.text = name
        }
        profileDetailTB?.let {
            it.title = name
        }
    }

    override fun setProfileImage(url: String?) {
        Timber.d("setProfileImage: $url")
        profileDetailIv?.let {
            if (!url.isNullOrEmpty()) {
                profileDetailIv.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4))
            }
            var options = RequestOptions()
            options.error(R.drawable.ic_profile)
            options.placeholder(R.drawable.ic_profile)
            options.circleCrop()
            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(it)
        }
    }

    private fun setProfileImageLocal(imgfile: File) {
        Timber.d("setProfileImageLocal: $imgfile")
        profileDetailIv?.let {
            it.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4))
            //todo should save image on server ?
            presenter.saveUserImg(Uri.fromFile(imgfile).toString())

            var options = RequestOptions()
            options.error(R.drawable.ic_profile)
            options.placeholder(R.drawable.ic_profile)
            options.circleCrop()
            Glide.with(context)
                    .load(Uri.fromFile(imgfile))
                    .apply(options)
                    .into(it)
        }
    }

    override fun setVerified(isVerified: Boolean) {
        profileDetailRegisterTB?.let {
            it.isChecked = isVerified
            if(isVerified)
                it.isEnabled = false
        }
    }

    override fun showKeepMeSignedIn(isEnabled: Boolean) {
        Timber.d("showKeepMeSignedIn: $isEnabled")
        profileDetailSwKeepSignedIn?.let {
            it.isChecked = isEnabled
//            it.setOnCheckedChangeListener { compoundButton, b ->
//
//            }
        }
    }

    private fun setupVersionNumber() {
        profileDetailTvVersion?.let {
            it.text = "${BuildConfig.VERSION_NAME} (build ${BuildConfig.VERSION_CODE})"
        }
    }

    override fun logout() {
        val intent = Intent(getBaseActivity(), StartActivity::class.java)
        startActivity(intent)
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if(show) View.VISIBLE else View.GONE
        profileFragmentRootContainer.visibility = if(!show) View.VISIBLE else View.GONE
    }
}