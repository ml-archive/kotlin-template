package dk.eboks.app.presentation.ui.components.folder.folders.newfolder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.folder.folders.FoldersComponentFragment
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderActivity
import kotlinx.android.synthetic.main.fragment_folder_newfolder.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class NewFolderComponentFragment : BaseFragment(), NewFolderComponentContract.View {

    @Inject
    lateinit var presenter: NewFolderComponentContract.Presenter
    var mode: FolderDrawerMode = FolderDrawerMode.NEW

    var parentFolder: Folder? = null
    var editFolder: Folder? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_folder_newfolder, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        arguments?.let { args ->
            if (args.containsKey("editFolder")) {
                mode = FolderDrawerMode.EDIT
                editFolder = args.getSerializable("editFolder") as Folder
                parentFolder = editFolder?.parentFolder
            }
        }

        setup()
    }

    private fun setup() {
        if (mode == FolderDrawerMode.EDIT) {
            titleTv.text = Translation.folders.editFolder
            folderRootTv.text = parentFolder?.name

            deleteIv.setOnClickListener {
                //todo delete folder
            }
        }

        setupButtons()
    }

    private fun setupButtons() {
        saveBtn.setOnClickListener {
            when (mode) {
                FolderDrawerMode.EDIT -> {
                    //todo save btn  do something
                }
                FolderDrawerMode.NEW -> {
                    //todo save btn  do something
                }
            }
        }

        cancelBtn.setOnClickListener {
            activity.onBackPressed()
        }

        selectFolderLl.setOnClickListener {
            var i = Intent(context, FolderActivity::class.java)
            i.putExtra("pick", true)
            startActivityForResult(i, FolderActivity.REQUEST_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                FolderActivity.REQUEST_ID -> {
                    when (mode) {
                        FolderDrawerMode.NEW -> {
                            parentFolder = data?.getSerializableExtra("res") as Folder
                            folderRootTv.text = parentFolder?.name
                        }
                        FolderDrawerMode.EDIT ->{
                            // todo API move folder to new location
                            // should also return a new parent folder so the new parent location gets shown correct
                        }
                    }
                }
            }


        }
    }

    override fun setRootFolder(name: String) {
        //should only set root folder if there is no selected parrentfolder
        if (parentFolder == null) {
            folderRootTv.text = name
        }
    }
}