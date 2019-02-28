package dk.eboks.app.profile.injection

import dagger.Binds
import dagger.Module
import dk.eboks.app.profile.presentation.ui.components.HelpContract
import dk.eboks.app.profile.presentation.ui.components.HelpPresenter
import dk.eboks.app.profile.presentation.ui.components.PrivacyContract
import dk.eboks.app.profile.presentation.ui.components.PrivacyPresenter
import dk.eboks.app.profile.presentation.ui.components.drawer.*
import dk.eboks.app.profile.presentation.ui.components.main.ProfileInfoComponentContract
import dk.eboks.app.profile.presentation.ui.components.main.ProfileInfoComponentPresenter
import dk.eboks.app.profile.presentation.ui.components.myinfo.MyInfoComponentContract
import dk.eboks.app.profile.presentation.ui.components.myinfo.MyInfoComponentPresenter
import dk.eboks.app.profile.presentation.ui.screens.MyInfoContract
import dk.eboks.app.profile.presentation.ui.screens.MyInfoPresenter
import dk.eboks.app.profile.presentation.ui.screens.ProfileContract
import dk.eboks.app.profile.presentation.ui.screens.ProfilePresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Module
abstract class ProfilePresentationModule {


    @ActivityScope
    @Binds
    abstract fun providePrivacyPresenter(presenter: PrivacyPresenter): PrivacyContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideHelpPresenter(presenter: HelpPresenter): HelpContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideProfilePresenter(presenter: ProfilePresenter): ProfileContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideProfileInfoComponentPresenter(presenter: ProfileInfoComponentPresenter): ProfileInfoComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideMyInfoComponentPresenter(presenter: MyInfoComponentPresenter): MyInfoComponentContract.Presenter


    @ActivityScope
    @Binds
    abstract fun provideEmailVerificationComponentPresenter(presenter: EmailVerificationComponentPresenter): EmailVerificationComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun providePhoneVerificationComponentPresenter(presenter: PhoneVerificationComponentPresenter): PhoneVerificationComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideFingerHintComponentPresenter(presenter: FingerHintComponentPresenter): FingerHintComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideFingerPrintComponentPresenter(presenter: FingerPrintComponentPresenter): FingerPrintComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideMergeAccountComponentPresenter(presenter: MergeAccountComponentPresenter): MergeAccountComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun provideMyInfoPresenter(presenter: MyInfoPresenter): MyInfoContract.Presenter

}