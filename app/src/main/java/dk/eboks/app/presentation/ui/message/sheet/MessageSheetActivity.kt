package dk.eboks.app.presentation.ui.message.sheet

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.presentation.base.ContextSheetActivity
import dk.eboks.app.presentation.ui.message.components.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.message.components.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.message.components.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.message.components.notes.NotesComponentFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MessageSheetActivity : ContextSheetActivity(), MessageSheetContract.View {

    @Inject
    lateinit var presenter: MessageSheetContract.Presenter

    var headerComponentFragment: HeaderComponentFragment? = null
    var notesComponentFragment: NotesComponentFragment? = null
    var attachmentsComponentFragment: AttachmentsComponentFragment? = null
    var folderInfoComponentFragment: FolderInfoComponentFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentSheet(R.layout.sheet_message)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun showError(msg: String) {
        Timber.e(msg)
    }

    override fun setupTranslations() {
        
    }

    override fun addHeaderComponentFragment()
    {
        headerComponentFragment = HeaderComponentFragment()
        headerComponentFragment?.let{
            it.arguments = Bundle()
            it.arguments.putBoolean("show_divider", true)
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, headerComponentFragment, HeaderComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addNotesComponentFragment() {
        notesComponentFragment = NotesComponentFragment()
        notesComponentFragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, notesComponentFragment, NotesComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addAttachmentsComponentFragment() {
        attachmentsComponentFragment = AttachmentsComponentFragment()
        attachmentsComponentFragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, attachmentsComponentFragment, AttachmentsComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addFolderInfoComponentFragment() {
        folderInfoComponentFragment = FolderInfoComponentFragment()
        folderInfoComponentFragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, folderInfoComponentFragment, FolderInfoComponentFragment::class.java.simpleName).commit()
        }
    }
}