package dk.eboks.app.injection.components

import dagger.Component
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.ui.components.folder.systemfolders.SystemFoldersComponentFragment
import dk.eboks.app.presentation.ui.components.folder.systemfolders.SystemFoldersComponentPresenter
import dk.eboks.app.presentation.ui.components.folder.userfolders.UserFoldersComponentFragment
import dk.eboks.app.presentation.ui.components.folder.userfolders.UserFoldersComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.foldershortcuts.FolderShortcutsComponentFragment
import dk.eboks.app.presentation.ui.components.mail.foldershortcuts.FolderShortcutsComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.sendercarousel.SenderCarouselComponentFragment
import dk.eboks.app.presentation.ui.components.mail.sendercarousel.SenderCarouselComponentPresenter
import dk.eboks.app.presentation.ui.debug.hinter.HintActivity
import dk.eboks.app.presentation.ui.mail.folder.FolderActivity
import dk.eboks.app.presentation.ui.mail.folder.FolderPresenter
import dk.eboks.app.presentation.ui.mail.list.MailListActivity
import dk.eboks.app.presentation.ui.mail.list.MailListPresenter
import dk.eboks.app.presentation.ui.mail.overview.MailOverviewActivity
import dk.eboks.app.presentation.ui.mail.overview.MailOverviewPresenter
import dk.eboks.app.presentation.ui.main.MainActivity
import dk.eboks.app.presentation.ui.main.MainPresenter
import dk.eboks.app.presentation.ui.message.MessageActivity
import dk.eboks.app.presentation.ui.message.MessagePresenter
import dk.eboks.app.presentation.ui.message.sheet.MessageSheetActivity
import dk.eboks.app.presentation.ui.message.sheet.MessageSheetPresenter
import dk.eboks.app.presentation.ui.components.message.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.components.message.attachments.AttachmentsComponentPresenter
import dk.eboks.app.presentation.ui.components.message.document.DocumentComponentFragment
import dk.eboks.app.presentation.ui.components.message.document.DocumentComponentPresenter
import dk.eboks.app.presentation.ui.components.message.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.components.message.folderinfo.FolderInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.message.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.components.message.header.HeaderComponentPresenter
import dk.eboks.app.presentation.ui.components.message.notes.NotesComponentFragment
import dk.eboks.app.presentation.ui.components.message.notes.NotesComponentPresenter
import dk.eboks.app.presentation.ui.components.message.pdfpreview.PdfPreviewComponentFragment
import dk.eboks.app.presentation.ui.components.message.pdfpreview.PdfPreviewComponentPresenter
import dk.eboks.app.presentation.ui.splash.SplashActivity
import dk.eboks.app.presentation.ui.splash.SplashPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

/**
 * Created by bison on 26-07-2017.
 */

@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(PresentationModule::class))
@ActivityScope
interface PresentationComponent {
    fun inject(target : MainActivity)
    fun inject(target : MainPresenter)
    fun inject(target : SplashActivity)
    fun inject(target : SplashPresenter)
    fun inject(target : MailOverviewActivity)
    fun inject(target : MailOverviewPresenter)
    fun inject(target : MailListActivity)
    fun inject(target : MailListPresenter)
    fun inject(target : FolderActivity)
    fun inject(target : FolderPresenter)
    fun inject(target : MessageActivity)
    fun inject(target : MessagePresenter)
    fun inject(target : MessageSheetActivity)
    fun inject(target : MessageSheetPresenter)

    // components
    fun inject(target : HeaderComponentFragment)
    fun inject(target : HeaderComponentPresenter)
    fun inject(target : NotesComponentFragment)
    fun inject(target : NotesComponentPresenter)
    fun inject(target : AttachmentsComponentFragment)
    fun inject(target : AttachmentsComponentPresenter)
    fun inject(target : FolderInfoComponentFragment)
    fun inject(target : FolderInfoComponentPresenter)
    fun inject(target : DocumentComponentFragment)
    fun inject(target : DocumentComponentPresenter)
    fun inject(target : PdfPreviewComponentFragment)
    fun inject(target : PdfPreviewComponentPresenter)
    fun inject(target : SystemFoldersComponentFragment)
    fun inject(target : SystemFoldersComponentPresenter)
    fun inject(target : UserFoldersComponentFragment)
    fun inject(target : UserFoldersComponentPresenter)
    fun inject(target : FolderShortcutsComponentFragment)
    fun inject(target : FolderShortcutsComponentPresenter)
    fun inject(target : SenderCarouselComponentFragment)
    fun inject(target : SenderCarouselComponentPresenter)




    fun inject(target : HintActivity)
}
