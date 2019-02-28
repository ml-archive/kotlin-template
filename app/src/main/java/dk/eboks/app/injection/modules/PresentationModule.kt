package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.keychain.injection.KeychainPresentationModule
import dk.eboks.app.mail.injection.MailBindingPresenterModule
import dk.eboks.app.pasta.activity.PastaContract
import dk.eboks.app.pasta.activity.PastaPresenter
import dk.eboks.app.presentation.ui.channels.ChannelsBindingPresenterModule
import dk.eboks.app.presentation.ui.channels.screens.content.storebox.ConnectStoreboxContract
import dk.eboks.app.presentation.ui.channels.screens.content.storebox.ConnectStoreboxPresenter
import dk.eboks.app.presentation.ui.debug.components.DebugOptionsComponentContract
import dk.eboks.app.presentation.ui.debug.components.DebugOptionsComponentPresenter
import dk.eboks.app.presentation.ui.debug.components.DebugUsersComponentContract
import dk.eboks.app.presentation.ui.debug.components.DebugUsersComponentPresenter
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserContract
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserPresenter
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentContract
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentPresenter
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentContract
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentPresenter
import dk.eboks.app.presentation.ui.home.screens.HomeContract
import dk.eboks.app.presentation.ui.home.screens.HomePresenter
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningContract
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningPresenter
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentContract
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentPresenter
import dk.eboks.app.presentation.ui.overlay.screens.OverlayContract
import dk.eboks.app.presentation.ui.overlay.screens.OverlayPresenter
import dk.eboks.app.presentation.ui.senders.components.SenderGroupsComponentContract
import dk.eboks.app.presentation.ui.senders.components.SenderGroupsComponentPresenter
import dk.eboks.app.presentation.ui.senders.components.categories.CategoriesComponentContract
import dk.eboks.app.presentation.ui.senders.components.categories.CategoriesComponentPresenter
import dk.eboks.app.presentation.ui.senders.components.list.SenderAllListComponentContract
import dk.eboks.app.presentation.ui.senders.components.list.SenderAllListComponentPresenter
import dk.eboks.app.presentation.ui.senders.components.register.RegisterPresenter
import dk.eboks.app.presentation.ui.senders.components.register.RegistrationContract
import dk.eboks.app.presentation.ui.senders.screens.browse.BrowseCategoryContract
import dk.eboks.app.presentation.ui.senders.screens.browse.BrowseCategoryPresenter
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailContract
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailPresenter
import dk.eboks.app.presentation.ui.senders.screens.list.SenderAllListContract
import dk.eboks.app.presentation.ui.senders.screens.list.SenderAllListPresenter
import dk.eboks.app.presentation.ui.senders.screens.overview.SendersOverviewContract
import dk.eboks.app.presentation.ui.senders.screens.overview.SendersOverviewPresenter
import dk.eboks.app.presentation.ui.senders.screens.registrations.PendingContract
import dk.eboks.app.presentation.ui.senders.screens.registrations.PendingPresenter
import dk.eboks.app.presentation.ui.senders.screens.registrations.RegistrationsContract
import dk.eboks.app.presentation.ui.senders.screens.registrations.RegistrationsPresenter
import dk.eboks.app.presentation.ui.senders.screens.segment.SegmentDetailContract
import dk.eboks.app.presentation.ui.senders.screens.segment.SegmentDetailPresenter
import dk.eboks.app.presentation.ui.start.screens.StartContract
import dk.eboks.app.presentation.ui.start.screens.StartPresenter
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentContract
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentPresenter
import dk.eboks.app.presentation.ui.uploads.screens.UploadsContract
import dk.eboks.app.presentation.ui.uploads.screens.UploadsPresenter
import dk.eboks.app.presentation.ui.uploads.screens.fileupload.FileUploadContract
import dk.eboks.app.presentation.ui.uploads.screens.fileupload.FileUploadPresenter
import dk.eboks.app.profile.injection.ProfilePresentationModule
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Module(
    includes = [
        MailBindingPresenterModule::class,
        ChannelsBindingPresenterModule::class,
        KeychainPresentationModule::class,
        ProfilePresentationModule::class
    ]
)
abstract class PresentationModule {
    @ActivityScope
    @Binds
    abstract fun providePastaPresenter(presenter: PastaPresenter): PastaContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideFileUploadPresenter(presenter: FileUploadPresenter): FileUploadContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideMessageOpeningPresenter(presenter: MessageOpeningPresenter): MessageOpeningContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideNavBarComponentPresenter(presenter: NavBarComponentPresenter): NavBarComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideSendersOverviewPresenter(presenter: SendersOverviewPresenter): SendersOverviewContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideStartPresenter(presenter: StartPresenter): StartContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideCategoriesComponentPresenter(presenter: CategoriesComponentPresenter): CategoriesComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideBrowseCategoryPresenter(presenter: BrowseCategoryPresenter): BrowseCategoryContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideRegistrationsPresenter(presenter: RegistrationsPresenter): RegistrationsContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideSenderGroupsComponentPresenter(presenter: SenderGroupsComponentPresenter): SenderGroupsComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideUploadOverviewPresenter(presenter: UploadOverviewComponentPresenter): UploadOverviewComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideRegisterPresenter(presenter: RegisterPresenter): RegistrationContract.Presenter

    @ActivityScope
    @Binds
    abstract fun providePendingPresenter(presenter: PendingPresenter): PendingContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideSenderDetailPresenter(presenter: SenderDetailPresenter): SenderDetailContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideSegmentDetailPresenter(presenter: SegmentDetailPresenter): SegmentDetailContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideDebugOptionsComponentPresenter(presenter: DebugOptionsComponentPresenter): DebugOptionsComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideDebugUsersComponentPresenter(presenter: DebugUsersComponentPresenter): DebugUsersComponentContract.Presenter


    @ActivityScope
    @Binds
    abstract fun provideDebugUserPresenter(presenter: DebugUserPresenter): DebugUserContract.Presenter


    @ActivityScope
    @Binds
    abstract fun provideUploadsPresenter(presenter: UploadsPresenter): UploadsContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideOverlayPresenter(presenter: OverlayPresenter): OverlayContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideConnectStoreboxPresenter(presenter: ConnectStoreboxPresenter): ConnectStoreboxContract.Presenter



    @ActivityScope
    @Binds
    abstract fun provideSenderAllListPresenter(presenter: SenderAllListPresenter): SenderAllListContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideSenderAllListComponentPresenter(presenter: SenderAllListComponentPresenter): SenderAllListComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideFolderPreviewComponentPresenter(presenter: FolderPreviewComponentPresenter): FolderPreviewComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideChannelControlComponentPresenter(presenter: ChannelControlComponentPresenter): ChannelControlComponentContract.Presenter


    @ActivityScope
    @Binds
    abstract fun provideHomePresenter(presenter: HomePresenter): HomeContract.Presenter
    /* Pasta
    @ActivityScope
    @Binds
    abstract fun provideComponentPresenter(stateManager: AppStateManager) : ComponentContract.Presenter {
        return ComponentPresenter(stateManager)
    }
    */
}