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
abstract class KeychainBindingPresentationModule {

    @ActivityScope
    @Binds
    internal abstract fun bindForgotPasswordDoneComponentPresenter(presenter: ForgotPasswordDoneComponentPresenter): ForgotPasswordDoneComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindUserCarouselComponentPresenter(presenter: UserCarouselComponentPresenter): UserCarouselComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindLoginComponentPresenter(presenter: LoginComponentPresenter): LoginComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindForgotPasswordComponentPresenter(presenter: ForgotPasswordComponentPresenter): ForgotPasswordComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindActivationCodeComponentPresenter(presenter: ActivationCodeComponentPresenter): ActivationCodeComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindSignupComponentPresenter(presenter: SignupComponentPresenter): SignupComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindVerificationComponentPresenter(presenter: VerificationComponentPresenter): VerificationComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindPopupLoginPresenter(presenter: PopupLoginPresenter): PopupLoginContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindDeviceActivationComponentPresenter(presenter: DeviceActivationComponentPresenter): DeviceActivationComponentContract.Presenter
}