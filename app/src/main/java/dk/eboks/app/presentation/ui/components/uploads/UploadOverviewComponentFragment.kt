package dk.eboks.app.presentation.ui.components.uploads

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.shared.Status
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.mail.maillist.MailListComponentFragment
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.fragment_upload_overview_component.*
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class UploadOverviewComponentFragment : BaseFragment(), UploadOverviewComponentContract.View {

    @Inject
    lateinit var presenter: UploadOverviewComponentContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    //mock data
    var redidValues = false
    var verifiedUser = true
    var showEkstraTopRow = true
    var numberOfRows = 2
    var uploads: MutableList<dk.eboks.app.domain.models.message.Message> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_upload_overview_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupView()
    }

    private fun setupView() {
        //create mocks
        createMockRows(numberOfRows)

        // show ekstra storage row
        if (showEkstraTopRow) {
            storageRowContainer.visibility = View.VISIBLE
        } else {
            storageRowContainer.visibility = View.GONE
        }

        // verified user
        if (!verifiedUser) {
            nonVerifiedUserContainerLl.visibility = View.VISIBLE
            emptyVerifiedUserTv.visibility = View.GONE
            contentRowHeaderTv.visibility = View.GONE
        } else {
            // user is verified
            nonVerifiedUserContainerLl.visibility = View.GONE
            contentVerifiedUSerLl.visibility = View.VISIBLE
            showAllBtn.setOnClickListener {
                var frag = MailListComponentFragment()
                frag?.putArg("folder", Folder(type = FolderType.UPLOADS, name = Translation.uploads.title))
                getBaseActivity()?.addFragmentOnTop(R.id.contentFl, frag, true)
            }
            fileBtn.setOnClickListener {
                //todo something when clicking file

            }
            photoBtn.setOnClickListener{
                //todo something when clicking photo
            }
            contentRowHeaderTv.visibility = View.VISIBLE

            if (uploads.size > 0) {
                emptyVerifiedUserTv.visibility = View.GONE
                contentVerifiedUSerLl.visibility = View.VISIBLE
                lastestUploadsFl.visibility = View.VISIBLE
                for (i in 1..uploads.size) {
                    var currentItem = uploads[i - 1]

                    //setting the header
                    val v = inflator.inflate(R.layout.viewholder_upload_row,contentVerifiedUSerLl , false)
                    val headerTv = v.findViewById<TextView>(R.id.headerTv)
                    val subHeaderTv = v.findViewById<TextView>(R.id.subHeaderTv)
                    val dateTv = v.findViewById<TextView>(R.id.dateTv)
                    val dividerV = v.findViewById<View>(R.id.dividerV)
                    val showContainerLl = v.findViewById<LinearLayout>(R.id.showContainerLl)
                    val uploadingContainerLl = v.findViewById<FrameLayout>(R.id.uploadingContainerLl)

                    headerTv.text = currentItem.id
                    subHeaderTv.text = currentItem.subject
                    dateTv.text = formatter.formatDateRelative(currentItem)

                    v.setOnClickListener {
                        //todo for now it switches to uploading state for testing
                        if(showContainerLl.visibility == View.VISIBLE){
                            uploadingContainerLl.visibility = View.VISIBLE
                            showContainerLl.visibility = View.GONE
                        } else {
                            uploadingContainerLl.visibility = View.GONE
                            showContainerLl.visibility = View.VISIBLE
                        }
                    }

                    if(i == uploads.size ){
                        dividerV.visibility = View.GONE
                    }

                    contentVerifiedUSerLl.addView(v)
                    contentVerifiedUSerLl.requestLayout()
                }
            } else {
                lastestUploadsFl.visibility = View.GONE
                emptyVerifiedUserTv.visibility = View.VISIBLE
                contentVerifiedUSerLl.visibility = View.GONE
            }
        }

    }



    private fun createMockRows(numberOfRows: Int) {
        for (i in 1..numberOfRows) {
            val random = Random()
            var unread = (random.nextInt(i) == 0)

            var randomStatus = Status(false, "important title", "important text", 0, Date())
            if (random.nextInt(i) == 0) {
                randomStatus.important = true
            }
            uploads.add(dk.eboks.app.domain.models.message.Message("id" + i, "subject" + i, Date(), unread, null, null, null, null, null, null, 0, null, null, null, null, randomStatus, "note string"))
        }

    }

    override fun onShake() {
        if (!redidValues) {
            redidValues = true
            val alert = AlertDialog.Builder(context)
            var layout = inflator.inflate(R.layout.debug_dialog, null, false)
            var numberOfRowsEt = layout.findViewById<EditText>(R.id.firstEt)
            var showEkstraTopRowEt = layout.findViewById<EditText>(R.id.middleEt)
            var verifiedEt = layout.findViewById<EditText>(R.id.lastEt)

            // Builder
            with(alert) {
                setTitle("Setup Data")
                setMessage("debug helper")

                // Add any  input field here
                numberOfRowsEt!!.hint = "numberOfRows"
                showEkstraTopRowEt!!.hint = "showEkstraRow: 1 = yes"
                verifiedEt!!.hint = "Verified:1 = true"

                setPositiveButton("OK") { dialog, whichButton ->
                    numberOfRows = Integer.parseInt(numberOfRowsEt.text.toString())
                    showEkstraTopRow = (Integer.parseInt(showEkstraTopRowEt.text.toString()) == 1)
                    verifiedUser = (Integer.parseInt(verifiedEt.text.toString()) == 1)
                    uploads.clear()
                    val verifiedRowsLl = view?.findViewById<LinearLayout>(R.id.contentVerifiedUSerLl)
                    verifiedRowsLl?.removeAllViews()
                    redidValues = false
                    setupView()
                    dialog.dismiss()

                }

                setNegativeButton("NO") { dialog, whichButton ->
                    redidValues = false
                    dialog.dismiss()
                }
            }

            // Dialog
            val dialog = alert.create()
            dialog.setView(layout)
            dialog.show()
        }
    }

}