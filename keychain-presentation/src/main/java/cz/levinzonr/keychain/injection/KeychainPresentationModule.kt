package cz.levinzonr.keychain.injection

import cz.levinzonr.keychain.presentation.PopupLoginContract
import cz.levinzonr.keychain.presentation.PopupLoginPresenter
import cz.levinzonr.keychain.presentation.components.*
import cz.levinzonr.keychain.presentation.components.verification.VerificationComponentContract
import cz.levinzonr.keychain.presentation.components.verification.VerificationComponentPresenter
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