package dk.eboks.app.presentation.ui.channels

import dagger.Binds
import dagger.Module
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.additem.EkeyAddItemComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.ekey.additem.EkeyAddItemComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.open.EkeyOpenItemComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.ekey.open.EkeyOpenItemComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.storebox.content.ChannelContentStoreboxComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.storebox.content.ChannelContentStoreboxComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.storebox.detail.ChannelContentStoreboxDetailComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.storebox.detail.ChannelContentStoreboxDetailComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.web.ChannelContentComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.web.ChannelContentComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.opening.ChannelOpeningComponentContract
import dk.eboks.app.presentation.ui.channels.components.opening.ChannelOpeningComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.overview.ChannelOverviewComponentContract
import dk.eboks.app.presentation.ui.channels.components.overview.ChannelOverviewComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.requirements.ChannelRequirementsComponentContract
import dk.eboks.app.presentation.ui.channels.components.requirements.ChannelRequirementsComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentContract
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.verification.ChannelVerificationComponentContract
import dk.eboks.app.presentation.ui.channels.components.verification.ChannelVerificationComponentPresenter
import dk.eboks.app.presentation.ui.channels.screens.content.ChannelContentContract
import dk.eboks.app.presentation.ui.channels.screens.content.ChannelContentPresenter
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentContract
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentPresenter
import dk.eboks.app.presentation.ui.channels.screens.content.storebox.ConnectStoreboxContract
import dk.eboks.app.presentation.ui.channels.screens.content.storebox.ConnectStoreboxPresenter
import dk.eboks.app.presentation.ui.channels.screens.overview.ChannelOverviewContract
import dk.eboks.app.presentation.ui.channels.screens.overview.ChannelOverviewPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Module
abstract class ChannelsBindingPresentationModule {
    @ActivityScope
    @Binds
    internal abstract fun provideChannelsPresenter(presenter: ChannelOverviewPresenter): ChannelOverviewContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideChannelListComponentPresenter(presenter: ChannelOverviewComponentPresenter): ChannelOverviewComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideChannelSettingsPopUpComponentPresenter(presenter: ChannelRequirementsComponentPresenter): ChannelRequirementsComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideChannelOpeningComponentPresenter(presenter: ChannelOpeningComponentPresenter): ChannelOpeningComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideChannelVerificationComponentPresenter(presenter: ChannelVerificationComponentPresenter): ChannelVerificationComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideChannelContentComponentPresenter(presenter: ChannelContentComponentPresenter): ChannelContentComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideChannelContentStoreboxComponentPresenter(presenter: ChannelContentStoreboxComponentPresenter): ChannelContentStoreboxComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideChannelContentStoreboxDetailComponentPresenter(presenter: ChannelContentStoreboxDetailComponentPresenter): ChannelContentStoreboxDetailComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideChannelSettingsComponentPresenter(presenter: ChannelSettingsComponentPresenter): ChannelSettingsComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideChannelContentPresenter(presenter: ChannelContentPresenter): ChannelContentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideEkeyContentPresenter(presenter: EkeyContentPresenter): EkeyContentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideEkeyComponentPresenter(presenter: EkeyComponentPresenter): EkeyComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideEkeyAddItemComponentPresenter(presenter: EkeyAddItemComponentPresenter): EkeyAddItemComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideEkeyDetailComponentPresenter(presenter: EkeyDetailComponentPresenter): EkeyDetailComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideEkeyOpenItemComponentPresenter(presenter: EkeyOpenItemComponentPresenter): EkeyOpenItemComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideEkeyPinComponentPresenter(presenter: EkeyPinComponentPresenter): EkeyPinComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun provideConnectStoreboxPresenter(presenter: ConnectStoreboxPresenter): ConnectStoreboxContract.Presenter
}