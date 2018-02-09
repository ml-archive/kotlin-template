package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.*
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.presentation.ui.mail.folder.FolderContract
import dk.eboks.app.presentation.ui.mail.folder.FolderPresenter
import dk.eboks.app.presentation.ui.mail.list.MailListContract
import dk.eboks.app.presentation.ui.mail.list.MailListPresenter
import dk.eboks.app.presentation.ui.mail.overview.MailOverviewContract
import dk.eboks.app.presentation.ui.mail.overview.MailOverviewPresenter
import dk.eboks.app.presentation.ui.main.MainContract
import dk.eboks.app.presentation.ui.main.MainPresenter
import dk.eboks.app.presentation.ui.message.MessageContract
import dk.eboks.app.presentation.ui.message.MessagePresenter
import dk.eboks.app.presentation.ui.message.sheet.MessageSheetContract
import dk.eboks.app.presentation.ui.message.sheet.MessageSheetPresenter
import dk.eboks.app.presentation.ui.splash.SplashContract
import dk.eboks.app.presentation.ui.splash.SplashPresenter
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
    fun provideMailOverviewPresenter(getSendersInteractor: GetSendersInteractor, getFoldersInteractor: GetCategoriesInteractor) : MailOverviewContract.Presenter {
        return MailOverviewPresenter(getSendersInteractor, getFoldersInteractor)
    }

    @ActivityScope
    @Provides
    fun provideMailListPresenter(appState: AppStateManager, getMessagesInteractor: GetMessagesInteractor) : MailListContract.Presenter {
        return MailListPresenter(appState, getMessagesInteractor)
    }

    @ActivityScope
    @Provides
    fun provideFolderPresenter(getFoldersInteractor: GetFoldersInteractor) : FolderContract.Presenter {
        return FolderPresenter(getFoldersInteractor)
    }

    @ActivityScope
    @Provides
    fun provideMessagePresenter(appState: AppStateManager) : MessageContract.Presenter {
        return MessagePresenter(appState)
    }

    @ActivityScope
    @Provides
    fun provideMessageSheetPresenter() : MessageSheetContract.Presenter {
        return MessageSheetPresenter()
    }
}