package dk.eboks.app.presentation.ui.folder.components.newfolder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.mail.presentation.ui.folder.components.FolderDrawerMode
import dk.eboks.app.mail.presentation.ui.folder.components.NewFolderComponentContract
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.folder.components.FoldersComponentFragment
import dk.eboks.app.presentation.ui.folder.screens.FolderActivity
import kotlinx.android.synthetic.main.fragment_folder_newfolder.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class NewFolderComponentFragment : BaseFragment(), NewFolderComponentContract.View {

    @Inject
    lateinit var presenter: NewFolderComponentContract.Presenter
    var mode: FolderDrawerMode = FolderDrawerMode.NEW

    private var parentFolder: Folder? = null
    private var editFolder: Folder? = null
    private var rootFolderName: String? = null
    private var disableFolderSelection: Boolean = false
    override val overrideActiveUser: Boolean
        get() = arguments?.getBoolean("override_user") ?: false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_folder_newfolder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        arguments?.let { args ->
            if (args.containsKey("editFolder")) {
                mode = FolderDrawerMode.EDIT
                editFolder = args.getSerializable("editFolder") as? Folder
                parentFolder = editFolder?.parentFolder
            }
            if (args.containsKey("disableFolderSelection")) {
                disableFolderSelection = true
            }
        }
        setup()
    }

    private fun setup() {
        when (mode) {
            FolderDrawerMode.EDIT -> {
                titleTv.text = Translation.folders.editFolder
                folderRootTv.text = parentFolder?.name
                deleteIv.visibility = View.VISIBLE
                editFolder?.name?.let {
                    val editableString = SpannableStringBuilder(it)
                    nameEt.text = editableString
                }
                deleteIv.setOnClickListener {
                    presenter.deleteFolder(editFolder!!.id)
                }
            }

            FolderDrawerMode.NEW -> {
                deleteIv.visibility = View.GONE
            }
        }
        if (disableFolderSelection) {
            selectFolderLl.isEnabled = false
            folderRootTv.setTextColor(ContextCompat.getColor(context ?: return, R.color.blueGrey))
        }

//        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view?.windowToken, 0)

        setupButtons()
    }

    private fun setupButtons() {
        saveBtn.setOnClickListener {
            when (mode) {
                FolderDrawerMode.EDIT -> {
                    editFolder?.id?.let { folderId ->
                        presenter.editFolder(folderId, parentFolder?.id, nameEt.text.toString())
                    }
                }
                FolderDrawerMode.NEW -> {
                    createFolder()
                }
            }
        }

        cancelBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        selectFolderLl.setOnClickListener {
            val i = Intent(context, FolderActivity::class.java)
            i.putExtra("pick", true)
            i.putExtra("selectFolder", true)
            i.putExtra(FolderActivity.ARG_OVERIDE_USER, overrideActiveUser)
            startActivityForResult(i, FolderActivity.REQUEST_ID)
        }
    }

    private fun createFolder() {
        val folderName = nameEt.text.toString()
        // todo find out if the error handling for names should be done frontend. also the server will not accept special characters etc
//        if (folderName.isNotBlank()) {
        presenter.createNewFolder(parentFolder?.id ?: 0, folderName)
//        } else {
//            presenter.folderNameNotAllowed()
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                FolderActivity.REQUEST_ID -> {
                    when (mode) {
                        FolderDrawerMode.NEW -> {
                            parentFolder = data?.getSerializableExtra("res") as? Folder
                            if (parentFolder?.type == FolderType.INBOX) {
                                folderRootTv.text = rootFolderName
                            } else {
                                folderRootTv.text = parentFolder?.name
                            }
                        }
                        FolderDrawerMode.EDIT -> {
                            parentFolder = data?.getSerializableExtra("res") as? Folder
                            folderRootTv.text = parentFolder?.name
                        }
                    }
                }
            }
        }
    }

    override fun finish() {
        FoldersComponentFragment.refreshOnResume = true
        activity?.onBackPressed()
    }

    override fun setRootFolder(name: String) {
        // should only set root folder if there is no selected parrentfolder
        rootFolderName = name
        if (parentFolder == null) {
            folderRootTv.text = rootFolderName
        }
    }

    override fun showFolderNameError() {
        // todo
    }
}