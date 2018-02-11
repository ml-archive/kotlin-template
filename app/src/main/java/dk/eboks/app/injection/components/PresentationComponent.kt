package dk.eboks.app.injection.components

import dagger.Component
import dk.eboks.app.injection.modules.PresentationModule
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
import dk.eboks.app.presentation.ui.message.sheet.components.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.message.sheet.components.attachments.AttachmentsComponentPresenter
import dk.eboks.app.presentation.ui.message.sheet.components.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.message.sheet.components.folderinfo.FolderInfoComponentPresenter
import dk.eboks.app.presentation.ui.message.sheet.components.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.message.sheet.components.header.HeaderComponentPresenter
import dk.eboks.app.presentation.ui.message.sheet.components.notes.NotesComponentFragment
import dk.eboks.app.presentation.ui.message.sheet.components.notes.NotesComponentPresenter
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
    fun inject(target : HeaderComponentFragment)
    fun inject(target : HeaderComponentPresenter)
    fun inject(target : NotesComponentFragment)
    fun inject(target : NotesComponentPresenter)
    fun inject(target : AttachmentsComponentFragment)
    fun inject(target : AttachmentsComponentPresenter)
    fun inject(target : FolderInfoComponentFragment)
    fun inject(target : FolderInfoComponentPresenter)
    fun inject(target : HintActivity)
}
