package dk.eboks.app.presentation.ui.components.uploads.myuploads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.shared.Status
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_upload_myuploadoverview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MyUploadsComponentFragment : BaseFragment(), MyUploadsComponentContract.View {

    @Inject
    lateinit var presenter: MyUploadsComponentContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    //mock stuff
    var numberOfMocks = 10
    var uploads: MutableList<dk.eboks.app.domain.models.message.Message> = ArrayList()
    var modeEdit: Boolean = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_upload_myuploadoverview, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupRows()
        setupTopBar()

    }

    private fun setupTopBar() {
        val menuProfile = getBaseActivity()?.mainTb?.menu?.add("_profile")
        menuProfile?.setIcon(R.drawable.icon_48_option_red_navigationbar)
        menuProfile?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuProfile?.setOnMenuItemClickListener { item: MenuItem ->
            switchMode()
            true }
    }

    private fun switchMode() {
        modeEdit = !modeEdit
        setupRows()
    }

    private fun setupRows() {
        //clear old rows
        rowsContainerLl.removeAllViews()

        //create mocks
        //todo Logic on how mocks are saved and generated might have to be changed when we get the api
        if(uploads.size != numberOfMocks) {
            uploads.clear()
            createmock(numberOfMocks)
        }
        for (i in 1..uploads.size) {
            var currentItem = uploads[i - 1]

            //todo change to recycler view
            //setting the header
            val v = inflator.inflate(R.layout.viewholder_upload_myuploads_row, rowsContainerLl, false)
            val headerTv = v.findViewById<TextView>(R.id.headerTv)
            val subHeaderTv = v.findViewById<TextView>(R.id.subHeaderTv)
            val dateTv = v.findViewById<TextView>(R.id.dateTv)
            val dividerV = v.findViewById<View>(R.id.dividerV)
            //todo change imageButton to checkbox
            val checkBox = v.findViewById<ImageButton>(R.id.checkboxIb)
            val uploadFl = v.findViewById<FrameLayout>(R.id.uploadFl)

            if(modeEdit){
                uploadFl.visibility = View.GONE
                checkBox.visibility = View.VISIBLE
            } else {
                uploadFl.visibility = View.VISIBLE
                checkBox.visibility = View.GONE
            }

            headerTv.text = currentItem.id
            subHeaderTv.text = currentItem.subject
            dateTv.text = formatter.formatDateRelative(currentItem)

            v.setOnClickListener {
                //todo for now it switches to uploading state for testing
                if(checkBox.visibility == View.VISIBLE){
                    checkBox.isSelected = !checkBox.isSelected
                }

                if (uploadFl.visibility == View.VISIBLE) {

                }
            }

            if (i == uploads.size) {
                dividerV.visibility = View.GONE
            }

            rowsContainerLl.addView(v)
            rowsContainerLl.requestLayout()
        }
    }

    private fun createmock(numberOfMocks: Int) {
        for (i in 1..numberOfMocks) {
            val random = Random()
            var unread = (random.nextInt(i) == 0)

            var randomStatus = Status(false, "important title", "important text", 0, Date())
            if (random.nextInt(i) == 0) {
                randomStatus.important = true
            }
            uploads.add(dk.eboks.app.domain.models.message.Message("id" + i, "subject" + i, Date(), unread, null, null, null, null, null, null, 0, null, null, null, null, randomStatus, "note string"))
        }
    }

}