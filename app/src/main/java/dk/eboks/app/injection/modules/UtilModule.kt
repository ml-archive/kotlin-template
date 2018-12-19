package dk.eboks.app.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.managers.EncryptionPreferenceManager
import dk.eboks.app.domain.managers.GuidManager
import dk.eboks.app.domain.managers.PermissionManager
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.system.managers.CryptoManagerImpl
import dk.eboks.app.system.managers.EboksFormatterImpl
import dk.eboks.app.system.managers.EncryptionPreferenceManagerImpl
import dk.eboks.app.system.managers.GuidManagerImpl
import dk.eboks.app.system.managers.permission.PermissionManagerImpl
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 15/12/17.
 */
@Module
class UtilModule {
    @Provides
    @AppScope
    fun provideEncryptionManager(context: Context): EncryptionPreferenceManager {
        return EncryptionPreferenceManagerImpl(context)
    }

    @Provides
    @AppScope
    fun provideGuidManager(): GuidManager {
        return GuidManagerImpl()
    }

    @Provides
    @AppScope
    fun provideEboksFormatter(context: Context): EboksFormatter {
        return EboksFormatterImpl(context)
    }

    @Provides
    @AppScope
    fun providePermissionManager(
            executor: Executor,
            context: Context,
            uiManager: UIManager
    ): PermissionManager {
        return PermissionManagerImpl(executor, context, uiManager)
    }


    @Provides
    @AppScope
    fun provideCryptoManager(context: Context, settingsRepository: SettingsRepository): CryptoManager {
        return CryptoManagerImpl(context, settingsRepository)
    }

    @Provides
    @AppScope
    fun provideTestUserLoginStates() : MutableList<LoginState>
    {
        val u : MutableList<LoginState> = ArrayList()
        u.add(LoginState(selectedUser= User(id = -1, name="Storebox (0909061345)", identity = "0909061345"),
                userName="0909061345", userPassWord="a12345", activationCode="f3M9KwDs"))

        u.add(LoginState(selectedUser= User(id = -2, name="Charlie Testburger (0703161319)", identity = "0703161319"),
                userName="0703161319", userPassWord="a12345", activationCode = "Lz70Kqt6"))

        u.add(LoginState(selectedUser= User(id = -3, name="Boxie Fairshare (0805730045)", identity = "0805730045"),
                userName="0805730045", userPassWord="a12345", activationCode=null))
        u.add(LoginState(selectedUser= User(id = -4, name="Signe Signfeature (090906-1349)", identity = "0909061349"),
                userName="0909061349", userPassWord="a12345", activationCode="c0XHs82M"))
        u.add(LoginState(selectedUser= User(id = -5, name="Payment (090906-1348)", identity = "0909061348"),
                userName="0909061348", userPassWord="a12345", activationCode="Do36SqNi"))
        u.add(LoginState(selectedUser= User(id = -6, name="Payment 2 (160773-1291)", identity = "1607731291"),
                userName="1607731291", userPassWord="a12345", activationCode="o9HMr6w5"))
        u.add(LoginState(selectedUser= User(id = -5, name="You got served (090906-1346)", identity = "0909061346"),
                userName="0909061346", userPassWord="a12345", activationCode="g8GYs92H"))
        u.add(LoginState(selectedUser= User(id = -7, name="Read receipts (2009160001)", identity = "2009160001"),
                userName="2009160001", userPassWord="a12345", activationCode="Qd63Jct0"))
        u.add(LoginState(selectedUser= User(id = -8, name="Laila Dollarmeyer (3010732572)", identity = "3010732572"),
                userName="3010732572", userPassWord="a12345", activationCode="n8HQr6e2"))
        u.add(LoginState(selectedUser= User(id = -9, name="Norway (08101500011 NO Snitflade)", identity = "08101500011"),
                userName="08101500011", userPassWord="a12345", activationCode="Rg7p4Y1G"))
        u.add(LoginState(selectedUser= User(id = -10, name="Sweden (201508101234 SE Snitflade)", identity = "201508101234"),
                userName="201508101234", userPassWord="a12345", activationCode="t6G3Cbg2"))
        u.add(LoginState(selectedUser= User(id = -11, name="Sec Level (SE) (197707096066)", identity = "197707096066"),
                userName="197707096066", userPassWord="a12345", activationCode="Rw3r0AZt"))
        u.add(LoginState(selectedUser= User(id = -12, name="Sec Level (NO) (15079411032)", identity = "15079411032"),
                userName="15079411032", userPassWord="a12345", activationCode="k8QGg7s6"))

        for(ls in u)
        {
            ls.userLoginProviderId = "cpr"
        }
        return u
    }
}