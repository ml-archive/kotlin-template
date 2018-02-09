package dk.eboks.app.presentation.ui.message.sheet

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.ui.dialogs.ContextSheetActivity
import dk.eboks.app.presentation.ui.message.sheet.components.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.message.sheet.components.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.message.sheet.components.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.message.sheet.components.notes.NotesComponentFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MessageSheetActivity : ContextSheetActivity(), MessageSheetContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }
    @Inject
    lateinit var presenter: MessageSheetContract.Presenter

    var headerComponentFragment: HeaderComponentFragment? = null
    var notesComponentFragment: NotesComponentFragment? = null
    var attachmentsComponentFragment: AttachmentsComponentFragment? = null
    var folderInfoComponentFragment: FolderInfoComponentFragment? = null

    fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentSheet(R.layout.sheet_message)
        injectDependencies()
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
            component.inject(it)
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, headerComponentFragment, HeaderComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addNotesComponentFragment() {
        notesComponentFragment = NotesComponentFragment()
        notesComponentFragment?.let{
            component.inject(it)
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, notesComponentFragment, NotesComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addAttachmentsComponentFragment() {
        attachmentsComponentFragment = AttachmentsComponentFragment()
        attachmentsComponentFragment?.let{
            component.inject(it)
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, attachmentsComponentFragment, AttachmentsComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun addFolderInfoComponentFragment() {
        folderInfoComponentFragment = FolderInfoComponentFragment()
        folderInfoComponentFragment?.let{
            component.inject(it)
            supportFragmentManager.beginTransaction().add(R.id.sheetComponentsLl, folderInfoComponentFragment, FolderInfoComponentFragment::class.java.simpleName).commit()
        }
    }
}