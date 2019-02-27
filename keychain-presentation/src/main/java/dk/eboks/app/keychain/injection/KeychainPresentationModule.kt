package dk.eboks.app.keychain.injection

import dagger.Binds
import dagger.Module
import dk.eboks.app.keychain.presentation.PopupLoginContract
import dk.eboks.app.keychain.presentation.PopupLoginPresenter
import dk.eboks.app.keychain.presentation.components.ActivationCodeComponentContract
import dk.eboks.app.keychain.presentation.components.ActivationCodeComponentPresenter
import dk.eboks.app.keychain.presentation.components.DeviceActivationComponentContract
import dk.eboks.app.keychain.presentation.components.DeviceActivationComponentPresenter
import dk.eboks.app.keychain.presentation.components.ForgotPasswordComponentContract
import dk.eboks.app.keychain.presentation.components.ForgotPasswordComponentPresenter
import dk.eboks.app.keychain.presentation.components.ForgotPasswordDoneComponentContract
import dk.eboks.app.keychain.presentation.components.ForgotPasswordDoneComponentPresenter
import dk.eboks.app.keychain.presentation.components.LoginComponentContract
import dk.eboks.app.keychain.presentation.components.LoginComponentPresenter
import dk.eboks.app.keychain.presentation.components.SignupComponentContract
import dk.eboks.app.keychain.presentation.components.SignupComponentPresenter
import dk.eboks.app.keychain.presentation.components.UserCarouselComponentContract
import dk.eboks.app.keychain.presentation.components.UserCarouselComponentPresenter
import dk.eboks.app.keychain.presentation.components.verification.VerificationComponentContract
import dk.eboks.app.keychain.presentation.components.verification.VerificationComponentPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Module
abstract class KeychainPresentationModule {

    @ActivityScope
    @Binds
    abstract fun provideForgotPasswordDoneComponentPresenter(presenter: ForgotPasswordDoneComponentPresenter): ForgotPasswordDoneComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideUserCarouselComponentPresenter(presenter: UserCarouselComponentPresenter): UserCarouselComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideLoginComponentPresenter(presenter: LoginComponentPresenter): LoginComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideForgotPasswordComponentPresenter(presenter: ForgotPasswordComponentPresenter): ForgotPasswordComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideActivationCodeComponentPresenter(presenter: ActivationCodeComponentPresenter): ActivationCodeComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideSignupComponentPresenter(presenter: SignupComponentPresenter): SignupComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideVerificationComponentPresenter(presenter: VerificationComponentPresenter): VerificationComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun providePopupLoginPresenter(presenter: PopupLoginPresenter): PopupLoginContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideDeviceActivationComponentPresenter(presenter: DeviceActivationComponentPresenter): DeviceActivationComponentContract.Presenter
}