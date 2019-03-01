package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.pasta.activity.PastaContract
import dk.eboks.app.pasta.activity.PastaPresenter
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
import dk.eboks.app.presentation.ui.start.screens.StartContract
import dk.eboks.app.presentation.ui.start.screens.StartPresenter
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentContract
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentPresenter
import dk.eboks.app.presentation.ui.uploads.screens.UploadsContract
import dk.eboks.app.presentation.ui.uploads.screens.UploadsPresenter
import dk.eboks.app.presentation.ui.uploads.screens.fileupload.FileUploadContract
import dk.eboks.app.presentation.ui.uploads.screens.fileupload.FileUploadPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Module
abstract class PresentationModule {
    @ActivityScope
    @Binds
    abstract fun bindPastaPresenter(presenter: PastaPresenter): PastaContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindFileUploadPresenter(presenter: FileUploadPresenter): FileUploadContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindMessageOpeningPresenter(presenter: MessageOpeningPresenter): MessageOpeningContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindNavBarComponentPresenter(presenter: NavBarComponentPresenter): NavBarComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindStartPresenter(presenter: StartPresenter): StartContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindUploadOverviewPresenter(presenter: UploadOverviewComponentPresenter): UploadOverviewComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindDebugOptionsComponentPresenter(presenter: DebugOptionsComponentPresenter): DebugOptionsComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindDebugUsersComponentPresenter(presenter: DebugUsersComponentPresenter): DebugUsersComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindDebugUserPresenter(presenter: DebugUserPresenter): DebugUserContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindUploadsPresenter(presenter: UploadsPresenter): UploadsContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindOverlayPresenter(presenter: OverlayPresenter): OverlayContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindFolderPreviewComponentPresenter(presenter: FolderPreviewComponentPresenter): FolderPreviewComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindChannelControlComponentPresenter(presenter: ChannelControlComponentPresenter): ChannelControlComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindHomePresenter(presenter: HomePresenter): HomeContract.Presenter
    /* Pasta
    @ActivityScope
    @Binds
    abstract fun bindComponentPresenter(stateManager: AppStateManager) : ComponentContract.Presenter {
        return ComponentPresenter(stateManager)
    }
    */
}