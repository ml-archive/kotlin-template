package dk.eboks.app.keychain.injection

import dk.eboks.app.keychain.presentation.PopupLoginContract
import dk.eboks.app.keychain.presentation.PopupLoginPresenter
import dk.eboks.app.keychain.presentation.components.*
import dk.eboks.app.keychain.presentation.components.verification.VerificationComponentContract
import dk.eboks.app.keychain.presentation.components.verification.VerificationComponentPresenter
import dagger.Binds
import dagger.Module
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