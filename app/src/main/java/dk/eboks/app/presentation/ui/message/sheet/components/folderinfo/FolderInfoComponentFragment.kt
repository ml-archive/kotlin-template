package dk.eboks.app.presentation.ui.message.sheet.components.folderinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.ui.message.sheet.components.SheetComponentFragment
import kotlinx.android.synthetic.main.fragment_folderinfo_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FolderInfoComponentFragment : SheetComponentFragment(), FolderInfoComponentContract.View {

    @Inject
    lateinit var presenter : FolderInfoComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_folderinfo_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun setupTranslations() {
        folderTv.text = Translation.message.folder
    }

    override fun updateView(folder: Folder) {
        folderNameTv.text = folder.name
    }
}