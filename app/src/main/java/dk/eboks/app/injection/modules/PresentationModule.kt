package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.*
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.presentation.ui.components.folder.systemfolders.SystemFoldersComponentContract
import dk.eboks.app.presentation.ui.components.folder.systemfolders.SystemFoldersComponentPresenter
import dk.eboks.app.presentation.ui.components.folder.userfolders.UserFoldersComponentContract
import dk.eboks.app.presentation.ui.components.folder.userfolders.UserFoldersComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.foldershortcuts.FolderShortcutsComponentContract
import dk.eboks.app.presentation.ui.components.mail.foldershortcuts.FolderShortcutsComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.maillist.MailListComponentContract
import dk.eboks.app.presentation.ui.components.mail.maillist.MailListComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.sendercarousel.SenderCarouselComponentContract
import dk.eboks.app.presentation.ui.components.mail.sendercarousel.SenderCarouselComponentPresenter
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderContract
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderPresenter
import dk.eboks.app.presentation.ui.screens.mail.list.MailListContract
import dk.eboks.app.presentation.ui.screens.mail.list.MailListPresenter
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewContract
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewPresenter
import dk.eboks.app.presentation.ui.screens.main.MainContract
import dk.eboks.app.presentation.ui.screens.main.MainPresenter
import dk.eboks.app.presentation.ui.screens.message.MessageContract
import dk.eboks.app.presentation.ui.screens.message.MessagePresenter
import dk.eboks.app.presentation.ui.screens.message.sheet.MessageSheetContract
import dk.eboks.app.presentation.ui.screens.message.sheet.MessageSheetPresenter
import dk.eboks.app.presentation.ui.components.message.attachments.AttachmentsComponentContract
import dk.eboks.app.presentation.ui.components.message.attachments.AttachmentsComponentPresenter
import dk.eboks.app.presentation.ui.components.message.document.DocumentComponentContract
import dk.eboks.app.presentation.ui.components.message.document.DocumentComponentPresenter
import dk.eboks.app.presentation.ui.components.message.folderinfo.FolderInfoComponentContract
import dk.eboks.app.presentation.ui.components.message.folderinfo.FolderInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.message.header.HeaderComponentContract
import dk.eboks.app.presentation.ui.components.message.header.HeaderComponentPresenter
import dk.eboks.app.presentation.ui.components.message.notes.NotesComponentContract
import dk.eboks.app.presentation.ui.components.message.notes.NotesComponentPresenter
import dk.eboks.app.presentation.ui.components.message.pdfpreview.PdfPreviewComponentContract
import dk.eboks.app.presentation.ui.components.message.pdfpreview.PdfPreviewComponentPresenter
import dk.eboks.app.presentation.ui.screens.splash.SplashContract
import dk.eboks.app.presentation.ui.screens.splash.SplashPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

/**
 * Created by bison on 07/12/17.
 */

@Module
class PresentationModule {
    @ActivityScope
    @Provides
    fun provideMainPresenter(getPostsInteractor: LoginInteractor) : MainContract.Presenter {
        return MainPresenter(getPostsInteractor)
    }

    @ActivityScope
    @Provides
    fun provideSplashPresenter(bootstrapInteractor: BootstrapInteractor, loginInteractor: LoginInteractor) : SplashContract.Presenter {
        return SplashPresenter(bootstrapInteractor, loginInteractor)
    }

    @ActivityScope
    @Provides
    fun provideMailOverviewPresenter(stateManager: AppStateManager) : MailOverviewContract.Presenter {
        return MailOverviewPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideMailListPresenter(appState: AppStateManager, getMessagesInteractor: GetMessagesInteractor) : MailListContract.Presenter {
        return MailListPresenter(appState, getMessagesInteractor)
    }

    @ActivityScope
    @Provides
    fun provideFolderPresenter(appState: AppStateManager, getFoldersInteractor: GetFoldersInteractor) : FolderContract.Presenter {
        return FolderPresenter(appState, getFoldersInteractor)
    }

    @ActivityScope
    @Provides
    fun provideMessagePresenter(appState: AppStateManager) : MessageContract.Presenter {
        return MessagePresenter(appState)
    }

    @ActivityScope
    @Provides
    fun provideMessageSheetPresenter(stateManager: AppStateManager) : MessageSheetContract.Presenter {
        return MessageSheetPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideHeaderComponentPresenter(stateManager: AppStateManager) : HeaderComponentContract.Presenter {
        return HeaderComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideNotesComponentPresenter(stateManager: AppStateManager) : NotesComponentContract.Presenter {
        return NotesComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideAttachmentsComponentPresenter(stateManager: AppStateManager) : AttachmentsComponentContract.Presenter {
        return AttachmentsComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideFolderInfoComponentPresenter(stateManager: AppStateManager) : FolderInfoComponentContract.Presenter {
        return FolderInfoComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideDocumentComponentPresenter(stateManager: AppStateManager) : DocumentComponentContract.Presenter {
        return DocumentComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun providePdfPreviewComponentPresenter(stateManager: AppStateManager) : PdfPreviewComponentContract.Presenter {
        return PdfPreviewComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideSystemFoldersComponentPresenter(stateManager: AppStateManager) : SystemFoldersComponentContract.Presenter {
        return SystemFoldersComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideUserFoldersComponentPresenter(stateManager: AppStateManager) : UserFoldersComponentContract.Presenter {
        return UserFoldersComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideFolderShortcutsComponentPresenter(stateManager: AppStateManager, getCategoriesInteractor: GetCategoriesInteractor) : FolderShortcutsComponentContract.Presenter {
        return FolderShortcutsComponentPresenter(stateManager, getCategoriesInteractor)
    }

    @ActivityScope
    @Provides
    fun provideSenderCarouselComponentPresenter(stateManager: AppStateManager, sendersInteractor: GetSendersInteractor) : SenderCarouselComponentContract.Presenter {
        return SenderCarouselComponentPresenter(stateManager, sendersInteractor)
    }

    @ActivityScope
    @Provides
    fun provideMailListComponentPresenter(stateManager: AppStateManager, getMessagesInteractor: GetMessagesInteractor) : MailListComponentContract.Presenter {
        return MailListComponentPresenter(stateManager, getMessagesInteractor)
    }
}