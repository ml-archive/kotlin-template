package dk.eboks.app.presentation.ui.uploads.components

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.StorageInfo
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentFragment
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.uploads.screens.fileupload.FileUploadActivity
import dk.eboks.app.util.Starter
import dk.eboks.app.util.putArg
import dk.eboks.app.util.setVisible
import dk.nodes.filepicker.FilePickerActivity
import dk.nodes.filepicker.FilePickerConstants
import dk.nodes.filepicker.uriHelper.FilePickerUriHelper
import kotlinx.android.synthetic.main.fragment_upload_overview_component.*
import java.util.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import kotlin.math.roundToInt

/**
 * Created by bison on 09-02-2018.
 */
class UploadOverviewComponentFragment : BaseFragment(), UploadOverviewComponentContract.View {

    @Inject
    lateinit var presenter: UploadOverviewComponentContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1682
    private val PICK_FILE_ACTIVITY_REQUEST_CODE = 4685

    var uploads: MutableList<Message> = ArrayList()
    var verified = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_upload_overview_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        presenter.setup()
        verifyProfileBtn.setOnClickListener {
            refreshOnResume = true
            getBaseActivity()?.openComponentDrawer(VerificationComponentFragment::class.java)
        }
        otherVerifyProfileBtn.setOnClickListener {
            refreshOnResume = true
            getBaseActivity()?.openComponentDrawer(VerificationComponentFragment::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        if(refreshOnResume)
        {
            refreshOnResume = false
            presenter.refresh()
        }
    }

    override fun setupView(verifiedUser : Boolean) {
        activity?.mainTb?.title = Translation.uploads.title
        storageRowContainer.setVisible(false)
        emptyVerifiedUserTv.setVisible(false)
        emptyNonVerifiedUserLl.setVisible(false)
        emptyVerifiedUserTv.setVisible(false)
        // verified user
        verified = verifiedUser
        if (verifiedUser) {
            // user is verified
            emptyNonVerifiedUserLl.visibility = View.GONE
            latestUploadsLl.visibility = View.VISIBLE
            latestUploadsFl.visibility = View.VISIBLE
        }

        showAllBtn.setOnClickListener {
            val frag = MailListComponentFragment()
            frag.putArg("folder", Folder(type = FolderType.UPLOADS, name = Translation.uploads.title))
            getBaseActivity()?.addFragmentOnTop(R.id.contentFl, frag, true)
        }
        fileBtn.setOnClickListener {
            findFile()
        }
        photoBtn.setOnClickListener{
            getPhoto()
        }
    }

    override fun showStorageInfo(storageInfo: StorageInfo) {
        if(storageInfo.used > 0) {
            leftProgressTv.text = "${formatter.formatSize(storageInfo.used)}"
            rightProgressTv.text = "${formatter.formatSize(storageInfo.total - storageInfo.used)} ${Translation.uploads.remainingText}"
        }
        else
        {
            if(!verified)
                leftProgressTv.text = "${Translation.uploads.notVerifiedInitialAvailableSpace}"
            else
                leftProgressTv.text = "${Translation.uploads.verifiedInitialAvailableSpace}"
        }
        storageProgressBar.visibility = View.INVISIBLE
        storagePb.progress = (storageInfo.used.toFloat() / storageInfo.total.toFloat() * 100f).roundToInt()
        storagePb.visibility = View.VISIBLE
    }

    override fun showLatestUploads(messages: List<Message>) {
        latestUploadsPb.visibility = View.GONE
        latestUploadsLl.removeAllViews()
        uploads.clear()
        uploads.addAll(messages)
        if (uploads.size > 0) {
            // if user is not verified but have uploads show the small verify teaser
            if(!verified)
            {
                storageRowContainer.setVisible(true)
            }
            latestUploadsLl.visibility = View.VISIBLE
            latestUploadsFl.visibility = View.VISIBLE
            latestUploadsTopRowFl.visibility = View.VISIBLE
            for (i in 1..uploads.size) {
                val currentItem = uploads[i - 1]

                //setting the header
                val v = inflator.inflate(R.layout.viewholder_upload_row,latestUploadsLl , false)
                val headerTv = v.findViewById<TextView>(R.id.headerTv)
                val subHeaderTv = v.findViewById<TextView>(R.id.subHeaderTv)
                val dateTv = v.findViewById<TextView>(R.id.dateTv)
                val dividerV = v.findViewById<View>(R.id.dividerV)
                val showContainerLl = v.findViewById<LinearLayout>(R.id.showContainerLl)
                val uploadingContainerLl = v.findViewById<FrameLayout>(R.id.uploadingContainerLl)
                showContainerLl.setVisible(true)
                uploadingContainerLl.setVisible(false)

                headerTv.text = currentItem.subject
                subHeaderTv.text = currentItem.sender?.name ?: ""
                dateTv.text = formatter.formatDateRelative(currentItem)

                v.setOnClickListener {
                    activity.Starter()
                            .activity(MessageOpeningActivity::class.java)
                            .putExtra(Message::class.java.simpleName, currentItem)
                            .start()
                }

                if(i == uploads.size ){
                    dividerV.visibility = View.GONE
                }

                latestUploadsLl.addView(v)
                latestUploadsLl.requestLayout()
            }
        } else {
            if(verified) {
                emptyVerifiedUserTv.setVisible(true)
            }
            else
            {
                emptyNonVerifiedUserLl.setVisible(true)
            }

            latestUploadsTopRowFl.setVisible(false)
            latestUploadsFl.setVisible(false)
            latestUploadsLl.setVisible(false)
        }
    }

    var uploadPctProgressTv : TextView? = null
    var uploadView : View? = null
    var circularProgress : CircularProgressBar? = null

    override fun showUploadProgress()
    {
        //setting the header
        val v = inflator.inflate(R.layout.viewholder_upload_row,latestUploadsLl, false)
        uploadView = v
        uploadPctProgressTv = v.findViewById<TextView>(R.id.uploadPctProgressTv)
        val showContainerLl = v.findViewById<LinearLayout>(R.id.showContainerLl)
        val uploadingContainerLl = v.findViewById<FrameLayout>(R.id.uploadingContainerLl)
        circularProgress = v.findViewById<CircularProgressBar>(R.id.circularProgressBar)
        circularProgress?.progress = 0.0f
        circularProgress?.visibility = View.VISIBLE
        uploadPctProgressTv?.text = "0%"
        uploadingContainerLl.visibility = View.VISIBLE
        showContainerLl.visibility = View.GONE

        fileBtn.isEnabled = false
        photoBtn.isEnabled = false

        /*
        headerTv.text = currentItem.id
        subHeaderTv.text = currentItem.subject
        dateTv.text = formatter.formatDateRelative(currentItem)
        */
        latestUploadsLl.addView(v, 0)
    }

    override fun updateUploadProgress(pct : Double)
    {
        uploadPctProgressTv?.let {
            val value = pct * 100.0
            it.text = "${value.roundToInt()}%"
        }
        circularProgress?.let {
            val value = pct * 100.0
            it.progress = value.toFloat()
        }
    }

    override fun hideUploadProgress()
    {
        circularProgress?.visibility = View.GONE
        uploadView?.let { latestUploadsLl.removeView(it) }
        fileBtn.isEnabled = true
        photoBtn.isEnabled = true
    }

    private fun findFile() {
        val intent = Intent(activity, FilePickerActivity::class.java)
        intent.putExtra(FilePickerConstants.FILE, true)
        intent.putExtra(FilePickerConstants.TYPE, "*/*")
        startActivityForResult(intent, PICK_FILE_ACTIVITY_REQUEST_CODE)
    }

    private fun getPhoto() {
        val intent = Intent(activity, FilePickerActivity::class.java)
        intent.putExtra(FilePickerConstants.CAMERA, true)
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            data?.let {
                uploadFile(it)
            }
        }
        if (requestCode == PICK_FILE_ACTIVITY_REQUEST_CODE) {
            data?.let {
                uploadFile(it)
            }
        }

        if(requestCode == FileUploadActivity.REQUEST_ID)
        {
            val filename = data?.getStringExtra("filename") ?: ""
            val destinationFolderId : Int = data?.getIntExtra("destinationFolderId", -1) ?: -1
            val uriString = data?.getStringExtra("uriString") ?: ""
            var mimetype = data?.getStringExtra("mimeType") ?: "application/octet-stream"
            if(filename.isNotEmpty() && uriString.isNotEmpty() && destinationFolderId != -1) {
                presenter.upload(destinationFolderId, filename, uriString, mimetype)
                Timber.e("Got filename = $filename and destiantionfolderID = $destinationFolderId back from upload window")
            }
            else
            {
                Timber.e("Invalid upload params")
            }
        }

    }

    private fun uploadFile(data : Intent) {
        //val uri = FilePickerUriHelper.getUri(data)
        Timber.e("About to upload file ${data.extras}")

        val mimetype = data.getStringExtra("mimeType")
        val i = Intent(context, FileUploadActivity::class.java)
        i.putExtra("uriString", FilePickerUriHelper.getUriString(data) )
        if(mimetype != null)
            i.putExtra("mimeType", mimetype)
        startActivityForResult(i, FileUploadActivity.REQUEST_ID)
    }

    companion object {
        var refreshOnResume = false
    }
}