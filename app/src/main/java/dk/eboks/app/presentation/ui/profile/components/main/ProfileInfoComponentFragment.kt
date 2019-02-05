package dk.eboks.app.presentation.ui.profile.components.main

import android.content.Context
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.overlay.screens.ButtonType
import dk.eboks.app.presentation.ui.overlay.screens.OverlayActivity
import dk.eboks.app.presentation.ui.overlay.screens.OverlayButton
import dk.eboks.app.presentation.ui.profile.components.HelpFragment
import dk.eboks.app.presentation.ui.profile.components.PrivacyFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.FingerHintComponentFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.FingerPrintComponentFragment
import dk.eboks.app.presentation.ui.profile.components.myinfo.MyInfoComponentFragment
import dk.eboks.app.presentation.ui.start.components.signup.AcceptTermsComponentFragment
import dk.eboks.app.presentation.ui.start.screens.StartActivity
import dk.eboks.app.util.dpToPx
import dk.eboks.app.util.visible
import dk.nodes.filepicker.FilePickerActivity
import dk.nodes.filepicker.FilePickerConstants
import dk.nodes.filepicker.uriHelper.FilePickerUriHelper
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_profile_main_component.*
import kotlinx.android.synthetic.main.include_profile_bottom.*
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ProfileInfoComponentFragment : BaseFragment(),
    ProfileInfoComponentContract.View {
    @Inject
    lateinit var presenter: ProfileInfoComponentContract.Presenter

    private var toolbarTitle = ""

    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 4532

    private var showProgressOnLoad = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_main_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgressOnLoad = true
        refreshOnResume = true
    }

    override fun onResume() {
        super.onResume()
        clearFindViewByIdCache()
        attachListeners()
        if (refreshOnResume) {
            refreshOnResume = false
            presenter.loadUserData(showProgressOnLoad)
            if (showProgressOnLoad)
                showProgressOnLoad = false
        }
    }

    override fun onPause() {
        detachListeners()
        super.onPause()
    }

    private fun setupCollapsingToolbar() {
        profileDetailCTL.isTitleEnabled = false

        profileDetailABL.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange + verticalOffset < 200) {
                profileDetailTB.title = toolbarTitle
            } else {
                profileDetailTB.title = ""
            }
        })

        profileDetailTB.setNavigationOnClickListener {
            activity?.finishAfterTransition()
        }

        profileDetailRegisterTB.textOn = Translation.senders.registered
        profileDetailRegisterTB.textOff = Translation.profile.verifyButton
    }

    override fun showFingerprintOptionIfSupported() {
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // Show our fingerprint stuff only if we are above API M
// Fingerprint API only available on from Android 6.0 (M)
            val fingerprintManager =
                context?.getSystemService(Context.FINGERPRINT_SERVICE) as? FingerprintManager
            if (fingerprintManager?.isHardwareDetected == true) {
                profileDetailSwFingerprint.visible = (true)
                // Device doesn't support fingerprint authentication
            } else fingerprintManager?.isHardwareDetected
        } else
            profileDetailSwFingerprint.visible = (false)
    }

    override fun attachListeners() {
        profileDetailRegisterTB.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                refreshOnResume = true
                handleRegisteredButtonDrawable(isChecked)
                VerificationComponentFragment.verificationSucceeded = false
                getBaseActivity()?.openComponentDrawer(VerificationComponentFragment::class.java)
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
            } else {
                if (isChecked) {
                    // show da Finger!
                    if (presenter.isUserVerified)
                        getBaseActivity()?.openComponentDrawer(FingerPrintComponentFragment::class.java)
                    else
                        getBaseActivity()?.openComponentDrawer(FingerHintComponentFragment::class.java)
                } else {
                    if (presenter.isUserVerified)
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
            getBaseActivity()?.addFragmentOnTop(
                R.id.profileActivityContainerFragment,
                PrivacyFragment()
            )
        }

        profileDetailContainerHelp.setOnClickListener {
            Timber.d("profileDetailContainerHelp Clicked")
            getBaseActivity()?.addFragmentOnTop(
                R.id.profileActivityContainerFragment,
                HelpFragment()
            )
        }

        profileDetailBtnSignout.setOnClickListener {
            Timber.d("profileDetailBtnSignout Clicked")
            presenter.doLogout()
        }

        profileDetailContainerFeedback.setOnClickListener {
            Config.getResourceLinkByType("feedback")?.let { link ->
                openUrlExternal(link.link.url)
            }
        }
    }

    private fun handleRegisteredButtonDrawable(isRegistered: Boolean) {
        if (isRegistered) {
            profileDetailRegisterTB.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.icon_48_checkmark_white,
                0
            )
        } else {
            profileDetailRegisterTB.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    override fun detachListeners() {
        profileDetailRegisterTB.setOnCheckedChangeListener(null)
        profileDetailContainerMyInformation.setOnClickListener(null)
        profileDetailSwFingerprint.setOnClickListener(null)
        profileDetailSwKeepSignedIn.setOnClickListener(null)
        profileDetailContainerTerms.setOnClickListener(null)
        profileDetailContainerHelp.setOnClickListener(null)
        profileDetailBtnSignout.setOnClickListener(null)
        profileDetailContainerFeedback.setOnClickListener(null)
    }

    private fun openUrlExternal(url: String) {
        val context = context ?: return
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

    private fun acquireUserImage() {
        startActivityForResult(
            OverlayActivity.createIntent(
                requireContext(),
                arrayListOf(OverlayButton(ButtonType.CAMERA), OverlayButton(ButtonType.GALLERY))
            ), OverlayActivity.REQUEST_ID
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            OverlayActivity.REQUEST_ID -> {
                val button = data?.getSerializableExtra("res") as? ButtonType
                when (button) {
                    ButtonType.GALLERY -> {
                        val intent = Intent(activity, FilePickerActivity::class.java)
                        intent.putExtra(FilePickerConstants.FILE, true)
                        intent.putExtra(FilePickerConstants.TYPE, FilePickerConstants.MIME_IMAGE)
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
                    }
                    ButtonType.CAMERA -> {
                        val intent = Intent(activity, FilePickerActivity::class.java)
                        intent.putExtra(FilePickerConstants.CAMERA, true)
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
                    }
                    else -> {
                        // Nothing to do
                    }
                }
            }
            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE -> {
                data?.let {
                    val file = FilePickerUriHelper.getFile(activity ?: return, data)
                    val uri = FilePickerUriHelper.getUri(data)
                    setProfileImageLocal(file)
                }
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
            val options = RequestOptions()
            options.error(R.drawable.ic_profile)
            options.placeholder(R.drawable.ic_profile)
            options.circleCrop()
            Glide.with(context ?: return)
                .load(url)
                .apply(options)
                .into(it)
        }
    }

    private fun setProfileImageLocal(imgfile: File) {
        Timber.d("setProfileImageLocal: $imgfile")
        profileDetailIv?.let {
            it.setPadding(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4))
            // todo should save image on server ?
            presenter.saveUserImg(Uri.fromFile(imgfile).toString())

            val options = RequestOptions()
            options.error(R.drawable.ic_profile)
            options.placeholder(R.drawable.ic_profile)
            options.circleCrop()
            Glide.with(context ?: return)
                .load(Uri.fromFile(imgfile))
                .apply(options)
                .into(it)
        }
    }

    override fun setVerified(isVerified: Boolean) {
        handleRegisteredButtonDrawable(isVerified)
        profileDetailRegisterTB?.let {
            it.isChecked = isVerified
            if (isVerified) {
                it.isEnabled = false
            }
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
        ActivityCompat.finishAffinity(activity ?: return)
        startActivity(intent)
    }

    override fun showProgress(show: Boolean) {
        Timber.e("showChannelProgress $show called in ProfileInfoComponentFragment")
        progressFl.visibility = if (show) View.VISIBLE else View.GONE
        profileFragmentRootContainer.visibility = if (!show) View.VISIBLE else View.GONE
    }

    companion object {
        var refreshOnResume = false
    }
}